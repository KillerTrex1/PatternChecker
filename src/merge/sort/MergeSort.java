package merge.sort;

import java.util.Random;

/**
 *
 * @author Paolo
 */
public class MergeSort {

        private int[] nums;
      private int[] tempArray;
      
      public MergeSort(int[] nums){
          this.nums = nums;
          tempArray = new int [nums.length];
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
        System.out.println("Memory before serieal algorithm");
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
        
        //Serial Merge Sort
        MergeSort seriealMerge = new MergeSort (nums);
        long startTimeSerial = System.currentTimeMillis();
        seriealMerge.mergeSort(0, nums.length - 1);   
        long endTimeSerial = System.currentTimeMillis();
        
        //Checking for Time
        System.out.println("Time for Serial Implementation:");
        long timeSerial = endTimeSerial-startTimeSerial;
        System.out.println("Time in milliseconds:" + timeSerial);
        
          
          //Print Data
//        for(int i =0; i< nums.length;++i){
//            System.out.println(nums[i]+ " ");
//        }
    }
}


