package com.bonrix.dggenraterset.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bonrix.dggenraterset.Model.Manager;

public interface ManagerRepository extends BaseRepository<Manager,Long>{
	
	@Query("from Manager where managername=:managername")
	public Manager findByManagerName(@Param("managername") String managername);
	
	/*@Query("SELECT u FROM User u WHERE u.status = ?1 and u.name = ?2")
	User findUserByUsernameAndPasword(Integer status, String name);*/
	
	@Query("from Manager where id=:managerid")
	public Manager findByManagerIdnew(@Param("managerid") long managerid);
	
	
//	public Manager findBymanagername(@Param("username") String username);
	
	@Query(value ="select * from managers", nativeQuery = true)
	public List<Manager> getAllManagerData();     
	
	@Query(value ="select * from managers", nativeQuery = true)
	public List<Object[]> getAllManagerDatanew();
	
	@Modifying
	@Query(value = "insert into user_roles (role, username) VALUES (?1, ?2)", nativeQuery = true)
	void saveRole(String role, long managername);
	
	
	@Modifying
	@Transactional
	@Query("delete from Manager u where u.id = ?1")
	void deleteManagersById(long managerid);

}
