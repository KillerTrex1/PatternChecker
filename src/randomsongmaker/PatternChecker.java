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
public class PatternChecker {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException,InterruptedException {
        int COUNT = 30000;
        int THREADCOUNT = 8;
        int COUNTNOTE = 20000;
        //WRITE DATA
        
        //ExcelWriter.WriteSheetToExel(COUNT,COUNTNOTE);
        
        
        //READ DATA
        ExcelReader ex = new ExcelReader("data.xlsx");
        ArrayList <Sheet> pat = ex.ReadData();
        long startTimeParallel = System.currentTimeMillis();
        
        /*
        //NO THREADS
        NoThreadPatternCounter pc = new NoThreadPatternCounter();
        pc.manualCount(pat);
        long endTimeParallel = System.currentTimeMillis();
        PrintPatterns(pc.pats);
        ExcelWriter.WritePatternToExel(pc.pats);
        */
        
        //THREADS
        Semaphore sem = new Semaphore(1);
        ArrayList <ThreadedPatternCounter> TA = new ArrayList();
        ThreadedPatternCounter.setSheets(pat);
        for (int i = 0;i<8;i++){
            String name = "T"+i;
            int start = (COUNT/THREADCOUNT )*i;
            int end= (COUNT/THREADCOUNT )*(i+1);
            TA.add(new ThreadedPatternCounter(sem,name,start,end));
            TA.get(i).start();
            TA.get(i).join();
            
        }
        
        while(checkAlive(TA)){
            
        }
        long endTimeParallel = System.currentTimeMillis();
        PrintPatterns(ThreadedPatternCounter.pats);
        ExcelWriter.WritePatternToExel(ThreadedPatternCounter.pats);
        
        
        long timeParallel = endTimeParallel-startTimeParallel;
        System.out.println("Time in milliseconds:" + timeParallel);
        
        //System.out.println("test");
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
}
