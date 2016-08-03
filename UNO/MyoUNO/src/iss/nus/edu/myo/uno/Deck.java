/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iss.nus.edu.myo.uno;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Myo Thu Htet
 */
public class Deck {
    
    private final int  NUMCARD = 108;
    
    private ArrayList<Card> cards = new ArrayList<Card>();// = new ArrayList<Card>();

    public Deck(){
        
    }
    /**
     * @param deck the deck to set
     */
    public void setCardToDeck(Card card) {
        this.getDeck().add(card);
    }

    public Card getCardFromDeck() {
        return this.getDeck().remove(0);
    }
    
    @Override
    public String toString() {
        return "Cards on deck: " + getDeck().size()+"\n";//+cards;
    }
    
    
    public Deck DeckCreate(){
        String color[] = {"Red","Yellow", "Green", "Blue", "RYGB"};
        String type[] = {"Normal","Skip","Reverse", "Draw2", "Wild", "WildDrawFour"};
        
        Card cards; 
        Deck decks = new Deck();
        
        for(int c = 0; c < color.length; c++){
            
            int typeIndex = 0;
            
            for(int n = 0; n <= 12; n++ ){
                
                int value = n;
                
                if(n > 9){
                    typeIndex++;
                    value = 20;
                }
                                
                if(c !=4){
                   cards = new Card(color[c], type[typeIndex], value, color[c]+type[0]+value);
                   decks.setCardToDeck(cards);
                   
                   if(n != 0) decks.setCardToDeck(cards); 
                }
                   
            }
            
            if(c == 4 ){
                int counter =1;
                while(counter <= 4){
                    
                    for(int t = 4; t <type.length; t++){
                        cards = new Card(color[c], type[t], 50, color[c]+type[t]+50);
                        decks.setCardToDeck(cards);                
                    }
                    
                    counter++;
                }
            }
        }
        return decks;
    }

    /**
     * @return the deck
     */
    public ArrayList<Card> getDeck() {
        return cards;
    }

    /**
     * @param deck the deck to set
     */
    public void setDeck(ArrayList<Card> deck) {
        this.cards = deck;
    }
}
