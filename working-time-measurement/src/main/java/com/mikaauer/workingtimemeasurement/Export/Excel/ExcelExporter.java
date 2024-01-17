package com.mikaauer.workingtimemeasurement.Export.Excel;

import com.mikaauer.workingtimemeasurement.WorkDay.WorkDay;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ExcelExporter {
    public static final String FILE_DIRECTORY = ".";
    public static final int START_X = 0;
    public static final int START_Y = 0;


    public String writeTimeFile(List<WorkDay> content){
        File currentDirectory = new File(FILE_DIRECTORY);
        String path = currentDirectory.getAbsolutePath() + "/exports/excel/";
        String fileLocation = path.substring(0, path.length()) + ((new Date()).getTime()) + ".xlsx";

        try(OutputStream os = Files.newOutputStream(Paths.get(fileLocation)); Workbook workbook = new Workbook(os, "Zeit", "1.0")){
            Worksheet worksheet = workbook.newWorksheet("Zeit");

            worksheet.value(0 + START_Y,0 + START_X,  "Datum");
            worksheet.value(0 + START_Y,1 + START_X,  "Start");
            worksheet.value(0 + START_Y,2 + START_X,  "Ende");
            worksheet.value(0 + START_Y,3 + START_X,  "Pause");
            worksheet.value(0 + START_Y,4 + START_X,  "Dauer");

            for(int i = 0; i < content.size(); i++){
                String totalTime = (content.get(i).getTotalWorkingTime() / 60)
                        + ":" + String.format("%02d",(content.get(i).getTotalWorkingTime() % 60));
                worksheet.value(1 + i + START_Y,0 + START_X,  content.get(i).getDate());
                worksheet.value(1 + i + START_Y,1 + START_X,  content.get(i).getStartingTime());
                worksheet.value(1 + i + START_Y,2 + START_X,  content.get(i).getEndTime());
                worksheet.value(1 + i + START_Y,3 + START_X,  content.get(i).getBreakDuration());
                worksheet.value(1 + i + START_Y,4 + START_X,  totalTime);
            }

            os.flush();

            return fileLocation;
        } catch (IOException e) {
            System.err.println(e);
        }

        return "";
    }
}
