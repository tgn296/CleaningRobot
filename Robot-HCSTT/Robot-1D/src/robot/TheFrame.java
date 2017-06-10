/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

/**
 *
 * @author Mr-Tuy
 */
public class TheFrame extends JFrame{
    private BoardPanel board;
    TheFrame(){
        JMenuBar menuBar = new JMenuBar();
        
        JMenu option = new JMenu("Chon vat the");
        JMenuItem item1 = new JMenuItem("Robot");
        JMenuItem item2 = new JMenuItem("Vat can co dinh");
        JMenuItem item3 = new JMenuItem("Vat can di dong");
        JMenuItem item4 = new JMenuItem("O ban");
        JMenuItem item5 = new JMenuItem("Set pin");
        JMenuItem item6 = new JMenuItem("Diem sac pin");
        menuBar.add(option);
        option.add(item1);
        option.add(item2);
        option.add(item3);
        option.add(item4);
        option.add(item5);
        option.add(item6);
        
        JMenu playOrPause = new JMenu("Control");
        JMenuItem item21 = new JMenuItem("Run");
        JMenuItem item22 = new JMenuItem("Allow path");
        menuBar.add(playOrPause);
        playOrPause.add(item21);
        playOrPause.add(item22);
        
        
        this.setJMenuBar(menuBar);
        this.setSize(560,560);
        this.board = new BoardPanel();
        this.add(board);
        
        
        item1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                board.setMode(1);
            }
            
        });
        
        item2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                board.setMode(2);
            }
            
        });
        
        item3.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                board.setMode(3);
            }
            
        });
        
        item4.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                TheFrame.this.board.setAllDirty();
            }
            
        });
        
        item5.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                new Power(TheFrame.this);
            }
        });
        
        item6.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                board.setMode(6);
            }
        });
        
        item21.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                
                
//                for(int i: BoardUtil.findWayThroughUntouchable(board.getUntouchable(), 89)){
//                    System.out.println(i);
//                }




                board.setUntouchable();
                board.setCycle();
                if(board.getPower()==-1){
                    TheFrame.this.getBoard().runRobotWithoutPower();
                }else{
                    TheFrame.this.getBoard().runRobotWithPower();
                }
                
                JOptionPane.showMessageDialog(rootPane, "so o bi lap lai la " + (board.countRepeat()-2)+"/"+(board.getCycle().size()-2));
//                System.out.println("\n\n\n" + board.countRepeat());
                
                for (int i=0;i<196;++i){
                    if(board.checkFakeDirtyTile(i)){
//                        System.out.println(i);
                    } 
                }
                
                
            }
            
        });
        
        item22.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                board.setAllowPath();
            }
        });
        
        
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
    public BoardPanel getBoard(){
        return this.board;
    }
}
