package game;

import board.*;


public class Game{
    private Player[] players;
    private Board board;

    public static void main(String[] args){
        Board test=new Board(4);
        test.afficher();
        int u=0;
        int j=0;
        int x=0;
        for(Tile[] t : test.getTiles()) {
            System.out.println("x="+u+" :");
            u++;
            j=0;
            for(Tile t1 : t) {
                System.out.print("        ");
                System.out.println("y="+j+" :");
                j++;
                x=0;
                for(Road r : t1.getRoads()) {
                    System.out.print("            "+r.checked+"   Route "+x+" :");
                    System.out.println(r);
                    x++;
                }
            }
        }
    }
}
