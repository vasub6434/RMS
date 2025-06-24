package com.bonrix.common.test;
import java.time.LocalDate;

public class PrintMonthDates {
    public static void main(String[] args) {
        // Specify the year and month
        int year = 2024;
        int month = 8;

        // Get the first and last day of the month
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        // Loop through each day of the month and print the date
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            System.out.print("\"" + currentDate+"\""+" ");
            currentDate = currentDate.plusDays(1);
        }
    }
}
