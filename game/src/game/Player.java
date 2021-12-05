package game;

import board.Port;

import java.util.HashMap;
import java.util.HashSet;

public abstract class Player{
    protected String color;
    protected HashMap<String,Integer> ressources;
    protected HashSet<Port> ports;
    protected int victoryPoint=0;

    protected void fillRessourcesMap(){
        ressources.put("Clay",0);
        ressources.put("Ore",0);
        ressources.put("Wheat",0);
        ressources.put("Wood",0);
        ressources.put("Wool",0);
    }
}
