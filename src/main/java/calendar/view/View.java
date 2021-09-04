package calendar.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * This class holds the methods that its children inheritance might use
 */
public abstract class View {

    /**
     * Create and set a new button with font and colour
     * @param text The text for this button
     * @return The button created
     */
    protected static Button getButton(String text) {
        Button button = new Button(text);
        button.setFont(new Font("Gill Sans", 15));
        button.setStyle("-fx-background-color: #E7F0FD;"
                        + "-fx-border-color: white;");
        return button;
    }

    /**
     * Create and set a new Label with font and size
     * @param text The text for this label
     * @return The label created
     */
    protected Label getMediumLabel(String text) {
        Label lbl = new Label(text);
        lbl.setFont(new Font("Gill Sans", 20));
        return lbl;
    }

    /**
     * Create and set a Holiday Label to Red, alignment and height to constant value
     * @param indication Indicated if this is a holiday or not
     * @param color The color this label is to be set
     * @return The label created
     */
    protected Label getSmallLabel(String text) {
        Label lbl = new Label(text);
        lbl.setFont(new Font("Gill Sans", 10));
        return lbl;
    }

    /**
     * Create and set a Holiday Label to Red, alignment and height to constant value
     * @param indication Indicated if this is a holiday or not
     * @param color The color this label is to be set
     * @return The label created
     */
    protected Label getHolidayLabel(String indication, Color color) {
        Label lbl = new Label(indication);
        lbl.setFont(new Font("Gill Sans", 15));
        if (color != null) {
            lbl.setTextFill(color);
        }
        return lbl;
    }

    /**
     * Create and set a Label's colour, alignment and height to constant value
     * @param text The text to be set in the label
     */
    protected Label getMonthLabel(String text) {
        Label lbl = new Label(text);
        lbl.setFont(new Font("Gill Sans", 25));
        lbl.setAlignment(Pos.BASELINE_CENTER);
        return lbl;
    }

    /**
     * Create a colour-filled HBox by setting the border of a HBox
     * @return the HBox that created
     */
    protected HBox createBorder() {
        HBox hbox = new HBox();
        hbox.setStyle("-fx-border-width: 48;" + "-fx-background-color: white;"
                    + "-fx-border-insets: 2;" + "-fx-border-color: #E9E7FD;");
        return hbox ;
    }

}
