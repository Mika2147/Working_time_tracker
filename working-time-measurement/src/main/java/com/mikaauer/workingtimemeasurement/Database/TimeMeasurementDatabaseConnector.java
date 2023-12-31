package com.mikaauer.workingtimemeasurement.Database;

import com.mikaauer.workingtimemeasurement.WorkDay.WorkDay;
import com.mongodb.client.*;
import org.bson.Document;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class TimeMeasurementDatabaseConnector {
    private MongoClient mongoClient;

    public TimeMeasurementDatabaseConnector() {
        this.mongoClient = MongoClients.create(DatabaseConstants.DATABASE_URL);
    }

    public boolean insertWorkday(WorkDay workDay){
        MongoDatabase database = mongoClient.getDatabase(DatabaseConstants.TIME_DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection(DatabaseConstants.WORKDAYS_COLLECTION_NAME);
        Document document = new Document();
        document.put(DatabaseConstants.KEY_DAY, workDay.getDay());
        document.put(DatabaseConstants.KEY_MONTH, workDay.getMonth());
        document.put(DatabaseConstants.KEY_YEAR, workDay.getYear());

        document.put(DatabaseConstants.KEY_STARTING_HOUR, workDay.getStartingHour());
        document.put(DatabaseConstants.KEY_STARTING_MINUTE, workDay.getStartingMinute());

        document.put(DatabaseConstants.KEY_END_HOUR, workDay.getEndHour());
        document.put(DatabaseConstants.KEY_END_MINUTE, workDay.getEndMinute());

        document.put(DatabaseConstants.KEY_BREAK_DURATION, workDay.getBreakDuration());
        return collection.insertOne(document).wasAcknowledged();
    }

    public List<WorkDay> getWorkdays(int month, int year){
        MongoDatabase database = mongoClient.getDatabase(DatabaseConstants.TIME_DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection(DatabaseConstants.WORKDAYS_COLLECTION_NAME);
        Document searchQuery = new Document();
        searchQuery.put(DatabaseConstants.KEY_MONTH, month);
        searchQuery.put(DatabaseConstants.KEY_YEAR, year);
        FindIterable<Document> cursor = collection.find(searchQuery);

        List<WorkDay> workdays = new ArrayList<>();

        try (final MongoCursor<Document> cursorIterator = cursor.cursor()) {
            while (cursorIterator.hasNext()) {
                Document document = cursorIterator.next();
                int documentDay = document.getInteger(DatabaseConstants.KEY_DAY);
                int documentMonth = document.getInteger(DatabaseConstants.KEY_MONTH);
                int documentYear = document.getInteger(DatabaseConstants.KEY_YEAR);
                int documentStartingHour = document.getInteger(DatabaseConstants.KEY_STARTING_HOUR);
                int documentStartingMinute = document.getInteger(DatabaseConstants.KEY_STARTING_MINUTE);
                int documentEndHour = document.getInteger(DatabaseConstants.KEY_END_HOUR);
                int documentEndMinute = document.getInteger(DatabaseConstants.KEY_END_MINUTE);
                int documentBreakDuration = document.getInteger(DatabaseConstants.KEY_BREAK_DURATION);
                WorkDay workDay = new WorkDay(documentDay, documentMonth, documentYear, documentStartingHour,
                        documentStartingMinute, documentEndHour, documentEndMinute, documentBreakDuration);
                workdays.add(workDay);
            }
        }

        return workdays;
    }
}
