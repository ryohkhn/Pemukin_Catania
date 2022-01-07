package vue;

import game.Game;
import game.Launcher;
import game.Player;

import javax.swing.*;
import java.awt.*;

public class Gui extends JFrame{
    private Game game;
    private Launcher launcher;
    private GuiBoard guiBoard;
    private GuiSideBar guiSideBar;

    public Gui(Launcher launcher){
        this.launcher=launcher;
        setTitle("Pemukin Catania");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width-400, screenSize.height-300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        guiSideBar=new GuiSideBar(game,this,launcher);
        guiSideBar.setVisible(true);
        getContentPane().add(guiSideBar);

        guiSideBar.chooseNbPlayers();
    }

    // getter
    public GuiSideBar getGuiSideBar(){
        return guiSideBar;
    }

    // fonction appelée à la fin de l'initialisation pour mettre en place la Gui pour la partie
    public void setUpGui(Game game){
        getContentPane().removeAll();
        getContentPane().revalidate();
        getContentPane().repaint();

        getContentPane().setLayout(new GridLayout());

        this.game=game;

        guiBoard=new GuiBoard(game,this,guiSideBar,launcher,game.getBoard(),game.getBoard().getTiles());
        guiSideBar.setGuiBoard(guiBoard);
        getContentPane().add(guiBoard);
        getContentPane().add(guiSideBar);
        guiBoard.setVisible(true);
    }

    // fonction de début de tour
    public void startRound(){
        int diceNumber=game.generateDiceNumber();
        guiSideBar.displayDiceNumber(diceNumber);
    }

    // fonction de fin de tour
    public void endRound(){
        Player currentPlayer=launcher.getCurrentPlayer();
        currentPlayer.alreadyPlayedCardThisTurn=false;
        currentPlayer.cardsDrawnThisTurnReset();
        game.checkLongestArmy();
        if(currentPlayer.hasWin()){
            guiSideBar.victory(currentPlayer);
        }
        else{
            launcher.nextPlayer();
            startRound();
        }
    }
}
