<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<style type="text/css">
body {
    background-color: ivory;
}
canvas {
    border:1px solid red;
}

</style>
<script type="text/javascript">
//get reference to the canvas and its context
window.onload = function() {
var canvas = document.getElementById("mycanvas");
var ctx = canvas.getContext("2d");
ctx.drawImage(document.getElementById("scream"), 0,0);
ctx.font = "16px helvetica";
}
// variables

// some text objects defining text on the canvas
var texts = [];

// variables used to get mouse position on the canvas
var $canvas = $("#canvas");
var canvasOffset = $canvas.offset();
var offsetX = canvasOffset.left;
var offsetY = canvasOffset.top;
var scrollX = $canvas.scrollLeft();
var scrollY = $canvas.scrollTop();

// variables to save last mouse position
// used to see how far the user dragged the mouse
// and then move the text by that distance
var startX;
var startY;

// this var will hold the index of the selected text
var selectedText = -1;

$(function() {
	
// make the <li> draggable 
$("li").draggable({
    helper: 'clone'
});

// drop on canvas
$("#canvas").droppable({
    accept: "li",
    drop: function (event, ui) {
        ctx.fillText($(ui.draggable).clone().text(), ui.position.left - event.target.offsetLeft, ui.position.top - event.target.offsetTop);

        var text = $(ui.draggable).clone().text();
        var x = ui.position.left - event.target.offsetLeft;
        var y = ui.position.top - event.target.offsetTop;
        var width = ctx.measureText(text).width;
        var height = 16;

        // save this text info in an object in texts[]
        texts.push({
            text: text,
            x: x,
            y: y,
            width: width,
            height: height
        });

        // draw all texts to the canvas
        draw();

    }
    
});

});

// clear the canvas draw all texts
function draw() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    console.log(texts);
    for (var i = 0; i < texts.length; i++) {
        var text = texts[i];
        ctx.fillText(text.text, text.x, text.y);
    }
}

// test if x,y is inside the bounding box of texts[textIndex]
function textHittest(x, y, textIndex) {
    var text = texts[textIndex];
    return (x >= text.x && x <= text.x + text.width && y >= text.y - text.height && y <= text.y);
}

// handle mousedown events
// iterate through texts[] and see if the user
// mousedown'ed on one of them
// If yes, set the selectedText to the index of that text
function handleMouseDown(e) {
    e.preventDefault();
    startX = parseInt(e.clientX - offsetX);
    startY = parseInt(e.clientY - offsetY);
    // Put your mousedown stuff here
    for (var i = 0; i < texts.length; i++) {
        if (textHittest(startX, startY, i)) {
            selectedText = i;
        }
    }
}

// done dragging
function handleMouseUp(e) {
    e.preventDefault();
    selectedText = -1;
}

// also done dragging
function handleMouseOut(e) {
    e.preventDefault();
    selectedText = -1;
}

// handle mousemove events
// calc how far the mouse has been dragged since
// the last mousemove event and move the selected text
// by that distance
function handleMouseMove(e) {
    if (selectedText < 0) {
        return;
    }
    e.preventDefault();
    mouseX = parseInt(e.clientX - offsetX);
    mouseY = parseInt(e.clientY - offsetY);

    // Put your mousemove stuff here
    var dx = mouseX - startX;
    var dy = mouseY - startY;
    startX = mouseX;
    startY = mouseY;

    var text = texts[selectedText];
    text.x += dx;
    text.y += dy;
    draw();
}

// listen for mouse events
$("#mycanvas").mousedown(function (e) {
    handleMouseDown(e);
});
$("#mycanvas").mousemove(function (e) {
    handleMouseMove(e);
});
$("#mycanvas").mouseup(function (e) {
    handleMouseUp(e);
});
$("#mycanvas").mouseout(function (e) {
    handleMouseOut(e);
});

</script>
</head>
<body>
<h4>Drag text listitems from top onto the red canvas.<br>Then drag the text around the canvas.</h4>

<ul id="drag">
    <li class="new-item">Drag me down1</li>
    <li class="new-item">Drag me down2</li>
    <li class="new-item">Drag me down3</li>
</ul>
<img src="images/img/Hydrangeas.jpg"  id="scream" alt="The Scream" height="300" width="300"  ">
<canvas  id="mycanvas" width=300 height=300></canvas>
</body>
</html>