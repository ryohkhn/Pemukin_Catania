package game;

import java.util.HashMap;

public abstract class Player{
    protected String color;
    protected HashMap<String,Integer> ressources;
    protected int victoryPoint=0;

    protected void fillRessourcesMap(){
        ressources.put("Clay",0);
        ressources.put("Ore",0);
        ressources.put("Wheat",0);
        ressources.put("Wood",0);
        ressources.put("Wool",0);
    }
}
