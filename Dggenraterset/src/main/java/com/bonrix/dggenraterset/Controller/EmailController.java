package com.bonrix.dggenraterset.Controller;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bonrix.dggenraterset.Model.EmailTemplate;
import com.bonrix.dggenraterset.Model.SpringException;
import com.bonrix.dggenraterset.Service.EmailService;

@CrossOrigin(origins = "*")
@Transactional
@RestController
public class EmailController {

	@Autowired
	EmailService emlSerive;

	@RequestMapping(value = "/api/emailtemp/{userid}", produces = { "application/json" }, method = RequestMethod.GET)
	public List<EmailTemplate> getemaillistByuserid(@PathVariable Long userid) {
		return emlSerive.getemaillistByUser_id(userid);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/api/saveemailtemplate")
	@ExceptionHandler(SpringException.class)
	public String saveemailtemplate(@RequestBody EmailTemplate emailtemplate) {
		emlSerive.saveemailtemplate(emailtemplate).getEid();
		return new SpringException(true, "Email Template Sucessfully Saved!").toString();
	}

	@RequestMapping(method = RequestMethod.GET, value = "api/updateemailtemplate/{eid}/{templatename}/{templatetype}/{email}/{emailsubject}/{userid}/{role}")
	public String updateetemplate(@PathVariable long eid, @PathVariable String templatename,
			@PathVariable String templatetype, @PathVariable String email, @PathVariable String emailsubject,
			@PathVariable long userid, @PathVariable String role) {
		EmailTemplate emailt = new EmailTemplate();
		emailt.setCreatedby(role);
		emailt.setTemplatename(templatename);
		emailt.setTemplatetype(templatetype);
		emailt.setUserid(userid);
		emailt.setEmailsubject(emailsubject);
		emailt.setEmail(email);
		emailt.setEid(eid);
		emlSerive.updateemailtemplate(emailt);
		return new SpringException(true, "EmailTemplate Sucessfully Updated!").toString();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/api/deletemailtemplate/{eid}")
	public String deletemailtemp(@PathVariable long eid) {
		return emlSerive.deleteemailtemplate(eid);
	}

}
