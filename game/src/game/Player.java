package game;

import board.Board;
import board.Port;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public abstract class Player{
    protected String color;
    protected HashMap<String,Integer> ressources=Board.generateHashMapRessource();
    protected HashMap<String,Integer> propertiesCounter=new HashMap<>();
    protected HashMap<Card,Integer> cards=new HashMap<>();
    protected LinkedList<Port> ports=new LinkedList<>();
    protected int victoryPoint=0;
    public boolean alreadyPlayedCardThisTurn=false;
    public boolean longestRoad;

    // constructeur qui initialise
    public Player(){
        propertiesCounter.put("City",0);
        propertiesCounter.put("Colony",0);
        propertiesCounter.put("Road",0);
        for(Card card : Card.values()){
            cards.put(card,0);
        }
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
        for(int value : ressources.values()) {
            compt+=value;
        }
        return compt;
    }

    public boolean hasWin(){
        if(this.victoryPoint>=10) return true;
        return false;
    }

    public void removeCard(Card card) {
        this.cards.merge(card, 1, (a, b) -> a-b);
    }

    public String getColor() {
        return color;
    }

    public int getVictoryPoint() {
        return victoryPoint;
    }

    public String getCardsToString() {
        String res="";
        for(Map.Entry<Card,Integer> entry: this.cards.entrySet()) {
            res+=entry.getKey().name() + ":"+ String.valueOf(entry.getValue())+ " ; ";
        }
        return res;
    }

    public String getRessourcesToString() {
        String res="";
        for(Map.Entry<String ,Integer> entry: this.ressources.entrySet()) {
            res+=entry.getKey() + ":"+ String.valueOf(entry.getValue())+ " ; ";
        }
        return res;
    }

    public String firstLetter() {
        return String.valueOf(color.charAt(0)) + " ";
    }

    @Override
    public String toString(){
        return "Player of color "+this.color;
    }

    public LinkedList<Port> getPorts(){
        return ports;
    }

    public boolean hasResources(String[] resources){
        int clayCount=0,oreCount=0,wheatCount=0,woodCount=0,woolCount=0;
        for(String resource:resources){
            switch(resource){
                case "Clay" : clayCount++;
                case "Ore" : oreCount++;
                case "Wheat" : wheatCount++;
                case "Wood" : woodCount++;
                case "Wool" : woolCount++;
            }
        }
        for(String resource:this.ressources.keySet()){
            switch(resource){
                case "Clay" : if(this.ressources.get(resource)<clayCount) return false;
                case "Ore" : if(this.ressources.get(resource)<oreCount) return false;
                case "Wheat" : if(this.ressources.get(resource)<wheatCount) return false;
                case "Wood" : if(this.ressources.get(resource)<woodCount) return false;
                case "Wool" : if(this.ressources.get(resource)<woolCount) return false;
            }
        }
        return true;
    }
}
