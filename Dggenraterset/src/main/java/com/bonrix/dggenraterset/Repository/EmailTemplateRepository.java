package com.bonrix.dggenraterset.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bonrix.dggenraterset.Model.EmailTemplate;
import com.bonrix.dggenraterset.Model.MessageTemplate;


public interface EmailTemplateRepository extends BaseRepository<EmailTemplate,Long>{

	
	@Query("from EmailTemplate where userid=:userid")
	public List<EmailTemplate> findByUser_id(@Param("userid") Long userid);

	@Query("from EmailTemplate where templatetype=:templatetype and userid=:uid OR createdby='ROLE_ADMIN'")
	public List<EmailTemplate> findByTemplatetype(@Param("templatetype") String templatetype,@Param("uid") Long uid);

}
