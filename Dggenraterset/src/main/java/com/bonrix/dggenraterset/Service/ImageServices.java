package com.bonrix.dggenraterset.Service;

import java.util.List;

import com.bonrix.dggenraterset.Model.Images;

public interface ImageServices {

	List<Images> getlist();

	void save(Images bs);

	Images get(Long id);

	String delete(Long id);

}
