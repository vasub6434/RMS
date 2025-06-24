package com.bonrix.dggenraterset.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bonrix.dggenraterset.Model.PoiData;

@Repository
public interface PoiRepository extends JpaRepository<PoiData, Long> {

	@Query(value = "SELECT * FROM poi_data WHERE poi_id = :id AND manager_id = :managerId", nativeQuery = true)
	PoiData findPoiByIdAndManagerId(@Param("id") Long id, @Param("managerId") Long managerId);

	@Query(value = "SELECT * FROM poi_data WHERE manager_id = :managerId", nativeQuery = true)
	List<PoiData> findAllByManagerId(@Param("managerId") Long managerId);

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM poi_data WHERE poi_id = :id AND manager_id = :managerId", nativeQuery = true)
	int deletePoiByIdAndManagerId(@Param("id") Long id, @Param("managerId") Long managerId);
	
	@Query(value = "SELECT * FROM poi_data WHERE manager_id = :managerId AND location = :poiName",nativeQuery = true) 
	List<PoiData> getPoiByManager(@Param("managerId") Long managerId, @Param("poiName") String poiName);
}
