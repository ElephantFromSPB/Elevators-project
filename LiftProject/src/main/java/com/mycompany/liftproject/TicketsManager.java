package com.mycompany.liftproject;
import static com.mycompany.liftproject.GUI.gameTime;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
/**
 * @author Belyaev Vitaly
 * Менеджер тикетов которые упраляет их состоянием и статусами и на основе тикетов делает ордера - краткоживущие приказы
 * Ticket manager who manages their state and statuses and makes orders based on tickets - short-lived orders
 */
public class TicketsManager {
    //флаг работы для потока
    //flag for work of thread 
    private boolean flag = true;
    //Хранилище всех тикетов
    //Storage of all tickets
    private ArrayList<Ticket> ListOfAlltickets = new ArrayList<Ticket>();
    //Cписок этажей вверх которые сейчас горят
    //List of orders (active tickets for move elevator)
    ArrayList<orders> ListOfOrders;
    //Таблица тикетов с гуя
    //Ticket table from GUII
    private DefaultTableModel tableTickets;
    //номер тикета уникальный
    //unique ticket number
    int ticketNumber=0;
    //Таблица журнала
    //Table of journal
    private DefaultTableModel journal;
    //Лифт 1
    //Elevator 1
    Elevator lift1;
    //Лифт 2
    //Elevator 2
    Elevator lift2;
    
    TicketsManager(DefaultTableModel _tableTickets,ArrayList<orders> _ListOfOrders,DefaultTableModel _journal,Elevator _lift1,Elevator _lift2){
        this.tableTickets = _tableTickets;
        this.ListOfOrders = _ListOfOrders;
        this.journal = _journal;
        this.lift1=_lift1;
        this.lift2=_lift2;
    }
     /**
    * Получить статус работы менеджера
    * Get status of TicketManager work
    */
    public boolean getStatusManager(){
        return flag;
    }   
    /**
    * Установить флаг менеджера - работает
    * Set flag of TicketManager - start work
    */
    public void turnOnManeger(){
        flag=true;
    }      
    /**
    * Установить флаг менеджера - не работает
    * Set flag of TicketManager - stop work
    */
    public void turnOffManeger(){
        flag=false;
    }       
    /**
    * Добавить билет в очередь вручную или генератором
    * Add ticket in queue manually or by generator. 
    */
    public void addNewTicket(String _timeQueue,int _toFlor,int _fromFloor){
        ticketNumber++;
        ListOfAlltickets.add(new Ticket(tableTickets,ticketNumber,_timeQueue,_toFlor,_fromFloor));
    }
    /**
    * Добавить билет в список с кнопки вызова на этаже. Мы не знаем куда поэтому _toFlor неизвестен
    *  Add ticket in queue by button on floor. We don't know where so _toFlor is unknown
    */
    public void addNewTicketForButton(String _timeQueue,int _fromFloor, String _direction){
        ticketNumber++;       
        ListOfAlltickets.add(new Ticket(tableTickets,ticketNumber,_timeQueue,_fromFloor,_direction));
    }
 
    
    
    /**
    * технический метод поиска тикета по статусу. Ищем по всей таблице
    * this is technical method of search ticket used his status
    */
    private Ticket findingTicket(int _statusCode){
        if (ListOfAlltickets.isEmpty()){
             return null; 
        } 
        else{
            String status="";
            switch(_statusCode){
            case 0: 
                status = "In queue";
                break;
            case 1: 
                status = "Assigned";
                break;
            case 2: 
                status = "Maked";
                break;
            }
            for(int i=0;i<=ListOfAlltickets.size();i++){
                Ticket tiketForChange = ListOfAlltickets.get(i);
                if(tiketForChange.getStatus()==status){
                    return tiketForChange;
                }
            }
            return null; 
        }
    }
    /**
    * технический метод поиска тикета по номеру. Ищем по всей таблице
    * this is technical method of search ticket used his number
    */
    private Ticket takeTicketWithNumber(int _ticketNumber){
            for(int i=0;i<=ListOfAlltickets.size();i++){
                Ticket tiketForChange = ListOfAlltickets.get(i);
                if(tiketForChange.getTicketNumber()==_ticketNumber){
                    //tiketForChange.ChangeStatus(1);
                    return tiketForChange;
                }
            }
            return null; 
    }
    /**
    * Получить любой билет со статусом "в очереди"
    * Get any ticket with status "In queue"
    */
    public Ticket getAnyTicketFromQuerry(){
            return findingTicket(0);
    }
    /**
    * Получить любой билет со статусом "Назначен"
    * Get any ticket with status "Assigned"
    */
    public Ticket getAnySetTicket(){
            return findingTicket(1);
    }
    /**
    * Получить любой билет со статусом "Выполнен"
    * Get any ticket with status "Maked"
    */
    public Ticket getAnyCompletedTicket(){
            return findingTicket(1);
    }      
    /**
    * Обновляем время ожидания всех рабочих тикетов
    * Change duration time of all active tickets
    */
    public void ChangeDurationTimeTickets(){
        if (ListOfAlltickets.isEmpty()!=true){
                    for(int i=0;i<=ListOfAlltickets.size()-1;i++){
                        Ticket tiketForChange = ListOfAlltickets.get(i);
                        if(tiketForChange.getStatus()!="Maked"){
                            tiketForChange.ChangeTicketDurationTime();
                        }
                    }
        }
    }
    /**
    * Формируем ордера из тикетов (просто упрастили тикеты убрав ненужную инфу перед передачей лифту и назвали их ордерами)
    * This is method make ordres from tickets
    */
    public void AdditionOrders(){
        if (ListOfAlltickets.isEmpty()!=true){
            for(int i=0;i<=ListOfAlltickets.size()-1;i++){
                Ticket tiket = ListOfAlltickets.get(i);
                if(tiket.getStatus()=="In queue"){
                    int toFloor = tiket.getToFloor();
                    int fromFloor=tiket.getFromFloor();
                    int numberTicket= tiket.getTicketNumber();
                    String direction = tiket.getDirectionTicket();
                    //если такого ордера с таким номером нет - не добавляем
                    //смотрим ордера
                    //не добавляем пока не убидимся что такого нет
                    if(ChackOrderNumber(numberTicket)==false){
                        ListOfOrders.add(new orders(numberTicket,fromFloor,toFloor,direction));
                    }
                }
            }     
        }  
    }
    /**
    * Проверяем есть ли ордер с таким номером 
    * Check if there is an order with this number
    */
    private boolean ChackOrderNumber(int nummer){
        boolean flag = false;
            for(int i=0;i<=ListOfOrders.size()-1;i++){
                   orders order = ListOfOrders.get(i);
                   if(order.GetTicketNumber()==nummer){
                     flag = true;
                     break;
                   }
            }
        return flag;
    }
    /**
    * Проверяем ордера и удаляем если он уже взяты лифтом
    * check the orders and delete them if they have already been taken by the elevator
    */
    public void ChackingOrderUP(){
        if (ListOfAlltickets.isEmpty()!=true){
            for(int i=0;i<=ListOfOrders.size()-1;i++){
                   orders order = ListOfOrders.get(i);
                   // если статус ордера тру (лифт взял)
                   if(order.GetStatus()){
                       Ticket ticket = takeTicketWithNumber(order.GetTicketNumber());
                       //прописываем ему номер лифта который его взял            
                       ticket.setLift(order.GetNumberLift());
                       //меняем статус на назначен             
                       ticket.ChangeStatus(1);
                       if(order.GetOrderMaked()){
                          //меняем статус на выполнен             
                          ticket.ChangeStatus(2);
                           //прописываем ему номер куда который сгененировался когда лифт приехал на этаж          
                          ticket.setFlorTo(order.GetFloorTo());
                          //вывести в жуонал
                          addNewRowToJournal("Tiket №" + ticket.getTicketNumber() + " maked elevator №"+ticket.getLift()+".");
                          //удаляем ордер
                          ListOfOrders.remove(i);
                       }
                   }
            }
        }  
    }    
    /**
    * Количество активных ордеров для лифта
    * Get count active orders for elevators
    */
    public int GetCountActiveOrders(){
        return ListOfOrders.size();
    }    
    /**
    * Добавить новую строку в журнал с системным игровым времени и нужной записью
    * Add new string in order with data
    */    
    private void addNewRowToJournal(String _data){
        if(_data!=""){
            journal.addRow(new Object[]{gameTime,_data});
        }
    }       
    /**
    * включить\выключить  второй лифт в зависимости от первого
    * turn on\turn off second elevator depending on the first elevator
    */
    public void WorkingSecondLift(){
       lift2.readyNotToWork();
       if(lift1.getStatusReadyToWork()){
          lift2.readyNotToWork();
       }
       else
       {
          lift2.readyToWork();
       }
    }    
}
