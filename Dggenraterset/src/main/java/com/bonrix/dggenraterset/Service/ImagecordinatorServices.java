package com.bonrix.dggenraterset.Service;

import java.util.List;

import com.bonrix.dggenraterset.Model.Imagecordinator;

public interface ImagecordinatorServices {
	List<Imagecordinator> getlist();

	void save(Imagecordinator bs);

	Imagecordinator get(Long id);

	String delete(Long id);
}
