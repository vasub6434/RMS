package com.bonrix.dggenraterset.Service;

import java.util.List;

import com.bonrix.dggenraterset.Model.SystemParameter;


public interface SystemParameterServices {
	
	List<SystemParameter> getlist();

	void save(SystemParameter bs);

	SystemParameter get(Long id);

	String delete(Long id);

	String update(SystemParameter bs);

	/*List<SystemParameter> getlistByprmtype(String prmtype);*/

}
