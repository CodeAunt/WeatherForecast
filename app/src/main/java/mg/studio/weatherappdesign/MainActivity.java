package mg.studio.weatherappdesign;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    String[] weekdays = {"MON", "TUR", "WED", "THU", "FRI", "SAT", "SUN"};
    String weatherText;
    String weatherText1;
    String weatherText2;
    String weatherText3;
    String weatherText4;
    //return the weather image
    public int getWeatherSmallImg(String weather){
        switch (weather){
            case "Clear":
                return R.drawable.sunny_small;
            case "Clouds":
                return R.drawable.partly_sunny_small;
            case "Rain":
                return R.drawable.rainy_small;
            case "Wind":
                return R.drawable.windy_small;
        }
        return R.drawable.sunny_small;
    }

    public int getWeatherUpImg(String weather){
        switch (weather){
            case "Clear":
                return R.drawable.sunny_up;
            case "Clouds":
                return R.drawable.partly_sunny_up;
            case "Rain":
                return R.drawable.rainy_up;
            case "Wind":
                return R.drawable.windy_up;
        }
        return R.drawable.sunny_up;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the date and weekday.
        Date current = new Date();
        SimpleDateFormat format = new SimpleDateFormat("EEEE");
        String todayWeekStr = format.format(current);
        ((TextView) findViewById(R.id.todayText)).setText(todayWeekStr);

        format = new SimpleDateFormat("MM-dd-yyyy");
        String dateStr = format.format(current);
        ((TextView) findViewById(R.id.tv_date)).setText(dateStr);


        Calendar cal = Calendar.getInstance();
        cal.setTime(current);
        int wIndex = cal.get(Calendar.DAY_OF_WEEK)-1;

        ((TextView) findViewById(R.id.nextText1)).setText(weekdays[wIndex++]);
        ((TextView) findViewById(R.id.nextText2)).setText(weekdays[wIndex++]);
        ((TextView) findViewById(R.id.nextText3)).setText(weekdays[wIndex++]);
        ((TextView) findViewById(R.id.nextText4)).setText(weekdays[wIndex]);

        new DownloadUpdate().execute();
    }

    public void btnClick(View view) {
        new DownloadUpdate().execute();
    }


    private class DownloadUpdate extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            //String stringUrl = "http://api.openweathermap.org/data/2.5/weather?APPID=1d7518337b40bcc5557731e9ddaf67bb&q=Chongqing&units=metrics";//"https://mpianatra.com/Courses/info.txt";
            // Get the weather
            String stringUrl = "http://api.openweathermap.org/data/2.5/forecast?q=chongqing&units=metric&appid=1d7518337b40bcc5557731e9ddaf67bb";
            HttpURLConnection urlConnection = null;
            BufferedReader reader;

            try {
                URL url = new URL(stringUrl);

                // Create the request to get the information from the server, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return "err";
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Mainly needed for debugging
                    Log.d("debugweather", line);
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return "err";
                }

                String jsonString = buffer.toString();
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonObjectlist = jsonObject.getJSONArray("list");

                JSONObject jsonObjectDay = jsonObjectlist.getJSONObject(0);
                JSONObject jsonObjectMain = jsonObjectDay.getJSONObject("main");
                String temperature = jsonObjectMain.getString("temp");
                //The temperature
                ((TextView) findViewById(R.id.temperature_of_the_day)).setText(temperature);


                // forecast weather and change the image for the next four days...
                JSONArray weatherList;

                jsonObjectDay = jsonObjectlist.getJSONObject(0);
                weatherList = jsonObjectDay.getJSONArray("weather");
                jsonObjectMain = weatherList.getJSONObject(0);
                weatherText = jsonObjectMain.getString("main");
                Log.i("debugweather", jsonObjectDay.toString());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    ((ImageView) findViewById(R.id.todayImg)).setImageDrawable(getResources().getDrawable(getWeatherUpImg(weatherText)));//setImageResource(getWeatherSmallImg(weatherText));
                    }
                });

                jsonObjectDay = jsonObjectlist.getJSONObject(8);
                weatherList = jsonObjectDay.getJSONArray("weather");
                jsonObjectMain = weatherList.getJSONObject(0);
                weatherText1 = jsonObjectMain.getString("main");
                Log.i("debugweather", jsonObjectDay.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    ((ImageView) findViewById(R.id.nextImg1)).setImageResource(getWeatherSmallImg(weatherText1));
                    }
                });


                jsonObjectDay = jsonObjectlist.getJSONObject(16);
                weatherList = jsonObjectDay.getJSONArray("weather");
                jsonObjectMain = weatherList.getJSONObject(0);
                weatherText2 = jsonObjectMain.getString("main");
                Log.i("debugweather", jsonObjectDay.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((ImageView) findViewById(R.id.nextImg2)).setImageResource(getWeatherSmallImg(weatherText2));
                    }
                });

                jsonObjectDay = jsonObjectlist.getJSONObject(24);
                weatherList = jsonObjectDay.getJSONArray("weather");
                jsonObjectMain = weatherList.getJSONObject(0);
                weatherText3 = jsonObjectMain.getString("main");
                Log.i("debugweather", jsonObjectDay.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((ImageView) findViewById(R.id.nextImg3)).setImageResource(getWeatherSmallImg(weatherText3));
                    }
                });

                jsonObjectDay = jsonObjectlist.getJSONObject(32);
                weatherList = jsonObjectDay.getJSONArray("weather");
                jsonObjectMain = weatherList.getJSONObject(0);
                weatherText4 = jsonObjectMain.getString("main");
                Log.i("debugweather", jsonObjectDay.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((ImageView) findViewById(R.id.nextImg4)).setImageResource(getWeatherSmallImg(weatherText4));
                    }
                });
                return temperature;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }catch (NullPointerException e) {//Use this code to avoid Internet Connection crash.
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "error";
        }



        // This method is called after doInBackground
        @Override
        protected void onPostExecute(String temperature) {
            //Update the temperature displayed

            if (temperature == "error"){
                Toast.makeText(MainActivity.this, "Update failed. Check your network please.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Information updated.", Toast.LENGTH_SHORT).show();
                ((TextView) findViewById(R.id.temperature_of_the_day)).setText(temperature);
            }

        }
    }
}
