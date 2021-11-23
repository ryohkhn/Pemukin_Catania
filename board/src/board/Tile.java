package board;

import java.util.LinkedList;
public class Tile{
    private final int id;
    boolean thief=false;
    private LinkedList<Vertex> vertices;
    private LinkedList<Road> roads;

    public Tile(int id){
        this.id=id;
        if(id==7){
            thief=true;
        }
    }
}
