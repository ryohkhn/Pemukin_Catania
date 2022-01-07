package vue;

import board.Tile;
import game.Card;
import game.Game;
import game.Player;

import java.util.HashMap;
import java.util.List;

public interface Vues{

// Les fonctions de cette interface sont redéfinies dans les différentes vues (GUI et CLI).

    void chooseCard();
    void chooseNbPlayers();

    void displayBoard(Game game);
    void displayDiceNumber(int diceNumber);
    void displayDrawnCard(Player player, Card randomCard);
    void displayOtherPlayers(Player player,Game game);
    void displayPlayer(Player player);
    void displayStolenResource(Player player, String resource, Player stolenPlayer,int quantity);
    void getAction(Player player);

    void getCityPlacement();
    void getRoadPlacement();
    void initialization(Game game);
    void setPlayers();

    void showBuildCost();
    void portSelection(Player player);
    void chooseResource();
    void getPortResource();
    void sevenAtDice(Player player,int quantity);
    void setThief();
    void steal(Player player, Tile thiefTile);
    void displayDiceProduction(HashMap<Player, List<String>> diceResultsProduction);
    void displayYopGivenResources(String resource, String resource1);
    void message(Player player, String type, String object, int error);
    void victory(Player player);

}
