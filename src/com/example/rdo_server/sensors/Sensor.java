package com.example.rdo_server.sensors;

import java.util.Vector;

import com.example.rdo_server.utilities.Database;

/**
 * @author Razican (Iban Eguia)
 */
public abstract class Sensor {

	private int					id;
	private String				description;
	private Vector<Measurement>	historic;
	private boolean				enabled;

	/**
	 * @param id - The ID of the sensor
	 * @param description - The description of the sensor
	 * @param enabled - If the sensor should be enabled
	 * @param historic - The historical measurements of the sensor
	 */
	public Sensor(int id, String description, boolean enabled,
	Vector<Measurement> historic)
	{
		this.id = id;
		this.description = description;
		this.enabled = enabled;
		this.historic = historic;
	}

	/**
	 * @return The name of the sensor
	 */
	public int getId()
	{
		return this.id;
	}

	/**
	 * @return The description of the sensor
	 */
	public String getDescription()
	{
		return this.description;
	}

	/**
	 * @return If the sensor is enabled
	 */
	public boolean isEnabled()
	{
		return this.enabled;
	}

	/**
	 * Gets the historic measurements for the sensor
	 * 
	 * @return The historic measurements
	 */
	public synchronized Vector<Measurement> getHistoric()
	{
		return historic;
	}

	/**
	 * Enables the sensor
	 */
	public void enable()
	{
		this.enabled = true;
	}

	/**
	 * Disables the sensor
	 */
	public void disable()
	{
		this.enabled = false;
	}

	/**
	 * Saves the given measurement to the database
	 * 
	 * @param m - The measurement to save
	 */
	public void saveMeasurement(Measurement m)
	{
		Database
		.getInstance()
		.getWritableDatabase()
		.execSQL(
		"INSERT INTO MEASUREMENT VALUES " + "("
		+ (m.getDate().getTime() / 1000) + "," + id + ","
		+ m.getLocation().getLatitude() + "," + m.getLocation().getLongitude()
		+ "," + m.getValue() + ");");

		historic.add(m);
	}

	/**
	 * Perform a measurement
	 * 
	 * @return The result of the measurement
	 */
	public abstract double measure();
}