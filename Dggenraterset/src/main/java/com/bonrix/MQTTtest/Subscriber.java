package com.bonrix.MQTTtest;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Component;
@Component
public class Subscriber {
  
  private MqttClient client;
  
  public void connect(String broker, int port, String topic) throws MqttException {
    String clientId = "spring-ravi-sub" + System.currentTimeMillis() % 10000;
    System.out.println("== START SUBSCRIBER == " + clientId);
    
    client = new MqttClient("tcp://" + broker + ":" + port, clientId);
    
    MqttConnectOptions options = new MqttConnectOptions();
    options.setAutomaticReconnect(true);
    options.setCleanSession(true);
    options.setConnectionTimeout(30);
    options.setKeepAliveInterval(60); 
    
    options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);

    options.setUserName("nxon");
    options.setPassword("nxon1234".toCharArray());
    client.setCallback(new SimpleMqttCallBack());
    
    try {
      System.out.println("Connecting with credentials to MQTT broker...");
      client.connect(options);
      
      System.out.println("Subscribing to topic: " + topic);
      client.subscribe(topic, 1);
      System.out.println("Successfully subscribed to: " + topic);
    } catch (MqttException e) {
      System.out.println("Failed to connect or subscribe: " + e.getMessage());
      throw e;
    }
  }
  
  public void subscribeToTopic(String topic) throws MqttException {
    if (client != null && client.isConnected()) {
      System.out.println("Subscribing to additional topic: " + topic);
      client.subscribe(topic, 1);
      System.out.println("Successfully subscribed to: " + topic);
      
    } else {
      throw new MqttException(MqttException.REASON_CODE_CLIENT_NOT_CONNECTED);
    }
  }
  
  public void disconnect() throws MqttException {
    if (client != null && client.isConnected()) {
      client.disconnect();
      System.out.println("== SUBSCRIBER DISCONNECTED ==");
    }
  }
  
  public boolean isConnected() {
    return client != null && client.isConnected();
  }
}