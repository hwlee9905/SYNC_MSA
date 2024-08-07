function showFriendBox() {
	const box = document.getElementById('friend_box');

	if (box.style.display === 'none') {
		box.style.display = 'block';

		fetch("/friend/list")
			.then((response) => response.json())
			.then((data) => checkFriend(data));
	} else {
		box.style.display = 'none';
	}
}

function checkFriend(data) {
	const list = document.getElementById('friend_list');
	if (data.result === 'no_friend') {
		list.textContent = '친구가 없습니다.';
	} else {
		list.textContent = data.result;
	}
}