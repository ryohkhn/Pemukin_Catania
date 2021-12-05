package board;

public class Port{
    protected int rate;
    protected String ressource;

    public Port(String ressource){
        this.ressource=ressource;
        this.rate=2;
    }

    public Port(){
        this.rate=3;
    }
}
