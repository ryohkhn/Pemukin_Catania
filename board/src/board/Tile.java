package board;

import game.Player;

import java.util.LinkedList;
public class Tile{
    protected final int id;
    protected boolean thief=false;
    protected String ressource;
    protected LinkedList<Colony> colonies;
    protected LinkedList<Road> roads;
    protected boolean isPort=false; // pas sur de l'utilité, à voir si on l'efface dans le futur
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

    public void setAsPort(){
        isPort=true;
    }

    @Override
    public String toString() { // TODO supprimer quand plus nécessaire
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
