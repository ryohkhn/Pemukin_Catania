package game;

import board.*;
import vue.Cli;

import java.util.*;

public class Controller{
    private final Launcher launcher;
    private final Cli cli;
    private Game game;
    private Scanner scanner=new Scanner(System.in);

    public Controller(Launcher launcher, Cli cli){
        this.launcher=launcher;
        this.cli=cli;
    }
// Les fonctions dans Controller sont des fonctions qui font appel à la classe Scanner de la bibliothèque Java
// pour récupérer une réponse du joueur, à propos d'une ressource, de coordonnées, etc

//le joueur :

    // (qui initialise) choisit le nombre de joueurs de la partie
    public void chooseNbPlayers() { //launcher of cli version
        scanner=new Scanner(System.in);
        if(scanner.nextLine().equals("3")) {
            this.game=launcher.createGame(3);
        } else {
            this.game=launcher.createGame(4);
        }
    }

    // (qui initialise) choisit la couleur des joueurs
    public void setPlayers(){
        scanner=new Scanner(System.in);
        HashMap<String, Boolean> color=new HashMap<>();
        color.put("blue", false);
        color.put("yellow", false);
        color.put("green", false);
        color.put("orange", false);
        String[] playersType=new String[game.getPlayers().length];
        String[] playersColor=new String[game.getPlayers().length];
        int i=0;
        for(Player p : game.getPlayers()){
            System.out.println("Type 1 to initialize a human player.\n Else this player will be a bot.");
            playersType[i]=scanner.nextLine();
            boolean verif=false;
            while(!verif) {
                System.out.println("please choose a color between :" + color);
                String s=scanner.nextLine();
                for(Map.Entry<String, Boolean> entry : color.entrySet()) {
                    if(entry.getKey().equals(s)) {
                        if(color.replace(s, false, true)) {
                            playersColor[i]=entry.getKey();
                            verif=true;
                        }
                    }
                }
            }
            i++;
        }
        game.setPlayers(playersType);
        game.setColors(playersColor);
    }

    // choisit son action
    public void getAction(Player player) {
        scanner=new Scanner(System.in);
        try{
            switch(scanner.nextInt()) {
                case 0 -> {
                    cli.displayBoard(this.game);
                    cli.getAction(player);
                }
                case 1 -> {
                    game.trade(player, this.portSelection(player), this.getPortResource(), this.chooseResource(1));
                    cli.getAction(player);
                }
                case 2 -> {
                    if(game.hasResourcesForColony(player)) {
                        game.buildColony(player, this.getCityPlacement());
                    }
                    cli.getAction(player);
                }
                case 3 -> {
                    if(game.hasResourcesForCity(player)) {
                        game.buildCity(player, this.getCityPlacement());
                    }
                    cli.getAction(player);
                }
                case 4 -> {
                    if(game.hasResourcesForRoad(player)) {
                        game.buildRoad(player, this.getRoadPlacement());
                    }
                    cli.getAction(player);
                }
                case 5 -> {
                    if(game.hasResourcesForCard(player)) {
                        game.buyCard(player);
                    }
                    cli.getAction(player);
                }
                case 6 -> {
                    if(player.alreadyPlayedCardThisTurn) {
                        cli.message(player, "error", "card", 3);
                    } else {
                        String card=this.chooseCard();
                        Card chosenCard=Card.valueOf(card);
                        if(player.hasCard()) {
                            if(game.hasChosenCard(player, chosenCard)) {
                                if(card.equals("ProgressYearOfPlenty")) {
                                    game.useCardProgressYearOfPlenty(player, this.chooseResource(2));
                                } else if(card.equals("ProgressMonopoly")) {
                                    game.useCardProgressMonopoly(player, this.chooseResource(1));
                                } else if(card.equals("ProgressRoadBuilding")) {
                                    int[] placement;
                                    boolean verify;
                                    do {
                                        placement=this.getRoadPlacement();
                                        verify=game.isRoadBuildable(placement, player);
                                    } while(!verify);
                                    game.useCardProgressRoadBuilding(player, placement);
                                    verify=false;
                                    do {
                                        placement=this.getRoadPlacement();
                                        verify=game.isRoadBuildable(placement, player);
                                    } while(!verify);
                                    game.useCardProgressRoadBuildingSecondRound(player, placement);
                                } else if(card.equals("Knight")) {
                                    game.useCardKnight(player, this.getThiefPlacement());
                                    cli.steal(player, this.game.getBoard().getThiefTile());
                                } else {
                                    game.useCardVP(player);
                                }
                            }else {
                                cli.message(player, "error", "card", 1);
                            }
                        }else {
                            cli.message(player, "error", "card", 1);
                        }
                    }
                    cli.getAction(player);

                }
                case 7 -> {
                    cli.displayPlayer(player);
                    cli.getAction(player);

                }
                case 8 -> {
                    cli.showBuildCost();
                    cli.getAction(player);
                }
                case 9 -> {
                    cli.displayOtherPlayers(player, game);
                    cli.getAction(player);
                }
                case 10 -> { // end the turn
                    player.cardsDrawnThisTurnReset();
                    player.alreadyPlayedCardThisTurn=false;
                }
                default -> {
                    System.out.println("Please enter an Integer between 0-10");
                    cli.getAction(player);
                }
            }

        }catch (InputMismatchException e) {
            System.out.println("Please enter an Integer between 0-10");
            cli.getAction(player);
        }
    }

    // choisit une carte
    private String chooseCard() {
        scanner=new Scanner(System.in);
        cli.chooseCard();
        String cardInput=scanner.next();
        if(!cardInput.equals("Knight") && !cardInput.equals("VictoryPoint") && !cardInput.equals("ProgressRoadBuilding") && !cardInput.equals("ProgressYearOfPlenty") && !cardInput.equals("ProgressMonopoly")){
            return chooseCard();
        }
        else return cardInput;
    }

    // choisit les coordonnées d'une ville
    private int[] getCityPlacement(){
        scanner=new Scanner(System.in);
        cli.getCityPlacement();
        try{
            System.out.println("Please enter the line of the tile.");
            int line=scanner.nextInt();
            if(line>3 || line<0){
                throw new InputMismatchException();
            }
            System.out.println("Please enter the column of the tile.");
            int column=scanner.nextInt();
            if(column>3 || column<0){
                throw new InputMismatchException();
            }
            System.out.println("Please enter the placement-coordinates of the future city. It is a number between 0-3. 0 is the top-left corner.");
            int cityPosition=scanner.nextInt();
            if(cityPosition<0 || cityPosition>3){
                throw new InputMismatchException();
            }
            int[] co={line,column,cityPosition};
            return co;
        }catch (InputMismatchException e) {
            System.out.println("the line of the tile should be an Integer between 0-3");
            System.out.println("the column of the tile should be an Integer between 0-3");
            System.out.println("placement-coordinates of the future city should be an Integer between 0-3");
            return this.getCityPlacement();
        }
    }

    // choisit les coordonnées d'une route
    private int[] getRoadPlacement() {
        scanner=new Scanner(System.in);
        cli.getRoadPlacement();
        try{
            System.out.println("Please enter the line of the tile.");
            int line=scanner.nextInt();
            if(line>3 || line<0){
                throw new InputMismatchException();
            }
            System.out.println("Please enter the column of the tile.");
            int column=scanner.nextInt();
            if(column>3 || column<0){
                throw new InputMismatchException();
            }
            System.out.println("Please enter the placement-coordinates of the road. It is a number between 0-3. 0 is the top road.");
            int roadPosition=scanner.nextInt();
            if(roadPosition<0 || roadPosition>3){
                throw new InputMismatchException();
            }
            int[] co={line,column,roadPosition};
            return co;
        }catch (InputMismatchException e) {
            System.out.println("the line of the tile should be an Integer between 0-3");
            System.out.println("the column of the tile should be an Integer between 0-3");
            System.out.println("placement-coordinates of the road should be an Integer between 0-3");
            return this.getRoadPlacement();
        }
    }

    // choisit un nombre number de ressources
    public String[] chooseResource(int number) {
        scanner=new Scanner(System.in);
        int compt=0;
        String[] res=new String[number];
        while(compt<number){
            cli.chooseResource();
            String resourceInput=scanner.next();
            if(!resourceInput.equals("Clay") && !resourceInput.equals("Ore") && !resourceInput.equals("Wheat") && !resourceInput.equals("Wood") && !resourceInput.equals("Wool")){
                return chooseResource(number);
            }
            res[compt]=resourceInput;
            compt++;
        }
        return res;
    }

    // choisit la ressource à donner au port
    private String getPortResource() {
        scanner=new Scanner(System.in);
        cli.getPortResource();
        String resourceInput=scanner.next();
        if(!resourceInput.equals("Clay") && !resourceInput.equals("Ore") && !resourceInput.equals("Wheat") && !resourceInput.equals("Wood") && !resourceInput.equals("Wool")){
            return getPortResource();
        }
        return resourceInput;
    }

    // choisit le port avec lequel il veut échanger
    private Port portSelection(Player p) {
        scanner=new Scanner(System.in);
        cli.portSelection(p);
        int compt=p.getPorts().size();
        try{
            int chosenPort=scanner.nextInt();
            if(chosenPort>compt || chosenPort<0){
                throw new InputMismatchException();
            }
            if(chosenPort==0) return null;
            return p.getPorts().get(chosenPort-1);
        }
        catch(InputMismatchException e){
            System.out.println("Vous devez rentrer un chiffre entre 0 et "+compt);
            return this.portSelection(p);
        }
    }

    // choisit l'emplacement de sa premiere et 2ᵉ colonie
    public void getFirstColonyPlacement(Player p,Game game, Boolean secondRound) {

        System.out.println("To build a colony :");
        scanner=new Scanner(System.in);
        try{
            System.out.println("Please enter the line of the tile.");
            int line=scanner.nextInt();
            if(line>3 || line<0){
                throw new InputMismatchException();
            }
            System.out.println("Please enter the column of the tile.");
            int column=scanner.nextInt();
            if(column>3 || column<0){
                throw new InputMismatchException();
            }
            System.out.println("Please enter the placement-coordinates of the future colony. It is a number between 0-3. 0 is the top-left corner.");
            int colonyPosition=scanner.nextInt();
            if(colonyPosition<0 || colonyPosition>3){
                throw new InputMismatchException();
            }
            int[] co={line,column,colonyPosition};
            Colony colony=this.game.buildColonyInitialization(p,co);
            if(colony==null) throw new InputMismatchException();
            if(secondRound) game.secondRoundBuildedColonies.put(colony,p);
            cli.getFirstRoadPlacement(p,colony,game);
        }catch (InputMismatchException e) {
            System.out.println("the line of the tile should be an Integer between 0-3");
            System.out.println("the column of the tile should be an Integer between 0-3");
            System.out.println("placement-coordinates of the future colony should be an Integer between 0-3");
            this.getFirstColonyPlacement(p,game,secondRound);
        }
    }

    // choisit l'emplacement de sa premiere et 2ᵉ route
    public void getFirstRoadPlacement(Player p, Colony colony){
        System.out.println("To build a road :");
        scanner=new Scanner(System.in);
        try{
            System.out.println("Please enter the line of the tile.");
            int line=scanner.nextInt();
            if(line>3 || line<0){
                throw new InputMismatchException();
            }
            System.out.println("Please enter the column of the tile.");
            int column=scanner.nextInt();
            if(column>3 || column<0){
                throw new InputMismatchException();
            }
            System.out.println("Please enter the placement-coordinates of the road. It is a number between 0-3. 0 is the top road.");
            int roadPosition=scanner.nextInt();
            if(roadPosition<0 || roadPosition>3){
                throw new InputMismatchException();
            }
            int[] co={line,column,roadPosition};
            if(!this.game.buildRoadInitialization(p,colony,co)) throw new InputMismatchException();
        }catch (InputMismatchException e) {
            System.out.println("the line of the tile should be an Integer between 0-3");
            System.out.println("the column of the tile should be an Integer between 0-3");
            System.out.println("placement-coordinates of the road should be an Integer between 0-3");
            this.getFirstRoadPlacement(p,colony);
        }

    }

    // choisit une ressource à détruire
    public void destroy(Player p) {
        scanner=new Scanner(System.in);
        String resourceInput=scanner.next();
        if(!resourceInput.equals("Clay") && !resourceInput.equals("Ore") && !resourceInput.equals("Wheat") && !resourceInput.equals("Wood") && !resourceInput.equals("Wool")){
            System.out.println(p +" Choose a resource you want to destroy among \"Clay, Ore, Wheat, Wood, Wool.\" ");
            destroy(p);
        }else {
            String[] res=new String[1];
            res[0]=resourceInput;
            if(p.hasResources(res)) game.destroy(p, resourceInput);
            else {
                System.out.println("You don't have this resource anymore.");
                destroy(p);
            }
        }
    }

    // choisit les coordonnées du voleur et le déplace
    public void setThief(){
        scanner=new Scanner(System.in);
        try{
            System.out.println("Please enter the line of the tile.");
            int line=scanner.nextInt();
            if(line>3 || line<0){
                throw new InputMismatchException();
            }
            System.out.println("Please enter the column of the tile.");
            int column=scanner.nextInt();
            if(column>3 || column<0){
                throw new InputMismatchException();
            }
            int[] co={line,column};
            game.setThief(co);
        }catch (InputMismatchException e) {
            System.out.println("the line of the tile should be an Integer between 0-3");
            System.out.println("the column of the tile should be an Integer between 0-3");
            this.setThief();
        }
    }

    // choisit les coordonnées du voleur (mais retourne son placement)
    public int[] getThiefPlacement() {
        scanner=new Scanner(System.in);
        try{
            System.out.println("Please enter the line of the tile.");
            int line=scanner.nextInt();
            if(line>3 || line<0){
                throw new InputMismatchException();
            }
            System.out.println("Please enter the column of the tile.");
            int column=scanner.nextInt();
            if(column>3 || column<0){
                throw new InputMismatchException();
            }
            int[] co={line,column};
            return co;
        }catch (InputMismatchException e) {
            System.out.println("the line of the tile should be an Integer between 0-3");
            System.out.println("the column of the tile should be an Integer between 0-3");
            return this.getThiefPlacement();
        }
    }

    // choisit un joueur à voler.
    public Player choosePlayerFromColonies(ArrayList<Colony> ownedColonies) {
        scanner=new Scanner(System.in);
        try{
            int i= scanner.nextInt();
            if(i>ownedColonies.size()-1 || i<0){
                throw new InputMismatchException();
            }
            return ownedColonies.get(i).getPlayer();
        }catch (InputMismatchException e) {
            System.out.println("choose a number between 0 and " + (ownedColonies.size()-1));
            return choosePlayerFromColonies(ownedColonies);
        }
    }

    // fonction à part qui appelle juste la fonction de game avec les deux joueurs concernés par le vol
    public void steal(Player p, Player playerOfColony) {
        game.steal(p,playerOfColony);
    }
}
