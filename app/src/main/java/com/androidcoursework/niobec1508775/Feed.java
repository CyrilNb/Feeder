package com.androidcoursework.niobec1508775;

/**
 * Class which represents a feed (of a website)
 * Created by Cyril on 22/04/16.
 */
public class Feed {
    private String mName;
    private String mURL;

    public Feed(String name, String url){
        setmName(name);
        setmURL(url);
    }

    public String getmURL() {
        return mURL;
    }

    public void setmURL(String mURL) {
        this.mURL = mURL;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    @Override
    public String toString(){
        return this.mName;
    }
}
