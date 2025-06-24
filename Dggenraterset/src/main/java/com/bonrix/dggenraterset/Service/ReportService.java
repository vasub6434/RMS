package com.bonrix.dggenraterset.Service;

import java.util.List;

import com.bonrix.dggenraterset.Model.History;

public interface ReportService {

	List<History> findBydeviceId(Long uid);
}
