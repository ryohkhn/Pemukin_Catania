package board;

import java.util.HashMap;
import java.util.Random;

public class Board{
    private Tile[][] tiles;
    private HashMap<Integer,Integer> tilesId;

    public Board(int size){
        this.tilesId=new HashMap<>();
        fillMapId();
        this.tiles=new Tile[size][size];
        fillTiles();
        createTileRoadAndVertex();
    }

    // fonction qui crée chaque case et remplit le tableau tiles
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

    // fonction qui vérifie dans le hashmap d'entier les valeurs des cases et si on a atteint la limite pour cette valeur (nombre d'apparitions sur le plateau)
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

    // fonction qui remplit la hashmap avec les valeurs de 0 à 12 correspondant aux cases, et initialise leur compteur à 0
    private void fillMapId(){
        for(int i=2; i<=12; i++){
            tilesId.put(i,0);
        }
    }

    // fonction qui créé les routes et les sommets pour chaque case, en partageant le même objet pour les cases adjaceantes
    private void createTileRoadAndVertex(){
        Road left;
        Road top;
        Vertex topL;
        Vertex topR;
        Vertex bottomL;
        for(int x=0; x<tiles[0].length; x++){
            for(int y=0; y<tiles[0].length; y++){
                if(x==0 && y==0){
                    for(int i=0; i<4; i++){
                        tiles[x][y].roads.add(i,new Road());
                        tiles[x][y].vertices.add(i,new Vertex());
                    }
                }
                else if(x==0){
                    for(int i=0; i<3; i++){
                        tiles[x][y].roads.add(i,new Road());
                    }
                    left=tiles[x][y-1].roads.get(1);
                    tiles[x][y].roads.add(3,left);

                    topL=tiles[x][y-1].vertices.get(1);
                    bottomL=tiles[x][y-1].vertices.get(2);
                    tiles[x][y].vertices.add(0,topL);
                    tiles[x][y].vertices.add(1,new Vertex());
                    tiles[x][y].vertices.add(2,new Vertex());
                    tiles[x][y].vertices.add(3,bottomL);
                }
                else if(y==0){
                    top=tiles[x-1][y].roads.get(2);
                    tiles[x][y].roads.add(0,top);
                    for(int i=1; i<4; i++){
                        tiles[x][y].roads.add(i,new Road());
                    }

                    topL=tiles[x-1][y].vertices.get(3);
                    topR=tiles[x-1][y].vertices.get(2);
                    tiles[x][y].vertices.add(0,topL);
                    tiles[x][y].vertices.add(1,topR);
                    tiles[x][y].vertices.add(2,new Vertex());
                    tiles[x][y].vertices.add(3,new Vertex());
                }
                else{
                    left=tiles[x][y-1].roads.get(1);
                    top=tiles[x-1][y].roads.get(2);
                    tiles[x][y].roads.add(0,top);
                    for(int i=1; i<3; i++){
                        tiles[x][y].roads.add(i,new Road());
                    }
                    tiles[x][y].roads.add(3,left);

                    topL=tiles[x-1][y].vertices.get(3);
                    topR=tiles[x-1][y].vertices.get(2);
                    bottomL=tiles[x][y-1].vertices.get(2);
                    tiles[x][y].vertices.add(0,topL);
                    tiles[x][y].vertices.add(1,topR);
                    tiles[x][y].vertices.add(2,new Vertex());
                    tiles[x][y].vertices.add(3,bottomL);
                }
            }
        }
    }
}
