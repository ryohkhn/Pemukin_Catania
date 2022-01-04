package game;

import vue.Cli;
import vue.Gui;
import vue.Vues;

import java.util.Scanner;

public class Launcher {
    private Game game;
    private Player currentPlayer;
    private int indexCurrentPlayer;
    private Vues vue;

    public static void main(String[] args){
        Launcher launcher=new Launcher();
        Gui gui=new Gui(launcher);
        launcher.vue=gui.getGuiSideBar();
        System.out.println("If you want to play on GUI, type 1.\nElse you'll play on console.");
        Scanner sc2=new Scanner(System.in);
        String input=sc2.nextLine();
        if(input.equals("1")){
            //Gui gui=new Gui(launcher);
        }else{
            Cli cli=new Cli(launcher);
            launcher.vue=cli;
            launcher.launch();
        }

    }



    public void launch(){
        vue.chooseNbPlayers();
        vue.setPlayers();
        vue.initialization(game);
        boolean hasWon=false;
        while(!hasWon){
            if(game!=null){
                for(Player p : game.getPlayers()){
                    vue.displayPlayer(p);
                    vue.displayBoard(game);
                    int diceNumber=game.generateDiceNumber();
                    vue.displayDiceNumber(diceNumber);
                    if(diceNumber==7) this.seven(p, vue);
                    game.diceProduction(diceNumber);
                    vue.getAction(p);
                    game.checkLongestArmy();
                    if(p.hasWin()){
                        vue.victory(p);
                        hasWon=true;
                    }
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
        Game game=new Game(nbPlayers,this.vue);
        this.game=game;
        return game;
    }

    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    public void nextPlayer() {
        if(indexCurrentPlayer==game.getPlayers().length-1){
            currentPlayer = game.getPlayers()[0];
            indexCurrentPlayer=0;
        }
        else{
            currentPlayer = game.getPlayers()[indexCurrentPlayer+1];
            indexCurrentPlayer+=1;
        }
	}

	public void prevPlayer() {
        if(indexCurrentPlayer==0){
            currentPlayer = game.getPlayers()[game.getPlayers().length-1];
            indexCurrentPlayer=game.getPlayers().length-1;
        }
        else{
            currentPlayer = game.getPlayers()[indexCurrentPlayer-1];
            indexCurrentPlayer-=1;
        }
	}

	public void setFirstPlayer() {
		currentPlayer = game.getPlayers()[0];
        indexCurrentPlayer=0;
	}
}
