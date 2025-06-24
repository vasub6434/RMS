package com.bonrix.dggenraterset.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bonrix.dggenraterset.Model.DeviceProfile;
import com.bonrix.dggenraterset.Model.Images;
import com.bonrix.dggenraterset.Service.DevicemasterServices;
import com.bonrix.dggenraterset.Service.DeviceProfileServices;
import com.bonrix.dggenraterset.Service.ImageServices;

@RestController
public class ImageController {

	@Autowired
	ImageServices Imagesservice;
	@Autowired
	DeviceProfileServices Deviceprofileservices;
	 
	@Autowired
	DevicemasterServices deviceinfoservices;
	
	
	@RequestMapping(value = "/admin/image", produces = { "application/json" })
	public List<Images> getStudentdetailslist() throws Exception {
		return Imagesservice.getlist();
	}
	@RequestMapping(value = "/admin/componetname", produces = { "application/json" })
	public List<Images> getComponetlist() throws Exception {
		return Imagesservice.getlist();
	}
	@RequestMapping(value = "/admin/image/{id}", method = RequestMethod.GET)
	public Images getImagesdata(@PathVariable Long id) {
		return Imagesservice.get(id);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/admin/image/{id}")
	public String deleteImagesdata(@PathVariable Long id) {
		return Imagesservice.delete(id);
	}
	
	@RequestMapping(value = "/admin/profilename", produces = { "application/json" })
	public List<DeviceProfile> getprofilename() throws Exception {
		System.out.println("asfdg");
		return Deviceprofileservices.getlist();
	}
	
	
	
}
