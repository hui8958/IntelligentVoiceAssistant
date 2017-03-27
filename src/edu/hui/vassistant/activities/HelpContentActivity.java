package edu.hui.vassistant.activities;

import java.util.ArrayList;

import edu.hui.vassistant.R;

import edu.hui.vassistant.entities.DetailAdapter;
import edu.hui.vassistant.entities.DetailEntity;
import edu.hui.vassistant.entities.DetailText;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class HelpContentActivity extends Activity {
	Intent intent;
	TextView tv = null;
	ImageView iv = null;
	private ListView View;
	private ArrayList<DetailEntity> list = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.helpcontent);
		tv = (TextView) findViewById(R.id.textView1);
		iv = (ImageView) findViewById(R.id.imageView1);
		View = (ListView) findViewById(R.id.listView1);
		list = new ArrayList<DetailEntity>();
		// View.setAdapter(new DetailAdapter(HelpContentActivity.this, list));
		intent = getIntent();
		String name = intent.getStringExtra("name");
		// String helpname = name+"help";
		tv.setText(name);
		initContent(name);

	}

	public void initContent(String name) {
		DetailEntity d1 = null;

		if (name.equals("weather")) {
			iv.setImageResource(R.drawable.weatherhelp);
			for (String a : createItems()[0]) {
				if ((a != null) && (!a.equals(""))) {
					d1 = new DetailText("me", a, R.layout.list_say_me_item);
					list.add(d1);
				}
			}

		} else if (name.equals("map")) {
			iv.setImageResource(R.drawable.maphelp);
			for (String a : createItems()[1]) {
				if ((a != null) && (!a.equals(""))) {
					d1 = new DetailText("me", a, R.layout.list_say_me_item);
					list.add(d1);
				}
			}
		} else if (name.equals("phone")) {
			iv.setImageResource(R.drawable.phonehelp);
			for (String a : createItems()[2]) {
				if ((a != null) && (!a.equals(""))) {
					d1 = new DetailText("me", a, R.layout.list_say_me_item);
					list.add(d1);
				}
			}
		} else if (name.equals("sms")) {
			iv.setImageResource(R.drawable.smshelp);
			for (String a : createItems()[3]) {
				if ((a != null) && (!a.equals(""))) {
					d1 = new DetailText("me", a, R.layout.list_say_me_item);
					list.add(d1);
				}
			}
		} else if (name.equals("email")) {
			iv.setImageResource(R.drawable.emailhelp);
			for (String a : createItems()[4]) {
				if ((a != null) && (!a.equals(""))) {
					d1 = new DetailText("me", a, R.layout.list_say_me_item);
					list.add(d1);
				}
			}
		} else if (name.equals("google")) {
			iv.setImageResource(R.drawable.googlehelp);
			for (String a : createItems()[5]) {
				if ((a != null) && (!a.equals(""))) {
					d1 = new DetailText("me", a, R.layout.list_say_me_item);
					list.add(d1);
				}
			}
		} else if (name.equals("wikipedia")) {
			iv.setImageResource(R.drawable.wikihelp);
			for (String a : createItems()[6]) {
				if ((a != null) && (!a.equals(""))) {
					d1 = new DetailText("me", a, R.layout.list_say_me_item);
					list.add(d1);
				}
			}
		} else if (name.equals("reminder")) {
			iv.setImageResource(R.drawable.reminderhelp);
			for (String a : createItems()[7]) {
				if ((a != null) && (!a.equals(""))) {
					d1 = new DetailText("me", a, R.layout.list_say_me_item);
					list.add(d1);
				}
			}
		} else if (name.equals("alarm")) {
			iv.setImageResource(R.drawable.alarmhelp);
			for (String a : createItems()[8]) {
				if ((a != null) && (!a.equals(""))) {
					d1 = new DetailText("me", a, R.layout.list_say_me_item);
					list.add(d1);
				}
			}
		} else if (name.equals("music")) {
			iv.setImageResource(R.drawable.musichelp);
			for (String a : createItems()[9]) {
				if ((a != null) && (!a.equals(""))) {
					d1 = new DetailText("me", a, R.layout.list_say_me_item);
					list.add(d1);
				}
			}
		} else if (name.equals("camera")) {
			iv.setImageResource(R.drawable.camerahelp);
			for (String a : createItems()[10]) {
				if ((a != null) && (!a.equals(""))) {
					d1 = new DetailText("me", a, R.layout.list_say_me_item);
					list.add(d1);
				}
			}
		} else if (name.equals("chat")) {
			iv.setImageResource(R.drawable.robothelp);
			for (String a : createItems()[11]) {
				if ((a != null) && (!a.equals(""))) {
					d1 = new DetailText("me", a, R.layout.list_say_me_item);
					list.add(d1);
				}
			}
		}else if (name.equals("translate")) {
			iv.setImageResource(R.drawable.translatehelp);
			for (String a : createItems()[12]) {
				if ((a != null) && (!a.equals(""))) {
					d1 = new DetailText("me", a, R.layout.list_say_me_item);
					list.add(d1);
				}
			}
		}
		
		View.setAdapter(new DetailAdapter(HelpContentActivity.this, list));
	}

	public String[][] createItems() {
		String[][] items = new String[13][10];
		for (int i = 0; i < 13; i++) {
			switch (i) {
			case 0:
				items[i][0] = "Weather today?";
				items[i][1] = "Tell me the weather tomorrow?";
				items[i][2] = "What's the weather next few days?";
				items[i][3] = "How is the weather in malmo today?";
				items[i][4] = "what's the termprature in stockholm tomorrow?";
				items[i][5] = "what's the climate in Gothenborg?";
				break;
			case 1:
				items[i][0] = "Where am I?";
				items[i][1] = "Show my current location.";
				items[i][2] = "How can I go to Lund?";
				items[i][3] = "Navigation to Kristianstad.";
				items[i][4] = "Go to Hassleholm.";
				break;
			case 2:
				items[i][0] = "Call Tom.";
				items[i][1] = "I want to give a call to Lucy.";
				items[i][2] = "Make a phone call to Lily.";
				items[i][3] = "Dial Song.";
				items[i][4] = "Please dial Andreas.";
				break;
			case 3:
				items[i][0] = "Send a message to LiLei Let's dinner together.";
				items[i][1] = "Message Tom go for basketball this afternoon.";
				items[i][2] = "SMS Hui Nihao.";
				items[i][3] = "Send a SMS to Hu	i hello world.";
				items[i][4] = "Text Linker I will get you out.";
				items[i][5] = "Send a text to Annie hui love you.";
				break;
			case 4:
				items[i][0] = "Mail Bellis it will rain today.";
				items[i][1] = "Send mail to Bellis I will cook kebab.";
				items[i][2] = "Email Bruce to go to Vinkins.";
				items[i][3] = "I want email Lee he is ugly.";
				items[i][4] = "Post Susan there will be a meeting at 9.";
				items[i][5] = "Post Mimy a boy is waiting for you.";
				break;
			case 5:
				items[i][0] = "Google China.";
				items[i][1] = "Try to google Java API.";
				items[i][2] = "Google Swedish learning process.";
				items[i][3] = "Search for Second World War.";
				break;
			case 6:
				items[i][0] = "Define Andriod.";
				items[i][1] = "Define true Love.";
				break;
			case 7:
				items[i][0] = "Set up a meeting at 10.";
				items[i][1] = "Make up a event that go fishing this Sunday.";
				items[i][2] = "Create an event to Copenhagen for shopping.";
				items[i][3] = "View all/one events.";
				items[i][4] = "Lookup all/one events.";
				items[i][5] = "Find event.";
				items[i][6] = "Delete all events.";
				items[i][7] = "Cancel events.";
				break;
			case 8:
				items[i][0] = "Set alarm to 10.";
				items[i][1] = "Set clock at 9:15.";
				items[i][2] = "Make time to 11:50.";
				break;
			case 9:
				items[i][0] = "Play a song for me.";
				items[i][1] = "I want to play a song.";
				items[i][2] = "Play Canon.";
				items[i][3] = "Pause playing music.";
				items[i][4] = "Stop music player.";
				items[i][4] = "Stop.";
				break;
			case 10:
				items[i][0] = "Open the camera.";
				items[i][1] = "Start the camera.";
				items[i][2] = "I want to use the camera.";
				break;
			case 11:
				items[i][0] = "Enable chat.";
				items[i][1] = "Let's chat.";
				items[i][2] = "Chat with me.";
				items[i][3] = "Finish/disable/end/complete chat.";
				break;
			case 12:
				items[i][0] = "Translate I love you in Chinese.";
				items[i][1] = "How to say Hello in Swedish";
				break;

			}

		}

		return items;

	}

}
