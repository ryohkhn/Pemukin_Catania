package vue;

import board.Colony;
import board.Port;
import board.Tile;
import game.Game;
import game.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;


public class Cli implements Vues{
    Scanner scanner;
    Game game;

    @Override
    public int chooseNbPlayers() { //launcher of cli version
        System.out.println("If you want to be 3 players type 3. \nElse you'll be 4.");
        scanner=new Scanner(System.in);
        if(scanner.next().equals("3")) {
            return 3;
        } else {
            return 4;
        }
    }
    @Override
    public boolean chooseHuman() {
        System.out.println("Type 1 to initialize a human player.\n Else this player will be a bot.");
        scanner=new Scanner(System.in);
        if(scanner.next().equals("1")) return true;
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
        System.out.println("Please select an action : \n -exchange ressources with bank=1"+
                " \n -build a new colony=2 \n"+" -upgrade a colony into a city=3 \n"+
                " -build a road=4 \n"+" -buy development cards=5 \n"+
                " -play a development card=6 \n"+" -End the round=else");
        scanner=new Scanner(System.in);
        switch(scanner.next()) {
            case "1":
                System.out.println("exchange resources bank");
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

            default:
                System.out.println("End of the round.");
                return 7;

        }
    }

    @Override
    public String[] ressourceToBeDiscarded(Player player,int quantity) {
        scanner=new Scanner(System.in);
        System.out.println(player.toString());
        System.out.println("Choose "+quantity+" resources you need to discard among : Clay, Ore, Wheat, Wood, Wool");
        String[] choosedResources=new String[quantity];
        int compt=0;
        while(compt<quantity){
            try{
                String ressource=scanner.next();
                if(!ressource.equals("Clay") && !ressource.equals("Ore") && !ressource.equals("Wheat") && !ressource.equals("Wood") && !ressource.equals("Wool")){
                    return ressourceToBeDiscarded(player, quantity);
                }
                choosedResources[compt]=ressource;
            } catch(InputMismatchException e){
                System.out.println("You need to input the ressources you want to discard among : Clay, Ore, Wheat, Wood, Wool");
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
            System.out.println("Please enter the X-coordinates of the tile.");
            int x=scanner.nextInt();
            if(x>3 || x<0){
                throw new InputMismatchException();
            }
            System.out.println("Please enter the Y-coordinates of the tile.");
            int y=scanner.nextInt();
            if(y>3 || y<0){
                throw new InputMismatchException();
            }
            System.out.println("Please enter the placement-coordinates of the road. It is a number between 0-3. 0 is the top road.");
            int z=scanner.nextInt();
            if(z<0 || z>3){
                throw new InputMismatchException();
            }
            int[] co={x,y,z};
            return co;
        }catch (InputMismatchException e) {
            System.out.println("x-coordinates of the tile should be an Integer between 0-3");
            System.out.println("y-coordinates of the tile should be an Integer between 0-3");
            System.out.println("placement-coordinates of the road should be an Integer between 0-3");
            return this.getRoadPlacement();
        }
    }

    @Override
    public int[] getColonyPlacement() {
        System.out.println("To build a colony :");
        scanner=new Scanner(System.in);
        try{
            System.out.println("Please enter the X-coordinates of the tile.");
            int x=scanner.nextInt();
            if(x>3 || x<0){
                throw new InputMismatchException();
            }
            System.out.println("Please enter the Y-coordinates of the tile.");
            int y=scanner.nextInt();
            if(y>3 || y<0){
                throw new InputMismatchException();
            }
            System.out.println("Please enter the placement-coordinates of the future colony. It is a number between 0-3. 0 is the top-left corner.");
            int z=scanner.nextInt();
            if(z<0 || z>3){
                throw new InputMismatchException();
            }
            int[] co={x,y,z};
            return co;
        }catch (InputMismatchException e) {
            System.out.println("x-coordinates of the tile should be an Integer between 0-3");
            System.out.println("y-coordinates of the tile should be an Integer between 0-3");
            System.out.println("placement-coordinates of the future colony should be an Integer between 0-3");
            return this.getColonyPlacement();
        }
    }
    @Override
    public int[] getCityPlacement() {
        System.out.println("To build a city :");
        scanner=new Scanner(System.in);
        try{
            System.out.println("Please enter the X-coordinates of the tile.");
            int x=scanner.nextInt();
            if(x>3 || x<0){
                throw new InputMismatchException();
            }
            System.out.println("Please enter the Y-coordinates of the tile.");
            int y=scanner.nextInt();
            if(y>3 || y<0){
                throw new InputMismatchException();
            }
            System.out.println("Please enter the placement-coordinates of the future city. It is a number between 0-3. 0 is the top-left corner.");
            int z=scanner.nextInt();
            int[] co={x,y,z};
            if(z<0 || z>3){
                throw new InputMismatchException();
            }
            return co;
        }catch (InputMismatchException e) {
            System.out.println("x-coordinates of the tile should be an Integer between 0-3");
            System.out.println("y-coordinates of the tile should be an Integer between 0-3");
            System.out.println("placement-coordinates of the future city should be an Integer between 0-3");
            return this.getColonyPlacement();
        }
    }

    /*@Override
    public String[] choose2Resources() {
        scanner=new Scanner(System.in);
        String ressources="Clay,Ore,Wheat,Wood,Wool";
        System.out.println("Choose the first resource among : "+ressources);
        String x=scanner.next();
        if(!x.equals("Clay") && !x.equals("Ore") && !x.equals("Wheat") && !x.equals("Wood") && !x.equals("Wool")){
            return choose2Resources();
        }
        System.out.println("Choose the second resource among : " + ressources);
        String x2=scanner.next();
        if(!x2.equals("Clay") && !x2.equals("Ore") && !x2.equals("Wheat") && !x2.equals("Wood") && !x2.equals("Wool")){
            return choose2Resources();
        }
        String[] res={x,x2};
        return res;
    }
     */

    public String[] chooseResource(int number){
        int compt=0;
        String[] res=new String[number];
        scanner = new Scanner(System.in);
        String ressources="Clay,Ore,Wheat,Wood,Wool";
        while(compt<number){
            System.out.println("Choose the resource number"+compt+" among : "+ressources);
            String x=scanner.next();
            if(!x.equals("Clay") && !x.equals("Ore") && !x.equals("Wheat") && !x.equals("Wood") && !x.equals("Wool")){
                return chooseResource(number);
            }
            res[compt]=x;
            compt++;
        }
        return res;
    }

    @Override
    public String chooseCard() {
        scanner=new Scanner(System.in);
        System.out.println("choose a card among : Knigth,VictoryPoint,ProgressRoadBuilding,ProgressYearOfPlenty,ProgressMonopoly;");
        String x=scanner.next();
        if(!x.equals("Knigth") && !x.equals("VictoryPoint") && !x.equals("ProgressRoadBuilding") && !x.equals("ProgressYearOfPlenty") && !x.equals("ProgressMonopoly")){
            return chooseCard();
        }
        else return x;
    }

    // j'ai ajouté une méthode générale au lieu de devoir refaire la fonction pour chaque quantité de ressource
    // je sais pas si la méthode que j'ai fait fonctionne je peux pas tester donc je laisse les méthodes qu'elle remplace en commentaire au cas ou
    /*@Override
    public String choose1Resource() {
        scanner=new Scanner(System.in);
        String ressources="Clay,Ore,Wheat,Wood,Wool";
        System.out.println("Choose a resource among :" + ressources);
        String x=scanner.next();
        if(!x.equals("Clay") && !x.equals("Ore") && !x.equals("Wheat") && !x.equals("Wood") && !x.equals("Wool")){
            return choose1Resource();
        }
        return x;
    }
     */

    @Override
    public void victory(Player p) {
        System.out.println(p.toString() + "has won the game!");
    }

    @Override
    public void displayBoard(Game game) {
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
                System.out.print("tile(" + x +"," + y + ")=" + game.getBoard().getTiles()[x][y].getRessource());
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
    public Port portSelection(Player player){
        scanner=new Scanner(System.in);
        int compt=1;
        for(Port port:player.getPorts()){
            System.out.println(compt+" - "+(port.getRate()==2?"Port de ressource spécialisée "+port.getRessource():"Port sans ressource spécialisée"));
            compt++;
        }
        System.out.println("Choisissez un port parmi cette liste pour faire une échange.");
        try{
            int choosedPort=scanner.nextInt();
            return player.getPorts().get(compt-1);
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
            System.out.println("Please enter the X-coordinates of the tile.");
            int x=scanner.nextInt();
            if(x>3 || x<0){
                throw new InputMismatchException();
            }
            System.out.println("Please enter the Y-coordinates of the tile.");
            int y=scanner.nextInt();
            if(y>3 || y<0){
                throw new InputMismatchException();
            }
            int[] co={x,y};
            return co;
        }catch (InputMismatchException e) {
            System.out.println("x-coordinates of the tile should be an Integer between 0-3");
            System.out.println("y-coordinates of the tile should be an Integer between 0-3");
            return this.getThiefPlacement();
        }
    }
}
