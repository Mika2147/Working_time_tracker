package com.mikaauer.vacation.Database;

import com.mikaauer.vacation.Model.Vacation;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class VacationDatabaseConnector {
    private MongoClient mongoClient;

    public VacationDatabaseConnector(){
        this.mongoClient = MongoClients.create(DatabaseConstants.DATABASE_URL);
    }

    public boolean insertVacation(Vacation vacation){
        // TODO: Check if there is already a vacation in the given time range
        List<Vacation> bookedVacations = getVacations(vacation.getUsername());

        Date start = new GregorianCalendar(vacation.getStartingYear(), vacation.getStartingMonth(), vacation.getStartingDay()).getTime();
        Date end = new GregorianCalendar(vacation.getEndYear(), vacation.getEndMonth(), vacation.getEndDay()).getTime();

        if (start.getTime() > end.getTime()){
            return false;
        }

        for(Vacation comparisonVacation: bookedVacations){
            Date comparisonStart = new GregorianCalendar(comparisonVacation.getStartingYear(), comparisonVacation.getStartingMonth(), comparisonVacation.getStartingDay()).getTime();
            Date comparisonEnd = new GregorianCalendar(comparisonVacation.getEndYear(), comparisonVacation.getEndMonth(), comparisonVacation.getEndDay()).getTime();

            if(start.getTime() < comparisonStart.getTime() && end.getTime() > comparisonStart.getTime()){
                return false;
            }

            if(start.getTime() > comparisonStart.getTime() && end.getTime() < comparisonEnd.getTime()){
                return false;
            }
        }

        MongoDatabase database = mongoClient.getDatabase(DatabaseConstants.TIME_DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection(DatabaseConstants.VACATION_COLLECTION_NAME);

        Document document = new Document();
        document.put(DatabaseConstants.KEY_ID, vacation.getId());

        Bson updates = Updates.combine(
                Updates.set(DatabaseConstants.KEY_STARTING_DAY, vacation.getStartingDay()),
                Updates.set(DatabaseConstants.KEY_STARTING_MONTH, vacation.getStartingMonth()),
                Updates.set(DatabaseConstants.KEY_STARTING_YEAR, vacation.getStartingYear()),
                Updates.set(DatabaseConstants.KEY_END_DAY, vacation.getEndDay()),
                Updates.set(DatabaseConstants.KEY_END_MONTH, vacation.getEndMonth()),
                Updates.set(DatabaseConstants.KEY_END_YEAR, vacation.getEndYear()),
                Updates.set(DatabaseConstants.KEY_USERNAME, vacation.getUsername())
        );

        UpdateOptions options = new UpdateOptions().upsert(true);

        try{
            UpdateResult result = collection.updateOne(document, updates, options);
            return result.wasAcknowledged();
        }catch(MongoException e){
            System.err.println("Unable to update due to an error: " + e);
            return false;
        }
    }

    public List<Vacation> getVacations(int year, String username){
        MongoDatabase database = mongoClient.getDatabase(DatabaseConstants.TIME_DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection(DatabaseConstants.VACATION_COLLECTION_NAME);

        Document searchQuery = new Document();
        searchQuery.put(DatabaseConstants.KEY_STARTING_YEAR, year);
        searchQuery.put(DatabaseConstants.KEY_USERNAME, username);
        FindIterable<Document> cursor = collection.find(searchQuery);

        List<Vacation> vacations = new ArrayList<>();

        try (final MongoCursor<Document> cursorIterator = cursor.cursor()) {
            while (cursorIterator.hasNext()) {
                Document document = cursorIterator.next();

                Vacation vacation = extractVacationFromDocument(document);
                vacations.add(vacation);
            }
        }

        searchQuery = new Document();
        searchQuery.put(DatabaseConstants.KEY_END_YEAR, year);
        searchQuery.put(DatabaseConstants.KEY_USERNAME, username);
        cursor = collection.find(searchQuery);

        try (final MongoCursor<Document> cursorIterator = cursor.cursor()) {
            while (cursorIterator.hasNext()) {
                Document document = cursorIterator.next();

                Vacation vacation = extractVacationFromDocument(document);
                vacations.add(vacation);
            }
        }

        vacations = vacations.stream().distinct().collect(Collectors.toList());

        return vacations;
    }

    public List<Vacation> getVacations(String username){
        MongoDatabase database = mongoClient.getDatabase(DatabaseConstants.TIME_DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection(DatabaseConstants.VACATION_COLLECTION_NAME);

        Document searchQuery = new Document();
        searchQuery.put(DatabaseConstants.KEY_USERNAME, username);
        FindIterable<Document> cursor = collection.find(searchQuery);

        List<Vacation> vacations = new ArrayList<>();

        try (final MongoCursor<Document> cursorIterator = cursor.cursor()) {
            while (cursorIterator.hasNext()) {
                Document document = cursorIterator.next();

                Vacation vacation = extractVacationFromDocument(document);
                vacations.add(vacation);
            }
        }

        return vacations;
    }

    public List<Vacation> getFutureVacations(String username){
        MongoDatabase database = mongoClient.getDatabase(DatabaseConstants.TIME_DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection(DatabaseConstants.VACATION_COLLECTION_NAME);

        Document searchQuery = new Document();
        searchQuery.put(DatabaseConstants.KEY_USERNAME, username);
        FindIterable<Document> cursor = collection.find(searchQuery);

        LocalDate now = LocalDate.now();

        int currentDay = now.getDayOfMonth();
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();

        List<Vacation> vacations = new ArrayList<>();

        try (final MongoCursor<Document> cursorIterator = cursor.cursor()) {
            while (cursorIterator.hasNext()) {
                Document document = cursorIterator.next();

                Vacation vacation = extractVacationFromDocument(document);

                if(vacation.getEndYear() > currentYear){
                    vacations.add(vacation);
                }else if (vacation.getEndYear() == currentYear && vacation.getEndMonth() > currentMonth){
                    vacations.add(vacation);
                }else if(vacation.getEndYear() == currentYear && vacation.getEndMonth() == currentMonth && vacation.getEndDay() >= currentDay){
                    vacations.add(vacation);
                }
            }
        }

        return vacations;
    }

    public Optional<Vacation> getVacation(int id, String username){
        MongoDatabase database = mongoClient.getDatabase(DatabaseConstants.TIME_DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection(DatabaseConstants.VACATION_COLLECTION_NAME);

        Document searchQuery = new Document();
        searchQuery.put(DatabaseConstants.KEY_ID, id);
        searchQuery.put(DatabaseConstants.KEY_USERNAME, username);
        FindIterable<Document> cursor = collection.find(searchQuery);


        try (final MongoCursor<Document> cursorIterator = cursor.cursor()) {
            if (cursorIterator.hasNext()) {
                Document document = cursorIterator.next();

                Vacation vacation = extractVacationFromDocument(document);
                return Optional.of(vacation);
            }
        }

        return Optional.empty();
    }

    private Vacation extractVacationFromDocument(Document document){
        int documentStartDay = document.getInteger(DatabaseConstants.KEY_STARTING_DAY);
        int documentStartMonth = document.getInteger(DatabaseConstants.KEY_STARTING_MONTH);
        int documentStartYear = document.getInteger(DatabaseConstants.KEY_STARTING_YEAR);
        int documentEndDay = document.getInteger(DatabaseConstants.KEY_END_DAY);
        int documentEndMonth = document.getInteger(DatabaseConstants.KEY_END_MONTH);
        int documentEndYear = document.getInteger(DatabaseConstants.KEY_END_YEAR);
        String documentUsername = document.getString(DatabaseConstants.KEY_USERNAME);
        int documentId = document.getInteger(DatabaseConstants.KEY_ID);
        //boolean documentAccepted = document.getBoolean(DatabaseConstants.KEY_ACCEPTED);

        Vacation vacation = new Vacation(documentId,documentStartDay, documentStartMonth, documentStartYear, documentEndDay, documentEndMonth, documentEndYear, documentUsername, false);
        return vacation;
    }


}
