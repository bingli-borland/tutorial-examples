var ws = null;
var host = window.location.host;

var url = "ws://" + host + "/cust";

function setConnected(connected) {
	document.getElementById('connect').disabled = connected;
	document.getElementById('disconnect').disabled = !connected;
	document.getElementById('echo').disabled = !connected;
}

function connect() {
    var username=document.getElementById('username').value;
	ws = new WebSocket(url + "?token=" +username);
	ws.onopen = function() {
		setConnected(true);
		log('Info: Connection Established.');
	};
	
	ws.onmessage = function(event) {
		log(event.data);
	};
	
	ws.onclose = function(event) {
		setConnected(false);
		log('Info: Closing Connection.');
	};
}

function disconnect() {
	if (ws != null) {
		ws.close();
		ws = null;
	}
	setConnected(false);
}

function echo() {
	if (ws != null) {
		var message = document.getElementById('message').value;
		log('Sent to server :: ' + message);
		ws.send(message);
	} else {
		alert('connection not established, please connect.');
	}
}

function log(message) {
	var console = document.getElementById('logging');
	var p = document.createElement('p');
	p.appendChild(document.createTextNode(message));
	console.appendChild(p);
}