package com.bonrix.dggenraterset.Service;

import com.bonrix.dggenraterset.Model.Lasttrack;
import java.util.List;

public interface LasttrackServices {
  void saveAndFlush(Lasttrack paramLasttrack);
  
  Lasttrack findOne(Long paramLong);
  
  List<Lasttrack> findByuserId(Long paramLong);
  
  List<Lasttrack> findBydeviceId(Long paramLong);
  
  List<Lasttrack> getLastrackDigitalData(long paramLong);
  
  List<Object[]> getlasttackloc();
  
  List<Object[]> getDeviceDataByIdLocation(long paramLong1, long paramLong2);
  
  List<Object[]> getLiveLocation(Long paramLong);
  
  List<Object[]> getlivelasttracklocation(long paramLong);
  
  List<Object[]> getUserDeviceDataByIdLocation(long paramLong1, long paramLong2);
  
  List<Object[]> getDeviceLocationByManager(long paramLong);
  
  List<Object[]> getDeviceLocationByUser(long paramLong);
  
  List<Object[]> getAllLastTrackData(long paramLong);
  
  List<Object[]> getLiveGrideData(String paramString);
  
  List<Lasttrack> getLastrackGlobalDigitalData(long paramLong, String paramString);
  
  List<Object[]> GetAnalogValue(long paramLong, String paramString1, String paramString2);
  
  List<Object[]> GetVodeoconLiveGride(long paramLong,String deviceDate);
  
  List<Object[]> GetVodeoconLastTrack(long paramLong);
  
  List<Object[]> getDeviceLocation(long paramLong1, long paramLong2);
  
  List<Object[]> GetVodeoconAnalogVoltlData(String paramString, long paramLong);
  
  List<Object[]> getLastrackData(String paramString);
}
