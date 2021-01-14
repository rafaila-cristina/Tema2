
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import ro.mta.se.lab.Model.DataFromFile;
import ro.mta.se.lab.Exception.HandleException;
import ro.mta.se.lab.Model.Weather;

import static org.junit.Assert.assertArrayEquals;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Aceasta clasa realizeaza teste unitare asupra clasei {@link DataFromFile} cu ajutorul obiectelor de tip mock si a librariei mockito.
 *
 * @author Rafaila Cristina
 * @see DataFromFile
 */
class DataFromFileTest {

    @Before
    void setUp() {
        System.out.println("Before");
    }

    @After
    void tearDown() {
        System.out.println("After");
    }

    /**
     * Acest test verifica daca se arunca o exceptie in momentul in care incercam sa accesam un fisier care nu exista.
     *
     * @throws FileNotFoundException
     */
    @Test
    public void getCountiesListException() throws FileNotFoundException {
        DataFromFile data=new DataFromFile();
        Throwable exception = assertThrows(FileNotFoundException.class, () -> data.set_mFileName("nofile"));
        assertEquals("nofile (The system cannot find the file specified)", exception.getMessage());
    }

    /**
     * Acest test verifica daca lista returnata de metoda <i>GetCountiesList()</i> este cea care ne asteptam sa fie.
     *
     * @throws HandleException
     * @throws FileNotFoundException
     */
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

    /**
     * Acest test verifica daca lista de orase pentru tara Romania este cea care ne asteptam sa fie (testam functia <i>GetCities(String)</i>)
     * @throws HandleException
     * @throws FileNotFoundException
     */
    @Test
    void getCities() throws HandleException, FileNotFoundException {
        ArrayList<String> arrayExpected=new ArrayList<String>();
        arrayExpected.add("Miercurea Ciuc");
        arrayExpected.add("Bucharest");

        DataFromFile data=new DataFromFile("Cities.txt");

        assertTrue(data.GetCities("RO").equals(arrayExpected));
    }

    /**
     * Acest test verifica daca se arunca o exceptie in momentul in care cautam orase dintr-o tara care nu exista in fisier.
     *
     * @throws FileNotFoundException
     */
    @Test
    public void getCitiesException() throws FileNotFoundException {
        DataFromFile data=new DataFromFile("Cities.txt");
        Throwable exception = assertThrows(HandleException.class, () ->data.GetCities("nuexista"));
        assertEquals("Nu exista niciun oras in nuexista", exception.getMessage());
    }

    /**
     * Acest test verifica daca se arunca o exceptie in momentul in care dorim sa accesam un id pentru o pereche [oras-tara] inexistenta
     *
     * @throws HandleException
     * @throws FileNotFoundException
     */
    @Test
    public void GetIDException() throws HandleException, FileNotFoundException {
        DataFromFile data=new DataFromFile("Cities.txt");
        Throwable exception = assertThrows(HandleException.class, () -> data.GetID("wd","e"));
        assertEquals("Nu s-a putut returna niciun id", exception.getMessage());
    }

    /**
     * Acest test verifica daca id-ul returnat de functia <i>GetID(String,String)</i> este cel care ar trebui sa fie.
     *
     * @throws HandleException
     * @throws FileNotFoundException
     */
    @Test
    public void GetID() throws HandleException, FileNotFoundException {
        DataFromFile data=new DataFromFile("Cities.txt");
        assertEquals( data.GetID("RO","Bucharest"),"683506");
    }
}