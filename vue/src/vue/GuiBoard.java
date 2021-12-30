package vue;

import board.Board;

import javax.swing.*;
import java.awt.*;

public class GuiBoard extends JPanel{
    private Board board;

    public GuiBoard(Board board){
        this.board=board;
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        int height=getHeight();
        int width=getWidth();

        for(int line=0; line<board.getTiles().length; line++){
            for(int column=0; column<board.getTiles().length; column++){
                System.out.println(line+" "+column);
                g.drawRect(line*20,line*20,width/4,height/4);
            }
        }
    }

    /*public void drawSquare(int line,int column){
        Polygon polygon=
    }

    public Polygon makeSquare(Point center){
        int xCenter=center.x;
        int yCenter=center.y;

        Polygon result=new Polygon();
        result.addPoint();
    }

     */
}
