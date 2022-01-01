package vue;

import game.Game;
import game.Launcher;
import game.Player;

import javax.swing.*;
import java.awt.*;

public class GuiSideBar extends JPanel{
    private Game game;
    private GuiBoard guiBoard;
    private Launcher launcher;
    private JPanel playerPanel;
    private JPanel mainPanel;
    private JPanel infoPanel;
    private int countInitialization;

    public GuiSideBar(Game game,Launcher launcher){
        this.game=game;
        this.launcher=launcher;
        countInitialization=this.game.getPlayers().length*4;
        setUpGui();
    }

    public void setGuiBoard(GuiBoard guiBoard){
        this.guiBoard=guiBoard;
    }

    public void setUpGui(){
        setLayout(new GridLayout(3,1));
        playerPanel=new JPanel();
        mainPanel=new JPanel();
        infoPanel=new JPanel();
        infoPanel.setLayout(new GridLayout(4,1));
        add(playerPanel);
        add(mainPanel);
        add(infoPanel);
    }

    public void showBuildCost(){
        JLabel buildCostRoad=new JLabel("Road : 1x Clay, 1x Wood -> 0 Victory Point");
        JLabel buildCostColony=new JLabel("Colony : 1x Clay, 1x Wheat, 1x Wood, 1x Wool -> 1 Victory Point");
        JLabel buildCostCity=new JLabel("City : 3x Ore, 2x Wheat -> 2 Victory Points");
        JLabel buildCostDevelopmentCard=new JLabel("Development Card : 1x Ore, 1x Wheat, 1x Wool -> ? Victory Point");
        infoPanel.add(buildCostRoad);
        infoPanel.add(buildCostColony);
        infoPanel.add(buildCostCity);
        infoPanel.add(buildCostDevelopmentCard);
    }

    public void displayPlayer(Player p){
        if(p!=null){

        }
    }

    public void initiateRoadsAndColonies(int nbPlayer, Player currentPlayer){
        if(nbPlayer==0){
            mainPanel.add(new JLabel("Initialization done !"));
            // lancement fonction partie
            return;
        }
        playerPanel.add(new JLabel(currentPlayer.toString()));
        playerPanel.add(new JLabel("Place your "+(countInitialization>game.getPlayers().length?"first":"second")+(countInitialization%2==1?" road.":"colony.")));
        final int[] compt=new int[1];
        compt[0]=nbPlayer;
        JButton button=new JButton("Place");
        button.addActionListener(actionEvent -> {
            if(countInitialization%2==1){
                guiBoard.setAllTileAsListener(false,false,false,false);
            }
            else{
                guiBoard.setAllTileAsListener(true,false,false,false);
            }
        });
        mainPanel.add(button);
    }

    public void roundInitializationDone(){
        mainPanel.removeAll();
        playerPanel.removeAll();
        mainPanel.revalidate();
        mainPanel.repaint();
        playerPanel.revalidate();
        playerPanel.repaint();
        this.countInitialization-=1;
        int nbPlayers=game.getPlayers().length*2;
        if(countInitialization!=nbPlayers){
            if(countInitialization>nbPlayers){
                launcher.nextPlayer();
            }
            else{
                launcher.prevPlayer();
            }
        }
        launcher.nextPlayer();
        initiateRoadsAndColonies(countInitialization, launcher.getCurrentPlayer());
    }

    public void displayDiceNumber(int diceNumber){

    }
}
