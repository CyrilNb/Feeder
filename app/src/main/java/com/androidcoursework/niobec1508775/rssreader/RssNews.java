package com.androidcoursework.niobec1508775.rssreader;

import android.text.format.DateFormat;

import java.net.URL;
import java.util.Date;

/**
 * Class which represents a RssItem
 */
public class RssNews {

	private int iD;
	private String title;
	private String link;
	private String description;
	private Date datePublication;
	//private String date;
	private String originalSite;

	public RssNews(){};

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDatePublication() {
		return this.datePublication;
	}
	public void setDate(Date datePub) {
		this.datePublication = datePub;
	}

	/*public String getDate() {
		return date;
	}
	public void setDateString(String date) {
		this.date = date;
	}*/

	public String getOriginalSite() {
		return originalSite;
	}
	public void setOriginalSite(String originalSite) {
		this.originalSite = originalSite;
	}

	public int getId() {
		return iD;
	}
	public void setId(int id) {
		iD = id;
	}

	@Override
	public String toString() {
		return title;
	}

}
