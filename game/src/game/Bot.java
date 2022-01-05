package game;

import board.Colony;
import board.Tile;

import java.util.*;


import board.*;

public class Bot extends Player{
    private final Random rand=new Random();
    public final HashMap<Colony,int[]> colonies=new HashMap<>();
    public final HashMap<Colony,int[]> buildableColonies=new HashMap<>();
    public final HashMap<Road,int[]> buildableRoads=new HashMap<>();


    public Bot(String color){
        super(color);
    }

    @Override
    public boolean isBot(){
        return true;
    }

    // Méthode qui crée une colonie et une route d'un bot lors d'un round de l'initialisation.
    public void iniBuild(Game game){
        Colony c;
        int[] placement=new int[3];
        boolean verify=false;
        do{
            placement[0]=rand.nextInt(4);
            placement[1]=rand.nextInt(4);
            placement[2]=rand.nextInt(4);
            c=game.buildColonyInitialization(this,placement);
            if(c!=null){
                verify=true;
            }
        }while(!verify);
        verify=false;
        do{
            placement[2]=rand.nextInt(4);
            verify=game.buildRoadInitialization(this,c,placement);
        }while(!verify);
    }

    //supprime un nombre = quantity de ressources aléatoires
    public void seventAtDice(int quantity, Game game) {
        boolean verif=false;
        String resource="";
        String[] resources={"Clay","Wood","Wool","Wheat","Ore"};
        while(!verif){
            resource=resources[rand.nextInt(4)];
            if(this.resources.get(resource)>0) {
                game.destroy(this,resource);
                quantity--;
            }
            verif=quantity==0;
        }
    }

    public void setThief(Game game) {
        int[] placement=new int[2];
        boolean verify=false;
        placement[0]=rand.nextInt(4);
        placement[1]=rand.nextInt(4);
        game.setThief(placement);
    }

    public int getRand(int size) {
        return rand.nextInt(size);
    }

    public void getAction( Game game) {
        switch(this.getRand(6)) {
            case 0 -> {
                this.setBuildableColonies(game);
                if(this.buildableColonies.size()!=0) {
                    game.buildColony(this, this.getColonyPlacement());
                }
                this.getAction(game);
            }
            case 1 -> {
                if(!this.isFullCity()) {
                    game.buildCity(this, this.getCityPlacement());
                }
                this.getAction(game);
            }
            case 2 -> {
                this.setBuildableRoads(game);
                if(this.buildableRoads.size()!=0) {
                    game.buildRoad(this, this.getRoadPlacement());
                }
                this.getAction(game);
            }
            case 3 -> {
                game.buyCard(this);
                this.getAction(game);
            }
            case 4 -> {
                if(!this.alreadyPlayedCardThisTurn) {
                    Card chosenCard;
                    if(!this.hasCard()) this.getAction(game);
                    chosenCard=this.chooseCard();
                    if(chosenCard.toString().equals("ProgressYearOfPlenty")) {
                        game.useCardProgressYearOfPlenty(this, this.chooseResource(2));
                    } else if(chosenCard.toString().equals("ProgressMonopoly")) {
                        game.useCardProgressMonopoly(this, this.chooseResource(1));
                    } else if(chosenCard.toString().equals("ProgressRoadBuilding")) {
                        game.useCardProgressRoadBuilding(this, this.getRoadPlacement());
                        game.useCardProgressRoadBuildingSecondRound(this,this.getRoadPlacement());
                    } else if(chosenCard.toString().equals("Knight")) {
                        game.useCardKnight(this, this.getTilePlacement());
                        Tile t=game.getBoard().getThiefTile();
                        Colony c;
                        do{
                            c=t.getColonies().get(rand.nextInt(3));
                        }while(c.isOwned() && c.getPlayer()!=this);
                        game.steal(this,c.getPlayer());
                    } else {
                        game.useCardVP(this);
                    }
                }
                this.getAction(game);
            }
            case 5 -> { // end the turn
                this.cardsDrawnThisTurn.clear();
                this.alreadyPlayedCardThisTurn=false;
            }
        }
    }

    public void steal(Tile thiefTile, Game game){
        ArrayList<Colony> ownedColonies=new ArrayList<>();
        for(Colony colony:thiefTile.getColonies()){
            if(colony.getPlayer()!=null && colony.getPlayer()!=this && !ownedColonies.contains(colony)){
                ownedColonies.add(colony);
            }
        }
        Player playerOfColony;
        if(ownedColonies.size()!=0){
            playerOfColony=ownedColonies.get(this.getRand(ownedColonies.size())).getPlayer();
            game.steal(this,playerOfColony);
        }
    }
    //fait une hashmap avec toutes les routes constructibles
    private void setBuildableRoads(Game game) {
        for(int x=0;x<3;x++){
            for(int y=0;y<3;y++){
                for(int i=0;i<3;i++){
                    if(game.getBoard().getTiles()[x][y].getRoads().get(i).isBuildable(this)){
                        int[] placement={x,y,i};
                        this.buildableRoads.put(game.getBoard().getTiles()[x][y].getRoads().get(i),placement);
                    }
                }
            }
        }
        if(buildableRoads.size()>=15) buildableRoads.clear(); //max 5 routes
    }

    //renvoie le placement de la route
    private int[] getRoadPlacement() {
        int x=rand.nextInt(buildableRoads.size());
        int[] res=new int[3];
        int i=0;
        for(Map.Entry<Road,int[]> entry : buildableRoads.entrySet()){
            if(i==x){
                res= entry.getValue();
            }
            i++;
        }
        return res;
    }
    //fait une hashmap avec toutes les colonies constructibles
    private void setBuildableColonies(Game game) {
        for(int x=0;x<3;x++){
            for(int y=0;y<3;y++){
                for(int i=0;i<3;i++){
                    if(game.getBoard().getTiles()[x][y].getColonies().get(i).isBuildable(this)){
                        int[] placement={x,y,i};
                        this.buildableColonies.put(game.getBoard().getTiles()[x][y].getColonies().get(i),placement);
                    }
                }
            }
        }
        if(buildableColonies.size()>=5) buildableColonies.clear(); //max 5 colonies
    }

    // renvoie le placement de la colonie
    private int[] getColonyPlacement() {
        int x=rand.nextInt(buildableColonies.size());
        int[] res=new int[3];
        int i=0;
        for(Map.Entry<Colony,int[]> entry : buildableColonies.entrySet()){
            if(i==x){
                res= entry.getValue();
            }
            i++;
        }
        return res;
    }

    private int[] getCityPlacement() {
        int x;
        boolean verify=false;
        int[] res=new int[3];
        do {
            x=rand.nextInt(colonies.size());
            int i=0;
            for(Map.Entry<Colony,int[]> entry : colonies.entrySet()){
                if(i==x){
                    if(!entry.getKey().isCity()){
                        verify=true;
                        res=entry.getValue();
                    }
                }
            }
        } while(!verify);
        return res;
    }

    private boolean isFullCity() {
        for(Map.Entry<Colony,int[]> entry: colonies.entrySet()){
            if(!entry.getKey().isCity()) return false;
        }
        return true;
    }

    private Card chooseCard() {
        Card chosenCard;
        do {
            chosenCard=Card.randomCard();
        }while(this.cards.get(chosenCard)>this.cardsDrawnThisTurn.get(chosenCard));
        return chosenCard;
    }

    private String[] chooseResource(int i) {
        String[] resources=new String[i];
        String[] resourcesList={"Clay","Wood","Wool","Wheat","Ore"};
        for(int x=0;x<i;x++){
            resources[i]=resourcesList[rand.nextInt(5)];
        }
        return resources;
    }

    private int[] getTilePlacement() {
        int[] placement=new int[2];
        placement[0]=rand.nextInt(4);
        placement[1]=rand.nextInt(4);
        return placement;
    }
}
