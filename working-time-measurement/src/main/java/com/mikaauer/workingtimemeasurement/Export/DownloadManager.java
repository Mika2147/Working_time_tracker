package com.mikaauer.workingtimemeasurement.Export;

import java.io.File;
import java.sql.Time;
import java.util.*;

public class DownloadManager {

    private static DownloadManager sharedInstance;

    private Map<UUID, DownloadFile> fileMap;

    private Timer timer;

    private DownloadManager() {
        this.fileMap = fileMap;
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                cleanUp();
            }
        };

        timer.scheduleAtFixedRate(task, 10000, 30000);
    }

    public static DownloadManager getInstance(){
        if (sharedInstance == null){
            sharedInstance = new DownloadManager();
        }

        return sharedInstance;
    }

    public Optional<DownloadFile> getFile(UUID token){
        if(fileMap.containsKey(token)){
            return Optional.of(fileMap.get(token));
        }

        return Optional.empty();
    }

    public UUID insertFile(DownloadFile file){
        UUID token = UUID.randomUUID();
        while (fileMap.containsKey(token)){
            token = UUID.randomUUID();
        }

        fileMap.put(token, file);

        return token;
    }

    private void cleanUp(){
        for(UUID token: fileMap.keySet()){
            DownloadFile file = fileMap.get(token);
            if(((new Date()).getTime() - file.getCreatedAt()) > 120000 ){
                fileMap.remove(token);
                File systemFile = new File((file.getFilepath()));
                systemFile.delete();
            }
        }
    }

}
