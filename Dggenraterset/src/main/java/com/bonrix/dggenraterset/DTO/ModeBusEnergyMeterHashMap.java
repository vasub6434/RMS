package com.bonrix.dggenraterset.DTO;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModeBusEnergyMeterHashMap {
	
	public static Map<String, LogObject> connectedClient = new ConcurrentHashMap<String, LogObject>();
	public static Map<String, LogObject> otpauth = new ConcurrentHashMap<String, LogObject>();
    private  static ModeBusEnergyMeterHashMap instance;
    
    public static ModeBusEnergyMeterHashMap getInstance()
     {  
        synchronized (ModeBusEnergyMeterHashMap.class)  
        {
            if (instance == null)
            {
            	instance = new ModeBusEnergyMeterHashMap();
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
