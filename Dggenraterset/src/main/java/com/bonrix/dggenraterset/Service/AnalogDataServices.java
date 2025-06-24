package com.bonrix.dggenraterset.Service;

import java.util.List;

import com.bonrix.dggenraterset.Model.Analogdata;
import com.bonrix.dggenraterset.Model.Devicemaster;

public interface AnalogDataServices {

	List<Analogdata> getlist();

	void save(Analogdata bs);

	Analogdata get(int id);

	String delete(int id);

	String update(Analogdata bs);

	Analogdata findBydevice(Devicemaster device);

	List<Object[]> getAnalogdatalist();

	List<Object[]> getDigitaldatalist();

}
