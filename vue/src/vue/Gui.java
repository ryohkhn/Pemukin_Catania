package vue;

import game.Game;
import game.Launcher;
import board.Colony;
import game.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class Gui extends JFrame{
    private GuiBoard guiBoard;
    private GuiSideBar guiSideBar;
    private Game game;

    public Gui(Launcher launcher){
        setUpGui(launcher);
    }

    // fonction qui initialise la gui et lance la configuration du jeu
    public void setUpGui(Launcher launcher){
        setTitle("Pemukin Catania");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width-700, screenSize.height-300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel gameNumberPanel=new JPanel();
        gameNumberPanel.add(new JLabel("Choose how many players you want in the game :"));
        String[] values={"1","3","4"};
        JComboBox comboBox=new JComboBox(values);
        JButton next=new JButton("Continue");
        gameNumberPanel.add(comboBox);
        gameNumberPanel.add(next);
        getContentPane().add(gameNumberPanel);
        final int[] returnedValue=new int[1];
        next.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                returnedValue[0]=Integer.parseInt((String)comboBox.getSelectedItem());
                game=launcher.createGame(returnedValue[0]);
                gameNumberPanel.setVisible(false);
                playerSelection(returnedValue[0]);
            }
        });
        setVisible(true);
    }

    // fonction pour sélectionner si un joueur est un humain ou un robot
    public void playerSelection(int nbplayer){
        JPanel playerSelectionPanel=new JPanel();
        playerSelectionPanel.add(new JLabel("Select which player is Human or IA :"));
        String[] values={"Human","IA"};
        ArrayList<JComboBox> comboBoxList=new ArrayList<>();
        for(int i=0; i<nbplayer; i++){
            comboBoxList.add(new JComboBox(values));
            playerSelectionPanel.add(new JLabel("Player "+(i+1)+" :"));
            playerSelectionPanel.add(comboBoxList.get(i));
        }
        JButton next=new JButton("Continue");
        playerSelectionPanel.add(next);
        getContentPane().add(playerSelectionPanel);
        final String[] returnedValue=new String[nbplayer];
        next.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                for(int i=0; i<comboBoxList.size(); i++){
                    returnedValue[i]=(String)comboBoxList.get(i).getSelectedItem();
                }
                game.setPlayers(returnedValue);
                playerSelectionPanel.setVisible(false);
                HashMap<String,Boolean> colorMap=new HashMap<>();
                colorMap.put("orange",false);
                colorMap.put("blue",false);
                colorMap.put("yellow",false);
                colorMap.put("green",false);
                colorSelection(nbplayer,nbplayer,new String[nbplayer],colorMap);
            }
        });
    }

    public void colorSelection(int nbPlayer,int count,String[] returnedValue,HashMap<String,Boolean> colorMap){
        final int[] compt=new int[1];
        compt[0]=count;
        JPanel colorSelectionPanel=new JPanel();
        if(compt[0]==0){
            game.setColors(returnedValue);
            initiateGame();
            return;
        }
        JButton next=new JButton("Continue");
        ButtonGroup buttonGroup=new ButtonGroup();
        colorSelectionPanel.add(new JLabel("Select a color for player :"));
        ArrayList<JRadioButton> jRadioButtonList=new ArrayList<>();
        for(String s:colorMap.keySet()){
            if(!colorMap.get(s)){
                JRadioButton button=new JRadioButton(s);
                jRadioButtonList.add(button);
                buttonGroup.add(button);
                colorSelectionPanel.add(button);
            }
        }
        jRadioButtonList.get(0).setSelected(true);
        colorSelectionPanel.add(next);
        getContentPane().add(colorSelectionPanel);
        next.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                for(JRadioButton button:jRadioButtonList){
                    if(button.isSelected()){
                        returnedValue[Math.abs(count-nbPlayer)]=button.getActionCommand();
                        colorMap.merge(button.getActionCommand(),true, (initialValue, replacedValue)->initialValue=replacedValue);
                    }
                }
                compt[0]-=1;
                colorSelection(nbPlayer,compt[0],returnedValue,colorMap);
                colorSelectionPanel.setVisible(false);
            }
        });
    }

    public void initiateGame(){
        getContentPane().removeAll();
        getContentPane().revalidate();
        getContentPane().repaint();

        getContentPane().setLayout(new GridLayout());
        guiBoard=new GuiBoard(game.getBoard());
        guiSideBar=new GuiSideBar(game);
        guiSideBar.setBackground(Color.red);
        getContentPane().add(guiBoard);
        getContentPane().add(guiSideBar);

        guiBoard.repaint();

        guiSideBar.setVisible(true);
        guiBoard.setVisible(true);
    }
}
