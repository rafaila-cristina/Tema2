package ro.mta.se.lab.Model;
import ro.mta.se.lab.Exception.HandleException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
/**
 * Modeleaza un obiect de tip DataFromFile pentru a putea fi folosit intr-o alta clasa(ex {@Link Weather})
 * Aceasta clasa asigura citirea din fisierul cu date despre fiecare oras si returneaza date din acesta pentru a putea fi filosite ulterior
 * la nivelul clasei {@Link Weather}.
 *
 * @author Rafaila Cristina
 * @see Weather
 * @see HandleException
 */
public class DataFromFile {
    private static String _mFileName; //numele fisierului
    private static ArrayList<String> _mLines; //continului fisierului

    /**
     * Constructor explicit care seteaza valoarea membrului {@link #_mFileName} cu valoarea stringului dat ca parametru.
     * De asemenea se initializeaza citirea fisierului prin apelul funtiei {@link #ReadFile()}
     *
     * @param _mFileName string care contine numele fisierului
     * @throws FileNotFoundException pentru exceptiile care pot aparea in cadrul functiei {@link #ReadFile()}
     */
    public DataFromFile(String _mFileName) throws FileNotFoundException {

        DataFromFile._mFileName = _mFileName;
        ReadFile();
    }

    /**
     * Constructor implicit
     */
    public DataFromFile() {
    }

    /**
     * Setter pentru membrul {@link #_mLines} folosit pentru testarea clasei.
     *
     * @param _mLines lista de stringuri pentru a asigna membrul
     */
    public static void set_mLines(ArrayList<String> _mLines) {
        DataFromFile._mLines = _mLines;
    }

    /**
     * Setter pentru membrul {@link #_mFileName}.
     * De asemenea se initializeaza citirea fisierului prin apelul funtiei {@link #ReadFile()}
     *
     * @param _mFileName lista de stringuri pentru a asigna membrul
     * @throws FileNotFoundException pentru exceptiile care pot aparea in cadrul functiei {@link #ReadFile()}
     */
    public static void set_mFileName(String _mFileName) throws FileNotFoundException {

        DataFromFile._mFileName = _mFileName;
        ReadFile();
    }

    /**
     * Aceasta functie realizeaza citirea fisierului a carui nume se afla in membrul {@link #_mFileName}. Continutul fisierului este salvat
     * in membrul {@link #_mLines}.
     *
     * @throws FileNotFoundException
     */
    private static void ReadFile() throws FileNotFoundException{

        _mLines=new ArrayList<String>();
        File myObj = new File(DataFromFile._mFileName);
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            DataFromFile._mLines.add(data);
        }
        myReader.close();

    }

    /**
     * Aceasta functie citeste din membrul {@link #_mLines} toate codurile tarilor (aflate la indexul 4 al fiecarei linii din fisier)
     * si formeaza local o lista cu aceste coduri (lista avand inregistrari unice, fara copii ale codurilor). Ulterior lista este sortata
     * alfabetic si returnata.
     *
     * @return lista cu codurile tarilor
     * @throws HandleException in cazul in care nu s-a efectuat corect citirea din fisier sau in cazul in care fisierul este gol si pentru
     * situatiile in care fisierul nu are formatul corespunzator (5 coloane despartite prin tab).
     */
    public static ArrayList<String> GetCountriesList() throws HandleException {

        if(_mLines==null) throw new HandleException("Nu s-a efectuat citirea din fisier!");
        ArrayList<String> countriesList=new ArrayList<String>();
        for (String line:_mLines) {
            String[] words=line.split("\t");
            if (words.length !=5)  throw new HandleException("Fisierul nu are formatul corespunzator!");
            if (!countriesList.contains(words[4])){
                countriesList.add(words[4]);
            }
        }

        Collections.sort(countriesList);
        return countriesList;
    }

    /**
     * Aceasta functie primeste ca parametru codul unei tari si formeaza local o lista cu toate orasele care se afla in tara respectiva.
     * Ulterior lista este returnata la finalul functiei.
     *
     * @param _country codul tarii
     * @return list oraselor din tara respectiva
     * @throws HandleException in cazul in care nu s-a efectuat corect citirea din fisier sau in cazul in care fisierul este gol si in cazul
     * in care nu s-a gasit niciun oras pentru tara data ca parametru.
     */
    public static ArrayList<String> GetCities(String _country) throws HandleException {
        if(_mLines==null) throw new HandleException("Nu s-a efectuat citirea din fisier!");
        ArrayList<String> citiesList=new ArrayList<String>();
        for (String line:_mLines) {
            String[] words=line.split("\t");
            if (words[4].equals(_country)){
                citiesList.add(words[1]);
            }
        }
        if (citiesList.isEmpty()) throw new HandleException("Nu exista niciun oras in "+_country);

        return citiesList;
    }

    /**
     * Aceasta functie returneaza id-ul orasului (aflat pe prima coloana la nivelul fisierului) pentru o pereche de tip [tara-oras].
     *
     * @param _country codul tarii
     * @param _city numele orasului
     * @return id-ul gasit
     * @throws HandleException in cazul in care nu s-a gasit niciun id
     */
    public static String GetID(String _country,String _city) throws HandleException {

        for (String line:_mLines) {
            String[] words=line.split("\t");
            if (words[4].equals(_country) && words[1].equals(_city)){
                return words[0];
            }
        }
        throw new HandleException("Nu s-a putut returna niciun id");
    }

}
