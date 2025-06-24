package com.bonrix.dggenraterset.Service;

import java.util.List;

import com.bonrix.dggenraterset.Model.MenuAllocationMst;
import com.bonrix.dggenraterset.Model.MenuMst;


public interface MenuService {
	
	List<Object[]> getMenu(long userId);
	
	void deleteMenu(long userId);
	
	List<Object[]> getUsermenu(long userId);
	
	void deleteUserMenu(long userId);
	
	MenuMst findByMid(Long mid);
}
