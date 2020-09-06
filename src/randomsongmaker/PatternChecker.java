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
        ExcelReader ex = new ExcelReader("data.xlsx");
        ArrayList <Sheet> pat = ex.ReadData();
        /*
        NO THREADS
        NoThreadPatternCounter pc = new NoThreadPatternCounter();
        pc.manualCount(pat);
        PrintPatterns(pc.pats);
        */
        Semaphore sem = new Semaphore(1);
        ThreadedPatternCounter mt1 = new ThreadedPatternCounter(sem,"A",0,1000,pat); 
        ThreadedPatternCounter mt2 = new ThreadedPatternCounter(sem, "B",1000,2000,pat);
        ThreadedPatternCounter mt3 = new ThreadedPatternCounter(sem,"C",2000,3000,pat); 
        ThreadedPatternCounter mt4 = new ThreadedPatternCounter(sem, "D",3000,4000,pat);
        ThreadedPatternCounter mt5 = new ThreadedPatternCounter(sem,"E",4000,5000,pat); 
        
        mt1.start(); 
        mt2.start(); 
        mt3.start(); 
        mt4.start(); 
        mt5.start(); 

        mt1.join(); 
        mt2.join(); 
        mt3.join();
        mt4.join(); 
        mt5.join(); 
        while(mt1.isAlive()||mt2.isAlive()||mt3.isAlive()||mt4.isAlive()||mt5.isAlive()){
            
        }
        PrintPatterns(ThreadedPatternCounter.pats);
        System.out.println("test");
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
}
