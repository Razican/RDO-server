package com.example.rdo_server.sensors;

import java.util.Date;

import android.location.Location;

/**
 * @author Razican (Iban Eguia)
 */
public class Measurement {

	private Date		date;
	private Location	location;
	private double		value;

	/**
	 * @param date - The date of the measurement
	 * @param location - The location of the measurement
	 * @param m - The value of the measurement
	 */
	public Measurement(Date date, Location location, double m)
	{
		this.date = date;
		this.location = location;
		this.value = m;
	}

	/**
	 * @return The date of the measurement
	 */
	public Date getDate()
	{
		return date;
	}

	/**
	 * @return The location of the measurement
	 */
	public Location getLocation()
	{
		return location;
	}

	/**
	 * @return The value of the measurement
	 */
	public double getValue()
	{
		return value;
	}

}