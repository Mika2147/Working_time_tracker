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
    public static final int START_X = 0;
    public static final int START_Y = 1;


    public String writeTimeFile(List<WorkDay> content, List<Vacation> vacations, int month, int year) {
        File currentDirectory = new File(FILE_DIRECTORY);
        String path = currentDirectory.getAbsolutePath() + "/exports/excel/";
        String fileLocation = path.substring(0, path.length()) + ((new Date()).getTime()) + ".xlsx";

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        int monthLength = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        try (OutputStream os = Files.newOutputStream(Paths.get(fileLocation)); Workbook workbook = new Workbook(os, "Zeit", "1.0")) {
            Worksheet worksheet = workbook.newWorksheet("Zeit");

            worksheet.value(0, 0, Utils.monthAsString(month, false) + " " + year);
            worksheet.style(0, 0).bold().fontSize(26).set();
            worksheet.value(0 + START_Y, 0 + START_X, "Datum");
            worksheet.value(0 + START_Y, 1 + START_X, "Start");
            worksheet.value(0 + START_Y, 2 + START_X, "Ende");
            worksheet.value(0 + START_Y, 3 + START_X, "Pause");
            worksheet.value(0 + START_Y, 4 + START_X, "Dauer");
            worksheet.value(0 + START_Y, 5 + START_X, "Bilanz");
            worksheet.value(0 + START_Y, 6 + START_X, "Aufgabe");
            worksheet.value(0 + START_Y, 7 + START_X, "Kommentar");

            worksheet.style(0 + START_Y, 0 + START_X).bold().fontSize(12).set();
            worksheet.style(0 + START_Y, 1 + START_X).bold().fontSize(12).set();
            worksheet.style(0 + START_Y, 2 + START_X).bold().fontSize(12).set();
            worksheet.style(0 + START_Y, 3 + START_X).bold().fontSize(12).set();
            worksheet.style(0 + START_Y, 4 + START_X).bold().fontSize(12).set();
            worksheet.style(0 + START_Y, 5 + START_X).bold().fontSize(12).set();
            worksheet.style(0 + START_Y, 6 + START_X).bold().fontSize(12).set();
            worksheet.style(0 + START_Y, 7 + START_X).bold().fontSize(12).set();

            int contentCounter = 0;
            int totalTimeBalance = 0;

            for (int i = 0; i < monthLength; i++) {
                if (contentCounter < content.size() && content.get(contentCounter).getDay() == (i - 1)) {
                    String totalTime = (content.get(contentCounter).getTotalWorkingTime() / 60)
                            + ":" + String.format("%02d", (content.get(contentCounter).getTotalWorkingTime() % 60));
                    int balanceValue = content.get(contentCounter).getTotalWorkingTime() - (8 * 60);
                    String balance = (balanceValue / 60)
                            + ":" + String.format("%02d", (balanceValue % 60));

                    totalTimeBalance += balanceValue;

                    worksheet.value(1 + i + START_Y, 0 + START_X, content.get(contentCounter).getDate());
                    worksheet.value(1 + i + START_Y, 1 + START_X, content.get(contentCounter).getStartingTime());
                    worksheet.value(1 + i + START_Y, 2 + START_X, content.get(contentCounter).getEndTime());
                    worksheet.value(1 + i + START_Y, 3 + START_X, content.get(contentCounter).getBreakDuration());
                    worksheet.value(1 + i + START_Y, 4 + START_X, totalTime);
                    worksheet.value(1 + i + START_Y, 5 + START_X, balance);
                    worksheet.value(1 + i + START_Y, 6 + START_X, content.get(contentCounter).getTasks());
                    worksheet.value(1 + i + START_Y, 7 + START_X, content.get(contentCounter).getComment());
                    contentCounter++;
                } else if (HolidayManager.getInstance().isHoliday(i, month, year)) {
                    fillEmptyRow(i, month, year, "Feiertag", true, worksheet);
                } else if (Utils.isVacationDay(i, month, year, vacations)) {
                    fillEmptyRow(i, month, year, "Urlaub", true, worksheet);
                } else if (Utils.isWeekend((i + 1), month, year)) {
                    fillEmptyRow(i, month, year, "Wochenende", true, worksheet);
                } else {
                    worksheet.value(1 + i + START_Y, 0 + START_X, ((i + 1) + "." + month + "." + year));
                    worksheet.style(1 + i + START_Y, 0 + START_X).format("dd.MM.yyyy").set();
                    worksheet.value(1 + i + START_Y, 1 + START_X, "-");
                    worksheet.value(1 + i + START_Y, 2 + START_X, "-");
                    worksheet.value(1 + i + START_Y, 3 + START_X, "-");
                    worksheet.value(1 + i + START_Y, 4 + START_X, 0);
                    worksheet.value(1 + i + START_Y, 5 + START_X, "-8:00");
                    worksheet.value(1 + i + START_Y, 6 + START_X, "");
                    worksheet.value(1 + i + START_Y, 7 + START_X, "");

                    totalTimeBalance -= (8 * 60);
                }



            }

            String formattedBalance = (totalTimeBalance / 60)
                    + ":" + String.format("%02d", Math.abs(totalTimeBalance % 60));
            worksheet.value(1 + monthLength + START_Y, 5 + START_X, formattedBalance);
            worksheet.style(1 + monthLength + START_Y, 5 + START_X).bold().set();

            worksheet.width(6 + START_X, 50);
            worksheet.width(7 + START_X, 50);

            os.flush();

            return fileLocation;
        } catch (IOException e) {
            System.err.println(e);
        }

        return "";
    }

    private void fillEmptyRow(int i, int month, int year, String reason, boolean fillColored, Worksheet worksheet) {
        worksheet.value(1 + i + START_Y, 0 + START_X, ((i + 1) + "." + month + "." + year));
        worksheet.value(1 + i + START_Y, 1 + START_X, "-");
        worksheet.value(1 + i + START_Y, 2 + START_X, "-");
        worksheet.value(1 + i + START_Y, 3 + START_X, "-");
        worksheet.value(1 + i + START_Y, 4 + START_X, 0);
        worksheet.value(1 + i + START_Y, 5 + START_X, 0);
        worksheet.value(1 + i + START_Y, 6 + START_X, reason);
        worksheet.value(1 + i + START_Y, 7 + START_X, "");

        if (fillColored) {
            worksheet.style(1 + i + START_Y, 0 + START_X).fillColor("FFFF00").set();
            worksheet.style(1 + i + START_Y, 1 + START_X).fillColor("FFFF00").set();
            worksheet.style(1 + i + START_Y, 2 + START_X).fillColor("FFFF00").set();
            worksheet.style(1 + i + START_Y, 3 + START_X).fillColor("FFFF00").set();
            worksheet.style(1 + i + START_Y, 4 + START_X).fillColor("FFFF00").set();
            worksheet.style(1 + i + START_Y, 5 + START_X).fillColor("FFFF00").set();
            worksheet.style(1 + i + START_Y, 6 + START_X).fillColor("FFFF00").set();
            worksheet.style(1 + i + START_Y, 7 + START_X).fillColor("FFFF00").set();
        }
    }


}
