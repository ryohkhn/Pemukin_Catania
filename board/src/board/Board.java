package board;

import java.util.HashMap;
import java.util.Random;

public class Board{
    private Tile[][] tiles;
    private HashMap<Integer,Integer> tilesId;

    public Board(int size){
        fillMapId();
        tiles=new Tile[size][size];
        fillTiles();
    }

    // fonction qui crée chaque case et rempli le tableau tiles
    private void fillTiles(){
        Random random=new Random();
        int randomId;
        for(int i=0; i<tiles.length; i++){
            for(int j=0; j<tiles.length; j++){
                do{
                    randomId=random.nextInt(11)+2;
                }
                while(!generateTileId(randomId));
                tiles[i][j]=new Tile(randomId);
            }
        }
    }

    // fonction qui vérifie dans le hashmap d'entier des valeurs des cases si on a atteint la limite pour cette valeur
    // si oui on return false, sinon on incrémente la valeur et on return true
    private boolean generateTileId(int key){
        switch(key){
            case 2,3,4,7,11,12 ->{
                if(tilesId.get(key)==1){
                    return false;
                }
                tilesId.merge(key,1,Integer::sum);
                return true;
            }
            case 5,6,8,9,10 ->{
                if(tilesId.get(key)==2){
                    return false;
                }
                tilesId.merge(key,1,Integer::sum);
                return true;
            }
            default ->{
                return false;
            }
        }
    }

    // fonction qui rempli la hashmap avec les valeurs de 0 à 12 correspondant aux cases, et initialise leur compteur à 0
    private void fillMapId(){
        for(int i=2; i<=12; i++){
            tilesId.put(i,0);
        }
    }
}
