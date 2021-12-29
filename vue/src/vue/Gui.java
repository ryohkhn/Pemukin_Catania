package vue;

import board.Colony;
import game.Game;
import game.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class Gui extends JFrame implements Vues{
    private GuiBoard guiBoard;
    private GuiSideBar guiSideBar;
    private int nbplayer;

    public Gui(){
        guiBoard=new GuiBoard();
        guiSideBar=new GuiSideBar();
        setUpGui();
    }

    public void setUpGui(){
        setTitle("Pemukin Catania");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width-700, screenSize.height-300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

//        getContentPane().setLayout(new GridLayout());
        guiSideBar.setBackground(Color.red);
        this.add(guiBoard);
        this.add(guiSideBar);
        guiSideBar.setVisible(false);
        guiBoard.setVisible(false);

        setVisible(true);
    }

    // marche pas encore il faut trouver une solution
    // ça return la valeur avant même de prendre l'input de la comboBox
    @Override
    public int chooseNbPlayers(){
        JPanel playerSelection=new JPanel();
        playerSelection.add(new JLabel("Choose how many players you want in the game."),CENTER_ALIGNMENT);
        String[] values={"3","4"};
        JComboBox comboBox=new JComboBox(values);
        JButton next=new JButton("Continue");
        playerSelection.add(comboBox,CENTER_ALIGNMENT);
        playerSelection.add(next,CENTER_ALIGNMENT);
        getContentPane().add(playerSelection);
        final int[] returnedValue=new int[1];
        ActionListener buttonListener=new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                returnedValue[0]=Integer.parseInt(comboBox.getActionCommand());
            }
        };
        next.addActionListener(buttonListener);

        return returnedValue[0];
    }

    @Override
    public boolean chooseHuman(){
        return false;
    }

    @Override
    public String chooseColor(HashMap<String, Boolean> color){
        return null;
    }

    @Override
    public int getAction(Player p){
        return 0;
    }

    @Override
    public String[] ressourceToBeDiscarded(Player player, int quantity){
        return new String[0];
    }

    @Override
    public int[] getRoadPlacement(){
        return new int[0];
    }

    @Override
    public int[] getColonyPlacement(){
        return new int[0];
    }

    @Override
    public int[] getCityPlacement(){
        return new int[0];
    }

    @Override
    public int[] getThiefPlacement(){
        return new int[0];
    }

    @Override
    public String chooseCard(){
        return null;
    }

    @Override
    public String[] chooseResource(int number){
        return new String[0];
    }

    @Override
    public void victory(Player p){

    }

    @Override
    public void displayBoard(Game game){

    }

    @Override
    public void displayPlayer(Player p){

    }

    @Override
    public Player choosePlayerFromColony(ArrayList<Colony> ownedColonies){
        return null;
    }

    @Override
    public int portSelection(Player player){
        return 0;
    }

    @Override
    public void displayDiceNumber(int diceNumber){

    }
}
