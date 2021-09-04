package calendar;

import calendar.view.*;
import calendar.model.*;
import calendar.model.twilio.*;
import calendar.model.holiday.*;
import calendar.model.database.*;

import java.util.List;
import javafx.stage.Stage;
import java.net.http.HttpClient;
import javafx.application.Application;



public class CalendarApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    /**
    * This class
    * @param primaryStage The javafx stage of this GUI
    * @throws IllegalArgumentException if the arguments provided are not "online/offline online/offline"
    */
    @Override
    public void start(Stage primaryStage) {
        List<String> params = getParameters().getRaw();
        HttpClient client = HttpClient.newHttpClient();

        Holiday holidayModel;
        Twilio twilioModel;
        Database databaseModel = new Database();

        if (params.get(0).equals("online")) {
            holidayModel = new HolidayImpl(client);
        }
        else if (params.get(0).equals("offline")) {
            holidayModel = new DummyHoliday();
        }
        else {throw new IllegalArgumentException("argument must be [online/offline online/offline]");}

        if (params.get(1).equals("online")) {
            twilioModel = new TwilioImpl(client);
        }
        else if (params.get(1).equals("offline")) {
            twilioModel = new DummyTwilio();
        }
        else {throw new IllegalArgumentException("argument must be [online/offline online/offline]");}

        Model model = new ModelImpl(holidayModel, twilioModel, databaseModel);
        HolidayView holidayView = new HolidayView(primaryStage, model);
        primaryStage.setTitle("Holiday Calendar");
        primaryStage.setScene(holidayView.initScene());
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            model.closeConnectionOfDatabase();
        });
    }
}
