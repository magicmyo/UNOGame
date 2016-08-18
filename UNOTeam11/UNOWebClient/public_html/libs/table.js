$(function () {

    var currentGid;
    var currentGName;
    var currentPid;
    var connection = null;


    var displayChat = function (msg) {
        $("#chatarea").prepend(
                $("<div>").text(msg)
                );
    }

    // create game
    $("#create").on("click", function () {



        //other
        $.post("http://localhost:8080/UNOWebServer/api/game/creategame", {
            gname: $("#name").val()
        }).done(function (result) {
            //console.info(">> result create game:", JSON.stringify(result));


            var json = $.parseJSON(result);
            $("#gid").append(json.gid);
            $("#gname").append(json.gname);

            currentGid = json.gid;
            currentGName = json.gname;
            $.UIGoToArticle("#tablewaitplayer");


            //websocket
            connection = new WebSocket("ws://localhost:8080/UNOWebServer/ws/" + currentGid);

            connection.onopen = function () {

                displayChat("websocket is connected");
            };

            connection.onclose = function () {
                displayChat("websocket is closed");
            }

            connection.onmessage = function (msg) {
                console.info("incoming: ", msg.data);

                var flag = false;
                var newMessage = JSON.parse(msg.data);

                displayChat("Player Name: " + newMessage.pname);
                flag = newMessage.flag;

                if (flag) {
                    $('#discardcard').append('<img src="images/' + newMessage.image + '.png" alt="' + newMessage.image + '" title="' + newMessage.cid + '" class="peppable1" />');

                }

            }
        });

    });

    // start game
    $("#start").on("click", function () {


        $.getJSON("http://localhost:8080/UNOWebServer/api/game/tablegamestart/" + currentGid)
                .done(function (result) {
                    //var json = $.parseJSON(result);
                    console.info(">>>>> Start To Show Player List " + result.length)
                    for (var i = 0; i < result.length; i++)
                    {
                        $('#droppable').append('<div class="pname"><img src="images/player.png" alt="' + result[i].pid + '" />' + result[i].pid + " : " + result[i].name + '</div>');

                        //delete when ui finish
                        currentPid = result[i].pid;
                    }



                });

        $.getJSON("http://localhost:8080/UNOWebServer/api/game/discardcard/" + currentGid)
                .done(function (result) {
                    $('#discardcard').append('<img src="images/' + result.image + '.png" alt="' + result.image + '" title="' + result.cid + '"/>');
                });

        $.getJSON("http://localhost:8080/UNOWebServer/api/game/showdeckcard/" + currentGid)
                .done(function (result) {
                    for (var i = 0; i < result.length; i++)
                    {
                        $('#deckcard').append('<img src="images/back.png" alt="' + result[i].image + '" title="' + result[i].cid + '" class="peppable3" />');
                        $('#deckcard img').draggable();
                    }


                });

        var message = {
            gid: currentGid,
            status: "start"
        }
        connection.send(JSON.stringify(message));

        $.UIGoToArticle("#tablestartgame");
    });

    // deck card to player
    $("#deckcard").on("singletap", "img", function () {
        var currendDiscardCid = $(this).attr("title");
        var currentDiscardImage = $(this).attr("alt");
        console.info("cid:" + currendDiscardCid + ":img" + currentDiscardImage);
        
        $(this).addClass('top').removeClass('bottom');
            $(this).siblings().removeClass('top').addClass('bottom');
            
        //send message
        var message = {
            gid: currentGid,
            pid: currentPid,
            cid: currendDiscardCid,
            image: currentDiscardImage,
            serverflag: true
        }
        connection.send(JSON.stringify(message));

        console.info(currentGid + "/" + currentPid + "/" + currendDiscardCid);
        $.post("http://localhost:8080/UNOWebServer/api/game/tabledecktoplayer", {
            gid: currentGid,
            pid: currentPid,
            cid: currendDiscardCid
        });

    });
    
    $("#droppable").droppable({
            accept: ".peppable3",
            classes: {
                "ui-droppable-active": "ui-state-active",
                "ui-droppable-hover": "ui-state-hover"
            },
            drop: function (event, ui) {
                ui.helper.remove();
                deleteMe = true;
                ui.draggable.remove();
                
                //transfer
                var currendDiscardCid = $("#deckcard").find("img").attr("title");
                var currentDiscardImage = $("#deckcard").find("img").attr("alt");
                console.info("cid:" + currendDiscardCid + ":img" + currentDiscardImage);
        
                $(this).addClass('top').removeClass('bottom');
                $(this).siblings().removeClass('top').addClass('bottom');
            
        //send message
        var message = {
            gid: currentGid,
            pid: currentPid,
            cid: currendDiscardCid,
            image: currentDiscardImage,
            serverflag: true
        }
        connection.send(JSON.stringify(message));

        alert(message.toString());
        console.info(message.toString());
        console.info(currentGid + "/" + currentPid + "/" + currendDiscardCid);
        $.post("http://localhost:8080/UNOWebServer/api/game/tabledecktoplayer", {
            gid: currentGid,
            pid: currentPid,
            cid: currendDiscardCid
        });
            }
        });

});