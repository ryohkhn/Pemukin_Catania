package vue;
import game.*;
import board.*;

import java.util.HashMap;
import java.util.Scanner;


public class Cli implements Vues{
    Scanner scanner;
    Game game;
    public Player launchCli(){
        System.out.println("If you want to be 3 players type 3. \nElse you'll be 4.");
        scanner=new Scanner(System.in);
        int nbPlayer;
        HashMap<String, Boolean> color=new HashMap<>();
        color.put("blue", false);
        color.put("yellow", false);
        color.put("green", false);
        color.put("orange", false);
        if(scanner.next().equals("3")) {
            nbPlayer=3;
        } else {
            nbPlayer=4;
        }
        game=new Game(nbPlayer);
        for(int i=0; i<nbPlayer; i++) {
            System.out.println("Type 1 to initialize a human player.\n Else player "+i+" will be a bot.");
            scanner=new Scanner(System.in);
            if(scanner.next().equals("1")) {
                System.out.println("choose a color between :"+color.toString());
                scanner=new Scanner(System.in);
                String colorChoice=scanner.next();
                while(!color.replace(colorChoice, false, true)) {
                    System.out.println("choose a color between :"+color.toString()); // TODO: 21/12/2021 changer le toString() par un méthode qui affichera uniquement les couleurs avec une valeur false 
                    scanner=new Scanner(System.in);
                    colorChoice=scanner.next();
                }
                game.setPlayers(i,new Human(colorChoice));
            } else {
                game.setPlayers(i,new Bot());
            }
        }
        for(int i=0;i<nbPlayer;i++){
            if(game.getPlayers()[i] instanceof Bot){
                if(color.replace("blue", false, true)) ((Bot) game.getPlayers()[i]).setColor("blue");
                else if(color.replace("green", false, true)) ((Bot) game.getPlayers()[i]).setColor("green");
                else if(color.replace("yellow", false, true)) ((Bot) game.getPlayers()[i]).setColor("yellow");
                else if(color.replace("orange", false, true)) ((Bot) game.getPlayers()[i]).setColor("orange");
            }
        }
        return play();
    }

    public void displayBoard(){

    }
    
    public Player play() {
        while(true){
            for(Player p : this.game.getPlayers()){
                this.game.getBoard().display(p);
                if(p.hasWin()){
                    System.out.println(p + "won the game");
                }
            }
        }
    }
}
