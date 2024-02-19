package com.mikaauer.workingtimemeasurement.Vacation;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VacationResponseDeserializer extends StdDeserializer<VacationResponse> {


    public VacationResponseDeserializer(){
        this(null);
    }
    public VacationResponseDeserializer(Class<?> vc) {
        super(vc);
    }

    /**{
     * "items":
     *  [
     *      {
     *          "id":-1401005521,
     *          "startingDay":24,
     *          "startingMonth":2,
     *          "startingYear":2024,
     *          "endDay":28,
     *          "endMonth":2,
     *          "endYear":2024,
     *          "username":"admin",
     *          "accepted":true,
     *          "startingDate":"24.2.2024",
     *          "endDate":"28.2.2024",
     *          "vacationDays":3
     *      }
     *  ],
     *  "restVacationDays":27
     * }**/

    @Override
    public VacationResponse deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode node = p.getCodec().readTree(p);

        JsonNode allItemsNode = node.get("items");

        List<Vacation> items = new ArrayList<>();

        for(int i = 0; i < allItemsNode.size(); i++){
            JsonNode itemNode = allItemsNode.get(i);

            int id = itemNode.get("id").intValue();
            int startingDay = itemNode.get("startingDay").intValue();
            int startingMonth = itemNode.get("startingMonth").intValue();
            int startingYear = itemNode.get("startingYear").intValue();

            int endDay = itemNode.get("endDay").intValue();
            int endMonth = itemNode.get("endMonth").intValue();
            int endYear = itemNode.get("endYear").intValue();

            String username = itemNode.get("username").toString();
            boolean accepted = itemNode.get("accepted").booleanValue();

            items.add(new Vacation(id, startingDay, startingMonth, startingYear, endDay, endMonth, endYear, username, accepted));
        }

        int restVacationDays = node.get("restVacationDays").intValue();

        return new VacationResponse(items, restVacationDays);
    }
}
