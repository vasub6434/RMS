package com.bonrix.dggenraterset.Service;

import java.util.List;

import com.bonrix.dggenraterset.Model.EmailTemplate;

public interface EmailService {

	public EmailTemplate saveemailtemplate(EmailTemplate emailtemplate);

	public abstract List<EmailTemplate> getemaillistByUser_id(Long userid);

	public void updateemailtemplate(EmailTemplate emailtemplate);

	String deleteemailtemplate(Long eid);
}
