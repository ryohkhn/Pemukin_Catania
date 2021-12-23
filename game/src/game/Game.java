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
                    player.ressources.replace("Clay", clayStock-1);
                    player.ressources.replace("Wood", woodStock-1);
                    player.addPropertie("Road");
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
                    player.ressources.replace("Clay",clayStock-1);
                    player.ressources.replace("Wheat",wheatStock-1);
                    player.ressources.replace("Wood",woodStock-1);
                    player.ressources.replace("Wool",woolStock-1);
                    player.addPropertie("Colony");
                    player.addVictoryPoint(1);
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
                    if(!tile.hasThief()){
                        String ressource=tile.getRessource();
                        for(Colony colony:tile.getColonies()){
                            int producedValue=colony.isCity()?2:1;
                            colony.getPlayer().propertiesCounter.merge(ressource,producedValue,Integer::sum);
                        }
                    }
                }
            }
        }
    }

    public void sevenAtDice(Player turnPlayer){
        for(Player player:this.players){
            if((player.ressourceCount())>7){
                String ressource=vueGenerale.ressourceToBeDefaussed();
                player.ressources.replace(ressource,player.ressources.get(ressource)/2);
            }
        }
        setThiefAndSteal(turnPlayer);
    }

    public void setThiefAndSteal(Player player){
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
        String ressource;
        Player playerOfColony;
        if(ownedColonies.size()!=0){
            if(ownedColonies.size()>1){
                playerOfColony=vueGenerale.choosePlayerFromColony(ownedColonies);
            }
            else{
                playerOfColony=ownedColonies.get(0).getPlayer();
            }
            if(playerOfColony.ressourceCount()>0){
                do{
                    ressource=Board.generateRandomRessource();
                }
                while(playerOfColony.ressources.get(ressource)==0);
                int ressourceStock=playerOfColony.ressources.get(ressource);
                playerOfColony.ressources.merge(ressource,1,(a,b)->a-b);
                player.ressources.merge(ressource,1,Integer::sum);
            }
        }
    }

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

    public void tradeWithPort(Player player){
        if(player.ports.size()==0){
            System.out.println("You can't trade resources, you don't have a port.");
            return;
        }
        Port choosedPort=null;
        if(player.ports.size()>1){
            // TODO: 23/12/2021 appel fonction de vue qui demande quelles ressources le joueur veut échanger (3 ressources)
            choosedPort=new Port();
        }
        else{
            for(Port p:player.ports){
                choosedPort=p;
            }
        }
        String resource1="",resource2="",resource3="";
        String[] resources;
        if(choosedPort.getRate()==2){
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
}
