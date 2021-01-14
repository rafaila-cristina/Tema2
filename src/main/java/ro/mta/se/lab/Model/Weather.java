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
/**
 * Modeleaza un obiect de tip Weather pentru a putea fi folosit intr-o alta clasa(ex {@Link Controller})
 * Aceasta clasa reprezinta modelul din structura MVC a proiectului si confera datele care vor fi afisate in interfata grafica
 *
 * @author Rafaila Cristina
 * @see DataFromFile
 * @see HandleException
 * @see ro.mta.se.lab.Controller.Controller
 */
public class Weather {

    private DataFromFile data;

    /**
     * Contructor care asigneaza membrului {@link #data} obictul <i>_data</i> dat ca parametru.
     *
     * @param _data obiect de tip DataFromFile
     * @throws FileNotFoundException pentru exceptiile aruncate in constructorul obiectului DataFromFile
     */
    public Weather(DataFromFile _data) throws FileNotFoundException {
        data=_data;
    }

    /**
     * Aceasta functie apeleaza metoda <i>getCountryList</i> din cadrul obiectului {@link #data} si returneza o lista cu codurile
     * tarilor dintr-un fisier.
     *
     * @return lista tarilor
     * @throws HandleException pentru exceptiile aruncate in metoda <i>getCountryList</i>
     */
    public ArrayList<String> getCountryList() throws HandleException {
        return data.GetCountriesList();
    }

    /**
     * Aceasta functie apeleaza metoda <i>getCityList(String)</i> din cadrul obiectului {@link #data} si returneza o lista cu numele
     * oraselor dintr-un fisier, in functie de codul tarii dat ca parametru.
     *
     * @param _country codul tarii
     * @return lista oraselor
     * @throws HandleException pentru exceptiile aruncate in metoda <i>getCityList(String)</i>
     */
    public ArrayList<String> getCityList(String _country) throws HandleException{
        return data.GetCities(_country);
    }

    /**
     * Aceasta functie apeleaza metoda <i>GetID(String,String)</i> din cadrul obiectului {@link #data} si returneza id-ul orasului
     * corespunzator perechii [tara-oras] date ca parametru.
     *
     * @param _country codul tarii
     * @param _city numele orasului
     * @return id-ul orasului
     * @throws HandleException pentru exceptiile aruncate in metoda <i>GetID(String,String)</i>
     */
    private String getID(String _country,String _city) throws HandleException {
        return data.GetID(_country,_city);
    }

    /**
     * Aceasta functie face un request serverului web <b>openweathermap</b> pentru a returna un obiect json care contine detalii privind
     * vremea la momentul interogarii in functie de un id al locatiei.
     * ID-ul este returnat prin intermediul functiei {@link #getID(String, String)}.
     * Rezultatul interogarii este scris intr-o variabila locala de tip String si returnat la finalul functiei.
     *
     * @param _country codul tarii (utilizat in functia {@link #getID(String, String)})
     * @param _city numele orasului (utilizat in functia {@link #getID(String, String)})
     * @return jsonul care contine detaliile despre vreme
     * @throws HandleException pentru exceptiile care pot fi aaruncate in functia {@link #getID(String, String)})
     * @throws IOException pentru exceptiile care pot fi aruncate in momentul conectarii la serverul web
     */
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

    /**
     * In aceasta functie se parseaza jsonul (cu ajutorul functiei {@link #jsonToMap(String)}) primit ca parametru si
     * se returneaza o lista de detalii care vor fi afisate utilizatorului in interfata grafica prin intermediul clasei
     * {@link ro.mta.se.lab.Controller.Controller}.
     *
     * Aceasta lista de detalii este o lista de tip <i>ArrayList<String></i> construita la nivel local si contine:
     * -index 0 => url-ul deunde se va lua imaginea care reflecta starea vremii
     * -index 1 => orasul ales de utilizator
     * -index 2 => data si ora curenta sub forma [zi ora:minute:secunde] (format String)
     * -index 3 => temperatura in grade Celsius (format String)
     * -index 4 => umiditatea (format String)
     * -index 5 => viteza vantului (format String)
     * -index 6 => procentul de innorare (format String)
     *
     * La final se apeleaza functia {@link #LogFile(String, String, String)}
     *
     * @param jsonString jsonul in care se afla raspunsul de la serverul web
     * @param _location numele orasului
     * @param _country codul tarii
     * @return lista de detalii
     * @throws IOException pentru exceptiile care pot aparea in urma scrierii in fisier in functia {@link #LogFile(String, String, String)}
     */
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

        //convertirea gradelor Kelvin in grade Celsius
        String temperature= mainMap.get("temp").toString();
        long tempInC=Math.round(Double.parseDouble(temperature)-273.15);

        //obtinerea datei si orei curente
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

    /**
     * Prin intermediul acestei functii se tine un istoric al cautarilor utilizatorilor prin fisierul log.txt.
     * O linie din acest fisier are formatul "data ora: In [nume_oras (cod_tara)] au fost [temperatur] grade Celsius".
     *
     * @param _city nume oras
     * @param _country cod tara
     * @param _temperature temperatura la momentul cautarii
     * @throws IOException pentru exceptiile care pot aparea in urma scrierii in fisier
     */
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

    /**
     * Functie care converteste jsonul intr-o structura de tip Map pentru a face mai usoara parsarea.
     *
     * @param str jsonul
     * @return obiectul de tip Map format din json
     */
    private static Map<String,Object> jsonToMap(String str){
        Map<String,Object> map = new Gson().fromJson(str,new TypeToken<HashMap<String,Object>>() {}.getType());
        return map;
    }
}
