package com.bonrix.dggenraterset.jobs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeSum {
    public static void main(String[] args) {
    	  // Input timestamps
        String[] timestamps = {
            "0 Days: 2 Hour: 51 Minutes: 17 Seconds",
            "0 Days: 0 Hour: 43 Minutes: 7 Seconds",
            "0 Days: 1 Hour: 32 Minutes: 59 Seconds"
        };

        // Initialize total time components
        int totalHours = 0;
        int totalMinutes = 0;
        int totalSeconds = 0;

        // Parse each timestamp and sum the time components
        for (String timestamp : timestamps) {
        	 // Regular expression to match hours, minutes, and seconds
            Pattern pattern = Pattern.compile("(\\d+) Hour: (\\d+) Minutes: (\\d+) Seconds");
            Matcher matcher = pattern.matcher(timestamp);

            if (matcher.find()) {
                int hours = Integer.parseInt(matcher.group(1));
                int minutes = Integer.parseInt(matcher.group(2));
                int seconds = Integer.parseInt(matcher.group(3));
                totalHours += hours;
                totalMinutes += minutes;
                totalSeconds += seconds;
                System.out.println("Hours: " + hours);
                System.out.println("Minutes: " + minutes);
                System.out.println("Seconds: " + seconds);
            } else {
                System.out.println("No match found.");
            }

          
        }

        // Adjust for overflow
        totalMinutes += totalSeconds / 60;
        totalSeconds = totalSeconds % 60;

        totalHours += totalMinutes / 60;
        totalMinutes = totalMinutes % 60;

        // Format and output the result
        String formattedTime = String.format("%02d:%02d:%02d", totalHours, totalMinutes, totalSeconds);
        System.out.println("Total Time: " + formattedTime);
    }
}
