package com.mikaauer.vacation;

import com.mikaauer.vacation.Model.Vacation;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
                workDays++;
            }
            startCal.add(Calendar.DAY_OF_MONTH, 1);
        }

        return workDays;
    }

    public static int getVacationDaysInYear(Vacation vacation, int year){
        int res = 0;

        Date start = new GregorianCalendar(vacation.getStartingYear(), vacation.getStartingMonth() - 1, vacation.getStartingDay()).getTime();
        Date end = new GregorianCalendar(vacation.getEndYear(), vacation.getEndMonth() - 1, vacation.getEndDay()).getTime();

        if(vacation.getStartingYear() == year && vacation.getEndYear() == year) {
            res = Utils.getWorkingDaysBetweenTwoDates(start, end);
        }else if(vacation.getStartingYear() < year && vacation.getEndYear() == year){
            start = new GregorianCalendar(year, 0, 1).getTime();
            res = Utils.getWorkingDaysBetweenTwoDates(start, end);
        }else if(vacation.getStartingYear() == year && vacation.getEndYear() > year){
            end = new GregorianCalendar(year, 11, 31).getTime();
            res = Utils.getWorkingDaysBetweenTwoDates(start, end);
        }

        return res;
    }
}
