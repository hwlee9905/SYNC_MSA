function signup() {
	let id = document.getElementById('id').value;
	let password = document.getElementById('password').value;
	let first_name = document.getElementById('first_name').value;
	let last_name = document.getElementById('last_name').value;
	let birth = document.getElementById('birth').value;
	let gender = document.querySelector('input[name="gender"]:checked').value;

	fetch("/user/signup", {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
		},
		body: JSON.stringify({
			"id": id,
			"password": password,
			"first_name": first_name,
			"last_name": last_name,
			"birth": birth,
			"gender": gender
		}),
	}).then(function(body) {
		return body.text();
	}).then(function(data) {
		if (data === 'success') {
			alert('가입성공');
			window.location.href = "/";
		} else
			alert('가입불가 사유: ' + data);
	});
}