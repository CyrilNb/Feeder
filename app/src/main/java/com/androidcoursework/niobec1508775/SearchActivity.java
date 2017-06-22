package com.androidcoursework.niobec1508775;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by Cyril on 26/04/16.
 */
public class SearchActivity extends AppCompatActivity {

    private Utils utils;
    private ListView mListViewSearch;
    private ArrayAdapter<Feed> adapter;
    private Button btnDate;
    private EditText editTextKeyword;
    private String mKeyword;
    private ArrayList<String> mListFeedsNamesChecked;
    private Date dateMaxToSearch = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_search);
        utils = new Utils(this.getApplicationContext());
        mListFeedsNamesChecked = new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        editTextKeyword = (EditText)findViewById(R.id.editTextSearch);
        mListViewSearch = (ListView)findViewById(R.id.listviewSearch);
        btnDate = (Button)findViewById(R.id.btnDate);
        adapter = new ArrayAdapter<Feed>(this,android.R.layout.simple_list_item_multiple_choice,utils.getAllFeedsFromPrefs());
        mListViewSearch.setAdapter(adapter);
        mListViewSearch.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            SparseBooleanArray sp = mListViewSearch.getCheckedItemPositions();
            Log.d("keyword","size: " + Integer.toString(sp.size()));
            if(sp.size() != 0) {
                    for (int i = 0; i <= sp.size(); i++) {
                        if(mListViewSearch.isItemChecked(i)){
                            String nameCheckedFeed = mListViewSearch.getItemAtPosition(i).toString();
                            mListFeedsNamesChecked.add(nameCheckedFeed);
                        }
                    }
                    mKeyword = editTextKeyword.getText().toString();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("keyword",mKeyword);
                    returnIntent.putStringArrayListExtra("listFeedsNamesChecked", mListFeedsNamesChecked);
                    if(dateMaxToSearch != null){
                        returnIntent.putExtra("datesearch",this.dateMaxToSearch);
                    }
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
            } else{
                final View viewPos = findViewById(R.id.coordinatorlayoutsearch);
                Snackbar.make(viewPos, getResources().getString(R.string.no_search), Snackbar.LENGTH_LONG)
                        .show();
            }
        }
        /*if (id == R.id.action_selectAll){
            for ( int i=0; i< mListViewSearch.getCount(); i++ ) {
                mListViewSearch.setItemChecked(i, true);
            }
        }
        if (id == R.id.action_deselectAll){
            for ( int i=0; i< mListViewSearch.getCount(); i++ ) {
                mListViewSearch.setItemChecked(i, false);
            }
        }*/

        return super.onOptionsItemSelected(item);
    }

    private void showDatePicker(){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void onDateMaxSearch(Date date){
        this.dateMaxToSearch = date;
    }
}
