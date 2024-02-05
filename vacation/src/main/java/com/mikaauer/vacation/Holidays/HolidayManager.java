package com.mikaauer.vacation.Holidays;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HolidayManager {

    private Map<Integer, HolidayYear> holidayYearMap;

    private static HolidayManager sharedInstance = new HolidayManager();

    public static HolidayManager getInstance(){
        return sharedInstance;
    }

    private HolidayManager() {
        this.holidayYearMap = new HashMap<>();

        List<Holiday> holidayList = new ArrayList<>();
        holidayList.add(new Holiday(1, 1));
        holidayList.add(new Holiday(6, 1));
        holidayList.add(new Holiday(29,3));
        holidayList.add(new Holiday(1,4));
        holidayList.add(new Holiday(1,5));
        holidayList.add(new Holiday(9,5));
        holidayList.add(new Holiday(20,5));
        holidayList.add(new Holiday(30,5));
        holidayList.add(new Holiday(3,10));
        holidayList.add(new Holiday(1, 11));
        holidayList.add(new Holiday(24,12));
        holidayList.add(new Holiday(25,12));
        holidayList.add(new Holiday(26,12));
        holidayList.add(new Holiday(31,12));

        holidayYearMap.put(2024, new HolidayYear(holidayList, 2024));
        holidayYearMap.put(2023, new HolidayYear(holidayList, 2023));
        holidayYearMap.put(2025, new HolidayYear(holidayList, 2025));
    }

    public void addHolidayYear(int year, List<Holiday> holidays){
        holidayYearMap.put(year, new HolidayYear(holidays, year));
    }

    public boolean isHoliday(int day, int month, int year){
        if(holidayYearMap.containsKey(year)){
            return holidayYearMap.get(year).isHoliday(day, month);
        }

        return false;
    }
}