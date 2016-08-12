/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myo.uno.rest;

import iss.nus.edu.myo.uno.Game;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import javax.json.JsonObject;
import javax.json.Json;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Myo Thu Htet
 */
@WebServlet("/addgame")
public class CreateGameServlet extends HttpServlet{

    //public Map<String, Game> hm = new HashMap<String, Game>();
    
    @Override
    
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        String strgname = req.getParameter("gname");
        String strgid = UUID.randomUUID().toString().substring(0,8);
        
        Game g = new Game(strgid, strgname, "waiting");
        
        GlobalHashMap.hm.put(strgid, g);
   
                JsonObject jso = Json.createObjectBuilder()
                .add("gid", g.getId())
                .add("gname", g.getGname())
                .add("status", g.getStatus())
                .build();
        
        
        
//        jso.putAll(hm);    
//        System.out.printf( "JSON: %s", json.toString(2) );
//
//        StringWriter stringWriter = new StringWriter();
//
//        objectMapper.writeValue(stringWriter, map);


        
        System.err.println("print gname>>"+jso);
        
        resp.setStatus(200);
        
    }
    
    
}
