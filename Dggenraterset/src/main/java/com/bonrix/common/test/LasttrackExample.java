package com.bonrix.common.test;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;

public class LasttrackExample {
    public static void main(String[] args) throws Exception {
        // The JSON string you provided
        String jsonString = "{\"Analog\": [{\"9160775\": \"0\"}, {\"9160842\": \"0\"}, {\"9160931\": \"0\"}, {\"9160988\": \"0\"}, {\"9161078\": \"0\"}, {\"9161132\": \"0\"}, {\"9161269\": \"0\"}, {\"9161268\": \"0\"}, {\"9159066\": \"54.0\"}, {\"9159257\": \"0.0\"}, {\"6337570\": \"0.0\"}, {\"6337574\": \"0.0\"}, {\"6337577\": \"0.0\"}, {\"6337582\": \"0.0\"}, {\"9159712\": \"4800\"}, {\"6337588\": \"25\"}, {\"237921\": \"59\"}, {\"9160140\": \"9831\"}, {\"9160254\": \"45527\"}, {\"9160343\": \"1.4\"}, {\"9160363\": \"8.700000000000001\"}, {\"9160381\": \"10.3\"}, {\"9160395\": \"0.0\"}, {\"9160414\": \"0.0\"}, {\"9160441\": \"0\"}, {\"9160505\": \"0\"}, {\"9160520\": \"0\"}, {\"9160551\": \"0\"}, {\"6387981\": \"54.23\"}, {\"6387982\": \"0\"}, {\"5557109\": \"505.661\"}, {\"6308790\": \"1.3015625\"}, {\"5557111\": \"10438.275\"}, {\"6308792\": \"13.8875\"}, {\"5557117\": \"0.017\"}, {\"6308794\": \"0.0\"}, {\"5557118\": \"0.019\"}, {\"6308796\": \"0.0\"}, {\"5557124\": \"53.91\"}], \"Digital\": [{\"284945\": \"1\"}, {\"6348798\": \"1\"}, {\"291934\": \"1\"}, {\"6348854\": \"1\"}, {\"6348815\": \"1\"}, {\"6348821\": \"1\"}, {\"6348824\": \"1\"}], \"DeviceName\": \"Durga Colony- PBRPUJAL0661\"}";

        // Parse the JSON string using ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> data = objectMapper.readValue(jsonString, Map.class);

        // Get the analog data (List of Maps)
        List<Map<String, String>> analogData = (List<Map<String, String>>) data.get("Analog");
        
        // Iterate and process analog data
        for (Map<String, String> analogItem : analogData) {
            for (Map.Entry<String, String> entry : analogItem.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                System.out.println("Analog Key: " + key + ", Value: " + value);
            }
        }

        // Similarly, you can get and process digital data
        List<Map<String, String>> digitalData = (List<Map<String, String>>) data.get("Digital");
        for (Map<String, String> digitalItem : digitalData) {
            for (Map.Entry<String, String> entry : digitalItem.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                System.out.println("Digital Key: " + key + ", Value: " + value);
            }
        }

        // Device name
        String deviceName = (String) data.get("DeviceName");
        System.out.println("Device Name: " + deviceName);
    }
}
