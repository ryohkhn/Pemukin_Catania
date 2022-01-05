package vue;

import board.Colony;
import board.Road;
import board.Tile;
import game.Game;
import game.Launcher;
import game.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GuiTile extends JPanel implements MouseInputListener{
    private Game game;
    private Launcher launcher;
    private Gui gui;
    private GuiSideBar guiSideBar;
    private GuiBoard guiBoard;
    private Tile tile;
    private String typeOfMove;
    private JLabel picLabel;
    private int line;
    private int column;
    private boolean water;
    private boolean empty;
    private boolean isActiveMouseListener;

    public GuiTile(Game game,int line,int column,boolean water, boolean empty){
        this.line=line;
        this.column=column;
        this.game=game;
        this.isActiveMouseListener=false;
        if(game!=null){
            this.tile=game.getBoard().getTiles()[line][column];
        }
        setLayout(new GridLayout(3, 1));
        if(water){
            if(tile!=null){
                JLabel portResource;
                JLabel portRate;
                portRate=new JLabel(tile.getPort().getRate()+":1");
                portRate.setHorizontalAlignment(JLabel.CENTER);
                add(portRate);
                if(tile.getPort().getRate()==2){
                    portResource=new JLabel(tile.getPort().getRessource());
                    portResource.setHorizontalAlignment(JLabel.CENTER);
                    add(portResource);
                }
            }
            setBackground(Color.blue);
        } else if(empty){
            setBackground(Color.WHITE);
        } else{
            setBackground(switch(tile.getRessource()){
                case "Clay" -> new Color(0xFF999999, true);
                case "Ore" -> new Color(0xB47804);
                case "Wheat" -> new Color(0xFFFF00);
                case "Wood" -> new Color(0x22440B);
                case "Wool" -> new Color(0x579632);
                case "" -> Color.white;
                default -> Color.black;
            });
            JLabel resourceName;
            if(tile.getRessource().equals("")){
                resourceName=new JLabel("Desert");
            } else{
                resourceName=new JLabel(tile.getRessource());
            }
            resourceName.setHorizontalAlignment(JLabel.CENTER);
            add(resourceName);
            JLabel tileIdLabel=new JLabel(String.valueOf(tile.getId()));
            tileIdLabel.setHorizontalAlignment(JLabel.CENTER);
            add(tileIdLabel);
            if(tile.hasThief()){
                try{
                    BufferedImage thiefImage=ImageIO.read(new File("resources/thief_dot.png"));
                    picLabel=new JLabel(new ImageIcon(thiefImage));
                    add(picLabel);
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
            setBorder(new GuiBorder(tile));
        }
        addMouseListener(this);
    }

    public void setAttributes(GuiSideBar guiSideBar,GuiBoard guiboard,Gui gui,Launcher launcher){
        this.guiSideBar=guiSideBar;
        this.guiBoard=guiboard;
        this.gui=gui;
        this.launcher=launcher;
    }

    public void setTypeOfMove(String typeOfMove){
        this.typeOfMove=typeOfMove;
    }

    public void activateMouseListener(){
        this.isActiveMouseListener=true;
    }

    public void disableMouseListener(){
        this.isActiveMouseListener=false;
    }

    @Override
    public void mouseClicked(MouseEvent e){
        if((!water || !empty) && isActiveMouseListener){
            int buildLocation=-1;
            switch(typeOfMove){
                case "ColonyInitialization","ColonyInitializationSecondRound","Colony","City" -> {
                    if(e.getX()<(this.getWidth()/3) && e.getY()<(this.getWidth()/3)){
                        buildLocation=0;
                    }
                    else if(e.getX()>((this.getWidth()*2)/3) && e.getY()<(this.getWidth()/3)){
                        buildLocation=1;
                    }
                    else if(e.getX()>((this.getWidth()*2)/3) && e.getY()>((this.getWidth()*2)/3)){
                        buildLocation=2;
                    }
                    else if(e.getX()<(this.getWidth()/3) && e.getY()>((this.getWidth()*2)/3)){
                        buildLocation=3;
                    }
                }
                case "RoadInitialization","Road","RoadCard","RoadCardSecondBuild" ->{
                    if(e.getY()<(this.getHeight()/4) && e.getX()>(this.getWidth()/4) && e.getX()<((this.getWidth()*3)/4)){
                        buildLocation=0;
                    }
                    else if(e.getX()>((this.getWidth()*3)/4) && e.getY()>(this.getHeight()/4) && e.getY()<((this.getHeight()*3)/4)){
                        buildLocation=1;
                    }
                    else if(e.getY()>((this.getHeight()*3)/4) && e.getX()>(this.getWidth()/4) && e.getX()<((this.getWidth()*3)/4)){
                        buildLocation=2;
                    }
                    else if(e.getX()<(this.getWidth()/4) && e.getY()>(this.getHeight()/4) && e.getY()<((this.getHeight()*3)/4)){
                        buildLocation=3;
                    }
                }
            }
            if(buildLocation!=-1){
                Player player=launcher.getCurrentPlayer();
                switch(typeOfMove){
                    case "ColonyInitialization" -> {
                        Colony buildedColony=game.buildColonyInitialization(player, new int[]{line, column,buildLocation});
                        if(buildedColony!=null){
                            Colony buildedOnSecondRound=game.getColonyFromPlayer(player);
                            if(buildedOnSecondRound!=null){
                                game.secondRoundBuildedColonies.remove(buildedOnSecondRound);
                            }
                            game.secondRoundBuildedColonies.put(buildedColony,player);
                            guiBoard.removeAllTileAsListener();
                            guiSideBar.roundInitializationDone();
                        }
                    }
                    case "RoadInitialization" -> {
                        Colony buildedOnSecondRound=game.getColonyFromPlayer(player);
                        if(game.buildRoadInitialization(player,buildedOnSecondRound, new int[]{line, column,buildLocation})){
                            guiBoard.removeAllTileAsListener();
                            guiSideBar.roundInitializationDone();
                        }
                    }
                    case "Colony" ->{
                        if(game.buildColony(player,new int[]{line,column,buildLocation})){
                            guiBoard.removeAllTileAsListener();
                            guiSideBar.buildDone("colony");
                        }
                    }
                    case "City" ->{
                        if(game.buildCity(player,new int[]{line,column,buildLocation})){
                            guiBoard.removeAllTileAsListener();
                            guiSideBar.buildDone("city");
                        }
                    }
                    case "Road" ->{
                        if(game.buildRoad(player,new int[]{line,column,buildLocation})){
                            guiBoard.removeAllTileAsListener();
                            guiSideBar.buildDone("road");
                        }
                    }
                    case "RoadCard" ->{
                        if(game.isRoadBuildable(new int[]{line,column,buildLocation},player)){
                            game.useCardProgressRoadBuilding(player,new int[]{line,column,buildLocation});
                            guiBoard.removeAllTileAsListener();
                            guiSideBar.buildRoadCardDone();
                        }
                    }
                    case "RoadCardSecondBuild" ->{
                        if(game.isRoadBuildable(new int[]{line,column,buildLocation},player)){
                            game.useCardProgressRoadBuildingSecondRound(player,new int[]{line,column,buildLocation});
                            guiSideBar.buildRoadCardDone();
                            guiBoard.removeAllTileAsListener();
                        }
                    }
                }
            }
            else if(typeOfMove.equals("Thief")){
                game.setThief(new int[]{this.line,this.column});
                guiSideBar.steal(launcher.getCurrentPlayer(),tile);
                guiBoard.removeThiefImage();
                this.setThiefImage();
                guiBoard.removeAllTileAsListener();
            }
        }
    }

    public void removeThiefImage(){
        if(picLabel!=null){
            remove(picLabel);
        }
    }

    public void setThiefImage(){
        try{
            BufferedImage thiefImage=ImageIO.read(new File("resources/thief_dot.png"));
            picLabel=new JLabel(new ImageIcon(thiefImage));
            add(picLabel);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void mousePressed(MouseEvent e){

    }

    @Override
    public void mouseReleased(MouseEvent e){

    }

    @Override
    public void mouseEntered(MouseEvent e){

    }

    @Override
    public void mouseExited(MouseEvent e){

    }

    @Override
    public void mouseDragged(MouseEvent e){

    }

    @Override
    public void mouseMoved(MouseEvent e){

    }

    public class GuiBorder extends AbstractBorder{
        Tile tile;

        public GuiBorder(Tile tile){
            this.tile=tile;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int line, int column, int width, int height){
            Point topLeft=new Point(line, column);
            Point topRight=new Point(line+width, column);
            Point bottomLeft=new Point(line, column+height);
            Point bottomRight=new Point(line+width, column+height);

            paintRoads((Graphics2D) g, topLeft, topRight, bottomLeft, bottomRight, this.tile);
            paintColonies((Graphics2D) g, topLeft, topRight, bottomLeft, bottomRight, this.tile);
        }

        private void paintRoads(Graphics2D g2d, Point topLeft, Point topRight, Point bottomLeft, Point bottomRight, Tile tile){
            g2d.setStroke(new BasicStroke(10));
            paintRoad(g2d, tile.getRoads().get(0), topLeft, topRight);
            paintRoad(g2d, tile.getRoads().get(2), bottomLeft, bottomRight);
            paintRoad(g2d, tile.getRoads().get(1), topRight, bottomRight);
            paintRoad(g2d, tile.getRoads().get(3), topLeft, bottomLeft);
        }

        private void paintRoad(Graphics2D g2d, Road road, Point p1, Point p2){
            Line2D line=new Line2D.Float(p1, p2);
            g2d.setPaint(road.isOwned() ? road.getPlayer().getColor() : Color.WHITE);
            g2d.draw(line);
        }

        private void paintColonies(Graphics2D g2d, Point topLeft, Point topRight, Point bottomLeft, Point bottomRight, Tile tile){
            g2d.setStroke(new BasicStroke(10));
            paintColony(g2d, tile.getColonies().get(0), topLeft);
            paintColony(g2d, tile.getColonies().get(1), topRight);
            paintColony(g2d, tile.getColonies().get(2), bottomRight);
            paintColony(g2d, tile.getColonies().get(3), bottomLeft);
        }

        private void paintColony(Graphics2D g2d, Colony colony, Point point){
            Shape shape;
            if(colony.isCity()) shape=new Rectangle2D.Float(point.x-10, point.y-10, 20, 20);
            else shape=new Ellipse2D.Float(point.x-10, point.y-10, 20, 20);
            g2d.setPaint(Color.WHITE);
            g2d.draw(shape);
            if(colony.isOwned()){
                g2d.setPaint(colony.getPlayer().getColor());
                g2d.fill(shape);
            }
        }
    }
}