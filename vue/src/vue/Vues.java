package vue;

import board.Colony;
import game.Game;
import game.Player;

import java.util.ArrayList;
import java.util.HashMap;

public interface Vues{
    void chooseNbPlayers();
    boolean chooseHuman();
    String chooseColor(HashMap<String, Boolean> color);
    String[] ressourceToBeDiscarded(Player player,int quantity);
    int[] getRoadPlacement();
    int[] getCityPlacement();
    int[] getThiefPlacement();
    String chooseCard();
    void victory(Player p);
    void displayBoard(Game game);
    void displayPlayer(Player p);
    Player choosePlayerFromColony(ArrayList<Colony> ownedColonies);
    void displayDiceNumber(int diceNumber);
    void showBuildCost();

    void setPlayers();
    void initialization(Game game);
    void getAction(Player p);
    void portSelection(Player player);
    void chooseResource();


    int[] getColonyPlacement();

    void getPortResource();
}
