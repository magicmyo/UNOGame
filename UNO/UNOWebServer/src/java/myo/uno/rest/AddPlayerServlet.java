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
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Myo Thu Htet
 */
@WebServlet("/addplayer")
public class AddPlayerServlet {
    
    HashMap hm = new HashMap();
    
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        String strgname = req.getParameter("gname");
        String strgid = UUID.randomUUID().toString().substring(0,8);
        
        //Game g = new Game(strgid, strgname);
        
        //hm.put(strgid, g);
        
        System.err.println("print gname>>"+strgname);
        
        resp.setStatus(200);
    }
    
}
