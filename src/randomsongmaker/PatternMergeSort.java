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
public class PatternMergeSort {
    private ArrayList <Pattern> pat;
    private ArrayList <Pattern> tpat;
    ArrayList <RuntimeCPUUsage> RCUs = new ArrayList();
    
    public PatternMergeSort(ArrayList <Pattern> pat){
        this.pat = new ArrayList(pat);
        this.tpat = new ArrayList(pat);
    }
    public ArrayList <Pattern> getAns(){
        return pat;
    }
    public void mergeSort(int low, int high){
        //RCUs.add(new RuntimeCPUUsage());
        if(low >= high){ //base case
            return;
        }

        int middle = (low + high) / 2;

        mergeSort(low,middle); //left array
        mergeSort(middle+1,high); //right array
        merge(low,middle,high);
    }
     
    public void merge (int low, int middle, int high) {
        //RCUs.add(new RuntimeCPUUsage());
        for(int i = low; i <= high; i++){
            tpat.set(i, pat.get(i));
        }
         
        int left = low;
        int right = middle+1;
        int result = low;
         
        while((left <= middle) && (right<=high)){
            //tpat.get(left).ctr
            //tpat.get(left).ctr <= tpat.get(right).ctr
            
            
            if(CompareTools.isGreater(tpat.get(left).pattern, tpat.get(right).pattern)){
                pat.set(result, tpat.get(left));
                //nums[result] = tempArray[left];
                left++;
            }else{
                pat.set(result, tpat.get(right));
                //nums[result] = tempArray[right];
                right++;
            }
            result++;
        }    
         
        while (left <= middle){
            pat.set(result, tpat.get(left));
            //nums[result] = tempArray[left];
            result++;
            left++;
        }
         
        while (right <= high){
            pat.set(result, tpat.get(right));
            //nums[result] = tempArray[right];
            result++;
            right++;
        }
     }

}
