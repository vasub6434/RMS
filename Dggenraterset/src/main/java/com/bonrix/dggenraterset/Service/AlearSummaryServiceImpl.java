package com.bonrix.dggenraterset.Service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.Model.AlearSummary;
import com.bonrix.dggenraterset.Repository.AlearSummaryRepository;

@Service
public class AlearSummaryServiceImpl implements AlearSummaryService {

    private final AlearSummaryRepository alearSummaryRepository;

    @Autowired
    public AlearSummaryServiceImpl(AlearSummaryRepository alearSummaryRepository) {
        this.alearSummaryRepository = alearSummaryRepository;
    }

    @Override
    public AlearSummary saveOrUpdate(AlearSummary alearSummary) {
        AlearSummary existingAlearSummary = alearSummaryRepository
            .findByDeviceIdAndParameterIdAndIsActive(
                alearSummary.getDeviceId(),
                alearSummary.getParameterId(),
                alearSummary.isIsactive()
            );

        if (existingAlearSummary != null) {
            existingAlearSummary.setEntryTime(alearSummary.getEntryTime());
            existingAlearSummary.setStartTime(alearSummary.getStartTime());
            existingAlearSummary.setManagerId(alearSummary.getManagerId());
            existingAlearSummary.setParametername(alearSummary.getParametername());
            existingAlearSummary.setEndTime(alearSummary.getEndTime());
            return alearSummaryRepository.save(existingAlearSummary);
        } else {
            return alearSummaryRepository.save(alearSummary);
        }
    }

    @Override
    public AlearSummary findByDeviceIdAndParameterIdAndIsActive(long deviceId, long parameterId, boolean isActive) {
        return alearSummaryRepository.findByDeviceIdAndParameterIdAndIsActive(deviceId, parameterId,isActive);
    }

	@Override
	public AlearSummary findByDeviceIdAndParameterIdAndIsActiveAndEndTimeNull(long deviceId, long parameterId,
			boolean isActive) {
		return alearSummaryRepository.findByDeviceIdAndParameterIdAndIsActiveAndEndTimeNull(deviceId, parameterId, isActive);
	}

	@Override
	public List<AlearSummary> findByStartTimeMoreThan4HoursAgoWithNullEndTimeAndManagerId(Long managerid) {
		//return alearSummaryRepository.findByStartTimeMoreThan4HoursAgoWithNullEndTimeAndManagerId(managerid);
		return null;
	}

}

