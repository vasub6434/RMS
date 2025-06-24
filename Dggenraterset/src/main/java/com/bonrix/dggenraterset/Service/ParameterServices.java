package com.bonrix.dggenraterset.Service;

import java.util.List;

import com.bonrix.dggenraterset.Model.Parameter;

public interface ParameterServices {
	
	List<Parameter> getlist();

	void save(Parameter bs);

	Parameter get(Long id);

	String delete(Long id);

	String update(Parameter bs);

	List<Parameter> getlistByprmtype(String prmtype);

    List<Object[]> getLasttrackByDeviceId(long deviceId);
	
	String getLasttrackUnits(long deviceId,String count);
	
	List<Object[]> getDeviceProfileByDeviceId(long deviceId);
}
