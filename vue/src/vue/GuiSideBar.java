package vue;

import board.Tile;
import game.Game;
import game.Launcher;
import game.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GuiSideBar extends JPanel implements Vues{
    private Game game;
    private GuiBoard guiBoard;
    private Gui gui;
    private Launcher launcher;
    private JPanel playerPanel;
    private JPanel mainPanel;
    private JPanel infoPanel;
    private int countInitialization;

    public GuiSideBar(Game game,Gui gui,Launcher launcher){
        this.game=game;
        this.launcher=launcher;
        this.gui=gui;
    }

    public void setGuiBoard(GuiBoard guiBoard){
        this.guiBoard=guiBoard;
    }

    public void setUpGui(){
        setLayout(new GridLayout(3,1));
        playerPanel=new JPanel(new GridLayout(5,1));
        mainPanel=new JPanel(new GridLayout(5,1));
        infoPanel=new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel,BoxLayout.Y_AXIS));
        this.add(playerPanel);
        this.add(mainPanel);
        this.add(infoPanel);
    }

    @Override
    public void chooseNbPlayers(){
        JPanel gameNumberPanel=new JPanel();
        gameNumberPanel.add(new JLabel("Choose how many players you want in the game :"));
        String[] values={"1","3","4"};
        JComboBox comboBox=new JComboBox(values);
        JButton next=new JButton("Continue");
        gameNumberPanel.add(comboBox);
        gameNumberPanel.add(next);
        this.add(gameNumberPanel);
        final int[] returnedValue=new int[1];
        next.addActionListener(actionEvent -> {
            returnedValue[0]=Integer.parseInt((String)comboBox.getSelectedItem());
            game=launcher.createGame(returnedValue[0]);
            remove(gameNumberPanel);
            refreshPanel(this);
            setPlayers();
        });
    }

    @Override
    public void setPlayers(){
        JPanel playerSelectionPanel=new JPanel();
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
        this.add(playerSelectionPanel);
        final String[] returnedValue=new String[nbplayer];
        next.addActionListener(actionEvent -> {
            for(int i=0; i<comboBoxList.size(); i++){
                returnedValue[i]=(comboBoxList.get(i).getSelectedItem()).equals("Human")?"1":"2";
            }
            game.setPlayers(returnedValue);
            remove(playerSelectionPanel);
            refreshPanel(this);
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
            remove(colorSelectionPanel);
            refreshPanel(this);
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
        this.add(colorSelectionPanel);
        next.addActionListener(actionEvent -> {
            for(JRadioButton button:jRadioButtonList){
                if(button.isSelected()){
                    returnedValue[Math.abs(count-nbPlayer)]=button.getActionCommand();
                    colorMap.merge(button.getActionCommand(),true, (initialValue, replacedValue)->initialValue=replacedValue);
                }
            }
            compt[0]-=1;
            colorSelectionController(nbPlayer,compt[0],returnedValue,colorMap);
            remove(colorSelectionPanel);
            refreshPanel(this);
        });
    }

    @Override
    public void initialization(Game game){
        countInitialization=this.game.getPlayers().length*4;
        this.setUpGui();
        gui.setUpGui(game);
        launcher.setFirstPlayer();
        initiateRoadsAndColonies(game.getPlayers().length,launcher.getCurrentPlayer());
    }

    public void initiateRoadsAndColonies(int nbPlayer, Player currentPlayer){
        if(nbPlayer==0){
            removeAndRefresh(true,true,false);
            refreshPanel(this);
            gui.startRound();
            return;
        }
        JLabel playerInfo=new JLabel(currentPlayer.toString()+" place your "+(countInitialization>(game.getPlayers().length*2)?"first":"second")+(countInitialization%2==1?" road.":" colony."));
        playerInfo.setHorizontalAlignment(SwingConstants.HORIZONTAL);
        playerPanel.add(playerInfo);
        final int[] compt=new int[1];
        compt[0]=nbPlayer;
        JButton button=new JButton("Place");
        button.addActionListener(actionEvent -> {
            mainPanel.remove(button);
            refreshPanel(mainPanel);
            JLabel placingLabel=new JLabel("Placing...");
            mainPanel.add(placingLabel);
            if(countInitialization%2==1){
                guiBoard.setAllTileAsListener(false,false,false,false);
            }
            else{
                guiBoard.setAllTileAsListener(true,false,false,false);
            }
        });
        mainPanel.add(button);
    }

    // fonction appelée par le MouseInputListener de la class GuiTile lorsque la construction a bien été faite
    // elle permet de diminuer le compteur, passer au joueur suivant et appeler la fonction tant que le compteur n'est pas à 0
    public void roundInitializationDone(){
        removeAndRefresh(true,true,false);
        refreshPanel(guiBoard);
        this.countInitialization-=1;
        int nbPlayers=game.getPlayers().length*2;
        if(countInitialization!=nbPlayers && countInitialization%2==0){
            if(countInitialization>nbPlayers){
                launcher.nextPlayer();
            }
            else{
                launcher.prevPlayer();
            }
        }
        initiateRoadsAndColonies(countInitialization,launcher.getCurrentPlayer());
    }

    @Override
    public void getAction(Player player){
        JButton buildOrBuyButton=new JButton("Build/Buy");
        JButton tradeButton=new JButton("Trade");
        JButton useButton=new JButton("Use card");
        JButton endRoundButton=new JButton("End round");
        buildOrBuyButton.addActionListener(event->{

        });
        tradeButton.addActionListener(event->{

        });
        useButton.addActionListener(event->{

        });
        endRoundButton.addActionListener(event->{
            gui.endRound();
        });
        mainPanel.add(buildOrBuyButton);
        mainPanel.add(tradeButton);
        mainPanel.add(useButton);
        mainPanel.add(endRoundButton);
        refreshPanel(mainPanel);
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
        guiBoard.repaint();
    }

    @Override
    public void displayPlayer(Player currentPlayer){
        JLabel playerTurn=new JLabel("Turn of "+currentPlayer.toString());
        JLabel playerResources=new JLabel("Resources : "+currentPlayer.getRessourcesToString());
        JLabel playerCards=new JLabel("Cards : "+currentPlayer.getCardsToString());
        JLabel playerVP=new JLabel("Victory Point : "+currentPlayer.getVictoryPoint());
        playerTurn.setHorizontalAlignment(SwingConstants.HORIZONTAL);
        playerResources.setHorizontalAlignment(SwingConstants.HORIZONTAL);
        playerCards.setHorizontalAlignment(SwingConstants.HORIZONTAL);
        playerVP.setHorizontalAlignment(SwingConstants.HORIZONTAL);
        playerPanel.add(playerTurn);
        playerPanel.add(playerResources);
        playerPanel.add(playerCards);
        playerPanel.add(playerVP);
        refreshPanel(playerPanel);
    }

    @Override
    public void displayOtherPlayers(Player p, Game game){
        // TODO: 04/01/2022 faire fonction 
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

    @Override
    public void displayDiceNumber(int diceNumber){
        removeAndRefresh(true,true,true);
        displayPlayer(launcher.getCurrentPlayer());
        JButton diceRollButton=new JButton("Roll the dices !");
        diceRollButton.addActionListener(event->{
            JLabel diceRollLabel=new JLabel("Dice roll : " + diceNumber);
            mainPanel.add(diceRollLabel);
            mainPanel.remove(diceRollButton);
            game.diceProduction(diceNumber);
            getAction(launcher.getCurrentPlayer());
            // TODO: 03/01/2022 appel de la fonction avec le panel général des actions + affichage des autre joueurs
            showBuildCost();
            refreshPanel(mainPanel);
        });
        mainPanel.add(diceRollButton);
    }

    @Override
    public void displayDiceProduction(HashMap<Player, String> diceResultsProduction){
        String stringProduction="";
        infoPanel.add(new JLabel(""));
        for(Player player:diceResultsProduction.keySet()){
            infoPanel.add(new JLabel(player+" : "+diceResultsProduction.get(player)));
        }
        infoPanel.add(new JLabel(stringProduction));
    }

    public void endGame(Player currentPlayer){
        removeAndRefresh(true,true,false);
        JLabel playerWonLabel=new JLabel(currentPlayer+" has won the game !");
        playerWonLabel.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(playerWonLabel);
        refreshPanel(mainPanel);
    }

    public void removeAndRefresh(boolean playerPanel,boolean mainPanel,boolean infoPanel){
        if(playerPanel){
            this.playerPanel.removeAll();
            this.playerPanel.revalidate();
            this.playerPanel.repaint();
        }
        if(mainPanel){
            this.mainPanel.removeAll();
            this.mainPanel.revalidate();
            this.mainPanel.repaint();
        }
        if(infoPanel){
            this.infoPanel.removeAll();
            this.infoPanel.revalidate();
            this.infoPanel.repaint();
        }
    }

    public static void refreshPanel(JPanel jPanel){
        jPanel.revalidate();
        jPanel.repaint();
    }
}
