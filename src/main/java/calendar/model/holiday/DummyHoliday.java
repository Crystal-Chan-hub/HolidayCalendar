package calendar.model.holiday;

import calendar.model.holiday.Holiday;

import java.util.List;
import java.util.ArrayList;

/**
* DummyHoliday class
* This is a model class which is a dummy version of the input API, it is
* responsible for getting and returning information about holidays.
*/
public class DummyHoliday implements Holiday {

    private int year;
    private String country;
    static List<String> retrievedDate;

    public DummyHoliday() {}

    @Override
    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public List<String> getThisDate(String day, String month, String currentYear) {
        retrievedDate = new ArrayList<>();
        if (month.equals("Dec") && day.equals("25")) {
            retrievedDate.add("true");
            retrievedDate.add("This is Christmas Day");
            retrievedDate.add(String.format("%s/%s/%s: Christmas Day", day, month, currentYear));
        }
        else if (month.equals("Jan") && day.equals("1")) {
            retrievedDate.add("true");
            retrievedDate.add("This is New Year Day");
            retrievedDate.add(String.format("0%s/%s/%s: New Year Day", day, month, currentYear));
        }
        else if (month.equals("Dec") && day.equals("24")) {
            retrievedDate.add("true");
            retrievedDate.add("This is Christmas Eve");
            retrievedDate.add(String.format("%s/%s/%s: Christmas Eve", day, month, currentYear));
        }
        else {
            retrievedDate.add("false");
            retrievedDate.add(String.format("%s %s %s in %s is not a holiday",
                                                day,month,currentYear,country));
        }
        return retrievedDate;
    }
}
