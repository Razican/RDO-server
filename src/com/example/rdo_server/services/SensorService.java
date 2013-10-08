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
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		// TODO Auto-generated method stub
	}

	private void checkSensors()
	{
		// TODO Auto-generated method stub
	}
}