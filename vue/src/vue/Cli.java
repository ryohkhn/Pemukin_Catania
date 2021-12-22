package vue;
import game.*;

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
    public String ressourceToBeDefaussed() {
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
    public int[] getRoadPlacement() {
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
    public int[] getColonyPlacement() {
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
    public int[] getCityPlacement() {
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


}
