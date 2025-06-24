package com.bonrix.dggenraterset.Service;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.Model.DisplaySetting;
import com.bonrix.dggenraterset.Repository.DisplaySettingRepository;

@Service("displaysettingservice")
public class DisplaySettingSeviceImpl implements DisplaySettingService{
	
	/*@Autowired
	SessionFactory sessionFactory;*/
	
	@Autowired
	DisplaySettingRepository displaysettingrepo;

	@Override
	public void save(DisplaySetting ds) {
		displaysettingrepo.save(ds);
	}

	@Override
	public List<Object[]> DisplaySettingList(String hostName) {
		return displaysettingrepo.DisplaySettingList(hostName);
	}

	@Override
	public List<Object[]> AdminDisplaySettingList() {
		return displaysettingrepo.AdminDisplaySettingList();
	}

	@Override
	public String delete(Long id) {
		displaysettingrepo.delete(id);
		return "1";
	}

	/*@Override
	public Object getSingleObject(String query) {
		Query query2 = sessionFactory.getCurrentSession().createQuery(query);
		return query2.uniqueResult();
	}*/

	@Override
	public String updateUserPass(String userName, String mobileNum, String pass) {
		return displaysettingrepo.updateUserPass(userName, mobileNum, pass);
	}

	@Override
	public List<Object[]> getUserBymobile(String userName, String mobileNum) {
		return displaysettingrepo.getUserBymobile(userName, mobileNum);
	}

}
