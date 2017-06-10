/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mr-Tuy
 */
public class RuleUtil {
    private ArrayList<Rules> ruleList;
    public RuleUtil(){
        this.ruleList = getAllRules();
    }
    public ArrayList<Rules> getAllRules(){
        ArrayList<Rules> ruleList = new ArrayList<Rules>();
        try {
            for(String line : Files.readAllLines(Paths.get("src/rules/luat.txt"))){
                for(int i=0;i<line.length();++i){
                    if(line.charAt(i) == '>'){
                        ArrayList<Integer> condition = new ArrayList<Integer>();
                        ArrayList<Integer> action = new ArrayList<Integer>();
                        String[] con = (line.substring(0, i)).split(" ");
                        for(int y=0;y<con.length;++y){
                            condition.add(Integer.parseInt(con[y]));
                        }
                        String[] con2 = (line.substring(i+2, line.length())).split(" ");
                        for(int y=0;y<con2.length;++y){
                            action.add(Integer.parseInt(con2[y]));
                        }
                        ruleList.add(new Rules(condition,action));
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return ruleList;
    }
    public ArrayList<Rules> getRules(){
        return this.ruleList;
    }
    public ArrayList<Integer> getActionFromCondition(ArrayList<Integer> condition){
        for(int index = 0 ; index < ruleList.size();++index){
            ArrayList<Integer> ruleCondition = ruleList.get(index).getCondition();
            if(condition.containsAll(ruleCondition)){
                return ruleList.get(index).getAction();
            }
        }
        return null;
    }
}
