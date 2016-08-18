$(function(){
    
    var currentGid;
    var currentGName;
    var currentPid;
    var currentPName;
    var currendDiscardCid;
    var currentDiscardImage;
    var connection = null;
    
    var displayChat = function(msg){
        $("#chatarea").prepend(
            $("<div>").text(msg)
        );
    }
    

    
    // availabe games
    var p = $.getJSON("http://localhost:8080/UNOWebServer/api/game/gamelist");
    p.done(function(result){
        var currentGame={};
        
        for(var  i =0; i < result.length; i++){
            var game = result[i];
            
            currentGid = game.gid;
            currentGName = game.name;
    
            var gid = $("<li>").text(game.gid);
            var gname = $("<li>").text(game.name);
            var gstatus = $("<li>").text(game.status);
            
            var row = (game.name).toString()+" ("+(game.gid).toString()+")"+(game.status).toString();
            var lirow = $("<li>").text(row.toString())
                    .attr("gid", game.gid)
                    .attr("name", game.name);
                    
            $("#game").append(lirow);
            
        }
    });
    
    // choose game
    $("#game").on("singletap", "li", function(){
        
        var gname = $(this).attr("name");
        currentGName = gname;
        
        var gid = $(this).attr("gid");
        currentGid = gid.toString();
        
        console.info("> gid ="+gid+" gname="+gname);
        
        $("#gid").text(gid.toString());
        $("#gname").text(gname.toString()+"("+gid.toString()+")");

        $.UIGoToArticle("#playerJoin");
    });
    
    // player join
    $("#join").on("click", function(){
        
        //websocket open
        connection = new WebSocket("ws://localhost:8080/UNOWebServer/ws/"+currentGid);
        
        connection.onopen = function(){
            console.info(">>"+connection.toString());
            displayChat("websocket is connected");
            
            //send message
            var message ={
                gid: currentGid,
                pid: currentPid,
                pname: $("#pname").val()
            }
            connection.send(JSON.stringify(message)); 
        };
        
        connection.onclose = function(){
            displayChat("websocket is closed");
        }
        
        connection.onmessage = function(msg){
            console.info("incoming: ", msg.data);
            
            var newMessage = JSON.parse(msg.data);
            
            displayChat("Player Name: "+newMessage.pname);
            
//            var serverflag = false;
//            serverflag = newMessage.serverflag;
//                
//                if(serverflag){
//                    alert("player inside");
//                    $('#gameImg').append('<img src="images/' + newMessage.image + '.png" alt="'+newMessage.image+'" title="' + newMessage.cid + '"/>');
//
//                }
//                
        }
        
        
        
        
        //other
        console.info(">> game:",currentGid);
        $.post("http://localhost:8080/UNOWebServer/api/game/addplayer",{
            gid: currentGid,
            pname: $("#pname").val()
        }).done(function(result){
            console.info(">> result create game:", JSON.stringify(result));
            var json = $.parseJSON(result);
            $("#gname").append(json.gname);
            
            currentPid = json.pid;
            currentPName =$("#pname").val();
            $.UIGoToArticle("#playerwaitplayer");
        });                
    });
    
    //timer
    var timer = setInterval(myTimer, 3000);

    function myTimer() {
        var d = new Date();
        var status = "a";
        connection.onmessage = function(msg){
            console.info("incoming: ", msg.data);
            var newMessage = JSON.parse(msg.data);
            status = newMessage.status;   
            
            if(status == "start"){
                UNOStart();
            }
            
             var serverflag = false;
            serverflag = newMessage.serverflag;
                
                if(serverflag && newMessage.pid === currentPid){
                    $('#dealcard').append('<img src="images/' + newMessage.image + '.png" alt="'+newMessage.image+'" title="' + newMessage.cid + '" />');

            }
                
        }
        console.info("this is status : "+status+": new message:"+newMessage.status+": msg.data :"+msg.data);
        document.getElementById("demo").innerHTML = d.toLocaleTimeString()+": "+status;
    }
    // player wait and start
    //$("#start").on("click", function(){
    function UNOStart() {
        $.getJSON("http://localhost:8080/UNOWebServer/api/game/dealplayercard/"+currentGid+"/"+currentPid)
                .done(function(result){
            console.info(">> result create game:"+result);
            //var json = $.parseJSON(result);
            console.info(">> result create game:");
            for (var i=0; i<result.length; i++)
            {
                console.info(">> result create game:", result[i].image);
                $('#dealcard').append('<img src="images/'+result[i].image+'.png" alt="'+result[i].image+'" title="'+result[i].cid + '" class="absolute' + i + '" />');          		
            }
            
            $.UIGoToArticle("#playergamestart");
        });                
    }; //);
    
    // chose discard card
    $("#dealcard").on("singletap", "img", function(){
        var currendDiscardCid =$(this).attr("title");
        var currentDiscardImage = $(this).attr("alt");
        console.info("cid:"+currendDiscardCid+":img"+currentDiscardImage);
        //send message
            var message ={
                gid: currentGid,
                cid:currendDiscardCid,
                image: currentDiscardImage,
                flag: true
            }
            connection.send(JSON.stringify(message));

        $.post("http://localhost:8080/UNOWebServer/api/game/cardtodiscardpile",{
            gid: currentGid,
            pid: currentPid,
            cid: currendDiscardCid
        });

    });
    
});