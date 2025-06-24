package com.bonrix.dggenraterset.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.Model.AssignUserGroup;
import com.bonrix.dggenraterset.Model.UserGroup;
import com.bonrix.dggenraterset.Repository.AssignUserGroupRepository;
import com.bonrix.dggenraterset.Repository.UserGroupRepository;

@Service("UserGroupservices")
public class UserGroupServiceImpl implements UserGroupService {

	@Autowired
	UserGroupRepository repository;

	@Autowired
	AssignUserGroupRepository assgnrepository;

	@Override
	public void save(UserGroup bs) {
		repository.save(bs);
	}

	@Override
	public void saveassigngrp(AssignUserGroup bs) {
		assgnrepository.save(bs);
	}

	@Override
	public List<Object[]> getUserGroupList(long managId) {
		return repository.getUserGroupList(managId);
	}

	@Override
	public String delete(Long id) {
		repository.delete(id);
		return "Successfully Deleted";
	}

	@Override
	public String deleteGrpAssign(Long id) {
		assgnrepository.deleteUserGroup(id);
		return "1";
	}

	@Override
	public void deleteGroup(Long id) {
		repository.delete(id);
	}

	@Override
	public List<Object[]> AssignedUsersByGrp(long grp_id) {
		return assgnrepository.AssignedUsersByGrp(grp_id);
	}

	@Override
	public List<Object[]> GetAssignedSiteByGrp(long grp_id) {
		return assgnrepository.GetAssignedSiteByGrp(grp_id);
	}

	@Override
	public List<Object[]> GetAssignedSiteByUser(long user_id) {
		return assgnrepository.GetAssignedSiteByUser(user_id);
	}

}
