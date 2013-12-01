package com.example.rdo_server.services;

import java.util.Vector;

import android.app.IntentService;
import android.content.Intent;

import com.example.rdo_server.sensors.Sensor;

/**
 * @author Razican (Iban Eguia)
 */
public class SensorService extends IntentService {

	private Vector<Sensor>	sensors;

	/**
	 * Creates a SensorService
	 */
	public SensorService()
	{
		super("SensorService");
		// TODO init sensors
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		String action = intent.getStringExtra("action");
		int sensorInt = intent.getIntExtra("sensor", - 1);
		Sensor sensor;

		if (action != null && sensorInt >= 0
		&& (sensor = sensors.get(sensorInt)) != null)
		{
			if (action.equals("enable"))
			{
				sensor.enable();
			}
			else if (action.equals("disable"))
			{
				sensor.disable();
			}
			else if (action.equals("check"))
			{
				int state = sensor.getState();

				// TODO return data to receivers
			}
			else if (action.equals("measure"))
			{
				double measurement = sensor.measure();

				// TODO return data to receivers
				// XXX Timers
			}
		}
	}
}