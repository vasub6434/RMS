package com.bonrix.dggenraterset.Repository;

import java.util.List;

import com.bonrix.dggenraterset.Model.Parameter;
import com.bonrix.dggenraterset.Model.TagType;

public interface TagTypeRepository extends BaseRepository<TagType,Long>{

	List<TagType> findAll();
	
	
}
