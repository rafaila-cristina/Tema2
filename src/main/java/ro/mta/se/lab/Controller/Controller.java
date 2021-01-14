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


public class Controller implements Initializable {

    public Controller() throws FileNotFoundException {
    }
    private Weather weather=new Weather(new DataFromFile("Cities.txt"));
    private Map<String, String> countriesMap = new HashMap<>();

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

    @FXML
    public void getCitiesList(MouseEvent mouseEvent) throws HandleException {
        ArrayList<String> listOfCities =new ArrayList<String>();
        String country=countriesMap.get((String) countryList.getSelectionModel().getSelectedItem());
        listOfCities=weather.getCityList(country);

        ObservableList<String> countryNames= FXCollections.observableArrayList(listOfCities);
        cityList.setItems(countryNames);

    }


    @FXML
    public void getWeather(MouseEvent mouseEvent) throws HandleException, IOException {

        if (cityList.getSelectionModel().getSelectedItem()!=null && countryList.getSelectionModel().getSelectedItem()!=null) {

            String country=countriesMap.get((String) countryList.getSelectionModel().getSelectedItem());
            String result=weather.getWeather(country,cityList.getSelectionModel().getSelectedItem().toString());
            GetDetails(result,(String)cityList.getSelectionModel().getSelectedItem(),country);
        }

    }
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
