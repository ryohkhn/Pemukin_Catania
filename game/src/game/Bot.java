package game;

import board.Colony;
import board.Tile;

import java.util.Random;

public class Bot extends Player{
    private Random rand=new Random();
    public Bot(String color){
        super(color);
    }

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
                game.buildColony(this, this.getColonyPlacement());
                this.getAction(game);
            }
            case 1 -> {
                game.buildCity(this, this.getCityPlacement());
                this.getAction(game);
            }
            case 2 -> {
                game.buildRoad(this, this.getRoadPlacement());
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
                this.alreadyPlayedCardThisTurn=false;
            }
        }
    }

    private int[] getColonyPlacement() {

        return null;
    }

    private int[] getCityPlacement() {
        return null;
    }

    private int[] getRoadPlacement() {
        return null;
    }

    private Card chooseCard() {
        Card chosenCard;
        do {
            chosenCard=Card.randomCard();
        }while(this.cards.get(chosenCard)>0);
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
