package com.bonrix.dggenraterset.DTO;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EmiotsHashMap {
	
	public static Map<String, String> connectedClient = new ConcurrentHashMap<String,String>();
    private  static EmiotsHashMap instance;
    public static EmiotsHashMap getInstance()
     {
        synchronized (EmiotsHashMap.class)
        {
            if (instance == null)
            {
            	instance = new EmiotsHashMap();
            }
        }
       return instance;
    }
    public  String AddClient(String key,String value) 
    {   
     return connectedClient.put(key, value); 
     
    }
    
    public  String getClient(String key) 
    {   
     return connectedClient.get(key);                
    }
    
    public String RemoveClient(String key) 
    {   
     return connectedClient.remove(key);                
    }
    
    public int getClientSize() 
    {
        return connectedClient.size();
        
    }



}
