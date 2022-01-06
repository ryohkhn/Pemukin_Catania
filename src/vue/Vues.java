package vue;

import board.Tile;
import game.Card;
import game.Game;
import game.Player;

import java.util.HashMap;
import java.util.List;

public interface Vues{

    void getRoadPlacement();
    void getCityPlacement();
    void chooseCard();
    void displayStolenResource(Player player, String resource, Player stolenPlayer,int quantity);
    void victory(Player player);
    void displayBoard(Game game);
    void displayPlayer(Player player);
    void displayOtherPlayers(Player player,Game game);
    void displayDiceNumber(int diceNumber);
    void showBuildCost();
    void chooseNbPlayers();
    void setPlayers();
    void initialization(Game game);
    void getAction(Player player);
    void portSelection(Player player);
    void chooseResource();
    void getPortResource();
    void sevenAtDice(Player player,int quantity);
    void setThief();
    void steal(Player player, Tile thiefTile);
    void displayDiceProduction(HashMap<Player, List<String>> diceResultsProduction);
    void displayYopGivenResources(String resource, String resource1);
    void message(Player player, String type, String object, int error);
    void displayDrawnCard(Player player, Card randomCard);

    //message template for the gui
    /*
    @Override
    public void message(Player p, String type, String object, int error) {
        if(!(p instanceof Bot)){
            if(type.equals("error")){
                if(object.equals("road")){
                }else if(object.equals("city")){
                    switch(error){
                        case 0 ->
                        case 1 ->
                        case 2 ->
                    }
                }else if(object.equals("card")){
                    switch(error){
                        case 0 ->
                        case 1 ->
                        case 2 ->
                    }
                }else if(object.equals("colony")){
                    switch(error){
                        case 0 ->
                        case 1 ->
                        case 2 ->
                    }
                }
            }if(type.equals("good")){
                if(object.equals("road")){
                    switch(error){
                        case 0 ->
                        case 1 ->
                        case 2 ->
                    }
                }else if(object.equals("city")){
                    switch(error){
                        case 0 ->
                        case 1 ->
                        case 2 ->
                    }
                }else if(object.equals("card")){
                    switch(error){
                        case 0 ->
                        case 1 ->
                        case 2 ->
                    }
                }else if(object.equals("colony")){
                    switch(error){
                        case 0 ->
                        case 1 ->
                        case 2 ->
                    }
                }
            }
        }
    }
     */
}
