package com.example.rdo_server;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;

import com.example.rdo_server.services.CommService;
import com.example.rdo_server.services.SensorService;
import com.example.rdo_server.utilities.Database;

/**
 * @author Razican (Iban Eguia)
 */
public class MainActivity extends Activity {

	/**
	 * The action for the receiver
	 */
	public static final String	ACTION	= "TAKEPHOTO";
	private PhotoReceiver		receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		startService(new Intent(this, SensorService.class));
		// Log.d("IP", getIP());

		Database.init(this);

		IntentFilter filter = new IntentFilter(ACTION);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		registerReceiver(receiver = new PhotoReceiver(), filter);

		Intent comIntent = new Intent(this, CommService.class);
		comIntent.putExtra("action", "init");
		comIntent.putExtra("port", 1099); // TODO from UI/config
		startService(comIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private String getIP()
	{
		List<NetworkInterface> interfaces;
		try
		{
			interfaces = Collections.list(NetworkInterface
			.getNetworkInterfaces());

			for (NetworkInterface intf: interfaces)
			{
				List<InetAddress> addrs = Collections.list(intf
				.getInetAddresses());
				for (InetAddress addr: addrs)
				{
					if ( ! addr.isLoopbackAddress())
					{
						String sAddr = addr.getHostAddress();

						if (InetAddressUtils.isIPv4Address(sAddr))
						{
							return sAddr;
						}
					}
				}
			}
		}
		catch (SocketException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	// @Override
	// protected void onStop()
	// {
	// unregisterReceiver(receiver);
	// super.onStop();
	// }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == RESULT_OK)
		{
			Intent comIntent = new Intent(this, CommService.class);
			comIntent.putExtra("action", "photo");
			comIntent.putExtra("client", requestCode - 1);
			comIntent.putExtra("photo", true);
			comIntent.putExtra("data", (Bitmap) data.getExtras().get("data"));
			startService(comIntent);
		}
		else
		{
			Intent comIntent = new Intent(this, CommService.class);
			comIntent.putExtra("action", "photo");
			comIntent.putExtra("client", requestCode - 1);
			comIntent.putExtra("photo", false);
			startService(comIntent);
		}
	}

	private class PhotoReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent)
		{
			Intent act = new Intent(context, MainActivity.class);
			act.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
			| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(act);

			Intent takePictureIntent = new Intent(
			MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(takePictureIntent,
			intent.getIntExtra("client", - 1) + 1);
		}
	}
}