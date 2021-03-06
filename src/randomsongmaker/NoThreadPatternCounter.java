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
public class NoThreadPatternCounter {
    ArrayList <Pattern> pats;
    ArrayList <RuntimeCPUUsage> RCUs = new ArrayList();
    
    public NoThreadPatternCounter(){
        pats = new ArrayList();
    }
    public void manualCount(ArrayList<Sheet> Sheets){
        //long startTimeParallel = System.currentTimeMillis();
        
        for (int  i = 0; i<Sheets.size();i++){
            Sheet sheet = Sheets.get(i);
            for(int j =0 ;j<sheet.Patterns.size();j++){
                checkPatterns(sheet.Patterns.get(j));
            }
            /*
            if(i%5000 == 0){
                System.out.println(i+":"+(System.currentTimeMillis()-startTimeParallel));
            }
            */
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
    public void manualCountWithRange(ArrayList<Sheet> Sheets, int start, int end){
        for (int  i = start; i<end;i++){
            Sheet sheet = Sheets.get(i);
            for(int j =0 ;j<sheet.Patterns.size();j++){
                checkPatterns(sheet.Patterns.get(j));
            }
        }
    }
    
}

