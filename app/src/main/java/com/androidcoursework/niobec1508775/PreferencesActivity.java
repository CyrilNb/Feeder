package com.androidcoursework.niobec1508775;

/**
 * Created by Cyril on 22/04/16.
 */


import android.app.Activity;
import android.app.Application;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class PreferencesActivity extends AppCompatActivity {

    private static final int MAX_FEEDS_IN_PREFERENCES = 10;
    private int counterFeeds;
    public ArrayList<Feed> mListFeedsPref = new ArrayList<Feed>();
    private ListView mListViewFeedsPref;
    private ArrayAdapter<Feed> adapter;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
         utils = new Utils(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_preferences);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        sharedPreferences = this.getSharedPreferences(getResources().getString(R.string.shared_preferences_file),MODE_PRIVATE);
        editor = sharedPreferences.edit();

        FloatingActionButton fabAddFeed = (FloatingActionButton) findViewById(R.id.fabAddFeed);
        fabAddFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(counterFeeds < MAX_FEEDS_IN_PREFERENCES) {
                    showDetailsDialog();
                }
                else{
                    Snackbar.make(view, "10 RSS feeds maximum !", Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        });

        mListViewFeedsPref = (ListView)findViewById(R.id.listViewPreferencesFeeds);
        mListViewFeedsPref.setEmptyView(findViewById(R.id.textViewNoURL));
        adapter = new ArrayAdapter<Feed>(this.getBaseContext(),android.R.layout.simple_list_item_2, android.R.id.text1,mListFeedsPref){
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position,convertView,parent);
                TextView tv1 = (TextView) view.findViewById(android.R.id.text1);
                TextView tv2 = (TextView) view.findViewById(android.R.id.text2);
                tv1.setText(mListFeedsPref.get(position).getmName());
                tv2.setText(mListFeedsPref.get(position).getmURL());
                return view;
            }
        };
        mListViewFeedsPref.setAdapter(adapter);
        this.registerForContextMenu(mListViewFeedsPref);

        mListFeedsPref.addAll(utils.getAllFeedsFromPrefs());
        adapter.notifyDataSetChanged();
        counterFeeds = adapter.getCount();
    }

    public void addFeed(Feed feed){
        this.mListFeedsPref.add(feed);
        adapter.notifyDataSetChanged();
        editor.putString(feed.getmName(),feed.getmURL());
        editor.commit();
        counterFeeds = adapter.getCount();
    }


    private void showDetailsDialog() {
        DialogFragment newFragment = FeedDetailsDialogFragment
                .newInstance();
        newFragment.show(getFragmentManager(), "prompt dialog");

    }

    private void showEditDialog(String name, String url,int position) {
        DialogFragment newFragment = FeedDetailsDialogFragment
                .newInstance(name,url,position);
        newFragment.show(getFragmentManager(), "edit dialog");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.popup_managefeed, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        String name, url;
        switch (item.getItemId()) {
            case R.id.item_edit:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                name =  mListFeedsPref.get(info.position).getmName();
                url = mListFeedsPref.get(info.position).getmURL();
                showEditDialog(name,url,info.position);
                return true;
            case R.id.item_delete:
                AdapterView.AdapterContextMenuInfo info2 = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                name = mListFeedsPref.get(info2.position).getmName();
                int position = info2.position;
                deleteFeed(name,position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_preferences, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            if(counterFeeds !=0){
            AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.title_alert_delete_all_dialog)
                .setMessage(R.string.message_alert_delete_all)
                .setPositiveButton(R.string.prompt_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAllFeeds();
                    }
                })
                .setNegativeButton(R.string.prompt_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"Canceled",Toast.LENGTH_LONG).show();
                    }
                })
                .show();
            }
            else{
                Toast.makeText(getApplicationContext(),"No feeds to delete",Toast.LENGTH_LONG).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteAllFeeds(){
        editor.clear();
        editor.commit();
        mListFeedsPref.clear();
        adapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(),"All RSS feeds Removed",Toast.LENGTH_LONG).show();
    }

    private void deleteFeed(String nameFeedToDelete, int position){
        editor.remove(nameFeedToDelete);
        editor.commit();
        mListFeedsPref.remove(position);
        adapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(),"RSS feed Removed. Refresh News!",Toast.LENGTH_LONG).show();
    }

    public void editFeed(String oldNameFeed, String newNameFeed, String newUrlFeed,int position){
        mListFeedsPref.remove(position);
        editor.remove(oldNameFeed);

        Feed editedFeed = new Feed(newNameFeed, newUrlFeed);
        this.addFeed(editedFeed);
        editor.putString(newNameFeed,newUrlFeed);

        editor.commit();
        adapter.notifyDataSetChanged();

        Toast.makeText(PreferencesActivity.this, "Feed edited. Refresh News!", Toast.LENGTH_SHORT).show();
    }

}
