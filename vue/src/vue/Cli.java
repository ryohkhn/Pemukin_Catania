package vue;

import board.Colony;
import board.Port;
import board.Tile;
import game.Controller;
import game.Game;
import game.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;


public class Cli implements Vues{
    Controller controller=new Controller();
    Scanner scanner;

    @Override
    public int chooseNbPlayers() { //launcher of cli version
        System.out.println("If you want to be 3 players type 3. \nElse you'll be 4.");
        controller.chooseNbPlayers();
        scanner=new Scanner(System.in);
        if(scanner.nextLine().equals("3")) {
            return 3;
        } else {
            return 4;
        }
    }

    @Override
    public boolean chooseHuman() {
        System.out.println("Type 1 to initialize a human player.\n Else this player will be a bot.");
        scanner=new Scanner(System.in);
        if(scanner.nextLine().equals("1")) return true;
        return false;
    }

    @Override
    public String chooseColor(HashMap<String, Boolean> color) {
        System.out.println("choose a color between :"+color.toString());
        scanner=new Scanner(System.in);
        return scanner.next();
    }

    @Override
    public int getAction(Player p) {
        System.out.println("\nPlease select an action : \n 1 - exchange ressources with bank"+
                " \n 2 - build a new colony\n"+" 3 - upgrade a colony into a city\n"+
                " 4 - build a road\n"+" 5 - buy development cards\n"+
                " 6 - play a development card\n"+" 7 - display player information\n"+" 8 - show building costs\n"+" else - end the round\n");
        scanner=new Scanner(System.in);
        switch(scanner.next()) {
            case "1":
                System.out.println("trade resources with port");
                return 1;

            case "2":
                System.out.println("build a new colony");
                return 2;

            case "3":
                System.out.println("upgrade a colony into a city");
                return 3;

            case "4":
                System.out.println("build a road");
                return 4;

            case "5":
                System.out.println("buy development cards");
                return 5;

            case "6":
                System.out.println("play a development card");
                return 6;
            case "7":
                System.out.println("display player information");
                return 7;
            case "8":
                System.out.println("show building costs");
                return 8;
            default:
                System.out.println("End of the round.");
                return 9;
        }
    }

    @Override
    public String[] ressourceToBeDiscarded(Player player,int quantity) {
        scanner=new Scanner(System.in);
        System.out.println("Choose "+quantity+" resources you need to discard among : Clay, Ore, Wheat, Wood, Wool");
        System.out.println("Resources of "+player+" :");
        System.out.println(player.getRessourcesToString());
        String[] choosedResources=new String[quantity];
        int compt=0;
        while(compt<quantity){
            try{
                System.out.println("Enter the resource number "+(compt+1)+" you want to discard.");
                String ressource=scanner.next();
                if(!ressource.equals("Clay") && !ressource.equals("Ore") && !ressource.equals("Wheat") && !ressource.equals("Wood") && !ressource.equals("Wool")){
                    return ressourceToBeDiscarded(player, quantity);
                }
                choosedResources[compt]=ressource;
            } catch(InputMismatchException e){
                System.out.println("You need to input the resources you want to discard among : Clay, Ore, Wheat, Wood, Wool");
                return ressourceToBeDiscarded(player, quantity);
            }
            compt++;
        }
        return choosedResources;
    }


    @Override
    public int[] getRoadPlacement() {
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
            return co;
        }catch (InputMismatchException e) {
            System.out.println("the line of the tile should be an Integer between 0-3");
            System.out.println("the column of the tile should be an Integer between 0-3");
            System.out.println("placement-coordinates of the road should be an Integer between 0-3");
            return this.getRoadPlacement();
        }
    }

    @Override
    public int[] getColonyPlacement() {
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
            return co;
        }catch (InputMismatchException e) {
            System.out.println("the line of the tile should be an Integer between 0-3");
            System.out.println("the column of the tile should be an Integer between 0-3");
            System.out.println("placement-coordinates of the future colony should be an Integer between 0-3");
            return this.getColonyPlacement();
        }
    }

    @Override
    public int[] getCityPlacement() {
        System.out.println("To build a city :");
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
            System.out.println("Please enter the placement-coordinates of the future city. It is a number between 0-3. 0 is the top-left corner.");
            int cityPosition=scanner.nextInt();
            int[] co={line,column,cityPosition};
            if(cityPosition<0 || cityPosition>3){
                throw new InputMismatchException();
            }
            return co;
        }catch (InputMismatchException e) {
            System.out.println("the line of the tile should be an Integer between 0-3");
            System.out.println("the column of the tile should be an Integer between 0-3");
            System.out.println("placement-coordinates of the future city should be an Integer between 0-3");
            return this.getColonyPlacement();
        }
    }

    public String[] chooseResource(int number){
        int compt=0;
        String[] res=new String[number];
        scanner = new Scanner(System.in);
        String ressources="Clay, Ore, Wheat, Wood, Wool.";
        while(compt<number){
            System.out.println("Choose the resource number "+(compt+1)+" among : "+ressources);
            String resourceInput=scanner.next();
            if(!resourceInput.equals("Clay") && !resourceInput.equals("Ore") && !resourceInput.equals("Wheat") && !resourceInput.equals("Wood") && !resourceInput.equals("Wool")){
                return chooseResource(number);
            }
            res[compt]=resourceInput;
            compt++;
        }
        return res;
    }

    @Override
    public String chooseCard() {
        scanner=new Scanner(System.in);
        System.out.println("choose a card among : Knigth,VictoryPoint,ProgressRoadBuilding,ProgressYearOfPlenty,ProgressMonopoly;");
        String cardInput=scanner.next();
        if(!cardInput.equals("Knigth") && !cardInput.equals("VictoryPoint") && !cardInput.equals("ProgressRoadBuilding") && !cardInput.equals("ProgressYearOfPlenty") && !cardInput.equals("ProgressMonopoly")){
            return chooseCard();
        }
        else return cardInput;
    }

    @Override
    public void victory(Player p) {
        System.out.println(p.toString() + "has won the game!");
        System.exit(0);
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
                            id+=String.valueOf(t.getId() + " ");
                        }
                        if(x==0) {
                            System.out.print("- ");
                            System.out.print(id);
                            System.out.print("- ");
                        }else{
                            System.out.print(id);
                            System.out.print("- ");
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
        System.out.println("\n    e       f\n");
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
    public void displayPlayer(Player p) {
        System.out.println("Player     : " + p.getColor() + ", Victory Points : " + p.getVictoryPoint() );
        System.out.println("Ressources : " + p.getRessourcesToString());
        System.out.println("Cards      : " + p.getCardsToString());
        System.out.println();

    }

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

    @Override
    public int portSelection(Player player){
        scanner=new Scanner(System.in);
        int compt=1;
        for(Port port:player.getPorts()){
            System.out.println(compt+" - "+(port.getRate()==2?"Port de ressource spécialisée "+port.getRessource()+", taux de 2:1":"Port sans ressource spécialisée, taux de 3:1"));
            compt++;
        }
        System.out.println("Choisissez un port parmi cette liste pour faire une échange.");
        try{
            int choosedPort=scanner.nextInt();
            if(choosedPort>compt || choosedPort<1){
                throw new InputMismatchException();
            }
            return choosedPort-1;
        }
        catch(InputMismatchException e){
            System.out.println("Vous devez rentrer un chiffre entre 1 et "+compt);
            return this.portSelection(player);
        }
    }

    @Override
    public void displayDiceNumber(int diceNumber) {
        System.out.println("Dices = " + diceNumber);
    }

    @Override
    public int[] getThiefPlacement(){
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

    public static void printBlankLines(){
        for(int i=0; i<50; i++){
            System.out.println();
        }
    }

    public void showBuildCost(){
        System.out.println("Road : 1x Clay, 1x Wood. 0 Victory Point");
        System.out.println("Colony: 1x Clay, 1x Wood, 1x Wheat, 1x Wool. 1 Victory Point");
        System.out.println("City : 2x Wheat, 3x Ore. 2 Victory Points");
        System.out.println("Card : 1x Wool, 1x Wheat, 1x Ore. 0 Victory Point");
    }

    @Override
    public void choosePlayers(){
        System.out.println("If you want to be 3 players type 3. \nElse you'll be 4.");
    }
}
