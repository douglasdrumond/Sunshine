package com.robotodojo.sunshine.service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.robotodojo.sunshine.data.WeatherContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Vector;

public class SunshineService extends IntentService {

    private static final String LOG_TAG = SunshineService.class.getSimpleName();

    public static final String ACTION_UPDATE_WEATHER = "com.robotodojo.sunshine.service.action.UPDATE_WEATHER";

    public static final String LOCATION_QUERY_EXTRA = "com.robotodojo.sunshine.service.extra.LOCATION_QUERY_EXTRA";

    public static void startActionUpdateWeather(Context context, String locationQuery) {
        Intent intent = new Intent(context, SunshineService.class);
        intent.setAction(ACTION_UPDATE_WEATHER);
        intent.putExtra(LOCATION_QUERY_EXTRA, locationQuery);
        context.startService(intent);
    }


    public SunshineService() {
        super(SunshineService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();

            if (ACTION_UPDATE_WEATHER.equals(action)) {
                final String locationQuery = intent.getStringExtra(LOCATION_QUERY_EXTRA);
//                fetchWeather(locationQuery);
            }
        }

    }



    public static class AlarmReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent sendIntent = new Intent(context, SunshineService.class);
            sendIntent.putExtra(SunshineService.LOCATION_QUERY_EXTRA, intent.getStringExtra(SunshineService.LOCATION_QUERY_EXTRA));
            context.startService(sendIntent);
        }
    }
}
