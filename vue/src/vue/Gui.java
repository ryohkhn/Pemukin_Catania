package vue;

import board.Tile;
import game.Game;
import game.Launcher;
import game.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Gui extends JFrame implements Vues{
    private Game game;
    private Launcher launcher;
    private GuiBoard guiBoard;
    private GuiSideBar guiSideBar;
    private JPanel gameNumberPanel;
    private JPanel playerSelectionPanel;

    public Gui(Launcher launcher){
        this.launcher=launcher;
        setTitle("Pemukin Catania");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width-400, screenSize.height-300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        chooseNbPlayers();
    }

    @Override
    public void chooseNbPlayers(){
        gameNumberPanel=new JPanel();
        gameNumberPanel.add(new JLabel("Choose how many players you want in the game :"));
        String[] values={"1","3","4"};
        JComboBox comboBox=new JComboBox(values);
        JButton next=new JButton("Continue");
        gameNumberPanel.add(comboBox);
        gameNumberPanel.add(next);
        getContentPane().add(gameNumberPanel);
        final int[] returnedValue=new int[1];
        next.addActionListener(actionEvent -> {
            returnedValue[0]=Integer.parseInt((String)comboBox.getSelectedItem());
            game=launcher.createGame(returnedValue[0]);
            gameNumberPanel.setVisible(false);
            setPlayers();
        });
    }

    @Override
    public void setPlayers(){
        playerSelectionPanel=new JPanel();
        playerSelectionPanel.add(new JLabel("Select which player is Human or IA :"));
        int nbplayer=game.getPlayers().length;
        String[] values={"Human","IA"};
        ArrayList<JComboBox> comboBoxList=new ArrayList<>();
        for(int i=0; i<nbplayer; i++){
            comboBoxList.add(new JComboBox(values));
            playerSelectionPanel.add(new JLabel("Player "+(i+1)+" :"));
            playerSelectionPanel.add(comboBoxList.get(i));
        }
        JButton next=new JButton("Continue");
        playerSelectionPanel.add(next);
        getContentPane().add(playerSelectionPanel);
        final String[] returnedValue=new String[nbplayer];
        next.addActionListener(actionEvent -> {
            for(int i=0; i<comboBoxList.size(); i++){
                returnedValue[i]=(comboBoxList.get(i).getSelectedItem()).equals("Human")?"1":"2";
            }
            game.setPlayers(returnedValue);
            playerSelectionPanel.setVisible(false);
            HashMap<String, Boolean> colorMap=new HashMap<>();
            colorMap.put("orange", false);
            colorMap.put("blue", false);
            colorMap.put("yellow", false);
            colorMap.put("green", false);
            colorSelectionController(nbplayer,nbplayer,new String[nbplayer],colorMap);
        });
    }

    public void colorSelectionController(int nbPlayer,int count,String[] returnedValue,HashMap<String,Boolean> colorMap){
        final int[] compt=new int[1];
        compt[0]=count;
        JPanel colorSelectionPanel=new JPanel();
        if(compt[0]==0){
            game.setColors(returnedValue);
            initialization(game);
            return;
        }
        JButton next=new JButton("Continue");
        ButtonGroup buttonGroup=new ButtonGroup();
        colorSelectionPanel.add(new JLabel("Select a color for player :"));
        ArrayList<JRadioButton> jRadioButtonList=new ArrayList<>();
        for(String s:colorMap.keySet()){
            if(!colorMap.get(s)){
                JRadioButton button=new JRadioButton(s);
                jRadioButtonList.add(button);
                buttonGroup.add(button);
                colorSelectionPanel.add(button);
            }
        }
        jRadioButtonList.get(0).setSelected(true);
        colorSelectionPanel.add(next);
        getContentPane().add(colorSelectionPanel);
        next.addActionListener(actionEvent -> {
            for(JRadioButton button:jRadioButtonList){
                if(button.isSelected()){
                    returnedValue[Math.abs(count-nbPlayer)]=button.getActionCommand();
                    colorMap.merge(button.getActionCommand(),true, (initialValue, replacedValue)->initialValue=replacedValue);
                }
            }
            compt[0]-=1;
            colorSelectionController(nbPlayer,compt[0],returnedValue,colorMap);
            colorSelectionPanel.setVisible(false);
        });
    }

    @Override
    public void initialization(Game game){
        getContentPane().removeAll();
        getContentPane().revalidate();
        getContentPane().repaint();

        guiSideBar=new GuiSideBar(game,launcher);
        guiBoard=new GuiBoard(game,this,guiSideBar,launcher,game.getBoard(),game.getBoard().getTiles());
        guiSideBar.setGuiBoard(guiBoard);

        getContentPane().setLayout(new GridLayout());
        getContentPane().add(guiBoard);
        getContentPane().add(guiSideBar);
        guiBoard.setVisible(true);
        guiSideBar.setVisible(true);

        guiSideBar.initiateRoadsAndColonies(game.getPlayers().length,launcher.getCurrentPlayer());
    }



    @Override
    public void getAction(Player player){

    }

    @Override
    public void portSelection(Player player){

    }

    @Override
    public void chooseResource(){

    }

    @Override
    public void getPortResource(){

    }

    @Override
    public void sevenAtDice(Player p, int quantity){

    }

    @Override
    public void setThief(){

    }

    @Override
    public void steal(Player p, Tile thiefTile){

    }

    @Override
    public void getRoadPlacement(){

    }

    @Override
    public void getCityPlacement(){

    }

    @Override
    public void chooseCard(){

    }

    @Override
    public void victory(Player p){

    }

    @Override
    public void displayBoard(Game game){
        guiBoard.createBoard();
    }

    @Override
    public void displayPlayer(Player p){
        guiSideBar.displayPlayer(p);
    }

    @Override
    public void displayDiceNumber(int diceNumber){
        guiSideBar.displayDiceNumber(diceNumber);
    }

    @Override
    public void showBuildCost(){
        guiSideBar.showBuildCost();
    }
}
