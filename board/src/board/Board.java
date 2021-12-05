package board;

import java.util.HashMap;
import java.util.LinkedList;
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
                        tiles[x][y].setAsPort();
                        tiles[x][y].port=tmp;
                        tiles[x][y].colonies.get(0).setLinkedPort(tmp);
                        tiles[x][y].colonies.get(1).setLinkedPort(tmp);
                    }
                    else if(y==0 && x%2==0){
                        tiles[x][y].setAsPort();
                        tiles[x][y].port=tmp;
                        tiles[x][y].colonies.get(0).setLinkedPort(tmp);
                        tiles[x][y].colonies.get(3).setLinkedPort(tmp);
                    }
                    else if(x==tiles.length-1 && y%2==0){
                        tiles[x][y].setAsPort();
                        tiles[x][y].port=tmp;
                        tiles[x][y].colonies.get(2).setLinkedPort(tmp);
                        tiles[x][y].colonies.get(3).setLinkedPort(tmp);
                    }
                    else if(y==tiles.length-1 && x%2==1){
                        tiles[x][y].setAsPort();
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
        res.put("Clay",0);
        res.put("Ore",0);
        res.put("Wheat",0);
        res.put("Wood",0);
        res.put("Wool",0);
        return res;
    }

    // fonction qui génère une ressource aléatoire et renvoi son String
    private String generateRandomRessource(){
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
