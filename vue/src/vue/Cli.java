package vue;

import board.Colony;
import board.Port;
import board.Tile;
import game.*;


import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;


public class Cli implements Vues{
    Launcher launcher;
    Controller controller;

    public Cli(Launcher launcher){
        this.launcher=launcher;
        this.controller=new Controller(launcher,this);
    }

    @Override
    public void chooseNbPlayers() { //launcher of cli version
        System.out.println("If you want to be 3 players type 3. \nElse you'll be 4.");
        controller.chooseNbPlayers();
    }

    @Override
    public void chooseResource(){
        System.out.println("Please choose a ressource among : Clay, Ore, Wheat, Wood, Wool.");
    }

    @Override
    public void chooseCard() {
        System.out.println("choose a card among : Knight,VictoryPoint,ProgressRoadBuilding,ProgressYearOfPlenty,ProgressMonopoly;");
    }

    @Override
    public void displayPlayer(Player p) {
        System.out.println("Player     : " + p.toString() + ", Victory Points : " + p.getVictoryPoint() );
        System.out.println("Ressources : " + p.getRessourcesToString());
        System.out.println("Cards      : " + p.getCardsToString());
        System.out.println();

    }

    @Override
    public void displayOtherPlayers(Player p, Game game) {
        System.out.println("Other players informations :");
        for(Player player : game.getPlayers()){
            if(!player.equals(p)){
                System.out.println(player.toString() + " : " +
                        player.getVictoryPoint() + " victory points - " +
                        player.getKnightPlayed() + " knights played - " +
                        player.getNbCards() + " cards.");
            }
        }
    }

    @Override
    public void displayBoard(Game game) {
        // TODO: 27/12/2021 ports qui n'affichent pas les bonnes valeurs régler l'affichage des routes
        Point thief=null;
        Port[] tab=new Port[8];
        int iter=0;
        System.out.println("This board is cut in 4 rows and 4 columns, going from 0 to 3.");
        System.out.println("c=colony, C=city, -=road, 7=desert, the tiles are numbered 1,2,3,4,5,6,7,8,9,A,B,Z (10=A, 11=B,12=Z), port are -a-,-b- etc.");
        System.out.println();
        System.out.println("        a       b");
        for(int i=0;i<4;i++){
            for(int j=0;j<2;j++){
                if(j==0) System.out.print("  ");
                if(j==1){
                    if(i==0) System.out.print("h ");
                    else if(i==2) System.out.print("g ");
                    else System.out.print("  ");
                }
                for(int x=0;x<4;x++){
                    Tile t=game.getBoard().getTiles()[i][x];
                    if(j==0){
                        if(t.isThief()) thief=new Point(i,x);
                        if(t.isPort()) {
                            tab[iter]=t.getPort();
                            iter++;
                        }
                        if(x==0) {
                            System.out.print(t.getColonies().get(0).isCity()?"C ":"c ");
                        }
                        System.out.print("- ");
                        System.out.print(t.getColonies().get(1).isCity()?"C ":"c ");
                    }else{ //j==1
                        String id="";
                        if(t.getId()>9){
                            if(t.getId()==10) id+="A ";
                            if(t.getId()==11) id+="B ";
                            if(t.getId()==12) id+="Z ";
                        }else{
                            id+=String.valueOf(t.getId() + " ");
                        }
                        if(x==0) {
                            System.out.print("- ");
                        }
                        System.out.print(id);
                        System.out.print("- ");
                    }
                }
                if(j==1){
                    if(i==1) System.out.print("z ");
                    else if(i==3) System.out.print("d ");
                }
                System.out.println();
            }
        }
        //derniere ligne
        System.out.print("  ");
        for(int x=0;x<4;x++) {
            Tile t=game.getBoard().getTiles()[3][x];
            if(x==0) {
                System.out.print(t.getColonies().get(3).isCity()?"C ":"c ");
            }
            System.out.print("- ");
            System.out.print(t.getColonies().get(2).isCity()?"C ":"c ");
        }
        System.out.println("\n    e       f\n");
        System.out.println("On this 2nd table, the elements have been replaced by their owner when they have one.\n");
        System.out.println("        a       b");
        for(int i=0;i<4;i++){
            for(int j=0;j<2;j++){
                if(j==0) System.out.print("  ");
                if(j==1){
                    if(i==0) System.out.print("h ");
                    else if(i==2) System.out.print("g ");
                    else System.out.print("  ");
                }
                for(int x=0;x<4;x++){
                    Tile t=game.getBoard().getTiles()[i][x];
                    if(j==0){
                        if(x==0) {
                            if(t.getColonies().get(0).isOwned()) System.out.print(t.getColonies().get(0).getPlayer().firstLetter());
                            else System.out.print(t.getColonies().get(0).isCity()?"C ":"c ");
                        }
                        if(t.getRoads().get(0).isOwned()) System.out.print(t.getRoads().get(0).getPlayer().firstLetter());
                        else System.out.print("- ");
                        if(t.getColonies().get(1).isOwned()) System.out.print(t.getColonies().get(1).getPlayer().firstLetter());
                        else System.out.print(t.getColonies().get(1).isCity()?"C ":"c ");
                    }else{ //j==1
                        String id="";
                        if(t.getId()>9){
                            if(t.getId()==10) id+="A ";
                            if(t.getId()==11) id+="B ";
                            if(t.getId()==12) id+="Z ";
                        }else{
                            id+=String.valueOf(t.getId())+ " ";
                        }
                        if(x==0) {
                            if(t.getRoads().get(3).isOwned()) System.out.print(t.getRoads().get(3).getPlayer().firstLetter());
                            else System.out.print("- ");
                            System.out.print(id);
                            if(t.getRoads().get(1).isOwned()) System.out.print(t.getRoads().get(1).getPlayer().firstLetter());
                            else System.out.print("- ");
                        }else{
                            System.out.print(id);
                            if(t.getRoads().get(1).isOwned()) System.out.print(t.getRoads().get(1).getPlayer().firstLetter());
                            else System.out.print("- ");
                        }
                    }
                }
                if(j==1){
                    if(i==1) System.out.print("z ");
                    else if(i==3) System.out.print("d ");
                }
                System.out.println();
            }
        }
        //derniere ligne
        System.out.print("  ");
        for(int x=0;x<4;x++) {
            Tile t=game.getBoard().getTiles()[3][x];
            if(x==0) {
                if(t.getColonies().get(3).isOwned()) System.out.print(t.getColonies().get(3).getPlayer().firstLetter());
                else System.out.print(t.getColonies().get(3).isCity()?"C ":"c ");
            }
            if(t.getRoads().get(2).isOwned()) System.out.print(t.getRoads().get(2).getPlayer().firstLetter());
            else System.out.print("- ");
            if(t.getColonies().get(2).isOwned()) System.out.print(t.getColonies().get(2).getPlayer().firstLetter());
            else System.out.print(t.getColonies().get(2).isCity()?"C ":"c ");
        }
        System.out.println("\n    f       e\n");
        for(int x=0;x<4;x++){
            for(int y=0;y<4;y++){
                String resource=game.getBoard().getTiles()[x][y].getRessource();
                resource=resource.equals("")?"Desert":resource;
                System.out.print("tile(" + x +"," + y + ")=" + resource +" ");
            }
            System.out.println();
        }
        System.out.println("The thief is on the tile : (" + (int)thief.getX() + "," + (int)thief.getY() + ").");
        System.out.print("   a=" + tab[1].toString() + "   b=" + tab[2].toString() + "   z=" + tab[3].toString() + "   d=" + tab[5].toString()
                + "   e=" + tab[6].toString() + "   f=" + tab[7].toString() + "   g=" + tab[4].toString() + "   h=" + tab[0].toString() );
        System.out.println("\n");

    }

    @Override
    public void displayDiceNumber(int diceNumber) {
        System.out.println("Dices :" + diceNumber);
    }

    @Override
    public void getAction(Player p) {
        if(!p.isBot()) {
            this.displayPlayer(p);
            this.displayBoard(launcher.getGame());
            System.out.println("\nPlease select an action : \n 0 - show board \n 1 - exchange ressources with bank"+
                    " \n 2 - build a new colony\n"+" 3 - upgrade a colony into a city\n"+
                    " 4 - build a road\n"+" 5 - buy development cards\n"+
                    " 6 - play a development card\n"+" 7 - display player information\n"+" 8 - show building costs\n"+
                    " 9 - show others players informations\n"+" 10 - end the round\n");
        }
        controller.getAction(p);
    }

    @Override
    public void getCityPlacement() {
        System.out.println("To build a city/colony please enter their coordinates");
    }

    public void getFirstColonyPlacement(Player p,Game game, boolean secondRound) {
        this.controller.getFirstColonyPlacement(p,game,secondRound);
    }

    public void getFirstRoadPlacement(Player p, Colony colony, Game game){
        this.displayBoard(game);
        this.displayPlayer(p);
        System.out.println("Please select coordinates for your road. She is next to the colony you just achieved.");
        this.controller.getFirstRoadPlacement(p,colony);

    }

    @Override
    public void getPortResource(){
        System.out.println("Please choose the resource (the resource you will give) you want to trade among \"Clay, Ore, Wheat, Wood, Wool.\" ");
    }

    @Override
    public void getRoadPlacement() {
        System.out.println("To build a road please enter their coordinates");
    }

    @Override
    public void initialization(Game game){
        Stack<Player> stack=new Stack<>();
        System.out.println("Phase d'initialisation :\nTous les joueurs construisent deux colonies et deux routes, la deuxième colonie peut se trouver éloignée de la première à condition que la règle de distance soit respectée.");
        for(Player p : game.getPlayers()){
            stack.push(p);
            if(!p.isBot()) {
                this.displayBoard(game);
                this.displayPlayer(p);
                System.out.println("Please select coordinates for your first colony.");
                this.getFirstColonyPlacement(p, game, false);
            }else{
                ((Bot)p).iniBuild(game);
            }
        }
        while(!stack.isEmpty()){
            Player p=stack.pop();
            if(!p.isBot()) {
                this.displayBoard(game);
                this.displayPlayer(p);
                System.out.println("Please select coordinates for your second colony.");
                this.getFirstColonyPlacement(p, game, true);
            }else{
                ((Bot)p).iniBuild(game);
            }
        }
        game.coloniesProduction();
    }

    @Override
    public void portSelection(Player player){
        int compt=1;
        System.out.println("0 - Pas de port : echange en 4:1");
        for(Port port:player.getPorts()){
            System.out.println(compt+" - "+(port.getRate()==2?"Port de ressource spécialisée "+port.getRessource()+", taux de 2:1":"Port sans ressource spécialisée, taux de 3:1"));
            compt++;
        }
        System.out.println("Choisissez un port parmi cette liste pour faire une échange.");
    }

/*
    public Player choosePlayerFromColony(ArrayList<Colony> colonies){
        scanner=new Scanner(System.in);
        int compt=1;
        for(Colony colony:colonies){
            System.out.println(compt+" - "+colony.getPlayer().toString());
            compt++;
        }
        try{
            int choice=scanner.nextInt();
            if(choice<1 || choice>compt){
                throw new InputMismatchException();
            }
            return colonies.get(choice-1).getPlayer();
        }catch(InputMismatchException e){
            System.out.println("The number should be between 1 and "+compt);
            return this.choosePlayerFromColony(colonies);
        }
    }

 */

    @Override
    public void setPlayers() {
        controller.setPlayers();
    }

    @Override
    public void setThief(){
        System.out.println("Please choose a tile to set the thief place.");
        controller.setThief();
    }

    @Override
    public void sevenAtDice(Player p, int quantity){
        if(p.isBot()){
            ((Bot)p).seventAtDice(quantity,this.launcher.getGame());
        }else {
            while(quantity>0) {
                System.out.println(quantity+" to destroy");
                System.out.println("Choose a resource you want to destroy among \"Clay, Ore, Wheat, Wood, Wool.\" ");
                controller.destroy(p);
                quantity--;
            }
        }
    }

    @Override
    public void showBuildCost(){
        System.out.println("Road : 1x Clay, 1x Wood. 0 Victory Point");
        System.out.println("Colony: 1x Clay, 1x Wood, 1x Wheat, 1x Wool. 1 Victory Point");
        System.out.println("City : 2x Wheat, 3x Ore. 2 Victory Points");
        System.out.println("Card : 1x Wool, 1x Wheat, 1x Ore. 0 Victory Point");
    }

    @Override
    public void steal(Player p, Tile thiefTile){
        System.out.println("You can then steal a resource from a player if one of his colonies are on the tile.");
        ArrayList<Colony> ownedColonies=new ArrayList<>();
        for(Colony colony:thiefTile.getColonies()){
            if(colony.getPlayer()!=null && colony.getPlayer()!=p && !ownedColonies.contains(colony)){
                ownedColonies.add(colony);
            }
        }
        Player playerOfColony;
        if(ownedColonies.size()!=0){
            if(ownedColonies.size()>1){
                if(!p.isBot()) {
                    System.out.println("Choose a player to steal the resource from.");
                    for(int i=0; i<ownedColonies.size(); i++) {
                        System.out.println(i+1+" "+ownedColonies.get(i).getPlayer());
                    }
                    System.out.println("choose a number between 1 and "+ownedColonies.size());
                    playerOfColony=controller.choosePlayerFromColonies(ownedColonies, p);
                }else{
                    playerOfColony=ownedColonies.get(((Bot)p).getRand(ownedColonies.size())).getPlayer();
                }
            }
            else{
                playerOfColony=ownedColonies.get(0).getPlayer();
            }
            controller.steal(p,playerOfColony);
        }
    }

    @Override
    public void displayDiceProduction(HashMap<Player, List<String>> diceResultsProduction) {
        for(Player player:diceResultsProduction.keySet()){
            for(String resource:diceResultsProduction.get(player)){
                System.out.println(player+" "+resource);
            }
        }
    }

    @Override
    public void displayStolenResource(Player player,String resource,Player playerOfColony,int quantity){
        System.out.println(player+" stole "+quantity+" "+resource+" from "+playerOfColony);
    }

    @Override
    public void victory(Player p) {
        System.out.println(p.toString() + "has won the game!");
        System.exit(0);
    }
}
