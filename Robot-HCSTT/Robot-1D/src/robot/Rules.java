/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robot;

import java.util.ArrayList;

/**
 *
 * @author Mr-Tuy
 */
public class Rules {
    private ArrayList<Integer> condition;
    private ArrayList<Integer> action;
    public Rules(ArrayList<Integer> con, ArrayList<Integer> act){
        this.condition = con;
        this.action = act;
    }
    public ArrayList<Integer> getCondition(){
        return this.condition;
    }
    public ArrayList<Integer> getAction(){
        return this.action;
    }
}
