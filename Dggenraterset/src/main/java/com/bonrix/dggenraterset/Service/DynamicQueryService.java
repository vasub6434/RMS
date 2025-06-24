package com.bonrix.dggenraterset.Service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class DynamicQueryService {

	@Autowired
	private EntityManager entityManager;
	
	private static final Logger log = Logger.getLogger(DynamicQueryService.class);

//    public List<Object[]> getDynamicDeviceStatusDetails(long userId, long profileId,long siteId,List<String> failureCodes, List<String> digitalFields) {
//    	
//    	 StringBuilder queryBuilder = new StringBuilder();
//         queryBuilder.append("SELECT device_id, devicename, altdevicename, ")
//                     .append("TO_CHAR(device_date, 'YYYY-MM-DD HH12:MI:SS AM') AS device_date, ");
//
//         for (int i = 0; i < digitalFields.size(); i++) {
//             if (i > 0) {
//                 queryBuilder.append(", ");
//             }
//             queryBuilder.append("devicedata -> 'Digital' ->> '")
//                         .append(digitalFields.get(i))
//                         .append("' AS ")
//                         .append("DIGITAL_"+digitalFields.get(i));
//         }
//
//         queryBuilder.append(", COALESCE(maintenancestaff.mobile, 'Not Set') ");
//         queryBuilder.append("FROM lasttrack ");
//         queryBuilder.append("JOIN devicemaster ON devicemaster.deviceid = lasttrack.device_id ");
//         queryBuilder.append("LEFT JOIN maintenancestaff ON maintenancestaff.deviceid = lasttrack.device_id ");
//         queryBuilder.append("WHERE ");
//
//         for (int i = 0; i < failureCodes.size(); i++) {
//             if (i > 0) {
//                 queryBuilder.append(" OR ");  // Use OR between conditions
//             }
//             queryBuilder.append("devicedata -> 'Digital' ->> '")
//                         .append(failureCodes.get(i))
//                         .append("' = '0'");
//         }
//
//         queryBuilder.append(" AND CAST(device_date AS DATE) = CURRENT_DATE ");
//
//         queryBuilder.append(" AND lasttrack.device_id IN (SELECT dm.deviceid ")
//                     .append("FROM devicemaster dm ")
//                     .append("JOIN lasttrack lt ON lt.device_id = dm.deviceid ")
//                     .append("JOIN assignuserdevice ad ON ad.device_id = dm.deviceid ")
//                     .append("JOIN assignsite ast ON ast.deviceid = dm.deviceid ")
//                     .append("WHERE dm.prid_fk = "+profileId+" AND ad.user_id = "+userId+" AND ast.siteid = "+siteId+")");
//
//         
//        Query query = entityManager.createNativeQuery(queryBuilder.toString());
//        System.out.println(queryBuilder.toString());
//        return query.getResultList();
//    }
//    
	public List<Object[]> getDynamicDeviceStatusDetails(long userId, long profileId, List<String> failureCodes,
			List<String> digitalFields) {

		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT device_id, devicename, altdevicename, ")
				.append("TO_CHAR(device_date, 'YYYY-MM-DD HH12:MI:SS AM') AS device_date, ");

		for (int i = 0; i < digitalFields.size(); i++) {
			if (i > 0) {
				queryBuilder.append(", ");
			}
			queryBuilder.append("devicedata -> 'Digital' ->> '").append(digitalFields.get(i)).append("' AS ")
					.append("DIGITAL_" + digitalFields.get(i));
		}

		queryBuilder.append(", COALESCE(maintenancestaff.mobile, 'Not Set') ");
		queryBuilder.append("FROM lasttrack ");
		queryBuilder.append("JOIN devicemaster ON devicemaster.deviceid = lasttrack.device_id ");
		queryBuilder.append("LEFT JOIN maintenancestaff ON maintenancestaff.deviceid = lasttrack.device_id ");
		queryBuilder.append("WHERE ");

		for (int i = 0; i < failureCodes.size(); i++) {
			if (i > 0) {
				queryBuilder.append(" OR "); // Use OR between conditions
			}
			queryBuilder.append("devicedata -> 'Digital' ->> '").append(failureCodes.get(i)).append("' = '0'");
		}

		queryBuilder.append(" AND CAST(device_date AS DATE) = CURRENT_DATE ");

		queryBuilder.append(" AND lasttrack.device_id IN (SELECT dm.deviceid ").append("FROM devicemaster dm ")
				.append("JOIN lasttrack lt ON lt.device_id = dm.deviceid ")
				.append("JOIN assignuserdevice ad ON ad.device_id = dm.deviceid ")
				.append("JOIN assignsite ast ON ast.deviceid = dm.deviceid ")
				.append("WHERE dm.prid_fk = " + profileId + " AND ad.user_id = " + userId + ")");

		Query query = entityManager.createNativeQuery(queryBuilder.toString());
		System.out.println(queryBuilder.toString());
		return query.getResultList();
	}

	// new
	public String getAlertCountsFromLasttrack(long managerId,long profileId, String[] paramIds, String[] paramNames) {
		Map<String, String> idToName = new HashMap<>();
		for (int i = 0; i < paramIds.length && i < paramNames.length; i++) {
			idToName.put(paramIds[i], paramNames[i]);
		}


		StringBuilder sql = new StringBuilder();
		sql.append("WITH exploded AS (").append(" SELECT a.siteid, kv.key AS param_id").append(" FROM lasttrack lt")
				.append(" JOIN assignsite a ON a.deviceid = lt.device_id AND a.managerid = :mgrId   JOIN devicemaster dm ON dm.deviceid = lt.device_id   AND dm.prid_fk  = :profileId  ")
				.append(" CROSS JOIN LATERAL jsonb_array_elements(lt.digitaldata->'Digital') AS dd(elem)")
				.append(" CROSS JOIN LATERAL jsonb_each_text(dd.elem) AS kv(key, val)")
				.append(" WHERE CAST(lt.device_date AS DATE) = CURRENT_DATE ").append("   AND kv.key IN (");

		for (int i = 0; i < paramIds.length; i++) {
			if (i > 0)
				sql.append(", ");
			sql.append("'").append(paramIds[i]).append("'");
		}
		sql.append(")").append("   AND kv.val = '0'").append(")").append("SELECT s.site_name, CASE");

		for (Map.Entry<String, String> e : idToName.entrySet()) {
			sql.append(" WHEN exploded.param_id = '").append(e.getKey()).append("' THEN '").append(e.getValue())
					.append("'");
		}
		sql.append(" ELSE 'Other' END AS alert_type, COUNT(*) AS count").append(" FROM exploded")
				.append(" JOIN site s ON s.siteid = exploded.siteid AND s.managerid = :mgrId")
				.append(" GROUP BY s.site_name, alert_type").append(" ORDER BY s.site_name, alert_type");

		Query q = entityManager.createNativeQuery(sql.toString()).setParameter("mgrId", managerId).setParameter("profileId", profileId);
		System.out.println(sql.toString());

		@SuppressWarnings("unchecked")
		List<Object[]> rows = q.getResultList();

		JSONArray resultArray = new JSONArray();
		for (Object[] row : rows) {
			JSONObject o = new JSONObject();
			o.put("siteName", (String) row[0]);
			o.put("alertType", (String) row[1]);
			o.put("count", ((Number) row[2]).intValue());
			resultArray.put(o);
		}

		
		 Set<String> seen = new HashSet<>();
		    for (int i = 0; i < resultArray.length(); i++) {
		        JSONObject o = resultArray.getJSONObject(i);
		        seen.add(o.getString("siteName") + "|" + o.getString("alertType"));
		    }

		    Set<String> sitesWithAlerts = new HashSet<>();
		    for (int i = 0; i < resultArray.length(); i++) {
		        sitesWithAlerts.add(resultArray.getJSONObject(i).getString("siteName"));
		    }

		    for (String site : sitesWithAlerts) {
		        for (String alertName : paramNames) {
		            String key = site + "|" + alertName;
		            if (!seen.contains(key)) {
		                JSONObject z = new JSONObject();
		                z.put("siteName",  site);
		                z.put("alertType", alertName);
		                z.put("count",     0);
		                resultArray.put(z);
		            }
		        }
		    }

		return resultArray.toString();
	}

	public List<Object[]> getAlertCountsByTypeDynamic(Long managerId, Date fromDate, Date toDate,
			List<String> parameterNameList) {
		StringBuilder queryBuilder = new StringBuilder();

		queryBuilder.append("WITH alert_extraction AS ( ").append("SELECT message, entrytime, deviceid ")
				.append("FROM alertmessageshistory ").append("WHERE managerid = :managerId ")
				.append("AND entrytime BETWEEN :fromDate AND :toDate ").append("AND message LIKE '%Status ACTIVE%' ")
				.append("), alert_types AS ( ").append("SELECT CASE ");

		for (String param : parameterNameList) {
			queryBuilder.append("WHEN message LIKE '%").append(param).append("%' THEN '").append(param).append("' ");
		}

		queryBuilder.append("ELSE 'Other' END AS alert_type, entrytime, deviceid ").append("FROM alert_extraction ) ")
				.append("SELECT alert_type, COUNT(*) as count FROM alert_types GROUP BY alert_type ORDER BY count DESC");

		Query query = entityManager.createNativeQuery(queryBuilder.toString());
		query.setParameter("managerId", managerId);
		query.setParameter("fromDate", fromDate);
		query.setParameter("toDate", toDate);
		System.out.println(queryBuilder.toString());
		return query.getResultList();
	}

	public String getDeviceAlertFromAlertMsgHistory(long managerId,long profileId, String[] parameterNameList, Date fromDate,
			Date toDate) {
		StringBuilder caseBuilder = new StringBuilder("CASE ");
		boolean hasCondition = false;
		for (String raw : parameterNameList) {
			String param = raw.trim();
			if (!param.isEmpty()) {
				hasCondition = true;
				caseBuilder.append("    WHEN message ILIKE '%").append(param).append("%' THEN '").append(param)
						.append("' ");
			}
		}
		if (!hasCondition) {
			return new JSONArray().toString();
		}
		caseBuilder.append("    ELSE 'Other' END AS alert_type");

		String sql = "WITH alert_extraction AS ( " + "    SELECT h.message, h.entrytime, h.deviceid, h.managerid "
				+ "    FROM alertmessageshistory h JOIN devicemaster dm ON h.deviceid = dm.deviceid AND dm.prid_fk = :profileId " + "    WHERE h.managerid = :mgrId "
				+ "      AND entrytime BETWEEN :fromDate AND :toDate " + "      AND message LIKE '%Status ACTIVE%' "
				+ "), " + "alert_types AS ( " + "    SELECT " + caseBuilder + ", entrytime, deviceid, managerid "
				+ "    FROM alert_extraction " + ") " + "SELECT s.site_name, t.alert_type, COUNT(*) AS count "
				+ "FROM alert_types t "
				+ "JOIN assignsite a ON t.deviceid = a.deviceid AND t.managerid = a.managerid "
				+ "JOIN site s       ON a.siteid   = s.siteid   AND a.managerid = s.managerid "
				+ "GROUP BY s.site_name, t.alert_type " + "ORDER BY s.site_name ASC";

		Query q = entityManager.createNativeQuery(sql).setParameter("mgrId", managerId)
				.setParameter("profileId", profileId)
				.setParameter("fromDate", fromDate, TemporalType.TIMESTAMP)
				.setParameter("toDate", toDate, TemporalType.TIMESTAMP);
		log.info("SAJAN : "+sql);
		@SuppressWarnings("unchecked")
		List<Object[]> rows = q.getResultList();

		JSONArray resultArray = new JSONArray();
		for (Object[] row : rows) {
			JSONObject obj = new JSONObject();
			obj.put("siteName", row[0]);
			obj.put("alertType", row[1]);
			obj.put("count", row[2]);
			resultArray.put(obj);
		}
		return resultArray.toString();
	}

	public List<Object[]> getAlertCountsByTypeDynamicLive(Long managerId, List<String> parameterIds) {
		StringBuilder queryBuilder = new StringBuilder();

		queryBuilder.append("SELECT ").append("  a.managerid, ").append("  lt.device_id, ");

		for (String paramId : parameterIds) {
			queryBuilder.append("  MAX(CAST(d.elem_val AS integer)) ").append("FILTER (WHERE d.elem_key = '")
					.append(paramId).append("') AS p_").append(paramId).append(", ");
		}
		if (!parameterIds.isEmpty()) {
			queryBuilder.setLength(queryBuilder.length() - 2);
		}

		queryBuilder.append(" FROM assignsite a ").append("JOIN lasttrack lt ON lt.device_id = a.deviceid ")
				.append("CROSS JOIN LATERAL jsonb_array_elements(lt.digitaldata->'Digital') AS arr(elem) ")
				.append("CROSS JOIN LATERAL jsonb_each_text(arr.elem) AS d(elem_key, elem_val) ")
				.append("WHERE a.managerid = :managerId ")

				.append("GROUP BY a.managerid, lt.device_id ")
				.append("HAVING SUM(CASE WHEN CAST(d.elem_val AS integer) = 0 THEN 1 ELSE 0 END) > 0 ")

				.append("ORDER BY lt.device_id");

		System.out.println(queryBuilder.toString());
		Query query = entityManager.createNativeQuery(queryBuilder.toString());
		query.setParameter("managerId", managerId);
		
		return query.getResultList();
	}

}

/*package com.bonrix.dggenraterset.Service;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DynamicQueryService {

    @Autowired
    private EntityManager entityManager;
    private static final Logger log = Logger.getLogger(DynamicQueryService.class);
//    public List<Object[]> getDynamicDeviceStatusDetails(long userId, long profileId,long siteId,List<String> failureCodes, List<String> digitalFields) {
//    	
//    	 StringBuilder queryBuilder = new StringBuilder();
//         queryBuilder.append("SELECT device_id, devicename, altdevicename, ")
//                     .append("TO_CHAR(device_date, 'YYYY-MM-DD HH12:MI:SS AM') AS device_date, ");
//
//         for (int i = 0; i < digitalFields.size(); i++) {
//             if (i > 0) {
//                 queryBuilder.append(", ");
//             }
//             queryBuilder.append("devicedata -> 'Digital' ->> '")
//                         .append(digitalFields.get(i))
//                         .append("' AS ")
//                         .append("DIGITAL_"+digitalFields.get(i));
//         }
//
//         queryBuilder.append(", COALESCE(maintenancestaff.mobile, 'Not Set') ");
//         queryBuilder.append("FROM lasttrack ");
//         queryBuilder.append("JOIN devicemaster ON devicemaster.deviceid = lasttrack.device_id ");
//         queryBuilder.append("LEFT JOIN maintenancestaff ON maintenancestaff.deviceid = lasttrack.device_id ");
//         queryBuilder.append("WHERE ");
//
//         for (int i = 0; i < failureCodes.size(); i++) {
//             if (i > 0) {
//                 queryBuilder.append(" OR ");  // Use OR between conditions
//             }
//             queryBuilder.append("devicedata -> 'Digital' ->> '")
//                         .append(failureCodes.get(i))
//                         .append("' = '0'");
//         }
//
//         queryBuilder.append(" AND CAST(device_date AS DATE) = CURRENT_DATE ");
//
//         queryBuilder.append(" AND lasttrack.device_id IN (SELECT dm.deviceid ")
//                     .append("FROM devicemaster dm ")
//                     .append("JOIN lasttrack lt ON lt.device_id = dm.deviceid ")
//                     .append("JOIN assignuserdevice ad ON ad.device_id = dm.deviceid ")
//                     .append("JOIN assignsite ast ON ast.deviceid = dm.deviceid ")
//                     .append("WHERE dm.prid_fk = "+profileId+" AND ad.user_id = "+userId+" AND ast.siteid = "+siteId+")");
//
//         
//        Query query = entityManager.createNativeQuery(queryBuilder.toString());
//        System.out.println(queryBuilder.toString());
//        return query.getResultList();
//    }
//    
    public List<Object[]> getDynamicDeviceStatusDetails(long userId, long profileId,List<String> failureCodes, List<String> digitalFields) {
    	
   	 StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT device_id, devicename, altdevicename, ")
                    .append("TO_CHAR(device_date, 'YYYY-MM-DD HH12:MI:SS AM') AS device_date, ");

        for (int i = 0; i < digitalFields.size(); i++) {
            if (i > 0) {
                queryBuilder.append(", ");
            }
            queryBuilder.append("devicedata -> 'Digital' ->> '")
                        .append(digitalFields.get(i))
                        .append("' AS ")
                        .append("DIGITAL_"+digitalFields.get(i));
        }

        queryBuilder.append(", COALESCE(maintenancestaff.mobile, 'Not Set') ");
        queryBuilder.append("FROM lasttrack ");
        queryBuilder.append("JOIN devicemaster ON devicemaster.deviceid = lasttrack.device_id ");
        queryBuilder.append("LEFT JOIN maintenancestaff ON maintenancestaff.deviceid = lasttrack.device_id ");
        queryBuilder.append("WHERE ");

        for (int i = 0; i < failureCodes.size(); i++) {
            if (i > 0) {
                queryBuilder.append(" OR ");  // Use OR between conditions
            }
            queryBuilder.append("devicedata -> 'Digital' ->> '")
                        .append(failureCodes.get(i))
                        .append("' = '0'");
        }

        queryBuilder.append(" AND CAST(device_date AS DATE) = '2025-04-08' ");

        queryBuilder.append(" AND lasttrack.device_id IN (SELECT dm.deviceid ")
                    .append("FROM devicemaster dm ")
                    .append("JOIN lasttrack lt ON lt.device_id = dm.deviceid ")
                    .append("JOIN assignuserdevice ad ON ad.device_id = dm.deviceid ")
                    .append("JOIN assignsite ast ON ast.deviceid = dm.deviceid ")
                    .append("WHERE dm.prid_fk = "+profileId+" AND ad.user_id = "+userId+")");

        
       Query query = entityManager.createNativeQuery(queryBuilder.toString());
       log.info(queryBuilder.toString());
       return query.getResultList();
   }
    
    public List<Object[]> getAllDeviceStatusBySite(long userId, long profileId, long siteId, List<String> failureCodes, List<String> digitalFields) {
        
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT device_id, devicename, altdevicename, ")
                    .append("TO_CHAR(device_date, 'YYYY-MM-DD HH12:MI:SS AM') AS device_date, ");
          
        for (int i = 0; i < digitalFields.size(); i++) {
            if (i > 0) {
                queryBuilder.append(", ");
            }
            queryBuilder.append("devicedata -> 'Digital' ->> '")
                        .append(digitalFields.get(i))
                        .append("' AS ")
                        .append("DIGITAL_" + digitalFields.get(i));
        }
        
        queryBuilder.append(", COALESCE(maintenancestaff.mobile, 'Not Set') ");
        queryBuilder.append("FROM lasttrack ");
        queryBuilder.append("JOIN devicemaster ON devicemaster.deviceid = lasttrack.device_id ");
        queryBuilder.append("LEFT JOIN maintenancestaff ON maintenancestaff.deviceid = lasttrack.device_id ");
        queryBuilder.append("WHERE ");
        
        for (int i = 0; i < failureCodes.size(); i++) {
            if (i > 0) {
                queryBuilder.append(" OR ");  // Use OR between conditions
            }
            queryBuilder.append("devicedata -> 'Digital' ->> '")
                        .append(failureCodes.get(i))
                        .append("' = '0'");
        }

        queryBuilder.append(" AND CAST(device_date AS DATE) = '2025-01-23' ");
        
        queryBuilder.append(" AND lasttrack.device_id IN (SELECT dm.deviceid ")
                    .append("FROM devicemaster dm ")
                    .append("JOIN lasttrack lt ON lt.device_id = dm.deviceid ")
                    .append("JOIN assignuserdevice ad ON ad.device_id = dm.deviceid ")
                    .append("JOIN assignsite ast ON ast.deviceid = dm.deviceid ")
                    .append("WHERE dm.prid_fk = ").append(profileId)
                    .append(" AND ad.user_id = ").append(userId)
                    .append(" AND ast.siteid = ").append(siteId)
                    .append(")");

       
        Query query = entityManager.createNativeQuery(queryBuilder.toString());
        System.out.println(queryBuilder.toString()); 
        return query.getResultList();
    }
    
    
    public List<Object[]> getAlertCountsByTypeDynamic(Long managerId, Date fromDate, Date toDate, List<String> parameterNameList) {
        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("WITH alert_extraction AS ( ")
            .append("SELECT message, entrytime, deviceid ")
            .append("FROM alertmessageshistory ")
            .append("WHERE managerid = :managerId ")
            .append("AND entrytime BETWEEN :fromDate AND :toDate ")
            .append("AND message LIKE '%Status ACTIVE%' ")
            .append("), alert_types AS ( ")
            .append("SELECT CASE ");

        for (String param : parameterNameList) {
            queryBuilder.append("WHEN message LIKE '%")
                .append(param)
                .append("%' THEN '")
                .append(param)
                .append("' ");
        }

        queryBuilder.append("ELSE 'Other' END AS alert_type, entrytime, deviceid ")
            .append("FROM alert_extraction ) ")
            .append("SELECT alert_type, COUNT(*) as count FROM alert_types GROUP BY alert_type ORDER BY count DESC");

        Query query = entityManager.createNativeQuery(queryBuilder.toString());
        query.setParameter("managerId", managerId);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);

        return query.getResultList();
    }
    
    public List<Object[]> getAlertCountsByTypeDynamicLive(Long managerId, List<String> parameterIds) {
        StringBuilder queryBuilder = new StringBuilder();
        
        queryBuilder.append("SELECT ")
                   .append("  a.managerid, ")
                   .append("  lt.device_id, ");
        
        for (String paramId : parameterIds) {
            queryBuilder.append("  MAX(CAST(d.elem_val AS integer)) ")
                       .append("FILTER (WHERE d.elem_key = '")
                       .append(paramId)
                       .append("') AS p_")
                       .append(paramId)
                       .append(", ");
        }
        if (!parameterIds.isEmpty()) {
            queryBuilder.setLength(queryBuilder.length() - 2);
        }
        
        queryBuilder.append(" FROM assignsite a ")
                   .append("JOIN lasttrack lt ON lt.device_id = a.deviceid ")
                   .append("CROSS JOIN LATERAL jsonb_array_elements(lt.digitaldata->'Digital') AS arr(elem) ")
                   .append("CROSS JOIN LATERAL jsonb_each_text(arr.elem) AS d(elem_key, elem_val) ")
                   .append("WHERE a.managerid = :managerId ")
                   
                   .append("GROUP BY a.managerid, lt.device_id ")
                   .append("HAVING SUM(CASE WHEN CAST(d.elem_val AS integer) = 0 THEN 1 ELSE 0 END) > 0 ")
                   
                   .append("ORDER BY lt.device_id");
        
        Query query = entityManager.createNativeQuery(queryBuilder.toString());
        query.setParameter("managerId", managerId);
        return query.getResultList();
    }
    
    public String getDeviceAlertFromAlertMsgHistory(long managerId, String[] parameterNameList) {
        StringBuilder caseBuilder = new StringBuilder();
        caseBuilder.append("CASE ");

        boolean hasCondition = false;

        for (String param : parameterNameList) {
            String cleanParam = param.trim();
            if (!cleanParam.isEmpty()) {
                hasCondition = true;
                caseBuilder.append("    WHEN message ILIKE '%")
                        .append(cleanParam)
                        .append("%' THEN '")
                        .append(cleanParam)
                        .append("' ");
            }
        }

        if (!hasCondition) {
            return new JSONArray().toString(); 
        }

        caseBuilder.append("    ELSE 'Other' ");
        caseBuilder.append("END AS alert_type");
  
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("WITH alert_extraction AS ( ")
                .append("    SELECT message, entrytime, deviceid, managerid ")
                .append("    FROM alertmessageshistory ")
                .append("    WHERE managerid = ").append(managerId).append(" ")
                .append("      AND entrytime BETWEEN '2025-02-23 00:00:00' AND '2025-03-23 23:59:59' ")
                .append("      AND message LIKE '%Status ACTIVE%' ")
                .append("), ")
                .append("alert_types AS ( ")
                .append("    SELECT ").append(caseBuilder).append(", ")
                .append("           entrytime, deviceid, managerid ")
                .append("    FROM alert_extraction ")
                .append(") ")
                .append("SELECT s.site_name, t.alert_type, COUNT(*) AS count ")
                .append("FROM alert_types t ")
                .append("JOIN assignsite a ON t.deviceid = a.deviceid AND t.managerid = a.managerid ")
                .append("JOIN site s ON a.siteid = s.siteid AND a.managerid = s.managerid ")
                .append("GROUP BY s.site_name, t.alert_type ")
                .append("ORDER BY s.site_name ASC");

        System.out.println("Executing SQL: " + queryBuilder.toString());

        List<Object[]> resultList = entityManager.createNativeQuery(queryBuilder.toString()).getResultList();

        JSONArray resultArray = new JSONArray();
        for (Object[] row : resultList) {
            JSONObject obj = new JSONObject();
            obj.put("siteName", row[0]);
            obj.put("alertType", row[1]);
            obj.put("count", row[2]);
            resultArray.put(obj);
        }

        return resultArray.toString();
    }

 // new
 	public String getAlertCountsFromLasttrack(long managerId, String[] paramIds, String[] paramNames) {
 		Map<String, String> idToName = new HashMap<>();
 		for (int i = 0; i < paramIds.length && i < paramNames.length; i++) {
 			idToName.put(paramIds[i], paramNames[i]);
 		}

 		// 2) Build the SQL string with nested JSON array handling
 		StringBuilder sql = new StringBuilder();
 		sql.append("WITH exploded AS (").append(" SELECT a.siteid, kv.key AS param_id").append(" FROM lasttrack lt")
 				.append(" JOIN assignsite a ON a.deviceid = lt.device_id AND a.managerid = :mgrId")
 				.append(" CROSS JOIN LATERAL jsonb_array_elements(lt.digitaldata->'Digital') AS dd(elem)")
 				.append(" CROSS JOIN LATERAL jsonb_each_text(dd.elem) AS kv(key, val)")
 				.append(" WHERE CAST(lt.device_date AS DATE) = CURRENT_DATE ").append("   AND kv.key IN (");

 		for (int i = 0; i < paramIds.length; i++) {
 			if (i > 0)
 				sql.append(", ");
 			sql.append("'").append(paramIds[i]).append("'");
 		}
 		sql.append(")").append("   AND kv.val = '0'").append(")").append("SELECT s.site_name, CASE");

 		for (Map.Entry<String, String> e : idToName.entrySet()) {
 			sql.append(" WHEN exploded.param_id = '").append(e.getKey()).append("' THEN '").append(e.getValue())
 					.append("'");
 		}
 		sql.append(" ELSE 'Other' END AS alert_type, COUNT(*) AS count").append(" FROM exploded")
 				.append(" JOIN site s ON s.siteid = exploded.siteid AND s.managerid = :mgrId")
 				.append(" GROUP BY s.site_name, alert_type").append(" ORDER BY s.site_name, alert_type");

 		Query q = entityManager.createNativeQuery(sql.toString()).setParameter("mgrId", managerId);
 		System.out.println(sql.toString());

 		@SuppressWarnings("unchecked")
 		List<Object[]> rows = q.getResultList();

 		JSONArray resultArray = new JSONArray();
 		for (Object[] row : rows) {
 			JSONObject o = new JSONObject();
 			o.put("siteName", (String) row[0]);
 			o.put("alertType", (String) row[1]);
 			o.put("count", ((Number) row[2]).intValue());
 			resultArray.put(o);
 		}

 		return resultArray.toString();
 	}
 	
 	public String getDeviceAlertFromAlertMsgHistory(long managerId, String[] parameterNameList, Date fromDate,
			Date toDate) {
		StringBuilder caseBuilder = new StringBuilder("CASE ");
		boolean hasCondition = false;
		for (String raw : parameterNameList) {
			String param = raw.trim();
			if (!param.isEmpty()) {
				hasCondition = true;
				caseBuilder.append("    WHEN message ILIKE '%").append(param).append("%' THEN '").append(param)
						.append("' ");
			}
		}
		if (!hasCondition) {
			return new JSONArray().toString();
		}
		caseBuilder.append("    ELSE 'Other' END AS alert_type");

		String sql = "WITH alert_extraction AS ( " + "    SELECT message, entrytime, deviceid, managerid "
				+ "    FROM alertmessageshistory " + "    WHERE managerid = :mgrId "
				+ "      AND entrytime BETWEEN :fromDate AND :toDate " + "      AND message LIKE '%Status ACTIVE%' "
				+ "), " + "alert_types AS ( " + "    SELECT " + caseBuilder + ", entrytime, deviceid, managerid "
				+ "    FROM alert_extraction " + ") " + "SELECT s.site_name, t.alert_type, COUNT(*) AS count "
				+ "FROM alert_types t "
				+ "JOIN assignsite a ON t.deviceid = a.deviceid AND t.managerid = a.managerid "
				+ "JOIN site s       ON a.siteid   = s.siteid   AND a.managerid = s.managerid "
				+ "GROUP BY s.site_name, t.alert_type " + "ORDER BY s.site_name ASC";

		Query q = entityManager.createNativeQuery(sql).setParameter("mgrId", managerId)
				.setParameter("fromDate", fromDate, TemporalType.TIMESTAMP)
				.setParameter("toDate", toDate, TemporalType.TIMESTAMP);

		@SuppressWarnings("unchecked")
		List<Object[]> rows = q.getResultList();

		JSONArray resultArray = new JSONArray();
		for (Object[] row : rows) {
			JSONObject obj = new JSONObject();
			obj.put("siteName", row[0]);
			obj.put("alertType", row[1]);
			obj.put("count", row[2]);
			resultArray.put(obj);
		}
		return resultArray.toString();
	}

}*/