package com.bonrix.dggenraterset.Repository;

import com.bonrix.dggenraterset.Model.AlearSummary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.repository.query.Param;

@Repository
public interface AlearSummaryRepository extends JpaRepository<AlearSummary, Long> {

    // Custom query to find records by deviceId, parameterId, and dynamic isActive value
    @Query("SELECT a FROM AlearSummary a WHERE a.deviceId = :deviceId AND a.parameterId = :parameterId AND a.isActive = :isActive")
    AlearSummary findByDeviceIdAndParameterIdAndIsActive(@Param("deviceId") long deviceId,
                                                          @Param("parameterId") long parameterId,
                                                          @Param("isActive") boolean isActive);
    
    @Query("SELECT a FROM AlearSummary a WHERE a.deviceId = :deviceId AND a.parameterId = :parameterId AND a.isActive = :isActive AND a.endTime IS NULL")
    AlearSummary findByDeviceIdAndParameterIdAndIsActiveAndEndTimeNull(@Param("deviceId") long deviceId,
                                                                       @Param("parameterId") long parameterId,
                                                                       @Param("isActive") boolean isActive);
    
    /*@Query("SELECT a FROM AlearSummary a WHERE a.starttime < CURRENT_TIMESTAMP - INTERVAL 4 HOUR " +
            "AND a.endtime IS NULL AND a.managerid = :managerid")*/
 /*  @Query("SELECT a FROM AlearSummary a WHERE " +
            " a.endtime IS NULL AND a.managerid = :managerid")
     List<AlearSummary> findByStartTimeMoreThan4HoursAgoWithNullEndTimeAndManagerId(Long managerid);*/
}
