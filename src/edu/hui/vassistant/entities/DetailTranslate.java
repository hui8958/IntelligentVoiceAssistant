package edu.hui.vassistant.entities;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;


public class DetailTranslate extends DetailEntity {
	private String text;
	private String language;
	private String result;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public DetailTranslate(String language, String result, String text,
			int layoutID) {

		this.language = language;
		this.result = result;
		// this.date = date;
		this.text = text;
		super.layoutID = layoutID;
	}

	public String getTranslateResult() {


		return result;
	}

	public String getDetailName() {
		return "translate";
	}

	

}
