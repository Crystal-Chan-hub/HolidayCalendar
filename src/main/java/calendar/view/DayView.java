package calendar.view;

import calendar.model.Model;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for a single day view
 */
public class DayView extends View {

    private final Color holidayColor = Color.web("#c03f43");
    private final Color nonHolidayColor = Color.web("#5972a6");

    private String date;
    private List<String> holidayInfo;
    private boolean accessed;
    private Circle indication;
    private Model model;
    private String selectedSource;
    private HolidayView holidayView;

    /**
     * DayView constructor
     * @param date The date of this day view class
     */
    public DayView(Model model, String date, HolidayView holidayView) {
        Circle indication = new Circle();
        this.model = model;
        this.date = date;
        this.holidayView = holidayView;
    }

    /**
     * Set the day with button and when the button is clicked, information of a
     * holiday will be display
     * @param model The Model model for this view to perform actions
     * @parma currentMonth The current month of this day
     * @param currentYear The current year of current month of this day
     */
    public VBox setDay(String currentMonth, String currentYear) {
        holidayInfo = new ArrayList<>();
        VBox dayBox = new VBox();
        dayBox.setAlignment(Pos.TOP_LEFT);

        Button dateButton = getButton(date);
        dateButton.setMinWidth(40);
        dayBox.getChildren().add(dateButton);
        Label emptyLbl = getHolidayLabel(" ", null);
        dayBox.getChildren().add(emptyLbl);
        dateButton.setOnAction(e -> {
            if (model.checkMatchDatabase(date,currentMonth,currentYear)) {
                holidayInfo = null;
                AlertBox.chooseDatabaseOrApi(this);
                if (selectedSource != null && selectedSource.equals("Database")) {
                    holidayInfo = model.getHolidayThisDateFromDatabase(date,currentMonth,currentYear);
                }
                else if (selectedSource != null && selectedSource.equals("API")){
                    holidayInfo = model.getHolidayThisDateFromApi(date,currentMonth,currentYear);
                }
            }
            else {
                holidayInfo = model.getHolidayThisDateFromApi(date,currentMonth,currentYear);
            }
            if (holidayInfo != null && holidayInfo.get(0).equals("true")) {
                if (!accessed) {
                    dayBox.getChildren().remove(emptyLbl);
                    dayBox.getChildren().add(getHolidayLabel("H", holidayColor));
                    accessed = true;
                }
                if (model.exceedThreshold()) {
                    holidayView.blink();
                }
                AlertBox.display("This is a holiday", holidayInfo.get(1));
            }
            else if (holidayInfo != null) {
                if (!accessed) {
                    dayBox.getChildren().remove(emptyLbl);
                    dayBox.getChildren().add(getHolidayLabel("NH", nonHolidayColor));
                    accessed = true;
                }
                AlertBox.display("This is not a holiday", holidayInfo.get(1));
            }
        });
        return dayBox;
    }

    /**
     * Store the selection from the AlertBox of retrieving data from database or API
     * @param selectedSource The selected source to retrieve data from
     */
    public void storeSelectedSource(String selectedSource) {
        this.selectedSource = selectedSource;
    }

}
