package game;

import vue.*;

import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Launcher {
    private Game game;

    public static void main(String[] args){
        Launcher launcher=new Launcher();
        System.out.println("If you want to play on GUI, type 1.\nElse you'll play on console.");
        Scanner sc2=new Scanner(System.in);
        String input=sc2.nextLine();
        if(input.equals("1")) launcher.launch(new Gui(launcher));
        else launcher.launch(new Cli(launcher));
    }



    public void launch(Vues vue){
        vue.chooseNbPlayers();
        game.setVueGenerale(vue);
        vue.setPlayers();
        vue.initialization(game); // je me suis arreté la
        boolean hasWon=false;
        while(!hasWon){
            for(Player p : game.getPlayers()){
                game.checkLongestArmy();
                vue.displayPlayer(p);
                vue.displayBoard(game);
                boolean check=false;
                Random rand=new Random();
                int diceNumber=rand.nextInt(6) + 1 + rand.nextInt(6) + 1;
                vue.displayDiceNumber(diceNumber);
                if(diceNumber==7) game.sevenAtDice(p);
                game.diceProduction(diceNumber);
                while(!check) {
                    //check=vue.getAction(p);
                }
                if(p.hasWin()){
                    vue.victory(p);
                    hasWon=true;
                }
            }
        }
    }
    public void launchGui(){
        Gui gui=new Gui(this);
    }

    private static boolean getAction(Player p,Vues vue,Game game) {
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

    public Game createGame(int nbPlayers){
        Game game=new Game(nbPlayers);
        this.game=game;
        return game;
    }
}
