package com.bonrix.dggenraterset.DTO;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DGHashMap {
	
	public static Map<String, WebSocketObj> connectedClient = new ConcurrentHashMap<String, WebSocketObj>();
	
	public static Map<String, WebSocketObj> otpauth = new ConcurrentHashMap<String, WebSocketObj>();
    private  static DGHashMap instance;
//com.bonrix.sms.utils.CRMHashMap map=null;   
    public static DGHashMap getInstance()
     {  
        synchronized (DGHashMap.class)
        {
            if (instance == null)
            {
            	instance = new DGHashMap();
            }
        }
       return instance;
    }
    public  WebSocketObj AddClient(String key,WebSocketObj value) 
    {   
    	
     return connectedClient.put(key, value); 
     
    }
    
    public  WebSocketObj getClient(String key) 
    {   
     return connectedClient.get(key);                
    }
    
    public  WebSocketObj RemoveClient(String key) 
    {   
     return connectedClient.remove(key);                
    }
    
    public int getClientSize() 
    {
        return connectedClient.size();
        
    }


}
