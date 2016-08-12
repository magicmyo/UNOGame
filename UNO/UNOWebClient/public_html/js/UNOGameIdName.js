$(function(){

			$.ajax({
			    type: 'GET', 
			    url: 'http://localhost:8080/UNOWebServer/api/game/tablewaitplayer', 
			    crossDomain: true,
			    dataType: 'text',
			    success: function(data) {
                              
//
			        var json = $.parseJSON(data);

                                    $('#gid').val(json.gid);
                                    $('#gida').append(json.gid);
                                    $('#gname').append(json.name);
                                                                    
                                console.log(data);			        
			    }
			});
		});