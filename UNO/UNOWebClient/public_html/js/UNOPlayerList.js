$(function(){

			$.ajax({
			    type: 'GET', 
			    url: 'http://localhost:8080/UNOWebServer/api/game/playerlist', 
			    crossDomain: true,
			    dataType: 'text',
			    success: function(data) {

			        var json = $.parseJSON(data);

			        for (var i=0; i<json.length; i++)
			        {
			            $('#demo').append('<div class="pname">'+ json[i].pid + " : " + json[i].name +'</div>');
			        }
			    }

			});
		});