package com.mycompany.liftproject;
import java.awt.Color;
import java.util.ArrayList;
import static com.mycompany.liftproject.GUI.gameTime;
import javax.swing.table.DefaultTableModel;
/**
 * @author Belyaev Vitaly
 * Класс лифта. Лифт осуществляет перемещние по ордерам формирующимся из тикетов. После выполнения ордер удаляется а тикет меняет статус на выполнено
 * Ордера создаёт и удаляет TicketsManager. Лифт только меняет их статус. 
 * В реальной жизини скорость лифта 5 сек\этаж без учёта дверей.Получается скорость игрового лифта 0,083 реаалиьных сек или 83 милисекунд\60000милисекунд  (игровая секунда\игровой час)
 * Class of elevator. Elevator is moving used orders formed from tickets. Completed order will delete and tikets will change state on maked.
 * Orders is making and deleting by Tickets Manager. Elevator can only change  state of order
 * In real life speed of lift is 5 sekunds \ floor (without open and close doors). Speed of game elevator is 0,083 sek or 83 miliseconds
 */
 class Elevator {
        //флаг работы для потока. Поток LiftThread работает до тех пор пока флаг не станет false
        //flag of work elevatorThread. Thread LiftThread will work until the flag becomes false
        private boolean flag = true;
        //статус лифта. Занят заказом = false. Свободен = true
        //state of lift. Take order=false. Haven't order = true
        private boolean statusLift;
        //номер лифта 
        //number of elevator
        private final int numberLift;
        //скорость лифта (в милисекундах)
        //speed of elevator (in miliseconds)
        private final int speedOfLiftInMilisecunds = 83; 
        //time открытия дверей
        //time of open the doors
        private final int timeOpenDoorInMilisecunds = 249;
        //time закрытия дверей
        //time of close the doors
        private final int timeCloseDoorInMilisecunds = 249;
        //текущий этаж
        //current floor
        private int currentFloor; 
        //максимальный этаж
        //maximum floor
        private int maxFloor; 
        //минимальный этаж
        //minimum floor
        private int minFloor; 
        //наличие ошибки
        //error state
        private boolean error=false;
        //массив текстбоксов с гуя которые играют роль этажей в лифте
        //array of textboxs from GUI. It will elevator shaft
        private javax.swing.JTextField arrayLift[] = new javax.swing.JTextField[maxFloor] ;
        //Список ордеров для работы
        //List of orders for work of lift
        ArrayList<orders> ListOfOrdersUP;
        //гуёвый журнал
        //journal from GUI
        DefaultTableModel journal;
        
        Elevator(int _numberLift, javax.swing.JTextField _arrayLift[],ArrayList<orders> _ListOfOrdersUP,DefaultTableModel _journal){
            this.maxFloor=13;
            this.minFloor=0;
            this.currentFloor=1;       
            this.arrayLift=_arrayLift;
            this.numberLift=_numberLift;
            this.statusLift=true;
            this.ListOfOrdersUP=_ListOfOrdersUP;
            this.journal=_journal;
            arrayLift[currentFloor].setBackground(Color.GREEN);
        } 
       /**
       * Получить флаг для работы потока
       * Get flag for elevator thread
       */
       public boolean getStatus(){
           return flag;
       }   
       /**
       * Установить флаг для работы потока - работаем
       * Set flag for elevator thread - turn on thread
       */
       public void turnOnThreadLift(){
           flag=true;
       }      
       /**
       * Установить флаг для работы потока - не работаем
       * Set flag for elevator thread - turn off thread
       */
        public void turnOffThreadLift(){
           flag=false;
        }          
        /**
        * Выдать текущий этаж и номер лифта в виде строки
        * Get current floor and lift number in one string
        */       
        public String CurrentStatus(){
            String text = "Elevator №"+numberLift+" is located on floor №"+currentFloor;
            return text;
        }
        /**
        * Выдать общую информацию о лифте
        * Get common information about the elevator
        */  
        public String CommonStatus(){
            String text="";
            if(error){
                 text = "Elevator №"+numberLift+" has error. Floor №"+currentFloor;
            }
            else{
                 text = "Elevator №"+numberLift+" ready to work on floor №"+currentFloor+".Errors none.Minimal floor the house:"+minFloor+".Maximum floor the house"+maxFloor;
            }
            return text;
        }
        /**
        * получить статус лифта. True - свободен
        * Get state of the elevator. True is free
        */        
        public boolean getStatusReadyToWork(){
            return this.statusLift;
        }
        /**
        * Устанолвить статус лифта - в работе
        * Set state of elevator - in work
        */        
        public void unreadyToWork(){
            this.statusLift=false;
        }
        /**
        * Установить готовность к работе
        * Set state of elevator - ready to work
        */        
        public void readyToWork(){
              this.statusLift=true;
        }
        /**
        * Установить неготовность к работе
        * Set state of elevator - unready to work
        */        
        public void readyNotToWork(){
              this.statusLift=false;
        }      
        /**
        * выдаем массив от текущего этажа вверх или вниз c заказами откуда (куда мы ещё не знаем). До начала движения
        * Get array from current floor to Up or to Down with orders. Before the start of the movement
        */  
        public boolean[] CheckingAndTakeArrayFloors(String _directionLift){
            boolean[] floorAll= new  boolean[14];
            if(ListOfOrdersUP.isEmpty()!=true){
                orders order=null;
                if("Up".equals(_directionLift)){
                         //смотрим все ордера  с выбранным направлением равные или больше текущего этажа и записываем массива куда ехать для передачи лифту 
                         //look all orders with selected direction or orders more than current floor. Marked boolean array for transmitted toelevator
                        for(int i=0;i<=ListOfOrdersUP.size()-1;i++){
                                    order = ListOfOrdersUP.get(i);
                                    //смотрим что данный ордер совпадает с нужным направлением
                                    // Check the order coincides with the desired direction
                                    if((order.GetDirection().equals(_directionLift))){ 
                                         //смотрим что данный ордер не назначен
                                         // Check the order haven't state "Assigned"
                                         if(order.GetStatus()==false){
                                            //метим элемент массива этажей как тру. Сюда надо ехать
                                            //mark the element of boolean array as true. Elevator have to go here
                                            floorAll[order.GetFloorFrom()]=true;
                                            //пометить ордер как назначенный и присвоить ему номер лифта
                                            //mark the order as "Assigned" and assign him an elevator number
                                            order.SetTakeOrder(numberLift);
                                            //вывести в журнал
                                            //output to the log
                                            addNewRowToJournal("Elevator №" + numberLift + " taked tiket №"+order.GetTicketNumber());
                                         }
                                    }
                        }     
                }
                if("Down".equals(_directionLift)){
                        //смотрим все ордера  с выбранным направлением равные или меньше текущего этажа и записываем массива куда ехать для передачи лифту 
                        //look all orders with selected direction or orders more than current floor. Marked boolean array for transmitted toelevator
                        for(int i=0;i<=ListOfOrdersUP.size()-1;i++){
                                    order = ListOfOrdersUP.get(i);
                                     if((order.GetDirection().equals(_directionLift))){ 
                                         //смотрим что данный ордер не назначен
                                         // Check the order haven't state "Assigned"
                                         if(order.GetStatus()==false){
                                            //метим элемент массива этажей как тру. Сюда надо ехать
                                            //mark the element of boolean array as true. Elevator have to go here
                                            floorAll[order.GetFloorFrom()]=true;
                                            //пометить ордер как назначенный и присвоить ему номер лифта
                                            //mark the order as "Assigned" and assign him an elevator number
                                            order.SetTakeOrder(numberLift);
                                            //вывести в журнал
                                            //output to the log
                                            addNewRowToJournal("Elevator №" + numberLift + " taked tiket №"+order.GetTicketNumber());
                                         }
                                    }
                        }     
                }
           }
           return floorAll;    
        }
        /**
        * массимв пересматриваемый по ходу движения. От текущего этажа вверх или вниз
        * Get array from current floor to Up or to Down with orders. In movement of elevators
        */  
        public boolean[] CheckingAndTakeArrayFloorsInMoving(String _directionLift){
            boolean[] floorAll= new  boolean[14];
            if(ListOfOrdersUP.isEmpty()!=true){
                orders order=null;
                if(_directionLift=="Up"){
                        //смотрим все ордера  с выбранным направлением равные или меньше текущего этажа и записываем массива куда ехать для передачи лифту 
                        //look all orders with selected direction or orders more than current floor. Marked boolean array for transmitted toelevator
                        for(int i=0;i<=ListOfOrdersUP.size()-1;i++){
                                    order = ListOfOrdersUP.get(i);
                                    //смотрим что его направление совпадает с нужным 
                                    if((order.GetDirection().equals(_directionLift))){
                                        //смотрим что этаж куда выше текущего
                                        //Check the order have floor more than current
                                        if(order.GetFloorFrom()>=this.currentFloor){
                                         //смотрим что данный ордер не назначен
                                         // Check the order haven't state "Assigned"
                                            if(order.GetStatus()==false){
                                                //метим элемент массива этажей как тру. Сюда надо ехать
                                                //mark the element of boolean array as true. Elevator have to go here
                                                floorAll[order.GetFloorFrom()]=true;
                                                //пометить ордер как назначенный и присвоить ему номер лифта
                                                //mark the order as "Assigned" and assign him an elevator number
                                                order.SetTakeOrder(numberLift);
                                                //вывести в журнал
                                                //output to the log
                                                 addNewRowToJournal("Elevator №" + numberLift + " take tiket №"+order.GetTicketNumber());
                                            }
                                        }
                                    }
                        }     
                       
                }
                if(_directionLift=="Down"){
                        //смотрим все ордера  с выбранным направлением равные или меньше текущего этажа и записываем массива куда ехать для передачи лифту 
                        for(int i=0;i<=ListOfOrdersUP.size()-1;i++){
                                    //текущий ордер
                                    order = ListOfOrdersUP.get(i);
                                     if((order.GetDirection().equals(_directionLift))){ 
                                        //смотрим что этаж куда ниже текущего
                                        //Check the order have floor less than current
                                         if(order.GetFloorFrom()<=this.currentFloor){
                                         //смотрим что данный ордер не назначен
                                         // Check the order haven't state "Assigned"
                                            if(order.GetStatus()==false){
                                               //метим элемент массива этажей как тру. Сюда надо ехать
                                                //mark the element of boolean array as true. Elevator have to go here
                                                floorAll[order.GetFloorFrom()]=true;
                                                //пометить ордер как назначенный и присвоить ему номер лифта
                                                //mark the order as "Assigned" and assign him an elevator number
                                                order.SetTakeOrder(numberLift);
                                                //вывести в журнал
                                                //output to the log
                                                addNewRowToJournal("Elevator №" + numberLift + " take tiket №"+order.GetTicketNumber());
                                            }

                                         }
                                    }
                        }     
                }
           }
           return floorAll;    
        }
        /**
        * Рабочий метод новый.
        * The working method is main.
        */  
        public void Working(){
            //проверяем что есть хоть один ордер
            //check that there is at least one order
            if(ListOfOrdersUP.isEmpty()!=true){
                //берём первое-попавшееся направление ордера от первого невыполненного ордера
                //we take the first direction of the order from the first unfulfilled order.
                String directionLift = ListOfOrdersUP.get(0).GetDirection();
                //Если лифт свободен (не выполняет ордер) т.е true
                //check that the list is ready to work (true)
                if(getStatus()){
                        //Устаноили у лифта статус "false" что значит в работе
                        //Set unready to work for the elevator
                        unreadyToWork();     
                        //двигаем лифтом по текущему массиву 
                        //Move the elevator using the array
                        MoveWithUseArray(CheckingAndTakeArrayFloors(directionLift),directionLift);
                        //Устаноили у лифта статус "true" что значит готов к работе      
                        //Set ready to work for the elevator
                        readyToWork();
                }
            }
        }        
    
        /**
        * Двигаем лифтом передавая ему для движения булевский массив из 14 элементов где каждый элемент массива равен этажу и выбранному направлению вверх или вниз
        * Move the elevator using the boolean array from 14 index. One index = one floor and selected direction 
        */
        public void MoveWithUseArray(boolean _floorAll[],String _directionLift){
            orders order=null;
            if(_directionLift=="Up"){
                for(int i=0;i<=13;i++){
                    if(_floorAll[i]==true){
                        Move(i);
                        OpenDoor();
                        //смотрим ордер к которому мы приехали и прописываем ему что он выполнен и генерирум новый этаж
                        //Check current order and mark as naked and generate  floor-target
                        for(int j=0;j<=ListOfOrdersUP.size()-1;j++){
                            order = ListOfOrdersUP.get(j);
                            //проверяем что этаж на котором мы находимся совпадает с ордером из списка
                            //Check the current floor equal to order from list
                            if(order.GetFloorFrom()==this.currentFloor){
                                //проверям что направление ордера и  лифта совпадает
                                //check equal direction for orders and  for elevators
                                if(order.GetDirection().equals(_directionLift)){
                                    //проверяем что статус лифта - назчен
                                    //check state of elevatros = assigned 
                                    if(order.GetStatus()){
                                        //проверяем что назначен нужному лифту
                                        //check that it is assigned to the right elevator 
                                        if(order.GetNumberLift()==this.numberLift){
                                            //генерируем значение от текущего до 13го типо чел зашёл в лифт и нажал
                                            //Generate target-floor
                                            int GenFloorTo = generationToFloorUp(this.currentFloor);
                                            //присваиваем ордеру что бы тикет менеджер забрал значение и вывел в таблице
                                            //assign the order so that the ticket manager takes the value and displays it in the table
                                            order.SetFloorTo(GenFloorTo);
                                            //ставим флажок на этаже что сюда тоже надо
                                            //turn or flag for move
                                            _floorAll[GenFloorTo]=true;
                                            //Выводим данные в журнал
                                            addNewRowToJournal("Tiket №"+order.GetTicketNumber()+" wants to "+GenFloorTo+" floor");                                           
                                            //метим таск как выполненный
                                            //output to the log
                                            order.SetOrderMaked();
                                            break; 
                                        }
                                    }
                                }
                            }       
                        }        
                        CloseDoor();
                    }
                    //пересматриваем появилось ли что то новое от текущего этажа и выше
                    //we are reviewing whether there is anything new from the current floor and above
                    boolean arrayFlor2[] = CheckingAndTakeArrayFloorsInMoving(_directionLift);
                    //добавляем к тому массиву по которому ездит лифт
                    //add to original array of target for move the elevator
                    for(int index=this.currentFloor;index<=13;index++){
                        if(arrayFlor2[index]==true){
                            _floorAll[index]=true;
                        }
                    }
                 }
                }
            if(_directionLift=="Down"){
                for(int i=13;i>=0;i--){
                    if(_floorAll[i]==true){
                        Move(i);
                        OpenDoor();
                        //смотрим ордер к которому мы приехали и прописываем ему что он выполнен и генерирум новый этаж
                        //Check current order and mark as naked and generate  floor-target
                        for(int j=0;j<=ListOfOrdersUP.size()-1;j++){
                            order = ListOfOrdersUP.get(j);
                             //проверяем что этаж на котором мы находимся совпадает с ордером из списка
                            //Check the current floor equal to order from list
                            if(order.GetFloorFrom()==this.currentFloor){
                                //проверям что направление ордера и  лифта совпадает
                                //check equal direction for orders and  for elevators
                                if(order.GetDirection()==_directionLift){
                                   //проверяем что статус лифта - назначен
                                    //check state of elevatros = assigned 
                                    if(order.GetStatus()){
                                        //проверяем что назначен нужному лифту
                                        if(order.GetNumberLift()==this.numberLift){
                                            //генерируем значение от текущего до 0-го типо чел зашёл в лифт и нажал
                                             //Generate target-floor
                                            int GenFloorTo = generationToFloorDown(this.currentFloor);
                                            //присваиваем ордеру что бы тикет менеджер забрал значение и вывел в таблице
                                            //assign the order so that the ticket manager takes the value and displays it in the table
                                            order.SetFloorTo(GenFloorTo);
                                            //ставим флажок на этаже что сюда тоже надо
                                            //turn or flag for move
                                            _floorAll[GenFloorTo]=true;
                                            //Выводим данные в журнал
                                            //output to the log
                                            addNewRowToJournal("Tiket №"+order.GetTicketNumber()+" wants to "+GenFloorTo+" floor");     
                                            //метим таск как выполненный
                                            //output to the log
                                            order.SetOrderMaked();
                                            break; 
                                        }
                                    }
                                }      
                            }                     
                        }     
                        CloseDoor();
                    }
                    //пересматриваем появилось ли что то новое от текущего этажа и выше
                    //we are reviewing whether there is anything new from the current floor and above
                    boolean arrayFlor2[] = CheckingAndTakeArrayFloorsInMoving(_directionLift);
                    //добавляем к тому массиву по которому ездит лифт
                    //add to original array of target for move the elevator
                    for(int index=this.currentFloor;index>=0;index--){
                        if(arrayFlor2[index]==true){
                            _floorAll[index]=true;
                        }
                    }
                }
               }           
            }
        /**
        * Сгенерировать этаж куда вверх
        * Generate floor to up
        */     
        private int generationToFloorUp(int _currentFloor){
             int generationToFloor= rnd(_currentFloor,13);
             return generationToFloor;
        }
        /**
        * Сгенерировать этаж куда вниз
        * Generate floor to down
        */     
        private int generationToFloorDown(int _currentFloor){
             int generationToFloor= rnd(0,_currentFloor);
             return generationToFloor;
        }
        /**
        * Технический метод для генерации
        * Texnical method for generate
        */   
        private static int rnd(int min,int max){
            max -=min;
            return (int) (Math.random()*++max)+min;
        }
        /**
        * Добавить новую строку в журнал с системным игровым времени и нужной записью
        * Add new string in journal with _data
        */    
        private void addNewRowToJournal(String _data){
            if(_data!=""){
                journal.addRow(new Object[]{gameTime,_data});
            }
        }          
        /**
        * Метод движения лифта к нужному этажу
        * Method of move elevator to need floor
        */
        public void Move(int _targetFloor){
            if((_targetFloor<=maxFloor)&(_targetFloor>=minFloor)){
                if(currentFloor<_targetFloor){
                   try{             
                         int startCurrentFloor= this.currentFloor;
                         for(int i=startCurrentFloor;i<=_targetFloor;i++){
                                arrayLift[i].setBackground(Color.GREEN);
                                if(i!=minFloor){ arrayLift[i-1].setBackground(Color.WHITE);} 
                                Thread.sleep(speedOfLiftInMilisecunds);
                                this.currentFloor=i;    
                         }
                    }
                    catch(InterruptedException e){}
                }
                else{
                    try{
                             int startCurrentFloor= this.currentFloor;
                             arrayLift[startCurrentFloor].setBackground(Color.WHITE);
                             for(int i=startCurrentFloor;i>=_targetFloor;i--){
                                    arrayLift[i].setBackground(Color.GREEN);
                                    if(i!=maxFloor){ arrayLift[i+1].setBackground(Color.WHITE);}
                                    Thread.sleep(speedOfLiftInMilisecunds);
                                    this.currentFloor=i;
                             }
                    }
                    catch(InterruptedException e){}
                }
            }
        }
        /**
        * Метод открытия двери
        * Method of opening the door
        */
        private void OpenDoor(){
            try{
                    Thread.sleep(timeOpenDoorInMilisecunds);
             }
            catch(InterruptedException e){} 
        }
        /**
        * Метод закрытия двери
        * Method of closing the door
        */
        private void CloseDoor(){
             try{
                    Thread.sleep(timeCloseDoorInMilisecunds);
             }
            catch(InterruptedException e){}  
        }
}
