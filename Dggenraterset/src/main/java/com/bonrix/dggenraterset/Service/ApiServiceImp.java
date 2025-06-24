package com.bonrix.dggenraterset.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.Model.Apikey;
import com.bonrix.dggenraterset.Repository.ApiRepositiry;

@Service("apiServiceImp")
public class ApiServiceImp implements ApiService {

	@Autowired
	ApiRepositiry repository;

	@Override
	public void saveObject(Apikey api) {
		repository.saveAndFlush(api);

	}

	@Override
	public Apikey findByuid(Long uid) {
		return repository.findByuid(uid);
	}

	@Override
	public Apikey findBykeyValue(String key) {
		return repository.findBykeyValue(key);
	}

}
