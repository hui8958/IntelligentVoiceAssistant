package edu.hui.vassistant.receivers;

import edu.hui.vassistant.MainActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.widget.Toast;

public class BluetoothReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();

		if (BluetoothDevice.ACTION_FOUND.equals(action)) {
			// Device found
		} else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
			// Device is now connected
		
			Toast.makeText(context, "Bluetooth Connected.", Toast.LENGTH_SHORT)
					.show();
			Message message = new Message();
			message.what = 4;

			MainActivity.myHandler.sendMessage(message);

		} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
			// Done searching
		} else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED
				.equals(action)) {
			// Device is about to disconnect
		} else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
			// Device has disconnected
			Toast.makeText(context, "Bluetooth Disonnected.",
					Toast.LENGTH_SHORT).show();
			Message message = new Message();
			message.what = 5;

			MainActivity.myHandler.sendMessage(message);

		}

	}

}
