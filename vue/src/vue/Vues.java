package vue;

import game.Player;
import board.Port;

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
    String[] choose2Ressources();
    String chooseCard();
    void victory(Player p);
}
