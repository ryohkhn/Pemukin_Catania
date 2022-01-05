package game;

import board.*;
import vue.Vues;

import java.util.*;

public class Game {
    private Player[] players;
    private Board board;
    public HashMap<Colony, Player> secondRoundBuildedColonies=new HashMap<>();
    private Vues vue;

    public Game(int nb,Vues vue) {
        board=new Board();
        players=new Player[nb];
        this.vue=vue;
    }

    public Board getBoard() {
        return board;
    }

    public Player[] getPlayers() {
        return players;
    }

    // fonction construisant une route pour un joueur
    public boolean buildRoad(Player player, int[] placement) {
        int line=placement[0];
        int column=placement[1];
        int roadNumber=placement[2];
        Road choosedRoad=board.getTiles()[line][column].getRoads().get(roadNumber);
        if(choosedRoad.isOwned()) {
            vue.message(player,"error","road", 0 );
            return false;
        }
        int clayStock=player.resources.get("Clay");
        int woodStock=player.resources.get("Wood");
        if(clayStock>=1&&woodStock>=1) {
            if(player.canBuildPropertie("Road", 15)) {
                if(choosedRoad.isBuildable(player)) {
                    choosedRoad.setPlayer(player);
                    player.resources.merge("Clay", 1, (initialValue, valueRemoved) -> initialValue-valueRemoved);
                    player.resources.merge("Wood", 1, (initialValue, valueRemoved) -> initialValue-valueRemoved);
                    player.addPropertie("Road");
                    vue.message(player,"good","road", 0 );
                    return true;
                } else {
                    vue.message(player,"error","road", 1 );
                }
            } else {
                vue.message(player,"error","road", 2 );
            }
        } else {
            vue.message(player,"error","road", 3 );
        }
        return false;
    }

    // fonction qui vérife si le joueur a les ressources nécessaires pour construire une route
    public boolean hasResourcesForRoad(Player player){
       int clayStock=player.resources.get("Clay");
       int woodStock=player.resources.get("Wood");
       if(clayStock>=1 && woodStock>=1){
           return true;
       }
       // message d'erreur
       return false;
    }

    // fonction construisant une colonie pour un joueur
    // si la colonie est un port il est ajouté au hashmap du joueur
    public boolean buildColony(Player player, int[] placement) {
        int line=placement[0];
        int column=placement[1];
        int colonyNumber=placement[2];
        Colony choosedColony=board.getTiles()[line][column].getColonies().get(colonyNumber);
        if(choosedColony.isOwned()) {
            vue.message(player,"error","colony", 0 );
            return false;
        }
        int clayStock=player.resources.get("Clay");
        int wheatStock=player.resources.get("Wheat");
        int woodStock=player.resources.get("Wood");
        int woolStock=player.resources.get("Wool");
        if(clayStock>=1&&wheatStock>=1&&woodStock>=1&&woolStock>=1) {
            if(player.canBuildPropertie("Colony", 5)) {
                if(choosedColony.isBuildable(player)) {
                    if(choosedColony.isPort()) {
                        player.addPort(choosedColony.getLinkedPort());
                    }
                    choosedColony.setPlayer(player);
                    player.resources.merge("Clay", 1, (initialValue, valueRemoved) -> initialValue-valueRemoved);
                    player.resources.merge("Wood", 1, (initialValue, valueRemoved) -> initialValue-valueRemoved);
                    player.resources.merge("Wheat", 1, (initialValue, valueRemoved) -> initialValue-valueRemoved);
                    player.resources.merge("Wool", 1, (initialValue, valueRemoved) -> initialValue-valueRemoved);
                    player.addPropertie("Colony");
                    player.addVictoryPoint(1);
                    if(player instanceof Bot) ((Bot) player).colonies.put(choosedColony,placement);
                    vue.message(player,"good","colony", 0 );
                    return true;
                } else {
                    vue.message(player,"error","colony", 1 );
                }
            } else {
                vue.message(player,"error","colony", 2 );
            }
        } else {
            vue.message(player,"error","colony", 3 );
        }
        return false;
    }

    // fonction qui vérife si le joueur a les ressources nécessaires pour construire une colonie
    public boolean hasResourcesForColony(Player player){
        int clayStock=player.resources.get("Clay");
        int wheatStock=player.resources.get("Wheat");
        int woodStock=player.resources.get("Wood");
        int woolStock=player.resources.get("Wool");
        if(clayStock>=1 && wheatStock>=1 && woodStock>=1 && woolStock>=1){
            return true;
        }
        System.out.println("Vous n'avez pas la quantité suffisante de ressources pour constuire une colonie.");
        return false;
    }

    // fonction construisant une ville pour un joueur aux coordonnées en argument
    public boolean buildCity(Player player, int[] placement) {
        int line=placement[0];
        int column=placement[1];
        int colonyNumber=placement[2];
        Colony chosenColony=board.getTiles()[line][column].getColonies().get(colonyNumber);
        if(!chosenColony.isOwned(player)) {
            vue.message(player, "error", "city", 0);
            return false;
        }
        if(player.canBuildPropertie("City", 4)) {
            chosenColony.setAsCity();
            player.resources.merge("Wheat",2,((initialValue, valueRemoved) -> initialValue-valueRemoved));
            player.resources.merge("Ore",3,((initialValue, valueRemoved) -> initialValue-valueRemoved));
            player.addPropertie("City");
            player.removeColonyInCounter();
            //ajoute 1 point (prend toujours en compte le point de la colonie qu'était la ville).
            player.addVictoryPoint(1);
            vue.message(player, "good", "city", 0);
            return true;
        } else {
            vue.message(player, "error", "city", 1);
        }
        return false;
    }

    // TODO: 05/01/2022 verifier hasResourcesForCity() avant d'appeler la contruction dans cli/controller ET dans bot
    // fonction qui vérife si le joueur a les ressources nécessaires pour construire une ville
    public boolean hasResourcesForCity(Player player){
        int oreStock=player.resources.get("Ore");
        int wheatStock=player.resources.get("Wheat");
        if(wheatStock>=2 && oreStock>=3) {
            return true;
        }
        vue.message(player, "error", "city", 2);
        return false;
    }

    // fonction donnant aux joueurs les ressources produites si leur colonie se trouve sur la case de l'id en argument
    public void diceProduction(int diceNumber) {
        HashMap<Player, List<String>> diceResultsProduction=new HashMap<>();
        for(Tile[] tiles : board.getTiles()) {
            for(Tile tile : tiles) {
                if(tile.getId()==diceNumber) {
                    if(!tile.hasThief()  && !tile.getRessource().equals("")) {
                        String ressource=tile.getRessource();
                        for(Colony colony : tile.getColonies()) {
                            int producedValue=colony.isCity()?2:1;
                            if(colony.isOwned()) {
                                List<String> resourcesAcquired=diceResultsProduction.get(colony.getPlayer());
                                if(resourcesAcquired!=null){
                                    resourcesAcquired.add(ressource+" + "+producedValue);
                                    diceResultsProduction.replace(colony.getPlayer(),resourcesAcquired);
                                }
                                else{
                                    ArrayList<String> resources=new ArrayList<>();
                                    resources.add(ressource+" + "+producedValue);
                                    diceResultsProduction.put(colony.getPlayer(),resources);
                                }
                                colony.getPlayer().resources.merge(ressource, producedValue, Integer::sum);
                            }
                        }
                    }
                }
            }
        }
        vue.displayDiceProduction(diceResultsProduction);
    }

    public void destroy(Player p, String resource) {
        p.resources.merge(resource, 1, (initialValue, valueRemoved) -> initialValue-valueRemoved);
    }

    public void setThief(int[] placement) {
        int line=placement[0];
        int column=placement[1];
        Tile chosenTile=board.getTiles()[line][column];
        board.getThiefTile().setThief(false);
        board.setThiefTile(chosenTile);
        chosenTile.setThief(true);
    }

    public boolean hasResourcesForCard(Player player){
        int oreStock=player.resources.get("Ore");
        int woolStock=player.resources.get("Wool");
        int wheatStock=player.resources.get("Wheat");
        if(oreStock>=1 && woolStock>=1 && wheatStock>=1) {
            return true;
        }
        vue.message(player, "error", "card", 0);
        return false;
    }

    // TODO une carte achetée dans le round ne peut pas être jouée directement
    // fonction permettant d'acheter une carte de développement
    public void buyCard(Player player) {
        player.resources.merge("Ore", 1, (initialValue, valueRemoved) -> initialValue-valueRemoved);
        player.resources.merge("Wool", 1, (initialValue, valueRemoved) -> initialValue-valueRemoved);
        player.resources.merge("Wheat", 1, (initialValue, valueRemoved) -> initialValue-valueRemoved);
        Card randomCard=Card.randomCard();
        vue.displayDrawnCard(player,randomCard);
        player.cards.merge(randomCard, 1, Integer::sum);
    }

    public boolean hasChosenCard(Player p, Card chosenCard){
        if(p.cards.get(chosenCard)>0) return true;
        return false;
        /*if(p.cards.get(chosenCard)>p.cardsDrawnThisTurn.get(chosenCard)) {
            vue.message(p, "error", "card", 1);
            return false;
        }
        return true;

         */
    }

    // fonction permettant d'utiliser une carte point de victoire
    public void useCardVP(Player turnPlayer) {
        turnPlayer.addVictoryPoint(1);
        turnPlayer.removeCard(Card.VictoryPoint);
        turnPlayer.alreadyPlayedCardThisTurn=true;
    }

    public boolean isRoadBuildable(int[] placement,Player p){
        return this.board.getTiles()[placement[0]][placement[1]].getRoads().get(placement[2]).isBuildable(p);
    }

    public void useCardKnight(Player turnPlayer,int[] placement) {
        turnPlayer.knightPlayed+=1;
        this.setThief(placement);
        turnPlayer.removeCard(Card.Knight);
        turnPlayer.alreadyPlayedCardThisTurn=true;
    }

    public void useCardProgressYearOfPlenty(Player turnPlayer, String[] resources) {
        turnPlayer.resources.merge(resources[0], 1, Integer::sum);
        turnPlayer.resources.merge(resources[1], 1, Integer::sum);
        vue.displayYopGivenResources(resources[0],resources[1]);
        turnPlayer.removeCard(Card.ProgressYearOfPlenty);
        turnPlayer.alreadyPlayedCardThisTurn=true;
    }

    public void useCardProgressRoadBuilding(Player turnPlayer, int[] placement) {
        turnPlayer.resources.merge("Clay", 2, Integer::sum);
        turnPlayer.resources.merge("Wood", 2, Integer::sum);
        buildRoad(turnPlayer, placement);
        turnPlayer.removeCard(Card.ProgressRoadBuilding);
        turnPlayer.alreadyPlayedCardThisTurn=true;
    }

    public void useCardProgressRoadBuildingSecondRound(Player turnPlayer, int[] placement){
        buildRoad(turnPlayer, placement);
    }

    public void useCardProgressMonopoly(Player turnPlayer, String[] resource) {
        for(Player player : players) {
            if(player.resources.get(resource[0])>0 && player!=turnPlayer) {
                int playerResourceQuantity=player.resources.get(resource[0]);
                player.resources.merge(resource[0],playerResourceQuantity, (initialValue, valueRemoved) -> initialValue-valueRemoved);
                vue.displayStolenResource(turnPlayer,resource[0],turnPlayer,playerResourceQuantity);
                turnPlayer.resources.merge(resource[0], playerResourceQuantity, Integer::sum);
            }
        }
        turnPlayer.removeCard(Card.ProgressMonopoly);
        turnPlayer.alreadyPlayedCardThisTurn=true;
    }

    // fonction qui permet de faire une échange avec un le port si le joueur en possède un
    public void trade(Player player, Port chosenPort, String portResource, String[] playerResource) {
        if(chosenPort==null) {
            if(player.resources.get(portResource)>=4) {
                player.resources.merge(portResource, 4, (initialValue, valueRemoved) -> initialValue-valueRemoved);
                player.resources.merge(playerResource[0], 1, Integer::sum);
            } else {
                vue.message(player, "error", "trade", 0);
            }
        } else if(chosenPort.getRate()==2) {
            if(player.resources.get(playerResource[0])>=2) {
                if(chosenPort.getRessource().equals(portResource)) {
                    player.resources.merge(portResource, 2, (initialValue, valueRemoved) -> initialValue-valueRemoved);
                    player.resources.merge(playerResource[0], 1, Integer::sum);
                } else {
                    vue.message(player, "error", "trade", 1);
                }
            } else {
                vue.message(player, "error", "trade", 0);
            }
        } else { //cas ou le port echange en 3:1
            if(player.resources.get(portResource)>=3) {
                player.resources.merge(portResource, 3, (initialValue, valueRemoved) -> initialValue-valueRemoved);
                player.resources.merge(playerResource[0], 1, Integer::sum);
            } else {
                vue.message(player, "error", "trade", 0);
            }
        }
    }

    public Colony buildColonyInitialization(Player player, int[] placement) {
        int line=placement[0];
        int column=placement[1];
        int colonyNumber=placement[2];
        Colony chosenColony=board.getTiles()[line][column].getColonies().get(colonyNumber);
        if(chosenColony.isOwned()) {
            vue.message(player, "error", "colony", 0);
            return null;
        }
        if(chosenColony.isBuildableInitialization(player)) {
            if(chosenColony.isPort()) {
                player.addPort(chosenColony.getLinkedPort());
            }
            chosenColony.setPlayer(player);
            player.addPropertie("Colony");
            player.addVictoryPoint(1);
            if(player instanceof Bot) ((Bot) player).colonies.put(chosenColony,placement);
            return chosenColony;
        } else {
            vue.message(player, "error", "colony", 4);
        }
        return null;
    }

    // fonction construisant une route pour un joueur
    public boolean buildRoadInitialization(Player player, Colony colony, int[] placement) {
        int line=placement[0];
        int column=placement[1];
        int roadNumber=placement[2];
        Road chosenRoad=board.getTiles()[line][column].getRoads().get(roadNumber);
        if(chosenRoad.isOwned()) {
            vue.message(player, "error", "road", 0);
            return false;
        }
        if(chosenRoad.isBuildableInitialization(colony)) {
            chosenRoad.setPlayer(player);
            player.addPropertie("Road");
            return true;
        } else {
            vue.message(player, "error", "road", 0);
        }
        return false;
    }

    // fonction d'initialisation: tous les joueurs construisent deux colonies et deux routes
    // on donne ensuite les ressources des cases autour des colonies construites aux joueurs
    /*
    public void initialization(){
        Colony buildedColony;
        boolean buildedRoad;
        HashMap<Colony,Player> secondRoundBuildedColonies=new HashMap<>();
        for(int i=0; i<players.length; i++){
            this.vueGenerale.displayPlayer(players[i]);
            this.vueGenerale.displayBoard(this);
            do{
                buildedColony=buildColonyInitialization(players[i]);
            }
            while(buildedColony==null);
            this.vueGenerale.displayPlayer(players[i]);
            this.vueGenerale.displayBoard(this);
            do{
                buildedRoad=buildRoadInitialization(players[i], buildedColony);
            }
            while(!buildedRoad);
        }
        for(int i=players.length-1; i>=0; i--){
            this.vueGenerale.displayPlayer(players[i]);
            this.vueGenerale.displayBoard(this);
            do{
                buildedColony=buildColonyInitialization(players[i]);
            }
            while(buildedColony==null);
            secondRoundBuildedColonies.put(buildedColony,players[i]);
            this.vueGenerale.displayPlayer(players[i]);
            this.vueGenerale.displayBoard(this);
            do{
                buildedRoad=buildRoadInitialization(players[i], buildedColony);
            }
            while(!buildedRoad);
        }
        coloniesProduction(secondRoundBuildedColonies);
    }

     */

    // fonction qui parcourt le tableau et donne les ressources aux joueurs de toutes les cases autour des colonies
    public void coloniesProduction() {
        HashMap<Colony, ArrayList<String>> result=new HashMap<>();
        for(int line=0; line<board.getTiles().length; line++) {
            for(int column=0; column<board.getTiles()[line].length; column++) {
                for(Colony colony : board.getTiles()[line][column].getColonies()) {
                    if(secondRoundBuildedColonies.containsKey(colony)) {
                        String resource=board.getTiles()[line][column].getRessource();
                        ArrayList<String> resources;
                        if(result.containsKey(colony)) {
                            resources=result.get(colony);
                            resources.add(resource);
                            result.replace(colony, resources);
                        } else {
                            resources=new ArrayList<>();
                            resources.add(resource);
                            result.put(colony, resources);
                        }
                    }
                }
            }
        }
        result.forEach((colony, ressources) -> {
            for(String resource : ressources) {
                if(!resource.equals("")) {
                    colony.getPlayer().resources.merge(resource, 1, Integer::sum);
                }
            }
        });
    }

    public void checkLongestArmy() {
        boolean cardOwned=false;
        int knightPlayedCount=0;
        Player playerOwningCard=null;
        Player nextPlayer=null;
        for(Player player : players) {
            if(player!=null && player.cards.get(Card.LargestArmy)>0) {
                cardOwned=true;
                knightPlayedCount=player.knightPlayed;
                playerOwningCard=player;
            }
        }
        for(Player player : players) {
            if(player!=null){
                if(cardOwned) {
                    if(player.knightPlayed>knightPlayedCount) {
                        nextPlayer=player;
                        knightPlayedCount=nextPlayer.knightPlayed;
                    }
                } else {
                    if(player.knightPlayed>=3) {
                        nextPlayer=player;
                    }
                }
            }
        }
        if(playerOwningCard!=nextPlayer) {
            if(playerOwningCard!=null) {
                vue.message(playerOwningCard, "error", "card", 2);
                playerOwningCard.removeCard(Card.LargestArmy);
                playerOwningCard.victoryPoint-=2;
            }
            if(nextPlayer!=null) {
                vue.message(nextPlayer, "error", "card", 2);
                nextPlayer.addCard(Card.LargestArmy);
                nextPlayer.victoryPoint+=2;
            }
        }
    }

    public void setPlayers(String[] playersType) {
        for(int i=0; i<this.players.length; i++) {
            switch(playersType[i]) {
                case "1" -> this.players[i]=new Human(null);
                default -> this.players[i]=new Bot(null);
            }
        }
    }
/*
    public void botsGetColor(HashMap<String, Boolean> color) {
        for(int i=0; i<this.players.length; i++) {
            if(this.getPlayers()[i] instanceof Bot) {
                if(color.replace("blue", false, true)) ((Bot) this.players[i]).setColor("blue");
                else if(color.replace("green", false, true)) ((Bot) this.players[i]).setColor("green");
                else if(color.replace("yellow", false, true)) ((Bot) this.players[i]).setColor("yellow");
                else if(color.replace("orange", false, true)) ((Bot) this.players[i]).setColor("orange");
            }
        }
    }
*/
    public void setColors(String[] playersColor) {
        for(int i=0; i<this.players.length; i++) {
            this.players[i].setColor(playersColor[i]);
        }
    }

    public void steal(Player p, Player playerOfColony) {
        String resource;
        if(playerOfColony.resourceCount()>0) {
            do {
                resource=Board.generateRandomRessource();
            }
            while(playerOfColony.resources.get(resource)==0);
            playerOfColony.resources.merge(resource, 1, (initialValue, valueRemoved) -> initialValue-valueRemoved);
            p.resources.merge(resource, 1, Integer::sum);
            vue.displayStolenResource(p,resource,playerOfColony,1);
        }
    }

    public Colony getColonyFromPlayer(Player player){
        for(Colony colony:secondRoundBuildedColonies.keySet()){
            if(secondRoundBuildedColonies.get(colony)==player){
                return colony;
            }
        }
        return null;
    }

    public int generateDiceNumber(){
        Random rand=new Random();
        return(rand.nextInt(6)+1+rand.nextInt(6)+1);
    }

    public void removeResourcesFromPlayer(Player player,int[] quantity){
        LinkedList<String> resources=Board.generateListResource();
        for(int i=0; i<resources.size(); i++){
            player.resources.merge(resources.get(i),quantity[i],(initialValue,valueRemoved)->initialValue-valueRemoved);
        }
    }
}

/*

Tentative de calcul de la route la plus longue, je laisse au cas ou on en ait encore besoin.


    public void addPointsForLongestRoad(Player p){
        this.removeLongestRoad();
        p.victoryPoint+=2;
        p.longestRoad=true;
    }

    private void removeLongestRoad() {
        for(Player p : this.players){
            if(p.longestRoad){
                p.victoryPoint-=2;
                p.longestRoad=false;
            }
        }
    }

    public void longestRoad(){
        int[] roadLength=new int[this.players.length];
        for(int i=0;i<this.players.length;i++){
            roadLength[i]=this.longestRoad(this.players[i]);
        }
        int index=0;
        boolean equalLength=false;
        for(int j=1;j<roadLength.length;j++){
            if(roadLength[j]>roadLength[index]) {
                index=j;
                if(equalLength) equalLength=false;
            }else if(roadLength[j]==roadLength[index]){
                if(this.players[j].longestRoad) {
                    index=j;
                    if(equalLength) equalLength=false;
                }
                else if(!this.players[index].longestRoad) equalLength=true;
            }
        }
        if(!equalLength){
            addPointsForLongestRoad(this.players[index]);
        }
    }

    public int longestRoad(Player p){
        int res=0;
        for(int x=0;x<4;x++){ //line index
            for(int y=0;y<4;y++){ //column index
                for(int i=0;i<4;i++){ //road index
                    if(this.board.getTiles()[x][y].getRoads().get(i).getPlayer().equals(p)) {
                        LinkedList<Road> list=new LinkedList<>();
                        list.add(this.board.getTiles()[x][y].getRoads().get(i));
                        res=Math.max(res, this.roadLength(p, x, y, i,list));
                    }
                }
            }
        }
        return res;
    }

    private int roadLength(Player p, int x, int y, int i, LinkedList<Road> list) {

    }

 */
