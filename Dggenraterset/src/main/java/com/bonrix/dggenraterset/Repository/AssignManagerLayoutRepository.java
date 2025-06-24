package com.bonrix.dggenraterset.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bonrix.dggenraterset.Model.AssignManagerLayout;

@Repository
public interface AssignManagerLayoutRepository extends BaseRepository<AssignManagerLayout, Long> {
	
		@Query(value="SELECT prid,profilename FROM deviceprofile",nativeQuery=true)
		public List<Object[]> getProfilelist();
		
		@Query(value="SELECT dp.prid,dp.profilename FROM public.dashboardlayout dl join deviceprofile dp  on dp.prid=dl.profile  where dl.profile=?1",nativeQuery=true)
		public List<Object[]> getprofileByid(long pr_id);
		
		@Query(value="delete from dashboardlayout u where u.dlid = ?1",nativeQuery=true)
		void deleteSiteAssignId(Long dlid);
		
		@Query(value="select DISTINCT  deviceid,devicename from  devicemaster left join assignuserdevice on devicemaster.deviceid = assignuserdevice.device_id where assignuserdevice.manager_id=?1",nativeQuery=true)
		public List<Object[]> getDeviceList(long managerid);
				
}