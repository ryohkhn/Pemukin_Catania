package vue;
import game.Bot;
import game.Game;
import game.Human;
import game.Player;

import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Launcher {

    public static void main(String[] args){
        System.out.println("If you want to play on GUI, type 1.\nElse you'll play on console.");
        Scanner sc2=new Scanner(System.in);
        if(sc2.next().equals("1")) System.exit(0); //lancer la version GUI
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
        cli.initialisation(); // TODO: 21/12/2021 méthode pour initialiser la partie (2 routes 2 colonies par joueurs)
        boolean hasWon=false;
        while(!hasWon){
            for(Player p : game.getPlayers()){
                game.display(p);
                boolean check=false;
                int diceNumber=new Random(5).nextInt() + 1 + new Random(5).nextInt() + 1;
                if(diceNumber==7) game.sevenAtDice(p);
                game.diceProduction(diceNumber);
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
        switch(cli.getAction(p)) {
            case 1:
                // TODO: 21/12/2021 appeler fonction dans game que j'ai pas comprise et donc pas complétée
                System.out.println("exchange resources bank");
                return false;

            case 2:
                game.buildColony(p);
                System.out.println("build a new colony");
                return false;

            case 3:
                game.buildCity(p);
                System.out.println("upgrade a colony into a city");
                return false;

            case 4:
                game.buildRoad(p);
                System.out.println("build a road");
                return false;

            case 5:
                game.buyCard(p);
                System.out.println("buy development cards");
                return false;

            case 6:
                // TODO: 21/12/2021 fonction qui fait jouer une carte + qui va mettre true a l'attribut alreadyPlayedThisTurn de card, ne fais rien si deja true
                System.out.println("play a development card");
                return false;

            case 7:
                System.out.println("End of the round.");
                return true;

        }
        return true; //value that will never be reached
    }
}
