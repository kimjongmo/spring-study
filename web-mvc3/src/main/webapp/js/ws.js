$(document).ready(function () {
    $('#sendBtn').click(function () {
        sendMessage();
    });
});

var wssocket;
function sendMessage() {
    wssocket = new WebSocket("ws://localhost:8080/mvc3/echo");
    wssocket.onmessage = onMessage;
    wssocket.onclose = onClose;
    wssocket.onopen = function () {
        wssocket.send($('#message').val());
    }
}

function onMessage(evt) {
    var data = evt.data;
    alert("서버에서 데이터 받음:"+data);
    wssocket.close();
}

function onClose(evt) {
    alert("연결 끊김");
}

