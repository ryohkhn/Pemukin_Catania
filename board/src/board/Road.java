package board;
import game.Player;

import java.util.ArrayList;

public class Road {
    protected Player player;
    protected ArrayList<Tile> roadArrayList=new ArrayList<>();
    public Road(Tile[][] t,int x, int y) {
        this.player=null;
    }

    @Override
    public String toString() {
        String res="";
        int x=0;
        for(Tile t : this.roadArrayList){
            res+=" tile n°" + x + " " + t.toString();
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
                    this.roadArrayList.add(t[x][y]);
                    this.roadArrayList.add(t[x][y+1]);
                }
                else if(t[x][y].roads.get(3)==this){
                    this.roadArrayList.add(t[x][y]);
                    this.roadArrayList.add(t[x+1][y]);
                }
                else if(t[x][y].roads.get(2)==this){
                    this.roadArrayList.add(t[x][y]);
                    this.roadArrayList.add(t[x+1][y]);
                    this.roadArrayList.add(t[x+1][y+1]);
                }
                else if(t[x][y].roads.get(1)==this){
                    this.roadArrayList.add(t[x][y]);
                    this.roadArrayList.add(t[x][y+1]);
                    this.roadArrayList.add(t[x+1][y+1]);
                }
            } else if(y==t.length-1) { //coin droit
                if(t[x][y].roads.get(0)==this){
                    this.roadArrayList.add(t[x][y]);
                    this.roadArrayList.add(t[x][y-1]);
                }
                else if(t[x][y].roads.get(1)==this){
                    this.roadArrayList.add(t[x][y]);
                    this.roadArrayList.add(t[x+1][y]);
                }
                else if(t[x][y].roads.get(2)==this){
                    this.roadArrayList.add(t[x][y]);
                    this.roadArrayList.add(t[x+1][y-1]);
                    this.roadArrayList.add(t[x+1][y]);

                }
            } else { // //reste ligne haut
                if(t[x][y].roads.get(0)==this){
                    this.roadArrayList.add(t[x][y+1]);
                    this.roadArrayList.add(t[x][y-1]);
                } else if(t[x][y].roads.get(1)==this) {
                    this.roadArrayList.add(t[x][y]);
                    this.roadArrayList.add(t[x][y+1]);
                    this.roadArrayList.add(t[x+1][y+1]);
                } else if(t[x][y].roads.get(2)==this) {
                    this.roadArrayList.add(t[x][y]);
                    this.roadArrayList.add(t[x+1][y-1]);
                    this.roadArrayList.add(t[x+1][y+1]);
                }
            }
        }
        else if(x==t.length-1) {// ligne du bas
            if(y==0) { //coin gauche
                if(t[x][y].roads.get(1)==this){
                    this.roadArrayList.add(t[x][y]);
                    this.roadArrayList.add(t[x-1][y+1]);
                    this.roadArrayList.add(t[x][y+1]);

                }
                else if(t[x][y].roads.get(2)==this){
                    this.roadArrayList.add(t[x][y]);
                    this.roadArrayList.add(t[x][y+1]);
                }
                else if(t[x][y].roads.get(3)==this){
                    this.roadArrayList.add(t[x][y]);
                    this.roadArrayList.add(t[x-1][y]);
                }
            } else if(y==t.length-1) { //coin droit
                if(t[x][y].roads.get(1)==this){
                    this.roadArrayList.add(t[x][y]);
                    this.roadArrayList.add(t[x-1][y]);
                }
                else if(t[x][y].roads.get(2)==this){
                    this.roadArrayList.add(t[x][y]);
                    this.roadArrayList.add(t[x][y-1]);
                }
            } else{ //reste ligne bas
                if(t[x][y].roads.get(1)==this){
                    this.roadArrayList.add(t[x][y]);
                    this.roadArrayList.add(t[x-1][y+1]);
                    this.roadArrayList.add(t[x][y+1]);
                }
                else if(t[x][y].roads.get(2)==this){
                    this.roadArrayList.add(t[x][y-1]);
                    this.roadArrayList.add(t[x][y+1]);
                }

            }
        }
        else if(y==0) { //coté gauche
            if(t[x][y].roads.get(1)==this) {
                this.roadArrayList.add(t[x][y]);
                this.roadArrayList.add(t[x-1][y+1]);
                this.roadArrayList.add(t[x+1][y+1]);
            } else if(t[x][y].roads.get(2)==this) {
                this.roadArrayList.add(t[x][y]);
                this.roadArrayList.add(t[x+1][y]);
                this.roadArrayList.add(t[x+1][y+1]);
            }
            else if(t[x][y].roads.get(3)==this) {
                this.roadArrayList.add(t[x-1][y]);
                this.roadArrayList.add(t[x+1][y]);
            }
        }
        else if(y==t.length-1) { //coté droit
            if(t[x][y].roads.get(1)==this) {
                this.roadArrayList.add(t[x-1][y]);
                this.roadArrayList.add(t[x+1][y]);
            }
            else if(t[x][y].roads.get(2)==this) {
                this.roadArrayList.add(t[x][y]);
                this.roadArrayList.add(t[x+1][y-1]);
                this.roadArrayList.add(t[x+1][y]);
            }
        } else { //cas général
            if(t[x][y].roads.get(1)==this) {
                this.roadArrayList.add(t[x][y]);
                this.roadArrayList.add(t[x-1][y+1]);
                this.roadArrayList.add(t[x+1][y+1]);
            } else if(t[x][y].roads.get(2)==this) {
                this.roadArrayList.add(t[x][y]);
                this.roadArrayList.add(t[x+1][y-1]);
                this.roadArrayList.add(t[x+1][y+1]);
            }
        }
    }
    // TODO: 27/11/2021 Fonction qui return true, si il y a une route (appartenant au joueur) adjacente a la route que l'ont veut construire
    public boolean isMyRoadAround(Player player){
        return false;
    }
}
