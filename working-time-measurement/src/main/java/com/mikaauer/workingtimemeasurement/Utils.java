package com.mikaauer.workingtimemeasurement;

import com.mikaauer.workingtimemeasurement.Holidays.HolidayManager;
import com.mikaauer.workingtimemeasurement.Vacation.Vacation;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    public static boolean isWeekend(int day, int month, int year){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,day);
        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
        boolean isWeekend = false;

        if(weekday == Calendar.SATURDAY || weekday == Calendar.SUNDAY){
            isWeekend = true;
        }

        return isWeekend;
    }

    public static String monthAsString(int month, boolean startsWithZero){
        int localMonth = month;
        if (startsWithZero){
            localMonth++;
        }

        return switch (localMonth) {
            case 1 -> "Januar";
            case 2 -> "Februar";
            case 3 -> "März";
            case 4 -> "April";
            case 5 -> "Mai";
            case 6 -> "Juni";
            case 7 -> "Juli";
            case 8 -> "August";
            case 9 -> "Septemeber";
            case 10 -> "Oktober";
            case 11 -> "November";
            case 12 -> "Dezember";
            default -> "";
        };
    }

    public static boolean isVacationDay(int day, int month, int year, List<Vacation> vacations){
        for (Vacation vacation: vacations){
            if (!isAfter(vacation.getEndDay(), vacation.getEndMonth(), vacation.getEndYear(), day, month, year)){
                if((isAfter(vacation.getStartingDay(), vacation.getStartingMonth(), vacation.getStartingYear(), day, month, year) ||
                        isEqual(vacation.getStartingDay(), vacation.getStartingMonth(), vacation.getStartingYear(), day, month, year)) &&
                        (isBefore(vacation.getEndDay(), vacation.getEndMonth(), vacation.getEndYear(), day, month, year) ||
                                isEqual(vacation.getEndDay(), vacation.getEndMonth(), vacation.getEndYear(), day, month, year))){
                    return true;
                }
            }else {
                return false;
            }
        }

        return false;
    }

    public static boolean isAfter(int baseDay, int baseMonth, int baseYear, int comparisonDay, int comparisonMonth, int comparisonYear){
        boolean res = false;
        if (comparisonYear > baseYear){
            res = true;
        }else if (comparisonYear == baseYear && comparisonMonth > baseMonth){
            res = true;
        }else if(comparisonYear == baseYear && comparisonMonth == baseMonth && comparisonDay > baseDay){
            res = true;
        }

        return res;
    }

    public static boolean isBefore(int baseDay, int baseMonth, int baseYear, int comparisonDay, int comparisonMonth, int comparisonYear){
        boolean res = false;
        if (comparisonYear < baseYear){
            res = true;
        }else if (comparisonYear == baseYear && comparisonMonth < baseMonth){
            res = true;
        }else if(comparisonYear == baseYear && comparisonMonth == baseMonth && comparisonDay < baseDay){
            res = true;
        }

        return res;
    }

    public static boolean isEqual(int baseDay, int baseMonth, int baseYear, int comparisonDay, int comparisonMonth, int comparisonYear){
        boolean res = false;
        if (comparisonYear == baseYear && comparisonMonth == baseMonth && comparisonDay == baseDay){
            res = true;
        }

        return res;
    }
}
