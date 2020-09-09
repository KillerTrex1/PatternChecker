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
public class RuntimeCPUUsage {
    public long total;
    public long free;
    
    public RuntimeCPUUsage(){
        
        Runtime runtime = Runtime.getRuntime();

        total = (runtime.totalMemory() / 1024) /1024;
        free = (runtime.freeMemory() / 1024) / 1024;
        /*
        long usage  = total - free;
        System.out.println("Total Memory in MB:" + total);
        System.out.println("Memory used in MB: " + usage);
        System.out.println("Memory Free in MB: " + free);
        System.out.println("Percent Used: " + ((double)usage/(double)total)*100 + "%");
        System.out.println("Percent Free: " + ((double)free/(double)total)*100 + "%");
        */
    }
    public RuntimeCPUUsage(long total,long free){

        this.total = total;
        this.free = free;
        /*
        long usage  = total - free;
        System.out.println("Total Memory in MB:" + total);
        System.out.println("Memory used in MB: " + usage);
        System.out.println("Memory Free in MB: " + free);
        System.out.println("Percent Used: " + ((double)usage/(double)total)*100 + "%");
        System.out.println("Percent Free: " + ((double)free/(double)total)*100 + "%");
        */
    }
    public void printDetails(){
        long usage  = total - free;
        System.out.println("CPU USAGE DETAILS");
        //System.out.println("Total Memory in MB:" + total);
        //System.out.println("Memory used in MB: " + usage);
        //System.out.println("Memory Free in MB: " + free);
        System.out.println("Percent Used: " + ((double)usage/(double)total)*100 + "%");
        System.out.println("Percent Free: " + ((double)free/(double)total)*100 + "%");
    }
    public double getUsePercent(){
        return ((double)(total-free)/(double)total)*100;
    }
}
