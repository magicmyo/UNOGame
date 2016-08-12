/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myo.uno.rest;

import iss.nus.edu.myo.uno.Card;
import iss.nus.edu.myo.uno.Game;
import iss.nus.edu.myo.uno.Player;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import java.net.URI;
/**
 *
 * @author Myo Thu Htet
 */
@RequestScoped
@Path("/game")
public class UNOGame {
 
    public static Map<String, Game> hm = new HashMap<String, Game>();
    public static String strcurrentgid="";
    public static String strcurrentpid="";
    public static Player  currentplayer=null;
    //public static InetAddress localhost = InetAddress.getLocalHost();
    private final String localhostport = "http://localhost:8383";
    
    @POST
    @Path("/creategame")
    @Produces("text/plain")
    public Response addGame(@FormParam("gname") String name){
        
        String strgname = name;
        String strgid = UUID.randomUUID().toString().substring(0,8);
        strcurrentgid=strgid;
        Game g = new Game(strgid, strgname, "waiting");
        
        hm.put(strgid, g);
        
        try {
            URI location = new java.net.URI(localhostport+"/UNOWebClient/TableWaitPlayer.html");            
            return Response.seeOther(location).build();
        } catch (URISyntaxException ex) {
            return Response.status(404).entity("Redirect Fail").build();
        }                  
    }

    
    @GET
    @Path("/gamelist")
    @Produces("text/plain")
    public Response listGame(){
        JsonObject jso ;
        JsonArrayBuilder jsa = Json.createArrayBuilder();

        Iterator entries = hm.entrySet().iterator();
        while (entries.hasNext()) {
          Entry thisEntry = (Entry) entries.next();
          Object key = thisEntry.getKey();
          Object value = thisEntry.getValue();
          
           jso = Json.createObjectBuilder()
            .add("gid",thisEntry.getKey().toString())
            .add("name", ((Game)thisEntry.getValue()).getGname())
            .add("status", ((Game)thisEntry.getValue()).getStatus())
            .build();
           
           jsa.add(jso);
        }

        Response resp = Response.ok(jsa.build().toString())
                .header("Access-Control-Allow-Origin", "*")
                .build();
        return resp;
    }
    
    @GET
    @Path("/tablewaitplayer")
    @Produces("text/plain")
    public Response newGame(){
        JsonObject jso ;
        //JsonArrayBuilder jsa = Json.createArrayBuilder();

        Game g = hm.get(strcurrentgid);
        
        jso = Json.createObjectBuilder()
            .add("gid",g.getId())
            .add("name", g.getGname())
            .add("status", g.getStatus())
            .build();

        Response resp = Response.ok(jso.toString())
                .header("Access-Control-Allow-Origin", "*")
                .build();
        return resp;
    }
    
    @POST
    @Path("/addplayer")
    @Produces("text/plain")
    public Response addPlayer(@FormParam("gid") String gid, @FormParam("pname") String pname){
        
        strcurrentgid = gid;
        String strpname = pname;
        String strpid = UUID.randomUUID().toString().substring(0,8);
        
        Player p = new Player(strpid, strpname);
        strcurrentpid = strpid;
        
        hm.get(strcurrentgid).addPlayer(p);      

        try {
            URI location = new java.net.URI(localhostport+"/UNOWebClient/PlayerWaitPlayer.html");            
            return Response.seeOther(location).build();
        } catch (URISyntaxException ex) {
            return Response.status(404).entity("Redirect Fail").build();
        }  
    }
    
    @GET
    @Path("/playerlist")
    @Produces("text/plain")
    public Response listPlayer(){
        JsonObject jso ;
        JsonArrayBuilder jsa = Json.createArrayBuilder();
        
        Game g = hm.get(strcurrentgid); 
                
        ArrayList<Player> pList = g.getPlayList();
        
        for (int i = 0; i < pList.size(); i++) {
            Player player = pList.get(i);
            
            jso = Json.createObjectBuilder()
            .add("pid",player.getId())
            .add("name", player.getName())
            .add("status",g.getStatus())
            .build();
           
           jsa.add(jso);
            
        }

        Response resp = Response.ok(jsa.build().toString())
                .header("Access-Control-Allow-Origin", "*")
                .build();
        return resp;
    }
    
    @GET
    @Path("/playerwaitplayer")
    @Produces("text/plain")
    public Response showTablePlayer(){
        JsonObject jso ;
    
        Game g = hm.get(strcurrentgid);
        
        jso = Json.createObjectBuilder()
            .add("gid",g.getId())
            .add("gname",g.getGname())
            .add("pid",strcurrentpid)
            .build();

        Response resp = Response.ok(jso.toString())
                .header("Access-Control-Allow-Origin", "*")
                .build();
        return resp;
    }
    
    @POST
    @Path("/tablegamestart")
    @Produces("text/plain")
    public Response startTablePlay(@FormParam("gid") String gid){
        
        strcurrentgid = gid;
        
        
        Game g = hm.get(strcurrentgid); 
        int playersize = g.getPlayList().size();
        
        if(playersize >= 2 && playersize <= 5)
        {
            g.startPlay();
        
            try {
                URI location = new java.net.URI(localhostport+"/UNOWebClient/TableGameStart.html");            
                return Response.seeOther(location).build();
            } catch (URISyntaxException ex) {
                return Response.status(404).entity("Redirect Fail").build();
            }
        }
        else{
            return Response.status(404).entity("This game can't play!At least 2 to 5 players needed. Now is "+playersize+" players").build();
        }      
    }
    
    @GET
    @Path("/discardcard")
    @Produces("text/plain")
    public Response showTableCards(){
        JsonObject jso ;
        
        Game g = hm.get(strcurrentgid);
        
        Card card = g.getDiscardPile();
        
        jso = Json.createObjectBuilder()
            .add("gid",g.getId())
            .add("gname",g.getGname())
            .add("cid",card.getCid())            
            .add("color",card.getColor())
            .add("type", card.getType())
            .add("value",card.getValue())
            .add("image",card.getImage())
            .build();

        Response resp = Response.ok(jso.toString())
                .header("Access-Control-Allow-Origin", "*")
                .build();
        return resp;
    }
    
    @POST
    @Path("/playergamestart")
    @Produces("text/plain")
    public Response startPlayerPlay(@FormParam("gid") String gid, @FormParam("pid") String pid){
        
        strcurrentgid = gid;
        strcurrentpid = pid;

        try {
            URI location = new java.net.URI(localhostport+"/UNOWebClient/PlayerGameStart.html");            
            return Response.seeOther(location).build();
        } catch (URISyntaxException ex) {
            return Response.status(404).entity("Redirect Fail").build();
        }
    }
    
    
    @GET
    @Path("/dealplayercard")
    @Produces("text/plain")
    public Response showPlayerCards(){
        JsonObject jso ;
        JsonArrayBuilder jsa = Json.createArrayBuilder();
        Player player=null;
        Game g = hm.get(strcurrentgid);
        
        ArrayList<Player> pList = g.getPlayList();
        for (int i = 0; i < pList.size(); i++) {
            
            player = pList.get(i);
            if(player.getId().equals(strcurrentpid)){
                break;
            }
            else{
                player =null;
            }
            
        }
        
        ArrayList<Card> cardinhand = player.getCardInHand();
        
        for (int i = 0; i < cardinhand.size(); i++) {
            Card card = cardinhand.get(i);
            
            jso = Json.createObjectBuilder()
            .add("cid",card.getCid()) 
            .add("color",card.getColor())
            .add("type", card.getType())
            .add("value",card.getValue())
            .add("image",card.getImage())
            .build();

           jsa.add(jso);
            
        }

        Response resp = Response.ok(jsa.build().toString())
                .header("Access-Control-Allow-Origin", "*")
                .build();
        
        return resp;
    }
    
    
//    ArrayList<Player> pList = g.getPlayList();
//    for (int i = 0; i < pList.size(); i++) {
//            
//            player = pList.get(i);
//            if(player.getId().equals(pid)){
//                break;
//            }
//            else{
//                player =null;
//            }
//            
//        }
//    
//    work
//    @POST
//    @Produces("text/plain")
//    public Response hello(@FormParam("gname") String name){
//        
//        //Game g = new Game(strgid, strgname, "waiting");
//        
//        JsonObject jso = Json.createObjectBuilder()
//                .add("gid", 1234)
//                .add("name",name)
//                .build();
//        
//        //System.err.println(cgs.hm);
//        System.err.println(jso.toString());
//        
//        Response resp = Response.ok("Hello")
//                .header("x-date", (new Date()).toString())
//                .build();
//        return resp;
//    }

    //work
//    @GET
//    @Produces("text/plain")
//    public Response hello(){
//        
//        //Game g = new Game(strgid, strgname, "waiting");
//        
//        JsonObject jso = Json.createObjectBuilder()
//                .add("gid", 1234)
//                .build();
//        
//        //System.err.println(cgs.hm);
//        System.err.println(jso.toString());
//        
//        Response resp = Response.ok("Hello")
//                .header("x-date", (new Date()).toString())
//                .build();
//        return resp;
//    }
    
    
//    @GET
//    @Produces("application/json")
//    public Response hello(){
//         
//        CreateGameServlet cgs = new CreateGameServlet();
//        System.err.println(cgs.hm);
//        
//        JsonObject jso = Json.createObjectBuilder()
//                .add("gid", 1234)
//                .add("gname", "Game Myo")
//                .build();
//        
//        JsonArrayBuilder b = Json.createArrayBuilder();
//        
//        Response resp = Response.ok("Hello")
//                .header("x-date", (new Date()).toString())
//                .build();
//        return resp;
//    }
    
//    @GET
//    @Produces("text/plain")
//    public Response hello(){
//        
////        Game g = null;
//        System.err.println(GlobalHashMap.hm.toString());
//        System.err.println(GlobalHashMap.hm.toString());
//        //System.err.println(GlobalHashMap.hm.toString());
//        Game g = GlobalHashMap.hm.get("1234");
//        
//        JsonObject jso = Json.createObjectBuilder()
//                .add("gid", 1234)
//                .add("gname", g.getGname())
//                .add("status", g.getStatus())
//                .build();
//        
//        //System.err.println(cgs.hm);
//        System.err.println(jso.toString());
//        
//        Response resp = Response.ok("Hello")
//                .header("x-date", (new Date()).toString())
//                .build();
//        return resp;
//    }
//    
//    @GET
//    @Produces("text/html")
//    public Response hellohtml(){
//        Response resp = Response.ok("<h1>Hello html</h1>")
//                .header("x-date", (new Date()).toString())
//                .build();
//        return resp;
//    }
//    
//    
//    $('#demo').append('<div class="pname">'+ json[i].color + " : " + json[i].type + " : " + json[i].value
//						+'<img src="images/'+ json[i].image +'.png" />'+'</div>');
}
