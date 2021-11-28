package board;

import game.Player;

import java.util.LinkedList;
public class Tile{
    protected final int id;
    boolean thief=false;
    protected LinkedList<Colony> colonies;
    protected LinkedList<Road> roads;

    public Tile(int id){
        this.id=id;
        this.roads=new LinkedList<>();
        this.colonies=new LinkedList<>();
        if(id==7){
            thief=true;
        }
    }

    @Override
    public String toString() {
        return "Tile{"+
                "id="+id+
                '}';
    }

    public LinkedList<Road> getRoads() { //todo supprimer quand plus tile : sert a tester les fonctions
        return roads;
    }

    // TODO: 27/11/2021 Fonction qui return true, si il y a une ville/colonie (appartenant au joueur) adjacente a la route que l'ont veut construire
    public boolean isMyCityAround(Player player){
        return false;
    }

}
