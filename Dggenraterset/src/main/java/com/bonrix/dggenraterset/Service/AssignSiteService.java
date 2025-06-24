package com.bonrix.dggenraterset.Service;

import com.bonrix.dggenraterset.Model.AssignSite;
import java.util.List;

public interface AssignSiteService {
  List<AssignSite> findBysiteid(Long paramLong);
  
  List<Object[]> getuserBySevices(Long paramLong1, Long paramLong2);
  
  List<Object[]> demo(Iterable<Long> paramIterable);
  
  List<Object[]> getuserfromGroup(Long paramLong1, Long paramLong2);
  
  List<AssignSite> findBysiteidAnddeviceid(Long paramLong1, Long paramLong2);
  
  List<Object[]> getuserByGroupId(Long paramLong1, Long paramLong2);
  
  String deleteUserAssignSite(long paramLong);
  
  String deleteUserGrpAssignSite(long paramLong);
  
  List<AssignSite> findBydeviceid(Long paramLong);
  
  List<Object[]> getAssignedDeviceBySite(long paramLong);
}
