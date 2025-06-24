package com.bonrix.dggenraterset.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.Model.Componet;
import com.bonrix.dggenraterset.Model.SpringException;
import com.bonrix.dggenraterset.Repository.ComponetRepository;


@Service("Componentservice")
public class ComponentServicesImpl implements ComponentServices {
	
	@Autowired
	ComponetRepository repository;

	public List<Componet> getlist() {
		return repository.findAll();
	}

	public void save(Componet bs) {
		repository.save(bs);
		
	}

	public Componet get(Long id) {
		return repository.findOne(id);
	}

	public String delete(Long id) {
		 repository.delete(id);
		 return new SpringException(true, "Componet sucessfully Deleted").toString();
	}

	public String update(Componet bs) {
		 repository.saveAndFlush(bs);
		return new SpringException(true, "Componet sucessfully Updated").toString();
	}

}	
