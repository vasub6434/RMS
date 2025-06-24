package com.bonrix.dggenraterset.Controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.bonrix.dggenraterset.Model.Componet;
import com.bonrix.dggenraterset.Model.Images;
import com.bonrix.dggenraterset.Model.SpringException;
import com.bonrix.dggenraterset.Service.ComponentServices;
import com.bonrix.dggenraterset.Service.DeviceProfileServices;
import com.bonrix.dggenraterset.Service.ImageServices;

@Controller
public class ViewController 
{
	@Autowired
	ImageServices Imagesservice;
	
	@Autowired
	ComponentServices Componentservice;
	@Autowired
	DeviceProfileServices Deviceprofileservices;
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView homepage() 
	{
		return new ModelAndView("login");
	}
	
	@RequestMapping(value = "/liveloggg", method = RequestMethod.GET)
	public ModelAndView livelogpage() 
	{
		return new ModelAndView("resources/livelog");
	}
	
	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public ModelAndView welcome() 
	{
		return new ModelAndView("Dashboard");
	}
	


	@RequestMapping(value = "/welcome-user", method = RequestMethod.GET)
	public ModelAndView defaultPage() {

		ModelAndView model = new ModelAndView();
		model.addObject("title", "Spring Security + Hibernate Example");
		model.addObject("message", "This is default page!");
		model.setViewName("pages/hello");
		return model;

	}

	@RequestMapping(value = "/admin**", method = RequestMethod.GET)
	public ModelAndView adminPage() {

		ModelAndView model = new ModelAndView();
		model.addObject("title", "Spring Security + Hibernate Example");
		model.addObject("message", "This page is for ROLE_ADMIN only!");
		model.setViewName("pages/admin");

		return model;

	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, HttpServletRequest request) {

		ModelAndView model = new ModelAndView();
		if (error != null) {
			model.addObject("error", "Invalid username and password!");
		}

		if (logout != null) {
			model.addObject("msg", "You've been logged out successfully.");
		}
		model.setViewName("login");

		return model;

	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/adddimage")
	@ExceptionHandler(SpringException.class)
	public @ResponseBody String savedata(HttpServletRequest request,
            HttpServletResponse response) {
		//request.getParameter("id");
		System.out.println("in servlet...");
		MultipartHttpServletRequest mRequest;
        try {
        	
            mRequest = (MultipartHttpServletRequest) request;
            mRequest.getParameterMap();
            Iterator<String> itr = mRequest.getFileNames();
            System.out.println( mRequest.getFileNames().hasNext());
            System.out.println("imgname"+mRequest.getParameter("imgname"));
            String imagename=mRequest.getParameter("imgname");
            while (itr.hasNext()) 
            {
                MultipartFile mFile = mRequest.getFile(itr.next());
                String fileName = mFile.getOriginalFilename();
                String SEP = System.getProperty("file.separator");
         String filePath=request.getServletContext().getRealPath("/")+SEP+"images"+SEP;
      	fileName = fileName.replaceAll("\\s+", "");
      	byte[] bytes = mFile.getBytes();
      	BufferedOutputStream buffStream = new BufferedOutputStream(
      	new FileOutputStream(new File(filePath + fileName)));
      	buffStream.write(bytes);
      	buffStream.close();
      	System.out.println(filePath);
      	System.out.println(fileName);
                Images imgg=new Images();
                imgg.setImage(fileName);
                imgg.setImagename(imagename);
                Imagesservice.save(imgg);
                return new SpringException(true, "Images Sucessfully uploaded").toString();
            }
        } catch (Exception e) 
        {
            e.printStackTrace();
            return null;
        }
		return null;
		
		
	}
	


	

	// for 403 access denied page
	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public ModelAndView accesssDenied() {
		ModelAndView model = new ModelAndView();
		// check if user is login
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			UserDetails userDetail = (UserDetails) auth.getPrincipal();
			System.out.println(userDetail);
			model.addObject("username", userDetail.getUsername());
		}

		model.setViewName("pages/403");
		return model;

	}


	
	
	@RequestMapping(method = RequestMethod.POST, value = "/uploadcompimage")
	@ExceptionHandler(SpringException.class)
	public @ResponseBody String savecompdata(HttpServletRequest request,
            HttpServletResponse response) {
		//"/uploadcompimage/{id}"
		System.out.println("in servlet...");
		MultipartHttpServletRequest mRequest;
        try {
            mRequest = (MultipartHttpServletRequest) request;
            mRequest.getParameterMap();
            Iterator<String> itr = mRequest.getFileNames();
            System.out.println( mRequest.getFileNames().hasNext());
            while (itr.hasNext()) 
            {
                MultipartFile mFile = mRequest.getFile(itr.next());
                String fileName = mFile.getOriginalFilename();
                System.out.println("fileName::"+fileName);
                String SEP = System.getProperty("file.separator");
         String filePath=request.getServletContext().getRealPath("/")+SEP+"compimages"+SEP;
      	fileName = fileName.replaceAll("\\s+", "");
      	byte[] bytes = mFile.getBytes();
      	BufferedOutputStream buffStream = new BufferedOutputStream(
      	new FileOutputStream(new File(filePath + fileName)));
      	buffStream.write(bytes);
      	buffStream.close();
      	System.out.println(filePath);
      	System.out.println(fileName);
	      	Componet Comp =	Componentservice.get(Long.parseLong(mRequest.getParameter("id")));
	      	Comp.setImage(fileName);
	      	Componentservice.update(Comp);
           return new SpringException(true, "Images Sucessfully uploaded").toString();
            }
        } catch (Exception e) 
        {
            e.printStackTrace();
            return null;
        }
		return null;
		
		
	}

	

}
