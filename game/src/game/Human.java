package game;

public class Human extends Player{
    public Human(String color){
        super(color);
    }

    @Override
    public boolean isBot() {
        return false;
    }

    public Human getPlayer(){
        return this.getPlayer();
    }
}
