package com.bonrix.dggenraterset.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.Model.EmailTemplate;
import com.bonrix.dggenraterset.Model.SpringException;
import com.bonrix.dggenraterset.Repository.EmailTemplateRepository;


@Service("emailService")
public class EmailServiceImp implements EmailService {

	@Autowired
	EmailTemplateRepository emailrepository;

	@Override
	public EmailTemplate saveemailtemplate(EmailTemplate emailtemplate) {
		return emailrepository.saveAndFlush(emailtemplate);
	}

	@Override
	public List<EmailTemplate> getemaillistByUser_id(Long userid) {
		return emailrepository.findByUser_id(userid);
	}

	@Override
	public void updateemailtemplate(EmailTemplate emailtemplate) {
		emailrepository.saveAndFlush(emailtemplate);

	}

	@Override
	public String deleteemailtemplate(Long eid) {
		emailrepository.delete(eid);
		return new SpringException(true, "Emailtemplate Sucessfully Deleted").toString();
	}
}
