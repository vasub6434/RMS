package com.bonrix.dggenraterset.Service;

import com.bonrix.dggenraterset.Model.AnalogInputAlert;
import com.bonrix.dggenraterset.Model.Devicemaster;
import com.bonrix.dggenraterset.Model.DigitalInputAlert;
import com.bonrix.dggenraterset.Model.EmailTemplate;
import com.bonrix.dggenraterset.Model.History;
import com.bonrix.dggenraterset.Model.MessageTemplate;
import com.bonrix.dggenraterset.Model.Parameter;
import com.bonrix.dggenraterset.Model.Site;
import com.bonrix.dggenraterset.Model.Tag;
import com.bonrix.dggenraterset.Model.TagType;
import com.bonrix.dggenraterset.Model.User;
import com.bonrix.dggenraterset.Model.UserGroup;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface HistoryServices {
  void saveAndFlush(History paramHistory);
  
  List<MessageTemplate> getlistByUser_id(Long paramLong);
  
  List<TagType> findAll();
  
  List<Tag> getsubtagbytag(String paramString);
  
  MessageTemplate savemsgtemplate(MessageTemplate paramMessageTemplate);
  
  void updatemsgtemplate(MessageTemplate paramMessageTemplate);
  
  String deletemessagetemplate(Long paramLong);
  
  List<MessageTemplate> getmessagetemplatebytype(String paramString, Long paramLong);
  
  User getstafflistbymid(Long paramLong);
  
  List<UserGroup> getstaffgrouplistbymid(Long paramLong);
  
  List<Site> getsitelistbymid(Long paramLong);
  
  List<Devicemaster> getdevicelistbymid(Long paramLong);
  
  List<EmailTemplate> getemaillistBytemplatebytype(String paramString, Long paramLong);
  
  List deltameterrpt(Long paramLong, String paramString1, String paramString2, String paramString3);
  
  List deltameterrpt2(Long paramLong, String paramString1, String paramString2, String paramString3);
  
  List deltameterrpt3(Long paramLong, String paramString1, String paramString2, String paramString3);
  
  DigitalInputAlert savedigitalinputalert(DigitalInputAlert paramDigitalInputAlert);
  
  List<DigitalInputAlert> editdigitalinputalert(Long paramLong);
  
  void updatedigitalinputalert(DigitalInputAlert paramDigitalInputAlert);
  
  String deletedigitalinputalert(Long paramLong);
  
  List<Object[]> displaydigitalinputalert(Long paramLong);
  
  List getdata(String paramString1, String paramString2, String paramString3, long paramLong);
  
  Parameter findByPrmname(String paramString);
  
  List energymeterrpt2(Long paramLong, String paramString1, String paramString2, String paramString3);
  
  List<Object[]> getalertmessagebymid(Long paramLong);
  
  List<Object[]> assigndeviceprofilebyuid(Long paramLong);
  
  List<Object[]> getdevicebyprid(Long paramLong1, Long paramLong2);
  
  List<Object[]> getdevicekeyvalbydid(Long paramLong);
  
  Parameter findpOne(Long paramLong);
  
  List<Object[]> getdevicekeyvaldigitalbydid(Long paramLong);
  
  List<Object[]> getdhistorynobydid(Long paramLong, String paramString1, String paramString2);
  
  List<Object[]> getdhistoryanalogbyno(Long paramLong);
  
  List<Object[]> getdhistorydigitalbyno(Long paramLong);
  
  List<Object[]> getprofileanalogunit(Long paramLong, String paramString);
  
  History findOne(Long paramLong);
  
  List<Object[]> adminprofiledevice();
  
  List<Object[]> getdevicebyonlyprid(Long paramLong);
  
  List<Object[]> assigndeviceprofilebymanagerid(Long paramLong);
  
  List<Object[]> getdevicebyprmid(Long paramLong1, Long paramLong2);
  
  List<Object[]> displayanaloginputalert(Long paramLong);
  
  AnalogInputAlert saveanaloginputalert(AnalogInputAlert paramAnalogInputAlert);
  
  List<AnalogInputAlert> editanaloginputalert(Long paramLong);
  
  void updateanaloginputalert(AnalogInputAlert paramAnalogInputAlert);
  
  String deleteanaloginputalert(Long paramLong);
  
  List<Object[]> getDigitalHistory(Long paramLong, String paramString1, String paramString2);
  
  List<History> findBydeviceId(Long paramLong);
  
  List<Object[]> getdeviceHistoryLocation(Long paramLong1, String paramString1, String paramString2, Long paramLong2);
  
  List<Object[]> getadminHistoryLocation(Long paramLong1, String paramString1, String paramString2, Long paramLong2);
  
  List getAnalogRawData(long paramLong, String paramString1, String paramString2);
  
  List<Object[]> getperametername(Long paramLong);
  
  List getAnalogData(Long paramLong, String paramString1, String paramString2, String paramString3);
  
  List getCandleData(Long paramLong, String paramString1, String paramString2, String paramString3);
  
  List getFuelCandle(long paramLong, String paramString1, String paramString2);
  
  List getReport(Long paramLong, String paramString1, String paramString2, String paramString3);
  
  List<Object[]> getSingleDigitalParmData(String paramString1, String paramString2, String paramString3, long paramLong);
  
  List getLiveGraphData(Long paramLong, String paramString1, String paramString2, String paramString3);
  
  List<Object[]> getDeltaAnalogData(String paramString, long paramLong);
  
  List<Object[]> getDeltaSingleAnalogData(String paramString1, String paramString2, String paramString3, long paramLong);
  
  List getMonthlyDeltaDeviceCount(String paramString1, String paramString2, long paramLong);
  
  List<Object[]> getDeltaDataReport(String paramString, long paramLong);
  
  long getDeltaDataReportCount(String paramString, long paramLong);
  
  List<Object[]> getDeltaDataReportSingleParam(String paramString1, long paramLong, String paramString2);
  
  List<Object[]> getDeltaDataReportActiveClearData(String paramString1, long paramLong, String paramString2, String paramString3);
  
  List<Object[]> getElasticsearch(String paramString, int paramInt1, int paramInt2);
  
  List GetDeltaDeviceCount(String paramString);
  
  void DeleteHistoryData(String paramString);  
  
  List<Object[]> getDigitalHistoryData(String paramString1, String paramString2, String paramString3, long paramLong);
  
  List<Object[]> getalertmessagebyUserId(long paramLong, String paramString1, Iterable<Long> paramIterable, String paramString2, String paramString3);
  
  List<Object[]> displaydigitalinputalertByUserId(Long paramLong);
  
  void updateNumber(String paramString, Long paramLong);
  
  List<Object[]> displayanaloginputalertByUserId(Long paramLong);
  
  void updateAnalogAlertNumber(String paramString1, Long paramLong1, Long paramLong2, String paramString2, Long paramLong3);
  
  List<Object[]> getFuelData(long paramLong, String paramString);
  
  void deleteHistoryData(String paramString);
  
  List GetRMSDeviceCount(String paramString, long paramLong);
  
  List<Object[]> getDeltaAllData(String paramString, long paramLong);
  
  List getMyData(String paramString);
  
  List<Object[]> gerDailyBarChartData(long paramLong, String paramString1, String paramString2, String paramString3);
  
  List<Object[]> gerMonthlyBarChartData(long paramLong, String paramString1, String paramString2, String paramString3);
  
  List<Object[]> gerYearChartData(long paramLong, String paramString1, String paramString2, String paramString3);
  
  public List<Object[]> getdhistorynobydidPaginated(Long deviceid, String startdate, String enddate, int page, int size);

  int getdhistorynobydidCount(Long deviceid, String startdate, String enddate);   
  
  ResponseEntity<Map<String, Object>> getPlaybackData(int deviceId, String startDate, String endDate, String dataType);

  ResponseEntity<Map<String, Object>> getHistoryData(int deviceId, String startDate, String endDate);

  List<Map<String, Object>> getAllByDeviceIdAndDateRange(Long deviceId);

   
}
