package com.bonrix.dggenraterset.Utility;
import com.bonrix.dggenraterset.Utility.Candle;
import com.bonrix.dggenraterset.Utility.MovingAvg;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MovingAvg {
  static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  
  public Candle movingavrage(List<Double> doublelist) {
    Double open = Double.valueOf(0.0D);
    Double close = Double.valueOf(0.0D);
    Double high = Double.valueOf(0.0D);
    Double low = Double.valueOf(0.0D);
    Double dist = Double.valueOf(0.0D);
    Candle cndle = new Candle();
    for (int i = 0; i < doublelist.size(); i++) {
      double analogval = ((Double)doublelist.get(i)).doubleValue();
      if (i == 0) {
        low = open = Double.valueOf(analogval);
        high = Double.valueOf(analogval);
      } 
      if (analogval > high.doubleValue())
        high = Double.valueOf(analogval); 
      if (analogval < low.doubleValue())
        low = Double.valueOf(analogval); 
    } 
    close = doublelist.get(doublelist.size() - 1);
    high = Double.valueOf(round(high.doubleValue(), 2));
    low = Double.valueOf(round(low.doubleValue(), 2));
    open = Double.valueOf(round(open.doubleValue(), 2));
    close = Double.valueOf(round(close.doubleValue(), 2));
    cndle.setClose(close);
    cndle.setDist(dist);
    cndle.setHigh(high);
    cndle.setLow(low);
    cndle.setOpen(open);
    System.out.println(String.valueOf(doublelist.size() - 1) + " ==== " + doublelist.size());
    return cndle;
  }
  
  public static double round(double value, int places) {
    if (places < 0)
      throw new IllegalArgumentException(); 
    long factor = (long)Math.pow(10.0D, places);
    value *= factor;
    long tmp = Math.round(value);
    return tmp / factor;
  }
  
  public static ArrayList<Date> gerDateRange(String startDate, String endDate, int minutes) throws ParseException {
    ArrayList<Date> date = new ArrayList<>();
    Calendar gcal = Calendar.getInstance();
    Date start = sdf.parse(startDate);
    Date end = sdf.parse(endDate);
    gcal.setTime(start);
    date.add(sdf.parse(sdf.format(gcal.getTime())));
    while (gcal.getTime().before(end)) {
      Date d1 = gcal.getTime();
      gcal.add(5, 1);
      int loop = 1440 / minutes;
      String dt = sdf.format(gcal.getTime());
      date.add(sdf.parse(sdf.format(gcal.getTime())));
      for (int i = 0; i < loop; i++) {
        if (i == 0) {
          dt = sdf.format(d1);
        } else {
          Calendar cal = Calendar.getInstance();
          cal.setTime(d1);
          cal.add(12, minutes);
          dt = next(sdf.format(sdf.parse(dt)), minutes);
          date.add(sdf.parse(dt));
        } 
      } 
    } 
    Collections.sort(date);
    date.forEach(action -> System.out.println(sdf.format(action)));
    return date;
  }
  
  public static String next(String myTime, int minut) throws ParseException {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date d = sdf.parse(myTime);
    Calendar cal = Calendar.getInstance();
    cal.setTime(d);
    cal.add(12, minut);
    String newTime = df.format(cal.getTime());
    return newTime;
  }
}
