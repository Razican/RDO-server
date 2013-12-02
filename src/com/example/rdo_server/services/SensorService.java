package com.example.rdo_server.services;

import java.util.Vector;

import com.example.rdo_server.sensors.Measurement;
import com.example.rdo_server.sensors.Pulsometer;
import com.example.rdo_server.sensors.Sensor;

/**
 * @author Razican (Iban Eguia)
 */
public class SensorService {

	private static SensorService	instance	= new SensorService();
	private Vector<Sensor>			sensors;

	/**
	 * Creates a SensorService
	 */
	private SensorService()
	{
		sensors = new Vector<Sensor>();
		Sensor pulsometer = new Pulsometer(1, "Pulsómetro", true);
		sensors.add(pulsometer);

		// TODO init sensors from DB
	}

	private Sensor getSensor(int sensor)
	{
		return sensors.get(sensor);
	}

	/**
	 * Disables a sensor
	 * 
	 * @param sensor - The sensor to disable
	 */
	public synchronized static void disable(int sensor)
	{
		instance.getSensor(sensor).disable();
	}

	/**
	 * Enables a sensor
	 * 
	 * @param sensor - The sensor to enable
	 */
	public synchronized static void enable(int sensor)
	{
		instance.getSensor(sensor).enable();
	}

	/**
	 * Check if the sensor is enabled
	 * 
	 * @param sensor - the sensor to check
	 * @return If it is enabled
	 */
	public synchronized static boolean isEnabled(int sensor)
	{
		return instance.getSensor(sensor).isEnabled();
	}

	/**
	 * Measures a sensor
	 * 
	 * @param sensor - The sensor to measure
	 * @return The measurement of the sensor
	 */
	public synchronized static int measure(int sensor)
	{
		return instance.getSensor(sensor).measure();
	}

	/**
	 * Checks a sensor
	 * 
	 * @param sensor - The sensor to check
	 * @return The state of the sensor
	 */
	public synchronized static Vector<Measurement> getHistoric(int sensor)
	{
		return instance.getSensor(sensor).getHistoric();
	}

	/**
	 * Gets the list of sensors
	 * 
	 * @return The vector containing the sensors
	 */
	public static synchronized Vector<Sensor> listSensors()
	{
		return instance.sensors;
	}
}