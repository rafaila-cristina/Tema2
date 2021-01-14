
import org.junit.Test;

import ro.mta.se.lab.Exception.HandleException;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import org.testng.annotations.BeforeMethod;
import ro.mta.se.lab.Model.DataFromFile;
import ro.mta.se.lab.Model.Weather;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
/**
 * Aceasta clasa realizeaza teste unitare asupra clasei {@link Weather} cu ajutorul obiectelor de tip mock si a librariei mockito.
 *
 * @author Rafaila Cristina
 * @see Weather
 */
public class WeatherTest{

    Weather instance;
    DataFromFile mockRn;
    ArrayList<String> arrayExpected;
    ArrayList<String> array=new ArrayList<String>();

    /**
     * In aceasta functie se face un setUp pentru executarea testelor ulterioare: se instantiaza un obiect de tip mock si un comportament
     * al acestuia si se construieste o lista pe care noi o asteptam sa o primim ca rezultat in urma unor teste, pentru a putea efectua verificari.
     *
     * @throws Exception
     */
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

    /**
     * Acest test verifica daca lista de orase returnata prin clasa cu obiectul mock este cea asteptata.
     * Se asteapta ca rezultatul sa fie Fals.
     *
     * @throws HandleException
     * @throws FileNotFoundException
     */
    @Test
    public void getCountryList() throws HandleException, FileNotFoundException {

        array.add("123\t123\t123\t123\t123");
        mockRn.set_mLines(array);
        instance = new Weather(mockRn);
        assertFalse(instance.getCountryList().equals(arrayExpected));

    }

    /**
     * Acest test verifica daca se arunca o exceptie in momentul in care cautam orase dintr-o tara care nu exista in lista noastra.
     *
     * @throws FileNotFoundException
     * @throws HandleException
     */
    @Test
    public void getCityList() throws FileNotFoundException, HandleException {
        array.add("123\t123\t123\t123\t123");
        mockRn.set_mLines(array);
        instance=new Weather(mockRn);
        Throwable exception = assertThrows(HandleException.class, () ->instance.getCityList("RO"));
        assertEquals("Nu exista niciun oras in RO", exception.getMessage());
    }

    /**
     * Acest test verifica daca se arunca o exceptie in momentul in care cautam vremea pentru o pereche [oras-tara] invalida.
     *
     * @throws IOException
     * @throws HandleException
     */
    @Test
    public void getWeather() throws IOException, HandleException {
        array.add("123\t123\t123\t123\t123");
        mockRn.set_mLines(array);
        instance=new Weather(mockRn);
        Throwable exception = assertThrows(HandleException.class, () ->instance.getWeather("invalid","test"));
        assertEquals("Nu s-a putut returna niciun id", exception.getMessage());

    }

    /**
     * Acest test verifica daca am primit corect vremea pentru un id cautat.
     *
     * @throws IOException
     * @throws HandleException
     */
    @Test
    public void getWeather2() throws IOException, HandleException {
        array.add("6942372\t123\t123\t123\t123");
        mockRn.set_mLines(array);
        instance=new Weather(mockRn);
        String verify="{\"coord\":{\"lon\":25.8041,\"lat\":46.3579}";
        assertTrue(instance.getWeather("123","123").contains(verify));

    }
}