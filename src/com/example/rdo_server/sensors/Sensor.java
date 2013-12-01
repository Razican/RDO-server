package com.example.rdo_server.sensors;

import java.util.Vector;

/**
 * @author Razican (Iban Eguia)
 */
public abstract class Sensor {

	/**
	 * The sensor is enabled
	 */
	public static final int		ENABLED		= 1;
	/**
	 * The sensor is enabled
	 */
	public static final int		DISABLED	= 2;

	private String				name;
	private String				description;
	private String				units;
	private int					state;
	private Vector<Measurement>	historic;

	/**
	 * @param name - The name for the sensor
	 * @param description - The description of the sensor
	 * @param units - The units for the sensor
	 * @param enabled - If the sensor should be enabled
	 */
	public Sensor(String name, String description, String units, boolean enabled)
	{
		this.name = name;
		this.description = description;
		this.units = units;
		this.state = enabled ? ENABLED : DISABLED;
		this.historic = new Vector<Measurement>();
	}

	/**
	 * @return The name of the sensor
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * @return The description of the sensor
	 */
	public String getDescription()
	{
		return this.description;
	}

	/**
	 * @return The units of the sensor
	 */
	public String getUnits()
	{
		return this.units;
	}

	/**
	 * @return The state of the sensor
	 */
	public int getState()
	{
		return this.state;
	}

	/**
	 * @param state - The state to check for
	 * @return If the sensor is in the specified state
	 */
	public boolean isInState(int state)
	{
		return (this.state & state) == state;
	}

	/**
	 * Changes the state of the sensor
	 * 
	 * @param state - The new state to set
	 */
	protected void setState(int state)
	{
		this.state = state;
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
	 * Perform a measurement
	 * 
	 * @return The result of the measurement
	 */
	public abstract int measure();

	/**
	 * Enables the sensor
	 */
	public abstract void enable();

	/**
	 * Disables the sensor
	 */
	public abstract void disable();
}