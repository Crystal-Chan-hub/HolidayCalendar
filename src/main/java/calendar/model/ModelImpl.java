package calendar.model;

import calendar.model.database.Database;
import calendar.model.holiday.Holiday;
import calendar.model.twilio.Twilio;

import java.util.*;

/**
* ModelImpl class
* This is a model class that communicates with views and sub-models.
* It gets data from the sub-models.
* View classes get info of sub-models through this model.
*/
public class ModelImpl implements Model {

    private int threshold;
    private String country;
    private final Database databaseModel;
    private final Twilio twilioModel;
    private final Holiday holidayModel;
    private int currentHolidayInfoSize;
    private Map<String, List<String>> knownHolidays;

    public ModelImpl(Holiday holidayModel, Twilio twilioModel, Database databaseModel) {
        this.databaseModel = databaseModel;
        this.holidayModel = holidayModel;
        this.twilioModel = twilioModel;
        knownHolidays = new HashMap<>();
        databaseModel.connect();
    }

    @Override
    public void setHolidayCountry(String country) {
        this.holidayModel.setCountry(country);
        this.country = country;
    }

    @Override
    public void setThresholdCount(String thresholdCount) {
        this.threshold = Integer.parseInt(thresholdCount);
    }

    @Override
    public void addKnownHolidays(String holiday, String month, String currentYear) {
        if (this.exceedThreshold()) {
            holiday = "*" + holiday;
        }
        boolean existed = false;
        String monthAndYr = month + currentYear;
        Set<String> setOfKeyInHolidays = knownHolidays.keySet();
        for (String s: setOfKeyInHolidays) {
            if (s.equals(monthAndYr)) {
                for (String k: this.knownHolidays.get(s)) {
                    if (k.equals(holiday)) {
                        existed = true;
                    }
                }
                if (!existed) {
                    this.knownHolidays.get(s).add(holiday);
                }
                return;
            }
        }
        List<String> holidays = new ArrayList<>();
        holidays.add(holiday);
        this.knownHolidays.put(monthAndYr,holidays);
    }

    @Override
    public List<String> getKnownHolidays(String month, String currentYear) {
        String monthAndYr = month + currentYear;
        Set<String> setOfKeyInHolidays = knownHolidays.keySet();
        for (String s: setOfKeyInHolidays) {
            if (s.equals(monthAndYr)) {
                return this.knownHolidays.get(s);
            }
        }
        return null;
    }

    @Override
    public List<String> getHolidayThisDateFromApi(String day, String month, String year) {
        List<String> info = this.holidayModel.getThisDate(day, month, year);
        this.currentHolidayInfoSize = info.size();
        if (info.get(0).equals("true")) {
            this.addKnownHolidays(info.get(2),month, year);
        }
        this.addHolidayToDatabase(day, month, year, info);
        return info;
    }

    @Override
    public String sendTwilioMessage(String month, String currentYear) {
        return this.twilioModel.sendMessage(this.getKnownHolidays(month, currentYear));
    }

    @Override
    public void addHolidayToDatabase(String day, String month, String year, List<String> dataInfo) {
        if (dataInfo.get(0).equals("true")) {
            for (int i = 1; i < dataInfo.size(); i += 2) {
                this.databaseModel.addHoliday(country, day, month, year, dataInfo.get(0), dataInfo.get(i), dataInfo.get(i+1));
            }
        }
        else {
            this.databaseModel.addHoliday(country, day, month, year, dataInfo.get(0), dataInfo.get(1), null);
        }
    }

    @Override
    public boolean checkMatchDatabase(String day, String month, String year) {
        return this.databaseModel.checkMatch(country, day, month, year);
    }

    @Override
    public List<String> getHolidayThisDateFromDatabase(String day, String month, String year) {
        List<String> info = this.databaseModel.getThisDate(country, day, month, year);
        this.currentHolidayInfoSize = info.size();
        if (info.get(0).equals("true")) {
            this.addKnownHolidays(info.get(2),month, year);
        }
        return info;
    }

    @Override
    public void closeConnectionOfDatabase() {
        databaseModel.closeConnection();
    }

    @Override
    public boolean exceedThreshold() {
        int holidaysCount = (this.currentHolidayInfoSize - 1)/ 2;
        // minus 1 of the boolean stating this is holiday or not,
        // divided by 2 because there is 2 info per holiday
        if (holidaysCount > threshold) {
            return true;
        }
        return false;
    }

    @Override
    public int getThresholdCount() {
        return this.threshold;
    }

}
