package com.mycompany.liftproject;
/**
 * @author Belyaev Vitaly
 * Класс который запускает работу тикетменеджера отдельном потоке и работает бесконечно.
 * A class that runs the etiquette manager in a separate thread and runs indefinitely.
 */
public class TicketsManagerThread extends Thread  {
        private final TicketsManager ticketManager;
        TicketsManagerThread(TicketsManager _ticketManager){
            this.ticketManager=_ticketManager;
        }
        public void run(){
                    while(ticketManager.getStatusManager()){                  
                        ticketManager.ChangeDurationTimeTickets();
                        ticketManager.AdditionOrders();
                        ticketManager.ChackingOrderUP();
                        ticketManager.WorkingSecondLift();
                        System.out.println("Active tasks for :"+ticketManager.GetCountActiveOrders());
                    }
        }
}
