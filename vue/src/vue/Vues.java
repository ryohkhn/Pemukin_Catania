package vue;

import board.Colony;
import game.Game;
import game.Player;

import java.util.ArrayList;
import java.util.HashMap;

public interface Vues{
    String[] ressourceToBeDiscarded(Player player,int quantity);
    void getRoadPlacement();
    void getCityPlacement();
    void getThiefPlacement();
    String chooseCard();
    void victory(Player p);
    void displayBoard(Game game);
    void displayPlayer(Player p);
    Player choosePlayerFromColony(ArrayList<Colony> ownedColonies);
    void displayDiceNumber(int diceNumber);
    void showBuildCost();

    void chooseNbPlayers();
    void setPlayers();
    void initialization(Game game);
    void getAction(Player p);
    void portSelection(Player player);
    void chooseResource();


    int[] getColonyPlacement();

    void getPortResource();
}
