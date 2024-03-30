function pageSignup() {
	window.location.href = "/page/signup"
}

function pageLogin() {
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

function myPage() {
	window.location.href = "/myPage"
}

function getCurrentPageURL() {
	return window.location.pathname;
}

function setCurrentPageButton() {
	var currentPageURL = getCurrentPageURL();

	document.querySelectorAll('.nav-item').forEach(function(item) {
		var link = item.querySelector('a').getAttribute('href');
		if (link === currentPageURL) {
			item.classList.add('current-page');
		} else {
			item.classList.remove('current-page');
		}
	});
}

setCurrentPageButton();