
var stompClient = null;
var sendUser = null;
var reciveUser = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/v2-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        //setConnected(true);
        console.log('Connected: ' + frame);
        var toRecivePath = "/topic/greetings/" + reciveUser;
        stompClient.subscribe(toRecivePath, function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
	var toSendPath = "/v2/chat/" + sendUser;
    stompClient.send(toSendPath, {}, JSON.stringify({'name': $("#name").val(),'message': $("#message").val()}));
    $("#message").val('');
}

function showGreeting(message) {
    //$("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    //$("form").on('submit', function (e) {
    //    e.preventDefault();
    //});
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendMessage(); });
});

setTimeout("connect()", 3000);