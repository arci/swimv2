function addRequest(type, username) {
	"use strict";
	var xmlhttp = new XMLHttpRequest();
	var url = null;
	var messageSpan = null;
	var okMessage = null;
	var errorSpan = null;
	if (type === "help") {
		var helperAbilities = document.getElementById("helperAbilities");
		var ability = helperAbilities.options[helperAbilities.selectedIndex].value;
		url = "/SWIMweb/user/helps?newHelper=" + username + "&ability="
				+ ability;
		messageSpan = "helpMessage";
		errorSpan = "helpError";
		okMessage = 'Help request for \'' + ability + '\' added successfully';
	} else if (type === "friend") {
		url = "/SWIMweb/user/friends?newFriend=" + username;
		messageSpan = "friendMessage";
		errorSpan = "friendError";
		okMessage = 'Friendship request added successfully';
	}
	console.log("AJAX REQUEST TO:\n" + url + "\n");
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState === 4) {
			if (xmlhttp.status === 200) {
				console.log("RESPONSE TEXT:\n" + xmlhttp.responseText);
				var response = xmlhttp.responseXML
						.getElementsByTagName("result")[0].childNodes[0].nodeValue;
				if (response === "OK") {
					manageMessage('message', messageSpan, type, okMessage);
					manageButtonDiv(type);
				} else {
					var error = xmlhttp.responseXML
							.getElementsByTagName("error")[0].childNodes[0].nodeValue;
					manageMessage('error', messageSpan, type, error);
				}
			} else {
				manageMessage('error', errorSpan, type,
						'Problems during the request');
			}
		}
	};
	xmlhttp.open("GET", url, true);
	xmlhttp.send(null);
}

function manageButtonDiv(type) {
	"use strict";
	var button = document.getElementById(type);
	if (type !== "help") {
		button.parentNode.removeChild(button);
	}
}

function manageMessage(clazz, spanId, type, message) {
	"use strict";
	var div = null;
	if (type === "help") {
		div = document.getElementById('helpDiv');
	} else if (type === "friend") {
		div = document.getElementById('friendDiv');
	}
	if (document.getElementById(spanId)) {
		div.removeChild(document.getElementById(spanId));
	}
	var span = document.createElement("span");
	span.setAttribute("class", clazz);
	span.setAttribute("id", spanId);
	span.innerHTML = message;
	div.appendChild(span);
}
