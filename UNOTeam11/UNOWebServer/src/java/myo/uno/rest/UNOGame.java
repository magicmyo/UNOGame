/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myo.uno.rest;

import iss.nus.edu.myo.uno.Card;
import iss.nus.edu.myo.uno.Deck;
import iss.nus.edu.myo.uno.Game;
import iss.nus.edu.myo.uno.Player;
import java.io.Console;
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
import javax.ws.rs.PathParam;
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
        JsonObject jso;
        String strgname = name;
        String strgid = UUID.randomUUID().toString().substring(0,8);
        strcurrentgid=strgid;
        Game g = new Game(strgid, strgname, "waiting");
        
        hm.put(strgid, g);
        
        jso = Json.createObjectBuilder()
            .add("gid",g.getId())
            .add("gname", strgname)
            .add("status", g.getStatus())
            .build();
        System.out.println(">>>>>Create Game "+jso.toString());
        Response resp = Response.ok(jso.toString())
                //.header("Access-Control-Allow-Origin", "*")
                .build();
        return resp;
        
//        try {
//            URI location = new java.net.URI(localhostport+"/UNOWebClient/TableWaitPlayer.html");            
//            return Response.seeOther(location).build();
//        } catch (URISyntaxException ex) {
//            return Response.status(404).entity("Redirect Fail").build();
//        }                  
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
                //.header("Access-Control-Allow-Origin", "*")
                .build();
        return resp;
    }
    
//    @GET
//    @Path("/tablewaitplayer")
//    @Produces("text/plain")
//    public Response newGame(){
//        JsonObject jso ;
//        //JsonArrayBuilder jsa = Json.createArrayBuilder();
//
//        Game g = hm.get(strcurrentgid);
//        
//        System.out.println(">>>>>>After Game Create" + strcurrentgid);
//        
//        jso = Json.createObjectBuilder()
//            .add("gid",g.getId())
//            .add("name", g.getGname())
//            .add("status", g.getStatus())
//            .build();
//
//        Response resp = Response.ok(jso.toString())
//                .header("Access-Control-Allow-Origin", "*")
//                .build();
//        return resp;
//    }
    
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
//        try {
//            URI location = new java.net.URI(localhostport+"/UNOWebClient/PlayerWaitPlayer.html");            
//            return Response.seeOther(location).build();
//        } catch (URISyntaxException ex) {
//            return Response.status(404).entity("Redirect Fail").build();
//        }  

        JsonObject jso = Json.createObjectBuilder()
            .add("gid",strcurrentgid)
            .add("pid",strcurrentpid)
            .build();

        Response resp = Response.ok(jso.toString())
                //.header("Access-Control-Allow-Origin", "*")
                .build();
        return resp;
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
                //.header("Access-Control-Allow-Origin", "*")
                .build();
        return resp;
    }
    
//    @GET
//    @Path("/playerwaitplayer")
//    @Produces("text/plain")
//    public Response showTablePlayer(){
//        JsonObject jso ;
//    
//        Game g = hm.get(strcurrentgid);
//        
//        jso = Json.createObjectBuilder()
//            .add("gid",g.getId())
//            .add("gname",g.getGname())
//            .add("pid",strcurrentpid)
//            .build();
//
//        Response resp = Response.ok(jso.toString())
//                .header("Access-Control-Allow-Origin", "*")
//                .build();
//        return resp;
//    }
    
    @GET
    @Path("/tablegamestart/{gid}")
    @Produces("text/plain")
    public Response startTablePlay(@PathParam("gid") String gid){
        
        strcurrentgid = gid;
        System.out.println(">>>> Enter Start Game " + strcurrentgid);        
        JsonObject jso ;
        JsonArrayBuilder jsa = Json.createArrayBuilder();
        
        Game g = hm.get(strcurrentgid); 
        int playersize = g.getPlayList().size();
        System.out.println(">>>>>> Player List of " + strcurrentgid + "is " + playersize);
        if(playersize >= 1 && playersize <= 5)
        {
            g.startPlay();                       
            ArrayList<Player> pList = g.getPlayList();

            for (int i = 0; i < pList.size(); i++) {
                Player player = pList.get(i);

                jso = Json.createObjectBuilder()
                .add("pid",player.getId())
                .add("name", player.getName())
                .add("status",g.getStatus())
                .build();

               jsa.add(jso);
                System.out.println(">>>> Each Player List " + jso.toString());
            }

            Response resp = Response.ok(jsa.build().toString())
                    //.header("Access-Control-Allow-Origin", "*")
                    .build();          
            
            return resp;
            
//            try {
//                URI location = new java.net.URI(localhostport+"/UNOWebClient/TableGameStart.html");            
//                return Response.seeOther(location).build();
//            } catch (URISyntaxException ex) {
//                return Response.status(404).entity("Redirect Fail").build();
//            }
        }
        else{
            return Response.status(404)
                    .entity("This game can't play!At least 2 to 5 players needed. Now is "+playersize+" players")
                    //.header("Access-Control-Allow-Origin", "*")
                    .build();
        }      
    }
    
    @GET
    @Path("/discardcard/{gid}")
    @Produces("text/plain")
    public Response showTableDiscardCards(@PathParam("gid") String gid){
        JsonObject jso ;
        strcurrentgid=gid;
        System.out.println(">>>>> Game ID" + strcurrentgid);
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
                //.header("Access-Control-Allow-Origin", "*")
                .build();
        return resp;
    }
    
    @GET
    @Path("/showdeckcard/{gid}")
    @Produces("text/plain")
    public Response showTableDeckCards(@PathParam("gid") String gid){
        JsonObject jso ;
        JsonArrayBuilder jsa = Json.createArrayBuilder();
        
        strcurrentgid=gid;
        System.out.println(">>>>> Game ID" + strcurrentgid);
        Game g = hm.get(strcurrentgid);     

        Deck gameDeck = g.getDeck();
        ArrayList<Card> deckCard = gameDeck.getDeck();
        
        for (Card card : deckCard) {
            jso = Json.createObjectBuilder()
            .add("gid",g.getId())
            .add("gname",g.getGname())
            .add("cid",card.getCid())            
            .add("color",card.getColor())
            .add("type", card.getType())
            .add("value",card.getValue())
            .add("image",card.getImage())
            .build();
            
             jsa.add(jso);
             
        }

        Response resp = Response.ok(jsa.build().toString())
                //.header("Access-Control-Allow-Origin", "*")
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
    @Path("/dealplayercard/{gid}/{pid}")
    @Produces("text/plain")
    public Response showPlayerCards(@PathParam("gid") String gid, @PathParam("pid") String pid){
        JsonObject jso ;
        JsonArrayBuilder jsa = Json.createArrayBuilder();
        
        strcurrentgid = gid;
        strcurrentpid = pid;
        System.out.println(">>>>>IN Deal Player Card " + gid +" " + pid);
        Player player=null;
        Game g = hm.get(strcurrentgid);
        
        System.out.println(">>>game: "+strcurrentgid);
        //System.out.println(">>>"+g);
        
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
                //.header("Access-Control-Allow-Origin", "*")
                .build();
        
        return resp;
    }
    
    //player to table
    @POST
    @Path("/cardtodiscardpile")
    @Produces("text/plain")
   // public void discardCardPlayerToTable(@PathParam("gid") String gid, @PathParam("pid") String pid, @PathParam("cid") String cid){
    public void discardCardPlayerToTable(@FormParam("gid") String gid, @FormParam("pid") String pid, @FormParam("cid") String cid){
//        JsonObject jso ;
//        JsonArrayBuilder jsa = Json.createArrayBuilder();
//        
        System.out.println("I am in disccar UNOGame");
        strcurrentgid = gid;
        strcurrentpid = pid;
        String strdiscardcid = cid;
        
        System.out.println(">>>>>IN discardcard Card " + gid +": " + pid+": " + cid);
        Player player=null;
        Game g = hm.get(strcurrentgid);
        
        System.out.println(">>>game: "+strcurrentgid);
        //System.out.println(">>>"+g);
        
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
            
            if(card.getCid().equals(strdiscardcid)){
                g.setDiscardPile(player.getCardFromHand(i));
            }
        }
        System.err.println("Discard:"+g.getDiscardPile()+": hand"+player.getCardInHand());

//        Response resp = Response.ok(jsa.build().toString())
//                //.header("Access-Control-Allow-Origin", "*")
//                .build();
//        
//        return resp;
    }
    
    //table deck to player 
    @POST
    @Path("/tabledecktoplayer")
    @Produces("text/plain")
   // public void discardCardPlayerToTable(@PathParam("gid") String gid, @PathParam("pid") String pid, @PathParam("cid") String cid){
    public void tableDeckToPlayer(@FormParam("gid") String gid, @FormParam("pid") String pid, @FormParam("cid") String cid){

        System.out.println("I am in disccar UNOGame");
        strcurrentgid = gid;
        strcurrentpid = pid;
        String strcurrentcid = cid;
        
        System.out.println("> table deck Card " + gid +": " + pid);
        Player player=null;
        Game g = hm.get(strcurrentgid);
        
        System.out.println(">>>game: "+strcurrentgid);
        //System.out.println(">>>"+g);
        
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
        
        ArrayList<Card> deckCard = g.getDeck().getDeck();
        
        for (int i=0; i< deckCard.size(); i++) {
            Card card = deckCard.get(i);
            if(card.getCid().equals(strcurrentcid)){
                player.setCardToHand(g.getDeck().getCardFromDeck(deckCard.indexOf(card)));
            }
        }      
        System.err.println("Discard:"+g.getDiscardPile()+"\n: hand"+player.getCardInHand());       
    }

}
