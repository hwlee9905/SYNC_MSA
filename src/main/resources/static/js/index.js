window.onload = function() {
	getLocation();
};

function signup() {
	window.location.href = "/page/signup"
}

function login() {
	window.location.href = "/page/login"
}

function logout() {
	fetch("/logout", {
		method: "GET",
	})
		.then(function(body) {
			return body.text();
		})
		.then(function(data) {
			if (data === "true") {
				alert('로그아웃완료');
				window.location.href = "/";
			} else {
				alert('시스템에러');
				window.location.href = "/";
			}
		});
}

function getLocation() {
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(sendLocation);
	} else {
		alert('Geolocation is not supported by this browser.');
	}
}

function sendLocation(position) {
	var latitude = position.coords.latitude;
	var longitude = position.coords.longitude;

	fetch('/weather', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		body: JSON.stringify({
			'latitude': latitude,
			'longitude': longitude
		}),
	}).then(function(body) {
		return body.text();
	}).then(function(data) {
		let jsonData = JSON.parse(data);
		let t1hEl = document.getElementById('t1h');
		let skyEl = document.getElementById('sky');
		let rehEl = document.getElementById('reh');
		t1hEl.textContent = jsonData.t1h + "℃";
		skyEl.textContent = jsonData.sky;
		rehEl.textContent = jsonData.reh + "%";
	});
}