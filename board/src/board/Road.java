package board;
import game.Player;

import java.util.ArrayList;

public class Road {
    protected Player player;
    protected ArrayList<Tile> tileArrayList=new ArrayList<>();
    public Road(Tile[][] t,int x, int y) {
        this.tileArrayList.add(t[x][y]);
        this.player=null;
        preciseAttributs(t,x,y);
    }
    /*
    Fonction qui remplit l'ArrayList de tile correspondant a ses voisins,
    de manière à pouvoir vérifier s'il y a bien une route voisine lors de la création d'une route.
     */
    private void preciseAttributs(Tile[][] t,int x, int y) { //todo changer le nom de la fonction
        if(x==0){ //ligne du haut
            if(y==0){ //coin gauche
                if(t[x][y].roads.lastIndexOf(this)==0) this.tileArrayList.add(t[x][y+1]);
                if(t[x][y].roads.lastIndexOf(this)==3) this.tileArrayList.add(t[x+1][y]);
            }
            else if(y==t.length){ //coin droit
                if(t[x][y].roads.lastIndexOf(this)==0) this.tileArrayList.add(t[x][y-1]);
                if(t[x][y].roads.lastIndexOf(this)==1) this.tileArrayList.add(t[x+1][y]);
                else this.tileArrayList.add(t[x+1][y-1]);
            }else this.tileArrayList.add(t[x+1][y+1]);
        }
        else if(x==t.length){// ligne du bas
            if(y==0){ //coin gauche
                if(t[x][y].roads.lastIndexOf(this)==2) this.tileArrayList.add(t[x][y+1]);
                else if(t[x][y].roads.lastIndexOf(this)==3) this.tileArrayList.add(t[x-1][y]);
            }
            else if(y==t.length){ //coin droit
                if(t[x][y].roads.lastIndexOf(this)==1) this.tileArrayList.add(t[x][y-1]);
                else if(t[x][y].roads.lastIndexOf(this)==2) this.tileArrayList.add(t[x-1][y]);
                else this.tileArrayList.add(t[x-1][y-1]);
            }else this.tileArrayList.add(t[x-1][y+1]);
        }
        else if(y==0){ //coté gauche
            if(t[x][y].roads.lastIndexOf(this)==0) this.tileArrayList.add(t[x-1][y+1]);
            else if(t[x][y].roads.lastIndexOf(this)==1){ //cas général
                this.tileArrayList.add(t[x-1][y+1]);
                this.tileArrayList.add(t[x+1][y+1]);
            }
            else if(t[x][y].roads.lastIndexOf(this)==2) this.tileArrayList.add(t[x+1][y+1]);
            else if(t[x][y].roads.lastIndexOf(this)==3){
                this.tileArrayList.add(t[x-1][y]);
                this.tileArrayList.add(t[x+1][y]);
            }
        }
        else if(y==t.length){ //coté droit
            if(t[x][y].roads.lastIndexOf(this)==0) this.tileArrayList.add(t[x-1][y-1]);
            else if(t[x][y].roads.lastIndexOf(this)==1){
                this.tileArrayList.add(t[x-1][y]);
                this.tileArrayList.add(t[x+1][y]);
            }
            else if(t[x][y].roads.lastIndexOf(this)==2) this.tileArrayList.add(t[x+1][y-1]);
            else if(t[x][y].roads.lastIndexOf(this)==3){
                this.tileArrayList.add(t[x-1][y-1]);
                this.tileArrayList.add(t[x+1][y-1]);
            }
        }
        else { //cas général
            this.tileArrayList.add(t[x-1][y+1]);
            this.tileArrayList.add(t[x+1][y+1]);
        }
    }
}
