package com.mycompany.liftproject;
/**
 * @author Belyaev Vitaly
 * Класс который занимается подсчётом игрового времени. 1 минута = 1 игровому часу
 * A class that counts game time. 1 minute = 1 game hour
 */
import static com.mycompany.liftproject.GUI.gameTime;


public class TimeThread implements Runnable  {
        public static int maxDays;
        public static int currentDay=1;
        public static int currentHour=0;
        public static int currentMinutes=0;
        int timer; //общий таймер милисекунд с которого идет отсчет времени
        private int GameMinuteInMiliseconds = 960; //сколько милисекунд длится игровая минута
        javax.swing.JLabel labelTimer;
        TimeThread(int _countDays , javax.swing.JLabel _labelTime){
            this.timer=0;
            this.maxDays=_countDays;         
            this.labelTimer=_labelTime;
        }
        private static String getCurrentDay(){
            if (currentDay<10){
                return "0"+currentDay;
            }
            return ""+currentDay;
        }
        
        private static String getCurrentHour(){
            if (currentHour<10){
                return "0"+currentHour;
            }
            return ""+currentHour;
        }
        private static String getCurrentMinutes(){
            if (currentMinutes<10){
                return "0"+currentMinutes;
            }
            return ""+currentMinutes;
        }
        
        public static String getCurrentData(){
            return "2022-02-"+getCurrentDay()+" "+getCurrentHour()+":"+getCurrentMinutes()+":00";
        }
             
        public void run(){
            try{
                    while (currentDay<=maxDays){                           
                        Thread.sleep(16);//16
                        timer=timer+16;
                        if(timer%GameMinuteInMiliseconds==0){
                                   currentMinutes++;
                                   System.out.println("currentMinutes = "+currentMinutes);
                                   if(currentMinutes==59){
                                       currentMinutes=0;
                                       currentHour++;
                                       System.out.println("currentHour = "+currentHour);
                                   }
                                   if(currentHour==24){
                                       currentMinutes=0;
                                       currentHour=0;
                                       currentDay++;
                                       timer=0;
                                       System.out.println("currentDay = "+currentDay);
                                   }
                               }                      
                       gameTime = getCurrentData();
                       labelTimer.setText(gameTime);
                    }
            }
            catch(InterruptedException e){
                System.out.println("TimeThread has been interrupted");
            }
        }
}
