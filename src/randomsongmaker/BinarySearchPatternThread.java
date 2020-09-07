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
public class BinarySearchPatternThread {
    ArrayList <Pattern> pats;
    ArrayList <Thread> searchThreads = new ArrayList();
    int AnswerIndex = -1;
    public ArrayList <RuntimeCPUUsage> RCUs = new ArrayList();
    public BinarySearchPatternThread (ArrayList <Pattern> pats, int ThreadCount,String goal){
        this.pats = new ArrayList(pats);
        int partition = pats.size()/ThreadCount;
        
        for(int i =0;i<ThreadCount;i++){
            int l = partition*i;
            int r = partition*(i+1);
            
            
            if (r>=pats.size()){
                r = pats.size() - 1;
            }
            searchThreads.add(new Thread (BinarySearchThread(l,r,goal)));
        }
    }
    public void startSearching() throws InterruptedException{
        for (int i = 0;i< searchThreads.size();i++){
            searchThreads.get(i).start();
            searchThreads.get(i).join();
        }
    }
    private Thread BinarySearchThread(int l, int r, String goal){
        return new Thread () {
            public void run (){
                
                BinarySearch(l,r,goal);
            }
        };
    }
    public void BinarySearch(int l, int r, String goal){
        //RCUs.add(new RuntimeCPUUsage());
        if (r >= l) { 
            int mid = l + (r - l) / 2; 

            // If the element is present at the middle 
            // itself 
            if (pats.get(mid).pattern.equals(goal)) {
                AnswerIndex = mid;
                for (int i = 0;i< searchThreads.size();i++){
                    searchThreads.get(i).stop();
                }
            }
            // If element is smaller than mid, then 
            // it can only be present in left subarray 
            if (!CompareTools.isGreater(pats.get(mid).pattern, goal)) 
                BinarySearch(l, mid - 1, goal); 

            // Else the element can only be present 
            // in right subarray 
            BinarySearch(mid + 1, r, goal); 
        } 

        // We reach here when element is not 
        // present in array 
        
    }
}
