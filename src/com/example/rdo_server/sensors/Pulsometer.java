package com.example.rdo_server.sensors;

import java.util.Date;
import java.util.Random;

/**
 * @author Razican (Iban Eguia)
 */
public class Pulsometer extends Sensor {

	/**
	 * @param name - The name for the sensor
	 * @param description - The description of the sensor
	 * @param units - The units for the sensor
	 * @param state - The state of the sensor
	 */
	public Pulsometer(String name, String description, String units, int state)
	{
		super(name, description, units, state);
	}

	@Override
	public double measure()
	{
		Random random = new Random(new Date().getTime());
		return 50 + random.nextInt(100);
	}
}