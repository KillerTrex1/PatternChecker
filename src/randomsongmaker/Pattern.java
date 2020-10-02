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
public class Pattern {
 
    
    String pattern;
    int ctr;

    
    public Pattern(String name){
        pattern = name;
        ctr = 1;
    }
    public Pattern(String name,int val){
        pattern = name;
        ctr = val;
    }

}
