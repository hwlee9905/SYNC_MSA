function login() {
	let id = document.getElementById('id').value;
	let password = document.getElementById('password').value;

	fetch("/login", {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
		},
		body: JSON.stringify({
			"id": id,
			"password": password
		}),
	}).then(function(body) {
		return body.text();
	}).then(function(data) {
		if (data === "true") {
			alert('로그인성공');
			window.location.href = "/";
		} else
			alert('아이디 또는 비밀번호를 확인하세요');
	});
}