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
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rdo_server.services.CommService;
import com.example.rdo_server.services.SensorService;
import com.example.rdo_server.utilities.Database;

/**
 * @author Razican (Iban Eguia)
 * @author Jordan Aranda Tejada
 */
public class MainActivity extends Activity {

	/**
	 * The action for the receiver
	 */
	public static final String	ACTION	= "TAKEPHOTO";
	private PhotoReceiver		receiver;
	private TextView			ipAddressTextView;
	private EditText			editTextPort;
	private EditText			editTextMaxUsers;
	private Button				btnUsers;
	private Button				btnUpdate;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// We get a reference to the interface controls
		ipAddressTextView = (TextView) findViewById(R.id.ip_textview);
		editTextPort = (EditText) findViewById(R.id.editText_port);
		editTextMaxUsers = (EditText) findViewById(R.id.editText_max_users);
		btnUsers = (Button) findViewById(R.id.button_users);
		btnUsers.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(final View view)
			{
				showUsers();
			}
		});
		btnUpdate = (Button) findViewById(R.id.button_update);
		btnUpdate.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(final View view)
			{
				update();
			}
		});

		startService(new Intent(this, SensorService.class));
		Log.d("IP", getIP());
		ipAddressTextView.setText(getIP());

		Database.init(this);

		IntentFilter filter = new IntentFilter(ACTION);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		registerReceiver(receiver = new PhotoReceiver(), filter);

		Intent comIntent = new Intent(this, CommService.class);
		comIntent.putExtra("action", "init");
		comIntent.putExtra("port", 1099); // TODO from UI/config
		editTextPort.setText("1099");
		editTextMaxUsers.setText("10"); // TODO Get from database
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

	/**
	 * Method pressing the users button
	 */
	public void showUsers()
	{
		final Intent intent = new Intent(MainActivity.this,
		ListUsersActivity.class);
		startActivity(intent);
	}

	/**
	 * Method pressing the update button
	 */
	public void update()
	{
		// TODO set database data
	}
}