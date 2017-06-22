package com.androidcoursework.niobec1508775.rssreader;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.androidcoursework.niobec1508775.ListListener;
import com.androidcoursework.niobec1508775.MyRssNewsAdapter;
import com.androidcoursework.niobec1508775.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GetRssDataTask extends AsyncTask<String, Void, List<RssNews> > {

	private Activity activity;
    private ArrayList<String> mLinkFeeds;
    private String mKeyword;
    private Date mDateSearch;
    private MyRssNewsAdapter adapter;
    private ListView mListViewNews;
    private SQLiteDatabase mDatabase;


	public GetRssDataTask(Activity activity,ArrayList<String> linkFeeds, String keyword, Date dateMaxSearch) {
		this.activity = activity;
        this.mLinkFeeds = new ArrayList<>(linkFeeds);
        this.mKeyword = keyword == null ? "" : keyword;
        this.mDateSearch = dateMaxSearch;

    }


    @Override
    protected List<RssNews> doInBackground(String... params) {
        ArrayList<RssNews> mAllNews = new ArrayList<RssNews>();
        try {
            for(String values : mLinkFeeds){
                // Create RSS reader
                RssReader rssReader = new RssReader(values);

                // Parse RSS, get items
                if(mKeyword.isEmpty()){
                    mAllNews.addAll(rssReader.getAllNews());

                } else {
                    if(mDateSearch == null){
                        mAllNews.addAll(rssReader.getSearchNews(mKeyword));
                    }
                    else{
                        mAllNews.addAll(rssReader.getSearchNews(mKeyword,mDateSearch));
                    }
                }
            }
            return mAllNews;
         
        } catch (Exception e) {
            Log.e("RssReader", "in doInBackground"+e.getMessage());
        }


        Toast.makeText(this.activity.getApplicationContext(),"Fatal parse error occured",Toast.LENGTH_LONG).show();
        return new ArrayList<>();
    }
     
    @Override
    protected void onPostExecute(List<RssNews> result) {
        mListViewNews = (ListView) activity.findViewById(R.id.listViewNews);
        adapter = new MyRssNewsAdapter(activity.getApplicationContext(),R.layout.list_news,result);
        mListViewNews.setOnItemClickListener(new ListListener(activity));
        mListViewNews.setAdapter(adapter);


    }

}
