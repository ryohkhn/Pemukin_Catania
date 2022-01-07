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
    protected HashMap<Card,Integer> cardsDrawnThisTurn=new HashMap<>();
    protected LinkedList<Port> ports=new LinkedList<>();
    protected int victoryPoint=0;
    protected int knightPlayed=0;
    public boolean alreadyPlayedCardThisTurn=false;

    public Player(String color){
        propertiesCounter.put("City",0);
        propertiesCounter.put("Colony",0);
        propertiesCounter.put("Road",0);
        for(Card card : Card.values()){
            cards.put(card,0);
        }
        setColor(color);
    }

    public abstract boolean isBot();

// setter, getter, toString ->
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

    public Color getColor() {
        return color;
    }

    public int getVictoryPoint() {
        return victoryPoint;
    }

    public HashMap<String, Integer> getResources(){
        return resources;
    }

    public LinkedList<Port> getPorts(){
        return ports;
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

    public String getCardsToString() {
        String res="";
        for(Map.Entry<Card,Integer> entry: this.cards.entrySet()) {
            res+=entry.getKey().name() + " : "+ String.valueOf(entry.getValue())+ " ; ";
        }
        return res;
    }

    public String getRessourcesToString() {
        return ("Clay : "+resources.get("Clay")+" ; Ore :"+resources.get("Ore")+" ; Wheat : "+resources.get("Wheat")+" ; Wood : "+resources.get("Wood")+" ; Wool : "+resources.get("Wool")+" ;");
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

//fin setter, getter, toString

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

    // fonction qui ajoute un port dans les ports du joueur
    public void addPort(Port port){
        if(!this.ports.contains(port)){
            this.ports.add(port);
        }
    }

    // fonction qui renvoie la quantité de ressources du joueur
    public int resourceCount() {
        int compt=0;
        for(Map.Entry<String,Integer> entry : resources.entrySet()){
            if(entry.getKey().equals("Clay") || entry.getKey().equals("Wool") || entry.getKey().equals("Wheat") || entry.getKey().equals("Ore") || entry.getKey().equals("Wood")){
                compt+=entry.getValue();
            }
        }
        return compt;
    }

    // fonction qui vérifie si le joueur a 10 VP
    public boolean hasWin(){
        if(this.victoryPoint>=10) return true;
        return false;
    }

    // fonction qui supprime une carte dans la hashmap correspondante
    public void removeCard(Card card) {
        this.cards.merge(card, 1, (initialValue, decreasedValue) -> initialValue-decreasedValue);
    }

    // fonction qui ajoute une carte dans la hashmap correspondante
    public void addCard(Card card){
        this.cards.merge(card,1,Integer::sum);
    }

    // fonction qui vérifie si le joueur a les ressources en argument
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

    // fonction qui vérifie si le joueur a une carte(quelconque) à jouer.
    public boolean hasCard(){
        for(Map.Entry<Card, Integer> entry : this.cards.entrySet()){ // pour toutes les cartes du joueur
            if(entry.getValue()>this.cardsDrawnThisTurn.getOrDefault(entry.getKey(), 0)) {
                // si le nombre de cartes de ce type dans la main du joueur > nombre de cartes de ce type dans les cartes achetées a ce tour
                return true;
            }
        }
        return false;
    }

    public void cardsDrawnThisTurnReset() {
        this.cardsDrawnThisTurn.clear();
        for(Card card : Card.values()){
            cardsDrawnThisTurn.put(card,0);
        }
    }
}
