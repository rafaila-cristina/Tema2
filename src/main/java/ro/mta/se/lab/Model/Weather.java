package ro.mta.se.lab.Model;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.scene.image.Image;
import ro.mta.se.lab.Exception.HandleException;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;

public class Weather {

    private DataFromFile data;

    public Weather(DataFromFile _data) throws FileNotFoundException {
        data=_data;
    }

    public ArrayList<String> getCountryList() throws HandleException {
        return data.GetCountriesList();
    }

    public ArrayList<String> getCityList(String _country) throws HandleException{
        return data.GetCities(_country);
    }

    private String getID(String _country,String _city) throws HandleException {
        return data.GetID(_country,_city);
    }

    public String getWeather(String _country, String _city) throws HandleException, IOException {
        String ID=getID(_country,_city);
        String API_KEY = "c8fb7378922bd83d6e119b3fc50e40d6";
        String urlString = "http://api.openweathermap.org/data/2.5/weather?id="+ID+"&appid=" + API_KEY ;

        String result = new String();
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result=result+line;
        }
        rd.close();
        return result;
    }

    public ArrayList<String> GetDetails(String jsonString,String _location,String _country) throws IOException {

        ArrayList<String> ret=new ArrayList<String>();

        Map<String, Object > respMap = jsonToMap (jsonString.toString());
        Map<String, Object > mainMap = jsonToMap (respMap.get("main").toString());
        Map<String, Object > windMap = jsonToMap (respMap.get("wind").toString());
        Map<String, Object > cloudsMap = jsonToMap (respMap.get("clouds").toString());

        int start=jsonString.indexOf("icon");
        int end=start+20;
        String aux=jsonString.substring(start,end);
        String []spl=aux.split("\"");

        String url="http://openweathermap.org/img/wn/"+spl[2]+"@2x.png";
        ret.add(url);

        String temperature= mainMap.get("temp").toString();
        long tempInC=Math.round(Double.parseDouble(temperature)-273.15);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String[] splited=formatter.format(calendar.getTime()).split(" ");

        ret.add(_location);
        ret.add(new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime())+"  "+splited[1]);
        ret.add(String.valueOf(tempInC));
        ret.add(mainMap.get("humidity").toString());
        ret.add(windMap.get("speed").toString());
        ret.add(cloudsMap.get("all").toString());

        LogFile(_location,_country,String.valueOf(tempInC));

        return ret;

    }

    private void LogFile(String _city,String _country, String _temperature) throws IOException {
        File yourFile = new File("log.txt");
        yourFile.createNewFile();
        FileWriter oFile = new FileWriter(yourFile, true);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        oFile.write(formatter.format(calendar.getTime()));
        oFile.write(": In "+_city+" ("+_country+") au fost "+_temperature+" grade Celsius\n");
        oFile.close();
    }


    private static Map<String,Object> jsonToMap(String str){
        Map<String,Object> map = new Gson().fromJson(str,new TypeToken<HashMap<String,Object>>() {}.getType());
        return map;
    }
}
