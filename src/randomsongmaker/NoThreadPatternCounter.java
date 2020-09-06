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
    
    
    public NoThreadPatternCounter(){
        pats = new ArrayList();
    }
    public void manualCount(ArrayList<Sheet> Sheets){
        for (int  i = 0; i<Sheets.size();i++){
            Sheet sheet = Sheets.get(i);
            for(int j =0 ;j<sheet.Patterns.size();j++){
                checkPatterns(sheet.Patterns.get(j));
            }
        }
    }
    public void checkPatterns(String p){
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

