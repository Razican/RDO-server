package com.example.rdo_server.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

/**
 * @author Razican (Iban Eguia)
 */
public class LocClient {

	private Socket				socket;
	private DataOutputStream	output;
	private BufferedReader		input;

	private String				username;
	private String				password;

	/**
	 * Creates a client for the location server
	 * 
	 * @param locServIP - The IP of the location server
	 * @param locServPort - The port of the location server
	 * @param user - The user for the location server
	 * @param pass - The password for the location server
	 */
	public LocClient(String locServIP, int locServPort, String user, String pass)
	{
		try
		{
			socket = new Socket(locServIP, locServPort);
			username = user;
			password = pass;

			output = new DataOutputStream(socket.getOutputStream());
			output.flush();
			input = new BufferedReader(new InputStreamReader(
			socket.getInputStream()));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Authenticates the patient in the location server
	 */
	public void authenticate()
	{
		try
		{
			output.writeBytes("USER " + username + "\n");
			Log.d("Location Message", "USER " + username);

			String response = input.readLine();
			Log.d("Location Response", response);

			if (response.substring(0, 3).equals("311"))
			{
				output.writeBytes("PASS " + password + "\n");
				Log.d("Location Server", "PASS " + password);

				response = input.readLine();
				Log.d("Location Message", response);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Closes the connection with the location server
	 */
	public synchronized void close()
	{
		try
		{
			output.writeBytes("SALIR\n");
			Log.d("Location Message", "SALIR");

			String response = input.readLine();
			Log.d("Location Response", response);

			output.close();
			input.close();
			socket.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Gets the current location from the location server
	 * 
	 * @param cell - The cell to check for
	 * @return The location of the device
	 */
	public synchronized Location getLocation(int cell)
	{
		Location loc = null;
		try
		{
			output.writeBytes("GETCOOR " + cell + "\n");
			Log.d("Location Message", "GETCOOR " + cell);

			String response = input.readLine();
			Log.d("Location Response", response);

			if (response.substring(0, 3).equals("224"))
			{
				String[] coords = response.split(" ")[2].split(";");

				loc = getLocation(coords);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return loc;
	}

	private Location getLocation(String[] coords)
	{
		String[] lat = coords[0].split(":");
		String[] lon = coords[1].split(":");

		double latitude, longitude;
		boolean latNeg = (latitude = Double.parseDouble(lat[0])) < 0, lonNeg = (longitude = Double
		.parseDouble(lon[0])) < 0;

		latitude = Math.abs(latitude) + Double.parseDouble(lat[1]) / 60
		+ Double.parseDouble(lat[2]) / 3600;
		longitude = Math.abs(longitude) + Double.parseDouble(lon[1]) / 60
		+ Double.parseDouble(lon[2]) / 3600;

		latitude *= latNeg ? - 1 : 1;
		longitude *= lonNeg ? - 1 : 1;

		Location loc = new Location(LocationManager.NETWORK_PROVIDER);
		loc.setLatitude(latitude);
		loc.setLongitude(longitude);

		return loc;
	}
}