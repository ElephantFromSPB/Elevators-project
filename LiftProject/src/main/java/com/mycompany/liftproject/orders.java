package com.mycompany.liftproject;
/**
 * Структура которую создает и удаляет TicketsManager и меняет Elevator1Thread и Elevator2Thread . 
 * The structure that Tickets Manager creates and deletes and changes Elevator1Thread and Elevator2Thread .
 */
public class orders {
     //Номер лифта взявшего ордер
     //Number elevator that take the order
     private int liftNumber;
     //Номер тикета из таблицы тикетов
     //number tickets from the table tickets
     private int ticketNumber;
     //Этаж откуда забрать
     //floor-source for move
     private int floorFrom;
     //этаж куда везти 
     //floor-targer for move
     private int floorTo;
     //статус что лифт взял заказ. Для статуса "Назначено"
     //the state that the elevator took the order. For state "Assigned"
     boolean orderStatus=false;
     //статус что лифт выполнил заказ. Для статуса "Выполнен"
     //the state that the elevator maked the order. For state "Maked"
     boolean orderMaked=false;
     //направление заказа
     //order direction
     private String direction ="";
     orders(int _ticketNumber,int _floorFrom,int _floorTo,String _direction){
         this.ticketNumber = _ticketNumber;
         this.floorFrom = _floorFrom;
         this.floorTo = _floorTo;
         this.direction=_direction;
     }
     /**
     * получаем состояние что лифт взял\не взял в работу тикет для статуса "Назначено" который ему выдаст тикетменеджер
     * get state of order. Elevator take\not take the order for state "Assigned"
     */
     public boolean GetStatus(){
         return orderStatus;
     }
     /**
     *взять ордер для статуса назначено и присвоить ему номер взявшего лифта
     *take order and set elevate number 
     */
     public void SetTakeOrder(int _numberLift){
         this.orderStatus=true;
         this.liftNumber=_numberLift;
     }
    /**
    * получаем состояние что лифт выполнил работу для статуса "Выполнено" который ему выдаст тикетменеджер и удалит тикет
    * get state of elevator for status "Maked". After change state on "Maked" order will be delite of TicketManager
    */
     public boolean GetOrderMaked(){
         return orderMaked;
     }
    /**
    * получаем состояние что лифт выполнил заказ
    * set state "Maked"
    */
     public void SetOrderMaked(){
          this.orderMaked=true;
     }
     /**
     * получить номер взявшего лифта
     * get lift number that take the order
     */   
     public int GetNumberLift(){
         return liftNumber;
     }
     /**
     * получить номер тикета по которому создан ордер
     * get ticket number
     */
     public int GetTicketNumber(){
         return ticketNumber;
     }
     /**
     * получить этаж откуда 
     * get floor-source
     */     
     public int GetFloorFrom(){
         return floorFrom;
     }
     /**
     * получить этаж куда
     * get floor-target
     */
     public int GetFloorTo(){
         return floorTo;
     }
     /**
     * Установить этаж куда 
     * Set floor-target
     */
     public void SetFloorTo(int _floorTo){
         floorTo=_floorTo;
     }
     /**
     * Получить направление ордера
     * Get order direction
     */
     public String GetDirection(){
         return direction;
     }
}
