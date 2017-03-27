package edu.hui.vassistant.supports;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


import edu.hui.vassistant.R.layout;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
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


import edu.hui.vassistant.entities.DetailWeather;
import edu.hui.vassistant.entities.WeatherWeek;

import android.util.Log;

public class YahooWeather {
	private Integer Latitude;// 纬度
	private Integer Longitude;// 经度
	private String url = null;
//	private String sTemp ="";// 温度
	public final String TODAY = "1";
	public final String TOMORROW = "2";
	public final String YESTERDAY = "3";
	private GoogleCity gc  = null ;
	public String cityName = null;
	public String cityID = null;
	public String apiKey="379fOSDV34FIzpLxPlvII28QNwqXI6DPyNMkAfwMYqwnfDPPegSlS9_oFHHUMAA.g9uS._S.YUClrbd_vUeTgC0K7_kzkDo-";
	
	public YahooWeather(double longi, double lati) {
		Latitude = (int) lati*1000000;
		Longitude = (int)longi*1000000;

		// http://www.google.com/ig/api?hl=zh-cn&weather=,,,30670000,104019996
	//	url = "http://www.google.com/ig/api?hl=en-us&weather=,,," + Longitude
		//		+ "," + Latitude;
		
		
		gc = new GoogleCity(lati,longi);
		try {
			cityName = gc.getName();
			cityID = getWorldId(cityName);
		    url = "http://weather.yahooapis.com/forecastrss?w="+cityID;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("log", "url=" + url);
	}
	
	
	public WeatherWeek getWeatherWeek(){
		
		Log.d("GetDetail", "week");
		
		DefaultHttpClient client = new DefaultHttpClient();
		HttpUriRequest Request = new HttpGet(url);
		HttpResponse Response = null;
		WeatherWeek ww=null;
		
		try {
				Response = client.execute(Request);
		
	
		HttpEntity Entity = Response.getEntity();
		InputStream stream = null;
		
			stream = Entity.getContent();
		
		DocumentBuilder Builder = null;
		
			Builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
		
		Document doc = Builder.parse(new InputSource(stream));
		
		NodeList n =null;
	//	String dayInfo = "";
		//String Icon = "";
	//	String temperature = "";
			
	//	String  condition  = "";
		ArrayList<String> days = new ArrayList<String>();
		ArrayList<String> icons = new ArrayList<String>();
		ArrayList<String> lts = new ArrayList<String>();
		ArrayList<String> hts = new ArrayList<String>();
		ArrayList<String> conditions = new ArrayList<String>();
		n = doc.getElementsByTagName("yweather:forecast");
	
		for(int i = 0;i<n.getLength();i++){
			
			days.add(n.item(i).getAttributes().item(0).getNodeValue());
			lts.add(n.item(i).getAttributes().item(2).getNodeValue()+" °F");
			hts.add(n.item(i).getAttributes().item(3).getNodeValue()+" °F");
			icons.add(n.item(i).getAttributes().item(4).getNodeValue().toLowerCase().replaceAll(" ", ""));
			conditions.add(n.item(i).getAttributes().item(4).getNodeValue());
			Log.v("add", ""+i);
		}
		ww = new WeatherWeek(days,icons,lts,hts,conditions,layout.weatherweekdetail);
		
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return ww;
	}
	
	
	
	
	
	public DetailWeather getWeatherDetail(String day){
		Log.d("GetDetail", day);
		try {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpUriRequest Request = new HttpGet(url);
		HttpResponse Response = client.execute(Request);
		
		HttpEntity Entity = Response.getEntity();
		InputStream stream = Entity.getContent();
		DocumentBuilder Builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document doc = Builder.parse(new InputSource(stream));
		NodeList n =null;
		String dayInfo = "";
		String Icon = null;
		String temperature = "";
		String Humidity = "";
		String wind = "";
		String  condition  = "";
		if(day.equals("1")){
			dayInfo = "Today";
			
			
		}else if(day.equals("2")){
			dayInfo = "tomorrow";
		}
		switch(Integer.parseInt(day)){
		case 1:
			 n = doc.getElementsByTagName("yweather:forecast");
			
			 String weatherCondition =  n.item(0).getAttributes().item(4).getNodeValue();
			 
			 Icon = weatherCondition.toLowerCase().replaceAll(" ", "");

 			 
			 temperature = doc.getElementsByTagName("yweather:condition").item(0).getAttributes().item(2).getNodeValue()+"°F";
			
			
				
			 
			 condition = weatherCondition;
			 
			 Humidity = doc.getElementsByTagName("yweather:atmosphere").item(0).getAttributes().item(0).getNodeValue();
			 wind =  doc.getElementsByTagName("yweather:wind").item(0).getAttributes().item(2).getNodeValue()+" mph";
			 break;
		
		}
		System.out.println( Icon+"|"+temperature+"|"+dayInfo+"|"+condition);
		
		DetailWeather dw = new DetailWeather(Icon,temperature,dayInfo,condition,Humidity,wind,gc.getName(), layout.weatherdetail);
		
		return dw;
		} catch(Exception E){
		}
		return null;
		
	}

	public String getWeatherData(String day) throws ClientProtocolException, IOException,
			ParserConfigurationException, FactoryConfigurationError,
			SAXException {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpUriRequest Request = new HttpGet(url);
		HttpResponse Response = client.execute(Request);
		HttpEntity Entity = Response.getEntity();
		InputStream stream = Entity.getContent();
		DocumentBuilder Builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document doc = Builder.parse(new InputSource(stream));
		NodeList n =null;
		String dayInfo = "";
		if(day.equals("1")){
			dayInfo = "today";
			
			
		}else if(day.equals("2")){
			dayInfo = "tomorrow";
		}
		String result = "This is "+dayInfo+"'s weather: Condition: ";
		
		switch(Integer.parseInt(day)){
		case 1:
			 n = doc.getElementsByTagName("current_conditions");
			 result += n.item(0).getChildNodes().item(0).getAttributes().item(0)
						.getNodeValue()+", Temperature:"+n.item(0).getChildNodes().item(1).getAttributes().item(0)
						.getNodeValue()+"°F, "+n.item(0).getChildNodes().item(3).getAttributes().item(0)
						.getNodeValue();
			break;
		case 2:
			n = doc.getElementsByTagName("forecast_conditions");
			result += n.item(0).getChildNodes().item(4).getAttributes().item(0)
					.getNodeValue()+",low Temperature:"+n.item(0).getChildNodes().item(1).getAttributes().item(0)
					.getNodeValue()+"°F, "+",high Temperature:"+n.item(0).getChildNodes().item(2).getAttributes().item(0)
					.getNodeValue()+"°F, ";
			break;
		}
		
	

		Log.d("log", "Node Length=" + n.getLength());
		
		//for (int i = 0; i < n.getLength(); i++)// 遍列current_condition所有节点
		//{
			// 获取节点的天气数据
			
		
			
		//}
		return result;
	}
	public String getWorldId(String name){
		String result = "";
		try {
		String url = "http://where.yahooapis.com/v1/places.q('"+name+"')?appid=379fOSDV34FIzpLxPlvII28QNwqXI6DPyNMkAfwMYqwnfDPPegSlS9_oFHHUMAA.g9uS._S.YUClrbd_vUeTgC0K7_kzkDo-";
		DefaultHttpClient client = new DefaultHttpClient();
		HttpUriRequest Request = new HttpGet(url);
		HttpResponse Response;
		
		
				Response = client.execute(Request);
		
		HttpEntity Entity = Response.getEntity();
		InputStream stream = Entity.getContent();
		DocumentBuilder Builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document doc = Builder.parse(new InputSource(stream));
		NodeList n  = doc.getElementsByTagName("woeid");
		String id = n.item(0).getChildNodes().item(0).getNodeValue();
		result = id;
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return result;
	}

}