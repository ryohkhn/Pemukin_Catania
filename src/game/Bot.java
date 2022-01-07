package game;

import board.Colony;
import board.Road;
import board.Tile;
import jdk.swing.interop.SwingInterOpUtils;
import vue.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
    public void sevenAtDice(int quantity, Game game) {
        boolean verif=false;
        String resource="";
        String[] resources={"Clay","Wood","Wool","Wheat","Ore"};
        while(!verif){
            resource=resources[rand.nextInt(5)];
            if(this.resources.get(resource)>0) {
                game.destroy(this,resource);
                quantity--;
            }
            verif=quantity==0;
        }
    }

    // renvoie un tableau de placement aléatoire pour le voleur
    // dans le cas ou la fonction est appelée après un lancement de carte, il faut enlever l'image du voleur de la case
    public void setThief(Game game){
        int[] placement=new int[2];
        placement[0]=rand.nextInt(4);
        placement[1]=rand.nextInt(4);
        game.setThief(placement);
        if(game.getVue() instanceof GuiSideBar){
            GuiSideBar guiSideBar=(GuiSideBar) game.getVue();
            guiSideBar.botRemoveAndSetThiefInBoard();
        }
    }

    // renvoie un random
    public int getRand(int size) {
        return rand.nextInt(size);
    }

    // Sélectionne une action aléatoire parmi les suivantes sans prendre en compte sa capacité à le faire (ressources/carte deja jouée pendant le tour, etc) :
    // - construire une route/une ville/une colonie.
    // - acheter/jouer une carte.
    // - mettre fin au tour.
    // Nous avons laissé la possibilité au bot (par l'aléatoire) de directement mettre fin a son tour sans rien construire.
    // Ceci a été décidé dans un objectif d'équilibre. En effet, si un bot devait forcément construire quelque chose dans son tour (s'il a les ressources pour),
    // alors il ferait des routes dès que possible (les routes étant les constructions les moins chères du jeu).
    // Nous avons aussi décidé que le choix de l'action serait défini par un nombre entre 0 et 11. Puis attribué ces numéros aux 6 cases.
    // (par exemple les numéros 4,5,6 correspondent tous à la construction d'une ville).
    // Cela a aussi été fait dans un souci d'équilibre pour que les IA produisent des coups "gagnant" en priorité.
    // Les coups gagnants sont les coups rapportant des points de victoire.
    public void getAction( Game game) {
        switch(this.getRand(12)) {
            case 0 -> { // construction d'une route
                this.setBuildableRoads(game);
                if(this.buildableRoads.size()!=0) {
                    if(game.hasResourcesForRoad(this)) {
                        game.buildRoad(this, this.getRoadPlacement());
                    }
                }
                this.getAction(game);
            }
            case 2,3 -> { // construction d'une colonie
                this.setBuildableColonies(game);
                if(this.buildableColonies.size()!=0) {
                    if(game.hasResourcesForColony(this)) {
                        game.buildColony(this, this.getColonyPlacement());
                    }
                }
                this.getAction(game);
            }
            case 4,5,6 -> { // construction d'une ville
                if(!this.isFullCity()) {
                    if(game.hasResourcesForCity(this)) {
                        game.buildCity(this, this.getCityPlacement());
                    }
                }
                this.getAction(game);
            }

            case 7 -> { // acheter une carte
                if(game.hasResourcesForCard(this)) {
                    game.buyCard(this);
                }
                this.getAction(game);
            }
            case 8 -> { // jouer une carte
                if(!this.alreadyPlayedCardThisTurn) {
                    Card chosenCard;
                    if(this.hasCard()) {
                        chosenCard=this.chooseCard();
                        System.out.println(this + "  " + chosenCard);
                        if(chosenCard==Card.ProgressYearOfPlenty) {
                            game.useCardProgressYearOfPlenty(this, this.chooseResource(2));
                        } else if(chosenCard==Card.ProgressMonopoly) {
                            game.useCardProgressMonopoly(this, this.chooseResource(1));
                        } else if(chosenCard==Card.ProgressRoadBuilding) {
                            game.useCardProgressRoadBuilding(this, this.getRoadPlacement());
                            game.useCardProgressRoadBuildingSecondRound(this, this.getRoadPlacement());
                        } else if(chosenCard==Card.Knight) {
                            game.useCardKnight(this, this.getTilePlacement());
                            this.steal(game.getBoard().getThiefTile(),game);
                        }else {
                            game.useCardVP(this);
                        }
                    }
                }
                this.getAction(game);
            }
            case 9,10,11 -> { // mettre fin au tour
                this.cardsDrawnThisTurnReset();
                this.alreadyPlayedCardThisTurn=false;
            }
        }
    }

    // vole les ressources d'une ville/colonie sur la tuile du voleur
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

    }

    // renvoie une case aléatoire
    private int[] getTilePlacement() {
        int[] placement=new int[2];
        placement[0]=rand.nextInt(4);
        placement[1]=rand.nextInt(4);
        return placement;
    }

    //renvoie le placement de la route
    private int[] getRoadPlacement() {
        int[] res=new int[3];
        if(buildableRoads.size()>0) {
            int x=rand.nextInt(buildableRoads.size());

            int i=0;
            for(Map.Entry<Road, int[]> entry : buildableRoads.entrySet()) {
                if(i==x) {
                    res=entry.getValue();
                }
                i++;
            }
        }
        return res;
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

    // renvoie le placement de la ville
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

    // vérifie si le bot n'a que des villes.
    private boolean isFullCity() {
        for(Map.Entry<Colony,int[]> entry: colonies.entrySet()){
            if(!entry.getKey().isCity()) return false;
        }
        return true;
    }

    // choisi une carte aléatoire que le bot possède
    private Card chooseCard() {
        Card chosenCard;
        boolean verify=false;
        do {
            chosenCard=Card.randomCard();
            if(this.cards.get(chosenCard)>this.cardsDrawnThisTurn.getOrDefault(chosenCard, 0)) {
                verify=true;
            }
        }while(!verify);
        return chosenCard;
    }

    // renvoie un tableau de taille i de ressources
    private String[] chooseResource(int i) {
        String[] resources=new String[i];
        String[] resourcesList={"Clay","Wood","Wool","Wheat","Ore"};
        for(int x=0;x<i;x++){
            resources[x]=resourcesList[rand.nextInt(5)];
        }
        return resources;
    }


}
