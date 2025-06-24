package com.bonrix.dggenraterset.Repository;

import com.bonrix.dggenraterset.Model.AlearSummary;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecentAlertRepository extends JpaRepository<AlearSummary, Long> {

	@Query(value = "SELECT COUNT(*) FROM alearsummary a " + "JOIN assignsite asg ON a.deviceid = asg.deviceid "
			+ "JOIN site s ON asg.siteid = s.siteid " + "WHERE a.starttime >= CAST(:fromdate AS timestamp) "
			+ "AND a.starttime <= CAST(:todate AS timestamp) "
			+ "AND (COALESCE(:deviceId, 0) = 0 OR a.deviceid = :deviceId) "
			+ "AND (COALESCE(:parameterId, 0) = 0 OR a.parameterid = :parameterId) "
			+ "AND (COALESCE(:statusStr, 'none') = 'none' OR " + "    (:statusStr = 'true' AND a.endtime IS NULL) OR "
			+ "    (:statusStr = 'false' AND a.endtime IS NOT NULL)) "
			+ "AND s.managerid = :managerId", nativeQuery = true)
	Integer countAlerts(@Param("fromdate") String fromdate, @Param("todate") String todate,
			@Param("deviceId") Long deviceId, @Param("statusStr") String statusStr,
			@Param("parameterId") Long parameterId, @Param("managerId") Long managerId);

	@Query(value = "SELECT s.site_name, a.deviceid, a.parametername, "
			+ "to_char(a.starttime, 'YYYY-MM-DD HH24:MI:SS') AS start_time, " + "CASE WHEN a.endtime IS NULL THEN NULL "
			+ "     ELSE to_char(a.endtime, 'YYYY-MM-DD HH24:MI:SS') END AS end_time, " + "a.duration, "
			+ "CASE WHEN a.endtime IS NULL THEN 'Active' ELSE 'Resolved' END AS status " + "FROM alearsummary a "
			+ "JOIN assignsite asg ON a.deviceid = asg.deviceid " + "JOIN site s ON asg.siteid = s.siteid "
			+ "WHERE a.starttime >= CAST(:fromdate AS timestamp) " + "AND a.starttime <= CAST(:todate AS timestamp) "
			+ "AND (COALESCE(:deviceId, 0) = 0 OR a.deviceid = :deviceId) "
			+ "AND (COALESCE(:parameterId, 0) = 0 OR a.parameterid = :parameterId) "
			+ "AND (COALESCE(:statusStr, 'none') = 'none' OR " + "    (:statusStr = 'true' AND a.endtime IS NULL) OR "
			+ "    (:statusStr = 'false' AND a.endtime IS NOT NULL)) " + "AND s.managerid = :managerId "
			+ "ORDER BY a.starttime DESC " + "LIMIT :limit OFFSET :offset", nativeQuery = true)
	List<Object[]> findAlerts(@Param("fromdate") String fromdate, @Param("todate") String todate,
			@Param("deviceId") Long deviceId, @Param("statusStr") String statusStr,
			@Param("parameterId") Long parameterId, @Param("managerId") Long managerId, @Param("limit") int limit,
			@Param("offset") int offset);

	@Query(value = "SELECT COUNT(*) FROM alearsummary a " + "JOIN assignuserdevice aud ON a.deviceid = aud.device_id "
			+ "JOIN assignsite asg ON a.deviceid = asg.deviceid " + "JOIN site s ON asg.siteid = s.siteid "
			+ "WHERE a.starttime >= CAST(:fromdate AS timestamp) " + "AND a.starttime <= CAST(:todate AS timestamp) "
			+ "AND (COALESCE(:deviceId, 0) = 0 OR a.deviceid = :deviceId) "
			+ "AND (COALESCE(:parameterId, 0) = 0 OR a.parameterid = :parameterId) "
			+ "AND (COALESCE(:statusStr, 'none') = 'none' OR " + "    (:statusStr = 'true' AND a.endtime IS NULL) OR "
			+ "    (:statusStr = 'false' AND a.endtime IS NOT NULL)) "
			+ "AND aud.user_id = :userId", nativeQuery = true)
	Integer countAlertsForUser(@Param("fromdate") String fromdate, @Param("todate") String todate,
			@Param("deviceId") Long deviceId, @Param("statusStr") String statusStr,
			@Param("parameterId") Long parameterId, @Param("userId") Long userId);

	@Query(value = "SELECT s.site_name, a.deviceid, a.parametername, "
			+ "to_char(a.starttime, 'YYYY-MM-DD HH24:MI:SS') AS start_time, "
			+ "CASE WHEN a.endtime IS NULL THEN NULL ELSE to_char(a.endtime, 'YYYY-MM-DD HH24:MI:SS') END AS end_time, "
			+ "a.duration, CASE WHEN a.endtime IS NULL THEN 'Active' ELSE 'Resolved' END AS status "
			+ "FROM alearsummary a " + "JOIN assignuserdevice aud ON a.deviceid = aud.device_id "
			+ "JOIN assignsite asg ON a.deviceid = asg.deviceid " + "JOIN site s ON asg.siteid = s.siteid "
			+ "WHERE a.starttime >= CAST(:fromdate AS timestamp) " + "AND a.starttime <= CAST(:todate AS timestamp) "
			+ "AND (COALESCE(:deviceId, 0) = 0 OR a.deviceid = :deviceId) "
			+ "AND (COALESCE(:parameterId, 0) = 0 OR a.parameterid = :parameterId) "
			+ "AND (COALESCE(:statusStr, 'none') = 'none' OR " + "    (:statusStr = 'true' AND a.endtime IS NULL) OR "
			+ "    (:statusStr = 'false' AND a.endtime IS NOT NULL)) " + "AND aud.user_id = :userId "
			+ "ORDER BY a.starttime DESC " + "LIMIT :limit OFFSET :offset", nativeQuery = true)
	List<Object[]> findAlertsForUser(@Param("fromdate") String fromdate, @Param("todate") String todate,
			@Param("deviceId") Long deviceId, @Param("statusStr") String statusStr,
			@Param("parameterId") Long parameterId, @Param("userId") Long userId, @Param("limit") int limit,
			@Param("offset") int offset);

	@Query(value = "SELECT " + "  parametername AS parametername, " + "  COUNT(*)       AS cnt           "
			+ "FROM alearsummary                  " + "WHERE (                            "
			+ "    starttime BETWEEN :fromDate AND :toDate " + " OR endtime   BETWEEN :fromDate AND :toDate "
			+ ") AND managerid = :managerId      " + "GROUP BY parametername            "
			+ "ORDER BY cnt DESC", nativeQuery = true)
	List<Object[]> countAlertsByTypeBetweenDates(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("managerId") Long managerId);

	@Query(value = "SELECT a.parametername, COUNT(*) FROM alearsummary a JOIN assignuserdevice aud ON a.deviceid = aud.device_id WHERE (a.starttime BETWEEN :fromDate AND :toDate OR a.endtime BETWEEN :fromDate AND :toDate) AND aud.user_id = :userId GROUP BY a.parametername ORDER BY COUNT(*) DESC", nativeQuery = true)
	List<Object[]> countAlertsByTypeForUser(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("userId") Long userId);

	@Query(value = "SELECT " + "  a.deviceid                        AS device, "
			+ "  s.site_name                       AS site,   " + "  a.parametername                   AS alert,  "
			+ "  CONCAT(                          "
			+ "    CAST(DATE_PART('hour',  NOW() - a.starttime)   AS integer), 'h ', "
			+ "    CAST(DATE_PART('minute', NOW() - a.starttime)   AS integer), 'm'  "
			+ "  )                                 AS since   " + "FROM alearsummary a                        "
			+ "  JOIN assignsite asg ON a.deviceid = asg.deviceid "
			+ "  JOIN site s        ON asg.siteid   = s.siteid   " + "WHERE a.endtime IS NULL                     "
			+ "  AND a.starttime BETWEEN :fromDate AND :toDate "
			+ "  AND a.managerid = :managerId             ", nativeQuery = true)
	List<Object[]> findActiveAlertsRaw(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("managerId") Long managerId);

	@Query(value = "SELECT " + "  a.deviceid AS device, " + "  s.site_name AS site, " + "  a.parametername AS alert, "
			+ "  CONCAT( " + "    CAST(DATE_PART('hour', NOW() - a.starttime) AS integer), 'h ', "
			+ "    CAST(DATE_PART('minute', NOW() - a.starttime) AS integer), 'm' " + "  ) AS since "
			+ "FROM alearsummary a " + "JOIN assignsite asg ON a.deviceid = asg.deviceid "
			+ "JOIN site s ON asg.siteid = s.siteid " + "JOIN assignuserdevice aud ON a.deviceid = aud.device_id "
			+ "WHERE a.endtime IS NULL " + "AND a.starttime BETWEEN :fromDate AND :toDate "
			+ "AND aud.user_id = :userId", nativeQuery = true)
	List<Object[]> findActiveAlertsForUser(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("userId") Long userId);

	@Query(value = "SELECT " + "  s.site_name        AS site, " + "  a.parametername    AS alert,"
			+ "  COUNT(*)           AS cnt   " + "FROM alearsummary a                        "
			+ "  JOIN assignsite asg ON a.deviceid = asg.deviceid "
			+ "  JOIN site s        ON asg.siteid   = s.siteid   " + "WHERE (                                    "
			+ "    a.starttime BETWEEN :fromDate AND :toDate " + " OR a.endtime   BETWEEN :fromDate AND :toDate "
			+ ")                                         " + "  AND a.managerid = :managerId            "
			+ "GROUP BY s.site_name, a.parametername     "
			+ "ORDER BY s.site_name, a.parametername", nativeQuery = true)
	List<Object[]> countBySiteAndType(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("managerId") Long managerId);

	@Query(value = "SELECT " + "  s.site_name AS site, " + "  a.parametername AS alert, " + "  COUNT(*) AS cnt "
			+ "FROM alearsummary a " + "JOIN assignsite asg ON a.deviceid = asg.deviceid "
			+ "JOIN site s ON asg.siteid = s.siteid " + "JOIN assignuserdevice aud ON a.deviceid = aud.device_id "
			+ "WHERE (a.starttime BETWEEN :fromDate AND :toDate OR a.endtime BETWEEN :fromDate AND :toDate) "
			+ "AND aud.user_id = :userId " + "GROUP BY s.site_name, a.parametername "
			+ "ORDER BY s.site_name, a.parametername", nativeQuery = true)
	List<Object[]> countBySiteAndTypeForUser(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("userId") Long userId);

	@Query(value = "SELECT " + "  a.parametername                                     AS alert_name, "
			+ "  AVG(EXTRACT(EPOCH FROM (a.endtime - a.starttime)) / 60) AS avg_minutes " + "FROM alearsummary a "
			+ "WHERE " + "  a.endtime IS NOT NULL " + "  AND (a.starttime BETWEEN :fromDate AND :toDate "
			+ "       OR a.endtime   BETWEEN :fromDate AND :toDate) " + "  AND a.managerid = :managerId "
			+ "GROUP BY a.parametername", nativeQuery = true)
	List<Object[]> findAvgDurationRawBetweenDates(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("managerId") Long managerId);

	@Query(value = "SELECT " + "  a.parametername AS alert_name, "
			+ "  AVG(EXTRACT(EPOCH FROM (a.endtime - a.starttime)) / 60) AS avg_minutes " + "FROM alearsummary a "
			+ "JOIN assignuserdevice aud ON a.deviceid = aud.device_id " + "WHERE " + "  a.endtime IS NOT NULL "
			+ "  AND (a.starttime BETWEEN :fromDate AND :toDate OR a.endtime BETWEEN :fromDate AND :toDate) "
			+ "  AND aud.user_id = :userId " + "GROUP BY a.parametername", nativeQuery = true)
	List<Object[]> findAvgDurationRawBetweenDatesForUser(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("userId") Long userId);

	@Query(value = "SELECT " + "  s.site_name AS name, " + "  COUNT(*)    AS count " + "FROM alearsummary a "
			+ "  JOIN assignsite ast ON a.deviceid = ast.deviceid " + "  JOIN site s        ON ast.siteid   = s.siteid "
			+ "WHERE " + "  a.starttime >= NOW() - INTERVAL '1 day' " + "  AND a.managerid = :managerId "
			+ "GROUP BY s.site_name " + "ORDER BY count DESC " + "LIMIT 5", nativeQuery = true)
	List<Object[]> findTop5Native(@Param("managerId") Long managerId);

	@Query(value = "SELECT " + "  s.site_name AS name, " + "  COUNT(*) AS count " + "FROM alearsummary a "
			+ "JOIN assignuserdevice aud ON a.deviceid = aud.device_id "
			+ "JOIN assignsite ast ON a.deviceid = ast.deviceid " + "JOIN site s ON ast.siteid = s.siteid "
			+ "WHERE a.starttime >= NOW() - INTERVAL '1 day' " + "AND aud.user_id = :userId " + "GROUP BY s.site_name "
			+ "ORDER BY count DESC " + "LIMIT 5", nativeQuery = true)
	List<Object[]> findTop5SitesForUser(@Param("userId") Long userId);

	@Query(value = "SELECT " + "  a.parametername AS alertName, " + "  COUNT(*) AS count " + "FROM alearsummary a "
			+ "JOIN assignuserdevice aud ON a.deviceid = aud.device_id " + "WHERE "
			+ "  a.starttime >= NOW() - INTERVAL '1 day' " + "AND aud.user_id = :userId " + "GROUP BY a.parametername "
			+ "ORDER BY count DESC " + "LIMIT 5", nativeQuery = true)
	List<Object[]> findTopAlertTypesForUser(@Param("userId") Long userId);

	@Query(value = "SELECT " + "  a.parametername AS alertName, " + "  COUNT(*)        AS count     "
			+ "FROM alearsummary a               " + "WHERE " + "  a.starttime >= NOW() - INTERVAL '1 day' "
			+ "  AND a.managerid = :managerId             " + "GROUP BY a.parametername          "
			+ "ORDER BY count DESC               " + "LIMIT 5", nativeQuery = true)
	List<Object[]> findTopAlertTypesNative(@Param("managerId") Long managerId);

	@Query(value = "SELECT " + "  a.parametername AS alertName, " + "  COUNT(*)         AS count     "
			+ "FROM alearsummary a                " + "WHERE a.endtime IS NULL            "
			+ "  AND a.starttime BETWEEN :fromDate AND :toDate " + "  AND a.managerid = :managerId     "
			+ "GROUP BY a.parametername           " + "ORDER BY count DESC", nativeQuery = true)
	List<Object[]> findActiveAlertCountsByTypeBetween(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("managerId") Long managerId);

	@Query(value = "SELECT " + "  a.parametername AS alertName, " + "  COUNT(*) AS count " + "FROM alearsummary a "
			+ "JOIN assignuserdevice aud ON a.deviceid = aud.device_id " + "WHERE a.endtime IS NULL "
			+ "AND a.starttime BETWEEN :fromDate AND :toDate " + "AND aud.user_id = :userId "
			+ "GROUP BY a.parametername " + "ORDER BY count DESC", nativeQuery = true)
	List<Object[]> findActiveAlertCountsByTypeForUserBetween(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("userId") Long userId);

	@Query(value = "SELECT " + "  DATE(a.starttime) AS day, " + "  COUNT(*)          AS cnt " + "FROM alearsummary a "
			+ "WHERE a.starttime >= :fromParam " + "  AND a.starttime <= :toParam " + "  AND a.managerid  = :managerId "
			+ "GROUP BY DATE(a.starttime) " + "ORDER BY DATE(a.starttime)", nativeQuery = true)
	List<Object[]> countAlertsPerDayBetween(@Param("fromParam") Date from, @Param("toParam") Date to,
			@Param("managerId") Long managerId);

	@Query(value = "SELECT " + "  DATE(a.starttime) AS day, " + "  COUNT(*) AS cnt " + "FROM alearsummary a "
			+ "JOIN assignuserdevice aud ON a.deviceid = aud.device_id " + "WHERE a.starttime >= :fromParam "
			+ "  AND a.starttime <= :toParam " + "  AND aud.user_id = :userId " + "GROUP BY DATE(a.starttime) "
			+ "ORDER BY DATE(a.starttime)", nativeQuery = true)
	List<Object[]> countAlertsPerDayForUserBetween(@Param("fromParam") Date from, @Param("toParam") Date to,
			@Param("userId") Long userId);

	@Query(value = "SELECT " + "  DATE_TRUNC('day', starttime)     AS day,             "
			+ "  AVG(duration / 60.0)              AS avg_duration_minutes " + "FROM alearsummary                   "
			+ "WHERE starttime >= CURRENT_DATE - INTERVAL '29 days' " + "  AND isactive = false               "
			+ "  AND managerid = :managerId         " + "GROUP BY DATE_TRUNC('day', starttime) "
			+ "ORDER BY day", nativeQuery = true)
	List<Object[]> findLast30DaysAvgDurationInMinutes(@Param("managerId") Long managerId);

	@Query(value = "SELECT " + "  DATE_TRUNC('day', starttime) AS day, "
			+ "  AVG(duration / 60.0) AS avg_duration_minutes " + "FROM alearsummary a "
			+ "JOIN assignuserdevice aud ON a.deviceid = aud.device_id "
			+ "WHERE a.starttime >= CURRENT_DATE - INTERVAL '29 days' " + "  AND a.isactive = false "
			+ "  AND aud.user_id = :userId " + "GROUP BY DATE_TRUNC('day', starttime) "
			+ "ORDER BY day", nativeQuery = true)
	List<Object[]> findLast30DaysAvgDurationForUserInMinutes(@Param("userId") Long userId);

	@Query(value = "SELECT " + "  to_char(a.starttime, 'FMDay')                                    AS day, "
			+ "  CAST(FLOOR(EXTRACT(hour FROM a.starttime) / 2) * 2 AS integer)   AS hour, "
			+ "  COUNT(*)                                                         AS cnt " + "FROM alearsummary a "
			+ "JOIN assignsite asg ON a.deviceid = asg.deviceid " + "JOIN site s ON asg.siteid = s.siteid "
			+ "WHERE a.starttime >= :cutoff " + "AND a.managerid = :managerId " + "GROUP BY day, hour "
			+ "ORDER BY hour ASC", nativeQuery = true)
	List<Object[]> findRawFrequencySince(@Param("cutoff") Timestamp cutoff, @Param("managerId") Long managerId);

	@Query(value = "SELECT " + "  to_char(a.starttime, 'FMDay') AS day, "
			+ "  CAST(FLOOR(EXTRACT(hour FROM a.starttime) / 2) * 2 AS integer) AS hour, " + "  COUNT(*) AS cnt "
			+ "FROM alearsummary a " + "JOIN assignsite asg ON a.deviceid = asg.deviceid "
			+ "JOIN site s ON asg.siteid = s.siteid " + "JOIN assignuserdevice aud ON a.deviceid = aud.device_id "
			+ "WHERE a.starttime >= :cutoff " + "AND aud.user_id = :userId " + "GROUP BY day, hour "
			+ "ORDER BY hour ASC", nativeQuery = true)
	List<Object[]> findRawFrequencySinceForUser(@Param("cutoff") Timestamp cutoff, @Param("userId") Long userId);

}
