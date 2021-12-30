package game;

import board.Colony;
import board.Road;
import vue.Cli;
import vue.Vues;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class Controller{
    Launcher launcher;
    private Game game;
    private Scanner scanner=new Scanner(System.in);

    public Controller(Launcher launcher){
        this.launcher=launcher;
    }

    public void chooseNbPlayers() { //launcher of cli version
        if(scanner.nextLine().equals("3")) {
            this.game=launcher.createGame(3);
        } else {
            this.game=launcher.createGame(4);
        }
    }


    public boolean chooseHuman() {
        if(scanner.nextLine().equals("1")) return true;
        return false;
    }
    public boolean getAction(Player p, Vues vue) {
        switch(vue.getAction(p)){
            case 1 -> {
                game.tradeWithPort(p);
                //System.out.println("exchange ressources bank");
                return false;
            }
            case 2 -> {
                game.buildColony(p,vue.getColonyPlacement());
                //System.out.println("build a new colony");
                return false;
            }
            case 3 -> {
                game.buildCity(p);
                //System.out.println("upgrade a colony into a city");
                return false;
            }
            case 4 -> {
                game.buildRoad(p);
                //System.out.println("build a road");
                return false;
            }
            case 5 -> {
                game.buyCard(p);
                //System.out.println("buy development cards");
                return false;
            }
            case 6 -> {
                game.useCard(p);
                //System.out.println("play a development card");
                return false;
            }
            case 7 -> {
                vue.displayPlayer(p);
                return false;
            }
            case 8 -> {
                vue.showBuildCost();
                return false;
            }
            default -> {
                //System.out.println("End of the round.");
                p.alreadyPlayedCardThisTurn=false;
                return true;
            }
        }
    }


    public void getFirstColonyPlacement(Player p, Cli cli,Game game, Boolean secondRound) {
        System.out.println("To build a colony :");
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
            if(colony==null) getFirstColonyPlacement(p,cli,game,secondRound);
            if(secondRound) game.secondRoundBuildedColonies.put(colony,p);
            cli.getFirstRoadPlacement(p,colony,game);
        }catch (InputMismatchException e) {
            System.out.println("the line of the tile should be an Integer between 0-3");
            System.out.println("the column of the tile should be an Integer between 0-3");
            System.out.println("placement-coordinates of the future colony should be an Integer between 0-3");
            this.getFirstColonyPlacement(p,cli,game,secondRound);
        }
    }

    public void getFirstRoadPlacement(Player p, Colony colony){
        System.out.println("To build a road :");
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
            if(!this.game.buildRoadInitialization(p,colony,co)){
                getFirstRoadPlacement(p,colony);
            }

        }catch (InputMismatchException e) {
            System.out.println("the line of the tile should be an Integer between 0-3");
            System.out.println("the column of the tile should be an Integer between 0-3");
            System.out.println("placement-coordinates of the road should be an Integer between 0-3");
            this.getFirstRoadPlacement(p,colony);
        }

    }

    public void setPlayers(){
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
            if(playersType[i].equals("1")){
                boolean verif=false;
                while(!verif) {
                    System.out.println("please choose a color between :" + color.toString());
                    String s=scanner.nextLine();
                    for(Map.Entry<String, Boolean> entry : color.entrySet()) {
                        if(color.replace(s,true,false)) {
                            playersColor[i]=entry.getKey();
                            verif=true;
                        }
                    }
                }
            }
            i++;
        }
        game.setPlayers(playersType);
        game.botsGetColor(color);
    }
}
