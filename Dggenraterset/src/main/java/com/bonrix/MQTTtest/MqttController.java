package com.bonrix.MQTTtest;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/mqtt")
public class MqttController {
    @Autowired
    private Publisher publisher;
    
    @Autowired
    private Subscriber subscriber;
    
    @GetMapping("/publish/{topic}/{message}")
    public String publishMessage(@PathVariable String topic, @PathVariable String message) {
        try {
            publisher.publishMessage(topic, message);
            return "Message published successfully!";
        } catch (MqttException e) {
            e.printStackTrace();
            return "Failed to publish message: " + e.getMessage();
        }
    }
    
    @GetMapping("/status")
    public String getSubscriberStatus() {
        boolean connected = subscriber.isConnected();
        return "MQTT Subscriber is " + (connected ? "connected" : "disconnected") + 
               ". Check server logs for received messages.";
    }
    
    @GetMapping("/subscribe/{topic}")
    public String subscribeToTopic(@PathVariable String topic) {
        try {
            subscriber.subscribeToTopic(topic);
            return "Successfully subscribed to topic: " + topic;
        } catch (MqttException e) {
            e.printStackTrace();
            return "Failed to subscribe to topic: " + e.getMessage();
        }
    }
}