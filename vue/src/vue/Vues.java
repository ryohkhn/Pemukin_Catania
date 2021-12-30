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
    int getAction(Player p);
    String[] ressourceToBeDiscarded(Player player,int quantity);
    int[] getRoadPlacement();
    int[] getCityPlacement();
    int[] getThiefPlacement();
    String chooseCard();
    String[] chooseResource(int number);
    void victory(Player p);
    void displayBoard(Game game);
    void displayPlayer(Player p);
    Player choosePlayerFromColony(ArrayList<Colony> ownedColonies);
    int portSelection(Player player);
    void displayDiceNumber(int diceNumber);
    void showBuildCost();

    void setPlayers();

    void initialization(Game game);
    int[] getColonyPlacement();

}
