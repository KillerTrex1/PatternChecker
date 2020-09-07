/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package randomsongmaker;

/**
 *
 * @author Mart Henrick Gamutan
 */
public class CompareTools {
    public static Boolean isGreater(String p1, String p2){
        char[] p1c = p1.toCharArray();
        char[] p2c = p2.toCharArray();
        int ctr1=0;
        int ctr2=0;
        while(true){
            if (p1c[ctr1] != p2c[ctr2]){
                if ( BeatVal(p1c[ctr1]) > BeatVal(p2c[ctr2]) ){
                    return true;
                }else{
                    return false;
                }
            }else{
                if (ctr1+1>=p1c.length){
                    return true;
                }
                if (ctr2+1>=p2c.length){
                    return false;
                }
                if (p1c[ctr1+1] == 'r' && p2c[ctr2+1] == 'r'){
                    ctr1+=2;
                    ctr2+=2;
                }else if(p1c[ctr1+1] == 'r'){
                    return false;
                }else if(p2c[ctr2+1] == 'r'){
                    return true;
                }else{
                    ctr1++;
                    ctr2++;
                }
            }
            
            
        }
    }
    public static int BeatVal(char p){
        switch(p){
            case 'W':
                return 4;
            case 'H':
                return 3;
            case 'Q':
                return 2;
            case 'E':
                return 1;
            default:
                return 0;
        }
    }
}
