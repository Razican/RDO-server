package com.example.rdo_server.sensors;

import java.util.Date;
import java.util.Random;
import java.util.Vector;

/**
 * @author Razican (Iban Eguia)
 */
public class BreathSensor extends Sensor {

	/**
	 * @param id - The ID of the sensor
	 * @param description - The description of the sensor
	 * @param enabled - If the sensor should be enabled
	 * @param historic - The historical measurements of the sensor
	 */
	public BreathSensor(int id, String description, boolean enabled,
	Vector<Measurement> historic)
	{
		super(id, description, enabled, historic);
	}

	@Override
	public double measure()
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
			return 5 + random.nextInt(50);
		}
		return 0;
	}
}