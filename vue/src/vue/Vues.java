package vue;

import board.Tile;
import game.Game;
import game.Player;

import java.util.HashMap;

public interface Vues{

    void getRoadPlacement();
    void getCityPlacement();
    void chooseCard();
    void victory(Player p);
    void displayBoard(Game game);
    void displayPlayer(Player p);
    void displayDiceNumber(int diceNumber);
    void showBuildCost();
    void chooseNbPlayers();
    void setPlayers();
    void initialization(Game game);
    void getAction(Player player);
    void portSelection(Player player);
    void chooseResource();
    void getPortResource();
    void sevenAtDice(Player p,int quantity);
    void setThief();
    void steal(Player p, Tile thiefTile);
    void displayDiceProduction(HashMap<Player,String> diceResultsProduction);
}
