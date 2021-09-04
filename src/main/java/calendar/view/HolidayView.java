package calendar.view;

import calendar.model.Model;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Hashtable;
import java.util.Set;

/**
* HolidayView class
* This a view class that communicates with the Model model, it is responsible for
* displaying data through javafx to user.
*/
public class HolidayView extends View {

    private final int NUM_DAYS_OF_WK = 7;

    private Stage stage;
    private Months months;
    private Pane currentPane;
    private String currentYear;
    private String currentMonth;
    private Model model;
    private Hashtable<String,Scene> scenesTable = new Hashtable<>();
    private String[] monthsInOrder = {"Jan","Feb","Mar","Apr","May","Jun"
                                ,"Jul","Aug","Sep","Oct","Nov","Dec"};

    /**
    * HolidayView Constructor
    * The constructor will retrieve current month and year information for displaying calendar
    * @param stage The stage of application
    * @param model The model of application
    */
    public HolidayView(Stage stage, Model model) {
        this.model = model;
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String[] todaySplit = today.split("/");
        setCurrentMonthInString(todaySplit[1]);
        this.currentYear = (todaySplit[2]);
        this.stage = stage;
        this.months = new Months(this.currentYear);
    }

    private void setCurrentMonthInString(String monthIntInString) {
        int monthInInt = Integer.parseInt(monthIntInString);
        this.currentMonth = monthsInOrder[monthInInt - 1];
    }

    /**
     * Initialise the scene of this application
     * @return The initial scenefor this application
     */
    @SuppressWarnings("unchecked")
    public Scene initScene() {
        BorderPane pane = new BorderPane();
        ComboBox countrySelectBox = new ComboBox();
        ComboBox thresholdSelectBox = new ComboBox();

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("countries.txt").getFile());
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String country;
            while ((country = reader.readLine()) != null) {
                countrySelectBox.getItems().add(country);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        countrySelectBox.setStyle("-fx-background-color: white;"
                        + "-fx-border-color: white;");
        VBox center = new VBox(20);
        center.setAlignment(Pos.CENTER);

        thresholdSelectBox.setStyle("-fx-background-color: white;"
                        + "-fx-border-color: white;");
        for (int i = 1; i < 6; i++) {
            thresholdSelectBox.getItems().add(String.valueOf(i));
        }

        Button startButton = getButton("Start");
        startButton.setOnAction(e -> {
            String countryInput = (String) countrySelectBox.getValue();
            String thresholdInput = (String) thresholdSelectBox.getValue();
            if (countryInput != null && thresholdInput != null) {
                String[] countryCode = countryInput.split(": ");
                this.model.setHolidayCountry(countryCode[1]);
                this.model.setThresholdCount(thresholdInput);
                this.setCalendarScene();
            }
            else {
                AlertBox.display("Warning", "Please select country and threshold holiday count.");
            }
        });

        startButton.setMinWidth(150);

        center.getChildren().addAll(getMediumLabel("Welcome to Holiday calendar"),
                                    getMediumLabel("Please select a country"),
                                    countrySelectBox,
                                    getMediumLabel("Please select a threshold holiday count"),
                                    thresholdSelectBox,
                                    startButton);
        pane.setCenter(center);
        pane.setStyle("-fx-background-color: #E9E7FD");
        Scene scene = new Scene(pane, 700, 670);
        return scene;
    }

    /**
     * Set the scene of calendar
     */
    public void setCalendarScene() {
        stage.setScene(getScenesForMonthAndYr(currentMonth + currentYear));
    }

    /**
     * Get the scene for that month that year
     * @param monthAndYrToGet The month and year as the key to get that scene
     * @return The scene that was stored in the Hashtable or created
     */
    private Scene getScenesForMonthAndYr(String monthAndYrToGet) {
        Pane pane = new Pane();
        Set<String> setOfKeyInScenes = scenesTable.keySet();
        for (String s: setOfKeyInScenes) {
            if (s.equals(monthAndYrToGet)) {
                return scenesTable.get(monthAndYrToGet);
            }
        }
        VBox background = setBackground();
        pane.getChildren().add(background);
        VBox vbox = new VBox();
        Label spacelbl = new Label(" ");
        spacelbl.setMinHeight(10);
        vbox.getChildren().addAll(setHeader(),spacelbl);
        vbox.getChildren().add(setDatesViewInMonth(new VBox(52),months.getFirstDayInTheMonth(currentMonth),1));
        pane.getChildren().add(vbox);
        pane.setStyle("-fx-background-color: white");
        currentPane = pane;
        Scene newMonthScene = new Scene(pane, 700, 670);
        scenesTable.put((currentMonth + currentYear), newMonthScene);
        return newMonthScene;
    }

    /**
     * Recursive function to set the layout of the days' buttons of that month that year
     * @param rowsWithDays The rows of weeks that created already
     * @param day The day number of the week the current day is
     * @param currentDay The current day of this month
     * @return The days that are in place in this month
     */
    private VBox setDatesViewInMonth(VBox rowsWithDays,int day, int currentDay) {
        HBox daysBox = new HBox(61);
        if (currentDay == 1) {
            for (int j = 0; j < day - 1; j++) {
                // add number of empty days in the first week
                VBox dayBox = new VBox();
                dayBox.setAlignment(Pos.TOP_LEFT);
                Label lbl = new Label(" ");
                lbl.setMinWidth(40);
                dayBox.getChildren().add(lbl);
                daysBox.getChildren().add(dayBox);
            }
        }
        for (int i = currentDay; i < months.getDaysInMonths(currentMonth) + 1; i++) {
            DayView newDay = new DayView(this.model, String.valueOf(i), this);
            daysBox.getChildren().add(newDay.setDay(currentMonth, currentYear));
            day ++;
            if (day == 8) {
                day = 1;
                i++;
                rowsWithDays.getChildren().add(daysBox);
                return this.setDatesViewInMonth(rowsWithDays,day,i);
            }
        }
        rowsWithDays.getChildren().add(daysBox);
        return rowsWithDays;
    }

    /**
     * Set the header with month and year and flip buttons of this calendar
     * @return The header layout of calendar that set
     */
    private HBox setHeader() {
        HBox monthHeader = new HBox(20);
        monthHeader.setAlignment(Pos.CENTER);
        Button prevButton = getButton("<");
        Button nextButton = getButton(">");
        prevButton.setOnAction(e -> {
            String prevMonth = this.months.getNextMonth(currentMonth, -1);
            currentYear = this.months.getCurrentYear();
            if (prevMonth != null) {
                this.currentMonth = prevMonth;
                this.setCalendarScene();
            }
        });
        nextButton.setOnAction(e -> {
            String nextMonth = this.months.getNextMonth(currentMonth, 1);
            currentYear = this.months.getCurrentYear();
            if (nextMonth != null) {
                this.currentMonth = nextMonth;
                this.setCalendarScene();
            }
        });
        Label currentMonthYearLbl = getMonthLabel(currentMonth + " " + currentYear);
        monthHeader.getChildren().addAll(prevButton,currentMonthYearLbl,nextButton);
        return monthHeader;
    }

    /**
     * Set the background with borders and colours, also set the days of weeks labels
     * @return The background layout of the calendar that set
     */
    private VBox setBackground() {
        VBox bgRows = new VBox();
        HBox daysInWeek = new HBox(95);
        daysInWeek.getChildren().add(getSmallLabel("  Sun"));
        daysInWeek.getChildren().add(getSmallLabel("Mon"));
        daysInWeek.getChildren().add(getSmallLabel("Tue"));
        daysInWeek.getChildren().add(getSmallLabel("Wed"));
        daysInWeek.getChildren().add(getSmallLabel("Thu"));
        daysInWeek.getChildren().add(getSmallLabel("Fri"));
        daysInWeek.getChildren().add(getSmallLabel("Sat"));
        bgRows.setAlignment(Pos.CENTER);
        bgRows.getChildren().addAll(getMonthLabel(" "),daysInWeek);
        for (int row = 0; row < 6; row++) {
            HBox oneBgRow = new HBox();
            for (int days = 0; days < 7; days++) {
                oneBgRow.getChildren().add(createBorder());
            }
            bgRows.getChildren().add(oneBgRow);
        }
        bgRows.getChildren().add(setFooter());
        return bgRows;
    }

    /**
     * Set the footer with the send message to twilio button and descriptive label
     * @return The footer of calendar that set
     */
    private HBox setFooter() {
        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER);
        Button sendButton = getButton("Send");
        Label label = getSmallLabel("Send a list of known public holidays for this month here ");
        sendButton.setOnAction(e -> {
            String twilioReply = this.model.sendTwilioMessage(currentMonth, currentYear);
            AlertBox.display("SMS Message", twilioReply);
        });
        footer.getChildren().addAll(label, sendButton);
        return footer;
    }

    /**
     * Set the current pane background from black to white a few times.
     */
    public void blink() {
        currentPane.setStyle("-fx-background-color: black");
        PauseTransition delay1 = new PauseTransition(Duration.seconds(0.3));
        delay1.setOnFinished(e1 -> {
            currentPane.setStyle("-fx-background-color: white");
            PauseTransition delay2 = new PauseTransition(Duration.seconds(0.3));
            delay2.setOnFinished(e2 -> {
                currentPane.setStyle("-fx-background-color: black");
                PauseTransition delay3 = new PauseTransition(Duration.seconds(0.3));
                delay3.setOnFinished(e3 -> currentPane.setStyle("-fx-background-color: white"));
                delay3.play();
            });
            delay2.play();
        });
        delay1.play();
    }

}
