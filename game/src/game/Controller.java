package game;

import board.*;
import vue.Cli;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class Controller{
    private Launcher launcher;
    private Cli cli;
    private Game game;
    private Scanner scanner=new Scanner(System.in);

    public Controller(Launcher launcher, Cli cli){
        this.launcher=launcher;
        this.cli=cli;
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

    public void getAction(Player p) {
        switch(scanner.nextInt()){
            case 1 -> {
                game.trade(p, this.portSelection(p), this.getPortResource(), this.chooseResource(1));
                cli.getAction(p);
            }
            case 2 -> {
                game.buildColony(p,cli.getColonyPlacement());
                cli.getAction(p);
            }
            case 3 -> {
                game.buildCity(p);
                cli.getAction(p);
            }
            case 4 -> {
                game.buildRoad(p);
                cli.getAction(p);
            }
            case 5 -> {
                game.buyCard(p);
                cli.getAction(p);
            }
            case 6 -> {
                game.useCard(p);
                cli.getAction(p);

            }
            case 7 -> {
                cli.displayPlayer(p);
                cli.getAction(p);

            }
            case 8 -> {
                cli.showBuildCost();
                cli.getAction(p);
            }
            case 9 -> {
                p.alreadyPlayedCardThisTurn=false;
            }
            default -> {
                System.out.println("Please enter an Integer between 1-9");
                cli.getAction(p);
            }
        }
    }

    private String[] chooseResource(int number) {
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

    private String getPortResource() {
        cli.getPortResource();
        String resourceInput=scanner.next();
        if(!resourceInput.equals("Clay") && !resourceInput.equals("Ore") && !resourceInput.equals("Wheat") && !resourceInput.equals("Wood") && !resourceInput.equals("Wool")){
            return getPortResource();
        }
        return resourceInput;
    }

    private Port portSelection(Player p) {
        cli.portSelection(p);
        int compt=p.getPorts().size();
        try{
            int choosedPort=scanner.nextInt();
            if(choosedPort>compt || choosedPort<0){
                throw new InputMismatchException();
            }
            if(choosedPort==0) return null;
            return p.getPorts().get(choosedPort-1);
        }
        catch(InputMismatchException e){
            System.out.println("Vous devez rentrer un chiffre entre 0 et "+compt);
            return this.portSelection(p);
        }
    }


    public void getFirstColonyPlacement(Player p,Game game, Boolean secondRound) {
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
            if(colony==null) getFirstColonyPlacement(p,game,secondRound);
            if(secondRound) game.secondRoundBuildedColonies.put(colony,p);
            cli.getFirstRoadPlacement(p,colony,game);
        }catch (InputMismatchException e) {
            System.out.println("the line of the tile should be an Integer between 0-3");
            System.out.println("the column of the tile should be an Integer between 0-3");
            System.out.println("placement-coordinates of the future colony should be an Integer between 0-3");
            this.getFirstColonyPlacement(p,game,secondRound);
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
                        if(color.replace(s,false,true)) {
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
        game.botsGetColor(color);
    }
}
