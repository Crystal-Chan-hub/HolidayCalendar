package calendar.model;

import calendar.view.*;
import calendar.model.twilio.*;
import calendar.model.holiday.*;
import calendar.model.database.*;

import org.junit.Test;
import java.util.List;
import org.junit.Before;
import javafx.scene.Scene;
import java.util.ArrayList;
import javafx.scene.layout.Pane;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ModelTest {

    private Model model;
    private Twilio twilioMock;
    private Holiday holidayMock;
    private Database databaseMock;
    private List<String> holidaysList;

    @Before
    public void setUp() {
        twilioMock = mock(Twilio.class);
        holidayMock = mock(Holiday.class);
        databaseMock = mock(Database.class);
        model = new ModelImpl(holidayMock,twilioMock,databaseMock);
        holidaysList = new ArrayList<>();
        holidaysList.add("true");
        holidaysList.add("This is New Year Day");
        holidaysList.add("01/01/2021: Christmas Day");
    }

    @Test
    public void testModelConstructor() {
        assertNotNull(model);
    }

    @Test
    public void testSetHolidayCountry() {
        model.setHolidayCountry("AU");
        verify(holidayMock, times(1)).setCountry(anyString());
    }

    @Test
    public void testGetHolidayThisDateFromApi() {
        when(holidayMock.getThisDate(anyString(), anyString(),anyString())).thenReturn(holidaysList);
        assertNotEquals(0, model.getHolidayThisDateFromApi("1","May","2021").size());
    }

    @Test
    public void TestAddKnownHolidays() {
        model.addKnownHolidays("01","Jan","2021");
    }

    @Test
    public void TestAddAndGetKnownHolidays() {
        model.addKnownHolidays("holiday","Jan","2021");
        assertNotEquals(0, model.getKnownHolidays("Jan","2021").size());
    }

    @Test
    public void testSendTwilioMessage() {
        model.addKnownHolidays("holiday","Jan","2021");
        when(twilioMock.sendMessage(anyList())).thenReturn("test");
        assertNotNull(model.sendTwilioMessage("Jan","2021"));
    }

    @Test
    public void TestAddHolidayToDatabase() {
        model.setHolidayCountry("AU");
        model.addHolidayToDatabase("01","Jan","2021", holidaysList);
        verify(databaseMock, times(1)).addHoliday(anyString(),anyString(),anyString(),
                            anyString(),anyString(),anyString(),anyString());
    }

    @Test
    public void TestCheckMatchDatabase() {
        model.setHolidayCountry("AU");
        model.addKnownHolidays("01","Jan","2021");
        when(databaseMock.checkMatch(anyString(), anyString(), anyString(),anyString())).thenReturn(true);
        assertTrue(model.checkMatchDatabase("01","Jan","2021"));
    }

    @Test
    public void testGetHolidayThisDateFromDatabase() {
        model.setHolidayCountry("AU");
        when(databaseMock.getThisDate(anyString(), anyString(), anyString(),anyString())).thenReturn(holidaysList);
        assertNotEquals(0, model.getHolidayThisDateFromDatabase("1","May","2021").size());
    }

    @Test
    public void testSetThresholdCount() {
        model.setThresholdCount("4");
        assertEquals(4, model.getThresholdCount());
    }

    @Test
    public void testNotExceedThresholdFromAPI() {
        when(holidayMock.getThisDate(anyString(), anyString(),anyString())).thenReturn(holidaysList);
        model.setThresholdCount("2");
        assertFalse(model.exceedThreshold());
    }

    @Test
    public void testNotExceedThresholdFromDatabase() {
        when(databaseMock.getThisDate(anyString(), anyString(), anyString(),anyString())).thenReturn(holidaysList);
        model.setThresholdCount("2");
        assertFalse(model.exceedThreshold());
    }

    @Test
    public void testExceedThresholdFromAPI() {
        holidaysList.add("This is Another Holiday");
        holidaysList.add("01/01/2021: Holiday");
        when(holidayMock.getThisDate(anyString(), anyString(),anyString())).thenReturn(holidaysList);
        model.setThresholdCount("1");
        model.getHolidayThisDateFromApi("1","May","2021");
        assertTrue(model.exceedThreshold());
    }

    @Test
    public void testExceedThresholdFromDatabase() {
        model.setHolidayCountry("AU");
        holidaysList.add("This is Another Holiday");
        holidaysList.add("01/01/2021: Holiday");
        when(databaseMock.getThisDate(anyString(), anyString(), anyString(),anyString())).thenReturn(holidaysList);
        model.setThresholdCount("1");
        model.getHolidayThisDateFromDatabase("1","May","2021");
        assertTrue(model.exceedThreshold());
    }

}
