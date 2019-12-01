package com.example.pogoda;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyIntentService extends IntentService {
    public static final String DAY = "day";
    public static final String CITY = "cityName";
    public static int dday;
    public void showToast(String message) {
        final String msg = message;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private String readStream(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public MyIntentService() {
        super("MyIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        Intent broadcastIntent = new Intent();
        String apiResponse;
        String urlApiKey = "&appid=710b8970ede239f08e3d2800095eb600";
        HttpURLConnection urlConnection = null;
        URL url = null;
        String foreCastType;
        String day = intent.getStringExtra(DAY);
        String cityName = intent.getStringExtra(CITY);
        if (day.equals("0")) {foreCastType = "weather";dday =0;}
        else{ foreCastType = "forecast";dday =1;}
        try {

            url = new URL("https://api.openweathermap.org/data/2.5/" + foreCastType + "?q=" + cityName + "&units=metric" + urlApiKey);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            apiResponse = readStream(in);
            urlConnection.disconnect();
        } catch (IOException e) {
            System.out.println("Error");
            String error = getResources().getString(R.string.error_connect);
            showToast(error);
            e.printStackTrace();
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(apiResponse);
            if (jsonObject.optInt("cod") != 200) {
                String error = getResources().getString(R.string.error_url);
                showToast(error);
                return;
            }
            int cnt = jsonObject.optInt("cnt", 1);

            Date dt = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(dt);
            c.add(Calendar.DATE, 1);
            dt = c.getTime();
            String nextDay = new SimpleDateFormat("dd", Locale.getDefault()).format(dt);
            String tmpDate = nextDay;

            JSONArray jsonArray = jsonObject.optJSONArray("list");
            JSONObject tmpJsonObject;

            Double avgTemp = 0.0;
            Double minTemp = 999.99;
            Double maxTemp = -999.99;
            Double avgWindSpeed = 0.0;
            Double avgHumidity = 0.0;

            Double tmpMin;
            Double tmpMax;
            int counter = 0;
            for (int i = 0; i < cnt; i++) {
                if (cnt != 1) {
                    tmpJsonObject = jsonArray.getJSONObject(i);
                    tmpDate = tmpJsonObject.optString("dt_txt").substring(8, 10);
                } else {
                    tmpJsonObject = jsonObject;
                }

                if (nextDay.equals(tmpDate)) {
                    avgTemp += tmpJsonObject.getJSONObject("main").optDouble("temp");
                    avgHumidity += tmpJsonObject.getJSONObject("main").optDouble("humidity");
                    avgWindSpeed += tmpJsonObject.getJSONObject("wind").optDouble("speed");
                    tmpMax = tmpJsonObject.getJSONObject("main").optDouble("temp_max");
                    tmpMin = tmpJsonObject.getJSONObject("main").optDouble("temp_min");

                    if (maxTemp < tmpMax) {
                        maxTemp = tmpMax;
                    }

                    if (minTemp > tmpMin) {
                        minTemp = tmpMin;
                    }
                    counter++;
                }

            }
            avgHumidity = avgHumidity / counter;
            avgTemp = avgTemp / counter;
            avgWindSpeed = avgWindSpeed / counter;

            broadcastIntent.setAction(day);
            DecimalFormat df2 = new DecimalFormat("#.##");
            broadcastIntent.putExtra("avgTemp", df2.format(avgTemp).toString());
            broadcastIntent.putExtra("avgHumidity", df2.format(avgHumidity).toString());
            broadcastIntent.putExtra("avgWindSpeed", df2.format(avgWindSpeed).toString());
            broadcastIntent.putExtra("minTemp", df2.format(minTemp).toString());
            broadcastIntent.putExtra("maxTemp", df2.format(maxTemp).toString());
            sendBroadcast(broadcastIntent);
        } catch (JSONException e) {
            String error = getResources().getString(R.string.error_processing);
            showToast(error);
            e.printStackTrace();
            return;
        }
    }

}
