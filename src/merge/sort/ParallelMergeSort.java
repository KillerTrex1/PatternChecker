/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merge.sort;

import java.util.Random;

public class ParallelMergeSort {
    
      private int[] nums;
      private int[] tempArray;
      
      public ParallelMergeSort(int[] nums){
          this.nums = nums;
          tempArray = new int [nums.length];
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
        
     public void mergeSort(int low, int high){
         
         if(low >= high){ //base case
             return;
         }
         
         int middle = (low + high) / 2;
                   
         mergeSort(low,middle); //left array
         mergeSort(middle+1,high); //right array
         merge(low,middle,high);
     }
     
     public void merge (int low, int middle, int high) {
         for(int i = low; i <= high; i++){
             tempArray[i] = nums [i];
         }
         
         int left = low;
         int right = middle+1;
         int result = low;
         
         while((left <= middle) && (right<=high)){
             if(tempArray[left] <= tempArray[right]){
                 nums[result] = tempArray[left];
                 left++;
             }
             else{
                 nums[result] = tempArray[right];
                 right++;
             }
             result++;
         }    
         
         while (left <= middle){
             nums[result] = tempArray[left];
             result++;
             left++;
         }
         
        while (right <= high){
             nums[result] = tempArray[right];
             result++;
             right++;
         }
     }
        

  public static void main( String args[] ) {
      
        Runtime runtime = Runtime.getRuntime();
       
        long total = (runtime.totalMemory() / 1024) /1024;
        long free = (runtime.freeMemory() / 1024) / 1024;
        long usage  = total - free;
        System.out.println("Memory before parallel algorithm");
        System.out.println("Total Memory in MB:" + total);
        System.out.println("Memory used in MB: " + usage);
        System.out.println("Memory Free in MB: " + free);
        System.out.println("Percent Used: " + ((double)usage/(double)total)*100 + "%");
        System.out.println("Percent Free: " + ((double)free/(double)total)*100 + "%"); 
      
        Random rand = new Random();   
        
        int [] nums = new int[105600];
        
        for (int x = 0; x < nums.length; x++){
            nums[x] = rand.nextInt(1000000000);
        }
                   
        //Parallel Merge Sort
        int numThreads = Runtime.getRuntime().availableProcessors();
        System.out.println("\nThreads: " + numThreads);
        ParallelMergeSort parallelMerge = new ParallelMergeSort (nums);
        long startTimeParallel = System.currentTimeMillis();
        parallelMerge.parallelmergeSort(0, nums.length - 1, numThreads);
        long endTimeParallel = System.currentTimeMillis();
        System.out.println("Time for Parallel Implementation:");
        long timeParallel = endTimeParallel-startTimeParallel;
        System.out.println("Time in milliseconds:" + timeParallel);
        
        //Print Data
//        for(int i =0; i< nums.length;++i){
//            System.out.println(nums[i]+ " ");
//        }
    }
}
