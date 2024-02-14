package com.mikaauer.workingtimemeasurement;

import com.mikaauer.workingtimemeasurement.Holidays.HolidayManager;

import java.util.Calendar;
import java.util.Date;

public class Utils {

    public static int getWorkingDaysBetweenTwoDates(Date startDate, Date endDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        int workDays = 0;

        if (startCal.getTimeInMillis() >= endCal.getTimeInMillis()) {
            return 0;
        }

        while (startCal.getTimeInMillis() <= endCal.getTimeInMillis()) {
            // TODO: check for holidays
            if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                if(!HolidayManager.getInstance().isHoliday(startCal.get(Calendar.DAY_OF_MONTH), startCal.get(Calendar.MONTH) + 1, startCal.get(Calendar.YEAR))){
                    workDays++;
                }
            }
            startCal.add(Calendar.DAY_OF_MONTH, 1);
        }

        return workDays;
    }
}
