package board;

import java.util.LinkedList;
public class Tile{
    protected final int id;
    boolean thief=false;
    protected LinkedList<Vertex> vertices;
    protected LinkedList<Road> roads;

    public Tile(int id){
        this.id=id;
        this.roads=new LinkedList<>();
        this.vertices=new LinkedList<>();
        if(id==7){
            thief=true;
        }
    }
}
