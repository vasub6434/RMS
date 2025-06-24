package com.bonrix.dggenraterset.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bonrix.dggenraterset.Model.MessageTemplate;
import com.bonrix.dggenraterset.Model.Parameter;

public interface MessageTemplateRepository extends BaseRepository<MessageTemplate, Long> {
	@Query("from MessageTemplate where userid=:userid")
	public List<MessageTemplate> findByUser_id(@Param("userid") Long userid);
	
	
	
	@Modifying
	@Query(value = "insert into messagetemplate (templatename, message,templatetype,userid,createdby) VALUES (?1, ?2, ?3, ?4, ?5)", nativeQuery = true)
	void savemsgtemplate(String templatename,String message,String templatetype, long user_id,String createdby);


	@Query("from MessageTemplate where templatetype=:templatetype  and userid=:uid   OR createdby='ROLE_ADMIN' ")
	public List<MessageTemplate> findByTemplatetype(@Param("templatetype") String templatetype,@Param("uid") Long uid);
	
}
