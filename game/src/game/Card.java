package game;

import java.util.Random;

public enum Card{
    Knigth,VictoryPoint,ProgressRoadBuilding,ProgressYearOfPlenty,ProgressMonopoly;
    public static boolean alreadyPlayedThisTurn=false;

    private static final Random random=new Random();

    public static Card randomCard(){
        int index=random.nextInt(Card.values().length);
        return Card.values()[index];
    }
}
