package com.androidcoursework.niobec1508775;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidcoursework.niobec1508775.database.DbHelper;
import com.androidcoursework.niobec1508775.database.NewsDataSource;
import com.androidcoursework.niobec1508775.database.SQLiteNewsDataLoader;
import com.androidcoursework.niobec1508775.rssreader.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, LoaderManager.LoaderCallbacks<ArrayList<RssNews>>{

    private static final int REQUEST_CODE = 0;
    public ArrayList<String> mListAllURLFeeds = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefresh;
    private Utils utils;
    private ListView mListViewNews;
    public List<RssNews> mListNewsReturned;
    public MyRssNewsAdapter adapter;
    private ArrayList<String> searchListFromExtra;
    private String keywordFromExtra = "";
    private Date dateSearch = null;

    private SQLiteDatabase mDatabase;
    private NewsDataSource mDataSource;
    private DbHelper mDbHelper;
    private static final int LOADER_ID = 1;
    // The callbacks through which we will interact with the LoaderManager.
    private LoaderManager.LoaderCallbacks<ArrayList<RssNews>> mCallbacks;
    private LoaderManager mLoaderManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        mListViewNews = (ListView)findViewById(R.id.listViewNews);

        utils = new Utils(this.getApplicationContext());
        searchListFromExtra = new ArrayList<>();

        //open database
        mDbHelper = new DbHelper(this);
        mDatabase = mDbHelper.getWritableDatabase();
        mDataSource = new NewsDataSource(mDatabase);

        // The Activity (which implements the LoaderCallbacks<Cursor>
        // interface) is the callbacks object through which we will interact
        // with the LoaderManager. The LoaderManager uses this object to
        // instantiate the Loader and to notify the client when data is made
        // available/unavailable.
        mCallbacks = this;

        // Initialize the Loader with id '1' and callbacks 'mCallbacks'.
        // If the loader doesn't already exist, one is created. Otherwise,
        // the already created Loader is reused. In either case, the
        // LoaderManager will manage the Loader across the Activity/Fragment
        // lifecycle, will receive any new loads once they have completed,
        // and will report this new data back to the 'mCallbacks' object.
        mLoaderManager = getLoaderManager();
        mLoaderManager.initLoader(LOADER_ID, null, mCallbacks);

        mListNewsReturned = new ArrayList<>();

        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeColors(Color.RED,Color.GREEN,Color.BLUE,Color.CYAN);

        for (Feed values : utils.getAllFeedsFromPrefs())
        {
            mListAllURLFeeds.add(values.getmURL());
        }
        //Log.d("feed",Integer.toString(mListAllURLFeeds.size()));


        if(mListAllURLFeeds.size() == 0){
            final View viewPos = findViewById(R.id.myCoordinatorLayout);
            Snackbar.make(viewPos, getResources().getString(R.string.no_results), Snackbar.LENGTH_LONG)
                    .show();
        }
        else{
            GetRssDataTask task = new GetRssDataTask(this,mListAllURLFeeds,null,null);
            task.execute();
        }
        adapter = new MyRssNewsAdapter(this, R.layout.list_news, mListNewsReturned);
        mListViewNews.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabSearch);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SearchActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent intent = new Intent();
            intent.setClass(MainActivity.this, PreferencesActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        mListAllURLFeeds.clear();
        for (Feed values : utils.getAllFeedsFromPrefs())
        {
            mListAllURLFeeds.add(values.getmURL());
        }
        Log.d("feed","refresh :" + Integer.toString(mListAllURLFeeds.size()));

        if(mListAllURLFeeds.size() == 0){
            final View viewPos = findViewById(R.id.myCoordinatorLayout);
            Snackbar.make(viewPos, getResources().getString(R.string.no_results), Snackbar.LENGTH_LONG)
                    .show();
        }
        if(dateSearch != null){
            GetRssDataTask task = new GetRssDataTask(this,mListAllURLFeeds,null,dateSearch);
            task.execute();
        }
        else{
            GetRssDataTask task = new GetRssDataTask(this,mListAllURLFeeds,null,null);
            task.execute();
        }

        mSwipeRefresh.setRefreshing(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if(resultCode == Activity.RESULT_OK){
                searchListFromExtra.clear();
                searchListFromExtra.addAll(data.getStringArrayListExtra("listFeedsNamesChecked"));
                keywordFromExtra = data.getStringExtra("keyword");
                dateSearch = (Date) data.getSerializableExtra("datesearch");
                ArrayList<String> urlFeedsToSearchIn = new ArrayList<>();
                for(String values : searchListFromExtra){
                    urlFeedsToSearchIn.add(utils.getFeedFromPrefs(values).getmURL());
                }
                GetRssDataTask task = new GetRssDataTask(this,urlFeedsToSearchIn,keywordFromExtra,dateSearch);
                task.execute();

            }

        }
    }

    @Override
    public Loader<ArrayList<RssNews>> onCreateLoader(int id, Bundle args) {
        SQLiteNewsDataLoader loader = new SQLiteNewsDataLoader(this, mDataSource, null, null, null, null, null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<RssNews>> loader, ArrayList<RssNews> data) {
        adapter.clear();
        for(RssNews news : data){
            adapter.add(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<RssNews>> loader) {
        {
            adapter.clear();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDbHelper.close();
        mDatabase.close();
        mDataSource = null;
        mDbHelper = null;
        mDatabase = null;
    }

}
