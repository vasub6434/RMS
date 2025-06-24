package com.bonrix.dggenraterset.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bonrix.dggenraterset.Model.MenuMst;

@Repository
public interface MenuRepositiry extends BaseRepository<MenuMst, Long> {
	
	
	@Query(value ="SELECT mid, menuname, menuurl FROM menumst where mid=?1", nativeQuery = true)
	public List GetAllocatedMenu(long userId);
	
	@Query(value = "SELECT menumst.mid, menumst.menuname, menumst.menuurl, menumst.role, menuallocationmst.manager_id FROM public.menumst left join menuallocationmst on menumst.mid=menuallocationmst.mid and menuallocationmst.manager_id=?1 where menumst.role='ROLE_MANAGER' order by mid asc ", nativeQuery = true)
	public List<Object[]> GetAllMenu(long userId);
	
	@Modifying
	@Transactional
	@Query(value = "delete from menuallocationmst where menuallocationmst.manager_id=?1", nativeQuery = true)
	public void deleterMenu(long userId);
 
	@Modifying
	@Query(value = "insert into menuallocationmst(manager_id,mid,user_id) values(?1,?2,?3)", nativeQuery = true)
	public void newMenu(long managetId,long menuId,long userId);
	
//	@Query(value = "SELECT menumst.mid, menumst.menuname, menumst.menuurl, menumst.role, menuallocationmst.user_id FROM public.menumst left join menuallocationmst on menumst.mid=menuallocationmst.mid and menuallocationmst.user_id=?1 where menumst.role='ROLE_USER' order by mid asc", nativeQuery = true)
//	public List<Object[]> getUsermenu(long userId);
	
	@Query(value = "SELECT menumst.mid, menumst.menuname, menumst.menuurl, menumst.role, menuallocationmst.user_id FROM public.menumst inner join menuallocationmst on menumst.mid=menuallocationmst.mid and menuallocationmst.user_id=?1 where menumst.role='ROLE_USER' order by mid asc", nativeQuery = true)
	public List<Object[]> getUsermenu(long userId);
	
	@Modifying
	@Transactional
	@Query(value = "delete from menuallocationmst where menuallocationmst.user_id=?1", nativeQuery = true)
	public void deleteUserMenu(long userId);
 
	@Query(value = "SELECT * FROM menumst WHERE mid = :mid", nativeQuery = true)
	Optional<MenuMst> findByMenuId(@Param("mid") Long mid);


}
