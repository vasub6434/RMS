//package com.bonrix.MQTTtest;
//
//import org.eclipse.paho.client.mqttv3.MqttClient;
//import org.eclipse.paho.client.mqttv3.MqttException;
//import org.eclipse.paho.client.mqttv3.MqttMessage;
//
//public class Publisher {
//
//
//  public void Publishermqtt() throws MqttException {
//
//    String messageString = "Hello Server from Publisher!";
//  
//    System.out.println("== START PUBLISHER ==");
//
//    MqttClient client = new MqttClient("tcp://nodered-mqtt.bonrix.in::21883", MqttClient.generateClientId());
//    client.connect();
//    MqttMessage message = new MqttMessage();
//    message.setPayload(messageString.getBytes());
//    client.publish("Ravi", message);
//    System.out.println("\tMessage '"+ messageString +"' to 'iot_data'");
//    client.disconnect();
//    System.out.println("== END PUBLISHER ==");
//
//  }
//
//
//}

package com.bonrix.MQTTtest;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;
@Component
public class Publisher {
  
  public void publishMessage(String topic, String message) throws MqttException {
    System.out.println("== START PUBLISHER ==");
    
    String clientId = "spring-ravi" + System.currentTimeMillis() % 10000;
    MqttClient client = new MqttClient("tcp://nodered-mqtt.bonrix.in:21883", clientId);
    
    MqttConnectOptions options = new MqttConnectOptions();
    options.setAutomaticReconnect(false);
    options.setCleanSession(true);
    options.setConnectionTimeout(30);
    
    options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
    
    options.setUserName("nxon");
    options.setPassword("nxon1234".toCharArray());
    
    try {
      System.out.println("Connecting to MQTT broker with credentials...");
      client.connect(options);
      
      MqttMessage mqttMessage = new MqttMessage();
      mqttMessage.setPayload(message.getBytes());
      client.publish(topic, mqttMessage);
      
      System.out.println("\tMessage '" + message + "' sent to topic '" + topic + "'");
    } finally {
      if (client.isConnected()) {
        client.disconnect();
        System.out.println("== END PUBLISHER ==");
      }
    }
  }
  
  public void Publishermqtt() throws MqttException {
    publishMessage("ravi", "Hello Server from Publisher!");
  }
}