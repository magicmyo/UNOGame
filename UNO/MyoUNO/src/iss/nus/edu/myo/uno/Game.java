/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iss.nus.edu.myo.uno;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Myo Thu Htet
 */
public class Game {
    
    private String id;
    private ArrayList<Player> playList;
    private String status;
    private Deck deck;
    private Card discardCard;

    public Game(String id, ArrayList<Player> playList, String status, Deck deck) {
        this.id = id;
        this.playList = playList;
        this.status = status;
        this.deck = deck;
        //this.discardPile = discardPile;
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
     * @return the playList
     */
    public ArrayList<Player> getPlayList() {
        return playList;
    }

    /**
     * @param playList the playList to set
     */
    public void setPlayList(ArrayList<Player> playList) {
        this.playList = playList;
    }

    public void setPlayer(Player player) {
        this.playList.add(player);
    }
    
    /** 
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the deck
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * @param deck the deck to set
     */
    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    /**
     * @return the discardPile
     */
    public Card getDiscardPile() {
        return discardCard;
    }

    /**
     * @param discardPile the discardPile to set
     */
    public void setDiscardPile(Card discardPile) {
        this.discardCard = discardPile;
    }
     
    public Deck ShuffleDeck(Deck d){
        Collections.shuffle(d.getDeck());
        return d;
    }

    public void startPlay(){
        deck = this.ShuffleDeck(deck.DeckCreate());
        
       int counter =1;
        while(counter <= 7){
            
            for (Player player : playList) {
                Card handCard = deck.getCardFromDeck();
                player.setCardToHand(handCard);
                
            }           
            counter++;
        }
        
        discardCard = deck.getCardFromDeck();
    }
    
    @Override
    public String toString() {
        return "Game id=" + id + "\nDiscard Card=" + discardCard+ "\nStatus=" + status  +"\n"+ deck + "\n\t"+ playList;
    }
    
    
}
