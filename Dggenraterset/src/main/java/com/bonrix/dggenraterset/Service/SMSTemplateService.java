package com.bonrix.dggenraterset.Service;

import com.bonrix.dggenraterset.Model.MessageTemplate;
import java.util.List;

public interface SMSTemplateService {
  MessageTemplate findBymid(Long paramLong);
  
  List<Object[]> GetVodeoconNewLiveGride(long paramLong);
  
  List<Object[]> getliveGrideNewAlertTime(long paramLong1, long paramLong2);
  
  List<Object[]> GetVodeoconNewLiveNewGride(long paramLong);
  
  List<Object[]> GetVodeoconSelectedLiveGride(long paramLong);
}
