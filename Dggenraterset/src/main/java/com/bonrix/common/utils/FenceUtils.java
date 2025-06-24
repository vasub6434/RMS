package com.bonrix.common.utils;

import java.util.List;

public class FenceUtils {

    public static boolean isPointInsideCircle(double lat, double lon, double centerLat, double centerLon, double radius) {
    	//System.out.println("into the circle");
    	double earthRadius = 6371000; 
        double dLat = Math.toRadians(centerLat - lat);
        double dLon = Math.toRadians(centerLon - lon);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(centerLat))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;
        return distance <= radius;
    }

    public static boolean isPointInsidePolygon(double lat, double lon, List<double[]> polygonPoints) {
       //System.out.println("into the poly");
    	int intersectCount = 0;
        for (int j = 0; j < polygonPoints.size() - 1; j++) {
            double[] p1 = polygonPoints.get(j);
            double[] p2 = polygonPoints.get(j + 1);

            if (((p1[1] > lon) != (p2[1] > lon)) &&
                    (lat < (p2[0] - p1[0]) * (lon - p1[1]) / (p2[1] - p1[1]) + p1[0])) {
                intersectCount++;
            }
        }
        return (intersectCount % 2 == 1);
    }
}
