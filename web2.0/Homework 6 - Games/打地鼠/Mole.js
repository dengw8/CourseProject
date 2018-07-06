window.onload = function() {
    ini();
    play();
}
function ini() { 
    var frag = document.createDocumentFragment();
    for(var i = 0; i < 6; i++) {
    	for(var j = 0; j < 10; j++) {
    		var mole = document.createElement("button");
            mole.setAttribute('class', 'mole');
            mole.setAttribute('className', 'mole');
            mole.setAttribute('value', '0');           
    		frag.appendChild(mole);
    	}
    	var newline = document.createElement("br");
    	frag.appendChild(newline);
    }
    document.getElementById('mole-wrapper').appendChild(frag);
}

function play() {
    var startstop = document.getElementById('start-stop');
    var mole = document.getElementsByClassName('mole');
    var score = document.getElementById('score');
    var state = document.getElementById('state');
    startOrStop();
    produceMole();
    for(var i = 0; i < 60; i++) {
        mole[i].onclick = function() {
            if (startstop.state == "start") {
                if (this.value == 1) {
                    produceMole();
                    score.isadd = true;
                } else {
                    score.isadd = false;
                }
                updateScore();
            }
        }
    }
}

function startOrStop() {
    var state = document.getElementById('state');
    var time = document.getElementById('time');
    var startstop = document.getElementById('start-stop');
    startstop.onclick = function() {
        if (state.value == "Game Over") {
            restart();
            state.value = "Playing";
            startstop.state = "start";
            time.settimeid = setInterval('updateTime()',1000);
        }
        else if (state.value == "Playing") {
            clearInterval(time.settimeid);
            state.value = "Stop";
            startstop.state = "stop";
        }else {
            time.settimeid = setInterval('updateTime()',1000);
            startstop.state = "start";
            state.value = "Playing";
        }
    }
}

function produceMole() {
    var mole = document.getElementsByClassName('mole');
    var rand = Math.round(Math.random()*59);
    for(var i = 0; i < 60; i++) {
        if (mole[i].value == 1) {
            mole[i].value = 0;
            mole[i].style.backgroundColor = '#DCDCDC';
            break;
        }
    }
    mole[rand].value = 1;
    mole[rand].style.backgroundColor = 'green';
}

function restart() {
    document.getElementById('time').innerHTML = 30;
    document.getElementById('score').innerHTML = 0;
}

function updateScore() {
    var score = document.getElementById('score');
    if (score.isadd == true)
        score.innerHTML++;
    else
        score.innerHTML--;
}
function updateTime() {
    var time = document.getElementById('time');
    if (time.innerHTML > 0) {
        time.innerHTML--;
    } else if (time.innerHTML == 0) {
        gameOver();
        clearInterval(time.settimeid);
    }
}

function gameOver() {
    document.getElementById('state').value = "Game Over";
    document.getElementById('start-stop').state = "stop";
    var score = document.getElementById('score');
    alert("Game Over.\nYour score is " + score.innerHTML);
}