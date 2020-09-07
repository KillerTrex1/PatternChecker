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
public class PatternParrallelMergeSort {
    private ArrayList <Pattern> pat;
    private ArrayList <Pattern> tpat;
    public ArrayList <RuntimeCPUUsage> RCUs = new ArrayList();
    public PatternParrallelMergeSort(ArrayList <Pattern> pat){
        this.pat = new ArrayList(pat);
        this.tpat = new ArrayList(pat);
    }
    public void parallelmergeSort(int low,int high, int numThreads){
           
            if (numThreads <= 1){ //if no more threads go back to serial
               mergeSort(low,high);
               return;
           }
           
           int middle = low + high / 2;
           
           Thread leftside = mergeSortparallel(low,middle,numThreads);
           Thread rightside = mergeSortparallel(middle+1,high,numThreads);
           
           leftside.start();
           rightside.start();
           
           try{
            leftside.join();
            rightside.join();
           }catch (InterruptedException e){
               e.printStackTrace();
           }
           
           merge(low,middle,high); 
    }
    private Thread mergeSortparallel (int low,int high, int numThreads){
        return new Thread () {
            public void run (){
                parallelmergeSort(low,high,numThreads / 2);
            }
        };
    }
    public ArrayList <Pattern> getAns(){
        return pat;
    }
    public void mergeSort(int low, int high){
        //RCUs.add(new RuntimeCPUUsage());
        

        int middle = (low + high) / 2;
        if(low >= high){

            return;
        }
        
        
        
        
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
