window.onload = function() {
	document.getElementById("submit").addEventListener("click", login);
}

function login() {
	let xhr = new XmlHttpRequest();
	let username = document.getElementById("username");
	let password = document.getElementById("password");
	xhr.open("POST", "http://localhost:8080/generate-token");
	xhr.send(JSON.stringify({username: username, password: password}));
}