package com.bonrix.dggenraterset.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.Model.Manager;
import com.bonrix.dggenraterset.Repository.ManagerRepository;


@Service("managerService")
public class ManagerServiceImpl implements ManagerService{
	
	@Autowired
	ManagerRepository managerrepository;

	@Override
	public Manager savemanager(Manager manager) {
		return managerrepository.saveAndFlush(manager);
	}

	@Override
	public Manager getmanagerbyid(long mngrid) {
		return managerrepository.findByManagerIdnew(mngrid);
	}

	@Override
	public Manager getmanagerbymanagername(String mngrname) {
		return managerrepository.findByManagerName(mngrname);
	}

	@Override
	public void savemanager(String role, long mngrname) {
		managerrepository.saveRole(role, mngrname);
	}

	@Override
	public List<Manager> getManagerlist() {
		return managerrepository.getAllManagerData();
	}

	@Override
	public List<Object[]> getManagerlistnew() {
		return managerrepository.getAllManagerDatanew();
	}

	@Override
	public List<Manager> getlist() {
		return managerrepository.findAll();
	}

	@Override
	public void update(Manager manager) {
		managerrepository.saveAndFlush(manager);
	}

	@Override
	public void deleteManagersById(long mngrid) {
		managerrepository.deleteManagersById(mngrid);
	}

	

}
