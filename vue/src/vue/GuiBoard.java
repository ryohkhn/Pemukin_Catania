package vue;

import board.Board;
import board.Tile;
import game.Game;
import game.Launcher;

import javax.swing.*;
import java.awt.*;

public class GuiBoard extends JPanel{
    private Game game;
    private Gui gui;
    private GuiSideBar guiSideBar;
    private Launcher launcher;
    private Board board;
    private Tile[][] tiles;

    public GuiBoard(Game game,Gui gui,GuiSideBar guiSideBar,Launcher launcher, Board board, Tile[][] tiles){
        this.game=game;
        this.gui=gui;
        this.guiSideBar=guiSideBar;
        this.launcher=launcher;
        this.board=board;
        this.tiles=tiles;
        setLayout(new GridLayout(8,8));
        createBoard();
    }

    public void displayBoard(){
        this.repaint();
    }

    public void createBoard(){
        for(int line=-2; line<tiles.length+2; line++){
            for(int column=-2; column<tiles.length+2; column++){
                if(column==-2 || column==tiles.length+1 || line==-2 || line==tiles.length+1){
                    GuiTile guiTile=new GuiTile(0,0,false,true);
                    guiTile.setAttributes(null,null,null,null,null);
                    this.add(guiTile);
                }
                else if(line==-1 || line==tiles.length || column==-1 || column==tiles.length){
                    if(line==-1 && column%2==1 && column<tiles.length){
                        GuiTile guiTile=new GuiTile(line+1,column,true,false);
                        guiTile.setAttributes(game,guiSideBar, this, gui,launcher);
                        this.add(guiTile);
                    }
                    else if(column==-1 && line%2==0 && line<tiles.length){
                        GuiTile guiTile=new GuiTile(line,column+1,true,false);
                        guiTile.setAttributes(game,guiSideBar, this, gui,launcher);
                        this.add(guiTile);
                    }
                    else if(line==tiles.length && column%2==0 && column<tiles.length){
                        GuiTile guiTile=new GuiTile(line-1,column,true,false);
                        guiTile.setAttributes(game,guiSideBar, this, gui,launcher);
                        this.add(guiTile);
                    }
                    else if(column==tiles.length && line%2==1 && line<tiles.length){
                        GuiTile guiTile=new GuiTile(line,column-1,true,false);
                        guiTile.setAttributes(game,guiSideBar, this, gui,launcher);
                        this.add(guiTile);
                    }
                    else{
                        GuiTile guiTile=new GuiTile(0,0,true,false);
                        guiTile.setAttributes(null,null,null,null,null);
                        this.add(guiTile);
                    }
                }
                else{
                    GuiTile guiTile=new GuiTile(line,column,false,false);
                    guiTile.setAttributes(game,guiSideBar, this, gui,launcher);
                    this.add(guiTile);
                }
            }
        }
    }

    public void setAllTileAsListener(boolean listenerForColonyInitialization,boolean listenerForColony,boolean listenerForCity,boolean listenerForRoad){
        for(Component component:this.getComponents()){
            if(component instanceof GuiTile){
                GuiTile tile=(GuiTile) component;
                if(listenerForColony) tile.setTypeOfMove("Colony");
                else if(listenerForColonyInitialization) tile.setTypeOfMove("ColonyInitialization");
                else if(listenerForCity) tile.setTypeOfMove("City");
                else if(listenerForRoad) tile.setTypeOfMove("Road");
                else tile.setTypeOfMove("RoadInitialization");
                addMouseListener(tile);
            }
        }
    }

    public void removeAllTileAsListener(){
        for(Component component:this.getComponents()){
            if(component instanceof GuiTile){
                removeMouseListener((GuiTile)component);
            }
        }
    }
}
