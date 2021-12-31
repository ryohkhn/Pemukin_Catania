package game;

import vue.Cli;
import vue.Gui;
import vue.Vues;

import java.util.Random;
import java.util.Scanner;

public class Launcher {
    private Game game;

    public static void main(String[] args){
        Launcher launcher=new Launcher();
        System.out.println("If you want to play on GUI, type 1.\nElse you'll play on console.");
        Scanner sc2=new Scanner(System.in);
        String input=sc2.nextLine();
        if(input.equals("1")) launcher.launch((Vues)new Gui(launcher));
        else launcher.launch(new Cli(launcher));
    }



    public void launch(Vues vue){
        vue.chooseNbPlayers();
        vue.setPlayers();
        vue.initialization(game);
        boolean hasWon=false;
        while(!hasWon){
            for(Player p : game.getPlayers()){
                game.checkLongestArmy();
                vue.displayPlayer(p);
                vue.displayBoard(game);
                Random rand=new Random();
                int diceNumber=rand.nextInt(6) + 1 + rand.nextInt(6) + 1;
                vue.displayDiceNumber(diceNumber);
                if(diceNumber==7) this.seven(p,vue);
                game.diceProduction(diceNumber);
                vue.getAction(p);
                if(p.hasWin()){
                    vue.victory(p);
                    hasWon=true;
                }
            }
        }
    }

    private void seven(Player p,Vues vue) {
        for(Player player: game.getPlayers()) {
            int quantity=player.resourceCount()/2;
            if(quantity>7) {
                vue.sevenAtDice(player,quantity);
            }
        }
        vue.setThief();
        vue.steal(p,game.getBoard().getThiefTile());
    }

    public Game createGame(int nbPlayers){
        Game game=new Game(nbPlayers);
        this.game=game;
        return game;
    }
}
