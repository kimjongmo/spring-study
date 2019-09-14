$(document).ready(function () {
   $('message').keypress(function (event) {
       var keycode = (event.key !== undefined ? event.key : event.which);
       console.log("keycode = "+keycode);
       if(keycode.eq("Enter")){
           send();
       }
       event.stopPropagation();
   });

   $('#sendBtn').click(function () {
       send();
   });

   $('#enterBtn').click(function () {
       connect();
   });

   $('#exitBtn').click(function () {
       disconnect();
   });
});

var wsocket;

function connect() {
    wsocket = new WebSocket("ws://localhost:8080/mvc3/chat");
    wsocket.onopen = onOpen;
    wsocket.onclose = onClose;
    wsocket.onmessage = onMessage;
}

function disconnect(){
    wsocket.close();
}

function onOpen(event) {
    appendMessage("연결되었습니다");
}

function onMessage(event) {
    var data = event.data;
    var dataArr = data.split(":");
    console.log(dataArr)
    if(dataArr[0] =='msg'){
        if(dataArr[1] == $("#nickname").val())
            appendMessage("나:"+dataArr[2]);
        else
            appendMessage(data.substring(4));
    }
}

function onClose(event) {
    appendMessage("연결을 끊었습니다.");
}

function send() {
    var nickname = $('#nickname').val();
    var msg = $('#message').val();
    wsocket.send("msg:"+nickname+":"+msg);
    $("#message").val("");
}

function appendMessage(msg) {
    $('#chatMessageArea').append(msg+"<br>");
    var chatAreaHeight = $("#chatArea").height();
    var maxScroll = $("#chatMessageArea").height() - chatAreaHeight;
    $("#chatMessageArea").scrollTop(maxScroll);
}