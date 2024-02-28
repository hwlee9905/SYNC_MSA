/*function search() {
	let category = document.getElementById("category").value;
	let keyword = document.getElementById("search_friend").value;
	
	fetch("/user/search", {
		method: "POST",
		headers: {
			'Content-Type': 'application/json',
		},
		body: JSON.stringify({
			'category': category,
			'keyword': keyword
		}),
	})
	.then(function(body) {
		return body.text();
	})
	.then(function(data) {
		console.log(data);
		let jsonData = JSON.parse(data);
		let result = document.getElementById("result_search_box");
		result.textContent = jsonData;
	});
}
*/