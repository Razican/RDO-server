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
	 * @param enabled - If the sensor should be enabled
	 */
	public Pulsometer(String name, String description, String units,
	boolean enabled)
	{
		super(name, description, units, enabled);
		// TODO Load historic
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
	public synchronized int measure()
	{
		// TODO communicate with the real sensor
		if (super.isInState(ENABLED))
		{
			Random random = new Random(new Date().getTime());
			try
			{
				Thread.sleep(500 + random.nextInt(1000)); // Simulate processing
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			return 50 + random.nextInt(100);
		}
		return 0;
	}
}