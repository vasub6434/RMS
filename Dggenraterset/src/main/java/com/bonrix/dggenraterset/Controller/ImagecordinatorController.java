package com.bonrix.dggenraterset.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bonrix.dggenraterset.Model.Imagecordinator;
import com.bonrix.dggenraterset.Model.SpringException;
import com.bonrix.dggenraterset.Service.ImagecordinatorServices;

@RestController
public class ImagecordinatorController {

	@Autowired
	ImagecordinatorServices imagecordinatorServices;
	
	@RequestMapping(method = RequestMethod.POST, value = "/admin/imagecordinator")
	@ExceptionHandler(SpringException.class)
	public String savedata(@RequestBody Imagecordinator bs) {
		System.out.println("jsxkaxbkz");
		imagecordinatorServices.save(bs);
		return new SpringException(true, "Parameter Sucessfully Added").toString();
		}

		@RequestMapping(value="/admin/imagecordinator" ,produces={"application/json"})
		public List<Imagecordinator> getparameterlist()
		{
			return imagecordinatorServices.getlist();
		}
	
		@RequestMapping(value="/admin/imagecordinator/{id}",method=RequestMethod.GET)
		public Imagecordinator getparameter(@PathVariable long id) {
			return imagecordinatorServices.get(id);
		}
		@RequestMapping(method=RequestMethod.PUT,value="admin/imagecordinator/{id}")
		public String updateparameter(@RequestBody Imagecordinator bs,@PathVariable long id)
		{
			bs.setNo(id);
			 imagecordinatorServices.save(bs);
			 return new SpringException(true, "Parameter Sucessfully Updated").toString();
		}
		
		@RequestMapping(method=RequestMethod.DELETE,value="/admin/imagecordinator/{id}")
		public String deleteparameter(@PathVariable long id)
		{
			return imagecordinatorServices.delete(id);
		}
		

}
