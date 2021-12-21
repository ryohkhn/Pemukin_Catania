package vue;
import game.Game;
import game.Player;

import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Scanner;

public class Launcher {

    public static void main(String[] args){
        System.out.println("If you want to play on GUI, type 1.\nElse you'll play on console.");
        Scanner sc2=new Scanner(System.in);
        if(sc2.next().equals("1")) System.exit(0); //lancer la version GUI
        else System.out.println("The winner is : " + new Cli().launchCli());
    }
}
