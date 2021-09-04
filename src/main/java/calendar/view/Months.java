package calendar.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
* Months class
* This class process the days' and months' details for view class to use
*/
public class Months {

    private String currentYear;

    private final int NUM_OF_MONTHS = 12;

    private String[] monthsInOrder = {"Jan","Feb","Mar","Apr","May","Jun"
                                ,"Jul","Aug","Sep","Oct","Nov","Dec"};
    private int[] daysInMonths = {31,28,31,30,31,30,31,31,30,31,30,31};

    /**
    * Months Constructor
    * @param year The current year of month
    */
    public Months(String year) {
        this.currentYear = year;
    }

    /**
     * Get next or previous month of the given month
     * @param month The current month
     * @param next Either 1 or -1, meaning this function is getting
     * current month + 1: next month, or current month - 1: previous month
     * @return The previous or next month that asked for in String
     */
    public String getNextMonth(String month, int next) {
        for (int i = 0; i < NUM_OF_MONTHS; i++) {
            if (monthsInOrder[i] == month) {
                if (i + next == -1){
                    setCurretYearToNextYear(-1);
                    i = NUM_OF_MONTHS;
                }
                else if (i + next == NUM_OF_MONTHS) {
                    setCurretYearToNextYear(1);
                    i = -1;
                }
                return monthsInOrder[i + next];
            }
        }
        return null;
    }

    /**
     * Set current year to next or previous year
     * @param next Either 1 or -1, meaning this function is getting
     * current year + 1: next year, or current year - 1: previous year
     */
    private void setCurretYearToNextYear(int next) {
        int yearInInt = Integer.parseInt(this.currentYear);
        this.currentYear = String.valueOf(yearInInt + next);
    }

    /**
     * Returns the current saved year
     * @return the current saved year in String
     */
    public String getCurrentYear() {
        return this.currentYear;
    }

    /**
     * Get the number of days in that month
     * @param month The month to get from
     * @return the number of days in that given month
     */
    public int getDaysInMonths(String month) {
        int yearInInt = Integer.parseInt(this.currentYear);
        for (int i = 0; i < NUM_OF_MONTHS; i++) {
            if (monthsInOrder[i] == month) {
                if (month.equals("Feb") && yearInInt%4 == 0) {
                    return 29;
                }
                return daysInMonths[i];
            }
        }
        return -1;
    }

    /**
     * Get the day of week of first day in that month
     * @param month The month to get from
     * @return the day of week of first day in that month
     */
    public int getFirstDayInTheMonth(String month) {
        for (int i = 0; i < NUM_OF_MONTHS; i++) {
            if (monthsInOrder[i] == month) {
                String monthInString = String.valueOf(i+1);
                if (i < 10) {
                    monthInString = '0' + monthInString;
                }
                String dateInString = "01/" + monthInString + "/" + this.currentYear;
                return this.getDayNumber(dateInString);
            }
        }
        return -1;
    }

    /**
     * Get the day of week of a given date
     * @param ddMMyyyy The date to get, in ddMMyyyy day,month and year format
     * @return the day of week represented as int where 1 representing sunday
     * and 7 representing saturday
     */
    private int getDayNumber(String ddMMyyyy) {
        try {
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(ddMMyyyy);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.get(Calendar.DAY_OF_WEEK);
            // 1 = Sunday, 7 = Saturday
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
