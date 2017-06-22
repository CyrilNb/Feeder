package com.androidcoursework.niobec1508775;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.androidcoursework.niobec1508775.rssreader.RssNews;

/**
 * Class implements a list listener.
 */
public class ListListener implements OnItemClickListener {
    // And a reference to a calling activity
    // Calling activity reference
    Activity mParent;
    public ListListener(Activity parent) {
        mParent  = parent;
    }

    /** Start a browser with url from the rss item.*/
    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri url = Uri.parse(((RssNews)parent.getItemAtPosition(pos)).getLink());
        intent.setData(url);
        mParent.startActivity(intent);
    }
}
