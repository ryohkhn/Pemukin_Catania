package vue;
import game.*;
import board.*;

import java.awt.*;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Random;
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
    public String ressourceToBeDefaussed() { // TODO: 22/12/2021 pas bon : Tous les joueurs comptent les cartes matières premières qu’ils ont en main. Ceux qui en possèdent plus de 7 doivent en choisir la moitié et s’en défausser. On arrondit toujours à l’entier inférieur : celui qui possède 9 cartes matières premières doit en défausser 4.
        scanner=new Scanner(System.in);
        System.out.println("choose a resource whose quantity you own will be divided by 2 among : Clay, Ore, Wheat, Wood, Wool");
        try{
            String x= scanner.next();
            if(!x.equals("Clay") && !x.equals("Ore") && !x.equals("Wheat") && !x.equals("Wood") && !x.equals("Wool")){
                return ressourceToBeDefaussed();
            }
            return x;
        }catch (InputMismatchException e) {
            return ressourceToBeDefaussed();
        }
    }

    @Override
    public int[] getRoadPlacement() { // TODO: 22/12/2021 verifier si indexoutofbounds
        scanner=new Scanner(System.in);
        try{
            System.out.println("Please enter the X-coordinates of the tile.");
            int x=scanner.nextInt();
            System.out.println("Please enter the Y-coordinates of the tile.");
            int y=scanner.nextInt();
            System.out.println("Please enter the placement-coordinates of the road. It is a number between 0-3. 0 is the top road.");
            int z=scanner.nextInt();
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
    public void initialisation() { // TODO: 21/12/2021 méthode pour initialiser la partie (2 routes 2 colonies par joueurs)
    }

    @Override
    public int[] getColonyPlacement() { // TODO: 22/12/2021 verifier si indexoutofbounds
        scanner=new Scanner(System.in);
        try{
            System.out.println("Please enter the X-coordinates of the tile.");
            int x=scanner.nextInt();
            System.out.println("Please enter the Y-coordinates of the tile.");
            int y=scanner.nextInt();
            System.out.println("Please enter the placement-coordinates of the future colony. It is a number between 0-3. 0 is the top-left corner.");
            int z=scanner.nextInt();
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
    public int[] getCityPlacement() { // TODO: 22/12/2021 verifier si indexoutofbounds
        scanner=new Scanner(System.in);
        try{
            System.out.println("Please enter the X-coordinates of the tile.");
            int x=scanner.nextInt();
            System.out.println("Please enter the Y-coordinates of the tile.");
            int y=scanner.nextInt();
            System.out.println("Please enter the placement-coordinates of the future city. It is a number between 0-3. 0 is the top-left corner.");
            int z=scanner.nextInt();
            int[] co={x,y,z};
            return co;
        }catch (InputMismatchException e) {
            System.out.println("x-coordinates of the tile should be an Integer between 0-3");
            System.out.println("y-coordinates of the tile should be an Integer between 0-3");
            System.out.println("placement-coordinates of the future city should be an Integer between 0-3");
            return this.getColonyPlacement();
        }
    }

    @Override
    public String[] choose2Ressources() {
        scanner=new Scanner(System.in);
        String[] ressources={"Clay", "Ore", "Wheat", "Wood", "Wool"};
        System.out.println("choose a resource among :" + ressources.toString());
        String x=scanner.next();
        if(!x.equals("Clay") && !x.equals("Ore") && !x.equals("Wheat") && !x.equals("Wood") && !x.equals("Wool")){
            return choose2Ressources();
        }
        for(int i=0;i<5;i++){
            if(ressources[i].equals(x)) ressources[i]=null;
        }
        System.out.println("choose a resource among :" + ressources.toString());
        String x2=scanner.next();
        if(!x2.equals("Clay") && !x2.equals("Ore") && !x2.equals("Wheat") && !x2.equals("Wood") && !x2.equals("Wool")){
            return choose2Ressources();
        }
        String[] res={x,x2};
        return res;
    }

    @Override
    public String chooseCard() {
        scanner=new Scanner(System.in);
        System.out.println("choose a resource among : Knigth,VictoryPoint,ProgressRoadBuilding,ProgressYearOfPlenty,ProgressMonopoly;");
        String x=scanner.next();
        if(!x.equals("Knigth") && !x.equals("VictoryPoint") && !x.equals("ProgressRoadBuilding") && !x.equals("ProgressYearOfPlenty") && !x.equals("ProgressMonopoly")){
            return chooseCard();
        }
        else return x;
    }

    @Override
    public String choose1Ressource() {
        scanner=new Scanner(System.in);
        String[] ressources={"Clay", "Ore", "Wheat", "Wood", "Wool"};
        System.out.println("choose a resource among :" + ressources.toString());
        String x=scanner.next();
        if(!x.equals("Clay") && !x.equals("Ore") && !x.equals("Wheat") && !x.equals("Wood") && !x.equals("Wool")){
            return choose1Ressource();
        }
        return x;
    }

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
        System.out.println("c=colony, C=city, -=road, 7=desert, the tiles are numbered 1,2,3,4,5,6,7,8,9,A,B,Z (10=A, 11=B,12=C), port are -a-,-b- etc.");
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


}
