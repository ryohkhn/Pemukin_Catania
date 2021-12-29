package vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Gui extends JFrame{
    private GuiBoard guiBoard;
    private GuiSideBar guiSideBar;
    private int nbplayer;

    public Gui(){
        guiBoard=new GuiBoard();
        guiSideBar=new GuiSideBar();
        setUpGui();
    }

    public int getNbplayer(){
        return nbplayer;
    }

    public void setUpGui(){
        setTitle("Pemukin Catania");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width-700, screenSize.height-300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel playerSelection=new JPanel();
        playerSelection.add(new JLabel("Choose how many players you want in the game."),CENTER_ALIGNMENT);
        String[] values={"3","4"};
        JComboBox comboBox=new JComboBox(values);
        JButton next=new JButton("Continue");
        playerSelection.add(comboBox,CENTER_ALIGNMENT);
        playerSelection.add(next,CENTER_ALIGNMENT);
        getContentPane().add(playerSelection);
        final int[] returnedValue=new int[1];
        ActionListener buttonListener=new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                returnedValue[0]=Integer.parseInt((String)comboBox.getSelectedItem());
                nbplayer=returnedValue[0];
            }
        };
        next.addActionListener(buttonListener);

        /*getContentPane().setLayout(new GridLayout());
        guiSideBar.setBackground(Color.red);
        this.add(guiBoard);
        this.add(guiSideBar);
        guiSideBar.setVisible(false);
        guiBoard.setVisible(false);
         */


        setVisible(true);
    }

    public int chooseNbPlayers(){

        return 0;
    }
}
