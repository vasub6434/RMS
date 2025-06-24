package com.bonrix.dggenraterset.Service;

import com.bonrix.dggenraterset.Model.MessageTemplate;
import com.bonrix.dggenraterset.Repository.SMSTemplateRepository;
import com.bonrix.dggenraterset.Service.SMSTemplateService;
import com.bonrix.dggenraterset.Service.SMSTemplateServiceImp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("sMSTemplateService")
public class SMSTemplateServiceImp implements SMSTemplateService {
  @Autowired
  SMSTemplateRepository repo;
  
  public MessageTemplate findBymid(Long mid) {
    return (MessageTemplate)this.repo.findOne(mid);
  }
  
  public List<Object[]> GetVodeoconNewLiveGride(long deviceId) {
    return this.repo.VodeoconNewLiveGride(deviceId);
  }
  
  public List<Object[]> getliveGrideNewAlertTime(long deviceId, long digitalInputId) {
    return this.repo.liveGrideNewAlertTime(deviceId, digitalInputId);
  }
  
  public List<Object[]> GetVodeoconNewLiveNewGride(long deviceId) {
    return this.repo.VodeoconNewLiveNewGride(deviceId);
  }
  
  public List<Object[]> GetVodeoconSelectedLiveGride(long userId) {
    return this.repo.GetVodeoconSelectedLiveGride(userId);
  }
}