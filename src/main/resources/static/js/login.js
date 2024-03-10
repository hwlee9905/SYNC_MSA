function login() {
	let userId = document.getElementById('userId').value;
	let password = document.getElementById('password').value;
	
	let formInfo = {
	    'userId': userId,
	    'password': password
	};
	
	let formBody = [];
	for (let property in formInfo) {
	  let encodedKey = encodeURIComponent(property);
	  let encodedValue = encodeURIComponent(formInfo[property]);
	  formBody.push(encodedKey + "=" + encodedValue);
	}
	
	formBody = formBody.join("&");
	
	fetch("/login/proc", {
		method: "POST",
		headers: {
			"Content-Type": "application/x-www-form-urlencoded; charset=utf-8",
		},
		body: formBody
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