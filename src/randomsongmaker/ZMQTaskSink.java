/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package randomsongmaker;

import java.util.ArrayList;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

/**
 *
 * @author Mart Henrick Gamutan
 */
public class ZMQTaskSink {
    public ZMQTaskSink(){
        //  Prepare our context and socket
        try (ZContext context = new ZContext()) {
            ZMQ.Socket receiver = context.createSocket(SocketType.PULL);
            receiver.bind("tcp://*:5558");

            //  Wait for start of batch
            System.out.println("Waiting to start.....");
            String string = new String(receiver.recv(0), ZMQ.CHARSET);
            System.out.println("Starting Task "+string);
            String[] values = string.split("/");
            int taskNo = Integer.parseInt(values[0]);
            int workers= Integer.parseInt(values[1]);
            //  Start our clock now
            long tstart = System.currentTimeMillis();

            //  Process 100 confirmations
            int task_nbr;
            int total_msec = 0; //  Total calculated cost in msecs
            ArrayList <String> results = new ArrayList();
            for (task_nbr = 0; task_nbr < workers; task_nbr++) {
                string = new String(receiver.recv(0), ZMQ.CHARSET).trim();
                System.out.println((System.currentTimeMillis()-tstart)+" msec");
                System.out.println(string.length());
                results.add(string);
            }
            if (taskNo == 1){
                ArrayList <Pattern> x = fusePC(results);
                System.out.println("END");
                System.out.println(x.size());
                System.out.println(x.get(0).pattern);
                System.out.println(x.get(0).ctr);
            }else if (taskNo == 2){
                ArrayList <Pattern> x = fuseMS(results);
                System.out.println("END");
                System.out.println(x.get(0).pattern);
            }else{
                int ans = fuseBS(results);
                System.out.println("END");
                System.out.println(ans);
            }
            //  Calculate and report duration of batch
            long tend = System.currentTimeMillis();

            System.out.println(
                "\nTotal elapsed time: " + (tend - tstart) + " msec"
            );
        }
    }
    public  ArrayList <Pattern> fusePC(ArrayList <String> results){
        ArrayList <Pattern> pat = new ArrayList();
        String[] firstResults = results.get(0).split("/");
        for(int i = 0;i<firstResults.length;i++){
            String[] curPattern = firstResults[i].split("#");
            
            pat.add(new Pattern(curPattern[0],Integer.parseInt(curPattern[1])));
            
        }
        if (results.size()>1){
            for (int i = 1;i<results.size();i++){
                String[] otherResults = results.get(0).split("/");
                for(int j = 0;j<otherResults.length;j++){
                    String[] curPattern = otherResults[j].split("#");

                    checkPatternsFuse(new Pattern(curPattern[0],Integer.parseInt(curPattern[1])),pat);

                }
            }
        }
        return pat;
    }
    public void checkPatternsFuse(Pattern p, ArrayList <Pattern> pats){
        //RCUs.add(new RuntimeCPUUsage());
        Boolean newP = true;
        for(int i = 0;i<pats.size();i++){
            if (pats.get(i).pattern.equals(p)){
                pats.get(i).ctr = pats.get(i).ctr + p.ctr ;
                newP = false;
                break;
            }
        }
        if (newP){
            pats.add(p);
        }
    }
    public ArrayList <Pattern> fuseMS(ArrayList <String> results){
        ArrayList <ArrayList <Pattern>> lists = new ArrayList();
        for(int i = 0;i< results.size();i++){
            ArrayList <Pattern> pat = new ArrayList();
            String[] strings= results.get(i).split("/");
            for(int j = 0;j<strings.length;j++){
                String[] curPattern = strings[i].split("#");

                pat.add(new Pattern(curPattern[0],Integer.parseInt(curPattern[1])));

            }
            lists.add(pat);
        }
        if (lists.size()==1){
            return lists.get(0);
        }
        int[] ctrs = new int[lists.size()];
        for(int i = 0; i<lists.size();i++){
            ctrs[i]=0;
        }
        boolean done = false;
        ArrayList<Pattern> Merged = new ArrayList();
        while(!done){
            
            String highest = "";
            int highestIndex = -1;
            for(int i = 0; i<lists.size();i++){
                
                if(ctrs[i] < lists.get(i).size()){
                    String curPattern = lists.get(i).get(ctrs[i]).pattern;
                    if(highestIndex == -1){
                        highest = curPattern;
                        highestIndex = i;
                    }else{
                        if (!CompareTools.isGreater(highest, curPattern)){
                            highest = curPattern;
                            highestIndex = i;
                        }
                    }
                }
                
                
                
            }
            Merged.add(lists.get(highestIndex).get(ctrs[highestIndex]));
            ctrs[highestIndex]++;
            done = true;
            for(int i = 0;i<lists.size();i++){
                if(ctrs[i] < lists.get(i).size()){
                    done = false;
                    break;
                }
            }
        }
        return Merged;
    }
    public int fuseBS(ArrayList <String> results){

        for(int i = 0;i<results.size();i++){
            if (Integer.parseInt(results.get(i))!=-1){
                return Integer.parseInt(results.get(i));
            }
        }
        return -1;
    }
}
