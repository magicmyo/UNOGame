$(function(){

			$.ajax({
			    type: 'GET', 
			    url: 'http://localhost:8080/UNOWebServer/api/game/gamelist', 
			    crossDomain: true,
			    dataType: 'text',
			    success: function(data) {

			        var json = $.parseJSON(data);

			        for (var i=0; i<json.length; i++)
			        {					
						$('#gid').append('<option value="'+ json[i].gid  + '">'+ json[i].name +' ('+json[i].gid +')'+'</option>');
			        }
			    }

			});
		});