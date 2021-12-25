package game;

import board.*;
import vue.Vues;
import java.util.ArrayList;
import java.util.HashMap;

public class Game{
    private Player[] players;
    private Board board;
    private Vues vueGenerale;

    public Game(int nb){
        board=new Board();
        players=new Player[nb];
    }

    public void setVueGenerale(Vues vueGenerale) {
        this.vueGenerale=vueGenerale;
    }

    public Board getBoard() {
        return board;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(int index,Player p) {
        this.players[index]=p;
    }

    // fonction construisant une route pour un joueur
    public boolean buildRoad(Player player){
        int[] placement=vueGenerale.getRoadPlacement();
        int line=placement[0];
        int column=placement[1];
        int roadNumber=placement[2];
        Road choosedRoad=board.getTiles()[line][column].getRoads().get(roadNumber);
        if(choosedRoad.isOwned()){
            return false;
        }
        int clayStock=player.ressources.get("Clay");
        int woodStock=player.ressources.get("Wood");
        if(clayStock>=1 && woodStock>=1){
            if(player.canBuildPropertie("Road",15)){
                if(choosedRoad.isBuildable(player)){
                    choosedRoad.setPlayer(player);
                    player.ressources.merge("Clay",1,(resourceStock,decreasedValue)->resourceStock-decreasedValue);
                    player.ressources.merge("Wood",1,(resourceStock,decreasedValue)->resourceStock-decreasedValue);
                    player.addPropertie("Road");
                    System.out.println("Route construite.");
                    return true;
                }
                else{
                    System.out.println("Vous ne pouvez pas constuire de route ici.");
                }
            }
            else{
                System.out.println("Vous ne pouvez pas constuire de route, vous avez atteint la quantité maximum possible.");
            }
        }
        else{
            System.out.println("Vous n'avez pas la quantité suffisante de ressources pour constuire une route.");
        }
        return false;
    }

    // fonction construisant une colonie pour un joueur
    // si la colonie est un port il est ajouté au hashmap du joueur
    public boolean buildColony(Player player){
        int[] placement=vueGenerale.getColonyPlacement();
        int line=placement[0];
        int column=placement[1];
        int colonyNumber=placement[2];
        Colony choosedColony=board.getTiles()[line][column].getColonies().get(colonyNumber);
        if(choosedColony.isOwned()){
            return false;
        }
        int clayStock=player.ressources.get("Clay");
        int wheatStock=player.ressources.get("Wheat");
        int woodStock=player.ressources.get("Wood");
        int woolStock=player.ressources.get("Wool");
        if(clayStock>=1 && wheatStock>=1 && woodStock>=1 && woolStock>=1){
            if(player.canBuildPropertie("Colony",5)){
                if(choosedColony.isBuildable(player)){
                    if(choosedColony.isPort()){
                        player.addPort(choosedColony.getLinkedPort());
                    }
                    choosedColony.setPlayer(player);
                    player.ressources.merge("Clay",1,(resourceStock,valueDecreased)->resourceStock-valueDecreased);
                    player.ressources.merge("Wood",1,(resourceStock,valueDecreased)->resourceStock-valueDecreased);
                    player.ressources.merge("Wheat",1,(resourceStock,valueDecreased)->resourceStock-valueDecreased);
                    player.ressources.merge("Wool",1,(resourceStock,valueDecreased)->resourceStock-valueDecreased);
                    player.addPropertie("Colony");
                    player.addVictoryPoint(1);
                    System.out.println("Colonie construite.");
                    return true;
                }
                else{
                    System.out.println("Vous ne pouvez pas constuire de colonie ici.");
                }
            }
            else{
                System.out.println("Vous ne pouvez pas constuire de colonie, vous avez atteint la quantité maximum possible.");
            }
        }
        else{
            System.out.println("Vous n'avez pas la quantité suffisante de ressources pour constuire une colonie.");
        }
        return false;
    }

    // fonction construisant une ville pour un joueur aux coordonnées en argument
    public boolean buildCity(Player player){
        int[] placement=vueGenerale.getCityPlacement();
        int line=placement[0];
        int column=placement[1];
        int colonyNumber=placement[2];
        Colony choosedColony=board.getTiles()[line][column].getColonies().get(colonyNumber);
        if(!choosedColony.isOwned(player)){
            System.out.println("La colonie ne vous appartient pas, vous ne pouvez pas constuire de ville ici.");
            return false;
        }
        int oreStock=player.ressources.get("Ore");
        int wheatStock=player.ressources.get("Wheat");
        if(wheatStock>=2 && oreStock>=3){
            if(player.canBuildPropertie("City",4)){
                choosedColony.setAsCity();
                player.ressources.replace("Wheat",wheatStock-2);
                player.ressources.replace("Ore",oreStock-3);
                player.addPropertie("City");
                player.removeColonyInCounter();
                player.addVictoryPoint(2);
                System.out.println("Ville construite.");
                return true;
            }
            else{
                System.out.println("Vous ne pouvez pas constuire de ville, vous avez atteint la quantité maximum possible.");
            }
        }
        else{
            System.out.println("Vous n'avez pas la quantité suffisante de ressources pour constuire une ville.");
        }
        return false;
    }

    // fonction donnant aux joueurs les ressources produites si leur colonie se trouve sur la case de l'id en argument
    public void diceProduction(int diceNumber){
        for(Tile[] tiles:board.getTiles()){
            for(Tile tile:tiles){
                if(tile.getId()==diceNumber){
                    if(!tile.hasThief() && !tile.getRessource().equals("")){
                        String ressource=tile.getRessource();
                        for(Colony colony:tile.getColonies()){
                            int producedValue=colony.isCity()?2:1;
                            if(colony.isOwned()) {
                                System.out.println(colony.getPlayer()+" : "+ressource+" + 1");
                                colony.getPlayer().ressources.merge(ressource, producedValue, Integer::sum);
                            }
                        }
                    }
                }
            }
        }
    }

    // fonction dans le cas d'un 7 aux dés, on défausse les cartes des joueurs et on change l'emplacement du brigand
    public void sevenAtDice(Player turnPlayer){
        String[] choosedResources;
        for(Player player:this.players){
            int quantity=player.resourceCount()/2;
            if(quantity>7){
                System.out.println(player+" you have to discard "+quantity+" cards of your hand.");
                do{
                    choosedResources=vueGenerale.ressourceToBeDiscarded(player,quantity);
                }
                while(player.hasResources(choosedResources));
                for(String resource:choosedResources){
                    player.ressources.merge(resource,1,(initialValue,decreasedValue)->initialValue-decreasedValue);
                }
            }
        }
        setThiefAndSteal(turnPlayer);
    }

    // fonction pour réattribuer l'emplacement du brigand et voler les ressources de colonies présentes sur la nouvelle case
    public void setThiefAndSteal(Player player){
        // TODO: 25/12/2021 dire quel joueur doit déplacer le voleur 
        // on set le voleur sur la nouvelle case
        int[] placement=vueGenerale.getThiefPlacement();
        int line=placement[0];
        int column=placement[1];
        Tile choosedTile=board.getTiles()[line][column];
        board.getThiefTile().setThief(false);
        board.setThiefTile(choosedTile);
        choosedTile.setThief(true);

        ArrayList<Colony> ownedColonies=new ArrayList<>();
        for(Colony colony:choosedTile.getColonies()){
            if(colony.getPlayer()!=null){
                ownedColonies.add(colony);
            }
        }

        // on vole une ressource à une colonie étant sur la case ou se trouve le voleur, s'il y en a plusieurs colonies le joueur choisit laquelle
        String resource;
        Player playerOfColony;
        if(ownedColonies.size()!=0){
            if(ownedColonies.size()>1){
                playerOfColony=vueGenerale.choosePlayerFromColony(ownedColonies);
            }
            else{
                playerOfColony=ownedColonies.get(0).getPlayer();
            }
            if(playerOfColony.resourceCount()>0){
                do{
                    resource=Board.generateRandomRessource();
                }
                while(playerOfColony.ressources.get(resource)==0);
                playerOfColony.ressources.merge(resource,1,(initialValue,decreasedValue)->initialValue-decreasedValue);
                player.ressources.merge(resource,1,Integer::sum);
            }
        }
    }

    // fonction permettant d'acheter une carte de développement
    public void buyCard(Player player){
        int oreStock=player.ressources.get("Ore");
        int woolStock=player.ressources.get("Wool");
        int wheatStock=player.ressources.get("Wheat");
        if(oreStock>=1 && woolStock>=1 && wheatStock>=1){
            player.cards.merge(Card.randomCard(),1,Integer::sum);
        }
        else{
            System.out.println("Vous n'avez pas les ressources suffisantes pour acheter une carte de développement.");
        }
    }

    // fonction permettant d'utiliser une carte de développement
    public void useCard(Player turnPlayer){
        String card = vueGenerale.chooseCard();
        Card choosedCard=Card.valueOf(card);
        if(turnPlayer.cards.get(choosedCard)>0){
            switch(choosedCard){
                case Knigth -> {
                    setThiefAndSteal(turnPlayer);
                }
                case VictoryPoint -> {
                    turnPlayer.addVictoryPoint(1);
                }
                case ProgressMonopoly -> {
                    String[] ressources=vueGenerale.chooseResource(1);
                    String ressource=ressources[0];
                    for(Player player:players){
                        if(player.ressources.get(ressource)>0){
                            player.ressources.merge(ressource,1,(a,b)->a-b);
                            turnPlayer.ressources.merge(ressource,1,Integer::sum);
                        }
                    }
                }
                case ProgressRoadBuilding -> {
                    int buildedRoad=0;
                    turnPlayer.ressources.merge("Clay",2,Integer::sum);
                    turnPlayer.ressources.merge("Wood",2,Integer::sum);
                    while(buildedRoad<2){
                        boolean isBuild=false;
                        do{
                            isBuild=!buildRoad(turnPlayer);
                        }
                        while(!isBuild);
                        buildedRoad++;
                    }
                }
                case ProgressYearOfPlenty -> {
                    String[] ressources=vueGenerale.chooseResource(2);
                    turnPlayer.ressources.merge(ressources[0],1,Integer::sum);
                    turnPlayer.ressources.merge(ressources[1],1,Integer::sum);
                }
            }
            turnPlayer.removeCard(choosedCard);
            turnPlayer.alreadyPlayedCardThisTurn=true;
        }else{
            System.out.println("You do not have this card.");
        }
    }

    // fonction qui permet de faire une échange avec un le port si le joueur en possède un
    public void tradeWithPort(Player player){
        if(player.ports.size()==0){
            System.out.println("You can't trade resources, you don't have a port.");
            return;
        }
        Port choosedPort;
        if(player.ports.size()>1){
            choosedPort=player.getPorts().get(vueGenerale.portSelection(player));
        }
        else{
            choosedPort=player.ports.get(0);
        }
        String resource1,resource2,resource3;
        String[] resources;
        if(choosedPort.getRate()==2){
            // TODO: 25/12/2021 prendre directement dans le stock du joueur la ressource du port au lieu de demander quelle ressource le joueur veut enlever de ses mains 
            resources=vueGenerale.chooseResource(2);
            resource1=resources[0];
            resource2=resources[1];
            if(player.ressources.get(resource1)>0 && player.ressources.get(resource2)>0){
                player.ressources.merge(resource1,1,(a,b)->a-b);
                player.ressources.merge(resource2,1,(a,b)->a-b);
                player.ressources.merge(choosedPort.getRessource(), 1,Integer::sum);
            }
            else{
                System.out.println("Vous n'avez pas les ressources suffisantes pour faire l'échange.");
            }
        }
        else{
            resources=vueGenerale.chooseResource(3);
            resource1=resources[0];
            resource2=resources[1];
            resource3=resources[2];
            if(player.ressources.get(resource1)>0 && player.ressources.get(resource2)>0 && player.ressources.get(resource3)>0){
                player.ressources.merge(resource1,1,(a,b)->a-b);
                player.ressources.merge(resource2,1,(a,b)->a-b);
                player.ressources.merge(resource3,1,(a,b)->a-b);
                player.ressources.merge(choosedPort.getRessource(), 1,Integer::sum);
            }
            else{
                System.out.println("Vous n'avez pas les ressources suffisantes pour faire l'échange.");
            }
        }
    }

    public void botsGetColor(HashMap<String, Boolean> color) {
        for(int i=0;i<this.players.length;i++){
            if(this.getPlayers()[i] instanceof Bot){
                if(color.replace("blue", false, true)) ((Bot) this.players[i]).setColor("blue");
                else if(color.replace("green", false, true)) ((Bot) this.players[i]).setColor("green");
                else if(color.replace("yellow", false, true)) ((Bot) this.players[i]).setColor("yellow");
                else if(color.replace("orange", false, true)) ((Bot) this.players[i]).setColor("orange");
            }
        }
    }

    public Colony buildColonyInitialization(Player player){
        int[] placement=vueGenerale.getColonyPlacement();
        int line=placement[0];
        int column=placement[1];
        int colonyNumber=placement[2];
        Colony choosedColony=board.getTiles()[line][column].getColonies().get(colonyNumber);
        if(choosedColony.isOwned()){
            System.out.println("Cette colonie appartient deja a quelqu'un.");
            return null;
        }
        if(choosedColony.isBuildableInitialization(player)){
            if(choosedColony.isPort()){
                player.addPort(choosedColony.getLinkedPort());
            }
            choosedColony.setPlayer(player);
            player.addPropertie("Colony");
            player.addVictoryPoint(1);
            return choosedColony;
        }
        else{
            System.out.println("Vous ne pouvez pas constuire de colonie ici, la règle de distance n'est pas respectée.");
        }
        return null;
    }

    // fonction construisant une route pour un joueur
    public boolean buildRoadInitialization(Player player,Colony colony){
        int[] placement=vueGenerale.getRoadPlacement();
        int line=placement[0];
        int column=placement[1];
        int roadNumber=placement[2];
        Road choosedRoad=board.getTiles()[line][column].getRoads().get(roadNumber);
        if(choosedRoad.isOwned()){
            System.out.println("Cette route appartient deja a quelqu'un."); // TODO: 24/12/2021 mettre tous les messages dans une fonction error de la vue, avec en argument un int qui represente le message a afficher (par exemple 1 = cette route appartient deja a quelqu'un)
            return false;
        }
        if(choosedRoad.isBuildableInitialization(player,colony)){
            choosedRoad.setPlayer(player);
            player.addPropertie("Road");
            return true;
        }
        else{
            System.out.println("Vous ne pouvez pas constuire de route ici.");
        }
        return false;
    }

    // fonction d'initialisation: tous les joueurs construisent deux colonies et deux routes
    // on donne ensuite les ressources des cases autour des colonies construites aux joueurs
    public void initialization(){
        System.out.println("Phase d'initialisation :\nTous les joueurs construisent deux colonies et deux routes, la deulineième colonie peut se trouver éloignée de la première à condition que la règle de distance soit respectée.");
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

    // fonction qui parcourt le tableau et donne les ressources aux joueurs de toutes les cases autour des colonies
    public void coloniesProduction(HashMap<Colony, Player> secondRoundBuildedColonies){
        HashMap<Colony,ArrayList<String>> result=new HashMap<>();
        for(int line=0; line<board.getTiles().length; line++) {
            for(int column=0; column<board.getTiles().length; column++) {
                for(Colony colony : board.getTiles()[line][column].getColonies()) {
                    if(secondRoundBuildedColonies.containsKey(colony)) {
                        String resource=board.getTiles()[line][column].getRessource();
                        ArrayList<String> resources;
                        if(result.containsKey(colony)) {
                            resources=result.get(colony);
                            resources.add(resource);
                            result.replace(colony, resources);
                        } else{
                            resources=new ArrayList<>();
                            resources.add(resource);
                            result.put(colony,resources);
                        }
                    }
                }
            }
        }
        result.forEach((colony,ressources)->{
            for(String resource:ressources){
                if(!resource.equals("")){
                    colony.getPlayer().ressources.merge(resource,1,Integer::sum);
                }
            }
        });
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
