/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iss.nus.edu.myo.uno;

import java.util.ArrayList;

/**
 *
 * @author Myo Thu Htet
 */
public class Player {
    
    private String id;
    private String name;
    private ArrayList<Card> hand = new ArrayList<Card>();
    
    public Player(){
        
    }

    public Player(String id, String name) {
        this.id = id;
        this.name = name;
    }

    
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
      
    /**
     * @return the hand
     */
    public Card getCardFromHand(int i) {
        return hand.remove(i);
    }

    /**
     * @param hand the hand to set
     */
    public void setCardToHand(Card card) {
        this.hand.add(card);
    }

    @Override
    public String toString() {
        return "Player:" + "id=" + id + ", name=" + name + "\n\t\tCards in hand:\n\t\t\t" + hand+"\n\t";
    }
    
    
}
