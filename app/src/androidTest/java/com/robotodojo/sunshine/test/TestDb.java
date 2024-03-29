package com.robotodojo.sunshine.test;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.robotodojo.sunshine.data.WeatherDbHelper;

import java.util.Map;
import java.util.Set;

import static com.robotodojo.sunshine.data.WeatherContract.LocationEntry;
import static com.robotodojo.sunshine.data.WeatherContract.WeatherEntry;

public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();
    static final String TEST_LOCATION = "99705";
    static final String TEST_DATE = "20141205";

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new WeatherDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }

    public void testInsertReadDb() {
        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Location
        long rowId = 0;
        try {
            rowId = insertReadDb(db, createNorthPoleLocationValues(), LocationEntry.TABLE_NAME);
        } catch (Throwable throwable) {
            fail("Exception: " + throwable.getMessage());
        }

        // Weather
        try {
            insertReadDb(db, createWeatherValues(rowId), WeatherEntry.TABLE_NAME);
        } catch (Throwable throwable) {
            fail("Exception: " + throwable.getMessage());
        }

        dbHelper.close();
    }

    private long insertReadDb(SQLiteDatabase db, ContentValues values, String table)
            throws Throwable {
        long rowId = db.insert(table, null, values);
        assertTrue(rowId != -1);
        Log.d(LOG_TAG, "New row id: " + rowId);
        Cursor cursor = db.query( table, null, null, null, null, null, null );
        validateCursor(cursor, values);
        return rowId;
    }


    static ContentValues createWeatherValues(long locationRowId) {
        ContentValues weatherValues = new ContentValues();
        weatherValues.put(WeatherEntry.COLUMN_LOC_KEY, locationRowId);
        weatherValues.put(WeatherEntry.COLUMN_DATETEXT, "20141205");
        weatherValues.put(WeatherEntry.COLUMN_DEGREES, 1.1);
        weatherValues.put(WeatherEntry.COLUMN_HUMIDITY, 1.2);
        weatherValues.put(WeatherEntry.COLUMN_PRESSURE, 1.3);
        weatherValues.put(WeatherEntry.COLUMN_MAX_TEMP, 75);
        weatherValues.put(WeatherEntry.COLUMN_MIN_TEMP, 65);
        weatherValues.put(WeatherEntry.COLUMN_SHORT_DESC, "Asteroids");
        weatherValues.put(WeatherEntry.COLUMN_WIND_SPEED, 5.5);
        weatherValues.put(WeatherEntry.COLUMN_WEATHER_ID, 321);

        return weatherValues;
    }

    static ContentValues createNorthPoleLocationValues() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(LocationEntry.COLUMN_LOCATION_SETTING, "99705");
        testValues.put(LocationEntry.COLUMN_CITY_NAME, "North Pole");
        testValues.put(LocationEntry.COLUMN_COORD_LAT, 64.7488);
        testValues.put(LocationEntry.COLUMN_COORD_LONG, -147.353);

        return testValues;
    }

    static void validateCursor(Cursor valueCursor, ContentValues expectedValues) {

        assertTrue(valueCursor.moveToFirst());

        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse(idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals(expectedValue, valueCursor.getString(idx));
        }
        valueCursor.close();
    }
}