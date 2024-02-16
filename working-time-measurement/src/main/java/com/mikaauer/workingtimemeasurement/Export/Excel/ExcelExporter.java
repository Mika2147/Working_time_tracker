package com.mikaauer.workingtimemeasurement.Export.Excel;

import com.mikaauer.workingtimemeasurement.Holidays.HolidayManager;
import com.mikaauer.workingtimemeasurement.Utils;
import com.mikaauer.workingtimemeasurement.Vacation.Vacation;
import com.mikaauer.workingtimemeasurement.WorkDay.WorkDay;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ExcelExporter {
    public static final String FILE_DIRECTORY = ".";
    public static final int START_X = 1;
    public static final int START_Y = 1;


    public String writeTimeFile(List<WorkDay> content, List<Vacation> vacations, int month, int year){
        File currentDirectory = new File(FILE_DIRECTORY);
        String path = currentDirectory.getAbsolutePath() + "/exports/excel/";
        String fileLocation = path.substring(0, path.length()) + ((new Date()).getTime()) + ".xlsx";

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        int monthLength = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        try(OutputStream os = Files.newOutputStream(Paths.get(fileLocation)); Workbook workbook = new Workbook(os, "Zeit", "1.0")){
            Worksheet worksheet = workbook.newWorksheet("Zeit");

            worksheet.value(0, 0, Utils.monthAsString(month, false) + " " + year);

            worksheet.value(0 + START_Y,0 + START_X,  "Datum");
            worksheet.value(0 + START_Y,1 + START_X,  "Start");
            worksheet.value(0 + START_Y,2 + START_X,  "Ende");
            worksheet.value(0 + START_Y,3 + START_X,  "Pause");
            worksheet.value(0 + START_Y,4 + START_X,  "Dauer");
            worksheet.value(0 + START_Y, 5 + START_X, "Bilanz");
            worksheet.value(0 + START_Y,6 + START_X,  "Aufgabe");
            worksheet.value(0 + START_Y, 6 + START_X, "Kommentar");

            int contentCounter = 0;
            for(int i = 0; i < monthLength; i++){
                if(contentCounter < content.size() && content.get(contentCounter).getDay() == i){
                    String totalTime = (content.get(contentCounter).getTotalWorkingTime() / 60)
                            + ":" + String.format("%02d",(content.get(contentCounter).getTotalWorkingTime() % 60));
                    int balanceValue = content.get(contentCounter).getTotalWorkingTime() - (8*60);
                    String balance = (balanceValue / 60)
                            + ":" + String.format("%02d",(balanceValue % 60));

                    worksheet.value(1 + i + START_Y,0 + START_X,  content.get(contentCounter).getDate());
                    worksheet.value(1 + i + START_Y,1 + START_X,  content.get(contentCounter).getStartingTime());
                    worksheet.value(1 + i + START_Y,2 + START_X,  content.get(contentCounter).getEndTime());
                    worksheet.value(1 + i + START_Y,3 + START_X,  content.get(contentCounter).getBreakDuration());
                    worksheet.value(1 + i + START_Y,4 + START_X,  totalTime);
                    worksheet.value(1 + i + START_Y,5 + START_X, balance);
                    worksheet.value(1 + i + START_Y,6 + START_X, content.get(contentCounter).getTasks());
                    worksheet.value(1 + i + START_Y,7 + START_X, content.get(contentCounter).getComment());
                    contentCounter++;
                }else if(HolidayManager.getInstance().isHoliday(i, month, year)){
                    fillEmptyRow(i, month, year, "Feiertag", worksheet);
                }else if (Utils.isVacationDay(i, month, year, vacations)){
                    fillEmptyRow(i, month, year, "Urlaub", worksheet);
                }else if (Utils.isWeekend((i+1), month, year)){
                    fillEmptyRow(i, month, year, "Wochenende", worksheet);
                }else{
                    worksheet.value(1 + i + START_Y,0 + START_X,  ((i+1) + "." + month + "." + year));
                    worksheet.value(1 + i + START_Y,1 + START_X,  "-");
                    worksheet.value(1 + i + START_Y,2 + START_X,  "-");
                    worksheet.value(1 + i + START_Y,3 + START_X,  "-");
                    worksheet.value(1 + i + START_Y,4 + START_X,  0);
                    worksheet.value(1 + i + START_Y,5 + START_X, "-8:00");
                    worksheet.value(1 + i + START_Y,6 + START_X, "");
                    worksheet.value(1 + i + START_Y,7 + START_X, "");
                }

            }

            os.flush();

            return fileLocation;
        } catch (IOException e) {
            System.err.println(e);
        }

        return "";
    }

    private void fillEmptyRow(int i, int month, int year, String reason, Worksheet worksheet){
        worksheet.value(1 + i + START_Y,0 + START_X,  ((i+1) + "." + month + "." + year));
        worksheet.value(1 + i + START_Y,1 + START_X,  "-");
        worksheet.value(1 + i + START_Y,2 + START_X,  "-");
        worksheet.value(1 + i + START_Y,3 + START_X,  "-");
        worksheet.value(1 + i + START_Y,4 + START_X,  0);
        worksheet.value(1 + i + START_Y,5 + START_X, 0);
        worksheet.value(1 + i + START_Y,6 + START_X, reason);
        worksheet.value(1 + i + START_Y,7 + START_X, "");
    }


}
