package randomsongmaker;

import java.io.IOException;
import java.util.ArrayList;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Mart Henrick Gamutan
 */
public class ZMQTaskWorker {
    public ZMQTaskWorker() throws InterruptedException, IOException{
        ExcelReader ex = new ExcelReader("data.xlsx");
        ArrayList <Sheet> pat = ex.ReadData();
        ExcelReader ex2 = new ExcelReader("patterns.xlsx");
        ArrayList <Pattern> pat2 = ex2.ReadPatterns();
        ExcelReader ex3 = new ExcelReader("mergepatterns.xlsx");
        ArrayList <Pattern> pat3 = ex3.ReadPatterns();
        try (ZContext context = new ZContext()) {

            //  Socket to receive messages on
            ZMQ.Socket receiver = context.createSocket(SocketType.PULL);
            receiver.connect("tcp://192.168.1.20:5557");

            //  Socket to send messages to
            ZMQ.Socket sender = context.createSocket(SocketType.PUSH);
            sender.connect("tcp://192.168.1.20:5558");
            System.out.println("Waiting for orders...");
            //  Process tasks forever
            while (!Thread.currentThread().isInterrupted()) {
                String string = new String(receiver.recv(0), ZMQ.CHARSET).trim();
                System.out.println("Receieved String: "+string);
                String[] ranges = string.split("/");
                System.out.println(ranges[0]);
                int l = Integer.parseInt(ranges[0]);
                int r = Integer.parseInt(ranges[1]);
                int task = Integer.parseInt(ranges[2]);
                String goal = "";
                String ans = "";
                if (task == 1){
                    ans = PCTask(l,r,pat);
                }else if(task == 2){
                    ans = MSTask(l,r,pat2);
                }else{
                    goal = ranges[3];
                    ans = BSTask(l,r,goal,pat3);
                }
                //  Simple progress indicator for the viewer


                //  Do the work
                //Thread.sleep(msec);

                //  Send results to sink
                sender.send(ans, 0);
                
            }
        }
        
    }
    public String PCTask(int l, int r,ArrayList <Sheet> pat) throws IOException{
        //setup task 1
        
        System.out.println("Starting Pattern Count");
        NoThreadPatternCounter pc = new NoThreadPatternCounter();
        
        long startTimeMS = System.currentTimeMillis();
        pc.manualCountWithRange(pat,l,r);
        String result = pc.pats.get(0).pattern + "#" + pc.pats.get(0).ctr;
        for (int i = 1;i<pc.pats.size();i++){
            result = result + "/" + pc.pats.get(i).pattern + "#" + pc.pats.get(i).ctr;
        }
        long endTimeMS = System.currentTimeMillis();
        long timeMS = endTimeMS-startTimeMS;
        System.out.println("Time in milliseconds:" + timeMS);
        return result;
    }
    public String MSTask(int l, int r,ArrayList <Pattern> pat) throws IOException{
        //setup task 2
        
        
        
        
        ArrayList <Pattern> patPartition = new ArrayList();
        for (int i = l; i< r;i++){
            patPartition.add(pat.get(i));
        }
        
        System.out.println("Starting Merge Sort");
        PatternMergeSort PMS = new PatternMergeSort(patPartition);
        long startTimeMS = System.currentTimeMillis();
        PMS.mergeSort(0, patPartition.size()-1);
        long endTimeMS = System.currentTimeMillis();
        ArrayList <Pattern> ans = PMS.getAns();
        /*
        int sum = 0;
        for (int i =0; i<ans.size();i++){
            System.out.println(ans.get(i).pattern+": "+ans.get(i).ctr);
            sum+=ans.get(i).ctr;
        }
        System.out.println(sum);
        System.out.println(ans.size()); 
        */
        long timeMS = endTimeMS-startTimeMS;
        System.out.println("Time in milliseconds:" + timeMS);
        String result = ans.get(0).pattern + "#" + ans.get(0).ctr;
        for (int i = 1;i<ans.size();i++){
            result = result + "/" + ans.get(i).pattern + "#" + ans.get(i).ctr;
        }
        return result;
    }
    public String BSTask(int l, int r, String goal,ArrayList <Pattern> pat) throws IOException{
        
        BinarySearchPattern BSP = new BinarySearchPattern(pat,goal);
        System.out.println("Starting Binary Search");
        long startTimeBSP= System.currentTimeMillis();
        long startTimeBSPNano= System.nanoTime();
        int x = BSP.BinarySearch(l,r);
        long endTimeBSP = System.currentTimeMillis();
        long endTimeBSPNano= System.nanoTime();
        long timeBSP = endTimeBSP-startTimeBSP;
        System.out.println("Time in milliseconds:" + timeBSP);
        long timeBSPNano = endTimeBSPNano-startTimeBSPNano;
        System.out.println("Time in Nanoseconds:" + timeBSPNano);
        //getAverageRuntime(BSP.RCUs).printDetails();
        
        /*
        //Answer
        if (x!=-1){
            System.out.println(goal+" is at index "+x+" with a count of "+ans.get(x).ctr);
        }else{
            System.out.println("Error");
        }
        */
        String ans = Integer.toString(x);
        return ans;
    }
}
