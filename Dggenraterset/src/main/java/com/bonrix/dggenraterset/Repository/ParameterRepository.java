package com.bonrix.dggenraterset.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bonrix.dggenraterset.Model.Parameter;

public interface ParameterRepository extends BaseRepository<Parameter,Long>{

	@Query("from Parameter where prmtype=:prmtype")
	public List<Parameter> findByPrmtype(@Param("prmtype") String prmtype);
	
	public Parameter findByPrmname(String prmname);
	
	public Parameter findByid(long id);
	
	@Query(value ="select analogs ->>'Analogunit' analogunit  from public.deviceprofile,jsonb_array_elements(parameters->'Analog') analogs where analogs->>'Analoginput'=:count AND prid=:deviceId", nativeQuery = true)
	public String getLasttrackUnitsNew(@Param("deviceId") long deviceId,@Param("count") String count);
	
	@Query(value ="SELECT device_date,system_date,text(digitaldata),devicename from lasttrack join devicemaster on device_id= deviceid where device_id=?1", nativeQuery = true)
	public List<Object[]> getLasttrackByDeviceId(long deviceId);
	
	@Query(value ="SELECT devicename,prid_fk from  devicemaster where deviceid=?1", nativeQuery = true)
	public List<Object[]> getDeviceProfileByDeviceId(long deviceId);

	
}
