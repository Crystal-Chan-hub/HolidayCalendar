package calendar.model.twilio;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.naming.ConfigurationException;
import java.io.FileReader;
import java.io.Reader;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* TwilioImpl class
* This is a model class of the twilio output API, it is
* responsible for sending message and returning status of the message sent.
*/
public class TwilioImpl implements Twilio {

    private final HttpClient client;
    static String accountSid;
    static String authToken;
    static String reply;
    static String from;
    static String to;

    /**
    * TwilioImpl Constructor
    * The constructor will retrieve the configuration by {@link #getConfig()} from the configuration file for use in request
    * @param client The Http Client for request
    */
    public TwilioImpl(HttpClient client) {
        this.client = client;
        getConfig();
    }

    @Override
    public String sendMessage(List<String> message) {
        if (message != null) {
            String messageBody = "";
            for (String s: message) {
                messageBody += s + "\n";
            }
            String credentials = String.format("%s:%s", accountSid, authToken);
            Map<String, String> parameters = new HashMap<>();
            parameters.put("To", to);
            parameters.put("From", from);
            parameters.put("Body", messageBody);

            String content = parameters.keySet().stream()
                        .map(key -> key + "=" + URLEncoder.encode(parameters.get(key), StandardCharsets.UTF_8))
                        .collect(Collectors.joining("&"));
            try {
                String authEncBytes = Base64.getEncoder().encodeToString(credentials.getBytes("utf-8"));
                String authStringEnc = new String(authEncBytes);
                String url = String.format("https://api.twilio.com/2010-04-01/Accounts/%s/Messages.json"
                                        , accountSid);
                HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(url))
                            .setHeader("Authorization", "Basic " + authStringEnc)
                            .headers("Content-Type", "application/x-www-form-urlencoded")
                            .POST(HttpRequest.BodyPublishers.ofString(content))
                            .build();
                client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(TwilioImpl::parseReply)
                    .join();
                return reply;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return reply;
        }
        else {
            return "no known holidays in this month";
        }
    }

    /**
     * Parse the response from the API after the request and store relevant info to an ArrayList
     * @param responseBody The response from the API after a request is sent
     */
    @SuppressWarnings("unchecked")
    public static String parseReply(String responseBody) {
        try {
            JSONParser parse = new JSONParser();
            JSONObject jsonObj = (JSONObject)parse.parse(responseBody);
            String status = (String) jsonObj.get("status");
            String fromInReply = (String) jsonObj.get("from");
            String toInReply = (String) jsonObj.get("to");
            String bodySent = (String) jsonObj.get("body");
            reply = String.format("%s\n",status)
                + String.format("From %s\n",fromInReply)
                + String.format("To %s\n",toInReply)
                + String.format("Body: %s\n",bodySent);
        }
        catch (Exception e) {
            System.out.println(responseBody);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Parse the configuration file of the twilio API and store the
     * account SID, auth token, send message from and to number for later use
     */
    @SuppressWarnings("unchecked")
    public static String getConfig() {
        JSONParser parser = new JSONParser();
        try {
            Reader reader = new FileReader("config/twilioConfig.json");
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            accountSid = (String) jsonObject.get("AccountSid");
            authToken = (String) jsonObject.get("AuthToken");
            from = (String) jsonObject.get("From");
            to = (String) jsonObject.get("To");
            if (accountSid == null) {throw new ConfigurationException("accountSid has has not been configured");}
            if (authToken == null) {throw new ConfigurationException("authToken has has not been configured");}
            if (from == null) {throw new ConfigurationException("from has has not been configured");}
            if (to == null) {throw new ConfigurationException("to has has not been configured");}
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
