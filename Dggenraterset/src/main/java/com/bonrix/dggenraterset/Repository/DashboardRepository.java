package com.bonrix.dggenraterset.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bonrix.dggenraterset.Model.Dashboarddetails;

@Repository
public interface DashboardRepository extends BaseRepository<Dashboarddetails,Long>{

	
	
}
