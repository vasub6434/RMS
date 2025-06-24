package com.bonrix.dggenraterset.Service;

import java.util.List;

import com.bonrix.dggenraterset.Model.AssignUserGroup;
import com.bonrix.dggenraterset.Model.UserGroup;

public interface UserGroupService {

	void save(UserGroup bs);

	void saveassigngrp(AssignUserGroup bs);

	public List<Object[]> getUserGroupList(long managId);

	String delete(Long id);

	String deleteGrpAssign(Long id);

	void deleteGroup(Long id);

	public List<Object[]> AssignedUsersByGrp(long grp_id);

	public List<Object[]> GetAssignedSiteByGrp(long grp_id);

	public List<Object[]> GetAssignedSiteByUser(long user_id);

}
