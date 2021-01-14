package ro.mta.se.lab.Model;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import ro.mta.se.lab.Exception.HandleException;


import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DataFromFileTest {

    @Before
    void setUp() {
        System.out.println("Before");
    }

    @After
    void tearDown() {
        System.out.println("After");
    }

    @Test
    public void getCountiesListException() throws FileNotFoundException {
        DataFromFile data=new DataFromFile();
        Throwable exception = assertThrows(FileNotFoundException.class, () -> data.set_mFileName("nofile"));
        assertEquals("nofile (The system cannot find the file specified)", exception.getMessage());
    }

    @Test
    void getCountiesList() throws HandleException, FileNotFoundException {
        ArrayList<String> arrayExpected=new ArrayList<String>();
        DataFromFile data=new DataFromFile("Cities.txt");
        ArrayList<String> arrayReturned=DataFromFile.GetCountriesList();
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

        assertTrue(arrayReturned.equals(arrayExpected));
    }


    @Test
    void getCities() throws HandleException, FileNotFoundException {
        ArrayList<String> arrayExpected=new ArrayList<String>();
        arrayExpected.add("Miercurea Ciuc");
        arrayExpected.add("Bucharest");

        DataFromFile data=new DataFromFile("Cities.txt");

        assertTrue(data.GetCities("RO").equals(arrayExpected));
    }

    @Test
    public void getCitiesException() throws FileNotFoundException {
        DataFromFile data=new DataFromFile("Cities.txt");
        Throwable exception = assertThrows(HandleException.class, () ->data.GetCities("nuexista"));
        assertEquals("Nu exista niciun oras in nuexista", exception.getMessage());
    }

    @Test
    public void GetIDException() throws HandleException, FileNotFoundException {
        DataFromFile data=new DataFromFile("Cities.txt");
        Throwable exception = assertThrows(HandleException.class, () -> data.GetID("wd","e"));
        assertEquals("Nu s-a putut returna niciun id", exception.getMessage());
    }

    @Test
    public void GetID() throws HandleException, FileNotFoundException {
        DataFromFile data=new DataFromFile("Cities.txt");
        assertEquals( data.GetID("RO","Bucharest"),"683506");
    }
}