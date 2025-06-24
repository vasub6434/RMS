package com.bonrix.dggenraterset.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.Model.MenuAllocationMst;
import com.bonrix.dggenraterset.Repository.MenuallocationmRepository;

@Service("menuallocationmService")
@SuppressWarnings("unchecked")
public class MenuallocationmServiceImp implements MenuallocationmService {

	@Autowired
	MenuallocationmRepository repository;
	
	@Override
	public void newMenu(MenuAllocationMst mst) {
		repository.save(mst);
	}
}
