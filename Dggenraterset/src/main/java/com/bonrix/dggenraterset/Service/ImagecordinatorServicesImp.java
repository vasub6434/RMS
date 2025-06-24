package com.bonrix.dggenraterset.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.Model.Imagecordinator;
import com.bonrix.dggenraterset.Model.SpringException;
import com.bonrix.dggenraterset.Repository.ImagecordinatorRepository;

@Service
public class ImagecordinatorServicesImp implements ImagecordinatorServices {
	
	ImagecordinatorRepository repository;

	@Override
	public List<Imagecordinator> getlist() {
		return repository.findAll();
	}

	@Override
	public void save(Imagecordinator bs) {
		repository.saveAndFlush(bs);
	}

	@Override
	public Imagecordinator get(Long id) {
		return repository.findOne(id);
	}

	@Override
	public String delete(Long id) {
		 repository.delete(id);
		 return new SpringException(true, "Image Cordinate Deleted").toString();
	}

}