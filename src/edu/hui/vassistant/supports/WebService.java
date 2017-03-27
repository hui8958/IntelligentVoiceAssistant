package edu.hui.vassistant.supports;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Application;
import android.util.Log;

public class WebService extends Application {
	
	   private static final String SOAP_ACTION = "http://tempuri.org/commandGenerator"; 
	   private static final String METHOD_NAME = "commandGenerator"; 
	   private static final String NAMESPACE = "http://tempuri.org/"; 
	   private static final String URL = "http://androidvoice.cloudapp.net/WebService1.asmx"; 

	public WebService() {

	}

	public String getResponse(String info) {
		String Response = "";
		try {

			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
			request.addProperty("text", info.toLowerCase());
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			
			Response = String.valueOf( envelope.getResponse());
			Log.v("Response", Response);

		} catch (IOException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		} catch (XmlPullParserException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Response:" + Response);
		return Response;
	}
}
