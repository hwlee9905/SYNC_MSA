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
