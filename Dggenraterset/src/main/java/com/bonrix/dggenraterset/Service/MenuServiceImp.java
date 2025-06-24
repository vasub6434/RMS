package com.bonrix.dggenraterset.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.Model.MenuAllocationMst;
import com.bonrix.dggenraterset.Model.MenuMst;
import com.bonrix.dggenraterset.Repository.MenuRepositiry;


@Service("menuService")
@SuppressWarnings("unchecked")
public class MenuServiceImp implements MenuService {

	
	@Autowired
	MenuRepositiry repository;
	
	
	@Override
	public List<Object[]> getMenu(long userId) {
			return repository.GetAllMenu(userId);
	}


	@Override
	public void deleteMenu(long userId) {
		 repository.deleterMenu(userId);
		
	}



	@Override
	public List<Object[]> getUsermenu(long userId) {
		return repository.getUsermenu(userId);
	}


	@Override
	public void deleteUserMenu(long userId) {
		 repository.deleteUserMenu(userId);
		
	}

	@Override
	public MenuMst findByMid(Long mid) {
	    return repository.findByMenuId(mid).orElse(null);
	}
}
