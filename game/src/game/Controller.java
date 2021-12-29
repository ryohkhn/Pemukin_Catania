package game;

import vue.Cli;

import java.util.Scanner;

public class Controller{
    private Game game;
    private Cli cli;
    private Scanner scanner=new Scanner(System.in);
//    de ce que j'ai compris, il a dit que sa vue faisait que afficher des choses,
//    et ça me parait plutot logique, si on appelle des fonctions de controleur depuis la vue,
//   aucun interet d'utiliser une classe controleur, vaut mieux laisser les calculs dedans

// au final le principe du contrôleur permet juste d'adapter nos fonctions de game pour ensuite les appeler dans la gui
// en soit c'est inutile


// c'est pareil au final "On a une classe qui fait office de controleur, qui fonctionne comme ce que j'ai décris ci-dessus, et une classe vue qui ne fait qu'afficher des trucs, que ce soit en terminal ou en GUI les évènements de la vue appellent des fonctions du controleur, le controleur peut appeler des fonctions d'affichage"
    public Controller(){

    }

    public int chooseNbPlayers() { //launcher of cli version

        if(scanner.nextLine().equals("3")) {

        } else {

        }
    }

}
