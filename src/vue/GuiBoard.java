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
    private Tile[][] tiles;

    public GuiBoard(Game game,Gui gui,GuiSideBar guiSideBar,Launcher launcher, Tile[][] tiles){
        this.game=game;
        this.gui=gui;
        this.guiSideBar=guiSideBar;
        this.launcher=launcher;
        this.tiles=tiles;
        setLayout(new GridLayout(8,8));
        createBoard();
    }

    // fonction pour revalider le plateau
    public void displayBoard(){
        this.repaint();
    }

    // fonction de la creation du plateau avec chaque case représentée par un objet GuiTile
    public void createBoard(){
        for(int line=-2; line<tiles.length+2; line++){
            for(int column=-2; column<tiles.length+2; column++){
                if(column==-2 || column==tiles.length+1 || line==-2 || line==tiles.length+1){
                    GuiTile guiTile=new GuiTile(null,0,0,false,true);
                    guiTile.setAttributes(null,null,null,null);
                    this.add(guiTile);
                }
                else if(line==-1 || line==tiles.length || column==-1 || column==tiles.length){
                    if(line==-1 && column%2==1 && column<tiles.length){
                        GuiTile guiTile=new GuiTile(game,line+1,column,true,false);
                        guiTile.setAttributes(guiSideBar, this, gui,launcher);
                        this.add(guiTile);
                    }
                    else if(column==-1 && line%2==0 && line<tiles.length){
                        GuiTile guiTile=new GuiTile(game,line,column+1,true,false);
                        guiTile.setAttributes(guiSideBar, this, gui,launcher);
                        this.add(guiTile);
                    }
                    else if(line==tiles.length && column%2==0 && column<tiles.length){
                        GuiTile guiTile=new GuiTile(game,line-1,column,true,false);
                        guiTile.setAttributes(guiSideBar, this, gui,launcher);
                        this.add(guiTile);
                    }
                    else if(column==tiles.length && line%2==1 && line<tiles.length){
                        GuiTile guiTile=new GuiTile(game,line,column-1,true,false);
                        guiTile.setAttributes(guiSideBar, this, gui,launcher);
                        this.add(guiTile);
                    }
                    else{
                        GuiTile guiTile=new GuiTile(null,0,0,true,false);
                        guiTile.setAttributes(null,null,null,null);
                        this.add(guiTile);
                    }
                }
                else{
                    GuiTile guiTile=new GuiTile(game,line,column,false,false);
                    guiTile.setAttributes(guiSideBar, this, gui,launcher);
                    this.add(guiTile);
                }
            }
        }
    }

    // fonction pour activer toutes les cases en tant que Mouse Input Listener
    public void setAllTileAsListener(String typeOfMove){
        for(Component component:this.getComponents()){
            if(component instanceof GuiTile){
                GuiTile tile=(GuiTile) component;
                tile.setTypeOfMove(typeOfMove);
                tile.activateMouseListener();
            }
        }
    }

    // fonction pour désactiver toutes les cases Mouse Input Listener
    public void removeAllTileAsListener(){
        for(Component component:this.getComponents()){
            if(component instanceof GuiTile){
                GuiTile tile=(GuiTile)component;
                tile.disableMouseListener();
            }
        }
    }

    // fonction pour retirer toutes les images du voleur sur les GuiTile
    public void removeThiefImage(){
        for(Component component:this.getComponents()){
            if(component instanceof GuiTile){
                GuiTile tile=(GuiTile)component;
                tile.removeThiefImage();
            }
        }
    }

    // fonction appelée dans le bot pour retirer l'image du voleur la ou elle ne doit plus être
    public void botRemoveAndSetThief(Tile thiefTile){
        for(Component component:this.getComponents()){
            if(component instanceof GuiTile){
                GuiTile tile=(GuiTile)component;
                if(!tile.getWater() && !tile.getEmpty() && tile.getTile()==thiefTile){
                    tile.setThiefImage();
                }
                else{
                    tile.removeThiefImage();
                }
            }
        }
    }
}
