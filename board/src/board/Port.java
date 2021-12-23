package board;

public class Port{ // TODO: 22/12/2021 est-ce que depuis la case 4 du schéma du projet, on peut acceder au port case 6 ? Est-ce que on peut acceder au port case 6 si on a une ville sur le coin 1 de la case 6 (il n'y a pas de coté ppoour un port)? 
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
