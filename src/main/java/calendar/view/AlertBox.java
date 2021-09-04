package calendar.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class is to create an alert box.
 */
public class AlertBox extends View {

    /**
     * Display a pop-up window with given details, this window need to be closed
     * in order to continue to perform further actions
     * @param title The title of the window
     * @param message The message this window will display
     */
    public static void display(String title, String message) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setMinWidth(150);

        Label messageLabel = new Label(message);
        messageLabel.setFont(new Font("Gill Sans", 15));
        Button closeButton = getButton("Close");
        closeButton.setOnAction(e -> stage.close());

        Pane pane = new Pane();
        VBox vb = new VBox(10);
        vb.setPadding(new Insets(20, 20, 20, 20));
        vb.getChildren().addAll(messageLabel, closeButton);
        vb.setAlignment(Pos.CENTER);

        pane.getChildren().add(vb);
        pane.setStyle("-fx-background-color: #E9E7FD");
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.showAndWait();
    }

    /**
     * Display a pop-up window to choose to retrieve info from database or API, the selection will
     * be passed to the DayView class.
     * This window need to be closed in order to continue to perform further actions.
     * @param dayView The DayView class object for the selection to pass to
     */
    @SuppressWarnings("unchecked")
    public static void chooseDatabaseOrApi(DayView dayView) {
        dayView.storeSelectedSource(null);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Please choose");
        stage.setMinWidth(150);
        ComboBox selectionBox = new ComboBox();
        selectionBox.getItems().add("Database");
        selectionBox.getItems().add("API");
        selectionBox.setStyle("-fx-background-color: white;"
                                + "-fx-border-color: white;");
        Label messageLabel = new Label("Choose where you want to get the\n" +
                                        "information of this date from");
        messageLabel.setFont(new Font("Gill Sans", 15));
        Button submitButton = getButton("Submit");
        submitButton.setOnAction(e -> {
            String selection = (String) selectionBox.getValue();
            if (selection == null) {
                display("Warning", "Please select your data source.");
            }
            else {
                dayView.storeSelectedSource(selection);
                stage.close();
            }
        });

        Pane pane = new Pane();
        VBox vb = new VBox(10);
        vb.setPadding(new Insets(20, 20, 20, 20));
        vb.getChildren().addAll(messageLabel, selectionBox, submitButton);
        vb.setAlignment(Pos.CENTER);

        pane.getChildren().add(vb);
        pane.setStyle("-fx-background-color: #E9E7FD");
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
