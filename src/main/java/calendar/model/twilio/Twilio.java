package calendar.model.twilio;

import java.util.List;

public interface Twilio {

    /**
     * Send a message to twilio API
     * @param message The list of holidays to be sent
     * @return The status and the number to and from of the sent message
     */
    String sendMessage(List<String> message);

}
