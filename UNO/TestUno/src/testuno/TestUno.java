/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testuno;
import iss.nus.edu.myo.uno.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
/**
 *
 * @author Myo Thu Htet
 */
public class TestUno {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //TestUno test = new TestUno();
        Deck testDeck = new Deck();   
        Game game1;
        //testDeck = testDeck.DeckCreate();
        
        
        Player p1 = new Player("id0001", "Myo Thu");
        Player p2 = new Player("id0002", "Newton");
        Player p3 = new Player("id0003", "UNOMaster");
        Player p4 = new Player("id0004", "CardMaster");
        Player p5 = new Player("id0005", "Aung Aung");
        
        ArrayList<Player> playerList = new ArrayList<Player>();
        playerList.add(p1);
        playerList.add(p2);
        playerList.add(p3);
        playerList.add(p4);
        playerList.add(p5);
        
        String gameId  = "g001";
        String status = "Waiting";
        
        game1 = new Game(gameId, playerList, status, testDeck);
        
        game1.startPlay();
        
        System.out.println(game1);
    }  
    
}
