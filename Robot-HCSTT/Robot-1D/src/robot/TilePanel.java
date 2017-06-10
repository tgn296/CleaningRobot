/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

/**
 *
 * @author Mr-Tuy
 */
public class TilePanel extends javax.swing.JPanel {
    private int index;
    private BoardPanel board;

    /**
     * Creates new form TilePanel
     */
    public TilePanel(int i,BoardPanel bp) {
        this.index = i;
        this.board = bp;
        initComponents();
        this.setPreferredSize(new Dimension(40,40));
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        this.setBorder(new LineBorder(Color.BLACK));
        
        this.setVisible(true);
        this.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
                TilePanel.this.clicked();
                
                
                if(board.getMode()==2){
                    board.addRealObstacle(index);
                    if((index % 14)%2 == 0 && (index /14)%2 == 0){
                        board.addObstacle(index+1);
                        board.addObstacle(index+14);
                        board.addObstacle(index+15);
                    }
                    if((index % 14)%2 == 1 && (index /14)%2 == 0){
                        board.addObstacle(index-1);
                        board.addObstacle(index+13);
                        board.addObstacle(index+14);
                    }
                    if((index % 14)%2 == 0 && (index /14)%2 == 1){
                        board.addObstacle(index+1);
                        board.addObstacle(index-14);
                        board.addObstacle(index-13);
                    }
                    if((index % 14)%2 == 1 && (index /14)%2 == 1){
                        board.addObstacle(index-1);
                        board.addObstacle(index-14);
                        board.addObstacle(index-15);
                    }
                }
                
                
                
                
//                if(board.getMode()==2){
//                    if((index % 14)%2 == 0 && (index /14)%2 == 0){
//                        board.getSpecificTile(index+1).clicked();
//                        board.getSpecificTile(index+14).clicked();
//                        board.getSpecificTile(index+15).clicked();
//                    }
//                    if((index % 14)%2 == 1 && (index /14)%2 == 0){
//                        board.getSpecificTile(index-1).clicked();
//                        board.getSpecificTile(index+14).clicked();
//                        board.getSpecificTile(index+13).clicked();
//                    }
//                    if((index % 14)%2 == 0 && (index /14)%2 == 1){
//                        board.getSpecificTile(index+1).clicked();
//                        board.getSpecificTile(index-14).clicked();
//                        board.getSpecificTile(index-13).clicked();
//                    }
//                    if((index % 14)%2 == 1 && (index /14)%2 == 1){
//                        board.getSpecificTile(index-1).clicked();
//                        board.getSpecificTile(index-14).clicked();
//                        board.getSpecificTile(index-15).clicked();
//                    }
//                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                
            }

            @Override
            public void mouseExited(MouseEvent e) {
                
            }
            
        });
    }
    public BoardPanel getBoard(){
        return this.board;
    }
    
    public int getIndex(){
        return this.index;
    }
    
    public boolean checkObstacle(){
        if(board.getObstacle().contains(index)){
            return true;
        }
        return false;
    }
    
    public void clicked(){
        if(board.getMode()==2){
            try {
                BufferedImage bi = ImageIO.read(new File("src/images/DO.png"));
                JLabel image = new JLabel(new ImageIcon(bi));
                TilePanel.this.add(image,BorderLayout.CENTER);
                TilePanel.this.repaint();
                TilePanel.this.validate();
                TilePanel.this.getBoard().setSize(700,700);
                TilePanel.this.getBoard().addObstacle(TilePanel.this.getIndex());
            } catch (IOException ex) {
                Logger.getLogger(TilePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            TilePanel.this.repaint();
            TilePanel.this.validate();
        }
        if(board.getMode() == -1){
            System.out.println("wrong");
        }
        if(board.getMode() == 1){
//            if(board.getExistRobot() != -1){
//                System.out.println("wrong1");
//            }else{
                try {
                    BufferedImage bi = ImageIO.read(new File("src/images/batdau.png"));
                    JLabel image = new JLabel(new ImageIcon(bi));
                    TilePanel.this.add(image,BorderLayout.CENTER);
                    TilePanel.this.repaint();
                    TilePanel.this.validate();
                    TilePanel.this.getBoard().setSize(700,700);
                    TilePanel.this.getBoard().setExistRobot(TilePanel.this.getIndex());
                    TilePanel.this.getBoard().setTemporaryRobotPosition(TilePanel.this.getIndex());
                } catch (IOException ex) {
                    Logger.getLogger(TilePanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                TilePanel.this.repaint();
                TilePanel.this.validate();
//            }
        }

        if(board.getMode()==3){
            try {
                BufferedImage bi = ImageIO.read(new File("src/images/moving.png"));
                JLabel image = new JLabel(new ImageIcon(bi));
                TilePanel.this.add(image,BorderLayout.CENTER);
                TilePanel.this.repaint();
                TilePanel.this.validate();
                TilePanel.this.getBoard().setSize(700,700);
                TilePanel.this.getBoard().addMovingObject(index);
            } catch (IOException ex) {
                Logger.getLogger(TilePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            TilePanel.this.repaint();
            TilePanel.this.validate();
        }

        if(board.getMode()==4){
            try {
                BufferedImage bi = ImageIO.read(new File("src/images/XP1.png"));
                JLabel image = new JLabel(new ImageIcon(bi));
                TilePanel.this.add(image,BorderLayout.CENTER);
                TilePanel.this.repaint();
                TilePanel.this.validate();
                TilePanel.this.getBoard().setSize(700,700);
            } catch (IOException ex) {
                Logger.getLogger(TilePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            TilePanel.this.repaint();
            TilePanel.this.validate();
        }

        if(board.getMode() == 6){
                TilePanel.this.removeAll();
                TilePanel.this.setBackground(Color.yellow);
                TilePanel.this.repaint();
                TilePanel.this.validate();
                TilePanel.this.getBoard().setSize(700,700);

            TilePanel.this.getBoard().setRechargePoint(index);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
