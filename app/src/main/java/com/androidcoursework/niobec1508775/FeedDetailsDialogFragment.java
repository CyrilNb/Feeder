package com.androidcoursework.niobec1508775;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Cyril on 23/04/16.
 */
public class FeedDetailsDialogFragment extends DialogFragment{

    private String mFeedOldName;
    private String mFeedOldUrl;
    private int mPosition;
    private static boolean editable;

    public static FeedDetailsDialogFragment newInstance() {
        FeedDetailsDialogFragment fragment = new FeedDetailsDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        editable = false;
        return fragment;
    }
    public static FeedDetailsDialogFragment newInstance(String name, String url,int position) {
        FeedDetailsDialogFragment fragment = new FeedDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putString("name",name);
        args.putString("url",url);
        args.putInt("position",position);
        fragment.setArguments(args);
        editable = true;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Activity parent = getActivity();
        LayoutInflater factory = LayoutInflater.from(parent);
        final View feedDetailsView = factory.inflate(R.layout.new_feeds_details, null);

        AlertDialog.Builder dialog_builder = new AlertDialog.Builder(getActivity());
        dialog_builder.setView(feedDetailsView);

        if(editable) {
            if (!getArguments().isEmpty()) {
                mFeedOldName = getArguments().getString("name");
                mFeedOldUrl = getArguments().getString("url");
                mPosition = getArguments().getInt("position");
                final EditText feedNameEditText = (EditText) feedDetailsView.findViewById(R.id.editText_feed_name);
                final EditText feedUrlEditText = (EditText) feedDetailsView.findViewById(R.id.editText_feed_url);
                feedNameEditText.setText(mFeedOldName);
                feedUrlEditText.setText(mFeedOldUrl);

                dialog_builder.setIcon(android.R.drawable.ic_menu_edit);
                dialog_builder.setTitle("Edit Feed");
                dialog_builder.setPositiveButton(R.string.prompt_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String mFeedNewName = feedNameEditText.getText().toString();
                        String mFeedNewUrl = feedUrlEditText.getText().toString();
                        ((PreferencesActivity)getActivity()).editFeed(mFeedOldName,mFeedNewName,mFeedNewUrl,mPosition);
                    }
                });
                dialog_builder.setNegativeButton(R.string.prompt_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(parent.getApplicationContext(),"Canceled",Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
        else{
            dialog_builder.setIcon(android.R.drawable.ic_menu_add);
            dialog_builder.setTitle("New Feed");
            dialog_builder.setPositiveButton(R.string.prompt_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    EditText feedNameEditText = (EditText) feedDetailsView.findViewById(R.id.editText_feed_name);
                    EditText feedUrlEditText = (EditText) feedDetailsView.findViewById(R.id.editText_feed_url);
                    String feed_name = feedNameEditText.getText().toString();
                    String feed_url = feedUrlEditText.getText().toString();
                    Feed newFeed = new Feed(feed_name,feed_url);
                    ((PreferencesActivity)getActivity()).addFeed(newFeed);
                    Toast.makeText(parent.getApplicationContext(), "Feed added. Refresh the News!", Toast.LENGTH_SHORT).show();
                }
            });
            dialog_builder.setNegativeButton(R.string.prompt_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Toast.makeText(parent.getApplicationContext(),"You canceled the addition of a new feed",Toast.LENGTH_LONG).show();
                }
            });
        }


        return dialog_builder.create();
    }
}


