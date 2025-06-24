package com.bonrix.dggenraterset.Repository;

import java.util.List;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bonrix.dggenraterset.Model.DynamicDashBoardLayout;

@Repository
public interface DynamicDashBoardLayoutRepository extends BaseRepository<DynamicDashBoardLayout, Long> {

	@Query(value="SELECT dy.dyid, dy.deviceid,dl.name,dy.dyname,dy.siteid,dl.view,dl.view_type,dl.profile,dy.dlid FROM dynamiclayout dy JOIN dashboardlayout dl on dy.dlid=dl.dlid",nativeQuery=true)
	public List<Object[]> getdynamiclayoutList();
	
	
	@Query(value = "SELECT devicename FROM devicemaster   where  assignuserdevice.deviceid=?1", nativeQuery = true)
	public abstract List<Object[]> getMyDeviced(Long userId);

	
	@Query(value = "select dm.devicename from devicemaster dm where dm.deviceid=:deviceid", nativeQuery = true)
	public  List<Object[]> getdevicelist(@Param("deviceid") long deviceid);

	@Query(value = "SELECT site_name FROM site where siteid=?1", nativeQuery = true)
	public  List<Object[]> getSitelist(long siteid);
	
	@Query(value ="select id,enabled,password,username,contact,email,name from users where addedby=:addedby", nativeQuery = true)
	public List<Object[]> getAllUserData(@Param("addedby") Long addedby);
	
	@Transactional
	@Modifying
	@Query(value="DELETE FROM assignmanagerlayout WHERE dlid=:dlid",nativeQuery=true)
	public void deleteAssManager(@Param("dlid") Long dlid);
}