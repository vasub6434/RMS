package com.bonrix.dggenraterset.jobs;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeExtractor {
    public static void main(String[] args) {
        String timeString = "0 Days: 2 Hour: 51 Minutes: 17 Seconds";
        
        // Regular expression to match hours, minutes, and seconds
        Pattern pattern = Pattern.compile("(\\d+) Hour: (\\d+) Minutes: (\\d+) Seconds");
        Matcher matcher = pattern.matcher(timeString);

        if (matcher.find()) {
            int hours = Integer.parseInt(matcher.group(1));
            int minutes = Integer.parseInt(matcher.group(2));
            int seconds = Integer.parseInt(matcher.group(3));

            System.out.println("Hours: " + hours);
            System.out.println("Minutes: " + minutes);
            System.out.println("Seconds: " + seconds);
        } else {
            System.out.println("No match found.");
        }
    }
}
