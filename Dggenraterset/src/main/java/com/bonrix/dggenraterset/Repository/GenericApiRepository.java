package com.bonrix.dggenraterset.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bonrix.dggenraterset.Model.Parameter;

@Repository
public interface GenericApiRepository extends JpaRepository<Parameter, Long>{
	
	List<Parameter> findByIdIn(List<Long> ids);
	
	
}
