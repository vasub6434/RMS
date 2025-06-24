package com.bonrix.dggenraterset.Repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bonrix.dggenraterset.Model.DashBoardLayout;
import com.bonrix.dggenraterset.Repository.BaseRepository;

@Repository
public interface DashBoardLayoutRepository extends BaseRepository<DashBoardLayout, Long> {

	@Modifying
	@Transactional
	@Query(value ="SELECT dlid, view,view_type, html_name, name,profile FROM dashboardlayout", nativeQuery = true)
	public List<Object[]> getAllDashBoardLayout();     
	

	@Query(value="SELECT dl.dlid,dl.name,dl.view,dl.view_type,dl.profile,dl.html_name FROM dashboardlayout dl join Assignmanagerlayout al on dl.dlid=al.dlid where al.managerid=?1",nativeQuery=true)
	public List<Object[]> getlayoutListByManager(long managerId);
	
	@Query(value="select dml.managerid,us.username from assignmanagerlayout dml join users us on dml.managerid=us.id where dml.dlid=?1",nativeQuery=true)
	public List<Object[]> getassManager(long dlid);
	
	@Query(value="select dml.profile,dml.dlid from dashboardlayout dml where dml.dlid=?1",nativeQuery=true)
	public List<Object[]> getassprofileManager(long dlid);
	 
	@Transactional
	@Modifying
	@Query(value="DELETE FROM assignmanagerlayout WHERE dlid=:dlid",nativeQuery=true)
	public void deleteAssManager(@Param("dlid") Long dlid);
	
	
	@Transactional
	@Modifying
	@Query(value="DELETE FROM assigndashboardusers WHERE managerdashid=:id",nativeQuery=true)
	public void deleteAssuser(@Param("id") Long id);
	
	@Transactional
	@Modifying
	@Query(value="DELETE FROM dashboardlayout WHERE dlid=:dlid",nativeQuery=true)
	public void deleteDashboard(@Param("dlid") Long dlid);
	
	
	@Query(value="SELECT dl.dlid,dl.name,dl.view,dl.view_type,dl.profile,dl.html_name FROM dashboardlayout dl join Assignmanagerlayout al on dl.dlid=al.dlid where al.managerid=?1",nativeQuery=true)
	public List<Object[]> getmanagerlayout(long managerId);
	
	
	@Query(value="SELECT dy.dyid,dl.dlid,dl.view,dl.view_type,dy.deviceid,dy.siteid,dl.profile FROM dashboardlayout dl join Assignmanagerlayout al on dl.dlid=al.dlid join dynamiclayout dy on dy.dlid=dl.dlid where dl.dlid=?1 and al.managerid=?2",nativeQuery=true)
	public List<Object[]> getdlayoutview(long dlid,long managerId);
	
	@Query(value="SELECT dl.dlid,dl.name,dl.view,dl.view_type,dl.profile,dl.html_name,dy.dyname,au.assid FROM assigndashboardusers au join dynamiclayout dy on au.managerdashid=dy.dyid join dashboardlayout dl on dl.dlid=dy.dlid where au.userid=?1",nativeQuery=true)
	public List<Object[]> getuserlayout(long userid);
	
	
	@Query(value="select dy.dyid,dl.dlid,dl.view,dl.view_type,dy.deviceid,dy.siteid,dl.profile from assigndashboardusers au join dynamiclayout dy on dy.dyid=au.managerdashid join dashboardlayout dl on dy.dlid=dl.dlid where dl.dlid=?1 and au.userid=?2",nativeQuery=true)
	public List<Object[]> getdulayoutview(long dlid,long managerId);
	
	@Query(value="select adu.userid,us.username from assigndashboardusers adu join users us on adu.userid=us.id where adu.managerdashid=?1",nativeQuery=true)
	public List<Object[]> getassuser(long mamagerdashid);
}