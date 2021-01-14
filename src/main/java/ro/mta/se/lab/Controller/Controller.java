package ro.mta.se.lab.Controller;

import java.io.*;
import java.net.URL;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import ro.mta.se.lab.Exception.HandleException;
import ro.mta.se.lab.Model.DataFromFile;
import ro.mta.se.lab.Model.Weather;
import javafx.scene.image.Image;

/**
 * Reprezinta partea de Controller din structura MVC.
 * Cu ajutorul acestei clase se populeaza structurile de la nivelul interfetei grafice si se asigura interactiunea dintre utilizator
 * si date (prin evenimente declansate de user).
 *
 * @author Rafaila Cristina
 * @see Weather
 * @see HandleException
 */
public class Controller implements Initializable {

    public Controller() throws FileNotFoundException {
    }
    private Weather weather=new Weather(new DataFromFile("Cities.txt")); //se initializeaza citirea din fisier
    private Map<String, String> countriesMap = new HashMap<>(); //lista pentru a face translatia cod-nume_tara si invers

    /**
     * Se preiau obiectele de tip FXML pentru a le putea gestiona in aceasta clasa
     */
    @FXML
    private Text cloudText;

    @FXML
    private Text windText;

    @FXML
    private Text humidityText;

    @FXML
    private Text temperatureText;

    @FXML
    private Text dateText;

    @FXML
    private Text locationText;

    @FXML
    private  javafx.scene.control.ListView  countryList;

    @FXML
    private  javafx.scene.control.ListView  cityList;

    @FXML
    private ImageView imageView;

    /**
     * In aceasta functie de afiseaza lista tarilor in obiectul {@link #countryList} in momentul pornirii aplicatiei.
     * Se pastreaza la nivel local o lista generata prin intermediul metodei <i>getCountryList()</i> din obiectul {@link #weather}.
     * Aceasta lista reprezinta o lista cu codurile tarilor aflate in fisier. Pentru a face translatia cod_tara - nume_tara se
     * apeleaza functia {@link #MakeCountryList(ArrayList)}.
     * Listei returnate de functia {@link #MakeCountryList(ArrayList)} i se face un cast la tipul ObservableList<String> pentru
     * a putea popula lista {@link #countryList}.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        
        ArrayList<String> listOfCountries= null;

        try {
            listOfCountries = weather.getCountryList();
        } catch (HandleException e) {
            System.out.println(e.getMessage());;
        }

        ArrayList<String> showListOfCountries=MakeCountryList(listOfCountries);
        ObservableList<String> countryNames= FXCollections.observableArrayList(showListOfCountries);
        countryList.setItems(countryNames);


    }

    /**
     * Aceasta functie realizeaza translatarea cod tara -> nume tara si pastreaza un obiect de tip Map in {@link #countriesMap}
     * cu aceste valori pentru a putea face ulterior si translatia nume tara -> cod tara.
     *
     * @param _countries lisat cu codurile tarilor
     * @return lista cu numele tarilor
     */
    private ArrayList<String> MakeCountryList(ArrayList<String> _countries)
    {
        ArrayList<String> showListOfCountries=new ArrayList<String>();
        for (String country:_countries)
        {
            Locale l = new Locale("", country);
            showListOfCountries.add(l.getDisplayCountry());
            countriesMap.put(l.getDisplayCountry(), country);
        }
        return showListOfCountries;
    }

    /**
     * Aceasta functie reprezinta modul in care aplicatia se va comporta in momentul in care utilizatorul selecteaza un nume de tara
     * din lista {@link #countryList}.
     * Numele va fi translata in cod prin intermediul obiectului de tip Map {@link #countriesMap} si transmis ca parametru functiei
     * <i>getCityList(String)</i> din clasa {@link #weather} pentru a returna o lista cu orasle aflate in tara respectiva.
     *
     * @param mouseEvent evenimentul selectarii unei tari din {@link #countryList}.
     * @throws HandleException pentru exceptiile care pot fi aruncate in metoda <i>getCityList(String)</i> din clasa {@link #weather}
     */
    @FXML
    public void getCitiesList(MouseEvent mouseEvent) throws HandleException {
        ArrayList<String> listOfCities =new ArrayList<String>();
        String country=countriesMap.get((String) countryList.getSelectionModel().getSelectedItem());
        listOfCities=weather.getCityList(country);

        ObservableList<String> countryNames= FXCollections.observableArrayList(listOfCities);
        cityList.setItems(countryNames);

    }

    /**
     * Aceasta functie reprezinta modul in care aplicatia se va comporta in momentul in care utilizatorul selecteaza un oras din
     * lista de orase {@link #cityList}.
     * Dupa selectie se vor afisa detaliile legate de vreme pentru locatia aleasa. Detaliile sunt obtinute prin
     * intermediul metodei <i>getWeather(String,String)</i> din clasa {@link #weather} si afisate prin apelarea functiei
     * {@link #GetDetails(String, String, String)}.
     *
     * @param mouseEvent evenimentul selectarii unei tari din {@link #countryList}.
     * @throws HandleException pentru exceptiile care pot fi aruncate in metoda <i>getWeather(String,String)</i> din clasa {@link #weather}
     * @throws IOException pentru exceptiile care pot fi aruncate in functia {@link #GetDetails(String, String, String)}.
     */
    @FXML
    public void getWeather(MouseEvent mouseEvent) throws HandleException, IOException {

        if (cityList.getSelectionModel().getSelectedItem()!=null && countryList.getSelectionModel().getSelectedItem()!=null) {

            String country=countriesMap.get((String) countryList.getSelectionModel().getSelectedItem());
            String result=weather.getWeather(country,cityList.getSelectionModel().getSelectedItem().toString());
            GetDetails(result,(String)cityList.getSelectionModel().getSelectedItem(),country);
        }

    }

    /**
     * Aceasta functie afiseaza detaliile legate de vreme. Aceste detalii sunt preluate din jsonul dat ca parametru prin intermediul
     * metodei  <i>GetDetails(String,String,String)</i> din clasa {@link #weather},
     *
     * @param jsonString jsonul care contine datele legate de vreme
     * @param _location numele orasului
     * @param _country codul tarii
     * @throws IOException pentru exceptiile care pot fi aruncate in metoda <i>GetDetails(String,String,String)</i> din clasa {@link #weather}
     */
    private void GetDetails(String jsonString,String _location,String _country) throws IOException {

        ArrayList<String> values=weather.GetDetails(jsonString,_location,_country);

        Image img = new Image(values.get(0),true);
        imageView.setVisible(true);
        imageView.setImage(img);

        locationText.setText(values.get(1));
        dateText.setText(values.get(2));
        temperatureText.setText(values.get(3)+" C");
        humidityText.setText("Humidity: "+values.get(4)+" %");
        windText.setText("Wind: "+values.get(5)+" m/s");
        cloudText.setText("Cloud: "+values.get(6)+" %");

    }

}
