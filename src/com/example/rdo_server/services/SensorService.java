package com.example.rdo_server.services;

import java.util.Date;
import java.util.Vector;

import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;

import com.example.rdo_server.sensors.BreathSensor;
import com.example.rdo_server.sensors.GlucoseMeter;
import com.example.rdo_server.sensors.Measurement;
import com.example.rdo_server.sensors.Pulsometer;
import com.example.rdo_server.sensors.Sensor;
import com.example.rdo_server.sensors.Thermometer;
import com.example.rdo_server.utilities.Database;

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

		Cursor s = Database.getInstance().getReadableDatabase()
		.rawQuery("SELECT * FROM SENSOR", null);

		s.moveToFirst();
		for (int i = 0; i < s.getCount(); i++)
		{
			sensors.add(createSensor(s));
			s.moveToNext();
		}
	}

	private Sensor createSensor(Cursor s)
	{
		Cursor m = Database
		.getInstance()
		.getReadableDatabase()
		.rawQuery(
		"SELECT * FROM MEASUREMENT WHERE sensor_id = " + s.getInt(0) + ";",
		null);

		Vector<Measurement> measurements = new Vector<Measurement>();

		m.moveToFirst();
		for (int i = 0; i < m.getCount(); i++)
		{
			Location loc = new Location(LocationManager.GPS_PROVIDER);
			loc.setLatitude(m.getDouble(2));
			loc.setLongitude(m.getDouble(3));

			measurements.add(new Measurement(new Date(m.getInt(0) * 1000), loc,
			m.getDouble(4)));
			m.moveToNext();
		}

		if (s.getString(1).equals("Pulsometro"))
		{
			return new Pulsometer(s.getInt(0), s.getString(1),
			s.getInt(2) == 1, measurements);
		}
		else if (s.getString(1).equals("Termometro"))
		{
			return new Thermometer(s.getInt(0), s.getString(1),
			s.getInt(2) == 1, measurements);
		}
		else if (s.getString(1).equals("Medidor de respiracion"))
		{
			return new BreathSensor(s.getInt(0), s.getString(1),
			s.getInt(2) == 1, measurements);
		}
		else if (s.getString(1).equals("Medidor de glucosa"))
		{
			return new GlucoseMeter(s.getInt(0), s.getString(1),
			s.getInt(2) == 1, measurements);
		}

		return null;
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
	public synchronized static double measure(int sensor)
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

	/**
	 * Saves the given measurement
	 * 
	 * @param sensor - The sensor in which to save the measurement
	 * @param m - The measurement to save
	 */
	public static void saveMeasurement(int sensor, Measurement m)
	{
		instance.getSensor(sensor).saveMeasurement(m);
	}
}