package game;

import board.*;

import java.util.ArrayList;

public class Game{
    private Player[] players;
    private Board board;
    //private Vues vueGenerale;

    public Game(int nb){
        board=new Board();
        players=new Player[nb];
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

    // fonction construisant une route pour un joueur aux coordonnées en argument
    public boolean buildRoad(int line,int column,int roadNumber,Player player){
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

    // fonction construisant une colonie pour un joueur aux coordonnées en argument.
    // si la colonie est un port il est ajouté au hashmap du joueur
    public boolean buildColony(int line,int column,int colonyNumber,Player player){
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
    public boolean buildCity(int line,int column,int colonyNumber,Player player){
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
                    String ressource=tile.getRessource();
                    for(Colony colony:tile.getColonies()){
                        int producedValue=colony.isCity()?2:1;
                        colony.getPlayer().propertiesCounter.merge(ressource,producedValue,Integer::sum);
                    }
                }
            }
        }
    }

    public void sevenAtDice(Player turnPlayer){ // TODO: 20/12/2021 compléter avec les appels de fonction de Vue pour terminer la fonction
        for(Player player:this.players){
            if((player.ressourceCount())>7){
                // appel de fonction de vue qui demande quelle ressource il doit défausser (quantité de ressource/2)
            }
        }
        setThiefAndSteal(turnPlayer);
    }

    public void setThiefAndSteal(Player player){
        // appel fonction de vue pour récupérer la case ou placer le voleur
        board.getThiefTile().setThief(false);
        // setThief la nouvelle Tile
        board.setThiefTile(null);
        // mettre la nouvelle tile en thief=true

        ArrayList<Colony> ownedColonies=new ArrayList<>();
        for(Colony colony:board.getThiefTile().getColonies()){
            if(colony.getPlayer()!=null){
                ownedColonies.add(colony);
            }
        }

        String ressource;
        Player playerOfColony;
        if(ownedColonies.size()==1){
            playerOfColony=ownedColonies.get(0).getPlayer();
            if(playerOfColony.ressourceCount()>0){
                do{
                    ressource=Board.generateRandomRessource();
                }
                while(playerOfColony.ressources.get(ressource)==0);
                int ressourceStock=playerOfColony.ressources.get(ressource);
                playerOfColony.ressources.replace(ressource,ressourceStock-1);
                player.ressources.merge(ressource,1,Integer::sum);
            }
        }else if(ownedColonies.size()>=1){
            // appel fonction de vue pour demander à quelle colonie le joueur veut-il voler une carte
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

    public void useCard(Player turnPlayer,String card){  // TODO: 21/12/2021 terminer la fonction avec des appels de vue
        Card choosedCard;
        try{
            choosedCard=Card.valueOf(card);
        }
        catch(IllegalArgumentException e){
            System.out.println("Cette carte n'existe pas.");
            return;
        }
       if(turnPlayer.cards.get(choosedCard)>0){
            switch(choosedCard){
                case Knigth -> {
                    setThiefAndSteal(turnPlayer);
                }
                case VictoryPoint -> {
                    turnPlayer.addVictoryPoint(1);
                }
                case ProgressMonopoly -> {
                    String ressource="";
                    //appel à une fonction de vue qui désigne une ressource à récupérer à chaque joueur
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
                        do{
                            //appel à la fonction de vue qui demande les coordonnées pour construire une route
                        }
                        // mettre les coordonnées récupérées avec la vue
                        while(!buildRoad(0,0,0,turnPlayer));
                        buildedRoad++;
                    }
                }
                case ProgressYearOfPlenty -> {
                    // appel à la fonction de vue qui va demander 2 ressources au joueur
                    String ressource1="",ressource2="";
                    turnPlayer.ressources.merge(ressource1,1,Integer::sum);
                    turnPlayer.ressources.merge(ressource2,1,Integer::sum);
                }
            }
            turnPlayer.removeCard(choosedCard);
        }
    }

    public void tradeWithPort(Player player){
        if(player.ports.size()==0){
            System.out.println("Vous ne pouvez pas faire d'échange de ressources, vous ne possédez pas de port.");
            return;
        }
        Port choosedPort=null;
        if(player.ports.size()>1){
            // afficher tous les ports via la vue (la ressource et si c'est du 3:1 ou 2:1) et en choisir un
            choosedPort=new Port();
        }
        else{
            for(Port p:player.ports){
                choosedPort=p;
            }
        }
        String ressource1="",ressource2="",ressource3="";

        if(choosedPort.getRate()==2){
            // appel fonction de vue qui demande quelles ressources le joueur veut échanger (2 ressources)
            if(player.ressources.get(ressource1)>0 && player.ressources.get(ressource2)>0){
                player.ressources.merge(ressource1,1,(a,b)->a-b);
                player.ressources.merge(ressource2,1,(a,b)->a-b);
                player.ressources.merge(choosedPort.getRessource(), 1,Integer::sum);
            }
            else{
                System.out.println("Vous n'avez pas les ressources suffisantes pour faire l'échange.");
            }
        }
        else{
            // appel fonction de vue qui demande quelles ressources le joueur veut échanger (3 ressources)
            if(player.ressources.get(ressource1)>0 && player.ressources.get(ressource2)>0 && player.ressources.get(ressource3)>0){
                player.ressources.merge(ressource1,1,(a,b)->a-b);
                player.ressources.merge(ressource2,1,(a,b)->a-b);
                player.ressources.merge(ressource3,1,(a,b)->a-b);
                player.ressources.merge(choosedPort.getRessource(), 1,Integer::sum);
            }
            else{
                System.out.println("Vous n'avez pas les ressources suffisantes pour faire l'échange.");
            }
        }
    }
}
