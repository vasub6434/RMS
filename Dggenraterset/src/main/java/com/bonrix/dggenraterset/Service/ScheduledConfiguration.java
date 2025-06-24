package com.bonrix.dggenraterset.Service;

import java.util.Date;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class ScheduledConfiguration {
	//https://stackoverflow.com/questions/26147044/spring-cron-expression-for-every-day-101am
	//@Scheduled(cron = "0 1 1 * * ?")
  /*  @Scheduled(fixedRate = 5000)
    public void executeTask1() {
        System.out.println(Thread.currentThread().getName()+" The Task1 executed at "+ new Date());
        try {
            Thread.sleep(10000);
          } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }*/
   /* @Scheduled(fixedRate = 1000)
    public void executeTask2() {
        System.out.println(Thread.currentThread().getName()+" The Task2 executed at "+ new Date());
    }*/
}