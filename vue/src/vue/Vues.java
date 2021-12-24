package vue;

import board.Colony;
import board.Port;
import game.Player;
import game.Game;

import java.util.ArrayList;
import java.util.HashMap;

public interface Vues{
    int chooseNbPlayers();
    boolean chooseHuman();
    String chooseColor(HashMap<String, Boolean> color);
    int getAction(Player p);
    String[] ressourceToBeDiscarded(Player player,int quantity);
    int[] getRoadPlacement();
    int[] getColonyPlacement();
    int[] getCityPlacement();
    int[] getThiefPlacement();
    String chooseCard();
    String[] chooseResource(int number);
    void victory(Player p);
    void displayBoard(Game game);
    void displayPlayer(Player p);
    Player choosePlayerFromColony(ArrayList<Colony> ownedColonies);
    Port portSelection(Player player);
    void displayDiceNumber(int diceNumber);
}
