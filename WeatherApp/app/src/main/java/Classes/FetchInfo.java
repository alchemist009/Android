package Classes; /**
 * Created by wra1th on 11/16/2017.
 */

import android.content.Context;
import android.widget.Toast;

import com.example.wra1th.weatherapp.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;


public class FetchInfo {

    private static final String OPEN_WEATHER_MAP_API_CITY = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=%s";
    private static final String OPEN_WEATHER_MAP_API_COORD = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s";

    public static JSONObject getJSON(Context context, String city, String scale){

        try{
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API_CITY, city, scale));
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            connection.addRequestProperty("x-api-key", context.getString(R.string.open_weather_maps_app_id));

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp = "";

            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            if(data.getInt("cod") != 200){
                return null;
            }


            return data;
        }catch(Exception e){
            return null;
        }
    }


    public static JSONObject getJSON(Context context, Double latitude, Double longitude){

        try{
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API_COORD, Double.toString(latitude), Double.toString(longitude)));
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            connection.addRequestProperty("x-api-key", context.getString(R.string.open_weather_maps_app_id));

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp = "";

            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            if(data.getInt("cod") != 200){
                return null;
            }


            return data;
        }catch(Exception e){
            return null;
        }
    }
}
