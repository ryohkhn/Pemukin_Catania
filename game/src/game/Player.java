package game;

import board.Board;
import board.Port;

import java.util.HashMap;
import java.util.HashSet;

public abstract class Player{
    protected String color;
    protected HashMap<String,Integer> ressources=Board.generateHashMapRessource();
    protected HashSet<Port> ports;
    protected int victoryPoint=0;
}
