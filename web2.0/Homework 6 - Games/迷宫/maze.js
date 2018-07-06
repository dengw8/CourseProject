var limit = false;
var complete = false;
var isout = false;

window.onload = function() {
    var bounds = document.querySelectorAll(".boundary");
    var begin = document.getElementById("start");
    var finish = document.getElementById("end");
    var out = document.getElementById("bottom");
    begin.onclick = restart;
    out.onmouseover = ischeat;
    finish.onmouseover = winner;
    for (var x = 0; x < bounds.length; x++) {
        bounds[x].onmouseover = lose;
    }
}

function lose(event) {
  var result = document.getElementById("result");
  if (!complete) {
    if(!limit)
    {
      limit = true;
      result.textContent = 'You Lose';
      event.target.className = "boundary youlose";
    }
  }
}

function winner() {
    var result = document.getElementById("result");
    if(!limit) {
      if(isout == false) {
        result.textContent = 'You Win'; 
      }  
      else {
        result.textContent = 'Do\'t cheat, you should start from the \'S\' and move to the \'E\' inside the maze!';
      }
      complete = true;
    }
}

function ischeat() {
  isout = true;
}

function restart(){
    var result = document.getElementById("result");
    var bounds = document.querySelectorAll(".boundary");
    limit = false;
    complete = false;
    isout = false;
    result.textContent = '';
    for (var x = 0; x < bounds.length; x++) {
      bounds[x].className = "boundary";
  }
}