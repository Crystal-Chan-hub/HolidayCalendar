package calendar.model.holiday;

import java.util.List;

public interface Holiday {

    /**
    * Set the country of the holidays this calendar refers to
    * @param country The country to be set
    */
    void setCountry(String country);

    /**
     * Retrieve the information of this given date if it is or is not a holiday
     * @param day The day of the month and year of info to be retrieved from
     * @param month The month of the day and year of info to be retrieved from
     * @param currentYear The current year of the day and month of info to be retrieved from
     * @return The ArrayList that stored the retrieved info of the date
     */
    List<String> getThisDate(String day, String month, String currentYear);
}
