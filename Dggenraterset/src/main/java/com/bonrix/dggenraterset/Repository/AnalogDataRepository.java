package com.bonrix.dggenraterset.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bonrix.dggenraterset.Model.Analogdata;
import com.bonrix.dggenraterset.Model.Devicemaster;

@Repository
public interface AnalogDataRepository extends BaseRepository<Analogdata,Integer> {

	Analogdata findBydevice(Devicemaster device);
	
	@Query(value = "SELECT id, prmname, prmtype FROM parameter where prmtype='Analog'", nativeQuery = true)
	public List<Object[]> getAnalogdatalist();
	
	@Query(value = "SELECT id, prmname, prmtype FROM parameter where prmtype='Digital'", nativeQuery = true)
	public List<Object[]> getDigitaldatalist();
}
