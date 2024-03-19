package com.mikaauer.workingtimemeasurement.Database;

import com.mikaauer.workingtimemeasurement.WorkDay.WorkDay;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TimeMeasurementDatabaseConnector {
    private MongoClient mongoClient;

    public TimeMeasurementDatabaseConnector() {
        this.mongoClient = MongoClients.create(DatabaseConstants.DATABASE_URL);
    }

    public boolean insertWorkday(WorkDay workDay) {
        MongoDatabase database = mongoClient.getDatabase(DatabaseConstants.TIME_DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection(DatabaseConstants.WORKDAYS_COLLECTION_NAME);
        Document document = new Document();
        document.put(DatabaseConstants.KEY_DAY, workDay.getDay());
        document.put(DatabaseConstants.KEY_MONTH, workDay.getMonth());
        document.put(DatabaseConstants.KEY_YEAR, workDay.getYear());
        document.put(DatabaseConstants.KEY_USERNAME, workDay.getUsername());

        Bson updates = Updates.combine(
                Updates.set(DatabaseConstants.KEY_STARTING_HOUR, workDay.getStartingHour()),
                Updates.set(DatabaseConstants.KEY_STARTING_MINUTE, workDay.getStartingMinute()),
                Updates.set(DatabaseConstants.KEY_END_HOUR, workDay.getEndHour()),
                Updates.set(DatabaseConstants.KEY_END_MINUTE, workDay.getEndMinute()),
                Updates.set(DatabaseConstants.KEY_BREAK_DURATION, workDay.getBreakDuration()),
                Updates.set(DatabaseConstants.KEY_TASKS, workDay.getTasks()),
                Updates.set(DatabaseConstants.KEY_COMMENT, workDay.getComment())
        );

        UpdateOptions options = new UpdateOptions().upsert(true);

        try {
            UpdateResult result = collection.updateOne(document, updates, options);
            return result.wasAcknowledged();
        } catch (MongoException e) {
            System.err.println("Unable to update due to an error: " + e);
            return false;
        }
    }

    public List<WorkDay> getWorkdays(int month, int year, String username) {
        MongoDatabase database = mongoClient.getDatabase(DatabaseConstants.TIME_DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection(DatabaseConstants.WORKDAYS_COLLECTION_NAME);
        Document searchQuery = new Document();
        searchQuery.put(DatabaseConstants.KEY_MONTH, month);
        searchQuery.put(DatabaseConstants.KEY_YEAR, year);
        searchQuery.put(DatabaseConstants.KEY_USERNAME, username);
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
                String documentTasks = document.getString(DatabaseConstants.KEY_TASKS);
                String documentComment = document.getString(DatabaseConstants.KEY_COMMENT);
                WorkDay workDay = new WorkDay(documentDay, documentMonth, documentYear, documentStartingHour,
                        documentStartingMinute, documentEndHour, documentEndMinute, documentBreakDuration, username, documentTasks, documentComment);
                workdays.add(workDay);
            }
        }

        return workdays;
    }

    public Optional<WorkDay> getWorkday(int day, int month, int year, String username) {
        MongoDatabase database = mongoClient.getDatabase(DatabaseConstants.TIME_DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection(DatabaseConstants.WORKDAYS_COLLECTION_NAME);
        Document searchQuery = new Document();
        searchQuery.put(DatabaseConstants.KEY_DAY, day);
        searchQuery.put(DatabaseConstants.KEY_MONTH, month);
        searchQuery.put(DatabaseConstants.KEY_YEAR, year);
        searchQuery.put(DatabaseConstants.KEY_USERNAME, username);
        FindIterable<Document> cursor = collection.find(searchQuery);

        try (final MongoCursor<Document> cursorIterator = cursor.cursor()) {
            if (cursorIterator.hasNext()) {
                Document document = cursorIterator.next();
                int documentDay = document.getInteger(DatabaseConstants.KEY_DAY);
                int documentMonth = document.getInteger(DatabaseConstants.KEY_MONTH);
                int documentYear = document.getInteger(DatabaseConstants.KEY_YEAR);
                int documentStartingHour = document.getInteger(DatabaseConstants.KEY_STARTING_HOUR);
                int documentStartingMinute = document.getInteger(DatabaseConstants.KEY_STARTING_MINUTE);
                int documentEndHour = document.getInteger(DatabaseConstants.KEY_END_HOUR);
                int documentEndMinute = document.getInteger(DatabaseConstants.KEY_END_MINUTE);
                int documentBreakDuration = document.getInteger(DatabaseConstants.KEY_BREAK_DURATION);
                String documentTasks = document.getString(DatabaseConstants.KEY_TASKS);
                String documentComment = document.getString(DatabaseConstants.KEY_COMMENT);
                String documentUsername = document.getString(DatabaseConstants.KEY_USERNAME);
                WorkDay workDay = new WorkDay(documentDay, documentMonth, documentYear, documentStartingHour,
                        documentStartingMinute, documentEndHour, documentEndMinute, documentBreakDuration, documentUsername, documentTasks, documentComment);
                return Optional.ofNullable(workDay);
            }
        }

        return Optional.empty();
    }

    public List<String> getUsernames(){
        MongoDatabase database = mongoClient.getDatabase(DatabaseConstants.TIME_DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection(DatabaseConstants.WORKDAYS_COLLECTION_NAME);
        FindIterable<Document> cursor = collection.find();

        Set<String> users = new HashSet<>();

        try (final MongoCursor<Document> cursorIterator = cursor.cursor()) {
            while (cursorIterator.hasNext()) {
                Document document = cursorIterator.next();
                String documentUsername = document.getString(DatabaseConstants.KEY_USERNAME);

                users.add(documentUsername);
            }
        }

        return users.stream().toList();
    }
}
