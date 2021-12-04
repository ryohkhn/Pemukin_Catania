package board;
import game.Player;

import java.util.ArrayList;
import java.util.HashSet;

public class Road {
    protected Player player;
    protected ArrayList<Tile> roadArrayList2=new ArrayList<>();
    protected HashSet<Road> roadHashSet=new HashSet<>();

    public Road() {
        this.player=null;
    }

    @Override
    public String toString() {
        String res="";
        int x=0;
        for(Road t : this.roadHashSet){
            res+=" Road n°" + x;
            x++;
        }
        return res;
    }

    /*
        Fonction qui remplit l'ArrayList de road correspondant a ses voisins,
        de manière à pouvoir vérifier s'il y a bien une route voisine appartenant au joueur lors de la création d'une route.
         */
    public void preciseAttributs(Tile[][] t,int x, int y) { // TODO: 28/11/2021 mettre des road plutot que tile dans this.roadArrayList
        if(x==0) { //ligne du haut
            if(y==0) { //coin gauche
                if(t[x][y].roads.get(0)==this){
                    this.roadHashSet.add(t[x][y].roads.get(1));
                    this.roadHashSet.add(t[x][y].roads.get(3));
                    this.roadHashSet.add(t[x][y+1].roads.get(0));
                }
                else if(t[x][y].roads.get(1)==this){
                    this.roadHashSet.add(t[x][y].roads.get(0));
                    this.roadHashSet.add(t[x][y].roads.get(2));
                    this.roadHashSet.add(t[x][y+1].roads.get(0));
                    this.roadHashSet.add(t[x][y+1].roads.get(2));
                    this.roadHashSet.add(t[x+1][y+1].roads.get(3));
                }
                else if(t[x][y].roads.get(2)==this){
                    this.roadHashSet.add(t[x][y].roads.get(1));
                    this.roadHashSet.add(t[x][y].roads.get(3));
                    this.roadHashSet.add(t[x+1][y].roads.get(1));
                    this.roadHashSet.add(t[x+1][y].roads.get(3));
                    this.roadHashSet.add(t[x+1][y+1].roads.get(0));
                }
                else if(t[x][y].roads.get(3)==this){
                    this.roadHashSet.add(t[x][y].roads.get(0));
                    this.roadHashSet.add(t[x][y].roads.get(2));
                    this.roadHashSet.add(t[x+1][y].roads.get(3));
                }
            }
            else if(y==t.length-1) { //coin droit
                if(t[x][y].roads.get(0)==this){
                    this.roadHashSet.add(t[x][y].roads.get(1));
                    this.roadHashSet.add(t[x][y].roads.get(3));
                    this.roadHashSet.add(t[x][y-1].roads.get(0));
                }
                else if(t[x][y].roads.get(1)==this){
                    this.roadHashSet.add(t[x][y].roads.get(0));
                    this.roadHashSet.add(t[x][y].roads.get(2));
                    this.roadHashSet.add(t[x+1][y].roads.get(1));
                }
                else if(t[x][y].roads.get(2)==this){
                    this.roadHashSet.add(t[x][y].roads.get(1));
                    this.roadHashSet.add(t[x][y].roads.get(3));
                    this.roadHashSet.add(t[x+1][y].roads.get(1));
                    this.roadHashSet.add(t[x+1][y].roads.get(3));
                    this.roadHashSet.add(t[x+1][y-1].roads.get(0));
                }
            }
            else { // //reste ligne haut
                if(t[x][y].roads.get(0)==this){
                    this.roadHashSet.add(t[x][y].roads.get(1));
                    this.roadHashSet.add(t[x][y].roads.get(3));
                    this.roadHashSet.add(t[x][y-1].roads.get(0));
                    this.roadHashSet.add(t[x][y+1].roads.get(0));
                }
                else if(t[x][y].roads.get(1)==this) {
                    this.roadHashSet.add(t[x][y].roads.get(0));
                    this.roadHashSet.add(t[x][y].roads.get(2));
                    this.roadHashSet.add(t[x][y+1].roads.get(0));
                    this.roadHashSet.add(t[x][y+1].roads.get(2));
                    this.roadHashSet.add(t[x+1][y+1].roads.get(3));
                }
                else if(t[x][y].roads.get(2)==this) {
                    this.roadHashSet.add(t[x][y].roads.get(1));
                    this.roadHashSet.add(t[x][y].roads.get(3));
                    this.roadHashSet.add(t[x+1][y+1].roads.get(0));
                    this.roadHashSet.add(t[x+1][y+1].roads.get(3));
                    this.roadHashSet.add(t[x+1][y-1].roads.get(0));
                    this.roadHashSet.add(t[x+1][y-1].roads.get(1));
                }
            }
        }
        else if(x==t.length-1) {// ligne du bas
            if(y==0) { //coin gauche
                if(t[x][y].roads.get(1)==this){
                    this.roadHashSet.add(t[x][y].roads.get(0));
                    this.roadHashSet.add(t[x][y].roads.get(2));
                    this.roadHashSet.add(t[x-1][y+1].roads.get(2));
                    this.roadHashSet.add(t[x-1][y+1].roads.get(3));
                    this.roadHashSet.add(t[x][y+1].roads.get(2));
                }
                else if(t[x][y].roads.get(2)==this){
                    this.roadHashSet.add(t[x][y].roads.get(1));
                    this.roadHashSet.add(t[x][y].roads.get(3));
                    this.roadHashSet.add(t[x][y+1].roads.get(2));
                }
                else if(t[x][y].roads.get(3)==this){
                    this.roadHashSet.add(t[x][y].roads.get(0));
                    this.roadHashSet.add(t[x][y].roads.get(2));
                    this.roadHashSet.add(t[x-1][y].roads.get(3));
                }
            }
            else if(y==t.length-1) { //coin droit
                if(t[x][y].roads.get(1)==this){
                    this.roadHashSet.add(t[x][y].roads.get(0));
                    this.roadHashSet.add(t[x][y].roads.get(2));
                    this.roadHashSet.add(t[x-1][y].roads.get(1));
                }
                else if(t[x][y].roads.get(2)==this){
                    this.roadHashSet.add(t[x][y].roads.get(1));
                    this.roadHashSet.add(t[x][y].roads.get(3));
                    this.roadHashSet.add(t[x][y-1].roads.get(2));
                }
            }
            else{ //reste ligne bas
                if(t[x][y].roads.get(1)==this){
                    this.roadHashSet.add(t[x][y].roads.get(0));
                    this.roadHashSet.add(t[x][y].roads.get(2));
                    this.roadHashSet.add(t[x-1][y+1].roads.get(2));
                    this.roadHashSet.add(t[x-1][y+1].roads.get(3));
                    this.roadHashSet.add(t[x][y+1].roads.get(2));
                }
                else if(t[x][y].roads.get(2)==this){
                    this.roadHashSet.add(t[x][y-1].roads.get(2));
                    this.roadHashSet.add(t[x][y-1].roads.get(1));
                    this.roadHashSet.add(t[x][y+1].roads.get(2));
                    this.roadHashSet.add(t[x][y+1].roads.get(3));
                }

            }
        }
        else if(y==0) { //coté gauche
            if(t[x][y].roads.get(1)==this) {
                this.roadHashSet.add(t[x][y].roads.get(0));
                this.roadHashSet.add(t[x][y].roads.get(2));
                this.roadHashSet.add(t[x-1][y+1].roads.get(2));
                this.roadHashSet.add(t[x-1][y+1].roads.get(3));
                this.roadHashSet.add(t[x+1][y+1].roads.get(0));
                this.roadHashSet.add(t[x+1][y+1].roads.get(3));
            } else if(t[x][y].roads.get(2)==this) {
                this.roadHashSet.add(t[x][y].roads.get(1));
                this.roadHashSet.add(t[x][y].roads.get(3));
                this.roadHashSet.add(t[x+1][y].roads.get(1));
                this.roadHashSet.add(t[x+1][y].roads.get(3));
                this.roadHashSet.add(t[x+1][y+1].roads.get(0));
            }
            else if(t[x][y].roads.get(3)==this) {
                this.roadHashSet.add(t[x-1][y].roads.get(2));
                this.roadHashSet.add(t[x-1][y].roads.get(3));
                this.roadHashSet.add(t[x+1][y].roads.get(3));
                this.roadHashSet.add(t[x+1][y].roads.get(0));
            }
        }
        else if(y==t.length-1) { //coté droit
            if(t[x][y].roads.get(1)==this) {
                this.roadHashSet.add(t[x-1][y].roads.get(1));
                this.roadHashSet.add(t[x-1][y].roads.get(2));
                this.roadHashSet.add(t[x+1][y].roads.get(0));
                this.roadHashSet.add(t[x+1][y].roads.get(1));
            }
            else if(t[x][y].roads.get(2)==this) {
                this.roadHashSet.add(t[x][y].roads.get(1));
                this.roadHashSet.add(t[x][y].roads.get(3));
                this.roadHashSet.add(t[x+1][y].roads.get(1));
                this.roadHashSet.add(t[x+1][y].roads.get(3));
                this.roadHashSet.add(t[x+1][y-1].roads.get(0));
            }
        }
        else { //cas général
            if(t[x][y].roads.get(1)==this) {
                this.roadHashSet.add(t[x][y].roads.get(0));
                this.roadHashSet.add(t[x][y].roads.get(2));
                this.roadHashSet.add(t[x-1][y+1].roads.get(2));
                this.roadHashSet.add(t[x-1][y+1].roads.get(3));
                this.roadHashSet.add(t[x+1][y+1].roads.get(0));
                this.roadHashSet.add(t[x+1][y+1].roads.get(3));
            }
            else if(t[x][y].roads.get(2)==this) {
                this.roadHashSet.add(t[x][y].roads.get(1));
                this.roadHashSet.add(t[x][y].roads.get(3));
                this.roadHashSet.add(t[x+1][y+1].roads.get(0));
                this.roadHashSet.add(t[x+1][y+1].roads.get(3));
                this.roadHashSet.add(t[x+1][y-1].roads.get(0));
                this.roadHashSet.add(t[x+1][y-1].roads.get(1));
            }
        }
    }
    // TODO: 27/11/2021 Fonction qui return true, si il y a une route (appartenant au joueur) adjacente a la route que l'ont veut construire
    public boolean isMyRoadAround(Player player){
        return false;
    }
}
