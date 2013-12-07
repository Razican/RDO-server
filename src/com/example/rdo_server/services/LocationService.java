package com.example.rdo_server.services;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.example.rdo_server.network.LocClient;

/**
 * @author Razican (Iban Eguia)
 */
public class LocationService {

	private static boolean	gpsActive	= false;
	private static String	locServIP	= "192.168.1.10";
	private static int		locServPort	= 1100;
	private static int		cell		= 1;
	private static String	username	= "admin";
	private static String	password	= "8cb2237d0679ca88db6464eac60da96345513964";	// pass:12345

	private LocationService()
	{
	}

	/**
	 * Gets the current location
	 * 
	 * @param c - The context of the application
	 * @return The location of the device
	 */
	public static synchronized Location getLocation(Context c)
	{
		Location loc = null;
		if (isGPSActive())
		{
			LocationManager locMan = (LocationManager) c
			.getSystemService(Context.LOCATION_SERVICE);
			Criteria locC = new Criteria();
			locC.setAccuracy(Criteria.ACCURACY_FINE);

			loc = locMan.getLastKnownLocation(locMan
			.getBestProvider(locC, true));
		}
		else
		{
			Log.d("GPS", "GPS not active, connecting to localization service");
			LocClient locClient = new LocClient(locServIP, locServPort,
			username, password);

			locClient.authenticate();
			loc = locClient.getLocation(cell);

			locClient.close();
		}

		return loc;
	}

	/**
	 * Checks if the GPS is active
	 * 
	 * @return If the GPS is active
	 */
	public static boolean isGPSActive()
	{
		return gpsActive;
	}

	/**
	 * Enables the GPS
	 */
	public static void enableGPS()
	{
		gpsActive = true;
	}

	/**
	 * Disables the GPS
	 */
	public static void disableGPS()
	{
		gpsActive = false;
	}

	/**
	 * @return The IP of the location server
	 */
	public static String getLocServIP()
	{
		return locServIP;
	}

	/**
	 * @return The port of the location server
	 */
	public static int getLocServPort()
	{
		return locServPort;
	}

	/**
	 * Sets the IP for the location server
	 * 
	 * @param ip - The IP of the location server
	 */
	public static void setLocServIP(String ip)
	{
		locServIP = ip;
	}

	/**
	 * Sets the port for the location server
	 * 
	 * @param port - The port of the location server
	 */
	public static void setLocServPort(int port)
	{
		locServPort = port;
	}

	/**
	 * Sets the new cell of the device
	 * 
	 * @param c - The new cell of the device
	 */
	public static void updateCell(int c)
	{
		cell = c;
	}

	/**
	 * Changes the username for the location server
	 * 
	 * @param u - The new username for the location server
	 */
	public static void setUsername(String u)
	{
		username = u;
	}

	/**
	 * Sets the password for the location server
	 * 
	 * @param p - The new password for the location server
	 */
	public static void setPassword(String p)
	{
		password = p;
	}
}