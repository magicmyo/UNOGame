$(function(){

			$.ajax({
			    type: 'GET', 
			    url: 'http://localhost:8080/UNOWebServer/api/game/discardcard', 
			    crossDomain: true,
			    dataType: 'text',
			    success: function(data) {
                              
//
			        var json = $.parseJSON(data);
                                    $('#game').append('<h1>'+json.gname+'</h1>');
                                    $('#game').append('<img src="images/'+json.image+'.png" title="'+json.cid+'"/>');
                                    $('#game').append('<img src="images/back.png" />');
                                    $('#gid').val(json.gid);                                                                   
                                    $('#cid').val(json.cid);                                                                   
                                console.log(data);			        
			    }
			});
		});