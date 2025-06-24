package com.bonrix.MQTTtest;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class MqttConfig {
    private static final String BROKER = "nodered-mqtt.bonrix.in";
    private static final int PORT = 21883;
    private static final String TOPIC = "iot_data";
    
    @Bean(destroyMethod = "disconnect")
    public Subscriber mqttSubscriber() {
        Subscriber subscriber = new Subscriber();
        try {
          
            subscriber.connect(BROKER, PORT, TOPIC);
            subscriber.subscribeToTopic("ravi");  
            subscriber.subscribeToTopic("test");  
            //subscriber.subscribeToTopic("#");     
            
            return subscriber;
        } catch (MqttException e) {
            System.err.println("Error connecting MQTT subscriber: " + e.getMessage());
            e.printStackTrace();
            return subscriber;
        }
    }
    
    @Bean
    public Publisher mqttPublisher() {
        return new Publisher();
    }
}