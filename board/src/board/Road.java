package board;
import game.Player;

public class Road {
    protected Player player;
    protected Tile tile1;
    protected Tile tile2;
    protected Tile tile3;
    public Road(Tile[][] t,int x, int y) {
        this.tile1=t[x][y];
        this.player=null;
        preciseAttributs(t,x,y);
    }

    private void preciseAttributs(Tile[][] t,int x, int y) {
        if(x==0){ //ligne du haut
            if(y==0){ //coin gauche
                if(t[x][y].roads.lastIndexOf(this)==0) this.tile2=t[x][y+1];
                if(t[x][y].roads.lastIndexOf(this)==3) this.tile2=t[x+1][y];
            }
            else if(y==t.length){ //coin droit
                if(t[x][y].roads.lastIndexOf(this)==0) this.tile2=t[x][y-1];
                if(t[x][y].roads.lastIndexOf(this)==1) this.tile2=t[x+1][y];
                else this.tile2=t[x+1][y-1];
            }else this.tile2=t[x+1][y+1];
        }
        else if(x==t.length){// ligne du bas
            if(y==0){ //coin gauche
                if(t[x][y].roads.lastIndexOf(this)==2) this.tile2=t[x][y+1];
                else if(t[x][y].roads.lastIndexOf(this)==3) this.tile2=t[x-1][y];
            }
            else if(y==t.length){ //coin droit
                if(t[x][y].roads.lastIndexOf(this)==1) this.tile2=t[x][y-1];
                else if(t[x][y].roads.lastIndexOf(this)==2) this.tile2=t[x-1][y];
                else this.tile2=t[x-1][y-1];
            }else this.tile2=t[x-1][y+1];
        }
        else if(y==0){ //coté gauche
            if(t[x][y].roads.lastIndexOf(this)==0) this.tile2=t[x-1][y+1];
            else if(t[x][y].roads.lastIndexOf(this)==1){ //cas général
                this.tile2=t[x-1][y+1];
                this.tile3=t[x+1][y+1];
            }
            else if(t[x][y].roads.lastIndexOf(this)==2) this.tile2=t[x+1][y+1];
            else if(t[x][y].roads.lastIndexOf(this)==3){
                this.tile2=t[x-1][y];
                this.tile3=t[x+1][y];
            }
        }
        else if(y==t.length){ //coté droit
            if(t[x][y].roads.lastIndexOf(this)==0) this.tile2=t[x-1][y-1];
            else if(t[x][y].roads.lastIndexOf(this)==1){
                this.tile2=t[x-1][y];
                this.tile3=t[x+1][y];
            }
            else if(t[x][y].roads.lastIndexOf(this)==2) this.tile2=t[x+1][y-1];
            else if(t[x][y].roads.lastIndexOf(this)==3){
                this.tile2=t[x-1][y-1];
                this.tile3=t[x+1][y-1];
            }
        }
        else { //cas général
            this.tile2=t[x-1][y+1];
            this.tile3=t[x+1][y+1];
        }
    }
}
