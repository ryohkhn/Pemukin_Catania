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
    public boolean alreadyPlayedCardThisTurn=false;

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
        this.ports.add(port);
    }

    public int ressourceCount() {
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
    public void display() { // TODO: 21/12/2021 afficher ressources, couleurs etc
    }
}
