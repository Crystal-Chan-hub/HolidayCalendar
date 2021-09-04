package calendar.model.twilio;

import calendar.model.twilio.Twilio;

import java.io.File;
import java.io.Reader;
import java.io.FileReader;
import java.nio.file.Files;

import java.util.List;
import java.util.ArrayList;
import javax.naming.ConfigurationException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
* DummyTwilio class
* This is a model class which is a dummy version of the twilio output API, it is
* responsible for sending message and returning status of the message sent.
*/
public class DummyTwilio implements Twilio {

    private String reply;
    static String from;
    static String to;

    public DummyTwilio() {
        getConfig();
    }

    @Override
    public String sendMessage(List<String> message) {
        if (message != null) {
            String messageBody = "";
            for (String s: message) {
                messageBody += s + "\n";
            }
            reply = "sent\n"
            + String.format("From %s\n",from)
            + String.format("To %s\n",to)
            + String.format("Body: %s\n",messageBody);
            return this.reply;
        }
        else {
            return "no known holidays in this month";
        }
    }

    /**
     * Parse the configuration file of the twilio API and store the
     * accound SID, auth token, send message from and to number for later use
     */
    @SuppressWarnings("unchecked")
    public static String getConfig() {
        JSONParser parser = new JSONParser();
        try {
            Reader reader = new FileReader("config/twilioConfig.json");
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            from = (String) jsonObject.get("From");
            to = (String) jsonObject.get("To");
            if (from == null) {throw new ConfigurationException("from has has not been configured");}
            if (to == null) {throw new ConfigurationException("to has has not been configured");}
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
