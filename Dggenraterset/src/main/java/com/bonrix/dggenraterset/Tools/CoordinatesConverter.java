package com.bonrix.dggenraterset.Tools;

import java.util.LinkedList;
import java.util.List;
//import org.apache.log4j.Logger;


public class CoordinatesConverter {
    
    
    public  static final double MAX_LATITUDE            = 90.0;
    public  static final double MIN_LATITUDE            = -90.0;
    
    public  static final double MAX_LONGITUDE           = 180.0;
    public  static final double MIN_LONGITUDE           = -180.0;

   // private static final Logger log = Logger.getLogger(CoordinatesConverter.class);

    public static List<Double> passLetLongVlaues(String latitude, String longitude, String latitudeDirection, String longitudeDirection) {
        
        
        Double lat = parseLatLongValue(latitude, false, latitudeDirection, longitudeDirection);
     //   log.info("lat---------"+lat);
        Double lon = parseLatLongValue(longitude, true, latitudeDirection, longitudeDirection);
       // log.info("lon---------"+lon);
        List<Double> latlong = new LinkedList<Double>();
        latlong.add(lat);
        latlong.add(lon);
        return latlong;
    }
    
    
 
    
    
    
    
    public static boolean isValid(double lat, double lon)
    {
        double latAbs = Math.abs(lat);
        double lonAbs = Math.abs(lon);
        if (latAbs >= MAX_LATITUDE) {
            // invalid latitude
            return false;
        } else
        if (lonAbs >= MAX_LONGITUDE) {
            // invalid longitude
            return false;
        } else
        if ((latAbs <= 0.0001) && (lonAbs <= 0.0001)) {
            // small square off the coast of Africa (Ghana)
            return false;
        } else {
            return true;
        }
    }

    private static double parseLatLongValue(String valueString, boolean isLongitude, String latitudeDirection, String longitudeDirection) {
        int degreeInteger = 0;
        double minutes = 0.0;
        
        if (isLongitude == true) {

            degreeInteger = Integer.parseInt(valueString.substring(0, 3));
          //  log.info("LongitudedegreeInteger---------"+degreeInteger);
            minutes = Double.parseDouble(valueString.substring(3,5)+"."+valueString.substring(5));
          //  log.info("Longitudeminutes---------"+minutes);

            //sec=Double.parseDouble(valueString.substring(5,7));
            //System.out.println("Longitudesec---------"+sec);
        } else {
            degreeInteger = Integer.parseInt(valueString.substring(0, 2));
          //  log.info("LatitudedegreeInteger---------"+degreeInteger);
           // log.info("valueString.substring(2,4)--------"+valueString.substring(2,4));
          //  log.info("valueString.substring(4)--------"+valueString.substring(4));
            minutes = Double.parseDouble(valueString.substring(2,4)+"."+valueString.substring(4));
         //   log.info("Latitudeminutes---------"+minutes);
            //sec=Double.parseDouble(valueString.substring(4,6));
            //System.out.println("Latitudesec---------"+sec);
        }
        //double degreeDecimals = minutes / 60.0;
        //double degrees = degreeInteger + degreeDecimals;
        //double degrees = degreeInteger + (0.0001*degreeDecimals);
        //double degrees=(degreeInteger+(minutes/60)+(sec/3600)) ;
        double degrees=(degreeInteger+(minutes/60)) ;
       
        if (!longitudeDirection.equals("E") && isLongitude) {
            degrees = -degrees;
        }
        if (!latitudeDirection.equals("N") && !isLongitude) {
            degrees = -degrees;
        }
        return degrees;
    }
    
    
    
     @SuppressWarnings("unused")
	private double _parseLatitude(String s, String d)
    {
        double _lat = StringTools.parseDouble(s, 99999.0);
        if (_lat < 99999.0) {
            double lat = (double)((long)_lat / 100L); // _lat is always positive here
            lat += (_lat - (lat * 100.0)) / 60.0;
            return d.equals("S")? -lat : lat;
        } else {
            return 90.0; // invalid latitude
        }
    }

    /**
    *** Parses longitude given values from GPS device.
    *** @param s Longitude String from GPS device in DDDmm.mmmm format.
    *** @param d Longitude hemisphere, "E" for eastern, "W" for western.
    *** @return Longitude parsed from GPS data, with appropriate sign based on hemisphere or
    *** 180.0 if invalid longitude provided.
    **/
    @SuppressWarnings("unused")
	private double _parseLongitude(String s, String d)
    {
        double _lon = StringTools.parseDouble(s, 99999.0);
        if (_lon < 99999.0) {
            double lon = (double)((long)_lon / 100L); // _lon is always positive here
            lon += (_lon - (lon * 100.0)) / 60.0;
            return d.equals("W")? -lon : lon;
        } else {
            return 180.0; // invalid longitude
        }
    }
}
