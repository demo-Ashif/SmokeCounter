package de.schipplock.smokecounter.app.database;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * wrapper for the preference key value store
 */
public class Database {

    SharedPreferences pref;
    String fileName = "";
    Activity context;

    /**
     *
     * @param databaseName
     * @param activity
     */
    public Database(String databaseName, Activity activity) {
        this.fileName = activity.getClass().getCanonicalName() + "." + databaseName;
        this.pref = activity.getSharedPreferences(this.fileName, Context.MODE_PRIVATE);
        this.context = activity;
    }

    /**
     * simply returns the pref object
     * @return
     */
    private SharedPreferences getPreferences() {
        return this.pref;
    }

    /**
     *
     * @return
     */
    private String getFileName() {
        return this.fileName;
    }

    /**
     * adds a string value
     * @param key
     * @param value
     * @return
     */
    public Boolean add(String key, String value) {
        SharedPreferences pref = this.getPreferences();
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    /**
     * adds an int value
     * @param key
     * @param value
     * @return
     */
    public Boolean add(String key, int value) {
        SharedPreferences pref = this.getPreferences();
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    /**
     * gets the string value
     * @param key
     * @return
     */
    public String getString(String key) {
        SharedPreferences pref = this.getPreferences();
        return pref.getString(key, "");
    }

    /**
     * gets the int value
     * @param key
     * @return
     */
    public int getInt(String key) {
        SharedPreferences pref = this.getPreferences();
        return pref.getInt(key, 0);
    }

    /**
     * checks if the key exists
     * @param key
     * @return
     */
    public Boolean keyExists(String key) {
        return this.getPreferences().contains(key);
    }

    /**
     *
     * @return
     */
    public Map<String, ?> getKeys() {
        return this.pref.getAll();
    }

    /**
     * destroys the database file
     * @return
     */
    public Boolean destroy() {
        SharedPreferences pref = this.getPreferences();
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        return editor.commit();
    }

}
