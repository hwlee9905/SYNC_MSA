package com.simple.book.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.book.vo.GpsTransfer;
import com.simple.book.vo.WeatherVo;

@Service
public class WeatherService {
	private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

	public HashMap<String, String> getWeather(HashMap<String, Integer> body) {
		logger.info("[weather] 위경도: " + body);
		HashMap<String, String> weatherInfo = new HashMap<>();
		GpsTransfer gpsTransfer = new GpsTransfer(body.get("latitude"), body.get("longitude"));
		gpsTransfer.transfer(gpsTransfer, 0);
		int x = (int) gpsTransfer.getxLat();
		int y = (int) gpsTransfer.getyLon();
		weatherInfo = requestApi(x, y);

		if (weatherInfo != null) {
			WeatherVo weatherVo = new WeatherVo(weatherInfo.get("sky"));
			String sky = weatherVo.getMessage();
			weatherInfo.put("sky", sky);
		}
		return weatherInfo;
	}

	private HashMap<String, String> requestApi(int x, int y) {
		HashMap<String, String> weatherInfo = new HashMap<>();
		try {
			URL url = new URL(getUrl(x, y));
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");

			weatherInfo = responseApi(con);
		} catch (Exception e) {
			e.getMessage();
		}
		return weatherInfo;
	}

	private String getUrl(int x, int y) {
		LocalDateTime dateTime = LocalDateTime.now().minusMinutes(30);
		String host = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0";
		String serviceType = "/getUltraSrtFcst";
		String serviceKey = "?ServiceKey=%2BgvdZknwfI9SkVlsApRoooatYw8pRkMt6V2%2BHIyiRJjDj6qzgibs2HN3KDmlp9wJ2YYMlTN3qqBRpQIPqJMjiQ%3D%3D";
		String pageResult = "&numOfRows=60";
		String dataType = "&dataType=JSON";
		String baseDate = "&base_date=" + dateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String baseTime = "&base_time=" + dateTime.format(DateTimeFormatter.ofPattern("HHmm"));
		String nx = "&nx=" + x;
		String ny = "&ny=" + y;
		return host + serviceType + serviceKey + pageResult + dataType + baseDate + baseTime + nx + ny;
	}

	private HashMap<String, String> responseApi(HttpURLConnection con) {
		HashMap<String, String> weatherInfo = new HashMap<>();

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			JsonNode rootNode = objectMapper.readTree(getInputStream(con));

			String resultCode = rootNode.path("response").path("header").path("resultCode").asText();
			JsonNode item = rootNode.path("response").path("body").path("items").path("item");

			if (resultCode.equals("00")) {
				String t1h = "";
				String sky = "";
				String reh = "";
				for (JsonNode itemNode : item) {
					String baseTime = itemNode.path("baseTime").asText();
					String fcstTime = itemNode.path("fcstTime").asText();
					LocalTime time = LocalTime.parse(baseTime, DateTimeFormatter.ofPattern("HHmm")).plusMinutes(30);
					String formattedTime = time.format(DateTimeFormatter.ofPattern("HHmm"));
					if (formattedTime.equals(fcstTime)) {
						String category = itemNode.path("category").asText();
						if (category.equals("T1H")) {
							t1h = itemNode.path("fcstValue").asText();
						}

						if (category.equals("SKY")) {
							sky = itemNode.path("fcstValue").asText();
						}

						if (category.equals("REH")) {
							reh = itemNode.path("fcstValue").asText();
						}

					}
				}
				weatherInfo.put("t1h", t1h);
				weatherInfo.put("sky", sky);
				weatherInfo.put("reh", reh);
			} else {
				// error 처리
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return weatherInfo;
	}

	private InputStream getInputStream(HttpURLConnection con) {
		StringBuilder response = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		InputStream inputStream = new ByteArrayInputStream(response.toString().getBytes());
		return inputStream;
	}
}
