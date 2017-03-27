package edu.hui.vassistant.receivers;


import edu.hui.vassistant.MainActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

	
    public void onReceive(Context context, Intent intent) {  
    	System.out.println("start broadcast!");
        Toast.makeText(context, "Hey, times up!", Toast.LENGTH_LONG).show();  
        Message message = new Message();
        
        message.what = 3;
        
        MainActivity.myHandler.sendMessage(message);
        System.out.println("start message 3 to handler!");
    }
}
