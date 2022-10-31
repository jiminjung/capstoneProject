const express = require('express');
const request = require('request');
const app = express();

const server = app.listen(9090, () => {
    console.log('Start Server : localhost:9090');
});

app.set('views', __dirname + '/views'); //dirname == 현재 디렉토리
app.set('view engine', 'ejs'); // 엔진을 ejs로 설정
app.engine('html', require('ejs').renderFile); // html 사용, ejs를 가져옴

app.get('/', getEqualId);

app.get('/deeplearning',getdeepLearning);

function getEqualId(req, res){
    let id = req.query.id;
    //console.log(req.connection.remoteAddress) 들어오는 ip 콘솔에 찍음
    const option = {
        url : "http://210.117.128.200:8080/SeniorValidate",
        qs:{
            id : id
        }
    }

    request(option, function(error, response, body) {
        if(error) {throw error}

        const checkAccess = body.trim()

        if(checkAccess === 'true'){
            res.redirect("http://211.117.125.107:34526/");
        }
        else{
            res.status(404).render('errorPage.html');
        }
    });
}

function getdeepLearning(req, res){
    let deeplearning = req.query.deeplearning;

    if(deeplearning === 'capstone2022'){
        res.redirect("http://211.117.125.107:34526/");
    }
}

app.all('*',function(req, res){
    res.status(404).render('errorPage.html');
});
