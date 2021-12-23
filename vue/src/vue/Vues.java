package vue;

import board.Colony;
import game.Player;
import game.Game;

import java.util.ArrayList;
import java.util.HashMap;

public interface Vues{
    int chooseNbPlayers();
    boolean chooseHuman();
    String chooseColor(HashMap<String, Boolean> color);
    void initialisation();
    int getAction(Player p);
    String ressourceToBeDefaussed();
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
}
