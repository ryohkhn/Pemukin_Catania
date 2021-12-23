package board;

import game.Player;

import java.util.HashSet;

public class Road {
    protected Player player;
    protected HashSet<Road> adjacentRoads=new HashSet<>();
    protected HashSet<Colony> adjacentColonies=new HashSet<>();
    public boolean checked=false;

    public Road() {
        this.player=null;
    }

    @Override
    public String toString() {
        String res="";
        int x=0;
        for(Road t : this.adjacentRoads){
            res+=" Road n°" + x;
            x++;
        }
        res+="\n";
        x=0;
        for(Colony c : this.adjacentColonies){
            res+=" Colony n°" + x;
            x++;
        }
        return res;
    }
    public Player getPlayer() {
        return player;
    }

    public void addAdjacentColonies(Tile[][] t, int x, int y){
        if(t[x][y].getRoads().get(0)==this){
            adjacentColonies.add(t[x][y].colonies.get(0));
            adjacentColonies.add(t[x][y].colonies.get(1));
        }else if(t[x][y].getRoads().get(1)==this){
            adjacentColonies.add(t[x][y].colonies.get(1));
            adjacentColonies.add(t[x][y].colonies.get(2));
        }else if(t[x][y].getRoads().get(2)==this){
            adjacentColonies.add(t[x][y].colonies.get(2));
            adjacentColonies.add(t[x][y].colonies.get(3));
        }else{
            adjacentColonies.add(t[x][y].colonies.get(3));
            adjacentColonies.add(t[x][y].colonies.get(0));
        }
    }

    public boolean isOwned(){
        return (player!=null);
    }

    public void setPlayer(Player player){
        this.player=player;
    }

    /*
            Fonction qui remplit l'ArrayList de road correspondant a ses voisins,
            de manière à pouvoir vérifier s'il y a bien une route voisine appartenant au joueur lors de la création d'une route.
        */
    public void addAdjacentRoads(Tile[][] t,int x, int y) {
        this.checked=true;
        if(x==0) { //ligne du haut
            if(y==0) { //coin gauche
                if(t[x][y].roads.get(0)==this){
                    this.adjacentRoads.add(t[x][y].roads.get(1));
                    this.adjacentRoads.add(t[x][y].roads.get(3));
                    this.adjacentRoads.add(t[x][y+1].roads.get(0));
                }
                else if(t[x][y].roads.get(1)==this){
                    this.adjacentRoads.add(t[x][y].roads.get(0));
                    this.adjacentRoads.add(t[x][y].roads.get(2));
                    this.adjacentRoads.add(t[x][y+1].roads.get(0));
                    this.adjacentRoads.add(t[x][y+1].roads.get(2));
                    this.adjacentRoads.add(t[x+1][y+1].roads.get(3));
                }
                else if(t[x][y].roads.get(2)==this){
                    this.adjacentRoads.add(t[x][y].roads.get(1));
                    this.adjacentRoads.add(t[x][y].roads.get(3));
                    this.adjacentRoads.add(t[x+1][y].roads.get(1));
                    this.adjacentRoads.add(t[x+1][y].roads.get(3));
                    this.adjacentRoads.add(t[x+1][y+1].roads.get(0));
                }
                else if(t[x][y].roads.get(3)==this){
                    this.adjacentRoads.add(t[x][y].roads.get(0));
                    this.adjacentRoads.add(t[x][y].roads.get(2));
                    this.adjacentRoads.add(t[x+1][y].roads.get(3));
                }
            }
            else if(y==t.length-1) { //coin droit
                if(t[x][y].roads.get(0)==this){
                    this.adjacentRoads.add(t[x][y].roads.get(1));
                    this.adjacentRoads.add(t[x][y].roads.get(3));
                    this.adjacentRoads.add(t[x][y-1].roads.get(0));
                }
                else if(t[x][y].roads.get(1)==this){
                    this.adjacentRoads.add(t[x][y].roads.get(0));
                    this.adjacentRoads.add(t[x][y].roads.get(2));
                    this.adjacentRoads.add(t[x+1][y].roads.get(1));
                }
                else if(t[x][y].roads.get(2)==this){
                    this.adjacentRoads.add(t[x][y].roads.get(1));
                    this.adjacentRoads.add(t[x][y].roads.get(3));
                    this.adjacentRoads.add(t[x+1][y].roads.get(1));
                    this.adjacentRoads.add(t[x+1][y].roads.get(3));
                    this.adjacentRoads.add(t[x+1][y-1].roads.get(0));
                }
            }
            else { // //reste ligne haut
                if(t[x][y].roads.get(0)==this){
                    this.adjacentRoads.add(t[x][y].roads.get(1));
                    this.adjacentRoads.add(t[x][y].roads.get(3));
                    this.adjacentRoads.add(t[x][y-1].roads.get(0));
                    this.adjacentRoads.add(t[x][y+1].roads.get(0));
                }
                else if(t[x][y].roads.get(1)==this) {
                    this.adjacentRoads.add(t[x][y].roads.get(0));
                    this.adjacentRoads.add(t[x][y].roads.get(2));
                    this.adjacentRoads.add(t[x][y+1].roads.get(0));
                    this.adjacentRoads.add(t[x][y+1].roads.get(2));
                    this.adjacentRoads.add(t[x+1][y+1].roads.get(3));
                }
                else if(t[x][y].roads.get(2)==this) {
                    this.adjacentRoads.add(t[x][y].roads.get(1));
                    this.adjacentRoads.add(t[x][y].roads.get(3));
                    this.adjacentRoads.add(t[x+1][y+1].roads.get(0));
                    this.adjacentRoads.add(t[x+1][y+1].roads.get(3));
                    this.adjacentRoads.add(t[x+1][y-1].roads.get(0));
                    this.adjacentRoads.add(t[x+1][y-1].roads.get(1));
                }
            }
        }
        else if(x==t.length-1) {// ligne du bas
            if(y==0) { //coin gauche
                if(t[x][y].roads.get(1)==this){
                    this.adjacentRoads.add(t[x][y].roads.get(0));
                    this.adjacentRoads.add(t[x][y].roads.get(2));
                    this.adjacentRoads.add(t[x-1][y+1].roads.get(2));
                    this.adjacentRoads.add(t[x-1][y+1].roads.get(3));
                    this.adjacentRoads.add(t[x][y+1].roads.get(2));
                }
                else if(t[x][y].roads.get(2)==this){
                    this.adjacentRoads.add(t[x][y].roads.get(1));
                    this.adjacentRoads.add(t[x][y].roads.get(3));
                    this.adjacentRoads.add(t[x][y+1].roads.get(2));
                }
                else if(t[x][y].roads.get(3)==this){
                    this.adjacentRoads.add(t[x][y].roads.get(0));
                    this.adjacentRoads.add(t[x][y].roads.get(2));
                    this.adjacentRoads.add(t[x-1][y].roads.get(3));
                }
            }
            else if(y==t.length-1) { //coin droit
                if(t[x][y].roads.get(1)==this){
                    this.adjacentRoads.add(t[x][y].roads.get(0));
                    this.adjacentRoads.add(t[x][y].roads.get(2));
                    this.adjacentRoads.add(t[x-1][y].roads.get(1));
                }
                else if(t[x][y].roads.get(2)==this){
                    this.adjacentRoads.add(t[x][y].roads.get(1));
                    this.adjacentRoads.add(t[x][y].roads.get(3));
                    this.adjacentRoads.add(t[x][y-1].roads.get(2));
                }
            }
            else{ //reste ligne bas
                if(t[x][y].roads.get(1)==this){
                    this.adjacentRoads.add(t[x][y].roads.get(0));
                    this.adjacentRoads.add(t[x][y].roads.get(2));
                    this.adjacentRoads.add(t[x-1][y+1].roads.get(2));
                    this.adjacentRoads.add(t[x-1][y+1].roads.get(3));
                    this.adjacentRoads.add(t[x][y+1].roads.get(2));
                }
                else if(t[x][y].roads.get(2)==this){
                    this.adjacentRoads.add(t[x][y-1].roads.get(2));
                    this.adjacentRoads.add(t[x][y-1].roads.get(1));
                    this.adjacentRoads.add(t[x][y+1].roads.get(2));
                    this.adjacentRoads.add(t[x][y+1].roads.get(3));
                }

            }
        }
        else if(y==0) { //coté gauche
            if(t[x][y].roads.get(1)==this) {
                this.adjacentRoads.add(t[x][y].roads.get(0));
                this.adjacentRoads.add(t[x][y].roads.get(2));
                this.adjacentRoads.add(t[x-1][y+1].roads.get(2));
                this.adjacentRoads.add(t[x-1][y+1].roads.get(3));
                this.adjacentRoads.add(t[x+1][y+1].roads.get(0));
                this.adjacentRoads.add(t[x+1][y+1].roads.get(3));
            } else if(t[x][y].roads.get(2)==this) {
                this.adjacentRoads.add(t[x][y].roads.get(1));
                this.adjacentRoads.add(t[x][y].roads.get(3));
                this.adjacentRoads.add(t[x+1][y].roads.get(1));
                this.adjacentRoads.add(t[x+1][y].roads.get(3));
                this.adjacentRoads.add(t[x+1][y+1].roads.get(0));
            }
            else if(t[x][y].roads.get(3)==this) {
                this.adjacentRoads.add(t[x-1][y].roads.get(2));
                this.adjacentRoads.add(t[x-1][y].roads.get(3));
                this.adjacentRoads.add(t[x+1][y].roads.get(3));
                this.adjacentRoads.add(t[x+1][y].roads.get(0));
            }
        }
        else if(y==t.length-1) { //coté droit
            if(t[x][y].roads.get(1)==this) {
                this.adjacentRoads.add(t[x-1][y].roads.get(1));
                this.adjacentRoads.add(t[x-1][y].roads.get(2));
                this.adjacentRoads.add(t[x+1][y].roads.get(0));
                this.adjacentRoads.add(t[x+1][y].roads.get(1));
            }
            else if(t[x][y].roads.get(2)==this) {
                this.adjacentRoads.add(t[x][y].roads.get(1));
                this.adjacentRoads.add(t[x][y].roads.get(3));
                this.adjacentRoads.add(t[x+1][y].roads.get(1));
                this.adjacentRoads.add(t[x+1][y].roads.get(3));
                this.adjacentRoads.add(t[x+1][y-1].roads.get(0));
            }
        }
        else { //cas général
            if(t[x][y].roads.get(1)==this) {
                this.adjacentRoads.add(t[x][y].roads.get(0));
                this.adjacentRoads.add(t[x][y].roads.get(2));
                this.adjacentRoads.add(t[x-1][y+1].roads.get(2));
                this.adjacentRoads.add(t[x-1][y+1].roads.get(3));
                this.adjacentRoads.add(t[x+1][y+1].roads.get(0));
                this.adjacentRoads.add(t[x+1][y+1].roads.get(3));
            }
            else if(t[x][y].roads.get(2)==this) {
                this.adjacentRoads.add(t[x][y].roads.get(1));
                this.adjacentRoads.add(t[x][y].roads.get(3));
                this.adjacentRoads.add(t[x+1][y+1].roads.get(0));
                this.adjacentRoads.add(t[x+1][y+1].roads.get(3));
                this.adjacentRoads.add(t[x+1][y-1].roads.get(0));
                this.adjacentRoads.add(t[x+1][y-1].roads.get(1));
            }
        }
    }
    // TODO: 27/11/2021 Fonction qui return true, si il y a une route (appartenant au joueur) adjacente a la route que l'ont veut construire
    public boolean isBuildable(Player player){
        for(Road road:adjacentRoads){
            if(road.player==player) return true;
        }
        for(Colony colony:adjacentColonies){
            if(colony.player==player) return true;
        }
        return false;
    }
}
