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
        String res="";
        switch(this){
            case ProgressMonopoly -> res+="ProgressMonopoly";
            case VictoryPoint -> res+="VictoryPoint";
            case Knight -> res+="Knight";
            case ProgressYearOfPlenty -> res+= "ProgressYearOfPlenty";
            case ProgressRoadBuilding -> res+= "ProgressRoadBuilding";
        }
        res+=" Card";
        return res;
    }
}
