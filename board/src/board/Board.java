package board;

import java.util.HashMap;
import java.util.Random;

public class Board{
    private Tile[][] tiles;

    public Board(int size){
        this.tiles=new Tile[size][size];
        fillTilesIdAndRessources();
        createTileRoadAndColony();
        preciseAttributs();
        addAdjacentRoadAndColony();
        addPorts();
    }

    public void preciseAttributs(){ // TODO: 26/11/2021 changer nom
        for(int i=0;i<tiles.length;i++){
            for(int j=0;j<tiles[i].length;j++){
                for(Road r: tiles[i][j].roads) {
                    r.preciseAttributs(this.tiles,i,j);
                }
            }
        }
    }

    // applique la fonction addAdjacentColonyAndRoad à chaque colony se trouvant dans les cases
    public void addAdjacentRoadAndColony(){
        for(int i=0;i<tiles.length;i++){
            for(int j=0;j<tiles[i].length;j++){
                for(Colony c: tiles[i][j].colonies) {
                    c.addAdjacentColonyAndRoad(this.tiles,i,j);
                }
            }
        }
    }

    public void afficher() { // TODO: 28/11/2021 Fonction de test, a supprimer avant le rendu 
        for(Tile[] t:tiles){
            System.out.println();
            for(Tile t1: t){
                System.out.print(t1.id + "  ");
            }
        }
        System.out.println();
    }

    public Tile[][] getTiles() { // todo supprimer quand plus utile, sert juste a tester les fonctions
        return tiles;
    }

    // fonction qui crée chaque case et remplit le tableau tiles avec son identifiant respectif ainsi que la ressource attribuée
    private void fillTilesIdAndRessources(){
        HashMap<Integer,Integer> tilesId=new HashMap<>();
        HashMap<String,Integer> tilesRessource=new HashMap<>();
        Random random=new Random();
        int randomId;
        int randomRessource;
        String ressource="";
        tilesRessource.put("Clay",0);
        tilesRessource.put("Ore",0);
        tilesRessource.put("Wheat",0);
        tilesRessource.put("Wood",0);
        tilesRessource.put("Wool",0);
        for(int i=2; i<=12; i++){
            tilesId.put(i,0);
        }
        // on traite le cas du désert à part, il ne peut se trouver que dans les 4 cases au centre du plateau
        // on l'attribue donc avant d'attribuer le reste des cases
        randomId=random.nextInt(4);
        switch(randomId){
            case 0 -> tiles[1][1]=new Tile(7,"");
            case 1 -> tiles[1][2]=new Tile(7,"");
            case 2 -> tiles[2][1]=new Tile(7,"");
            case 3 -> tiles[2][2]=new Tile(7,"");
        }
        for(int i=0; i<tiles.length; i++){
            for(int j=0; j<tiles.length; j++){
                if(tiles[i][j]==null){
                    do{
                        randomId=random.nextInt(11)+2;
                    }
                    while(!generateTileId(randomId,tilesId));
                    do{
                        randomRessource=random.nextInt(5);
                        switch(randomRessource){
                            case 0 -> ressource="Clay";
                            case 1 -> ressource="Ore";
                            case 2 -> ressource="Wheat";
                            case 3 -> ressource="Wood";
                            case 4 -> ressource="Wool";
                        }
                    }
                    while(!generateTileRessource(ressource,tilesRessource));
                    tiles[i][j]=new Tile(randomId,ressource);
                }
            }
        }
    }

    // fonction qui vérifie dans le hashmap de string-entier la ressource en argument si on a atteint la limite pour cette ressource (nombre d'apparitions sur le plateau)
    // si oui on return false, sinon on incrémente la valeur et on return true
    private boolean generateTileRessource(String ressource,HashMap<String,Integer> tilesRessource){
        if(tilesRessource.get(ressource)==3){
            return false;
        }
        tilesRessource.merge(ressource,1,Integer::sum);
        return true;
    }

    // fonction qui vérifie dans le hashmap d'entier les valeurs des cases et si on a atteint la limite pour cette valeur (nombre d'apparitions sur le plateau)
    // si oui on return false, sinon on incrémente la valeur et on return true
    private boolean generateTileId(int key,HashMap<Integer,Integer> tilesId){
        switch(key){
            case 2,3,4,11,12 ->{
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

    // fonction qui créé les routes et les sommets pour chaque case, en partageant le même objet pour les cases adjaceantes
    private void createTileRoadAndColony(){
        Road left;
        Road top;
        Colony topL;
        Colony topR;
        Colony bottomL;
        for(int x=0; x<tiles[0].length; x++){
            for(int y=0; y<tiles[0].length; y++){
                if(x==0 && y==0){
                    for(int i=0; i<4; i++){
                        tiles[x][y].roads.add(i,new Road());
                        tiles[x][y].colonies.add(i,new Colony());
                    }
                }
                else if(x==0){
                    for(int i=0; i<3; i++){
                        tiles[x][y].roads.add(i,new Road());
                    }
                    left=tiles[x][y-1].roads.get(1);
                    tiles[x][y].roads.add(3,left);

                    topL=tiles[x][y-1].colonies.get(1);
                    bottomL=tiles[x][y-1].colonies.get(2);
                    tiles[x][y].colonies.add(0,topL);
                    tiles[x][y].colonies.add(1,new Colony());
                    tiles[x][y].colonies.add(2,new Colony());
                    tiles[x][y].colonies.add(3,bottomL);
                }
                else if(y==0){
                    top=tiles[x-1][y].roads.get(2);
                    tiles[x][y].roads.add(0,top);
                    for(int i=1; i<4; i++){
                        tiles[x][y].roads.add(i,new Road());
                    }

                    topL=tiles[x-1][y].colonies.get(3);
                    topR=tiles[x-1][y].colonies.get(2);
                    tiles[x][y].colonies.add(0,topL);
                    tiles[x][y].colonies.add(1,topR);
                    tiles[x][y].colonies.add(2,new Colony());
                    tiles[x][y].colonies.add(3,new Colony());
                }
                else{
                    left=tiles[x][y-1].roads.get(1);
                    top=tiles[x-1][y].roads.get(2);
                    tiles[x][y].roads.add(0,top);
                    for(int i=1; i<3; i++){
                        tiles[x][y].roads.add(i,new Road());
                    }
                    tiles[x][y].roads.add(3,left);

                    topL=tiles[x-1][y].colonies.get(3);
                    topR=tiles[x-1][y].colonies.get(2);
                    bottomL=tiles[x][y-1].colonies.get(2);
                    tiles[x][y].colonies.add(0,topL);
                    tiles[x][y].colonies.add(1,topR);
                    tiles[x][y].colonies.add(2,new Colony());
                    tiles[x][y].colonies.add(3,bottomL);
                }
            }
        }
    }

    public void addPorts(){
        Port tmp;
        for(int x=0; x<tiles[0].length; x++){
            for(int y=0; y<tiles[0].length; y++){
                if(x==0 && y%2==1){
                    tiles[x][y].setAsPort();
                    tmp=new Port();
                    tiles[x][y].port=tmp;
                    tiles[x][y].colonies.get(0).setLinkedPort(tmp);
                    tiles[x][y].colonies.get(1).setLinkedPort(tmp);
                }
                else if(y==0 && x%2==0){
                    tiles[x][y].setAsPort();
                    tmp=new Port();
                    tiles[x][y].port=tmp;
                    tiles[x][y].colonies.get(0).setLinkedPort(tmp);
                    tiles[x][y].colonies.get(3).setLinkedPort(tmp);
                }
                else if(x==tiles.length-1 && y%2==0){
                    tiles[x][y].setAsPort();
                    tmp=new Port();
                    tiles[x][y].port=tmp;
                    tiles[x][y].colonies.get(2).setLinkedPort(tmp);
                    tiles[x][y].colonies.get(3).setLinkedPort(tmp);
                }
                else if(y==tiles.length-1 && x%2==1){
                    tiles[x][y].setAsPort();
                    tmp=new Port();
                    tiles[x][y].port=tmp;
                    tiles[x][y].colonies.get(1).setLinkedPort(tmp);
                    tiles[x][y].colonies.get(2).setLinkedPort(tmp);
                }
            }
        }
    }
}
