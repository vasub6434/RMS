package com.bonrix.dggenraterset.jobs;


import com.bonrix.dggenraterset.Service.ListenerServices;
import com.bonrix.dggenraterset.jobs.ListnerJob;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class ListnerJob {
  private static ResourceBundle rb = ResourceBundle.getBundle("socket");
  
  private Logger log = Logger.getLogger(ListnerJob.class);
  
  @Autowired
  ListenerServices ls;
  
  @PreDestroy
  public void Destroy() {
    this.log.info("Destroy ListnerJob");
  }
  
  @PostConstruct
  public void init() {
    String Ipaddress = rb.getString("ipaddress");
   /* this.log.info("Start ListnerJob");
    if (rb.containsKey("GT06"))
      this.ls.startGT06(Ipaddress, Integer.parseInt(rb.getString("GT06"))); 
    if (rb.containsKey("Tk103"))
      this.ls.startTk103(Ipaddress, Integer.parseInt(rb.getString("Tk103"))); 
    if (rb.containsKey("EnagryMeter"))
      this.ls.startEnergyMeterServer(Ipaddress, Integer.parseInt(rb.getString("EnagryMeter"))); 
    if (rb.containsKey("Radient")) {
      this.log.info("RADIENT ListnerJob ConstructEd");
      this.ls.radient(Ipaddress, Integer.parseInt(rb.getString("Radient")));
    } 
    if (rb.containsKey("Emiots")) {
      this.log.info("Emiots ListnerJob ConstructEd");
      this.ls.EmiotsServer(Ipaddress, Integer.parseInt(rb.getString("Emiots")));
    } 
    if (rb.containsKey("ATWP30C")) {
      this.log.info("AtlantaWP30C ListnerJob ConstructEd");
      this.ls.AtlantaWP30CServer(Ipaddress, Integer.parseInt(rb.getString("ATWP30C")));
    } 
    if (rb.containsKey("ModBus")) {
      this.log.info("MODBUSRTU ListnerJob ConstructEd");
      this.ls.StartMODBUS(Ipaddress, Integer.parseInt(rb.getString("ModBus")));
    } 
    if (rb.containsKey("WP30CRS485")) {
      this.log.info("WP30CRS485 ListnerJob ConstructEd");
      this.ls.StartWP30CRS485(Ipaddress, Integer.parseInt(rb.getString("WP30CRS485")));
    } 
    if (rb.containsKey("ICERDG")) {
      this.log.info("ICERDG ListnerJob ConstructEd");
      this.ls.StartICERDG(Ipaddress, Integer.parseInt(rb.getString("ICERDG")));
    } 
    if (rb.containsKey("CV2ENGMTR")) {
      this.log.info("CV2ENGMTR ListnerJob ConstructEd");
      this.ls.StartCV2EnergyMeterDevice(Ipaddress, Integer.parseInt(rb.getString("CV2ENGMTR")));
    } 
    if (rb.containsKey("WP30C1256")) {
      this.log.info("WP30C1256 ListnerJob ConstructEd");
      this.ls.StartWP30C1256(Ipaddress, Integer.parseInt(rb.getString("WP30C1256")));
    } 
    if (rb.containsKey("EnergyMeter1252")) {
      this.log.info("EnergyMeter1252 ListnerJob ConstructEd");
      this.ls.StartEnergyMeterServer1252(Ipaddress, Integer.parseInt(rb.getString("EnergyMeter1252")));
    } 
    if (rb.containsKey("TestPort")) {
      this.log.info("CV2NewDataTest ListnerJob ConstructEd");
      this.ls.StartCV2NewDataTest1233(Ipaddress, Integer.parseInt(rb.getString("TestPort")));
    } 
    if (rb.containsKey("GTLEnergyMeter")) {
      this.log.info("GTLEnergyMeterDevice ListnerJob ConstructEd");
      this.ls.StartGTLEnergyMeterDevice5113(Ipaddress, Integer.parseInt(rb.getString("GTLEnergyMeter")));
    } 
    if (rb.containsKey("GTLPowerAndDG")) {
      this.log.info("GTLPowerAndDGEnergyMeterDevice ListnerJob ConstructEd");
      this.ls.StartGTLGTLPowerAndDGEnergyMeterDevice5114(Ipaddress, Integer.parseInt(rb.getString("GTLPowerAndDG")));
    } 
    if (rb.containsKey("LIBattry")) {
      this.log.info("StartLiBattry1255 ListnerJob ConstructEd");
      this.ls.StartLiBattry1255(Ipaddress, Integer.parseInt(rb.getString("LIBattry")));
    } 
    if (rb.containsKey("Error")) {
      this.log.info("CV2EnergyMeterDeviceError ListnerJob ConstructEd");
      this.ls.StartCV2EnergyMeterDeviceError(Ipaddress, Integer.parseInt(rb.getString("Error")));
    } 
    if (rb.containsKey("ModeBusEnergyMeter")) {
        this.log.info("CV2EnergyMeterDeviceError ListnerJob ConstructEd");
        this.ls.StartModeBusBEnergyMeterServer(Ipaddress, Integer.parseInt(rb.getString("ModeBusEnergyMeter")));
      } */
    this.log.info("ListnerJob ConstructEd");
  }
}
