package com.mycompany.liftproject;
/**
 * @author Belyaev Vitaly
 * Класс который запускает работу лифта 1 в отдельном потоке и работает бесконечно.
 * A class that starts elevator 1 in a separate thread and runs indefinitely.
 */
public class Elevator1Thread implements Runnable  {
        Elevator lift1;
        Elevator1Thread(Elevator _lift1){
            this.lift1=_lift1;
        }
        public void run(){
            try{
                    while(lift1.getStatus()){
                        
                        if(lift1.getStatusReadyToWork()){
                            if(lift1.getStatusReadyToWork()){
                                lift1.readyNotToWork();
                                lift1.Working();
                                lift1.readyToWork();
                            } 
                        }
                        Thread.sleep(16);//16
                    }
            }
            catch(InterruptedException e){
                System.out.println("Случилось страшное");
            }
    }
}