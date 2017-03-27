package edu.hui.vassistant.supports;

import java.io.DataInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.util.Log;

public class Geo
{

    private String _latitude = "";

    private String _longtitude = "";


    public Geo()
    {

    }


    public Geo(String latitude, String longtitude)
    {
        _latitude = latitude;
        _longtitude = longtitude;
    }
    
    public String get_latitude() {
		return _latitude;
	}

	public void set_latitude(String _latitude) {
		this._latitude = _latitude;
	}

	public String get_longtitude() {
		return _longtitude;
	}

	public void set_longtitude(String _longtitude) {
		this._longtitude = _longtitude;
	}

    /// construct geo given name of a place
    public Geo(String location)
    {
        String url =  "http://maps.googleapis.com/maps/api/geocode/xml?address="+location+"&sensor=true";
        try
        {
        	DefaultHttpClient client = new DefaultHttpClient();
    		HttpUriRequest Request = new HttpGet(url);
    		HttpResponse Response = client.execute(Request);
    		
    		HttpEntity Entity = Response.getEntity();
    		InputStream stream = Entity.getContent();
    		DocumentBuilder Builder = DocumentBuilderFactory.newInstance()
    				.newDocumentBuilder();
    		Document doc = Builder.parse(new InputSource(stream));
    		
    		NodeList n  = doc.getElementsByTagName("lat");
    		
       		this._latitude = n.item(0).getChildNodes().item(0).getNodeValue();
    		
    		System.out.println("!"+_latitude);
    		
    		
    		
    		
    		n  = doc.getElementsByTagName("lng");
    		
    		this._longtitude = n.item(0).getChildNodes().item(0).getNodeValue();
    		
    		System.out.println("?"+_longtitude);
    		
    		
    	
    	//	this._latitude = n.item(0).getNodeValue();
    	//	this._longtitude = n.item(0).getChildNodes().item(1).getAttributes().item(0).getNodeValue();
    		System.out.println(_latitude+"|"+_longtitude);
        }
        catch (Exception ex)
        {
        	System.out.println(ex.toString());
        }
    }
}