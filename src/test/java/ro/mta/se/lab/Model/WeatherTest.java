package ro.mta.se.lab.Model;

import org.junit.Test;

import ro.mta.se.lab.Exception.HandleException;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import org.testng.annotations.BeforeMethod;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class WeatherTest{

    Weather instance;
    DataFromFile mockRn;
    ArrayList<String> arrayExpected;
    ArrayList<String> array=new ArrayList<String>();

    @BeforeMethod
    public void setUp() throws Exception {

        arrayExpected=new ArrayList<String>();
        arrayExpected.add("DE");
        arrayExpected.add("ES");
        arrayExpected.add("FR");
        arrayExpected.add("GB");
        arrayExpected.add("HU");
        arrayExpected.add("IT");
        arrayExpected.add("NL");
        arrayExpected.add("RO");
        arrayExpected.add("RU");
        arrayExpected.add("US");

        mockRn = mock(DataFromFile.class);
        when(mockRn.GetID("123","123")).thenReturn("6942372");
    }

    @Test
    public void getCountryList() throws HandleException, FileNotFoundException {

        array.add("123\t123\t123\t123\t123");
        mockRn.set_mLines(array);
        instance = new Weather(mockRn);
        assertFalse(instance.getCountryList().equals(arrayExpected));

    }

    @Test
    public void getCityList() throws FileNotFoundException, HandleException {
        array.add("123\t123\t123\t123\t123");
        mockRn.set_mLines(array);
        instance=new Weather(mockRn);
        Throwable exception = assertThrows(HandleException.class, () ->instance.getCityList("RO"));
        assertEquals("Nu exista niciun oras in RO", exception.getMessage());
    }

    @Test
    public void getWeather() throws IOException, HandleException {
        array.add("123\t123\t123\t123\t123");
        mockRn.set_mLines(array);
        instance=new Weather(mockRn);
        Throwable exception = assertThrows(HandleException.class, () ->instance.getWeather("invalid","test"));
        assertEquals("Nu s-a putut returna niciun id", exception.getMessage());

    }
    @Test
    public void getWeather2() throws IOException, HandleException {
        array.add("6942372\t123\t123\t123\t123");
        mockRn.set_mLines(array);
        instance=new Weather(mockRn);
        String verify="{\"coord\":{\"lon\":25.8041,\"lat\":46.3579}";
        assertTrue(instance.getWeather("123","123").contains(verify));

    }
}