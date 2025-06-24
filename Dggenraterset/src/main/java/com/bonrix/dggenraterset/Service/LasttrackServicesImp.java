package com.bonrix.dggenraterset.Service;

import com.bonrix.dggenraterset.Model.Lasttrack;
import com.bonrix.dggenraterset.Repository.LasttrackRepository;
import com.bonrix.dggenraterset.Service.LasttrackServices;
import com.bonrix.dggenraterset.Service.LasttrackServicesImp;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("lasttrackServices")
public class LasttrackServicesImp implements LasttrackServices {
  @Autowired
  LasttrackRepository repository;
  
  @PersistenceContext
  private EntityManager em;
  
  public void saveAndFlush(Lasttrack lTrack) {
    this.repository.saveAndFlush(lTrack);
  }
  
  public Lasttrack findOne(Long deviceid) {
    return (Lasttrack)this.repository.findOne(deviceid);
  }
  
  public List<Lasttrack> findByuserId(Long userId) {
    return this.repository.findByuserId(userId);
  }
  
  public List<Lasttrack> findBydeviceId(Long deviceid) {
    return this.repository.findBydeviceId(deviceid);
  }
  
  public List<Lasttrack> getLastrackDigitalData(long userId) {
    return this.repository.getLastrackDigitalData(userId);
  }
  
  public List<Object[]> getlasttackloc() {
    return this.repository.getlasttackloc();
  }
  
  public List<Object[]> getDeviceDataByIdLocation(long managerId, long siteID) {
    return this.repository.getDeviceDataByIdLocation(managerId, siteID);
  }
  
  public List<Object[]> getLiveLocation(Long deviceId) {
    return this.repository.getLiveLocation(deviceId);
  }
  
  public List<Object[]> getlivelasttracklocation(long deviceid) {
    return this.repository.getlivelasttracklocation(deviceid);
  }
  
  public List<Object[]> getUserDeviceDataByIdLocation(long userId, long siteID) {
    return this.repository.getUserDeviceDataByIdLocation(userId, siteID);
  }
  
  public List<Object[]> getDeviceLocationByManager(long managerId) {
    return this.repository.getDeviceLocationByManager(managerId);
  }
  
  public List<Object[]> getDeviceLocationByUser(long userId) {
    return this.repository.getDeviceLocationByUser(userId);
  }
  
  public List<Object[]> getAllLastTrackData(long userId) {
    return this.repository.getAllLastTrackData(userId);
  }
  
  public List<Object[]> getLiveGrideData(String deviceDate) {
    return this.repository.getLiveGrideData(deviceDate);
  }
  
  public List<Lasttrack> getLastrackGlobalDigitalData(long deviceId, String deviceDate) {
    return this.repository.getGolbalLiveGrideData(deviceId, deviceDate);
  }
  
  public List<Object[]> GetAnalogValue(long deviceId, String deviceDate, String param) {
    return this.repository.GetAnalogValue(deviceId, deviceDate, param);
  }
  
  public List<Object[]> GetVodeoconLiveGride(long deviceId,String deviceDate) {
    return this.repository.VodeoconLiveGride(deviceId,deviceDate);
  }
  
  public List<Object[]> GetVodeoconLastTrack(long deviceId) {
    return this.repository.VideoconLastTrack(deviceId);
  }
  
  public List<Object[]> getDeviceLocation(long userId, long deviceId) {
    return this.repository.getDeviceLocation(userId, deviceId);
  }
  
  public List<Object[]> GetVodeoconAnalogVoltlData(String paramId, long deviceId) {
    return this.repository.VodeoconAnalogVoltlData(paramId, deviceId);
  }
  
  public List<Object[]> getLastrackData(String query) {
    List<Object[]> data = this.em.createNativeQuery(query).getResultList();
    return data;
  }
}
