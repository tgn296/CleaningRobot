/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.Timer;

/**
 *
 * @author Mr-Tuy
 */
public class BoardPanel extends javax.swing.JPanel {

    private int clickMode;
    private ArrayList<TilePanel> tileList;
    private ArrayList<Integer> movingList;
    private ArrayList<Integer> obstacleList;
    private int initiateRobot;
    private ArrayList<Integer> dirtyTile;
    private int robotPower;
    private int rechargePoint;
    private Power power;
    private int fullPower;
    private int robotTemporaryPosition;
    private int temporary;
    private ArrayList<Integer> cycle;
    private ArrayList<Integer> realObstacle;
    private int repeatCell;
    private ArrayList<Integer> untouchable;
    private RuleUtil rules;
    private boolean allowPath;
    

    public BoardPanel() {
        this.allowPath = false;
        this.rules = new RuleUtil();
        this.repeatCell = 0;
        this.temporary = -1;
        this.robotTemporaryPosition = -1;
        this.clickMode = -1;
        this.robotPower = -1;
        this.rechargePoint = -1;
        this.fullPower = -1;
        this.tileList = new ArrayList<TilePanel>();
        this.obstacleList = new ArrayList<Integer>();
        this.movingList = new ArrayList<Integer>();
        this.cycle = null;
        this.initiateRobot = -1;
        this.power = null;
        this.dirtyTile = new ArrayList<Integer>();
        this.realObstacle = new ArrayList<Integer>();
        this.untouchable = null;
        initComponents();
        this.setSize(560, 560);
        this.setLayout(new GridLayout(14, 14));
        for (int i = 0; i < 196; ++i) {
            TilePanel tp = new TilePanel(i, this);
            add(tp);
            tileList.add(tp);
        }
        repaint();
        validate();
        this.setVisible(true);

    }
    
    public void setAllowPath(){
        if(this.allowPath == true){
            this.allowPath = false;
        }else{
            this.allowPath = true;
        }
    }

    public void setPower(Power powerIndicator) {
        this.power = powerIndicator;
    }
    
    public void setCycle(){
        this.cycle = findRealCycle();
        cycle.add(cycle.get(0));
        cycle.add(cycle.get(1));
    }

    public int getMode() {
        return this.clickMode;
    }

    public void setMode(int mode) {
        this.clickMode = mode;
    }
    
    public int getRepeat(){
        return this.repeatCell;
    }
    
    public void addRealObstacle(int index){
        this.realObstacle.add((Integer)index);
    }

    public void addMovingObject(int location) {
        this.movingList.add(location);
    }

    public void addObstacle(int i) {
        this.obstacleList.add(i);
    }

    public void setExistRobot(int robotTile) {
        this.initiateRobot = robotTile;
    }

    public int getExistRobot() {
        return this.initiateRobot;
    }

    public void addDirty(int id) {
        this.dirtyTile.add(id);
    }
    
    public void setTemporaryRobotPosition(int index){
        this.robotTemporaryPosition = index;
        this.temporary = index;
    }

    public void setPower(int power) {
        this.robotPower = power;
        this.fullPower = power;
    }
    
    public void setUntouchable(){
        this.untouchable = getUntouchable();
    }
    
    public ArrayList<Integer> getCycle(){
        return this.cycle;
    }
    
    public int countRepeat(){
        int result =0;
        ArrayList<Integer> distinct = new ArrayList<Integer>();
        for(int i=0;i<cycle.size();++i){
            if(!distinct.contains(cycle.get(i))){
                distinct.add(cycle.get(i));
            }
        }
        for(int y : distinct){
            for(int x=0;x<cycle.size();++x){
                if(cycle.get(x) == y){
                    result++;
                }
            }
            result--;
        }
        
        return result;
    }
    
    public ArrayList<Integer> getUntouchable(){
        ArrayList<Integer> result = new ArrayList<Integer>();
        ArrayList<Integer> points = findSpanningPoint();
        ArrayList<Integer> tree = findSpanningTree(points);
        ArrayList<Integer> eliminated = new ArrayList<Integer>();
        int size = points.size();
        for(int i=0; i<size; ++i){
            int value = points.get(i);
            if(!tree.contains(value)){
                eliminated.add(value);
//                //System.out.println(value);
                continue;
            }
            points.add(value+1);
            points.add(value+14);
            points.add(value+15);
            
        }
        int robotPivot = 0;
        if((initiateRobot%14)%2 ==0 && (initiateRobot/14) %2 == 0){
            robotPivot = initiateRobot;
        }
        if((initiateRobot%14)%2 ==1 && (initiateRobot/14) %2 == 0){
            robotPivot = initiateRobot-1;
        }
        if((initiateRobot%14)%2 ==0 && (initiateRobot/14) %2 == 1){
            robotPivot = initiateRobot-14;
        }
        if((initiateRobot%14)%2 ==1 && (initiateRobot/14) %2 ==1){
            robotPivot = initiateRobot-15;
        }
        
        for(int i: eliminated){
            points.remove((Integer)i);
        }
        for (int i=0; i<196; ++i){
            if(i==(robotPivot-1)){
                result.add(i);
            }
            
            if(i==0){
                if((realObstacle.contains(15) || realObstacle.contains(1) || realObstacle.contains(14) || (!points.contains(2) && !points.contains(28))) && !realObstacle.contains(i)){
                    result.add(i);
                }
            }else if(i==13){
                if((realObstacle.contains(26) || realObstacle.contains(12) || realObstacle.contains(27) || (!points.contains(10) && !points.contains(40))) && !realObstacle.contains(i)){
                    result.add(i);
                }
            }else if(i==182){
                if((realObstacle.contains(169) || realObstacle.contains(168) || realObstacle.contains(183) || (!points.contains(140) && !points.contains(170))) && !realObstacle.contains(i)){
                    result.add(i);
                }
            }else if(i==195){
                if((realObstacle.contains(180) || realObstacle.contains(194) || realObstacle.contains(181) || (!points.contains(152) && !points.contains(178))) && !realObstacle.contains(i)){
                    result.add(i);
                }
            }else if(i>0 && i<13){
                if(!points.contains(i+14) && !points.contains(i+1) && !points.contains(i-1) && !realObstacle.contains(i)){
                    result.add(i);
                }
            }else if(i>182 && i <195){
                if(!points.contains(i+14) && !points.contains(i+1) && !points.contains(i-1) && !realObstacle.contains(i)){
                    result.add(i);
                }
            }else if(i%14 ==0 && i!=0 && i!=182){
                if(!points.contains(i+14) && !points.contains(i+1) && !points.contains(i-14) && !realObstacle.contains(i)){
                    result.add(i);
                }
            }else if(i%14 ==13 && i!=13 && i!=195){
                if(!points.contains(i+14) && !points.contains(i-14) && !points.contains(i-1) && !realObstacle.contains(i)){
                    result.add(i);
                }
            }else{
                if(!points.contains(i+14) && !points.contains(i+1) && !points.contains(i-1) && !points.contains(i-14) && !realObstacle.contains(i)){
                    result.add(i);
                }
            }
        }
        return result;
    }
    
    public int getPower() {
        return this.robotPower;
    }

    public void setRechargePoint(int point) {
        this.rechargePoint = point;
    }

    public int getRechargePoint() {
        return this.rechargePoint;
    }
    
    
    //return true if fake
    public boolean checkFakeDirtyTile(int index){
        if(obstacleList.contains((Integer)index) && !realObstacle.contains((Integer)index)){
            return true;
        }
        return false;
    }

    public void setAllDirty() {

        for (int id = 0; id < 196; ++id) {
//            ////System.out.println(id);
            if ((!this.realObstacle.contains(id))) {
//                ////System.out.println("we are here   "+id);
                this.addDirty(id);
//                ////System.out.println("add "+id);
                try {
                    BufferedImage bi = ImageIO.read(new File("src/images/XP1.png"));
                    JLabel image = new JLabel(new ImageIcon(bi));
                    this.getSpecificTile(id).add(image, BorderLayout.CENTER);
                    this.getSpecificTile(id).repaint();
                    this.getSpecificTile(id).validate();
                    this.getSpecificTile(id).getBoard().setSize(700, 700);

//                    ////System.out.println(id);
                } catch (IOException ex) {
                    Logger.getLogger(TilePanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                this.getSpecificTile(id).repaint();
                this.getSpecificTile(id).validate();
            }

        }

    }

    public ArrayList<Integer> getObstacle() {
        return this.obstacleList;
    }
    
    public ArrayList<Integer> getRealObstacle(){
        return this.realObstacle;
    }

    public TilePanel getSpecificTile(int index) {
        return this.tileList.get(index);
    }

    public ArrayList<Integer> getDirty() {
        return this.dirtyTile;
    }

    public boolean checkDirty(ArrayList<Integer> needToCheck) {
        int i = 0;
        ArrayList<Integer> lol = new ArrayList<Integer>();
        while (i < needToCheck.size()) {
            if (!lol.contains(needToCheck.get(i))) {
                lol.add(needToCheck.get(i));
            }
        }
        if (lol.size() == findSpanningPoint().size()) {
            return true;
        }
        return false;
    }

    public ArrayList<Integer> findSpanningPoint() {
        ArrayList<Integer> result = new ArrayList<Integer>();
        int i = 0;
        while (i < 182) {
            if ((dirtyTile.contains(i)) && (dirtyTile.contains(i + 1)) && (dirtyTile.contains(i + 14)) && (dirtyTile.contains(i + 15)) && ((i/14)%2 ==0)) {
                result.add(i);
                
            } 
            i+=2;
        }
        for(int ii = 0 ; ii < result.size(); ++ii){
            if((result.get(ii)== initiateRobot) || (result.get(ii) +1 == initiateRobot) || (result.get(ii) +14 == initiateRobot) || (result.get(ii) +15 == initiateRobot)){
                result.add(0,result.get(ii));
                break;
            }
            
        }
                
        return result;
    }

    public ArrayList<Integer> findSpanningTree(ArrayList<Integer> points) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        int index = points.get(0);
        int lastVisited = index - 2;
        result.add(index);
        int test = 0;
        while (test == 0) {
            if (lastVisited == index - 2) {
                if ((result.contains(index + 2) || !points.contains(index + 2))
                        && (result.contains(index + 28) || !points.contains(index + 28))
                        && (result.contains(index - 28) || !points.contains(index - 28))) {
                    int position = result.size() - 2;
                    if(position == -1){
                        return result;
                    }
                    int current = result.get(position);
                    while (true) {
                        if ((points.contains(current - 28) && !result.contains(current - 28))
                                || (points.contains(current + 28) && !result.contains(current + 28))
                                || (points.contains(current - 2) && !result.contains(current - 2))
                                || (points.contains(current + 2) && !result.contains(current + 2))) {
                            for (int i = result.size() - 2; i >= position; --i) {
                                result.add(result.get(i));
                            }
                            break;
                        }

                        position--;
                        if (position < 0) {
                            test = 1;
                            break;
                        } else {
                            current = result.get(position);
                        }
                    }
                    index = result.get(result.size() - 1);
                    lastVisited = result.get(result.size() - 2);
                } else if (points.contains(index + 2) && !result.contains(index + 2)) {
                    lastVisited = index;
                    index += 2;
                    result.add(index);
                } else if (points.contains(index - 28) && !result.contains(index - 28)) {
                    lastVisited = index;
                    index -= 28;
                    result.add(index);
                } else if (points.contains(index + 28) && !result.contains(index + 28)) {
                    lastVisited = index;
                    index += 28;
                    result.add(index);
                }
            }

            if (lastVisited == index + 2) {
                if ((result.contains(index + 28) || !points.contains(index + 28))
                        && (result.contains(index - 2) || !points.contains(index - 2))
                        && (result.contains(index - 28) || !points.contains(index - 28))) {
                    int position = result.size() - 2;
                    if(position == -1){
                        return result;
                    }
                    int current = result.get(position);
                    while (true) {
                        if ((points.contains(current - 28) && !result.contains(current - 28))
                                || (points.contains(current + 28) && !result.contains(current + 28))
                                || (points.contains(current - 2) && !result.contains(current - 2))
                                || (points.contains(current + 2) && !result.contains(current + 2))) {
                            for (int i = result.size() - 2; i >= position; --i) {
                                result.add(result.get(i));
                            }
                            break;
                        }

                        position--;
                        if (position <= -1) {
                            test = 1;
                            break;
                        } else {
                            current = result.get(position);
                        }
                    }
                    index = result.get(result.size() - 1);
                    lastVisited = result.get(result.size() - 2);
                } else if (points.contains(index - 28) && !result.contains(index - 28)) {
                    lastVisited = index;
                    index -= 28;
                    result.add(index);
                } else if (points.contains(index - 2) && !result.contains(index - 2)) {
                    lastVisited = index;
                    index -= 2;
                    result.add(index);
                } else if (points.contains(index + 28) && !result.contains(index + 28)) {
                    lastVisited = index;
                    index += 28;
                    result.add(index);
                }
            }

            if (lastVisited == index - 28) {
                if ((result.contains(index + 2) || !points.contains(index + 2))
                        && (result.contains(index - 2) || !points.contains(index - 2))
                        && (result.contains(index + 28) || !points.contains(index + 28))) {
                    int position = result.size() - 2;
                    if(position == -1){
                        return result;
                    }
                    int current = result.get(position);
                    while (true) {
                        if ((points.contains(current - 28) && !result.contains(current - 28))
                                || (points.contains(current + 28) && !result.contains(current + 28))
                                || (points.contains(current - 2) && !result.contains(current - 2))
                                || (points.contains(current + 2) && !result.contains(current + 2))) {
                            for (int i = result.size() - 2; i >= position; --i) {
                                result.add(result.get(i));
                            }
                            break;
                        }

                        position--;
                        if (position <= -1) {
                            test = 1;
                            break;
                        } else {
                            current = result.get(position);
                        }
                    }
                    index = result.get(result.size() - 1);
                    lastVisited = result.get(result.size() - 2);
                } else if (points.contains(index + 2) && !result.contains(index + 2)) {
                    lastVisited = index;
                    index += 2;
                    result.add(index);
                } else if (points.contains(index - 2) && !result.contains(index - 2)) {
                    lastVisited = index;
                    index -= 2;
                    result.add(index);
                } else if (points.contains(index + 28) && !result.contains(index + 28)) {
                    lastVisited = index;
                    index += 28;
                    result.add(index);
                }
            }

            if (lastVisited == index + 28) {
                if ((result.contains(index + 2) || !points.contains(index + 2))
                        && (result.contains(index - 2) || !points.contains(index - 2))
                        && (result.contains(index - 28) || !points.contains(index - 28))) {
                    int position = result.size() - 2;
                    if(position == -1){
                        return result;
                    }
                    int current = result.get(position);
                    while (true) {
                        if ((points.contains(current - 28) && !result.contains(current - 28))
                                || (points.contains(current + 28) && !result.contains(current + 28))
                                || (points.contains(current - 2) && !result.contains(current - 2))
                                || (points.contains(current + 2) && !result.contains(current + 2))) {
                            for (int i = result.size() - 2; i >= position; --i) {
                                result.add(result.get(i));
                            }
                            break;
                        }

                        position--;
                        if (position <= -1) {
                            test = 1;
                            break;
                        } else {
                            current = result.get(position);
                        }
                    }
                    index = result.get(result.size() - 1);
                    lastVisited = result.get(result.size() - 2);
                } else if (points.contains(index + 2) && !result.contains(index + 2)) {
                    lastVisited = index;
                    index += 2;
                    result.add(index);
                } else if (points.contains(index - 28) && !result.contains(index - 28)) {
                    lastVisited = index;
                    index -= 28;
                    result.add(index);
                } else if (points.contains(index - 2) && !result.contains(index - 2)) {
                    lastVisited = index;
                    index -= 2;
                    result.add(index);
                }
            }

        }
        return result;
    }
    
    public ArrayList<Integer> getState(int index, int last, ArrayList<Integer> fakeDirty){
        
        
        int pivot = -1;
        
        ArrayList<Integer> state = new ArrayList<Integer>();
        if(((index%14)%2 == 0) && ((index/14)%2 == 0)){
            pivot = index;
            state.add(21);
        }
        if(((index%14)%2 == 1) && ((index/14)%2 == 0)){
            pivot = index-1;
            state.add(22);
        }
        if(((index%14)%2 == 0) && ((index/14)%2 == 1)){
            pivot = index-14;
            state.add(23);
        }
        if(((index%14)%2 == 1) && ((index/14)%2 == 1)){
            pivot = index-15;
            state.add(24);
        }
        
        if(fakeDirty.contains(index-15) && (index%14 != 0)){
            state.add(31);
        }else{
            state.add(40);
        }
        if(fakeDirty.contains(index-14)){
            state.add(11);
        }else{
            state.add(28);
        }
        if(fakeDirty.contains(index-13) && (index%14 != 13)){
            state.add(33);
        }else{
            state.add(42);
        }
        if(fakeDirty.contains(index-1) && (index%14 != 0)){
            state.add(9);
        }else{
            state.add(26);
        }
        if(fakeDirty.contains(index+1) && (index%14 != 13)){
            state.add(10);
        }else{
            state.add(27);
        }
        if(fakeDirty.contains(index+13) && (index%14 != 0)){
            state.add(30);
        }else{
            state.add(39);
        }
        if(fakeDirty.contains(index+14)){
            state.add(12);
        }else{
            state.add(29);
        }
        if(fakeDirty.contains(index+15) && (index%14 != 13)){
            state.add(32);
        }else{
            state.add(41);
        }
        if(BoardUtil.anythingBeneath(findSpanningTree(findSpanningPoint()), pivot)){
            state.add(8);
        }else{
            state.add(20);
        }
        if(BoardUtil.anythingAbove(findSpanningTree(findSpanningPoint()), pivot)){
            state.add(7);
        }else{
            state.add(19);
        }
        if(BoardUtil.anythingLeft(findSpanningTree(findSpanningPoint()), pivot)){
            state.add(6);
        }else{
            state.add(17);
        }
        if(BoardUtil.anythingRight(findSpanningTree(findSpanningPoint()), pivot)){
            state.add(5);
        }else{
            state.add(18);
        }
        if(last == index-1){
            state.add(3);
        }
        if(last == index-14){
            state.add(2);
        }
        if(last == index+1){
            state.add(4);
        }
        if(last == index+14){
            state.add(1);
        }
        return state;
    }
    
    public ArrayList<Integer> doAction(int actionCode,int current, int last, ArrayList<Integer> fake, ArrayList<Integer> result){
        ArrayList<Integer> lastAndCurrent = new ArrayList<Integer>();
        if(actionCode == 101){
            lastAndCurrent.add(current);
            lastAndCurrent.add(current+14);
//            current+=14;
            result.add(current+14);
        }
        if(actionCode == 102){
            lastAndCurrent.add(current);
            lastAndCurrent.add(current-14);
//            current-=14;
            result.add(current-14);
        }
        if(actionCode == 103){
            lastAndCurrent.add(current);
            lastAndCurrent.add(current-1);
//            current--;
            result.add(current-1);
        }
        if(actionCode == 104){
            lastAndCurrent.add(current);
            lastAndCurrent.add(current+1);
//            current++;
//            //System.out.println("\t\t"+current);
            result.add(current+1);
        }
        
        if(actionCode == 115){
            result.add(current-1);
            if(untouchable.contains(current-2) && current%14 != 1){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current-2, this));
                result.add(current-1);
            }
            if(untouchable.contains(current+13)){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current+13, this));
                result.add(current-1);
            }
            if(untouchable.contains(current-15)){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current-15, this));
                result.add(current-1);
            }
            fake.remove((Integer)(current-1));
            result.add(current);
        }
        if(actionCode == 116){
            result.add(current+1);
            if(untouchable.contains(current+2) && current%14 != 13){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current+2, this));
                result.add(current+1);
            }
            if(untouchable.contains(current-13)){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current-13, this));
                result.add(current+1);
            }
            if(untouchable.contains(current+15)){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current+15, this));
                result.add(current+1);
            }
            fake.remove((Integer)(current+1));
            result.add(current);
        }
        if(actionCode == 117){
            result.add(current-14);
            if(untouchable.contains(current-28)){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current-28, this));
                result.add(current-14);
            }
            if(untouchable.contains(current-13) && current%14 != 13){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current-13, this));
                result.add(current-14);
            }
            if(untouchable.contains(current-15) && current%14 != 0){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current-15, this));
                result.add(current-14);
            }
            fake.remove((Integer)(current-14));
            result.add(current);
        }
        if(actionCode == 118){
            result.add(current+14);
            if(untouchable.contains(current+28)){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current+28, this));
                result.add(current+14);
            }
            if(untouchable.contains(current+13) && current%14 != 0){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current+13, this));
                result.add(current+14);
            }
            if(untouchable.contains(current+15) && current%14 != 13){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current+15, this));
                result.add(current+14);
            }
            fake.remove((Integer)(current+14));
            result.add(current);
        }
        
        
        
        if(actionCode == 107){
            result.add(current-1);
            if(untouchable.contains(current-2) && current%14 != 1){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current-2, this));
                result.add(current-1);
            }
            if(untouchable.contains(current-15)){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current-15, this));
                result.add(current-1);
            }
            result.add(current+13);
            if(untouchable.contains(current+12) && current%14 != 1){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current+12, this));
                result.add(current+13);
            }
            if(untouchable.contains(current+27)){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current+27, this));
                result.add(current+13);
            }
            result.add(current+14);
            fake.remove((Integer)(current-1));
            fake.remove((Integer)(current+13));
            lastAndCurrent.add(current);
            lastAndCurrent.add(current+14);
//            current+=14;
        }
        if(actionCode == 108){
            result.add(current-1);
            if(untouchable.contains(current-2) && current%14 != 1){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current-2, this));
                result.add(current-1);
            }
            if(untouchable.contains(current+13)){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current+13, this));
                result.add(current-1);
            }
            result.add(current-15);
            if(untouchable.contains(current-16) && current%14 != 1){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current-16, this));
                result.add(current-15);
            }
            if(untouchable.contains(current-29)){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current-29, this));
                result.add(current-15);
            }
            result.add(current-14);
            fake.remove((Integer)(current-1));
            fake.remove((Integer)(current-15));
            lastAndCurrent.add(current);
            lastAndCurrent.add(current-14);
//            current-=14;
        }
        if(actionCode == 109){
            result.add(current+1);
            if(untouchable.contains(current+2) && current%14 != 12){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current+2, this));
                result.add(current+1);
            }
            if(untouchable.contains(current-13)){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current-13, this));
                result.add(current+1);
            }
            result.add(current+15);
            if(untouchable.contains(current+16) && current%14 != 12){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current+16, this));
                result.add(current+15);
            }
            if(untouchable.contains(current+29)){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current+29, this));
                result.add(current+15);
            }
            result.add(current+14);
            fake.remove((Integer)(current+1));
            fake.remove((Integer)(current+15));
            lastAndCurrent.add(current);
            lastAndCurrent.add(current+14);
//            current+=14;
        }
        if(actionCode == 110){
            result.add(current+1);
            if(untouchable.contains(current+2) && current%14 != 12){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current+2, this));
                result.add(current+1);
            }
            if(untouchable.contains(current+15)){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current+15, this));
                result.add(current+1);
            }
            result.add(current-13);
            if(untouchable.contains(current-12) && current%14 != 12){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current-12, this));
                result.add(current-13);
            }
            if(untouchable.contains(current-27)){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current-27, this));
                result.add(current-13);
            }
            result.add(current-14);
            fake.remove((Integer)(current+1));
            fake.remove((Integer)(current-13));
            lastAndCurrent.add(current);
            lastAndCurrent.add(current-14);
//            current-=14;
        }
        if(actionCode == 111){
            result.add(current+14);
            if(untouchable.contains(current+15) && current%14 != 13){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current+15, this));
                result.add(current+14);
            }
            if(untouchable.contains(current+28)){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current+28, this));
                result.add(current+14);
            }
            result.add(current+13);
            if(untouchable.contains(current+12) && current%14 != 1){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current+12, this));
                result.add(current+13);
            }
            if(untouchable.contains(current+27)){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current+27, this));
                result.add(current+13);
            }
            result.add(current-1);
            fake.remove((Integer)(current+14));
            fake.remove((Integer)(current+13));
            lastAndCurrent.add(current);
            lastAndCurrent.add(current-1);
//            current--;
        }
        if(actionCode == 112){
            result.add(current+14);
            if(untouchable.contains(current+13) && current%14 != 0){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current+13, this));
                result.add(current+14);
            }
            if(untouchable.contains(current+28)){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current+28, this));
                result.add(current+14);
            }
            result.add(current+15);
            if(untouchable.contains(current+16) && current%14 != 12){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current+16, this));
                result.add(current+15);
            }
            if(untouchable.contains(current+29)){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current+29, this));
                result.add(current+15);
            }
            result.add(current+1);
            fake.remove((Integer)(current+14));
            fake.remove((Integer)(current+15));
            lastAndCurrent.add(current);
            lastAndCurrent.add(current+1);
//            current++;
        }
        if(actionCode == 113){
            result.add(current-14);
            if(untouchable.contains(current-13) && current%14 != 13){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current-13, this));
                result.add(current-14);
            }
            if(untouchable.contains(current-28)){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current-28, this));
                result.add(current-14);
            }
            result.add(current-15);
            if(untouchable.contains(current-16) && current%14 != 1){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current-16, this));
                result.add(current-15);
            }
            if(untouchable.contains(current-29)){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current-29, this));
                result.add(current-15);
            }
            result.add(current-1);
            fake.remove((Integer)(current-14));
            fake.remove((Integer)(current-15));
            lastAndCurrent.add(current);
            lastAndCurrent.add(current-1);
//            current--;
        }
        if(actionCode == 114){
            result.add(current-14);
            if(untouchable.contains(current-15) && current%14 != 0){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current-15, this));
                result.add(current-14);
            }
            if(untouchable.contains(current-28)){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current-28, this));
                result.add(current-14);
            }
            result.add(current-13);
            if(untouchable.contains(current-12) && current%14 != 12){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current-12, this));
                result.add(current-13);
            }
            if(untouchable.contains(current-27)){
                result.addAll(BoardUtil.findWayThroughUntouchable(untouchable, current-27, this));
                result.add(current-13);
            }
            result.add(current+1);
            fake.remove((Integer)(current-14));
            fake.remove((Integer)(current-13));
            lastAndCurrent.add(current);
            lastAndCurrent.add(current+1);
//            current++;
        }
        
        
        return lastAndCurrent;
    }
    
    public ArrayList<Integer> findCycleKBS(){
        ArrayList<Integer> result = new ArrayList<Integer>();
        ArrayList<Integer> tree = findSpanningTree(findSpanningPoint());
        int value = tree.get(0);
        int last = value - 1;
        result.add(value);
        ArrayList<Integer> fakeDirty = new ArrayList<Integer>();
        for(int i=0; i<196; ++i){
            if(checkFakeDirtyTile(i)){
                fakeDirty.add(i);
            }
        }
        while ((result.get(result.size() - 1) != result.get(0)) || (result.size() == 1)) {
            //System.out.println(result.get(result.size()-1));
            ArrayList<Integer> currentState = getState(value, last, fakeDirty);
            //System.out.println("aaa");
            for(int i: currentState){
                //System.out.println(i);
            }
            //System.out.println("bbb");
            ArrayList<Integer> action = rules.getActionFromCondition(currentState);
            //System.out.println("ccc");
            for(int i: action){
                //System.out.println(i);
            }
            //System.out.println("ddd");
            for(int i=0; i< action.size();++i){
                ArrayList<Integer> afterAction = doAction(action.get(i),value,last,fakeDirty,result);
                if(afterAction.size()!=0){
                    value = afterAction.get(1);
                    last = afterAction.get(0);
                }
            }
        }
        for(int i=0;i<result.size();++i){
            //System.out.println(result.get(i));
        }
        return result;
    }


    public ArrayList<Integer> findRealCycle() {
        ArrayList<Integer> cycle = findCycleKBS();
        ArrayList<Integer> result = new ArrayList<Integer>();

        int position = 0;
        while (cycle.get(position) != initiateRobot) {
            position++;
        }
        int start = position;
        while (position < cycle.size()) {
            result.add(cycle.get(position));
            position++;
        }
        position = 0;
        while (position < start) {
            result.add(cycle.get(position));
            position++;
        }
        return result;
    }

    public void runRobotWithoutPower() {
//        ArrayList<Integer> cycle = findRealCycle();
        int local[] = {0};
        Timer timer = new Timer(150, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                moveObject();

                int next = local[0] + 1;
                if (movingList.contains(cycle.get(local[0] + 1)) || movingList.contains(cycle.get(local[0] + 1)+1) || movingList.contains(cycle.get(local[0] + 1)-1)
                        || movingList.contains(cycle.get(local[0] + 1)+14) || movingList.contains(cycle.get(local[0] + 1)-14)) {

                } else {
                    
//                    if (dirtyTile.isEmpty()) {
                    if (local[0] == cycle.size() - 2) {
                        ((Timer) e.getSource()).stop();
                    }
                    tileList.get(cycle.get(local[0])).removeAll();
                    dirtyTile.remove(cycle.get(local[0]));
                    if(allowPath == true){
                        if((local[0]<=2) || (cycle.get(local[0]) - cycle.get(local[0]-2)) != 0){
                            if(local[0]>2){
                                //System.out.println("aaa"+local[0]+"--"+cycle.get(local[0])+"--"+cycle.get(local[0]-2));
                            }else{
                                //System.out.println("aaa"+local[0]);
                            }
                            if((local[0]!=0) && (local[0]!=cycle.size()-3) && (((cycle.get(local[0])==(cycle.get(local[0]-1)+1)) && (cycle.get(local[0])==(cycle.get(local[0]+1)-1)))  ||
                                    ((cycle.get(local[0])==(cycle.get(local[0]-1)-1)) && (cycle.get(local[0])==(cycle.get(local[0]+1)+1))))){
                                try {
                                    //System.out.println("-1+1     1          " + cycle.get(local[0]));
                                    BufferedImage bi = ImageIO.read(new File("src/images/ngang.png"));
                                    JLabel image = new JLabel(new ImageIcon(bi));
                                    tileList.get(cycle.get(local[0])).add(image, BorderLayout.CENTER);
                                    tileList.get(cycle.get(local[0])).revalidate();
                                    tileList.get(cycle.get(local[0])).repaint();
                                    temporary = cycle.get(local[0]);
                                    ////System.out.println("robot is here " + temporary);

                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }else
                            if((local[0]!=0) && (local[0]!=cycle.size()-3) && (((cycle.get(local[0])==(cycle.get(local[0]-1)+14)) && (cycle.get(local[0])==(cycle.get(local[0]+1)-14)))  ||
                                    ((cycle.get(local[0])==(cycle.get(local[0]-1)-14)) && (cycle.get(local[0])==(cycle.get(local[0]+1)+14))))){
                                try {
                                    //System.out.println("-14+14     1         " + cycle.get(local[0]));
                                    BufferedImage bi = ImageIO.read(new File("src/images/doc.png"));
                                    JLabel image = new JLabel(new ImageIcon(bi));
                                    tileList.get(cycle.get(local[0])).add(image, BorderLayout.CENTER);
                                    tileList.get(cycle.get(local[0])).revalidate();
                                    tileList.get(cycle.get(local[0])).repaint();
                                    temporary = cycle.get(local[0]);
                                    ////System.out.println("robot is here " + temporary);

                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }else
                            if((local[0]!=0) && (local[0]!=cycle.size()-3) && (((cycle.get(local[0])==(cycle.get(local[0]-1)+14)) && (cycle.get(local[0])==(cycle.get(local[0]+1)-1)))  ||
                                    ((cycle.get(local[0])==(cycle.get(local[0]-1)-1)) && (cycle.get(local[0])==(cycle.get(local[0]+1)+14))))){
                                try {
    //                                //System.out.println("-1+14     1           " + cycle.get(local[0]) +"--"+cycle.get(local[0]-1) +"--"+cycle.get(local[0]-2) +"--"+cycle.get(local[0]-3));
                                    BufferedImage bi = ImageIO.read(new File("src/images/phai_tren.png"));
                                    JLabel image = new JLabel(new ImageIcon(bi));
                                    tileList.get(cycle.get(local[0])).add(image, BorderLayout.CENTER);
                                    tileList.get(cycle.get(local[0])).revalidate();
                                    tileList.get(cycle.get(local[0])).repaint();
                                    temporary = cycle.get(local[0]);
                                    ////System.out.println("robot is here " + temporary);

                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }else
                            if((local[0]!=0) && (local[0]!=cycle.size()-3) && (((cycle.get(local[0])==(cycle.get(local[0]-1)+14)) && (cycle.get(local[0])==(cycle.get(local[0]+1)+1)))  ||
                                    ((cycle.get(local[0])==(cycle.get(local[0]-1)+1)) && (cycle.get(local[0])==(cycle.get(local[0]+1)+14))))){
                                try {
                                    //System.out.println("+1+14     1           " + cycle.get(local[0]));
                                    BufferedImage bi = ImageIO.read(new File("src/images/trai_tren.png"));
                                    JLabel image = new JLabel(new ImageIcon(bi));
                                    tileList.get(cycle.get(local[0])).add(image, BorderLayout.CENTER);
                                    tileList.get(cycle.get(local[0])).revalidate();
                                    tileList.get(cycle.get(local[0])).repaint();
                                    temporary = cycle.get(local[0]);
                                    ////System.out.println("robot is here " + temporary);

                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }else
                            if((local[0]!=0) && (local[0]!=cycle.size()-3) && (((cycle.get(local[0])==(cycle.get(local[0]-1)+1)) && (cycle.get(local[0])==(cycle.get(local[0]+1)-14)))  ||
                                    ((cycle.get(local[0])==(cycle.get(local[0]-1)-14)) && (cycle.get(local[0])==(cycle.get(local[0]+1)+1))))){
                                try {
                                    //System.out.println("-14+1     1           " + cycle.get(local[0]));
                                    BufferedImage bi = ImageIO.read(new File("src/images/trai_duoi.png"));
                                    JLabel image = new JLabel(new ImageIcon(bi));
                                    tileList.get(cycle.get(local[0])).add(image, BorderLayout.CENTER);
                                    tileList.get(cycle.get(local[0])).revalidate();
                                    tileList.get(cycle.get(local[0])).repaint();
                                    temporary = cycle.get(local[0]);
                                    ////System.out.println("robot is here " + temporary);

                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }else
                            if((local[0]!=0) && (local[0]!=cycle.size()-3) && (((cycle.get(local[0])==(cycle.get(local[0]-1)-1)) && (cycle.get(local[0])==(cycle.get(local[0]+1)-14)))  ||
                                    ((cycle.get(local[0])==(cycle.get(local[0]-1)-14)) && (cycle.get(local[0])==(cycle.get(local[0]+1)-1))))){
                                try {
                                    //System.out.println("-1-14     1           " + cycle.get(local[0]));
                                    BufferedImage bi = ImageIO.read(new File("src/images/phai_duoi.png"));
                                    JLabel image = new JLabel(new ImageIcon(bi));
                                    tileList.get(cycle.get(local[0])).add(image, BorderLayout.CENTER);
                                    tileList.get(cycle.get(local[0])).revalidate();
                                    tileList.get(cycle.get(local[0])).repaint();
                                    temporary = cycle.get(local[0]);
                                    ////System.out.println("robot is here " + temporary);

                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }else if(local[0]>=1){
                                if(cycle.get(local[0]+1) - cycle.get(local[0]-1) == 0){
                                    //System.out.println("3   "+cycle.get(local[0]));
                                    if(cycle.get(local[0]-1) == cycle.get(local[0]) + 1){
                                        try {
            //                                //System.out.println("-1-14     2           " + cycle.get(local[0]));
                                            BufferedImage bi = ImageIO.read(new File("src/images/phai.png"));
                                            JLabel image = new JLabel(new ImageIcon(bi));
                                            tileList.get(cycle.get(local[0])).add(image, BorderLayout.CENTER);
                                            tileList.get(cycle.get(local[0])).revalidate();
                                            tileList.get(cycle.get(local[0])).repaint();
                                            temporary = cycle.get(local[0]);
                                            ////System.out.println("robot is here " + temporary);

                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                    if(cycle.get(local[0]-1) == cycle.get(local[0]) + 14){
                                        try {
            //                                //System.out.println("-1-14     2           " + cycle.get(local[0]));
                                            BufferedImage bi = ImageIO.read(new File("src/images/duoi.png"));
                                            JLabel image = new JLabel(new ImageIcon(bi));
                                            tileList.get(cycle.get(local[0])).add(image, BorderLayout.CENTER);
                                            tileList.get(cycle.get(local[0])).revalidate();
                                            tileList.get(cycle.get(local[0])).repaint();
                                            temporary = cycle.get(local[0]);
                                            ////System.out.println("robot is here " + temporary);

                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                    if(cycle.get(local[0]-1) == cycle.get(local[0]) - 1){
                                        try {
            //                                //System.out.println("-1-14     2           " + cycle.get(local[0]));
                                            BufferedImage bi = ImageIO.read(new File("src/images/trai.png"));
                                            JLabel image = new JLabel(new ImageIcon(bi));
                                            tileList.get(cycle.get(local[0])).add(image, BorderLayout.CENTER);
                                            tileList.get(cycle.get(local[0])).revalidate();
                                            tileList.get(cycle.get(local[0])).repaint();
                                            temporary = cycle.get(local[0]);
                                            ////System.out.println("robot is here " + temporary);

                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                    if(cycle.get(local[0]-1) == cycle.get(local[0]) - 14){
                                        try {
            //                                //System.out.println("-1-14     2           " + cycle.get(local[0]));
                                            BufferedImage bi = ImageIO.read(new File("src/images/tren.png"));
                                            JLabel image = new JLabel(new ImageIcon(bi));
                                            tileList.get(cycle.get(local[0])).add(image, BorderLayout.CENTER);
                                            tileList.get(cycle.get(local[0])).revalidate();
                                            tileList.get(cycle.get(local[0])).repaint();
                                            temporary = cycle.get(local[0]);
                                            ////System.out.println("robot is here " + temporary);

                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }else if(cycle.get(local[0]) - cycle.get(local[0]-2)==0){
                            if((local[0]!=0) && (local[0]!=cycle.size()-3) && (((cycle.get(local[0])==(cycle.get(local[0]-3)+1)) && (cycle.get(local[0])==(cycle.get(local[0]+1)-1)))  ||
                                    ((cycle.get(local[0])==(cycle.get(local[0]-3)-1)) && (cycle.get(local[0])==(cycle.get(local[0]+1)+1))))){
                                try {
                                    //System.out.println("-1+1     2           " + cycle.get(local[0]));
                                    BufferedImage bi = ImageIO.read(new File("src/images/ngang.png"));
                                    JLabel image = new JLabel(new ImageIcon(bi));
                                    tileList.get(cycle.get(local[0])).add(image, BorderLayout.CENTER);
                                    tileList.get(cycle.get(local[0])).revalidate();
                                    tileList.get(cycle.get(local[0])).repaint();
                                    temporary = cycle.get(local[0]);
                                    ////System.out.println("robot is here " + temporary);

                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                            if((local[0]!=0) && (local[0]!=cycle.size()-3) && (((cycle.get(local[0])==(cycle.get(local[0]-3)+14)) && (cycle.get(local[0])==(cycle.get(local[0]+1)-14)))  ||
                                    ((cycle.get(local[0])==(cycle.get(local[0]-3)-14)) && (cycle.get(local[0])==(cycle.get(local[0]+1)+14))))){
                                try {
                                    //System.out.println("-14+14     2           " + cycle.get(local[0]));
                                    BufferedImage bi = ImageIO.read(new File("src/images/doc.png"));
                                    JLabel image = new JLabel(new ImageIcon(bi));
                                    tileList.get(cycle.get(local[0])).add(image, BorderLayout.CENTER);
                                    tileList.get(cycle.get(local[0])).revalidate();
                                    tileList.get(cycle.get(local[0])).repaint();
                                    temporary = cycle.get(local[0]);
                                    ////System.out.println("robot is here " + temporary);

                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                            if((local[0]!=0) && (local[0]!=cycle.size()-3) && (((cycle.get(local[0])==(cycle.get(local[0]-3)+14)) && (cycle.get(local[0])==(cycle.get(local[0]+1)-1)))  ||
                                    ((cycle.get(local[0])==(cycle.get(local[0]-3)-1)) && (cycle.get(local[0])==(cycle.get(local[0]+1)+14))))){
                                try {
                                    //System.out.println("-1+14     2           " + cycle.get(local[0]));
                                    BufferedImage bi = ImageIO.read(new File("src/images/phai_tren.png"));
                                    JLabel image = new JLabel(new ImageIcon(bi));
                                    tileList.get(cycle.get(local[0])).add(image, BorderLayout.CENTER);
                                    tileList.get(cycle.get(local[0])).revalidate();
                                    tileList.get(cycle.get(local[0])).repaint();
                                    temporary = cycle.get(local[0]);
                                    ////System.out.println("robot is here " + temporary);

                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                            if((local[0]!=0) && (local[0]!=cycle.size()-3) && (((cycle.get(local[0])==(cycle.get(local[0]-3)+14)) && (cycle.get(local[0])==(cycle.get(local[0]+1)+1)))  ||
                                    ((cycle.get(local[0])==(cycle.get(local[0]-3)+1)) && (cycle.get(local[0])==(cycle.get(local[0]+1)+14))))){
                                try {
                                    //System.out.println("+1+14     2           " + cycle.get(local[0]));
                                    BufferedImage bi = ImageIO.read(new File("src/images/trai_tren.png"));
                                    JLabel image = new JLabel(new ImageIcon(bi));
                                    tileList.get(cycle.get(local[0])).add(image, BorderLayout.CENTER);
                                    tileList.get(cycle.get(local[0])).revalidate();
                                    tileList.get(cycle.get(local[0])).repaint();
                                    temporary = cycle.get(local[0]);
                                    ////System.out.println("robot is here " + temporary);

                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                            if((local[0]!=0) && (local[0]!=cycle.size()-3) && (((cycle.get(local[0])==(cycle.get(local[0]-3)+1)) && (cycle.get(local[0])==(cycle.get(local[0]+1)-14)))  ||
                                    ((cycle.get(local[0])==(cycle.get(local[0]-3)-14)) && (cycle.get(local[0])==(cycle.get(local[0]+1)+1))))){
                                try {
                                    //System.out.println("-14+1     2           " + cycle.get(local[0]));
                                    BufferedImage bi = ImageIO.read(new File("src/images/trai_duoi.png"));
                                    JLabel image = new JLabel(new ImageIcon(bi));
                                    tileList.get(cycle.get(local[0])).add(image, BorderLayout.CENTER);
                                    tileList.get(cycle.get(local[0])).revalidate();
                                    tileList.get(cycle.get(local[0])).repaint();
                                    temporary = cycle.get(local[0]);
                                    ////System.out.println("robot is here " + temporary);

                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                            if((local[0]!=0) && (local[0]!=cycle.size()-3) && (((cycle.get(local[0])==(cycle.get(local[0]-3)-1)) && (cycle.get(local[0])==(cycle.get(local[0]+1)-14)))  ||
                                    ((cycle.get(local[0])==(cycle.get(local[0]-3)-14)) && (cycle.get(local[0])==(cycle.get(local[0]+1)-1))))){
                                try {
                                    //System.out.println("-1-14     2           " + cycle.get(local[0]));
                                    BufferedImage bi = ImageIO.read(new File("src/images/phai_duoi.png"));
                                    JLabel image = new JLabel(new ImageIcon(bi));
                                    tileList.get(cycle.get(local[0])).add(image, BorderLayout.CENTER);
                                    tileList.get(cycle.get(local[0])).revalidate();
                                    tileList.get(cycle.get(local[0])).repaint();
                                    temporary = cycle.get(local[0]);
                                    ////System.out.println("robot is here " + temporary);

                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    }
                    tileList.get(cycle.get(local[0])).repaint();
                    tileList.get(cycle.get(local[0])).revalidate();
                    tileList.get(cycle.get(local[0] + 1)).removeAll();
                    dirtyTile.remove(cycle.get(local[0] + 1));
                    try {
                        BufferedImage bi = ImageIO.read(new File("src/images/batdau.png"));
                        JLabel image = new JLabel(new ImageIcon(bi));
                        tileList.get(cycle.get(local[0] + 1)).add(image, BorderLayout.CENTER);
                        tileList.get(cycle.get(local[0] + 1)).revalidate();
                        tileList.get(cycle.get(local[0] + 1)).repaint();
                        temporary = cycle.get(local[0] + 1);
                        ////System.out.println("robot is here " + temporary);

                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    local[0]++;

                }
            }
        });
        timer.start();
    }

    public void runRobotWithPower() {
        int local[] = {0};
        while(cycle.get(local[0]) != robotTemporaryPosition){
            ++local[0];
        }
        Timer timer = new Timer(150, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                moveObject();

                if (movingList.contains(cycle.get(local[0] + 1)) || movingList.contains(cycle.get(local[0] + 1)+1) || movingList.contains(cycle.get(local[0] + 1)-1)
                        || movingList.contains(cycle.get(local[0] + 1)+14) || movingList.contains(cycle.get(local[0] + 1)-14)) {

                } else {
                    if ((robotPower - 2) < findWayToRechargePoint(cycle.get(1 + local[0])).size()) {
                        ////System.out.println("we wont reach " + cycle.get(1 + local[0]));
                        power.makeWarning();
                        ((Timer) e.getSource()).stop();
                        robotTemporaryPosition = cycle.get(local[0]);
                        goBackToRechargePoint(cycle.get(local[0]));
                    } else if (dirtyTile.isEmpty()) {
                        ((Timer) e.getSource()).stop();
                    } else {
                        tileList.get(cycle.get(local[0])).removeAll();
                        dirtyTile.remove(cycle.get(local[0]));
                        tileList.get(cycle.get(local[0])).repaint();
                        tileList.get(cycle.get(local[0])).revalidate();
                        tileList.get(cycle.get(local[0] + 1)).removeAll();
                        dirtyTile.remove(cycle.get(local[0] + 1));
                        try {
                            BufferedImage bi = ImageIO.read(new File("src/images/batdau.png"));
                            JLabel image = new JLabel(new ImageIcon(bi));
                            tileList.get(cycle.get(local[0] + 1)).add(image, BorderLayout.CENTER);
                            tileList.get(cycle.get(local[0] + 1)).revalidate();
                            tileList.get(cycle.get(local[0] + 1)).repaint();
                            temporary = cycle.get(local[0] + 1);
                            ////System.out.println("robot is here " + temporary);

                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        ////System.out.println("reach " + cycle.get(1 + local[0]));
                        robotPower-=2;
                        power.getTextField().setText(((Integer) robotPower).toString());
                        local[0]++;
                    }

                }
            }
        });
        timer.start();
    }

    public void moveObjectToLeft(int position) {
        ////System.out.println(position + "\tleft");
        tileList.get(position).removeAll();
        tileList.get(position).repaint();
        tileList.get(position).revalidate();

        if (dirtyTile.contains(position)) {
            try {
                BufferedImage bi = ImageIO.read(new File("src/images/XP1.png"));
                JLabel image = new JLabel(new ImageIcon(bi));
                tileList.get(position).add(image, BorderLayout.CENTER);
                tileList.get(position).repaint();
                tileList.get(position).revalidate();
                BoardPanel.this.setSize(700, 700);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        tileList.get(position).repaint();
        tileList.get(position).revalidate();

        tileList.get(position - 1).removeAll();
        tileList.get(position - 1).repaint();
        tileList.get(position - 1).revalidate();

        if (dirtyTile.contains(position - 1)) {
            try {
                BufferedImage bi = ImageIO.read(new File("src/images/XP1.png"));
                JLabel image = new JLabel(new ImageIcon(bi));
                tileList.get(position - 1).add(image, BorderLayout.CENTER);
                tileList.get(position - 1).repaint();
                tileList.get(position - 1).revalidate();
                BoardPanel.this.setSize(700, 700);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        tileList.get(position - 1).repaint();
        tileList.get(position - 1).revalidate();

        try {
            BufferedImage bi = ImageIO.read(new File("src/images/moving.png"));
            JLabel image = new JLabel(new ImageIcon(bi));
            tileList.get(position - 1).add(image, BorderLayout.CENTER);
            tileList.get(position - 1).repaint();
            tileList.get(position - 1).revalidate();
            BoardPanel.this.setSize(700, 700);

        } catch (Exception ee) {
            ee.printStackTrace();
        }

    }

    public void moveObjectToRight(int position) {
        ////System.out.println(position + "\tright");
        tileList.get(position).removeAll();
        tileList.get(position).repaint();
        tileList.get(position).revalidate();

        if (dirtyTile.contains(position)) {
            try {
                BufferedImage bi = ImageIO.read(new File("src/images/XP1.png"));
                JLabel image = new JLabel(new ImageIcon(bi));
                tileList.get(position).add(image, BorderLayout.CENTER);
                tileList.get(position).repaint();
                tileList.get(position).revalidate();
                BoardPanel.this.setSize(700, 700);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        tileList.get(position).repaint();
        tileList.get(position).revalidate();

        tileList.get(position + 1).removeAll();
        tileList.get(position + 1).repaint();
        tileList.get(position + 1).revalidate();

        if (dirtyTile.contains(position + 1)) {
            try {
                BufferedImage bi = ImageIO.read(new File("src/images/XP1.png"));
                JLabel image = new JLabel(new ImageIcon(bi));
                tileList.get(position + 1).add(image, BorderLayout.CENTER);
                tileList.get(position + 1).repaint();
                tileList.get(position + 1).revalidate();
                BoardPanel.this.setSize(700, 700);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        tileList.get(position + 1).repaint();
        tileList.get(position + 1).revalidate();

        try {
            BufferedImage bi = ImageIO.read(new File("src/images/moving.png"));
            JLabel image = new JLabel(new ImageIcon(bi));
            tileList.get(position + 1).add(image, BorderLayout.CENTER);
            tileList.get(position + 1).repaint();
            tileList.get(position + 1).revalidate();
            BoardPanel.this.setSize(700, 700);

        } catch (Exception ee) {
            ee.printStackTrace();
        }

    }

    public void moveObjectUp(int position) {
        ////System.out.println(position + "\ttop");
        tileList.get(position).removeAll();
        tileList.get(position).repaint();
        tileList.get(position).revalidate();

        if (dirtyTile.contains(position)) {
            try {
                BufferedImage bi = ImageIO.read(new File("src/images/XP1.png"));
                JLabel image = new JLabel(new ImageIcon(bi));
                tileList.get(position).add(image, BorderLayout.CENTER);
                tileList.get(position).repaint();
                tileList.get(position).revalidate();
                BoardPanel.this.setSize(700, 700);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        tileList.get(position).repaint();
        tileList.get(position).revalidate();

        tileList.get(position - 14).removeAll();
        tileList.get(position - 14).repaint();
        tileList.get(position - 14).revalidate();

        if (dirtyTile.contains(position - 14)) {
            try {
                BufferedImage bi = ImageIO.read(new File("src/images/XP1.png"));
                JLabel image = new JLabel(new ImageIcon(bi));
                tileList.get(position - 14).add(image, BorderLayout.CENTER);
                tileList.get(position - 14).repaint();
                tileList.get(position - 14).revalidate();
                BoardPanel.this.setSize(700, 700);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        tileList.get(position - 14).repaint();
        tileList.get(position - 14).revalidate();

        try {
            BufferedImage bi = ImageIO.read(new File("src/images/moving.png"));
            JLabel image = new JLabel(new ImageIcon(bi));
            tileList.get(position - 14).add(image, BorderLayout.CENTER);
            tileList.get(position - 14).repaint();
            tileList.get(position - 14).revalidate();
            BoardPanel.this.setSize(700, 700);

        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public void moveObjectDown(int position) {
        ////System.out.println(position + "\tunder");
        tileList.get(position).removeAll();
        tileList.get(position).repaint();
        tileList.get(position).revalidate();

        if (dirtyTile.contains(position)) {
            try {
                BufferedImage bi = ImageIO.read(new File("src/images/XP1.png"));
                JLabel image = new JLabel(new ImageIcon(bi));
                tileList.get(position).add(image, BorderLayout.CENTER);
                tileList.get(position).repaint();
                tileList.get(position).revalidate();
                BoardPanel.this.setSize(700, 700);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        tileList.get(position).repaint();
        tileList.get(position).revalidate();

        tileList.get(position + 14).removeAll();
        tileList.get(position + 14).repaint();
        tileList.get(position + 14).revalidate();

        if (dirtyTile.contains(position + 14)) {
            try {
                BufferedImage bi = ImageIO.read(new File("src/images/XP1.png"));
                JLabel image = new JLabel(new ImageIcon(bi));
                tileList.get(position + 14).add(image, BorderLayout.CENTER);
                tileList.get(position + 14).repaint();
                tileList.get(position + 14).revalidate();
                BoardPanel.this.setSize(700, 700);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        tileList.get(position + 14).repaint();
        tileList.get(position + 14).revalidate();

        try {
            BufferedImage bi = ImageIO.read(new File("src/images/moving.png"));
            JLabel image = new JLabel(new ImageIcon(bi));
            tileList.get(position + 14).add(image, BorderLayout.CENTER);
            tileList.get(position + 14).repaint();
            tileList.get(position + 14).revalidate();
            BoardPanel.this.setSize(700, 700);

        } catch (Exception ee) {
            ee.printStackTrace();
        }

    }

    public void moveObject() {
        int count = 0;
        while (count < movingList.size()) {
            double ran = Math.random();
            if (ran < 0.25) {
                if (movingList.get(0) % 14 != 13 && !realObstacle.contains(movingList.get(0) + 1) && ((movingList.get(0) + 1) != temporary)) {
                    moveObjectToRight(movingList.get(0));
                    movingList.add(movingList.get(0) + 1);
                    movingList.remove(0);
                } else if (movingList.get(0) / 14 != 0 && !realObstacle.contains(movingList.get(0) - 14) && (movingList.get(0) - 14) != temporary) {
                    moveObjectUp(movingList.get(0));
                    movingList.add(movingList.get(0) - 14);
                    movingList.remove(0);
                } else if (movingList.get(0) % 14 != 0 && !realObstacle.contains(movingList.get(0) - 1) && (movingList.get(0) - 1) != temporary) {
                    moveObjectToLeft(movingList.get(0));
                    movingList.add(movingList.get(0) - 1);
                    movingList.remove(0);
                } else if (movingList.get(0) / 14 != 13 && !realObstacle.contains(movingList.get(0) + 14) && (movingList.get(0) + 14) != temporary) {
                    moveObjectDown(movingList.get(0));
                    movingList.add(movingList.get(0) + 14);
                    movingList.remove(0);
                }
            } else if (ran < 0.5) {
                if (movingList.get(0) / 14 != 0 && !realObstacle.contains(movingList.get(0) - 14) && (movingList.get(0) - 14) != temporary) {
                    moveObjectUp(movingList.get(0));
                    movingList.add(movingList.get(0) - 14);
                    movingList.remove(0);
                } else if (movingList.get(0) % 14 != 0 && !realObstacle.contains(movingList.get(0) - 1) && (movingList.get(0) - 1) != temporary) {
                    moveObjectToLeft(movingList.get(0));
                    movingList.add(movingList.get(0) - 1);
                    movingList.remove(0);
                } else if (movingList.get(0) / 14 != 13 && !realObstacle.contains(movingList.get(0) + 14) && (movingList.get(0) + 14) != temporary) {
                    moveObjectDown(movingList.get(0));
                    movingList.add(movingList.get(0) + 14);
                    movingList.remove(0);
                } else if (movingList.get(0) % 14 != 13 && !realObstacle.contains(movingList.get(0) + 1) && ((movingList.get(0) + 1) != temporary)) {
                    moveObjectToRight(movingList.get(0));
                    movingList.add(movingList.get(0) + 1);
                    movingList.remove(0);
                }
            } else if (ran < 0.75) {
                if (movingList.get(0) % 14 != 0 && !realObstacle.contains(movingList.get(0) - 1) && (movingList.get(0) - 1) != temporary) {
                    moveObjectToLeft(movingList.get(0));
                    movingList.add(movingList.get(0) - 1);
                    movingList.remove(0);
                } else if (movingList.get(0) / 14 != 13 && !realObstacle.contains(movingList.get(0) + 14) && (movingList.get(0) + 14) != temporary) {
                    moveObjectDown(movingList.get(0));
                    movingList.add(movingList.get(0) + 14);
                    movingList.remove(0);
                } else if (movingList.get(0) % 14 != 13 && !realObstacle.contains(movingList.get(0) + 1) && ((movingList.get(0) + 1) != temporary)) {
                    moveObjectToRight(movingList.get(0));
                    movingList.add(movingList.get(0) + 1);
                    movingList.remove(0);
                } else if (movingList.get(0) / 14 != 0 && !realObstacle.contains(movingList.get(0) - 14) && (movingList.get(0) - 14) != temporary) {
                    moveObjectUp(movingList.get(0));
                    movingList.add(movingList.get(0) - 14);
                    movingList.remove(0);
                }
            } else {
                if (movingList.get(0) / 14 != 13 && !realObstacle.contains(movingList.get(0) + 14) && (movingList.get(0) + 14) != temporary) {
                    moveObjectDown(movingList.get(0));
                    movingList.add(movingList.get(0) + 14);
                    movingList.remove(0);
                } else if (movingList.get(0) % 14 != 13 && !realObstacle.contains(movingList.get(0) + 1) && ((movingList.get(0) + 1) != temporary)) {
                    moveObjectToRight(movingList.get(0));
                    movingList.add(movingList.get(0) + 1);
                    movingList.remove(0);
                } else if (movingList.get(0) / 14 != 0 && !realObstacle.contains(movingList.get(0) - 14) && (movingList.get(0) - 14) != temporary) {
                    moveObjectUp(movingList.get(0));
                    movingList.add(movingList.get(0) - 14);
                    movingList.remove(0);
                } else if (movingList.get(0) % 14 != 0 && !realObstacle.contains(movingList.get(0) - 1) && (movingList.get(0) - 1) != temporary) {
                    moveObjectToLeft(movingList.get(0));
                    movingList.add(movingList.get(0) - 1);
                    movingList.remove(0);
                }
            }
            count++;
        }
    }

    public ArrayList<Integer> findWayToRechargePoint(int index) {
        if (index == rechargePoint) {
            ArrayList<Integer> result = new ArrayList<Integer>();
            return result;
        }
        ArrayList<Integer> open = new ArrayList<Integer>();
        ArrayList<Integer> close = new ArrayList<Integer>();
        HashMap<Integer, Integer> gscore = new HashMap<Integer, Integer>();
        HashMap<Integer, Double> fscore = new HashMap<Integer, Double>();
        HashMap<Integer, Integer> cameFrom = new HashMap<Integer, Integer>();
        open.add(index);
        gscore.put(index, 0);
        fscore.put(index, distanceToRechargePoint(index));

        while (!open.isEmpty()) {

            //find the element in open with lowest fscore : current
            int current = open.get(0);
            for (int openElement : open) {
                if (fscore.get(openElement) < fscore.get(current)) {
                    current = openElement;
                }
            }

            //next steps
            if (current == rechargePoint) {
                return BoardUtil.reconstructPath(cameFrom, current, index);
            }
            open.remove((Integer) current);
            close.add(current);
            ArrayList<Integer> neighbor = findNeighbor(current);
            for (int neighborElement : neighbor) {
                if (close.contains(neighborElement)) {
                    continue;
                }
                int comingGscore = gscore.get(current) + 1;
                if (!open.contains(neighborElement)) {
                    open.add(neighborElement);
                } else if (comingGscore >= gscore.get(neighborElement)) {
                    continue;
                }

                //update
                gscore.put(neighborElement, comingGscore);
                fscore.put(neighborElement, (double) gscore.get(neighborElement) + distanceToRechargePoint(neighborElement));
                cameFrom.put(neighborElement, current);
            }

        }
        return null;
    }
    
    
    public ArrayList<Integer> findWay2Points(int start, int end){
        if (start == end) {
            ArrayList<Integer> result = new ArrayList<Integer>();
            return result;
        }
        ArrayList<Integer> open = new ArrayList<Integer>();
        ArrayList<Integer> close = new ArrayList<Integer>();
        HashMap<Integer, Integer> gscore = new HashMap<Integer, Integer>();
        HashMap<Integer, Double> fscore = new HashMap<Integer, Double>();
        HashMap<Integer, Integer> cameFrom = new HashMap<Integer, Integer>();
        open.add(start);
        gscore.put(start, 0);
        fscore.put(start, distanceBetween2Points(start,end));

        while (!open.isEmpty()) {

            //find the element in open with lowest fscore : current
            int current = open.get(0);
            for (int openElement : open) {
                if (fscore.get(openElement) < fscore.get(current)) {
                    current = openElement;
                }
            }

            //next steps
            if (current == end) {
                return BoardUtil.reconstructPath(cameFrom, current, start);
            }
            open.remove((Integer) current);
            close.add(current);
            ArrayList<Integer> neighbor = findNeighbor(current);
            for (int neighborElement : neighbor) {
                if (close.contains(neighborElement)) {
                    continue;
                }
                int comingGscore = gscore.get(current) + 1;
                if (!open.contains(neighborElement)) {
                    open.add(neighborElement);
                } else if (comingGscore >= gscore.get(neighborElement)) {
                    continue;
                }

                //update
                gscore.put(neighborElement, comingGscore);
                fscore.put(neighborElement, (double) gscore.get(neighborElement) + distanceToRechargePoint(neighborElement));
                cameFrom.put(neighborElement, current);
            }

        }
        return new ArrayList<Integer>();
    }

    public ArrayList<Integer> findNeighbor(int index) {
        ArrayList<Integer> neighbor = new ArrayList<Integer>();
        if (index % 14 != 0 && index % 14 != 13 && index > 13 && index < 182) {
            if (!realObstacle.contains(index - 1)) {
                neighbor.add(index - 1);
            }
            if (!realObstacle.contains(index + 1)) {
                neighbor.add(index + 1);
            }
            if (!realObstacle.contains(index - 14)) {
                neighbor.add(index - 14);
            }
            if (!realObstacle.contains(index + 14)) {
                neighbor.add(index + 14);
            }
        }
        if (index > 0 && index < 13) {
            if (!realObstacle.contains(index - 1)) {
                neighbor.add(index - 1);
            }
            if (!realObstacle.contains(index + 1)) {
                neighbor.add(index + 1);
            }
            if (!realObstacle.contains(index + 14)) {
                neighbor.add(index + 14);
            }
        }
        if (index > 182 && index < 195) {
            if (!realObstacle.contains(index - 1)) {
                neighbor.add(index - 1);
            }
            if (!realObstacle.contains(index + 1)) {
                neighbor.add(index + 1);
            }
            if (!realObstacle.contains(index - 14)) {
                neighbor.add(index - 14);
            }
        }
        if (index != 0 && index != 182 && index % 14 == 0) {
            if (!realObstacle.contains(index + 14)) {
                neighbor.add(index + 14);
            }
            if (!realObstacle.contains(index + 1)) {
                neighbor.add(index + 1);
            }
            if (!realObstacle.contains(index - 14)) {
                neighbor.add(index - 14);
            }
        }
        if (index != 13 && index != 195 && index % 14 == 13) {
            if (!realObstacle.contains(index + 14)) {
                neighbor.add(index + 14);
            }
            if (!realObstacle.contains(index - 1)) {
                neighbor.add(index - 1);
            }
            if (!realObstacle.contains(index - 14)) {
                neighbor.add(index - 14);
            }
        }
        if (index == 0) {
            if (!realObstacle.contains(index + 14)) {
                neighbor.add(index + 14);
            }
            if (!realObstacle.contains(index + 1)) {
                neighbor.add(index + 1);
            }
        }
        if (index == 13) {
            if (!realObstacle.contains(index + 14)) {
                neighbor.add(index + 14);
            }
            if (!realObstacle.contains(index - 1)) {
                neighbor.add(index - 1);
            }
        }
        if (index == 182) {
            if (!realObstacle.contains(index - 14)) {
                neighbor.add(index - 14);
            }
            if (!realObstacle.contains(index + 1)) {
                neighbor.add(index + 1);
            }
        }
        if (index == 195) {
            if (!realObstacle.contains(index - 14)) {
                neighbor.add(index - 14);
            }
            if (!realObstacle.contains(index - 1)) {
                neighbor.add(index - 1);
            }
        }
        return neighbor;
    }

    public double distanceToRechargePoint(int index) {
        int columnRechargePoint = rechargePoint % 14;
        int rowRechargePoint = rechargePoint / 14;
        int columnIndex = index % 14;
        int rowIndex = index / 14;
        int square = (columnRechargePoint - columnIndex) * (columnRechargePoint - columnIndex) + (rowRechargePoint - rowIndex) * (rowRechargePoint - rowIndex);
        double result = Math.sqrt(square);

        return result;
    }
    
    public double distanceBetween2Points(int start, int end){
        int columnEnd = end % 14;
        int rowEnd = end / 14;
        int columnStart = start % 14;
        int rowStart = start / 14;
        int square = (columnEnd - columnStart) * (columnEnd - columnStart) + (rowEnd - rowStart) * (rowEnd - rowStart);
        double result = Math.sqrt(square);

        return result;
    }
    

    public void goBackToRechargePoint(int index) {
        ////System.out.println("start to go back from " + index);
        ArrayList<Integer> way = findWayToRechargePoint(index);
        int size = way.size();
        way.add(index);
        Collections.reverse(way);
//        for (int i : way) {
//            ////System.out.println(i);
//        }
        ////System.out.println("XDD");

        int local[] = {0};
        Timer timer = new Timer(150, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ////System.out.println("iteration");
                moveObject();

                if (movingList.contains(way.get(local[0] + 1)) || movingList.contains(way.get(local[0] + 1)+1) || movingList.contains(way.get(local[0] + 1)-1)
                        || movingList.contains(way.get(local[0] + 1)+14) || movingList.contains(way.get(local[0] + 1)-14)) {

                } else {
                    if (local[0] == way.size() - 2) {
                        ////System.out.println("stop");
                        power.getTextField().setText(((Integer) fullPower).toString());
                        power.hideWarning();
                        robotPower = fullPower;
                        ////System.out.println(((Integer) fullPower).toString());
                        ((Timer) e.getSource()).stop();
                        ArrayList<Integer> insideWay = findWayToRechargePoint(index);
                        int size = insideWay.size();
                        insideWay.add(index);
                        int insideLocal[] = {0};
                        Timer timer2 = new Timer(150, new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                ////System.out.println("iteration1");
                                moveObject();

                                if (movingList.contains(insideWay.get(insideLocal[0] + 1)) || movingList.contains(insideWay.get(insideLocal[0] + 1)+1) || movingList.contains(insideWay.get(insideLocal[0] + 1)-1)
                                    || movingList.contains(insideWay.get(insideLocal[0] + 1)+14) || movingList.contains(insideWay.get(insideLocal[0] + 1)-14)) {

                                } else {
                                    if (insideLocal[0] == insideWay.size() - 2) {
                                        ////System.out.println("stop1");
                                        ((Timer) e.getSource()).stop();
                                        runRobotWithPower();

                                    }

                                    tileList.get(insideWay.get(insideLocal[0])).removeAll();
                                    if (dirtyTile.contains(insideWay.get(insideLocal[0])) && (insideWay.get(insideLocal[0])!=rechargePoint)) {
                                        try {
                                            BufferedImage bi = ImageIO.read(new File("src/images/XP1.png"));
                                            JLabel image = new JLabel(new ImageIcon(bi));
                                            tileList.get(insideWay.get(insideLocal[0])).add(image, BorderLayout.CENTER);
                                            tileList.get(insideWay.get(insideLocal[0])).revalidate();
                                            tileList.get(insideWay.get(insideLocal[0])).repaint();
                                            temporary = cycle.get(insideLocal[0]);

                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                        }

//                                        dirtyTile.remove(insideWay.get(insideLocal[0]));
                                    }
                                    tileList.get(insideWay.get(insideLocal[0])).repaint();
                                    tileList.get(insideWay.get(insideLocal[0])).revalidate();
                                    tileList.get(insideWay.get(insideLocal[0] + 1)).removeAll();
//                                    if (dirtyTile.contains(insideWay.get(1 + insideLocal[0]))) {
//                                        dirtyTile.remove(insideWay.get(insideLocal[0] + 1));
//                                    }
                                    try {
                                        BufferedImage bi = ImageIO.read(new File("src/images/batdau.png"));
                                        JLabel image = new JLabel(new ImageIcon(bi));
                                        tileList.get(insideWay.get(insideLocal[0] + 1)).add(image, BorderLayout.CENTER);
                                        tileList.get(insideWay.get(insideLocal[0] + 1)).revalidate();
                                        tileList.get(insideWay.get(insideLocal[0] + 1)).repaint();
                                        temporary = cycle.get(local[0] + 1);

                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }
                                    robotPower--;
                                    power.getTextField().setText(((Integer) robotPower).toString());
                                    insideLocal[0]++;
                                }
                            }
                        });
                        timer2.start();
                    }

                    tileList.get(way.get(local[0])).removeAll();
                    if (dirtyTile.contains(way.get(local[0])) &&(way.get(local[0]) != rechargePoint)) {
                        try {
                            BufferedImage bi = ImageIO.read(new File("src/images/XP1.png"));
                            JLabel image = new JLabel(new ImageIcon(bi));
                            tileList.get(way.get(local[0])).add(image, BorderLayout.CENTER);
                            tileList.get(way.get(local[0])).revalidate();
                            tileList.get(way.get(local[0])).repaint();
                            temporary = cycle.get(local[0]);

                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                    tileList.get(way.get(local[0])).repaint();
                    tileList.get(way.get(local[0])).revalidate();
                    tileList.get(way.get(local[0] + 1)).removeAll();
//                    if (dirtyTile.contains(way.get(1 + local[0]))) {
//                        dirtyTile.remove(way.get(local[0] + 1));
//                    }
                    try {
                        BufferedImage bi = ImageIO.read(new File("src/images/batdau.png"));
                        JLabel image = new JLabel(new ImageIcon(bi));
                        tileList.get(way.get(local[0] + 1)).add(image, BorderLayout.CENTER);
                        tileList.get(way.get(local[0] + 1)).revalidate();
                        tileList.get(way.get(local[0] + 1)).repaint();
                        temporary = cycle.get(local[0] + 1);

                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    robotPower--;
                    power.getTextField().setText(((Integer) robotPower).toString());
                    local[0]++;
                }
            }
        });
        timer.start();
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
