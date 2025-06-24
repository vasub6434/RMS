package com.bonrix.dggenraterset.Service;

import java.util.List;

import com.bonrix.dggenraterset.Model.Componet;


public interface ComponentServices {
	
	List<Componet> getlist();

	void save(Componet bs);

	Componet get(Long id);

	String delete(Long id);

	String update(Componet bs);

}
