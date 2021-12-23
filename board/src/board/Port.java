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

    public String getRessource(){
        return ressource;
    }

    public int getRate(){
        return rate;
    }

    @Override
    public String toString() {
        if(rate==3) return "3:1";
        else return "2:1->" + ressource;
    }
}
