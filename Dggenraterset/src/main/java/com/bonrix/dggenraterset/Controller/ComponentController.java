package com.bonrix.dggenraterset.Controller;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bonrix.dggenraterset.Model.Componet;
import com.bonrix.dggenraterset.Model.SpringException;
import com.bonrix.dggenraterset.Service.ComponentServices;

@Transactional
@RestController
public class ComponentController {
	
	@Autowired
	ComponentServices Componentservice;
	
	// set Content-Type=application/json ANd set raw={"macaddress":"asuiags", "scannerName":"asuiags","latlang":"asuiags", "cordinate":"asuiags" }(here put key and value and pass arry of it)
		@RequestMapping(method=RequestMethod.POST,value="/api/componet")@ExceptionHandler(SpringException.class)
		public String savedata(@RequestBody Componet bs)
		{ 
			Componentservice.save(bs);
			return new SpringException(true, "Componet Sucessfully Added").toString();
		}
		
		@RequestMapping(value="/api/componet" ,produces={"application/json"})
		public List<Componet> getcomponetlist()
		{
			return Componentservice.getlist();
		}
		
			@RequestMapping(value="/api/componet/{id}",method=RequestMethod.GET)
			public Componet getcomponet(@PathVariable long id) {
				return Componentservice.get(id);
			}
		
		 //set Content-Type=application/json ANd set raw=raw={"macaddress":"asuiags", "scannerName":"asuiags","latlang":"asuiags", "cordinate":"asuiags" }(here put key and value and pass arry of it)
			@RequestMapping(method=RequestMethod.PUT,value="/api/componet/{id}")
			public String updatecomponet(@RequestBody Componet bs,@PathVariable long id)
			{
				bs.setId(id);
				return Componentservice.update(bs);
			}
			
			@RequestMapping(method=RequestMethod.DELETE,value="/api/componet/{id}")
			public String deletecomponet(@PathVariable long id)
			{
				return Componentservice.delete(id);
			}

}
