package vue;

import game.Game;
import game.Launcher;

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
    private int nbplayer;

    public Gui(Launcher launcher){
        guiBoard=new GuiBoard();
        guiSideBar=new GuiSideBar();
        setUpGui(launcher);
        /*
        getContentPane().setLayout(new GridLayout());
        guiSideBar.setBackground(Color.red);
        getContentPane().add(guiBoard);
        getContentPane().add(guiSideBar);
        guiSideBar.setVisible(true);
        guiBoard.setVisible(true);
         */
    }

    public int getNbplayer(){
        return nbplayer;
    }

    public void setUpGui(Launcher launcher){
        setTitle("Pemukin Catania");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width-700, screenSize.height-300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel gameNumberPanel=new JPanel();
        gameNumberPanel.add(new JLabel("Choose how many players you want in the game :"));
        String[] values={"3","4"};
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

    public void playerSelection(int nbplayer){
        JPanel playerSelectionPanel=new JPanel();
        playerSelectionPanel.add(new JLabel("Select which player is Human or IA :"));
        String[] values={"Human","IA"};
        ArrayList<JComboBox> comboBoxList=new ArrayList<>();
        for(int i=1; i<=nbplayer; i++){
            comboBoxList.add(new JComboBox(values));
        }
        for(int i=0; i<comboBoxList.size(); i++){
            playerSelectionPanel.add(new JLabel("Player "+i+" :"));
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
                colorSelection(nbplayer);
            }
        });
    }

    public void colorSelection(int nbplayer){
        final int[] compt=new int[1];
        compt[0]=nbplayer;
        JPanel colorSelectionPanel=new JPanel();
        final String[] returnedValue=new String[nbplayer];
        while(compt[0]>0){
            ButtonGroup buttonGroup=new ButtonGroup();
            colorSelectionPanel.add(new JLabel("Select a color for every player :"));
            HashMap<String,Boolean> colorMap=new HashMap<>();
            ArrayList<JRadioButton> jRadioButtonList=new ArrayList<>();
            for(String s:colorMap.keySet()){
                if(!colorMap.get(s)){
                    jRadioButtonList.add(new JRadioButton(s));
                }
            }
            for(JRadioButton button:jRadioButtonList){
                buttonGroup.add(button);
                colorSelectionPanel.add(button);
            }
            getContentPane().add(colorSelectionPanel);
        }
        JButton next=new JButton("Continue");
        colorSelectionPanel.add(next);
        next.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                compt[0]--;
                if(compt[0]==0){
                    game.setColors(returnedValue);
                    colorSelectionPanel.setVisible(false);
                }
            }
        });
    }
}
