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
	public void enable()
	{
		// TODO communicate with the real sensor
		super.setState(ENABLED);
	}

	@Override
	public void disable()
	{
		// TODO communicate with the real sensor
		super.setState(DISABLED);
	}

	@Override
	public double measure()
	{
		// TODO communicate with the real sensor
		if (super.isInState(ENABLED))
		{
			Random random = new Random(new Date().getTime());
			return 50 + random.nextInt(100);
		}
		return 0;
	}
}