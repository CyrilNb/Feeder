package com.androidcoursework.niobec1508775.rssreader;

import android.text.format.DateUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


/**
 * Class reads RSS data.
 */
public class RssReader {

    private String rssFeedUrl;
    private static SAXParserFactory factory;
    private static SAXParser saxParser;

    static {
        try {
            factory = SAXParserFactory.newInstance();
            saxParser = factory.newSAXParser();
        }
        catch(Exception e){
            Log.e("RssReaderException", "static initialisation:"+e.getMessage());
        }
    }

    public RssReader(String rssUrl) {
        this.rssFeedUrl = rssUrl;
    }

    /**
     * Get RSS News. This method will be called to get the parsing process result.
     * @return
     */
    public List<RssNews> getAllNews() /*throws Exception*/ {
    	try {
	        // We need the SAX parser handler object
	        RssParseHandler handler = new RssParseHandler();
	        saxParser.parse(rssFeedUrl, handler);
	        // The result of the parsing process is being stored in the handler object
	        return handler.getItems();
    	}
    	catch(Exception e){
    		Log.e("RssNews", "in getItems:"+e.getMessage());
    		return new ArrayList<RssNews>();
    	}
    }

    /**
     * Get RSS News. This method will be called to get the parsing process result when a search by keyword is call by the user.
     * @return
     */
    public List<RssNews> getSearchNews(String keywordToSearch) /*throws Exception*/ {
        List<RssNews> newSorted = new ArrayList<>();
        try {
            // We need the SAX parser handler object
            RssParseHandler handler = new RssParseHandler();
            saxParser.parse(rssFeedUrl, handler);
            // The result of the parsing process is being stored in the handler object
            List<RssNews> allnews = new ArrayList<>(handler.getItems());
            for (RssNews news : allnews) {
                if (news.getTitle().toLowerCase().contains(keywordToSearch.toLowerCase())) {
                    newSorted.add(news);
                }
            }
            return newSorted;
        } catch (Exception e) {
            Log.e("RssNews", "in getItems:" + e.getMessage());
            return new ArrayList<RssNews>();
        }
    }

    /**
     * Get RSS News. This method will be called to get the parsing process result when a search by keyword is call by the user and a date.
     * @return
     */
    public List<RssNews> getSearchNews(String keywordToSearch, Date dateToSearch) /*throws Exception*/ {
        List<RssNews> newSorted = new ArrayList<>();
        try {
            // We need the SAX parser handler object
            RssParseHandler handler = new RssParseHandler();
            saxParser.parse(rssFeedUrl, handler);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            // The result of the parsing process is being stored in the handler object
            List<RssNews> allnews = new ArrayList<>(handler.getItems());
            for (RssNews news : allnews) {
                if (news.getTitle().toLowerCase().contains(keywordToSearch.toLowerCase()) && ((news.getDatePublication().before(dateToSearch)) || sdf.format(news.getDatePublication()).equals(sdf.format(dateToSearch)))) {
                    newSorted.add(news);
                }
            }
            return newSorted;
        } catch (Exception e) {
            Log.e("RssNews", "in getItems:" + e.getMessage());
            return new ArrayList<RssNews>();
        }
    }
}
