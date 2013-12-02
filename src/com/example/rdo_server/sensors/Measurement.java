package com.example.rdo_server.sensors;

import java.util.Date;

import android.location.Location;

/**
 * @author Razican (Iban Eguia)
 */
public class Measurement {

	private Date		date;
	private Location	location;
	private int			value;

	/**
	 * @param date - The date of the measurement
	 * @param location - The location of the measurement
	 * @param value - The value of the measurement
	 */
	public Measurement(Date date, Location location, int value)
	{
		this.date = date;
		this.location = location;
		this.value = value;
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
	public int getValue()
	{
		return value;
	}

}