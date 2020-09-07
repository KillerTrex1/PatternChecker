/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package randomsongmaker;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mart Henrick Gamutan
 */
public class ThreadedPatternCounter extends Thread{
    
    public Semaphore sem; 
    public String threadName; 
    public int startRange;
    public int endRange;
    static public ArrayList <Sheet> sheets;
    static public ArrayList <Pattern> pats = new ArrayList();
    static public ArrayList <RuntimeCPUUsage> RCUs = new ArrayList();
    public ThreadedPatternCounter(Semaphore sem, String threadName, int startRange, int endRange)  
    { 
        super(threadName); 
        this.sem = sem; 
        this.threadName = threadName; 
        this.startRange = startRange;
        this.endRange = endRange;
        //System.out.println("SETUP");
    }
    public static void setSheets(ArrayList <Sheet> sheets){
        ThreadedPatternCounter.sheets = sheets;
    }
    @Override
    public void run(){
        
        try{
            for (int i = startRange;i<endRange;i++){
                //System.out.println("run");
                Sheet sheet = sheets.get(i);
                //System.out.println(threadName + " is waiting for a permit."); 
                for(int j = 0;j<sheet.Patterns.size();j++){
                    

                    sem.acquire();

                    checkPatterns(sheet.Patterns.get(j));
                    
                    sem.release();

                }
            
            }
        }catch (InterruptedException ex) {
                System.out.println(ex);
        }
        
    }
    
    
    
    
    public void checkPatterns(String p){
        //RCUs.add(new RuntimeCPUUsage());
        Boolean newP = true;
        for(int i = 0;i<pats.size();i++){
            if (pats.get(i).pattern.equals(p)){
                pats.get(i).ctr++;
                newP = false;
                break;
            }
        }
        if (newP){
            pats.add(new Pattern(p));
        }
    }
}
