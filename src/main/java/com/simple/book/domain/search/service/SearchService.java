package com.simple.book.domain.search.service;

import org.springframework.stereotype.Service;

import ai.bareun.protos.AnalyzeSyntaxResponse;
import ai.bareun.tagger.LanguageServiceClient;

@Service
public class SearchService {
	private String API_KEY = "";
	
	public String test(String text) {
		LanguageServiceClient conn = new ai.bareun.tagger.LanguageServiceClient("localhost", API_KEY);
        AnalyzeSyntaxResponse response = conn.analyze_syntax(text);
        String str = conn.toJson();
        return str;
	}
}
