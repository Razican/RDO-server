package com.example.rdo_server.services;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

/**
 * @author Razican (Iban Eguia)
 */
public class LocationService {

	private LocationService()
	{
	}

	/**
	 * Gets the current location
	 * 
	 * @param c - The context of the application
	 * @return The location of the device
	 */
	public static Location getLocation(Context c)
	{
		LocationManager locMan = (LocationManager) c
		.getSystemService(Context.LOCATION_SERVICE);
		Criteria locC = new Criteria();
		locC.setAccuracy(Criteria.ACCURACY_FINE);
		// TODO contact with location server if gps not enabled

		return locMan.getLastKnownLocation(locMan.getBestProvider(locC, true));
	}
}