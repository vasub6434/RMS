package com.bonrix.dggenraterset.jobs;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HoursToHMS {
    public static void main(String[] args) {

        // Convert hours to seconds
       /* int totalSecs = 2 * 3600;
        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;
        int  seconds = totalSecs % 60;
        System.out.println(hours+" hours "+minutes+" minutes "+seconds);*/
    	//0.7186111111111111
    	//1.549722222222222
    	//2.854722222222222
    	double hour = 0;
        Pattern pattern = Pattern.compile("(\\d+) Days: (\\d+) Hour: (\\d+) Minutes: (\\d+) Seconds");
        Matcher matcher = pattern.matcher("0 Days: 2 Hour: 51 Minutes: 17 Seconds");
        if (matcher.find()) {
            String hours = matcher.group(2);
            String minutes = matcher.group(3);  
            String seconds = matcher.group(4);
            hour = convertToTotalHours(Integer.parseInt(hours), Integer.parseInt(minutes), Integer.parseInt(seconds)); 
            System.out.println(hour);
        }
    }
    
    public static double convertToTotalHours(int hours, int minutes, int seconds) {
        double hoursFromMinutes = minutes / 60.0;
        double hoursFromSeconds = seconds / 3600.0;
        return hours + hoursFromMinutes + hoursFromSeconds;
    }
    
    public static String timeUnitToFullTime(long time, TimeUnit timeUnit) {
        long day = timeUnit.toDays(time);
        long hour = timeUnit.toHours(time) % 24;
        long minute = timeUnit.toMinutes(time) % 60;
        long second = timeUnit.toSeconds(time) % 60;
        if (day > 0) {
            return String.format("%dday %02d:%02d:%02d", day, hour, minute, second);
        } else if (hour > 0) {
            return String.format("%d:%02d:%02d", hour, minute, second);
        } else if (minute > 0) {
            return String.format("%d:%02d", minute, second);
        } else {
            return String.format("%02d", second);
        }
    }
}