$(function(){

			$.ajax({
			    type: 'GET', 
			    url: 'http://localhost:8080/UNOWebServer/api/game/playerwaitplayer', 
			    crossDomain: true,
			    dataType: 'text',
			    success: function(data) {
                              
//
			        var json = $.parseJSON(data);
                                    $('#gname').append("Waiting For "+json.gname+" to start"); 
                                    $('#gid').val(json.gid);    
                                    $('#pid').val(json.pid); 
                                console.log(data);			        
			    }
			});
		});