/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 *
 * @author Mr-Tuy
 */
public class BoardUtil {
    public static boolean anythingAbove(ArrayList<Integer> array, int point){
        if(array.size() == 1){
            return false;
        }
        if(array.get(0)==point && array.get(1) == (point-28)){
//            //System.out.println("above true");
            return true;
        }
        if(array.get(array.size()-1)==point && array.get(array.size()-2) == (point-28)){
//            //System.out.println("above true");
            return true;
        }
        int index = 1;
        while(index < array.size()-1){
//            //System.out.println("here ?");
            if(array.get(index)==point && (array.get(index+1) == (point-28) || array.get(index-1) == (point-28))){
//                //System.out.println("above true");
                return true;
            }
            index++;
        }
//        //System.out.println("above false");
        return false;
    }
    
    
    public static boolean anythingBeneath(ArrayList<Integer> array, int point){
        if(array.size() == 1){
            return false;
        }
        if(array.get(0)==point && array.get(1) == (point+28)){
//            //System.out.println("beneath true");
            return true;
        }
        if(array.get(array.size()-1)==point && array.get(array.size()-2) == (point+28)){
//            //System.out.println("beneath true");
            return true;
        }
        int index = 1;
        while(index < array.size()-1){
            if((array.get(index)==point) && ((array.get(index+1) == (point+28)) || (array.get(index-1) == (point+28)))){
//                //System.out.println("beneath true");
                return true;
            }
            index++;
        }
//        //System.out.println("beneath false");
        return false;
    }
    
    public static boolean anythingLeft(ArrayList<Integer> array, int point){
        if(array.size() == 1){
            return false;
        }
        if(array.get(0)==point && array.get(1) == (point-2)){
//            //System.out.println("left true");
            return true;
        }if(array.get(array.size()-1)==point && array.get(array.size()-2) == (point-2)){
//            //System.out.println("left true");
            return true;
        }
        int index = 1;
        while(index < array.size()-1){
            if(array.get(index)==point && (array.get(index+1) == (point-2) || array.get(index-1) == (point-2))){
//                //System.out.println("left true");
                return true;
            }
            index++;
        }
//        //System.out.println("left false");
        return false;
    }
    
    public static boolean anythingRight(ArrayList<Integer> array, int point){
        if(array.size() == 1){
            return false;
        }
        if(array.get(0)==point && array.get(1) == (point+2)){
//            //System.out.println("right true");
            return true;
        }
        if(array.get(array.size()-1)==point && array.get(array.size()-2) == (point+2)){
//            //System.out.println("right true");
            return true;
        }
        int index = 1;
        while(index < array.size()-1){
            if(array.get(index)==point && (array.get(index+1) == (point+2) || array.get(index-1) == (point+2))){
//                //System.out.println("right true");
                return true;
            }
            index++;
        }
//        //System.out.println("right false");
        return false;
    }
    
    public static boolean isTurnAround(ArrayList<Integer> array, int point){
        int index = 1;
        while(index < array.size()-1){
            if(array.get(index) == point && array.get(index-1) == array.get(index+1)){
                return true;
            }
            index++;
        }
        return false;
    }
    
    public static boolean isTurnAroundFromTop(ArrayList<Integer> array, int point){
        int index = 1;
        while(index < array.size()-1){
            if(array.get(index) == point && array.get(index-1) == array.get(index+1) && array.get(index) == (array.get(index-1)+28)){
                return true;
            }
            index++;
        }
        return false;
    }
    
    public static boolean isTurnAroundFromLeft(ArrayList<Integer> array, int point){
        int index = 1;
        while(index < array.size()-1){
            if(array.get(index) == point && array.get(index-1) == array.get(index+1) && array.get(index) == (array.get(index-1)+2)){
                return true;
            }
            index++;
        }
        return false;
    }
    
    public static boolean isTurnAroundFromRight(ArrayList<Integer> array, int point){
        int index = 1;
        while(index < array.size()-1){
            if(array.get(index) == point && array.get(index-1) == array.get(index+1) && array.get(index) == (array.get(index-1)-2)){
                return true;
            }
            index++;
        }
        return false;
    }
    
    public static boolean isTurnAroundFromBottom(ArrayList<Integer> array, int point){
        int index = 1;
        while(index < array.size()-1){
            if(array.get(index) == point && array.get(index-1) == array.get(index+1) && array.get(index) == (array.get(index-1)-28)){
                return true;
            }
            index++;
        }
        return false;
    }
    
    public static ArrayList<Integer> reconstructPath(HashMap<Integer,Integer> cameFrom,int current,int start){
        ArrayList<Integer> result = new ArrayList<Integer>();
        result.add(current);
        int next = cameFrom.get(current);
        while(next!=start){
            result.add(next);
            next = cameFrom.get(next);
        }
        return result;
    }
    
    public static ArrayList<Integer> findWayThroughUntouchable(ArrayList<Integer> theUntouchable, int start,BoardPanel board){
        ArrayList<Integer> result = new ArrayList<Integer>();
        result.add(start);
        theUntouchable.remove((Integer)start);
        int current = start;
        int last = start - 1;
//        while(true){
        while((result.get(result.size()-1) != start) || (result.size() == 1)){
            if(last == current-1){
                System.out.println(current + "from -1");
                if(theUntouchable.contains(current+14)){
                    System.out.println("go +14");
                    theUntouchable.remove((Integer)(current+14));
                    last = current;
                    current+=14; 
                    result.add(current);
                }else if(theUntouchable.contains(current+1) && current%14 !=13){
                    
                    System.out.println("go +1");
                    theUntouchable.remove((Integer)(current+1));
                    last = current;
                    current++;
                    result.add(current);
                }else if (theUntouchable.contains(current-14)){
                    System.out.println("go -14");
                    theUntouchable.remove((Integer)(current-14));
                    last = current;
                    current-=14;
                    result.add(current);
                }else{
                    System.out.println("go 0");
                    int size = result.size() - 2;
                    if(size<0){
                        return result;
                    }else{
                        while(!checkSurroundObstacle(theUntouchable,result.get(size)) && size!=0){
                            size--;
                        }
                        ArrayList<Integer> way = board.findWay2Points(result.get(size),result.get(result.size()-1));
                        result.addAll(way);
                        result.add(result.get(size));
                        current = result.get(size);
                        last = result.get(size+1);
                    }
                }
            }else if(last == current+1){
                System.out.println(current + "from +1");
                if(theUntouchable.contains(current-14)){
                    System.out.println("go -14");
                    theUntouchable.remove((Integer)(current-14));
                    last = current;
                    current-=14; 
                    result.add(current);
                }else if(theUntouchable.contains(current-1) && current%14 != 0){
                    System.out.println("go -1");
                    theUntouchable.remove((Integer)(current-1));
                    last = current;
                    current--;
                    result.add(current);
                }else if (theUntouchable.contains(current+14)){
                    System.out.println("go +14");
                    theUntouchable.remove((Integer)(current+14));
                    last = current;
                    current+=14;
                    result.add(current);
                }else{
                    
                    System.out.println("go 0");
                    int size = result.size() - 2;
                    if(size<0){
                        return result;
                    }else{
                        while(!checkSurroundObstacle(theUntouchable,result.get(size)) && size!=0){
                            
                            size--;
                        }
                        ArrayList<Integer> way = board.findWay2Points(result.get(size),result.get(result.size()-1));
                        result.addAll(way);
                        result.add(result.get(size));
                        current = result.get(size);
                        last = result.get(size+1);
                    }
                }
            }else if(last == current+14){
                System.out.println(current + "from +14");
                if(theUntouchable.contains(current+1) && current %14 !=13){
                    System.out.println("go +1");
                    theUntouchable.remove((Integer)(current+1));
                    last = current;
                    current++; 
                    result.add(current);
                }else if(theUntouchable.contains(current-14)){
                    System.out.println("go -14");
                    theUntouchable.remove((Integer)(current-14));
                    last = current;
                    current-=14;
                    result.add(current);
                }else if (theUntouchable.contains(current-1) && current%14 != 0){
                    System.out.println("go -1");
                    theUntouchable.remove((Integer)(current-1));
                    last = current;
                    current--;
                    result.add(current);
                }else{
                    System.out.println("go 0");
                    int size = result.size() - 2;
                    if(size<0){
                        return result;
                    }else{
                        while(!checkSurroundObstacle(theUntouchable,result.get(size)) && size!=0){
                            
                            size--;
                        }
                        ArrayList<Integer> way = board.findWay2Points(result.get(size),result.get(result.size()-1));
                        result.addAll(way);
                        result.add(result.get(size));
                        current = result.get(size);
                        last = result.get(size+1);
                    }
                }
            }else if(last == current-14){
                System.out.println(current + "from -14");
                if(theUntouchable.contains(current-1) && current%14 != 0){
                    System.out.println("go -1");
                    theUntouchable.remove((Integer)(current-1));
                    last = current;
                    current--; 
                    result.add(current);
                }else if(theUntouchable.contains(current+14)){
                    System.out.println("go +14");
                    theUntouchable.remove((Integer)(current+14));
                    last = current;
                    current+=14;
                    result.add(current);
                }else if (theUntouchable.contains(current+1) && current%14 != 13){
                    System.out.println("go +1");
                    theUntouchable.remove((Integer)(current+1));
                    last = current;
                    current++;
                    result.add(current);
                }else{
                    System.out.println("go 0");
                    int size = result.size() - 2;
                    if(size<0){
                        return result;
                    }else{
                        while(!checkSurroundObstacle(theUntouchable,result.get(size)) && size!=0){
                            size--;
                        }
                        ArrayList<Integer> way = board.findWay2Points(result.get(size),result.get(result.size()-1));
                        result.addAll(way);
                        result.add(result.get(size));
                        current = result.get(size);
                        last = result.get(size+1);
                    }
                }
            }
        }
        
        return result;
    }
    
    public static ArrayList<Integer> getGlobalNeighbor(int index){
        ArrayList<Integer> result = new ArrayList<Integer>();
        if(index == 0){
            result.add(1);
            result.add(14);
        }else if (index == 13){
            result.add(12);
            result.add(27);
        }else if (index == 182){
            result.add(168);
            result.add(183);
        }else if (index == 195){
            result.add(194);
            result.add(181);
        }else if (index >0 && index < 13){
            result.add(index+1);
            result.add(index-1);
            result.add(index+14);
        }else if(index >182 && index <195){
            result.add(index+1);
            result.add(index-1);
            result.add(index-14);
        }else if (index!= 0 && index!= 182 && index%14 ==0){
            result.add(index+1);
            result.add(index-14);
            result.add(index+14);
        }else if(index!=13 && index != 195 && index%14 ==13){
            result.add(index-1);
            result.add(index-14);
            result.add(index+14);
        }else {
            result.add(index+1);
            result.add(index-1);
            result.add(index+14);
            result.add(index-14);
        }
        return result;
    }
    
    
    //return true if 1 obstacle around
    public static boolean checkSurroundObstacle(ArrayList<Integer> input, int index){
        for (int i: getGlobalNeighbor(index)){
            if(input.contains(i)){
                return true;
            }
        }
        return false;
    }

}
