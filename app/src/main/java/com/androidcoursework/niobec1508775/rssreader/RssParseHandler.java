package com.androidcoursework.niobec1508775.rssreader;


import android.util.Log;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.net.URL;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * SAX tag handler. The Class contains a list of RssNews which is being filled while the parser is working
 */
public class RssParseHandler extends DefaultHandler {
 
    // List of news parsed
    private List<RssNews> rssNewses;
    // Used to reference news while parsing
    private RssNews currentItem;
    // parsing item indicator
    private boolean parsingItem;
    // We have three indicators which are used to differentiate whether a tag title, link or description is being processed by the parser
    private boolean parsingTitle;
    private boolean parsingLink;
	private boolean parsingDescription;
	private boolean parsingDate;
	private Date dateToSave;

	private DateFormat dateFormat;

    // Add characters to title while parsing title
    StringBuilder title;
    StringBuilder link;
	StringBuilder description;
	StringBuilder date;

    public RssParseHandler() {
		rssNewses = new ArrayList<RssNews>();
        parsingItem = false;
        parsingTitle = false;
        parsingLink = false;
		parsingDate = false;
		dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
    }

    public List<RssNews> getItems() {
        return rssNewses;
    }

    // The StartElement method creates an empty RssNews object when an item start tag is being processed. When a title, link or description tag are being processed appropriate indicators are set to true.
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) /*throws SAXException*/ {
    	try {
	        if ("item".equals(qName)) {
	        	parsingItem = true;
	            currentItem = new RssNews();
	        } else if ("title".equals(qName) && parsingItem) {
	            parsingTitle = true;
	            title = new StringBuilder();
	        } else if ("link".equals(qName) && parsingItem) {
	            parsingLink = true;
	            link = new StringBuilder();
	        } else if ("description".equals(qName) && parsingItem) {
				parsingDescription = true;
				description = new StringBuilder();
			} else if ("pubDate".equals(qName) && parsingItem) {
				parsingDate = true;
				date = new StringBuilder();
			}
    	}
    	catch(Exception e){
    		Log.e("RssParseHandler", "in startElement: "+e.getMessage());
    	}
    }

    // The EndElement method adds the  current Rssnews to the list when a closing item tag is processed. It sets appropriate indicators to false -  when title, link and description closing tags are processed
    @Override
    public void endElement(String uri, String localName, String qName) /*throws SAXException*/ {
    	try {
	        if ("item".equals(qName)) {
				rssNewses.add(currentItem);
	            currentItem = null;
	            parsingItem = false;
	        } else if ("title".equals(qName) && parsingItem) {
	        	// now save the title in current item
	            currentItem.setTitle(title.toString());
	            parsingTitle = false;
	        } else if ("link".equals(qName) && parsingItem) {
	        	// now save link in current item
	        	currentItem.setLink(link.toString());
				URL url = new URL(currentItem.getLink());
				currentItem.setOriginalSite(url.getHost());
	            parsingLink = false;
	        } else if ("description".equals(qName) && parsingItem) {
				// now save description in current item
				currentItem.setDescription(description.toString());
				parsingDescription = false;
			} else if ("pubDate".equals(qName) && parsingItem) {
				// now save date in current item
				try{
					String dateString = date.toString();
					dateToSave = dateFormat.parse(dateString);
					currentItem.setDate(dateToSave);
				}catch (ParseException e){
					Log.d("news",e.toString());
				}
				currentItem.setDate(dateToSave);
				//currentItem.setDateString(date.toString());
				parsingDate = false;

			}
    	}
    	catch(Exception e){
    		Log.e("RssParseHandler", "in endElement: "+e.getMessage());  	
    	}
    }

    // Characters method adds to title, link or description
    @Override
    public void characters(char[] ch, int start, int length) /*throws SAXException*/ {
    	try {
	        if (parsingTitle && parsingItem) {
	            title.append(ch, start, length);
	        } else if (parsingLink && parsingItem) {
	            link.append(ch, start, length);
	        } else if (parsingDescription && parsingItem) {
				description.append(ch, start, length);
			} else if (parsingDate && parsingItem) {
				date.append(ch, start, length);
			}
    	}
    	catch (Exception e) {
    		Log.e("RssParseHandler", "in characters: "+e.getMessage());
    	}
    }
}