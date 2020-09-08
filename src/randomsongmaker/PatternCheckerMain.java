/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package randomsongmaker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Mart Henrick Gamutan
 */
public class PatternCheckerMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException,InterruptedException {
        int COUNT = 30000;
        int THREADCOUNT = 6;
        int COUNTNOTE = 20000;
        //WRITE DATA
        
        //ExcelWriter.WriteSheetToExel(COUNT,COUNTNOTE);
        
        
        //-------------------------------------------------------------------------------------------------------
        //COUNTING PATTERNS
        
        
        /*
        //READ DATA 
        ExcelReader ex = new ExcelReader("data.xlsx");
        ArrayList <Sheet> pat = ex.ReadData();
        //long startTimeParallel = System.currentTimeMillis();
        */

        
        /*
        //NO THREADS PATTERN
        long startTimeParallel = System.currentTimeMillis();
        NoThreadPatternCounter pc = new NoThreadPatternCounter();
        pc.manualCount(pat);
        long endTimeParallel = System.currentTimeMillis();
        PrintPatterns(pc.pats);
        //ExcelWriter.WritePatternToExel(pc.pats);
        */
        
        
        
        /*
        //THREADS PATTERN
        Semaphore sem = new Semaphore(1);
        ArrayList <ThreadedPatternCounter> TA = new ArrayList();
        ThreadedPatternCounter.setSheets(pat);
        //System.out.println(pat.size());
        for (int i = 0;i<THREADCOUNT;i++){
            String name = "T"+i;
            int start = (COUNT/THREADCOUNT )*i;
            int end= (COUNT/THREADCOUNT )*(i+1);
            TA.add(new ThreadedPatternCounter(sem,name,start,end));
            //TA.get(i).start();
            //TA.get(i).join();
            
        }
        long startTimeParallel = System.currentTimeMillis();
        for (int i = 0;i<THREADCOUNT;i++){
            TA.get(i).start();
            TA.get(i).join();
            
        }
        long endTimeParallel = System.currentTimeMillis();
        PrintPatterns(ThreadedPatternCounter.pats);
        //ExcelWriter.WritePatternToExel(ThreadedPatternCounter.pats);
        */
       
        
        
        /*
        //PATTERN TIME CHECK
        long timeParallel = endTimeParallel-startTimeParallel;
        System.out.println("Time in milliseconds:" + timeParallel);
        */
        
        
        //-------------------------------------------------------------------------------------------------------
        //SORTING DATA
        
        //READ DATA PATTERNS
        ExcelReader ex = new ExcelReader("patterns.xlsx");
        ArrayList <Pattern> pat = ex.ReadPatterns();
        //System.out.println(pat.size());
        
        
        
       
        /*
        //MERGESORT PATTERNS
        PatternMergeSort PMS = new PatternMergeSort(pat);
        long startTimeMS = System.currentTimeMillis();
        PMS.mergeSort(0, pat.size()-1);
        long endTimeMS = System.currentTimeMillis();
        ArrayList <Pattern> ans = PMS.getAns();
        
        int sum = 0;
        for (int i =0; i<ans.size();i++){
            System.out.println(ans.get(i).pattern+": "+ans.get(i).ctr);
            sum+=ans.get(i).ctr;
        }
        System.out.println(sum);
        
        System.out.println(ans.size());
        long timeMS = endTimeMS-startTimeMS;
        System.out.println("Time in milliseconds:" + timeMS);
        */
        
        
        
        //MERGESORT PATTERNS PARRALLEL
        PatternParrallelMergeSort PPMS = new PatternParrallelMergeSort(pat);
        long startTimePMS = System.currentTimeMillis();
        PPMS.parallelmergeSort(0, pat.size()-1, 6);  
        long endTimePMS = System.currentTimeMillis();
        ArrayList <Pattern> ans = PPMS.getAns();
        /*
        int sum = 0;
        for (int i =0; i<ans.size();i++){
            System.out.println(ans.get(i).pattern+": "+ans.get(i).ctr);
            sum+=ans.get(i).ctr;
        }
        System.out.println(sum);
        */
        System.out.println(ans.size());
        long timePMS = endTimePMS-startTimePMS;
        System.out.println("Time in milliseconds:" + timePMS);
        //getAverageRuntime(PPMS.RCUs).printDetails();
        
        //-------------------------------------------------------------------------------------------------------
        //SEARCHING DATA
        
        //SETUP
        // ArrayList <Pattern> ans = 
        String goal = "QQQQr";
        
        
        //NO THREAD BINARY
        BinarySearchPattern BSP = new BinarySearchPattern(ans,goal);
        long startTimeBSP= System.currentTimeMillis();
        int x = BSP.startSearching();
        long endTimeBSP = System.currentTimeMillis();
        long timeBSP = endTimeBSP-startTimeBSP;
        System.out.println("Time in milliseconds:" + timeBSP);
        
        
        
        /*
        //THREAD BINARY
        BinarySearchPatternThread BSPT = new BinarySearchPatternThread(ans,THREADCOUNT,goal);
        long startTimeBSPT = System.currentTimeMillis();
        BSPT.startSearching();
        int x = BSPT.AnswerIndex;
        long endTimeBSPT = System.currentTimeMillis();
        long timeBSPT = endTimeBSPT-startTimeBSPT;
        System.out.println("Time in milliseconds:" + timeBSPT);
        */
        
        //Answer
        if (x!=-1){
            System.out.println(goal+" is at index "+x+" with a count of "+ans.get(x).ctr);
        }else{
            System.out.println("Error");
        }
    }
    public static void PrintPatterns(ArrayList <Pattern> pats){
        int sum = 0;
        for(int i = 0;i<pats.size();i++){
            //System.out.println(pats.get(i).pattern+"'s count = "+pats.get(i).ctr);
            sum+=pats.get(i).ctr;
        }
        System.out.println("TOTAL PATTERNS: "+pats.size());
        System.out.println("COUNT: "+sum);
    }
    public static Boolean checkAlive(ArrayList <ThreadedPatternCounter> TA){
        for (int i=0;i<TA.size();i++){
            if (TA.get(i).isAlive()){
                return true;
            }
        }
        return false;
    }
    public static RuntimeCPUUsage getAverageRuntime(ArrayList <RuntimeCPUUsage> RCUs){
        if (RCUs.size() == 0){
            System.out.println("No RCU added");
            return new RuntimeCPUUsage();
        }
        
        long totalTotal = 0;
        long totalFree =0;
        int deathCount = 0;
   
        for(int i = 0;i<RCUs.size();i++){
            try{
            totalTotal += RCUs.get(i).total;
            totalFree+= RCUs.get(i).free;
            }catch(Exception Ex){
                //System.out.println("DEATH at "+i);
                deathCount++;
            }
        }
        System.out.println("RCUs Full Size: "+ RCUs.size());
        int acceptedSize = (RCUs.size()-deathCount);
        System.out.println("RCUs Accepted: "+ acceptedSize);
        return new RuntimeCPUUsage(totalTotal/acceptedSize,totalFree/acceptedSize);
    }
}
