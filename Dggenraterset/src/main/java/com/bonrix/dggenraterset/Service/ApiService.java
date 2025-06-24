package com.bonrix.dggenraterset.Service;

import com.bonrix.dggenraterset.Model.Apikey;

public interface ApiService {
	
	void saveObject(Apikey api);
	
	Apikey findByuid(Long uid); 
	
	Apikey findBykeyValue(String key); 
	
	

}
