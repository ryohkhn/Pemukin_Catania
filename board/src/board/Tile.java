package board;

import java.util.LinkedList;
public class Tile{
    protected final int id;
    protected boolean thief=false;
    protected String ressource;
    protected LinkedList<Colony> colonies;
    protected LinkedList<Road> roads;
    protected Port port=null;

    public Tile(int id,String ressource){
        this.id=id;
        this.ressource=ressource;
        this.roads=new LinkedList<>();
        this.colonies=new LinkedList<>();
        if(id==7){
            thief=true;
        }
    }

    public boolean isPort() {
        return port!=null;
    }

    public Port getPort() {
        return port;
    }

    public int getId(){
        return id;
    }

    public String getRessource(){
        return ressource;
    }

    public void setThief(boolean thief){
        this.thief=thief;
    }
    public boolean isThief(){
        return thief;
    }

    public boolean hasThief(){
        return thief;
    }

    public LinkedList<Road> getRoads() {
        return roads;
    }

    public LinkedList<Colony> getColonies(){
        return colonies;
    }
}
