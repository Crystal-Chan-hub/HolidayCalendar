package calendar.model.database;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;

import java.util.List;
import java.util.ArrayList;

/**
 * Database class
 * This class is a model for caching data from the local SQLite database.
 */
public class Database {

    private List<String> retrievedDate;
    private Connection conn;

    public Database() {
        conn = null;
    }

    /**
     * Connect to the database, create a Holiday table with corresponding columns
     * if it does not exist
     */
    public void connect() {
        try {
            // db parameters
            String url = "jdbc:sqlite:Holidays.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            Statement statement = conn.createStatement();
            statement.setQueryTimeout(30);
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS holiday ("
                                    + "country string,"
                                    + " day string,"
                                    + " month string,"
                                    + " year string,"
                                    + " booleanHoliday string,"
                                    + " data string PRIMARY KEY,"
                                    + " dataForOutput string"
                                    + ");");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a row of data of the holiday/non-holiday to the table
     * @param country The country code of this holiday/non-holiday is in
     * @param day The day of the month this holiday/non-holiday is in
     * @param month The month of this holiday/non-holiday is in
     * @param year The year of this holiday/non-holiday is in
     * @param booleanHoliday The boolean as String if this is a holiday or not
     * @param data The information of this holiday/non-holiday
     * @param dataForOutput The data that is used for output of this holiday/non-holiday
     */
    public void addHoliday(String country, String day, String month, String year,
                    String booleanHoliday, String data, String dataForOutput) {
        try {
            String query = "INSERT OR REPLACE INTO holiday"
                        + " (country,day,month,year,booleanHoliday,data,dataForOutput)"
                        + " VALUES(?,?,?,?,?,?,?);";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, country);
            statement.setString(2, day);
            statement.setString(3, month);
            statement.setString(4, year);
            statement.setString(5, booleanHoliday);
            statement.setString(6, data);
            statement.setString(7, dataForOutput);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Check if the given date is presented in the table of the database already
     * @param country The country code of this holiday/non-holiday date
     * @param day The day of the month this date
     * @param month The month of this date
     * @param year The year of this date
     * @return true if the given date is presented, false if not presented
     */
    public boolean checkMatch(String country, String day, String month, String year) {
        try {
            String query = "SELECT count(*)"
                        + " FROM holiday"
                        + " WHERE country = ? and day = ? and month = ? and year = ?;";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, country);
            statement.setString(2, day);
            statement.setString(3, month);
            statement.setString(4, year);
            ResultSet result = statement.executeQuery();
            result.next();
            int count = result.getInt(1);
            if (count > 0) {
                return true;
            }
            else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieve the information of a holiday/non-holiday with given details
     * @param country The country code of this holiday/non-holiday date
     * @param day The day of the month this date
     * @param month The month of this date
     * @param year The year of this date
     * @return the ArrayList that stored the information about this holiday/non-holiday
     */
    public List<String> getThisDate(String country, String day, String month, String year) {
        try {
            retrievedDate = new ArrayList<>();
            String query = "SELECT *"
                        + " FROM holiday"
                        + " WHERE country = ? and day = ? and month = ? and year = ?;";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, country);
            statement.setString(2, day);
            statement.setString(3, month);
            statement.setString(4, year);
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                retrievedDate.add(result.getString("booleanHoliday"));
                retrievedDate.add(result.getString("data") + "\n(from database)");
                retrievedDate.add(result.getString("dataForOutput"));
            }
            return retrievedDate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Close the connection of the database
     */
    public void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
