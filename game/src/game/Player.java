package game;

import board.Board;
import board.Port;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public abstract class Player{
    protected Color color;
    protected HashMap<String,Integer> resources=Board.generateHashMapRessource();
    protected HashMap<String,Integer> propertiesCounter=new HashMap<>();
    protected HashMap<Card,Integer> cards=new HashMap<>();
    protected LinkedList<Port> ports=new LinkedList<>();
    protected int victoryPoint=0;
    protected int knightPlayed=0;
    public boolean alreadyPlayedCardThisTurn=false;
    public boolean longestRoad;

    // constructeur qui initialise
    public Player(String color){
        propertiesCounter.put("City",0);
        propertiesCounter.put("Colony",0);
        propertiesCounter.put("Road",0);
        for(Card card : Card.values()){
            cards.put(card,0);
        }
        setColor(color);
    }

    public int getKnightPlayed() {
        return knightPlayed;
    }
    public int getNbCards(){
        int res=0;
        for(Map.Entry<Card,Integer> entry : this.cards.entrySet()){
            if(!entry.getKey().name().equals("LargestArmy")){
                res+=entry.getValue();
            }
        }
        return res;
    }

    // fonction qui incrémente les points de victoire d'un joueur en fonction de l'entier en argument
    public void addVictoryPoint(int point){
        this.victoryPoint+=point;
    }

    // fonction qui incrémente le compteur de construction de joueur en fonction du type de construction en argument
    public void addPropertie(String propertie){
        this.propertiesCounter.merge(propertie,1,Integer::sum);
    }

    // fonction qui diminue le compteur de colonie du joueur
    public void removeColonyInCounter(){
        this.propertiesCounter.merge("Colony",1,(a,b)->a-b);
    }

    // fonction qui vérifie si le joueur n'a pas dépassé la quantité maximum possible
    public boolean canBuildPropertie(String propertie,int maxQuantity){
        if(propertiesCounter.get(propertie)<maxQuantity){
            return true;
        }
        return false;
    }

    public void addPort(Port port){
        if(!this.ports.contains(port)){
            this.ports.add(port);
        }
    }

    public int resourceCount() {
        int compt=0;
        for(int value : resources.values()) {
            compt+=value;
        }
        return compt;
    }

    public boolean hasWin(){
        if(this.victoryPoint>=10) return true;
        return false;
    }

    public void removeCard(Card card) {
        this.cards.merge(card, 1, (initialValue, decreasedValue) -> initialValue-decreasedValue);
    }

    public void addCard(Card card){
        this.cards.merge(card,1,Integer::sum);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(String color){
        if(color!=null){
            switch(color){
                case "orange" -> this.color=new Color(0xE85C05);
                case "green" -> this.color=new Color(0x55A100);
                case "blue" -> this.color=new Color(0x0C59A2);
                case "yellow" ->this.color=new Color(0xEEEE00);
            }
        }
    }

    public int getVictoryPoint() {
        return victoryPoint;
    }

    public String getCardsToString() {
        String res="";
        for(Map.Entry<Card,Integer> entry: this.cards.entrySet()) {
            res+=entry.getKey().name() + " : "+ String.valueOf(entry.getValue())+ " ; ";
        }
        return res;
    }

    public String getRessourcesToString() {
        String res="";
        for(Map.Entry<String ,Integer> entry: this.resources.entrySet()) {
            res+=entry.getKey() + " : "+ entry.getValue()+ " ; ";
        }
        return res;
    }

    public String firstLetter() {
        return this.toString().charAt(0) + " ";
    }

    @Override
    public String toString(){
        return switch(color.getRGB()){
            case -1549307 -> "orange";
            case -11165440 -> "green";
            case -15967838 -> "blue";
            case -1118720 -> "yellow";
            default -> throw new IllegalStateException();
        };
    }

    public LinkedList<Port> getPorts(){
        return ports;
    }

    public boolean hasResources(String[] resources){
        int clayCount=0,oreCount=0,wheatCount=0,woodCount=0,woolCount=0;
        for(String resource:resources){
            switch(resource){
                case "Clay" -> clayCount++;
                case "Ore" -> oreCount++;
                case "Wheat" -> wheatCount++;
                case "Wood" -> woodCount++;
                case "Wool" -> woolCount++;
            }
        }
        for(String resource:this.resources.keySet()){
            switch(resource){
                case "Clay" -> {
                    if(this.resources.get(resource)<clayCount) return false;
                }
                case "Ore" -> {
                    if(this.resources.get(resource)<oreCount) return false;
                }
                case "Wheat" -> {
                    if(this.resources.get(resource)<wheatCount) return false;
                }
                case "Wood" -> {
                    if(this.resources.get(resource)<woodCount) return false;
                }
                case "Wool" -> {
                    if(this.resources.get(resource)<woolCount) return false;
                }
            }
        }
        return true;
    }
}
