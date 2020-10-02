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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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
    static int COUNT = 30000;
    static int THREADCOUNT = 6;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException,InterruptedException, TimeoutException {

        //RuntimeCPUUsage rc = new RuntimeCPUUsage();
        //rc.printDetails();

        //-------------------------------------------------------------------------------------------------------
        //COUNTING PATTERNS
        
        //startPCisThreaded(false);

        //-------------------------------------------------------------------------------------------------------
        //SORTING DATA
        //startMSisThreaded(true);
       
        //-------------------------------------------------------------------------------------------------------
        //SEARCHING DATA
        //String goal = "WW";
        //String goal = "QSrEHrS";
        //startBSisThreaded(true,goal);
        //-------------------------------------------------------------------------------------------------------
        //MQ
        ZMQTaskSink sink = new ZMQTaskSink();
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
        
        //long totalTotal = 0;
        //long totalFree =0;
        double peakPercent = 0;
        int highIndex = 0;
        //int deathCount = 0;
        for(int i = 0;i<RCUs.size();i++){
            try{
                if (RCUs.get(i).getUsePercent()>peakPercent){
                    peakPercent = RCUs.get(i).getUsePercent();
                    highIndex = i;
                }
            //totalTotal += RCUs.get(i).total;
            //totalFree+= RCUs.get(i).free;
            }catch(Exception Ex){
                //System.out.println("DEATH at "+i);
                //deathCount++;
            }
        }
        //System.out.println("RCUs Full Size: "+ RCUs.size());
        //int acceptedSize = (RCUs.size()-deathCount);
        //System.out.println("RCUs Accepted: "+ acceptedSize);
        //return new RuntimeCPUUsage(totalTotal/acceptedSize,totalFree/acceptedSize);
        return RCUs.get(highIndex);
    }
    static public ArrayList <Sheet> PCPat() throws IOException{
        ExcelReader ex = new ExcelReader("data.xlsx");
        return ex.ReadData();
    }
    static public void startPCisThreaded(Boolean isThread) throws IOException, InterruptedException{
        ArrayList <Sheet> pat = PCPat();
        TimeUnit.SECONDS.sleep(10);
        if (isThread){
            PCT(pat);
        }else{
            PC(pat);
        }
    }
    static public void PC(ArrayList <Sheet> pat) throws IOException{
        System.out.println("Starting Serial Pattern Counter");
        long startTimeParallel = System.currentTimeMillis();
        NoThreadPatternCounter pc = new NoThreadPatternCounter();
        pc.manualCount(pat);
        long endTimeParallel = System.currentTimeMillis();
        PrintPatterns(pc.pats);
        //PATTERN TIME CHECK
        long timeParallel = endTimeParallel-startTimeParallel;
        System.out.println("Time in milliseconds:" + timeParallel);
        //ExcelWriter.WritePatternToExel(pc.pats);
        //getAverageRuntime(pc.RCUs).printDetails();
    }
    static public void PCT(ArrayList <Sheet> pat) throws IOException, InterruptedException{
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
        System.out.println("Starting Parallel Pattern Counter");
        long startTimeParallel = System.currentTimeMillis();
        for (int i = 0;i<THREADCOUNT;i++){
            TA.get(i).start();
            TA.get(i).join();
            
        }
        long endTimeParallel = System.currentTimeMillis();
        PrintPatterns(ThreadedPatternCounter.pats);
        //PATTERN TIME CHECK
        long timeParallel = endTimeParallel-startTimeParallel;
        System.out.println("Time in milliseconds:" + timeParallel);
        //ExcelWriter.WritePatternToExel(ThreadedPatternCounter.pats);
        //getAverageRuntime(ThreadedPatternCounter.RCUs).printDetails();
    }
    static public void startMSisThreaded(Boolean isThread) throws IOException, InterruptedException{
        ArrayList <Pattern> pat = MSPat();
        TimeUnit.SECONDS.sleep(10);
        if (isThread){
            MST(pat);
        }else{
            MS(pat);
        }
    }
    static public ArrayList <Pattern> MSPat() throws IOException{
        ExcelReader ex = new ExcelReader("patterns.xlsx");
        return ex.ReadPatterns();
    } 
    static public void MS(ArrayList <Pattern> pat ) throws IOException{
                //MERGESORT SERIAL

        System.out.println("Starting Serial Merge Sort");
        PatternMergeSort PMS = new PatternMergeSort(pat);
        long startTimeMS = System.currentTimeMillis();
        PMS.mergeSort(0, pat.size()-1);
        long endTimeMS = System.currentTimeMillis();
        ArrayList <Pattern> ans = PMS.getAns();
        /*
        int sum = 0;
        for (int i =0; i<ans.size();i++){
            System.out.println(ans.get(i).pattern+": "+ans.get(i).ctr);
            sum+=ans.get(i).ctr;
        }
        System.out.println(sum);
        System.out.println(ans.size()); 
        */
        long timeMS = endTimeMS-startTimeMS;
        System.out.println("Time in milliseconds:" + timeMS);
        //getAverageRuntime(PMS.RCUs).printDetails();
    }
    static public void MST(ArrayList <Pattern> pat ) throws IOException{
        System.out.println("Starting Parallel Merge Sort");
        PatternParrallelMergeSort PPMS = new PatternParrallelMergeSort(pat);
        long startTimePMS = System.currentTimeMillis();
        PPMS.parallelmergeSort(0, pat.size()-1, 6);  
        long endTimePMS = System.currentTimeMillis();
        ArrayList <Pattern> ans = PPMS.getAns();
        /*
        //Printed Check for the patterns if arranged
        int sum = 0;
        for (int i =0; i<ans.size();i++){
            System.out.println(ans.get(i).pattern+": "+ans.get(i).ctr);
            sum+=ans.get(i).ctr;
        }
        System.out.println(sum);
        System.out.println(ans.size());
        */
        long timePMS = endTimePMS-startTimePMS;
        System.out.println("Time in milliseconds:" + timePMS);
        //getAverageRuntime(PPMS.RCUs).printDetails();
        //ExcelWriter.WritePatternToExel(ans,"mergepatterns.xlsx");
    }
    static public void startBSisThreaded(Boolean isThread,String goal) throws IOException, InterruptedException{
        ArrayList <Pattern> pat = BSPat();
        TimeUnit.SECONDS.sleep(10);
        if (isThread){
            BST(pat,goal);
        }else{
            BS(pat,goal);
        }
    }
    static public ArrayList <Pattern> BSPat() throws IOException{
        ExcelReader ex = new ExcelReader("mergepatterns.xlsx");
        return  ex.ReadPatterns();
    } 
    static public void BS(ArrayList <Pattern> pat,String goal) throws IOException{

        BinarySearchPattern BSP = new BinarySearchPattern(pat,goal);
        System.out.println("Starting Binary Search");
        //long startTimeBSP= System.currentTimeMillis();
        long startTimeBSPNano= System.nanoTime();
        int x = BSP.startSearching();
        //long endTimeBSP = System.currentTimeMillis();
        long endTimeBSPNano= System.nanoTime();
        //long timeBSP = endTimeBSP-startTimeBSP;
        //System.out.println("Time in milliseconds:" + timeBSP);
        long timeBSPNano = endTimeBSPNano-startTimeBSPNano;
        System.out.println("Time in Nanoseconds:" + timeBSPNano);
        //getAverageRuntime(BSP.RCUs).printDetails();
        
        /*
        //Answer
        if (x!=-1){
            System.out.println(goal+" is at index "+x+" with a count of "+ans.get(x).ctr);
        }else{
            System.out.println("Error");
        }
        */
    }
    static public void BST(ArrayList <Pattern> ans,String goal) throws IOException, InterruptedException{
        BinarySearchPatternThread BSPT = new BinarySearchPatternThread(ans,THREADCOUNT,goal);
        //long startTimeBSPT = System.currentTimeMillis();
        System.out.println("Starting Parallel Binary Search");
        long startTimeBSPTNano= System.nanoTime();
        BSPT.startSearching();
        int x = BSPT.AnswerIndex;
        //long endTimeBSPT = System.currentTimeMillis();
        long endTimeBSPTNano= System.nanoTime();
        //long timeBSPT = endTimeBSPT-startTimeBSPT;
        long timeBSPTNano = endTimeBSPTNano-startTimeBSPTNano;
        //System.out.println("Time in milliseconds:" + timeBSPT);
        System.out.println("Time in Nanoseconds:" + timeBSPTNano);
        //getAverageRuntime(BSPT.RCUs).printDetails();
        
        /*
        //Answer
        if (x!=-1){
            System.out.println(goal+" is at index "+x+" with a count of "+ans.get(x).ctr);
        }else{
            System.out.println("Error");
        }
        */
    }
    
}
