package com.example.rdo_server.sensors;

/**
 * @author Razican (Iban Eguia)
 */
public abstract class Sensor {

	/**
	 * The sensor is enabled
	 */
	public static final int	ENABLED		= 1;
	/**
	 * The sensor is enabled
	 */
	public static final int	DISABLED	= 2;
	/**
	 * The sensor is performing a measurement
	 */
	public static final int	MEASURING	= 4;
	/**
	 * The sensor is not responding
	 */
	public static final int	WAITING		= 8;
	/**
	 * The sensor is not available
	 */
	public static final int	UNAVAILABLE	= 16;

	private String			name;
	private String			description;
	private String			units;
	private int				state;

	/**
	 * @param name - The name for the sensor
	 * @param description - The description of the sensor
	 * @param units - The units for the sensor
	 * @param state - The state of the sensor
	 */
	public Sensor(String name, String description, String units, int state)
	{
		this.name = name;
		this.description = description;
		this.units = units;
		this.state = state;
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
	 * Perform a measurement
	 * 
	 * @return The result of the measurement
	 */
	public abstract double measure();

	/**
	 * Enables the sensor
	 */
	public abstract void enable();

	/**
	 * Disables the sensor
	 */
	public abstract void disable();
}