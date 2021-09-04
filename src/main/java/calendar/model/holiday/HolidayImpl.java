package calendar.model.holiday;

import calendar.model.holiday.Holiday;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

import java.io.File;
import java.io.Reader;
import java.io.FileReader;
import java.nio.file.Files;
import javax.naming.ConfigurationException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
* HolidayImpl class
* This a model class that communicates with input API, it is responsible
* for getting and returning information about holidays.
*/
public class HolidayImpl implements Holiday {

    static int year;
    static String day;
    static String key;
    static String country;
    static int monthInInt;
    private final HttpClient client;
    static List<String> retrievedDate;

    private final int NUM_OF_MONTHS = 12;
    private final String[] monthsInOrder = {"Jan","Feb","Mar","Apr","May","Jun"
                                ,"Jul","Aug","Sep","Oct","Nov","Dec"};

    /**
    * HolidayImpl Constructor
    * The constructor will retrieve the key by {@link #getKey()} from the configuration file for use in request
    * @param client The Http Client for request
    */
    public HolidayImpl(HttpClient client) {
        this.client = client;
        getKey();
    }

    @Override
    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public List<String> getThisDate(String day, String month, String currentYear) {
        retrievedDate = new ArrayList<>();
        for (int i = 0; i < NUM_OF_MONTHS; i++) {
            if (monthsInOrder[i] == month) {
                this.monthInInt = i + 1;
            }
        }
        this.day = day;
        this.year = Integer.parseInt(currentYear);
        String url = String.format("https://holidays.abstractapi.com/v1/?api_key=%s&country=%s&year=%s&month=%s&day=%s"
                                ,key, country, year, monthInInt, day);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenApply(HolidayImpl::parseThisDate)
            .join();
        return retrievedDate;
    }

    /**
     * Parse the response from the API after the request and store relevant info to an ArrayList
     * @param responseBody The response from the API after a request is sent
     */
    @SuppressWarnings("unchecked")
    public static String parseThisDate(String responseBody) {
        try {
            JSONParser parse = new JSONParser();
            JSONArray jsonArray = (JSONArray)parse.parse(responseBody);
            if (jsonArray.size() == 0) {
                retrievedDate.add("false");
                retrievedDate.add(String.format("%s/%d/%d in %s is not a holiday",
                                                    day,monthInInt,year,country));
            }
            else {
                retrievedDate.add("true");
                Iterator<JSONObject> iterator = (Iterator<JSONObject>) jsonArray.iterator();
                    while (iterator.hasNext()) {
                        JSONObject hDay = (JSONObject) iterator.next();
                        String name = (String) hDay.get("name");
                        String location = (String) hDay.get("location");
                        String type = (String) hDay.get("type");
                        String date = (String) hDay.get("date");
                        String data = String.format("Holiday name: %s\n",name)
                                    + String.format("Holiday location: %s\n",location)
                                    + String.format("Holiday type: %s\n",type)
                                    + String.format("Date: %s",date);
                        retrievedDate.add(data);
                        retrievedDate.add(date + ": " + name);
                    }
            }
        }
        catch (Exception e) {
            System.out.println(responseBody);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Parse the configuration file of the holiday API and store the key for later use
     */
    @SuppressWarnings("unchecked")
    public static String getKey() {
        JSONParser parser = new JSONParser();
        try {
            Reader reader = new FileReader("config/holidayConfig.json");
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            key = (String) jsonObject.get("key");
            if (key == null) {throw new ConfigurationException("Key has has not been configured");}
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
