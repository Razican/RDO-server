package com.example.rdo_server;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.example.rdo_server.services.CommService;
import com.example.rdo_server.services.SensorService;

/**
 * @author Razican (Iban Eguia)
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		startService(new Intent(this, SensorService.class));
		Log.d("IP", getIP());

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
}