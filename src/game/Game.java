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

    public Vues getVue(){
        return vue;
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

    // fonction qui vérifie si le joueur a les ressources nécessaires pour construire une route
    public boolean hasResourcesForRoad(Player player){
       int clayStock=player.resources.get("Clay");
       int woodStock=player.resources.get("Wood");
       if(clayStock>=1 && woodStock>=1){
           return true;
       }
       vue.message(player,"error","road", 3 );
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

    // fonction qui vérifie si le joueur a les ressources nécessaires pour construire une colonie
    public boolean hasResourcesForColony(Player player){
        int clayStock=player.resources.get("Clay");
        int wheatStock=player.resources.get("Wheat");
        int woodStock=player.resources.get("Wood");
        int woolStock=player.resources.get("Wool");
        if(clayStock>=1 && wheatStock>=1 && woodStock>=1 && woolStock>=1){
            return true;
        }
        vue.message(player,"error","colony", 3 );
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

    // fonction qui vérifie si le joueur a les ressources nécessaires pour construire une ville
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

    // fonction détruisant une ressource (en argument) pour le joueur (en argument)
    public void destroy(Player player, String resource) {
        player.resources.merge(resource, 1, (initialValue, valueRemoved) -> initialValue-valueRemoved);
    }

    // fonction qui déplace le voleur aux coordonnées en argument
    public void setThief(int[] placement) {
        int line=placement[0];
        int column=placement[1];
        Tile chosenTile=board.getTiles()[line][column];
        board.getThiefTile().setThief(false);
        board.setThiefTile(chosenTile);
        chosenTile.setThief(true);
    }

    // fonction qui vérifie si le joueur a les ressources nécessaires pour acheter une carte
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

    // fonction permettant d'acheter une carte de développement
    public void buyCard(Player player) {
        // On supprime les ressources nécessaires à l'achat d'une carte
        player.resources.merge("Ore", 1, (initialValue, valueRemoved) -> initialValue-valueRemoved);
        player.resources.merge("Wool", 1, (initialValue, valueRemoved) -> initialValue-valueRemoved);
        player.resources.merge("Wheat", 1, (initialValue, valueRemoved) -> initialValue-valueRemoved);
        Card randomCard=Card.randomCard();
        vue.displayDrawnCard(player,randomCard);
        if(randomCard!=Card.VictoryPoint) { //si la carte n'est pas une carte VP,
            player.cardsDrawnThisTurn.merge(randomCard, 1, Integer::sum); // on l'ajoute à la hashmap de stockage des cartes achetées dans le tour.
        }
        player.cards.merge(randomCard, 1, Integer::sum); //on ajoute la carte à la hashmap de stockage des cartes du joueur.
    }

    // fonction qui vérifie que le joueur (en argument) a la carte (en argument)
    public boolean hasChosenCard(Player player, Card chosenCard){
        if(player.cards.get(chosenCard)>player.cardsDrawnThisTurn.getOrDefault(chosenCard,0)){
            return true;
        }
        vue.message(player, "error", "card", 1);
        return false;
    }

    // fonction permettant d'utiliser une carte point de victoire
    public void useCardVP(Player turnPlayer) {
        turnPlayer.addVictoryPoint(1);
        turnPlayer.removeCard(Card.VictoryPoint);
        turnPlayer.alreadyPlayedCardThisTurn=true;
    }

    // fonction qui vérifie qu'une route est constructible
    public boolean isRoadBuildable(int[] placement,Player player){
        return this.board.getTiles()[placement[0]][placement[1]].getRoads().get(placement[2]).isBuildable(player);
    }

    // fonction permettant d'utiliser une carte Chevalier
    public void useCardKnight(Player turnPlayer,int[] placement) {
        turnPlayer.knightPlayed+=1;
        this.setThief(placement);
        turnPlayer.removeCard(Card.Knight);
        turnPlayer.alreadyPlayedCardThisTurn=true;
    }

    // fonction permettant d'utiliser une carte Invention
    public void useCardProgressYearOfPlenty(Player turnPlayer, String[] resources) {
        turnPlayer.resources.merge(resources[0], 1, Integer::sum);
        turnPlayer.resources.merge(resources[1], 1, Integer::sum);
        vue.displayYopGivenResources(resources[0],resources[1]);
        turnPlayer.removeCard(Card.ProgressYearOfPlenty);
        turnPlayer.alreadyPlayedCardThisTurn=true;
    }

    // fonction permettant d'utiliser une carte Construction de routes et de construire la 1ere route
    public void useCardProgressRoadBuilding(Player turnPlayer, int[] placement) {
        turnPlayer.resources.merge("Clay", 2, Integer::sum);
        turnPlayer.resources.merge("Wood", 2, Integer::sum);
        buildRoad(turnPlayer, placement);
        turnPlayer.removeCard(Card.ProgressRoadBuilding);
        turnPlayer.alreadyPlayedCardThisTurn=true;
    }

    // fonction permettant de construire la 2ᵉ route d'une carte Construction de routes.
    public void useCardProgressRoadBuildingSecondRound(Player turnPlayer, int[] placement){
        buildRoad(turnPlayer, placement);
    }
    // fonction permettant d'utiliser une carte Monopole
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

    // fonction qui permet de faire un échange avec un le port si le joueur en possède un
    public void trade(Player player, Port chosenPort, String portResource, String[] playerResource) {
        if(chosenPort==null) { // échange en 4:1
            if(player.resources.get(portResource)>=4) {
                player.resources.merge(portResource, 4, (initialValue, valueRemoved) -> initialValue-valueRemoved);
                player.resources.merge(playerResource[0], 1, Integer::sum);
            } else {
                vue.message(player, "error", "trade", 0);
            }
        } else if(chosenPort.getRate()==2) { // cas où le port échange en 2:1
            if(player.resources.get(portResource)>=2) {
                if(chosenPort.getRessource().equals(portResource)) {
                    player.resources.merge(portResource, 2, (initialValue, valueRemoved) -> initialValue-valueRemoved);
                    player.resources.merge(playerResource[0], 1, Integer::sum);
                } else {
                    vue.message(player, "error", "trade", 1);
                }
            } else {
                vue.message(player, "error", "trade", 0);
            }
        } else { // cas où le port échange en 3:1
            if(player.resources.get(portResource)>=3) {
                player.resources.merge(portResource, 3, (initialValue, valueRemoved) -> initialValue-valueRemoved);
                player.resources.merge(playerResource[0], 1, Integer::sum);
            } else {
                vue.message(player, "error", "trade", 0);
            }
        }
    }

    // fonction construisant une colonie lors de l'initialisation
    public Colony buildColonyInitialization(Player player, int[] placement) {
        int line=placement[0];
        int column=placement[1];
        int colonyNumber=placement[2];
        Colony chosenColony=board.getTiles()[line][column].getColonies().get(colonyNumber);
        if(chosenColony.isOwned()) {
            vue.message(player, "error", "colony", 0);
            return null;
        }
        if(chosenColony.isBuildableInitialization()) {
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

    // fonction construisant une route lors de l'initialisation
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
            vue.message(player, "error", "road", 1);
        }
        return false;
    }

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

    // fonction vérifiant quel joueur a la plus grande armée (et la carte qui va avec)
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
                vue.message(nextPlayer, "good", "card", 0);
                nextPlayer.addCard(Card.LargestArmy);
                nextPlayer.victoryPoint+=2;
            }
        }
    }

    // fonction qui défini si le joueur est humain ou une IA
    public void setPlayers(String[] playersType) {
        for(int i=0; i<this.players.length; i++) {
            switch(playersType[i]) {
                case "1" -> this.players[i]=new Human(null);
                default -> this.players[i]=new Bot(null);
            }
        }
    }

    // fonction qui défini la couleur des joueurs
    public void setColors(String[] playersColor) {
        for(int i=0; i<this.players.length; i++) {
            this.players[i].setColor(playersColor[i]);
        }
    }

    // fonction qui permet le vol d'une ressource d'un joueur a un autre joueur lors du 7 aux dés
    public void steal(Player player, Player playerOfColony) {
        String resource;
        if(playerOfColony.resourceCount()>0) {
            do {
                resource=Board.generateRandomRessource();
            }
            while(playerOfColony.resources.get(resource)==0);
            playerOfColony.resources.merge(resource, 1, (initialValue, valueRemoved) -> initialValue-valueRemoved);
            player.resources.merge(resource, 1, Integer::sum);
            vue.displayStolenResource(player,resource,playerOfColony,1);
        }else{
            vue.message(player,"error","trade", 2);
        }
    }

    // fonction qui return la seconde colonie du joueur
    public Colony getColonyFromPlayer(Player player){
        for(Colony colony:secondRoundBuildedColonies.keySet()){
            if(secondRoundBuildedColonies.get(colony)==player){
                return colony;
            }
        }
        return null;
    }

    // fonction simulant le lancé des deux dés
    public int generateDiceNumber(){
        Random rand=new Random();
        return(rand.nextInt(6)+1+rand.nextInt(6)+1);
    }

    // fonction qui supprime uen quantité d'une ressource d'un joueur
    public void removeResourcesFromPlayer(Player player,int[] quantity){
        LinkedList<String> resources=Board.generateListResource();
        for(int i=0; i<resources.size(); i++){
            player.resources.merge(resources.get(i),quantity[i],(initialValue,valueRemoved)->initialValue-valueRemoved);
        }
    }
}

/*

Tentative de calcul de la route la plus longue


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
