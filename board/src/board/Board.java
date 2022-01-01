package board;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public class Board{
    protected Tile[][] tiles;
    protected Tile thiefTile;

    public Board(){
        this.tiles=new Tile[4][4];
        fillTilesIdAndRessources();
        createTileRoadAndColony();
        addAdjacentRoadAndColony();
        addPorts();
    }

    public Tile getThiefTile(){
        return thiefTile;
    }

    public void setThiefTile(Tile thiefTile){
        this.thiefTile=thiefTile;
    }

    // ajoute les Road et Colony adjacentes pour les Colony et les Road de toutes les cases du plateau
    public void addAdjacentRoadAndColony(){
        for(int i=0;i<tiles.length;i++){
            for(int j=0;j<tiles[i].length;j++){
                // ajoute les Road et Colony adjacentes pour les objets Road
                for(Road r: tiles[i][j].roads) {
                    r.addAdjacentRoads(this.tiles,i,j);
                    r.addAdjacentColonies(this.tiles,i,j);
                }
                // ajoute les Road et Colony adjacentes pour les objets Colony
                for(Colony c: tiles[i][j].colonies) {
                    c.addAdjacentColonyAndRoad(this.tiles,i,j);
                }
            }
        }
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    // fonction qui crée chaque case et remplit le tableau tiles avec son identifiant respectif ainsi que la ressource attribuée
    private void fillTilesIdAndRessources(){
        HashMap<Integer,Integer> tilesId=new HashMap<>();
        HashMap<String,Integer> tilesRessource=generateHashMapRessource();
        Random random=new Random();
        int randomId;
        int randomRessource;
        String ressource="";
        for(int i=2; i<=12; i++){
            tilesId.put(i,0);
        }
        // on traite le cas du désert à part, il ne peut se trouver que dans les 4 cases au centre du plateau
        // on l'attribue donc avant d'attribuer le reste des cases
        randomId=random.nextInt(4);
        Tile desert=new Tile(7,"");
        thiefTile=desert;
        switch(randomId){
            case 0 -> tiles[1][1]=desert;
            case 1 -> tiles[1][2]=desert;
            case 2 -> tiles[2][1]=desert;
            case 3 -> tiles[2][2]=desert;
        }
        for(int i=0; i<tiles.length; i++){
            for(int j=0; j<tiles.length; j++){
                if(tiles[i][j]==null){
                    do{
                        randomId=random.nextInt(11)+2;
                    }
                    while(!generateTileId(randomId,tilesId));
                    ressource=generateRessource(tilesRessource,3);
                    tiles[i][j]=new Tile(randomId,ressource);
                }
            }
        }
    }

    // fonction qui cherche une ressource qui n'est pas à som maximum dans la hashmap et l'incrémente si ce n'est pas le cas(nombre d'apparitions sur le plateau)
    private String generateRessource(HashMap<String,Integer> hashMapRessource,int max){
        String ressource;
        do{
            ressource=generateRandomRessource();
        }
        while(hashMapRessource.get(ressource)==max);
        hashMapRessource.merge(ressource,1,Integer::sum);
        return ressource;
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

    // fonction permettant de générer aléatoirement ports présents sur le plateau, et attribue aux colonies une référence à ce port
    private void addPorts(){
        Random random=new Random();
        Port tmp;
        String ressource;
        LinkedList<Integer> portNumber=new LinkedList<>();
        LinkedList<Integer> specializedPortsNumber=new LinkedList<>();
        HashMap<String,Integer> portRessource=generateHashMapRessource();
        int compt=1;
        int randomPortNumber;
        // boucles qui génèrent quels ports seront des ports spécialisés parmi les 8 ports du plateau
        for(int i=1; i<9; i++){
            portNumber.push(i);
        }
        for(int i=0; i<5; i++){
            do{
                randomPortNumber=random.nextInt(8);
            }
            while(portNumber.get(randomPortNumber)==0);
            specializedPortsNumber.push(portNumber.get(randomPortNumber));
            portNumber.set(randomPortNumber,0);
        }
        for(int x=0; x<tiles[0].length; x++){
            for(int y=0; y<tiles[0].length; y++){
                if(x==0 && y%2==1 || y==0 && x%2==0 || x==tiles.length-1 && y%2==0 || y==tiles.length-1 && x%2==1){
                    if(specializedPortsNumber.contains(compt)){
                        tmp=new Port(generateRessource(portRessource,1));
                    }
                    else{
                        tmp=new Port();
                    }
                    if(x==0 && y%2==1){
                        tiles[x][y].port=tmp;
                        tiles[x][y].colonies.get(0).setLinkedPort(tmp);
                        tiles[x][y].colonies.get(1).setLinkedPort(tmp);
                    }
                    else if(y==0 && x%2==0){
                        tiles[x][y].port=tmp;
                        tiles[x][y].colonies.get(0).setLinkedPort(tmp);
                        tiles[x][y].colonies.get(3).setLinkedPort(tmp);
                    }
                    else if(x==tiles.length-1 && y%2==0){
                        tiles[x][y].port=tmp;
                        tiles[x][y].colonies.get(2).setLinkedPort(tmp);
                        tiles[x][y].colonies.get(3).setLinkedPort(tmp);
                    }
                    else if(y==tiles.length-1 && x%2==1){
                        tiles[x][y].port=tmp;
                        tiles[x][y].colonies.get(1).setLinkedPort(tmp);
                        tiles[x][y].colonies.get(2).setLinkedPort(tmp);
                    }
                    compt++;
                }
            }
        }
    }

    // fonction permettant de générer un hashmap de <String,Integer> pré-rempli avec toutes les ressources présentes dans le jeu et initialisées à 0
    public static HashMap<String,Integer> generateHashMapRessource(){
        HashMap<String,Integer> res=new HashMap<>();
        res.put("Clay",5);
        res.put("Ore",5);
        res.put("Wheat",5);
        res.put("Wood",5);
        res.put("Wool",5);
        return res;
    }

    // fonction qui génère une ressource aléatoire et renvoi son String
    public static String generateRandomRessource(){
        Random random=new Random();
        String res="";
        int randomNumber=random.nextInt(5);
        switch(randomNumber){
            case 0 -> res="Clay";
            case 1 -> res="Ore";
            case 2 -> res="Wheat";
            case 3 -> res="Wood";
            case 4 -> res="Wool";
        }
        return res;
    }
}
