package com.bonrix.dggenraterset.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.Model.Images;
import com.bonrix.dggenraterset.Model.SpringException;
import com.bonrix.dggenraterset.Repository.ImagesRepository;

@Service("Imagesservice")
public class ImageServicesImp implements ImageServices {

	@Autowired
	ImagesRepository repository;
	
	public List<Images> getlist() {
		return repository.findAll();
	}

	public void save(Images bs) {
		repository.save(bs);
	}

	public Images get(Long id) {
		return repository.findOne(id);
	}

	public String delete(Long id) {
		 repository.delete(id);
		 return new SpringException(true, "Reseller Details sucessfully Deleted").toString();
	}

	

}
