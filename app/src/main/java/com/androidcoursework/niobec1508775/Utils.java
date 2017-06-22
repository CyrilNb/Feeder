package com.androidcoursework.niobec1508775;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import com.androidcoursework.niobec1508775.rssreader.GetRssDataTask;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Cyril on 26/04/16.
 *
 * This class is a helper object.
 * It contains operations which are common to several classes.
 */
public class Utils {

    private static SharedPreferences sharedPreferences;

    public Utils(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.shared_preferences_file),Context.MODE_PRIVATE);
    }

    /**
     * Method returns the list of feeds saved into preferences file.
     * @return
     */
    public ArrayList<Feed> getAllFeedsFromPrefs(){
        ArrayList<Feed> list = new ArrayList<>();
        Map<String,?> keys = sharedPreferences.getAll();
        if(keys != null){
            for (Map.Entry<String,?> entry : keys.entrySet()) {
                list.add(new Feed(entry.getKey(),entry.getValue().toString()));
            }
        }
        return list;
    }

    public Feed getFeedFromPrefs(String feedName){
        Feed feed = null;
        Map<String,?> keys = sharedPreferences.getAll();
        if(keys != null){
            for (Map.Entry<String,?> entry : keys.entrySet()) {
                if(entry.getKey().equals(feedName))
                    feed = new Feed(entry.getKey(),entry.getValue().toString());
            }
        }
        return feed;
    }

}
