package com.bonrix.dggenraterset.DTO;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MODBUSDataHashMap {
	
	public static Map<String, LogObject> connectedClient = new ConcurrentHashMap<String, LogObject>();
	public static Map<String, LogObject> otpauth = new ConcurrentHashMap<String, LogObject>();
    private  static MODBUSHashMap instance;
    
    public static MODBUSHashMap getInstance()
     {  
        synchronized (DataHashMap.class)  
        {
            if (instance == null)
            {
            	instance = new MODBUSHashMap();
            }
        }
       return instance;
    }
    
    public  LogObject AddClient(String key,LogObject value) 
    {   
     return connectedClient.put(key, value); 
    }
    
    public  LogObject getClient(String key) 
    {   
     return connectedClient.get(key);                
    }
    
    public  LogObject RemoveClient(String key) 
    {   
     return connectedClient.remove(key);                
    }
    
    public int getClientSize() 
    {
        return connectedClient.size();
    }

}
