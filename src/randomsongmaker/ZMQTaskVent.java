/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package randomsongmaker;

import java.io.IOException;
import java.util.Random;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

/**
 *
 * @author Mart Henrick Gamutan
 */
public class ZMQTaskVent {
    public ZMQTaskVent(int task,String goal) throws IOException, InterruptedException{
        int workers = 2;
        try (ZContext context = new ZContext()) {
            //  Socket to send messages on
            ZMQ.Socket sender = context.createSocket(SocketType.PUSH);
            sender.bind("tcp://*:5557");

            //  Socket to send messages on
            ZMQ.Socket sink = context.createSocket(SocketType.PUSH);
            sink.connect("tcp://localhost:5558");

            System.out.println("Press Enter when the workers are ready: ");
            System.in.read();
            System.out.println("Sending tasks to workers\n");

            //  The first message is "0" and signals start of batch
            String sinkStarter = task +"/"+workers;
            sink.send(sinkStarter, 0);
            int dataSize = 30000;
            if (task != 1){
                dataSize = 105600;
            }
            for (int task_nbr = 0; task_nbr < workers; task_nbr++) {
                int partition = dataSize/workers;
                System.out.println(partition);
                int l = partition*task_nbr;
                int r = partition*(task_nbr+1);
                if (r>dataSize){
                    r = dataSize;
                }
                System.out.println(task);
                String instruction = l + "/" + r + "/"+ (task);
                if (task == 3){
                    instruction = instruction + "/"+ goal;
                }
                System.out.println(instruction);

                sender.send(instruction, 0);
            }
            //  Give 0MQ time to deliver
            Thread.sleep(1000);
        }
    }
    /*
    public void ventCount(ZMQ.Socket sender,ZMQ.Socket sink, int workers){
        int dataSize = 30000;
 
        for (int task_nbr = 0; task_nbr < workers; task_nbr++) {
            int partition = dataSize/workers;
            int l = partition*task_nbr;
            int r = partition*(task_nbr+1);
            if (r>=dataSize){
                r = dataSize - 1;
            }
            String instruction = l + "_" + r;
            //  Random workload from 1 to 100msecs

            System.out.print(instruction);

            sender.send(instruction, 0);
        }
            
    }
*/
    
}
