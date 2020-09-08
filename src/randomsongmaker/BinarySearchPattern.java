/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package randomsongmaker;

import java.util.ArrayList;

/**
 *
 * @author Mart Henrick Gamutan
 */
public class BinarySearchPattern {
    ArrayList <Pattern> pats;
    String goal;
    public ArrayList <RuntimeCPUUsage> RCUs = new ArrayList();
    
    public BinarySearchPattern(ArrayList <Pattern> pats,String goal){
        this.pats = new ArrayList(pats);
        this.goal = goal;
    }
    public int startSearching(){
        return BinarySearch(0,pats.size()-1);
    }
    public int BinarySearch(int l, int r){
        //RCUs.add(new RuntimeCPUUsage());
        if (r >= l) { 
            int mid = l + (r - l) / 2; 

            if (pats.get(mid).pattern.equals(goal)) {
                return mid; 
                
            } 

            if (!CompareTools.isGreater(pats.get(mid).pattern, goal)) 
                return BinarySearch(l, mid - 1); 

            return BinarySearch(mid + 1, r); 
        } 

        return -1; 
    }
}
