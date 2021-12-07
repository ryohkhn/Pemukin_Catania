package game;

import board.Board;
import board.Port;

import java.util.HashMap;
import java.util.HashSet;

public abstract class Player{
    protected String color;
    protected HashMap<String,Integer> ressources=Board.generateHashMapRessource();
    protected HashMap<String,Integer> propertiesCounter=new HashMap<>();
    protected HashMap<Card,Integer> cards=new HashMap<>();
    protected HashSet<Port> ports;
    protected int victoryPoint=0;

    public Player(){
        propertiesCounter.put("City",0);
        propertiesCounter.put("Colony",0);
        propertiesCounter.put("Road",0);
        for(Card card : Card.values()){
            cards.put(card,0);
        }
    }
}
