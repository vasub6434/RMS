package com.bonrix.dggenraterset.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bonrix.dggenraterset.Model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	@Query("from User where username=:username")
	public User findByUserName(@Param("username") String username);
	
	@Query(value ="select * from user", nativeQuery = true)
	public List getAllUserData();      
	     
	/*@Query("SELECT u FROM User u WHERE u.status = ?1 and u.name = ?2")
	User findUserByUsernameAndPasword(Integer status, String name);*/
	
	@Modifying
	@Query(value = "insert into user_roles (role, username) VALUES (?1, ?2)", nativeQuery = true)
	void saveRole(String role, long username);
	
	
	@Query(value ="select id,enabled,password,username,contact,email,name from users where addedby=:addedby", nativeQuery = true)
	public List<Object[]> getAllUserDatanew(@Param("addedby") Long addedby);
	
	@Query(value ="select * from users", nativeQuery = true)
	public List<Object[]> getAllUserDataAdmin();
	
	@Query("from User where id=:userid")
	public User findByUserIdnew(@Param("userid") long userid);
	
	@Modifying
	@Transactional
	@Query("delete from User u where u.id = ?1")
	void deleteUsersById(long userid);

	@Query(value ="select id,username from users where addedby=?1", nativeQuery = true)
	public List getManagetList(long managerId);
	
	
	
	@Query("from User where addedby=:managerid")
	public User findByAddedby(@Param("managerid") long managerid);
	
	
	
	//ashishaj
	
	
	//get all managerlist 
	

	@Query(value ="select us.id,us.username,us.password,us.contact,us.email from public.users  us JOIN user_roles ur on us.id=ur.username where ur.role='ROLE_MANAGER'", nativeQuery = true)
	public List<Object[]> getAllmangerlist();
	
	
	
	@Query(value ="select us.id,us.username,ur.role from users us  JOIN user_roles ur on us.id=ur.username where us.username=:username AND us.password=:password", nativeQuery = true)
	public List<Object[]> getuserverify(@Param("username") String username,@Param("password") String password);
	
	@Modifying
	@Transactional
	@Query("delete from AssignUserDevice u where u.user_id = ?1")
	void deleteUserAssignDevice(long user_id);
  
	@Modifying
	@Query(value ="	UPDATE users SET master_password = :password WHERE id =:userid", nativeQuery = true)
	void newMasterPassword(@Param("userid") Long userid,@Param("password") String password);
	
	@Modifying
	@Query(value ="	UPDATE users SET enabled = :status WHERE id =:userid", nativeQuery = true)
	void updateStatus(@Param("userid") Long userid,@Param("status") boolean status);
	
	
	@Modifying
	@Query(value ="	UPDATE users SET password = :password WHERE id =:userid", nativeQuery = true)
	void updateManagerPassword(@Param("userid") Long userid,@Param("password") String password);
	
	
	@Modifying
	@Transactional
	@Query(value="delete from user_roles  where username = :userid", nativeQuery = true)
	void deleteUsersroleById(@Param("userid")long userid);
	
	@Modifying
	@Transactional
	@Query(value="delete from assignuserdevice  where user_id = :userid", nativeQuery = true)
	void deleteUsersDeviceById(@Param("userid")long userid);


}
