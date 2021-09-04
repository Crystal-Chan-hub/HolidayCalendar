package calendar.model;

import java.util.List;

public interface Model {

    /**
    * Set the country of the holiday model this calendar refers to
    * @param country The country to be set
    */
    void setHolidayCountry(String country);

    /**
    * Set the threshold count of the holiday model this calendar refers to
    * @param thresholdCount The threshold count to be set
    */
    void setThresholdCount(String thresholdCount);

    /**
     * Store a known holiday by its month and year
     * @param holiday The dates and name of the holiday to store
     * @param month The month to store
     * @param currentYear The year of the month to store
     */
    void addKnownHolidays(String holiday, String month, String currentYear);

    /**
     * Get the saved known holidays of a given month and year
     * @param month The month to extract holidays from
     * @param currentYear The year of the month to extract holidays from
     * @return The list containing known holidays of given month and year
     */
    List<String> getKnownHolidays(String month, String currentYear);

    /**
     * Retrieve the information from the API of this given date about a holiday/non-holiday
     * the holiday model
     * @param day The day of the month and year of info to be retrieved from
     * @param month The month of the day and year of info to be retrieved from
     * @param year The current year of the day and month of info to be retrieved from
     * @return The ArrayList of the retrieved info of the date that returned from the holiday model
     */
    List<String> getHolidayThisDateFromApi(String day, String month, String year);

    /**
     * Send message of known holidays of the given month to twilio with the twilio model
     * @param month The month to extract holidays from
     * @param currentYear The year of the month to extract holidays from
     * @return The status, message body and the number to and from of the sent message
     */
    String sendTwilioMessage(String month, String currentYear);

    /**
     * Add a known holiday/non-holiday to the database
     * @param day The day of the date to be added
     * @param month The month to of the date to be added
     * @param year The year of the date to be added
     * @param dataInfo The information to be added to the database of the date
     */
    void addHolidayToDatabase(String day, String month, String year, List<String> dataInfo);

    /**
     * Check if the given date about a holiday/non-holiday is presented in the database already
     * @param day The day of the month this date
     * @param month The month of this date
     * @param year The year of this date
     * @return true if the given date is presented, false if not presented
     */
    boolean checkMatchDatabase(String day, String month, String year);

    /**
     * Retrieve the information from the database of a holiday/non-holiday with given details
     * @param day The day of the month this date
     * @param month The month of this date
     * @param year The year of this date
     * @return The ArrayList of the retrieved info of the date that returned from the database
     */
    List<String> getHolidayThisDateFromDatabase(String day, String month, String year);

    /**
     * Close the connection of the database
     */
    void closeConnectionOfDatabase();

    /**
    * @return The threshold count this calendar has set
    */
    int getThresholdCount();

    /**
    * Check if the holiday of requested date has exceeded the holiday threshold count
    * @return The boolean if the threshold has been exceeded or not
    */
    boolean exceedThreshold();

}
