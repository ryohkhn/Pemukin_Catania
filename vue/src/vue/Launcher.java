package vue;

import game.*;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Launcher {

    public static void main(String[] args){
        System.out.println("If you want to play on GUI, type 1.\nElse you'll play on console.");
        Scanner sc2=new Scanner(System.in);
        String input=sc2.nextLine();
        if(input.equals("1")) System.exit(0); //lancer la version GUI
        else launchCli();
    }

    public static void launchCli(){
        Cli cli=new Cli();
        int nbPlayer=cli.chooseNbPlayers();
        Game game=new Game(nbPlayer);
        game.setVueGenerale(cli);
        HashMap<String, Boolean> color=new HashMap<>();
        color.put("blue", false);
        color.put("yellow", false);
        color.put("green", false);
        color.put("orange", false);
        for(int i=0; i<nbPlayer; i++) {
            if(cli.chooseHuman()){
                String colorChoice= cli.chooseColor(color);
                while(!color.replace(colorChoice, false, true)) {
                    colorChoice= cli.chooseColor(color);
                }
                game.setPlayers(i,new Human(colorChoice));
            }else{
                game.setPlayers(i,new Bot());
            }
        }
        game.botsGetColor(color);
        Cli.printBlankLines();
        game.initialization();
        Cli.printBlankLines();
        boolean hasWon=false;
        while(!hasWon){
            for(Player p : game.getPlayers()){
                cli.displayBoard(game);
                boolean check=false;
                Random rand=new Random();
                int diceNumber=rand.nextInt(6) + 1 + rand.nextInt(6) + 1;
                cli.displayDiceNumber(diceNumber);
                if(diceNumber==7) game.sevenAtDice(p);
                game.diceProduction(diceNumber);
                cli.displayPlayer(p);
                while(!check) {
                    check=getAction(p,cli,game);
                }
                if(p.hasWin()){
                    cli.victory(p);
                    hasWon=true;
                }
            }
        }
    }

    private static boolean getAction(Player p,Cli cli,Game game) {
        switch(cli.getAction(p)){
            case 1 -> {
                game.tradeWithPort(p);
                //System.out.println("exchange ressources bank");
                return false;
            }
            case 2 -> {
                game.buildColony(p);
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
                cli.displayPlayer(p);
                return false;
            }
            default -> {
                //System.out.println("End of the round.");
                p.alreadyPlayedCardThisTurn=false;
                return true;
            }
        }
    }
}
