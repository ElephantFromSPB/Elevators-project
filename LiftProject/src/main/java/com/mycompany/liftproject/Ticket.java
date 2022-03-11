
package com.mycompany.liftproject;

import static com.mycompany.liftproject.GUI.gameTime;
import javax.swing.table.DefaultTableModel;

/**
 * @author Belyaev Vitaly
 * Класс для тикета,которые выводятся в таблицы и управляются менеджером тикетов. Тикет это задание получаемое от кнопок или генератора на основе
 * которого создаются временные ордера-приказы для движения лифта
 *A class for tickets that are displayed in tables and managed by the ticket manager. 
 *A ticket is a task received from buttons or a generator based on
 *which creates temporary orders-orders for the movement of the elevator
 */
public class Ticket  {
    private int numberRowInTable;
    private final int ticketNumber;
    private String statusQueue;
    private final String timeStartInQueue;
    private String durationTime = "00:00:00";
    private int toFloor;
    private final int fromFloor;
    private int liftNumber;
    private final String direction;
    DefaultTableModel tableTickets;
    
    //конструктор для генератора или ручного заказа
    Ticket(DefaultTableModel _tableTickets,int _ticketNumber,String _timeQueue,int _toFlor,int _fromFloor){
        this.tableTickets = _tableTickets;
        setStatus(0);
        this.timeStartInQueue = _timeQueue;
        this.toFloor = _toFlor;
        this.fromFloor = _fromFloor;
        this.ticketNumber =_ticketNumber;
        this.direction = getDirection();
        addNewTicket();
    }
    //конструктор для кнопки вызова лифта где не знаем назначение
    Ticket(DefaultTableModel _tableTickets,int _ticketNumber,String _timeQueue,int _fromFloor,String _direction){
        this.tableTickets = _tableTickets;
        setStatus(0);
        this.timeStartInQueue = _timeQueue;
        this.toFloor = -1000; // заглушка так как не знаем куда
        this.fromFloor = _fromFloor;
        this.ticketNumber =_ticketNumber;
        this.direction = _direction;
        addNewTicketForButton();

    } 
    /**
    * Добавляем свою строку с тикетом в таблюцу тикетов
    * Adding your ticket row to the ticket table
    */
    private void addNewTicket(){
      tableTickets.addRow(new Object[]{ticketNumber,statusQueue,timeStartInQueue,durationTime,fromFloor,toFloor,"",direction});
      this.numberRowInTable=tableTickets.getRowCount()-1;
      
    }
    /**
    * Добавляем свою строку с тикетом от кнопок где мы не знаем этажа
    * Adding your ticket row to the ticket table but we don't know target-floor
    */
    private void addNewTicketForButton(){
      tableTickets.addRow(new Object[]{ticketNumber,statusQueue,timeStartInQueue,durationTime,fromFloor,"","",direction});
      this.numberRowInTable=tableTickets.getRowCount()-1;
    }

    /**
    * Изменить свою строку с тикетом в табилце
    * Change state of ticket in the ticket table
    */
    public void ChangeStatus(int _statusCode){
        switch(_statusCode){
        case 0: 
            this.statusQueue = "In queue";
            tableTickets.setValueAt(statusQueue, numberRowInTable, 1);
            break;
        case 1: 
            this.statusQueue = "Assigned";
            tableTickets.setValueAt(statusQueue, numberRowInTable, 1);
            break;
        case 2: 
            this.statusQueue = "Maked";
            tableTickets.setValueAt(statusQueue, numberRowInTable, 1);
            break;
        } 
    }
    /**
    * Получить номер тикета
    * Get ticket number
    */      
    public int getTicketNumber(){
        return ticketNumber;
    }
    /**
    * Получить направление тикета
    * Get ticket direction 
    */      
    public String getDirectionTicket(){
        return direction;
    }  
    /**
    * Получить этаж откуда забрать
    * Get floor from
    */      
    public int getFromFloor(){
        return fromFloor;
    }
    /**
    * Получить этаж куда забрать
    * Get floor to 
    */      
    public int getToFloor(){
        return toFloor;
    }
    /**
    * Получить состояние в очереди
    * Get state ticket in queue
    */      
    public String getStatus(){
        return statusQueue;
    }
    /**
    * Получить время ожидания
    * Get time of tiket duration
    */      
    public String getDurationTime(){
        return durationTime;
    }
    /**
    * Установить статус тикета. 0 - в очереди. 1 - назначен лифту. 2 - выполнен лифтом
    * Set state of ticket. 0 - "In queue" .1 - "Assigned". 2 - "Maked"
    */ 
    private void setStatus(int _statusCode){
        switch(_statusCode){
        case 0: 
            this.statusQueue = "In queue";
            break;
        case 1: 
            this.statusQueue = "Assigned";
            break;
        case 2: 
            this.statusQueue = "Maked";
            break;
        }
    }
    /**
    * Изменить статус тикета. 0 - в очереди. 1 - назначен лифту. 2 - выполнен лифтом
    * Change state of ticket. 0 - "In queue" .1 - "Assigned". 2 - "Maked"
    */ 
    public void changeStatus(int _statusCode){
        switch(_statusCode){
        case 0: 
            this.statusQueue = "In queue";
            break;
        case 1: 
            this.statusQueue = "Assigned";
            break;
        case 2: 
            this.statusQueue = "Maked";
            break;
        }
    }
    /**
    * Получиь направление движения
    * Get ticket direction
    */ 
    private String getDirection(){
        String direction="";
        if(fromFloor<=toFloor){
            direction ="Up";
        }
        else{
            direction = "Down";
        }
        return direction;
    }
    /**
    * Получить время создания
    * Get time of ticket created
    */ 
    public String getTimeStartInQueue(){
       return timeStartInQueue ;
    }
    /**
    * Получить номер назначенного лифта
    * Get the assigned elevator number
    */ 
    public int getLift(){
       return liftNumber;
    }
    /**
    * Назначить лифт
    * Assign elevator
    */ 
    public void setLift(int _liftNumber){
        this.liftNumber=_liftNumber;
        tableTickets.setValueAt(liftNumber, numberRowInTable, 6);
    }
    /**
    * Назначить этаж куда
    * Assign target-floor
    */ 
    public void setFlorTo(int _florTo){
        this.toFloor=_florTo;
        tableTickets.setValueAt(_florTo, numberRowInTable, 5);
    }
    /**
    * Изменить  время таймера
    * Change time of taimer
    */
    public void ChangeTicketDurationTime(){
        String test = Timer(timeStartInQueue,gameTime);
        tableTickets.setValueAt(test, numberRowInTable, 3);
        this.durationTime=test;
    }
    /**
    * Таймер технический метод. 
    * Technical method of timer
    */
    private String Timer(String _oldTime, String _currentTime){
            int oldTimeSpace= _oldTime.lastIndexOf(" ");
            String oldTimedata= _oldTime.substring(0, oldTimeSpace);
            String oldTimetime = _oldTime.substring(oldTimeSpace, _oldTime.length());
            int oldTimeHh =Integer.parseInt(oldTimetime.substring(1,oldTimetime.indexOf(":")));
            int oldTimeMM =Integer.parseInt(oldTimetime.substring(oldTimetime.indexOf(":")+1,oldTimetime.lastIndexOf(":")));
            
            int currentTimeSpace= _currentTime.lastIndexOf(" ");
            String currentTimeData= _currentTime.substring(0, currentTimeSpace);
            String currentTimeTime = _currentTime.substring(currentTimeSpace, _currentTime.length());
            int currentTimeHh =Integer.parseInt(currentTimeTime.substring(1,currentTimeTime.indexOf(":")));
            int currentTimeMm =Integer.parseInt(currentTimeTime.substring(currentTimeTime.indexOf(":")+1,currentTimeTime.lastIndexOf(":")));
            
            int taimerHh = currentTimeHh-oldTimeHh;
            int taimerMm = currentTimeMm - oldTimeMM;
            String hh;
            String mm;
            if(taimerHh<10){
                 hh = "0"+taimerHh;
            }
            else{
                 hh=""+taimerHh;
            }
            if(taimerMm<10){
                mm="0"+taimerMm;
            }
            else{
                mm=""+taimerMm;
            }
            return hh+":"+mm+":00";
    }
    
    
}
