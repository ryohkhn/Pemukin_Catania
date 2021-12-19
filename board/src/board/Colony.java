package board;

import game.Player;

import java.util.HashSet;

public class Colony{
    protected Player player;
    protected Port linkedPort;
    protected HashSet<Colony> adjacentColonies=new HashSet<>();
    protected HashSet<Road> adjacentRoads=new HashSet<>();
    protected boolean city=false;

    public Colony(){
        this.player=null;
        this.linkedPort=null;
    }

    public boolean isCity(){
        return this.city;
    }

    public void setAsCity(){
        this.city=true;
    }

    public boolean isOwned(){
        if(this.player!=null) return true;
        else return false;
    }

    public boolean isOwned(Player player){
        if(this.player==player) return true;
        else return false;
    }

    public Player getPlayer(){
        return player;
    }

    public void setPlayer(Player player){
        this.player=player;
    }

    // fonction permettant d'ajouter à chaque colonie ses colonies adjacentes et les routes adjacentes à la colonie
    public void addAdjacentColonyAndRoad(Tile[][] t,int x,int y){
        if(t[x][y].colonies.get(0)==this || t[x][y].colonies.get(2)==this){
            adjacentColonies.add(t[x][y].colonies.get(1));
            adjacentColonies.add(t[x][y].colonies.get(3));

            if(t[x][y].colonies.get(0)==this){
                adjacentRoads.add(t[x][y].roads.get(0));
                adjacentRoads.add(t[x][y].roads.get(3));
            }
            else{
                adjacentRoads.add(t[x][y].roads.get(1));
                adjacentRoads.add(t[x][y].roads.get(2));
            }

        }
        else if(t[x][y].colonies.get(1)==this || t[x][y].colonies.get(3)==this){
            adjacentColonies.add(t[x][y].colonies.get(0));
            adjacentColonies.add(t[x][y].colonies.get(2));

            if(t[x][y].colonies.get(1)==this){
                adjacentRoads.add(t[x][y].roads.get(0));
                adjacentRoads.add(t[x][y].roads.get(1));
            }
            else{
                adjacentRoads.add(t[x][y].roads.get(2));
                adjacentRoads.add(t[x][y].roads.get(3));
            }
        }
    }

    public void setLinkedPort(Port linkedPort){
        this.linkedPort=linkedPort;
    }

    // fonction vérifiant si une colonie peut-être construite à cet endroit
    public boolean isBuildable(Player player){
        for(Colony colony:adjacentColonies){
            if(colony.isOwned()){
                return false;
            }
        }
        for(Road road:adjacentRoads){
            if(road.player==player){
                return true;
            }
        }
        return false;
    }
}
