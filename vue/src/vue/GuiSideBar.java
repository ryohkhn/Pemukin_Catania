package vue;

import board.Board;
import board.Colony;
import board.Tile;
import game.Card;
import game.Game;
import game.Launcher;
import game.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuiSideBar extends JPanel implements Vues{
    private Game game;
    private GuiBoard guiBoard;
    private Gui gui;
    private Launcher launcher;
    private JPanel playerPanel;
    private JPanel mainPanel;
    private JPanel infoPanel;
    private JPanel infoPanelOtherPlayers;
    private int countInitialization;

    public GuiSideBar(Game game,Gui gui,Launcher launcher){
        this.game=game;
        this.launcher=launcher;
        this.gui=gui;
    }

    public void setGuiBoard(GuiBoard guiBoard){
        this.guiBoard=guiBoard;
    }

    // fonction d'initialisation du JPanel appelée à la fin de l'initialisation de la partie
    // elle permet de créer tout les panels pour l'objet et mettre en place les layout
    public void setUpGui(){
        setLayout(new GridLayout(3,1));
        playerPanel=new JPanel(new GridLayout(5,1));
        mainPanel=new JPanel(new GridLayout(5,1));
        infoPanel=new JPanel();
        infoPanel.setLayout(new GridLayout(1,2));
        infoPanelOtherPlayers=new JPanel();
        infoPanel.add(infoPanelOtherPlayers);
        this.add(playerPanel);
        this.add(mainPanel);
        this.add(infoPanel);
    }

    // fonction de la Vue pour choisir le nombre de joueurs dans la partie
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
        refreshPanel(this);
    }

    // fonction de la Vue pour choisir si chaque joueur est une Humain ou un Robot
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

    // fonction pour la sélection des couleurs pour chaque joueur
    // la fin de la fonction fait appel à la fonction d'initialisation de la partie
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
        colorSelectionPanel.add(new JLabel("Select a color for Player "+(Math.abs(count-nbPlayer)+1)+" :"));
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

    // fonction de la Vue d'initialisation de la partie, on met en place les JPanels et on appelle la fonction pour initialiser les routes et colonies
    @Override
    public void initialization(Game game){
        countInitialization=this.game.getPlayers().length*4;
        this.setUpGui();
        gui.setUpGui(game);
        launcher.setFirstPlayer();
        initiateRoadsAndColonies(game.getPlayers().length,launcher.getCurrentPlayer());
    }

    // fonction d'initilisation des routes et colonies, tant qu'il reste des tours à effectuer on active les Mouse Input Listener sur le plateau
    public void initiateRoadsAndColonies(int nbPlayer, Player currentPlayer){
        if(nbPlayer==0){
            startGame();
            return;
        }
        JLabel playerInfo=new JLabel("Player "+currentPlayer.toString()+" place your "+(countInitialization>(game.getPlayers().length*2)?"first":"second")+(countInitialization%2==1?" road.":" colony."));
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
                guiBoard.setAllTileAsListener(false,false,false,false, false);
            }
            else{
                guiBoard.setAllTileAsListener(true,false,false,false, false);
            }
        });
        mainPanel.add(button);
    }

    // fonction appelée par le MouseInputListener de la class GuiTile lorsque la construction a bien été faite
    // elle permet de diminuer le compteur et vérifier s'il faut passer au joueur suivant ou précédent en fonction de l'avancement des constructions
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

    // fonction de lancement de partie
    // On lance la fonction de Game pour générer les ressources de la fin d'initialisation et on affiche les informations nécessaires sur les JPanels
    public void startGame(){
        game.coloniesProduction();
        removeAndRefresh(true,true,false);
        refreshPanel(this);
        displayPlayer(launcher.getCurrentPlayer());
        showBuildCost();
        displayOtherPlayers(launcher.getCurrentPlayer(),game);
        gui.startRound();
    }

    // fonction de la Vue pour afficher les informations relatives au Joueur du tour
    // elle parmet d'afficher la couleur du joueur, les ressources et les cartes qu'il possède ainsi que ses points de victoire
    @Override
    public void displayPlayer(Player currentPlayer){
        JLabel playerTurn=new JLabel("Turn of player "+currentPlayer.toString());
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

    // fonction de la vue pour afficher les informations des autre joueurs dont ce n'est pas le tour
    // on affiche le nombre de cartes en leur possession ainsi que le nombre de chevaliers joués et leur points de victoire
    @Override
    public void displayOtherPlayers(Player p, Game game){
        for(Player player:game.getPlayers()){
            if(player!=p){
                infoPanelOtherPlayers.add(new JLabel("Player "+player+" : "+player.getVictoryPoint()+" Victory Points, "+player.getKnightPlayed()+" Knights Played, "+player.getNbCards()+" Cards."));
            }
        }
        refreshPanel(infoPanelOtherPlayers);
    }

    // fonction qui affiche les prix de constructions ansi que le prix d'achat d'une carte de développement
    public void showBuildCost(){
        JPanel infoPanelBuildCostPanel=new JPanel();
        infoPanelBuildCostPanel.setLayout(new GridLayout(5,1));
        infoPanel.add(infoPanelBuildCostPanel);
        JLabel buildCostRoad=new JLabel("Road : 1x Clay, 1x Wood -> 0 Victory Point");
        JLabel buildCostColony=new JLabel("Colony : 1x Clay, 1x Wheat, 1x Wood, 1x Wool -> 1 Victory Point");
        JLabel buildCostCity=new JLabel("City : 3x Ore, 2x Wheat -> 2 Victory Points");
        JLabel buildCostDevelopmentCard=new JLabel("Development Card : 1x Ore, 1x Wheat, 1x Wool -> ? Victory Point");
        infoPanelBuildCostPanel.add(buildCostRoad);
        infoPanelBuildCostPanel.add(buildCostColony);
        infoPanelBuildCostPanel.add(buildCostCity);
        infoPanelBuildCostPanel.add(buildCostDevelopmentCard);
        refreshPanel(infoPanel);
    }

    // fonction de la Vue qui met un bouton pour afficher le tirage de dés
    // il lance ensuite la fonction pour afficher le panel d'actions et met à jour ceux des ressources des joueurs
    @Override
    public void displayDiceNumber(int diceNumber){
        Player player=launcher.getCurrentPlayer();
        removeAndRefresh(true,true,false);
        displayPlayer(player);
        JButton diceRollButton=new JButton("Roll the dices !");
        diceRollButton.addActionListener(event->{
            JLabel diceRollLabel=new JLabel("Dice roll : " + diceNumber);
            mainPanel.add(diceRollLabel);
            mainPanel.remove(diceRollButton);
            infoPanelOtherPlayers.removeAll();
            game.diceProduction(diceNumber);
            displayOtherPlayers(player,game);

            getAction(player);
            playerPanel.removeAll();
            displayPlayer(player);
            refreshPanel(mainPanel);
            refreshPanel(this);
        });
        mainPanel.add(diceRollButton);
    }

    // fonction de la Vue appelée dans Game qui affiche la production de ressource des dés et à quel joueur elle est donnée
    @Override
    public void displayDiceProduction(HashMap<Player, List<String>> diceResultsProduction){
        for(Player player:diceResultsProduction.keySet()){
            for(String resource:diceResultsProduction.get(player)){
                infoPanelOtherPlayers.add(new JLabel("Player "+player+" : "+resource));
            }
        }
    }

    // fonction de la Vue qui affiche les buttons d'action pour le tour
    @Override
    public void getAction(Player player){
        JButton buildOrBuyButton=new JButton("Build/Buy");
        JButton tradeButton=new JButton("Trade");
        JButton useButton=new JButton("Use card");
        JButton endRoundButton=new JButton("End round");
        buildOrBuyButton.addActionListener(event->{

        });
        tradeButton.addActionListener(event->{
            JOptionPane.showMessageDialog(this,"Not implemented yet !");
        });
        useButton.addActionListener(event->{
            if(player.alreadyPlayedCardThisTurn){
                // error message
            }
            else{
                chooseCard();
            }
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
    public void getPortResource(){

    }

    @Override
    public void sevenAtDice(Player p, int quantity){

    }

    @Override
    public void displayStolenResource(Player player,String resource,Player playerOfColony,int quantity){
        mainPanel.add(new JLabel("You stole "+quantity+" "+resource+" from player "+playerOfColony));
    }

    @Override
    public void getRoadPlacement(){

    }

    @Override
    public void getCityPlacement(){

    }

    @Override
    public void chooseCard(){
        removeAndRefresh(false,true,false);
        Player player=launcher.getCurrentPlayer();
        if(player.getNbCards()==0){
            // error message
            JLabel noCardsLabel=new JLabel("You don't have any card to use.");
            mainPanel.add(noCardsLabel);
        }
        else{
            HashMap<Card,Integer> playerCards=player.getCards();
            JButton useKnight=new JButton("Knight");
            JButton useMonopoly=new JButton("Monopoly");
            JButton useYOP=new JButton("Year of Plenty");
            JButton useRoadBuilding=new JButton("Road Building");
            JButton useVP=new JButton("Victory Point");
            useKnight.addActionListener(event->{
                removeAndRefresh(false,true,false);
                setThief();
            });
            useMonopoly.addActionListener(event->{
                removeAndRefresh(false,true,false);
                chooseResource();
            });
            useYOP.addActionListener(event->{

                removeAndRefresh(false,true,true);
            });
            useRoadBuilding.addActionListener(event->{

            });
            useVP.addActionListener(event->{

                game.useCardVP(player);
            });
            mainPanel.add(useKnight);
            mainPanel.add(useMonopoly);
            mainPanel.add(useYOP);
            mainPanel.add(useRoadBuilding);
            mainPanel.add(useVP);
        }
        JButton returnToMenu=new JButton("Go back to the menu");
        returnToMenu.addActionListener(event->{
            removeAndRefresh(false,true,false);
            getAction(player);
        });
        mainPanel.add(returnToMenu);
    }

    @Override
    public void setThief(){
        JLabel placeThiefLabel=new JLabel("Place thief on the board");
        guiBoard.setAllTileAsListener(false,false,false,false,true);
    }

    // fonction de la Vue pour voler une ressource à un joueur après avoir déplacé le voleur
    @Override
    public void steal(Player p, Tile thiefTile){
        removeAndRefresh(false,true,false);
        ArrayList<Colony> ownedColonies=new ArrayList<>();
        for(Colony colony:thiefTile.getColonies()){
            if(colony.getPlayer()!=null && colony.getPlayer()!=p && !ownedColonies.contains(colony)){
                ownedColonies.add(colony);
            }
        }
        final Player[] playerOfColony=new Player[1];
        if(ownedColonies.size()!=0){
            if(ownedColonies.size()>1){
                JLabel choosePlayer=new JLabel("Choose a player to steal a random resource from");
                JButton next=new JButton("Continue");
                ButtonGroup buttonGroup=new ButtonGroup();
                ArrayList<JRadioButton> jRadioButtonList=new ArrayList<>();

                for(Colony colony:ownedColonies){
                    JRadioButton radioButton=new JRadioButton("Player "+colony.getPlayer());
                    jRadioButtonList.add(radioButton);
                    buttonGroup.add(radioButton);
                    mainPanel.add(radioButton);
                }
                jRadioButtonList.get(0).setSelected(true);
                mainPanel.add(next);
                next.addActionListener(actionEvent -> {
                    for(JRadioButton button:jRadioButtonList){
                        if(button.isSelected()){
                            for(Colony colony:ownedColonies){
                                String playerName="Player "+colony.getPlayer().toString();
                                if(playerName.equals(button.getActionCommand())){
                                    playerOfColony[0]=colony.getPlayer();
                                }
                            }
                            game.steal(p, playerOfColony[0]);
                            JButton returnToMenu=new JButton("Go back to the menu");
                            returnToMenu.addActionListener(event->{
                                removeAndRefresh(false,true,false);
                                getAction(p);
                            });
                        }
                    }
                    refreshPanel(this);
                });
            }
            else{
                playerOfColony[0]=ownedColonies.get(0).getPlayer();
                game.steal(p, playerOfColony[0]);
                JButton returnToMenu=new JButton("Go back to the menu");
                returnToMenu.addActionListener(event->{
                    removeAndRefresh(false,true,false);
                    getAction(p);
                });
            }
        }
    }

    @Override
    public void chooseResource(){
        final Player[] resourceChoice=new Player[1];
        Player player=launcher.getCurrentPlayer();
        JLabel choosePlayer=new JLabel("Please choose a resource");
        JButton next=new JButton("Continue");
        ButtonGroup buttonGroup=new ButtonGroup();
        ArrayList<JRadioButton> jRadioButtonList=new ArrayList<>();
        HashMap<String,Integer> resources=Board.generateHashMapRessource();

        for(String resource: resources.keySet()){
            JRadioButton radioButton=new JRadioButton(resource);
            jRadioButtonList.add(radioButton);
            buttonGroup.add(radioButton);
            mainPanel.add(radioButton);
        }
        jRadioButtonList.get(0).setSelected(true);
        mainPanel.add(next);
        next.addActionListener(actionEvent -> {
            for(JRadioButton button : jRadioButtonList){
                if(button.isSelected()){
                    game.useCardProgressMonopoly(player, new String[]{button.getActionCommand()});
                    JButton returnToMenu=new JButton("Go back to the menu");
                    returnToMenu.addActionListener(event -> {
                        removeAndRefresh(false, true, false);
                        getAction(player);
                    });
                }
            }
            refreshPanel(this);
        });
    }

    @Override
    public void victory(Player p){

    }

    // fonction de la Vue qui met à jour le plateau qui se trouve dans la classe guiBoard
    @Override
    public void displayBoard(Game game){
        guiBoard.repaint();
    }

    // fonction qui termine la partie
    public void endGame(Player currentPlayer){
        removeAndRefresh(true,true,false);
        JLabel playerWonLabel=new JLabel(currentPlayer+" has won the game !");
        playerWonLabel.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(playerWonLabel);
        refreshPanel(mainPanel);
    }

    // fonction qui selon les booléens en argument enlève tous les components d'un panel et le revalide
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
