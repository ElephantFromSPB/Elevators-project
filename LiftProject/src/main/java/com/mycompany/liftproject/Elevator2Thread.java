package com.mycompany.liftproject;
/**
 * @author Belyaev Vitaly
 * Класс который запускает работу лифта 2 в отдельном потоке и работает бесконечно.
 * A class that starts elevator 2 in a separate thread and runs indefinitely.
 */
public class Elevator2Thread implements Runnable  {
        Elevator lift2;
        Elevator2Thread(Elevator _lift2){
            this.lift2=_lift2;
        }
        public void run(){
            try{
                    while(lift2.getStatus()){
                        
                        if(lift2.getStatusReadyToWork()){
                            System.out.println(lift2.CurrentStatus());
                            if(lift2.getStatusReadyToWork()){
                                lift2.readyNotToWork();
                                lift2.Working();
                                lift2.readyToWork();
                            } 
                        }
                        Thread.sleep(16);
                    }
            }
            catch(InterruptedException e){
                System.out.println("Случилось страшное");
            }
    }
}
