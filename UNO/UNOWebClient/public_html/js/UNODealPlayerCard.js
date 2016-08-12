$(function(){

			$.ajax({
			    type: 'GET', 
			    url: 'http://localhost:8080/UNOWebServer/api/game/dealplayercard', 
			    crossDomain: true,
			    dataType: 'text',
			    success: function(data) {

			        var json = $.parseJSON(data);

			        for (var i=0; i<json.length; i++)
			        {
			            $('#demo').append('<img src="images/'+json[i].image+'.png" title="'+json[i].cid+'"/>');
                                        		
			        }
			    }

			});
		});