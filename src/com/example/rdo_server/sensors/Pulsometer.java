package com.example.rdo_server.sensors;

import java.util.Date;
import java.util.Random;

/**
 * @author Razican (Iban Eguia)
 */
public class Pulsometer extends Sensor {

	/**
	 * @param id - The ID of the sensor
	 * @param description - The description of the sensor
	 * @param enabled - If the sensor should be enabled
	 */
	public Pulsometer(int id, String description, boolean enabled)
	{
		super(id, description, enabled);
		// TODO Load historic
	}

	@Override
	public synchronized int measure()
	{
		if (super.isEnabled())
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