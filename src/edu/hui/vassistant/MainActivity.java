package edu.hui.vassistant;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.StringTokenizer;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import edu.hui.vassistant.activities.CameraActivity;
import edu.hui.vassistant.activities.DateMemoActivity;
import edu.hui.vassistant.activities.HelpActivity;
import edu.hui.vassistant.activities.MapsActivity;
import edu.hui.vassistant.activities.NavigationActivity;
import edu.hui.vassistant.entities.DetailAdapter;
import edu.hui.vassistant.entities.DetailEntity;
import edu.hui.vassistant.entities.DetailImage;
import edu.hui.vassistant.entities.DetailText;
import edu.hui.vassistant.entities.DetailTranslate;
import edu.hui.vassistant.entities.DetailWeather;
import edu.hui.vassistant.entities.WeatherWeek;
import edu.hui.vassistant.receivers.AlarmReceiver;
import edu.hui.vassistant.receivers.BluetoothReceiver;
import edu.hui.vassistant.receivers.ScanSdReceiver;
import edu.hui.vassistant.supports.DefaultResponse;
import edu.hui.vassistant.supports.Geo;
import edu.hui.vassistant.supports.GetContacts;
import edu.hui.vassistant.supports.Person;
import edu.hui.vassistant.supports.Translator;
import edu.hui.vassistant.supports.WebService;
import edu.hui.vassistant.supports.YahooWeather;

public class MainActivity extends Activity implements OnInitListener {
	private ListView talkView;
	private ArrayList<DetailEntity> list = null;
	private EditText te = null;
	private Button sendBtn = null;
	private WebService ws = null;
	private YahooWeather gw = null;
	private Geo geo = null;
	private List<Person> personList = null;
	private ScanSdReceiver scanSdReceiver = null;
	private BluetoothReceiver bluetoothReceiver  = null;
	private boolean chat = false;
	private Button ChangeMode = null;
	private Button speakButton = null;
	private MediaPlayer sMediaPlayer = null;
	private MediaPlayer rMediaPlayer = null;
	private TextToSpeech mTts = null;
	private SpeechRecognizer sr = null;
	private String userInputMessage = null;
	public static Handler myHandler = null;
	private ImageButton bts = null;
	static Context c = null;
	GetContacts getContacts = new GetContacts();
	private AudioManager amanager = null;
	boolean bluetoothheadset = false;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
		intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
	
		bluetoothReceiver = new BluetoothReceiver();
		registerReceiver(bluetoothReceiver, intentFilter);
		// amanager.startBluetoothSco();
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
	//	bd = BluetoothDevice.this;
		te = (EditText) findViewById(R.id.editText1);
		talkView = (ListView) findViewById(R.id.list);
		sendBtn = (Button) findViewById(R.id.button1);
		ChangeMode = (Button) findViewById(R.id.buttonChangeMode);
		speakButton = (Button) findViewById(R.id.buttonSpeak);
		bts = (ImageButton) findViewById(R.id.imageButtonbt);
		// ChangeMode.setText("B");
	
		
		ChangeMode.setBackgroundResource(R.drawable.speechbutton);
		te.setVisibility(View.INVISIBLE);
		speakButton.setVisibility(View.VISIBLE);
		sendBtn.setOnClickListener(listener);
		bts.setVisibility(View.VISIBLE);
		bts.setOnClickListener(listener);
		
		sendBtn.setVisibility(View.INVISIBLE);
		ChangeMode.setOnClickListener(listener);
		speakButton.setOnClickListener(listener);
		list = new ArrayList<DetailEntity>();
		DetailEntity d1 = new DetailText("he", "what can I help you with?",
				R.layout.list_say_he_item);
		list.add(d1);
		c = this;
		IntentFilter intentfilter = new IntentFilter(
				Intent.ACTION_MEDIA_SCANNER_STARTED);
		intentfilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
		intentfilter.addDataScheme("file");
		scanSdReceiver = new ScanSdReceiver();
		registerReceiver(scanSdReceiver, intentfilter);
		sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
				Uri.parse("file://"
						+ Environment.getExternalStorageDirectory()
								.getAbsolutePath())));
	

		// talkView.setOnScrollListener(listScrollListener);
		talkView.setAdapter(new DetailAdapter(MainActivity.this, list));
		myHandler = new Handler() {
			MediaPlayer mediaPlayer = null;
			AlertDialog.Builder ad = null;

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					try {
						sendWSMessage(userInputMessage);
					} catch (Exception e) {
						speakUnknowMessage();
						System.out.println(e.toString());
						talkView.setAdapter(new DetailAdapter(
								MainActivity.this, list));
					}
					break;
				case 3:
					mediaPlayer = MediaPlayer.create(c, R.raw.canon);

					if (!mediaPlayer.isPlaying()) {
						mediaPlayer.start();
					}

					ad = new AlertDialog.Builder(c);
					ad.setTitle("Alarm");// 设置对话框标题
					ad.setMessage("Times up!");// 设置对话框内容
					persendInfo("he", "Time's up.");
					ad.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int i) {
									// TODO Auto-generated method stub
									if (mediaPlayer.isPlaying()) {
										mediaPlayer.stop();
									}
								}
							});
					ad.setNegativeButton("NO",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int i) {

								}
							});

					break;
				case 4:

					bts.setEnabled(true);
					bluetoothheadset=true;
					break;
				case 5:

					bts.setEnabled(false);
					bluetoothheadset=false;
					break;
				}

			}
		};
		
		ws = (WebService) this.getApplicationContext();

	
	
		TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String uuid = tManager.getDeviceId();

	
		if (bluetoothheadset) {
			// System.out.println("BluetoothAdapter.getBondedDevices().size()"+mBluetoothAdapter.getBondedDevices().size());

			bts.setEnabled(true);
		} else {
			bts.setEnabled(false);
		}
	
		sr = SpeechRecognizer.createSpeechRecognizer(this);
		sr.setRecognitionListener(new speechListener());

		sMediaPlayer = MediaPlayer.create(this, R.raw.speech_on);
		rMediaPlayer = MediaPlayer.create(this, R.raw.speech_off);

		// sMediaPlayer.setAudioStreamType(AudioManager.MODE_IN_CALL);
		// rMediaPlayer.setAudioStreamType(AudioManager.MODE_IN_CALL);

	
		
		amanager = (AudioManager) getSystemService("audio");
	
		// private static final String ACTION_BT_HEADSET_STATE_CHANGED =
		// "android.bluetooth.headset.action.STATE_CHANGED";
		
		
//		registerReceiver(mBlueToothHeadSetEventReceiver, filter1);
//		registerReceiver(mBlueToothHeadSetEventReceiver, filter2);
//		registerReceiver(mBlueToothHeadSetEventReceiver, filter3);
//		registerReceiver(mBlueToothHeadSetEventReceiver, filter4);

//		Intent intentBt = new Intent(MainActivity.this,
//				BluetoothReceiver.class);
//		PendingIntent pendingIntent = PendingIntent.getBroadcast(
//				MainActivity.this, 0, intentBt, 0);
	//	BroadcastReceiver mBlueToothHeadSetEventReceiver = 
		//		new mBlueToothHeadSetEventReceiver();
//
//		registerReceiver(mBlueToothHeadSetEventReceiver, filter1);
//		registerReceiver(mBlueToothHeadSetEventReceiver, filter2);
//		registerReceiver(mBlueToothHeadSetEventReceiver, filter3);
//		registerReceiver(mBlueToothHeadSetEventReceiver, filter4);

		// IntentFilter intentFilter = new IntentFilter(
		// "android.bluetooth.headset.extra.STATE" );

		// registerReceiver( mBlueToothHeadSetEventReceiver , intentFilter);

		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, 0);

	}


	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem helpMItm = menu.add(1, 1, 1, "Help");
		helpMItm.setIcon(R.drawable.helpicon);

		MenuItem exitMItm = menu.add(1, 2, 2, "Exit");
		exitMItm.setIcon(R.drawable.exiticon);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case 1:
			Intent it = new Intent();
			it.setClass(MainActivity.this, HelpActivity.class);
			startActivity(it);
			break;
		case 2:
			this.finish();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	private int getCategory(String text) {
		StringTokenizer st = new StringTokenizer(text, "|");
		while (st.hasMoreTokens()) {
			return Integer.parseInt(st.nextToken());

		}
		return 0;
	}

	private String getSecondInfo(String text) {
		StringTokenizer st = new StringTokenizer(text, "|");
		while (st.hasMoreTokens()) {
			st.nextToken();
			return st.nextToken();

		}
		return "0";
	}

	private String getName(String text) {
		StringTokenizer st = new StringTokenizer(text, "|");
		while (st.hasMoreTokens()) {
			st.nextToken();
			return st.nextToken();
		}
		return "0";
	}

	private String getThirdInfo(String text) {
		StringTokenizer st = new StringTokenizer(text, "|");
		while (st.hasMoreTokens()) {
			st.nextToken();
			st.nextToken();
			return st.nextToken();

		}
		return "0";
	}

	private String getFourthInfo(String text) {
		StringTokenizer st = new StringTokenizer(text, "|");
		while (st.hasMoreTokens()) {
			st.nextToken();
			st.nextToken();
			st.nextToken();
			return st.nextToken();

		}
		return "0";
	}


	private OnClickListener listener = new OnClickListener() {

		public void onClick(View v) {
			Log.v("Click", "Button!!");
			if (v instanceof Button) {
				Button btn = (Button) v;
				// Intent intent=new Intent();
				switch (btn.getId()) {

				case R.id.button1:
					playSendSound();
					String userInputText = te.getText().toString();
					userInputMessage = userInputText;
					Message msg = new Message();
					msg.what = 1;
					myHandler.sendMessage(msg);
					// String result = "";
					break;
				case R.id.buttonChangeMode:
					if (speakButton.getVisibility() == View.VISIBLE) {
						speakButton.setVisibility(View.INVISIBLE);
						bts.setVisibility(View.INVISIBLE);
						te.setVisibility(View.VISIBLE);
						sendBtn.setVisibility(View.VISIBLE);
						
						ChangeMode.setBackgroundResource(R.drawable.textbutton);

					} else {
						speakButton.setVisibility(View.VISIBLE);
						bts.setVisibility(View.VISIBLE);
						te.setVisibility(View.INVISIBLE);
						sendBtn.setVisibility(View.INVISIBLE);
						ChangeMode
								.setBackgroundResource(R.drawable.speechbutton);

					}

					break;
				case R.id.buttonSpeak:
					playSendSound();
					Intent intent = new Intent(
							RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
					intent.putExtra("calling_package", "VoiceIME");
					intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
					sr.startListening(intent);

					break;

				}
			} else if (v instanceof ImageButton) {
				ImageButton btn = (ImageButton) v;
				switch (btn.getId()) {
				case R.id.imageButtonbt:
					if (bts.isEnabled()) {
						if (!bluetoothheadset) {
							amanager = (AudioManager) getSystemService("audio");
							amanager.startBluetoothSco();
							amanager.setMode(2);
							// amanager.setBluetoothA2dpOn(true);
							amanager.setBluetoothScoOn(true);
							bts.setImageResource(R.drawable.bton_sm);
							notifyBluetooth(true);
							bluetoothheadset = true;
						} else {
							amanager = (AudioManager) getSystemService("audio");
							amanager.setBluetoothScoOn(false);
							amanager.stopBluetoothSco();
							amanager.setMode(0);
							// amanager.setBluetoothA2dpOn(true);
							bts.setImageResource(R.drawable.btoff_sm);
							notifyBluetooth(false);
							bluetoothheadset = false;
						}

					}
					break;
				}
			}

		}

	};

	public void notifyBluetooth(boolean state) {
		if (state) {
			Toast.makeText(this, "Bluetooth Handset On.", Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(this, "Bluetooth Handset Off.", Toast.LENGTH_SHORT)
					.show();
		}

	}
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// 释放TTS的资源
		unregisterReceiver(scanSdReceiver);
		unregisterReceiver(bluetoothReceiver);
		if (sMediaPlayer != null) {
			sMediaPlayer.release();
			sMediaPlayer = null;
		}
		if (rMediaPlayer != null) {
			rMediaPlayer.release();
			rMediaPlayer = null;
		}
		if (mTts != null) {
			mTts.shutdown();

		}
		sr.destroy();

	}

	private void playSendSound() {
		if (!sMediaPlayer.isPlaying()) {
			sMediaPlayer.start();
		} else {
			sMediaPlayer.stop();
		}
	}

	private void playReceiveSound() {
		if (!rMediaPlayer.isPlaying()) {
			rMediaPlayer.start();
		} else {
			rMediaPlayer.stop();
		}
	}

	public double getLatitude() {
		String Location = getLocation();
		String locations[] = Location.split(" ");
		double lati = Double.parseDouble(locations[0]);
		Log.v("lati", "" + lati);
		return lati;

	}

	public double getLongtitude() {
		String Location = getLocation();
		String locations[] = Location.split(" ");
		double longi = Double.parseDouble(locations[1]);
		Log.v("longi", "" + longi);
		return longi;

	}

	public String getLocation() {
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		criteria.setCostAllowed(false);
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		String providerName = lm.getBestProvider(criteria, true);

		if (providerName != null) {
			Location location = lm.getLastKnownLocation(providerName);
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			return latitude + " " + longitude;
		} else {
			return "Get Location fail, please check your network";

		}

	}

	private boolean isNameFound(String name, List<Person> pList) {
		for (int i = 0; i < pList.size(); i++) {
			if (pList.get(i).getName().toLowerCase()
					.contains(name.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	private String getTele(String name, List<Person> pList) {
		for (int i = 0; i < pList.size(); i++) {
			Person p = pList.get(i);
			if (p.getName().toLowerCase().contains(name.toLowerCase())) {
				if (p.getPhone().size() != 0) {
					return p.getPhone().get(0);
				}

			}
		}

		return "0";
	}

	private String getMailAddress(String name, List<Person> pList) {
		for (int i = 0; i < pList.size(); i++) {
			Person p = pList.get(i);
			if (p.getName().toLowerCase().contains(name.toLowerCase())) {
				return p.getEmail();
			}
		}

		return "0";
	}

	public int nameToId(String name, String titles[], int ids[]) {
		int result = -1;
		System.out.println("name:" + name.toLowerCase() + "|");
		for (int i = 0; i < titles.length; i++) {

			System.out.println("title:" + titles[i].toLowerCase() + "| ID:"
					+ ids[i]);
			if (titles[i].toLowerCase().contains(name.toLowerCase())) {
				System.out.print("Found!");
				result = i;
			}
		}
		return result;
	}

	public String idToName(int id, String titles[]) {
		String result = "null";
		result = titles[id];
		return result;
	}

	public void onInit(int status) {
		// TODO Auto-generated method stub
		if (status == TextToSpeech.SUCCESS) {
			int result = mTts.setLanguage(Locale.US);
			// 设置发音语言
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED)
			// 判断语言是否可用
			{
				Log.v("", "Language is not available");
				// speakButton.setEnabled(false);
			} else {
				// mTts.speak("Hello world!", TextToSpeech.QUEUE_ADD, null);
				// speakButton.setEnabled(true);
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println("the result is " + requestCode);
		if (requestCode == 0) {
			switch (resultCode) {
			case TextToSpeech.Engine.CHECK_VOICE_DATA_PASS:
			// 这个返回结果表明TTS Engine可以用
			{
				mTts = new TextToSpeech(this, this);
				Log.v("", "TTS Engine is installed!");

			}

				break;
			case TextToSpeech.Engine.CHECK_VOICE_DATA_BAD_DATA:
				// 需要的语音数据已损坏
			case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_DATA:
				// 缺少需要语言的语音数据
			case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_VOLUME:
			// 缺少需要语言的发音数据
			{
				// 这三种情况都表明数据有错,重新下载安装需要的数据
				Log.v("", "Need language stuff:" + resultCode);
				Intent dataIntent = new Intent();
				dataIntent
						.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(dataIntent);

			}
				break;
			case TextToSpeech.Engine.CHECK_VOICE_DATA_FAIL:
				// 检查失败
			default:
				Log.v("", "Got a failure. TTS apparently not available");
				break;
			}
		} else if (requestCode == 1) {
			Log.v("enter camera result", "true");
			if (data != null) {

				Bundle b = data.getExtras();
				String path = b.getString("path");
				Log.v("image path", path);
				DetailImage di = new DetailImage(path, R.layout.imagedetail,
						this);
				list.add(di);
				persendInfo("he", "The photo has been saved into gallery.....");
				talkView.setAdapter(new DetailAdapter(MainActivity.this, list));
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void persendInfo(String name, String statement) {
		DetailEntity d1 = null;
		if (name.equals("me")) {
			d1 = new DetailText(name, statement, R.layout.list_say_me_item);
		} else {
			d1 = new DetailText(name, statement, R.layout.list_say_he_item);
			mTts.speak(statement, TextToSpeech.QUEUE_ADD, null);
		}
		list.add(d1);
		// talkView.setAdapter(new DetailAdapter(MainActivity.this, list));
	}

	public void sendWSMessage(String message) {

		String info = "";
		if (chat == true) {
			info += "1|";
		} else {
			info += "0|";
		}

		if (!info.equals("")) {
			Log.v("Click", "Button!!");
			info += message;
			persendInfo("me", getSecondInfo(info));

			String response = ws.getResponse(info);

			playReceiveSound();
			DetailWeather dw = null;
			WeatherWeek ww = null;

			switch (getCategory(response)) {
			default:
				speakUnknowMessage();
				break;
				
			case 0:
				int state = Integer.parseInt(getSecondInfo(response));
				switch (state) {
				case 1:
					chat = true;
					persendInfo("he", getThirdInfo(response));
					break;
				case 0:
					chat = false;
					persendInfo("he", DefaultResponse.getChatFinishSpeak());
					break;
				}
				break;
				
			case 1:// robot
				switch (Integer.parseInt(getSecondInfo(response))) {
				case 1:
					chat = true;
					persendInfo("he", DefaultResponse.getChatSuccessSpeak());
					break;
				case 0:
					chat = false;
					persendInfo("he",DefaultResponse.getChatFinishSpeak());
					break;
				}
				break;
			case 2:// maps
				switch (Integer.parseInt(getSecondInfo(response))) {
				case 1:// home
					persendInfo("he", "Locating your position.");
					Intent intentMap = new Intent();
					intentMap.putExtra("style", "home");
					intentMap.putExtra("Fromlatitude", getLatitude() / 1000000.0
							+ "");
					intentMap.putExtra("Fromlongtitude", getLongtitude()
							/ 1000000.0 + "");

					intentMap.setClass(MainActivity.this, MapsActivity.class);
					startActivity(intentMap);

					break;

				case 2:// navigation
					persendInfo("he", "Navigation to your destination.");
					String cityName = getThirdInfo(response);
					Intent intentNavigationMap = new Intent();
					geo = new Geo(cityName);
					Log.v("la", geo.get_latitude());
					Log.v("lo", geo.get_longtitude());
					intentNavigationMap.putExtra("style", "else");
					intentNavigationMap.putExtra("Fromlatitude", getLatitude() + "");
					intentNavigationMap.putExtra("Fromlongtitude", getLongtitude() + "");
					intentNavigationMap.putExtra("tolatitude", geo.get_latitude());
					intentNavigationMap.putExtra("tolongtitude", geo.get_longtitude());
					intentNavigationMap.putExtra("cityName", cityName);
					intentNavigationMap.setClass(MainActivity.this, NavigationActivity.class);
					startActivity(intentNavigationMap);
					break;
				}

				break;// 56.157047,13.760741
			case 3:// weather
				if (getThirdInfo(response).equals("0")) {// local
					gw = new YahooWeather(getLatitude(), getLongtitude());
				}
				if (getThirdInfo(response).equals("1")) {// other place
					String cityName = getFourthInfo(response);
					geo = new Geo(cityName);
					gw = new YahooWeather(Double.parseDouble(geo
							.get_latitude()), Double.parseDouble(geo
							.get_longtitude()));
				}
				switch (Integer.parseInt(getSecondInfo(response))) {
				default: // local today weather
						persendInfo("he",
								"Here's the forecast for recent days:");
						ww = gw.getWeatherWeek();
						DetailEntity d2 = ww;
						list.add(d2);
					break;
				case 1: // local weather for recent days
					
						persendInfo("he", "Here's the weather for today:");
						dw = gw.getWeatherDetail(String
								.valueOf(getSecondInfo(response)));
						DetailEntity d3 = dw;
						list.add(d3);
					break;
				}
				break;
			case 4:// WikiPedia
				String wikiWords = getSecondInfo(response);
				persendInfo("he", "Searching for wikipedia");

				Uri myBlogUri = Uri
						.parse("http://www.wikipedia.org/search-redirect.php?search="
								+ wikiWords
								+ "&language=en&go=++→ "
								+ "++&go=Go");
				Intent returnwikiIt = new Intent(Intent.ACTION_VIEW, myBlogUri);
				startActivity(returnwikiIt);
				break;

			case 5:// call
				personList = getContacts.getPerson(MainActivity.this);
				String callName = getName(response);
				if (isNameFound(callName, personList)) {
					String tele = getTele(callName, personList);
					if (!tele.equals("0")) {
						persendInfo("he", "Calling to " + callName + ".");
						Intent myIntentDial = new Intent(Intent.ACTION_CALL,
								Uri.parse("tel:" + tele));
						
						startActivity(myIntentDial);
					} else {
						persendInfo("he", "Sorry, no telephone exist!");
					}
				} else {
					persendInfo("he",
							"Sorry, no person is found by the given name!");
				}
				break;
			case 6:// send SMS
				String smsName = getSecondInfo(response);
				String smsContent = getThirdInfo(response);
				if (smsName != null) {
					personList = getContacts.getPerson(MainActivity.this);
					if (isNameFound(smsName, personList)) {
						String tele = getTele(smsName, personList);
						if (!tele.equals("0")) {
							persendInfo("he", "Sending message to " + smsName);
							Log.v("tele", tele);
							Intent smsreturnIt = new Intent();
							smsreturnIt.setAction(Intent.ACTION_SENDTO);
							smsreturnIt.setData(Uri.parse("smsto:" + tele));
							smsreturnIt.putExtra("sms_body", smsContent);
							startActivity(smsreturnIt);

						} else {
							persendInfo("he", "Sorry, no telephone exist!");

						}
					} else {
						persendInfo("he",
								"Sorry, no person is found by the given name!");
					}
				}
				break;
			case 7:// send Email
				personList = getContacts.getPerson(MainActivity.this);
				String mailReceiver = getSecondInfo(response);
				String mailBody = getThirdInfo(response);

				if (isNameFound(mailReceiver, personList)) {
					String mailAddress = getMailAddress(mailReceiver,
							personList);
					if (!mailAddress.equals("0")) {
						persendInfo("he", "Sending email to " + mailReceiver
								+ ".");
						Intent intent = new Intent(
								android.content.Intent.ACTION_SEND);
						intent.setType("plain/text");
						intent.putExtra(android.content.Intent.EXTRA_EMAIL,
								mailReceiver); 
						intent.putExtra(android.content.Intent.EXTRA_TEXT,
								mailBody); 
						startActivity(Intent.createChooser(intent,
								"Choose Email Client"));
					} else {
						persendInfo("he", "Sorry, no mail box exist!");
					}
				} else {
					persendInfo("he",
							"Sorry, no person is found by the given name!");
				}
				break;
			case 8:// google search
				String googlWords = getSecondInfo(response);
				persendInfo("he", "Searching for google.");
				Uri searchUri = Uri
						.parse("http://www.google.com/search?hl=en&site=&source=hp&q="
								+ googlWords + "&oq=" + googlWords);
				Intent returnIt = new Intent(Intent.ACTION_VIEW, searchUri);
				startActivity(returnIt);
				break;
			case 9:
				Calendar calendar = Calendar.getInstance();
				int hourOfDay = Integer.parseInt(getSecondInfo(response));
				int minute = Integer.parseInt(getThirdInfo(response));
				calendar.setTimeInMillis(System.currentTimeMillis());
				calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
				calendar.set(Calendar.MINUTE, minute);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				Intent intentAlarm = new Intent(MainActivity.this,
						AlarmReceiver.class);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(
						MainActivity.this, 0, intentAlarm, 0);
				AlarmManager am;
				am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
				am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
						pendingIntent);
				persendInfo("he", "Set up clock at " + hourOfDay + ":" + minute
						+ " .");

				break;
			case 10:
				int option = Integer.parseInt(getSecondInfo(response));
				switch (option) {
				case 1:// play song
					String songName = getThirdInfo(response);
					int songIds[] = scanSdReceiver.getIds();
					Intent intentMusic = new Intent();
					intentMusic.putExtra("_ids", songIds);

					int position = 0;
					if (!songName.equals("000")) {
						position = nameToId(songName,
								scanSdReceiver.getTitles(),
								scanSdReceiver.getIds());
					} else {
						Random r = new Random();
						position = r.nextInt(songIds.length);
						songName = idToName(position,
								scanSdReceiver.getTitles());
						persendInfo("he",
								DefaultResponse.playSongReponse(songName));
					}

					if (position != -1) {
						intentMusic.putExtra("position", position);
						intentMusic.putExtra("op", 1);
						intentMusic.setAction("com.android.MusicService");
						startService(intentMusic);
						persendInfo("he",
								"Playing song "+ songName+".");
						Log.v("Music", "Play song :" + songName
								+ ", position: " + position);
					} else {
						persendInfo("he",
								"Sorry, I can't find the song on your device.");
					}
					break;
				case 2:
					Intent intent1 = new Intent();
					intent1.setAction("com.android.MusicService");

					intent1.putExtra("op", 2);
					// while(mTts.isSpeaking()){}
					startService(intent1);
					Log.v("Music", "pause song");
					break;
				case 3:
					// unregisterReceiver(musicReceiver);
					Intent intent2 = new Intent();
					intent2.setAction("com.android.MusicService");
					intent2.putExtra("op", 3);
					// unregisterReceiver(scanSdReceiver);
					// while(mTts.isSpeaking()){}
					startService(intent2);
					Log.v("Music", "Stop song");
					break;
				}

				break;
			case 11:
				String command = getSecondInfo(response);
				Intent it = new Intent();

				if (command.equals("1")) {// Add
					persendInfo("he", "Adding your event.");
					String title = getThirdInfo(response);
					String content = getFourthInfo(response);
					it.putExtra("command", "add");
					it.putExtra("title", title);
					it.putExtra("content", content);
				} else if (command.equals("2")) { // View one
					it.putExtra("command", "viewOne");
				} else if (command.equals("3")) { // View one
					it.putExtra("command", "viewAll");
				} else if (command.equals("4")) { // View one
					it.putExtra("command", "deleteAll");
				}
				it.setClass(MainActivity.this, DateMemoActivity.class);
				startActivity(it);
				break;
			case 12:
				switch (Integer.parseInt(getSecondInfo(response))) {
				case 1:
					persendInfo("he", "Start the camera.");
					Intent in = new Intent();
					in.setClass(MainActivity.this, CameraActivity.class);
					startActivityForResult(in, 1);
					break;
				}

				break;
			case 13:
				Intent in = new Intent();
				in.setClass(MainActivity.this, HelpActivity.class);
				startActivity(in);
				break;
			case 14:
				String languagecode = getSecondInfo(response);
				String text = getThirdInfo(response);
				persendInfo("he", DefaultResponse.getTranslateSuccessSpeak());
				Translator ts = new Translator(text,languagecode);
				DetailTranslate dt = new DetailTranslate(languagecode,ts.getTranslateResult(), text,
						R.layout.translate);
				list.add(dt);
				
				break;
			}

			te.setText("");
			talkView.setAdapter(new DetailAdapter(MainActivity.this, list));
		}

	}

	public void speakUnknowMessage() {
		Random r = new Random();
		int index = r.nextInt(3);
		switch (index) {
		case 0:
			persendInfo("he", "Sorry, I don't know what you mean by “ "
					+ userInputMessage + " ”, if you want to "
					+ " chat please say “ Chat with me ”.");
			break;
		case 1:
			persendInfo("he", "Sorry, I don't understand “ " + userInputMessage
					+ " ”, maybe you can"
					+ " switch to text mode and tell me clearly.");
			break;
		case 2:
			persendInfo("he", "Sorry, may I beg your pardon?");
			break;

		}

	}

	class speechListener implements RecognitionListener {
		public void onReadyForSpeech(Bundle params) {
			// Toast.makeText(MyStt3Activity.this, "onReadyForSpeech",
			// Toast.LENGTH_SHORT).show();
			mTts.stop();
			speakButton.setEnabled(false);
			speakButton.setText("Listening...");
		}

		public void onBeginningOfSpeech() {
			// Toast.makeText(MyStt3Activity.this, "onBeginningOfSpeech",
			// Toast.LENGTH_SHORT).show();

		}

		public void onRmsChanged(float rmsdB) {
			// Toast.makeText(MyStt3Activity.this, "onRmsChanged",
			// Toast.LENGTH_SHORT).show();

		}

		public void onBufferReceived(byte[] buffer) {

		}

		public void onEndOfSpeech() {

			// Toast.makeText(MyStt3Activity.this, "onEndOfSpeech",
			// Toast.LENGTH_SHORT).show();
			speakButton.setText("Tap to Speak");
			speakButton.setEnabled(true);
		}

		public void onError(int error) {
			String s = "";
			switch (error) {
			case SpeechRecognizer.ERROR_AUDIO:
				s = "录音设别错误";
				break;
			case SpeechRecognizer.ERROR_CLIENT:
				s = "其他客户端错误";
				break;
			case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
				s = "权限不足";
				break;
			case SpeechRecognizer.ERROR_NETWORK:
				s = "网络连接错误";
				break;
			case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
				s = "网络连接超时";
				break;
			case SpeechRecognizer.ERROR_NO_MATCH:
				s = "没有匹配项";
				break;
			case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
				s = "识别服务繁忙";
				break;
			case SpeechRecognizer.ERROR_SERVER:
				s = "识别服务器错误";
				break;
			case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
				s = "无语音输入";
				break;
			}
			s += " 请重试";
			Log.v("Error", s);
			// mText.setText(s);

		}

		public void onResults(Bundle results) {

			String recognizer_result = results.getStringArrayList(
					SpeechRecognizer.RESULTS_RECOGNITION).get(0);
			// mText.setText( recognizer_result);
			userInputMessage = recognizer_result;
			Log.v("GetMessage", userInputMessage);
			Message msg = new Message();
			msg.what = 1;
			myHandler.sendMessage(msg);

		}

		public void onPartialResults(Bundle partialResults) {

		}

		public void onEvent(int eventType, Bundle params) {

		}
		   class InitialThread extends Thread{
		    	@Override
			     public void run()
			     {
		    		
		    		Message msg;
					msg = new Message();
					
					myHandler.sendMessage(msg);
		    		
					
			     }
		    }

	}



}