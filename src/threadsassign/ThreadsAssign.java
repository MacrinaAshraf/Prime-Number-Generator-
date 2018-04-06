package threadsassign;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadsAssign {
    
    public static Object lock1 = new Object();
    public static int val;
    public static Queue<Integer> queue = new LinkedList<Integer>();
   // public static boolean flag = false;
    public static boolean done = false;
    public static boolean isprime(int x)
    {
        for(int i = 2; i < x; i++)
        {
            if(x % i == 0)
                return false;
        }
        return true;
    
    }
    
    
    public static void main(String[] args) 
    {
        Scanner sc = new Scanner(System.in);
        val = sc.nextInt();
        Producer p1 = new Producer();
        p1.start();
        Consumer c1 = new Consumer();
        //Consumer c2 = new Consumer();
        c1.start();        
//        if(!c1.isAlive())
//            c2.start();
    }
    
}


class Producer extends Thread
{
   @Override
   public void run()
   {
       
       System.out.println("running state");
       produce();
              
   }
   public void produce()
   {
       synchronized (ThreadsAssign.lock1) 
       {
       for(int i = 2; i <= ThreadsAssign.val; i++)
       {
           try{
           
               //th.lock1.wait();
               if(ThreadsAssign.isprime(i))
               {
                   //ThreadsAssign.flag = true;
                   ThreadsAssign.queue.add(i);
                   System.out.println("the added number: " + i);
                   
                   ThreadsAssign.lock1.notify();
                   ThreadsAssign.lock1.wait();
                   //System.out.println("the added number: " + i);
               }
               //System.out.println("iam in the p1");
           }catch (InterruptedException ex) {
               Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
           }
               
           }
             
          
           ThreadsAssign.done = true;
           ThreadsAssign.lock1.notify();
       }
   }
}



class Consumer extends Thread
{
    //public Object lock1 = new Object();
    @Override
    public void run()
    {
        consume();
             
    }
       
   public void consume()
   {
        try 
      {
          FileWriter fr = new FileWriter("output.txt");
          BufferedWriter out = new BufferedWriter(fr);
          while(true)
          {
            synchronized(ThreadsAssign.lock1)
            {   
                System.out.println("inside consumer.");
                if (!(ThreadsAssign.queue.isEmpty()) /*&& (ThreadsAssign.flag == true)*/)
                { 
                    String s = ThreadsAssign.queue.poll().toString();
                    out.write(/*ThreadsAssign.queue.poll().toString()*/ s + "  "); //poll--> retrieves the head of the queue then deletes it.
                        // System.out.println(th.queue.poll());
                    System.out.println("the removed number: " + s);
                } 
                else if((ThreadsAssign.done == true) && (ThreadsAssign.queue.isEmpty()))
                {
                    System.out.println("i finished. ");
                    break;
                }
                else /*if(!th.queue.isEmpty())*/
                {
                  //while(!ThreadsAssign.queue.isEmpty())
                    //{
                        //ThreadsAssign.flag = false;
                        ThreadsAssign.lock1.notify();
                        ThreadsAssign.lock1.wait();
                        System.out.println("queue is empty. ");
                        
                    //}
                  //ThreadsAssign.lock1.notify();
                  //ThreadsAssign.lock1.wait(500);
                  //System.out.println("waiting... ");
                }
          }
          
        }   
              out.close();
              System.out.println("Process is finished");
      }
      catch(FileNotFoundException ex) 
      {
          System.out.print("Error wile opening file.");
      }
      catch(IOException ex)
      {
          System.out.printf("Error in input/output of the file");
      } 
      catch (InterruptedException ex) 
      {
            Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
      }
     
   }
}