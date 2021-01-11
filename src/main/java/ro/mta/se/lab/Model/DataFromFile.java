package ro.mta.se.lab.Model;
import ro.mta.se.lab.Exception.HandleException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DataFromFile {
    private static String _mFileName;
    private static ArrayList<String> _mLines;

    public DataFromFile(String _mFileName) throws FileNotFoundException {

        DataFromFile._mFileName = _mFileName;
        ReadFile();
    }
    public DataFromFile() {

    }

    public static void set_mLines(ArrayList<String> _mLines) {
        DataFromFile._mLines = _mLines;
    }

    public static void set_mFileName(String _mFileName) throws FileNotFoundException {

        DataFromFile._mFileName = _mFileName;
        ReadFile();
    }

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
    public static ArrayList<String> GetCountriesList() throws HandleException {

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
