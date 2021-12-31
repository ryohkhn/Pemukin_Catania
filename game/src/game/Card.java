package game;

import java.util.Random;

public enum Card{
    Knight,VictoryPoint,ProgressRoadBuilding,ProgressYearOfPlenty,ProgressMonopoly,LargestArmy;


    private static final Random random=new Random();

    public static Card randomCard(){
        int index=random.nextInt(Card.values().length-1);
        return Card.values()[index];
    }

    public String toString(){
        String res="Carte : ";
        switch(this){
            case ProgressMonopoly -> res+="Monopole";
            case VictoryPoint -> res+="Point de Victoire";
            case Knight -> res+="Roi";
            case ProgressYearOfPlenty -> res+= "Invention";
            case ProgressRoadBuilding -> res+= "Construction de route";
        }
        return res;
    }
}
