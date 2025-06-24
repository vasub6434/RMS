package com.bonrix.dggenraterset.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bonrix.dggenraterset.Model.MessageTemplate;
import com.bonrix.dggenraterset.Model.Tag;

public interface TagRepository extends BaseRepository<Tag,Long>{

	@Query("from Tag where tag=:tag")
	public List<Tag> findByTag(@Param("tag") String tag);

}
