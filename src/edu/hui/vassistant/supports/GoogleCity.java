package edu.hui.vassistant.supports;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.util.Log;

public class GoogleCity {
	private Double Latitude;// Î³¶È
	private Double Longitude;// ¾­¶È
	private String url = "";
	public GoogleCity(double latitude, double longitude) {
		super();
		
		
		Latitude =	latitude;
		Longitude = longitude;
		
		url = "http://maps.google.com/maps/api/geocode/xml?latlng="+Longitude+","+Latitude+"&language=en-us&sensor=true";
		
		Log.v("cityUrl", url);
		
		
		
	}
	public String getName() throws ClientProtocolException, IOException,
	ParserConfigurationException, SAXException {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpUriRequest Request = new HttpGet(url);
		HttpResponse Response = client.execute(Request);
		HttpEntity Entity = Response.getEntity();
		InputStream stream = Entity.getContent();
		DocumentBuilder Builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document doc = Builder.parse(new InputSource(stream));
		NodeList n  = doc.getElementsByTagName("result");

		String cityName =  n.item(0).getChildNodes().item(9).getChildNodes().
				item(1).getChildNodes().item(0).getNodeValue();
		return cityName;
		
		
	}

}
