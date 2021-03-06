package com.example.rdo_server.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;

import com.example.rdo_server.MainActivity;
import com.example.rdo_server.network.Client;
import com.example.rdo_server.network.Server;
import com.example.rdo_server.sensors.Measurement;
import com.example.rdo_server.sensors.Sensor;
import com.example.rdo_server.utilities.CommandAnalizer;
import com.example.rdo_server.utilities.User;
import com.example.rdo_server.utilities.exceptions.NonExistentUserException;

/**
 * @author Razican (Iban Eguia)
 */
public class CommService extends IntentService {

	private static Server	server;

	/**
	 * Creates a Communication Service
	 */
	public CommService()
	{
		super("CommService");
	}

	private void doCommand(String l, int cIndex)
	{
		String command = CommandAnalizer.getCommand(l);
		Client c = server.getClient(cIndex);

		if (command.equals("USUARIO"))
		{
			String user = CommandAnalizer.getParameter(l);
			if (user != null)
			{
				try
				{
					c.setUser(user);
					c.write("301 OK Bienvenido " + user + ".");
				}
				catch (NonExistentUserException e)
				{
					c.write("504 ERR El usuario no existe");
				}
			}
			else
			{
				c.write("501 ERR Falta el nombre de usuario.");
			}
		}
		else if (command.equals("CLAVE") && c.getUser() != null)
		{
			String password = CommandAnalizer.getParameter(l);

			if (password != null
			&& ! password.equals("da39a3ee5e6b4b0d3255bfef95601890afd80709"))
			{
				if (c.checkPassword(password))
				{
					c.write("302 OK Bienvenido al sistema.");
				}
				else
				{
					c.write("502 ERR La clave es incorrecta.");
				}
			}
			else
			{
				c.write("503 ERR Falta la clave.");
			}
		}
		else if (c.isAuthenticated())
		{
			if (command.equals("LISTSENSOR"))
			{
				c.write("222 OK Lista de sensores.");

				for (Sensor s: SensorService.listSensors())
				{
					String response = String.format(Locale.getDefault(),
					"%02d", s.getId());
					response += ";" + s.getDescription() + ";";
					response += s.isEnabled() ? "ON" : "OFF";
					c.write(response);
				}

				c.write("322 OK Lista finalizada.");
			}
			else if (command.equals("HISTORICO"))
			{
				String i = CommandAnalizer.getParameter(l);
				int index = - 1;

				if (i != null
				&& (index = Integer.parseInt(i) - 1) < SensorService
				.listSensors().size() && index >= 0)
				{
					c.write("223 OK Lista de medidas.");

					for (Measurement m: SensorService.getHistoric(index))
					{
						SimpleDateFormat df = new SimpleDateFormat(
						"dd/MM/yy;HH:mm:ss", Locale.getDefault());

						String response = df.format(m.getDate()) + ";";

						Location loc = m.getLocation();

						response += Location.convert(loc.getLatitude(),
						Location.FORMAT_SECONDS) + ";";
						response += Location.convert(loc.getLongitude(),
						Location.FORMAT_SECONDS);

						response += ";" + m.getValue();

						c.write(response);
					}

					c.write("322 OK Lista finalizada.");
				}
				else if (i != null)
				{
					c.write("524 ERR Sensor desconocido.");
				}
				else
				{
					c.write("525 ERR Falta parametro id_sensor.");
				}
			}
			else if (command.equals("ON"))
			{
				String i = CommandAnalizer.getParameter(l);
				int index = - 1;

				if (i != null
				&& (index = Integer.parseInt(i) - 1) < SensorService
				.listSensors().size() && index >= 0)
				{
					if (SensorService.isEnabled(index))
					{
						c.write("528 ERR Sensor en estado ON.");
					}
					else
					{
						SensorService.enable(index);
						c.write("313 OK Sensor activo.");
					}
				}
				else
				{
					c.write("527 ERR Sensor no existe.");
				}
			}
			else if (command.equals("OFF"))
			{
				String i = CommandAnalizer.getParameter(l);
				int index = - 1;

				if (i != null
				&& (index = Integer.parseInt(i) - 1) < SensorService
				.listSensors().size() && index >= 0)
				{
					if ( ! SensorService.isEnabled(index))
					{
						c.write("529 ERR Sensor en estado OFF.");
					}
					else
					{
						SensorService.disable(index);
						c.write("314 OK Sensor desactivado.");
					}
				}
				else
				{
					c.write("527 ERR Sensor no existe.");
				}
			}
			else if (command.equals("ONGPS"))
			{
				if ( ! LocationService.isGPSActive())
				{
					LocationService.enableGPS();
					c.write("315 OK GPS activado.");
				}
				else
				{
					c.write("529 ERR GPS en estado ON.");
				}
			}
			else if (command.equals("OFFGPS"))
			{
				if (LocationService.isGPSActive())
				{
					LocationService.disableGPS();
					c.write("316 OK GPS desactivado.");
				}
				else
				{
					c.write("530 ERR GPS en estado OFF.");
				}
			}
			else if (command.equals("GET_VALACT"))
			{
				String i = CommandAnalizer.getParameter(l);
				int index = - 1;

				if (i != null
				&& (index = Integer.parseInt(i) - 1) < SensorService
				.listSensors().size() && index >= 0)
				{
					if (SensorService.isEnabled(index))
					{
						double m = SensorService.measure(index);
						Location loc = LocationService.getLocation(this);
						Date d = new Date();

						String response = "224 OK ";
						SimpleDateFormat df = new SimpleDateFormat(
						"dd/MM/yy;HH:mm:ss", Locale.getDefault());
						response += df.format(d) + ";";

						response += Location.convert(loc.getLatitude(),
						Location.FORMAT_SECONDS) + ";";
						response += Location.convert(loc.getLongitude(),
						Location.FORMAT_SECONDS);

						response += ";" + m;

						c.write(response);

						SensorService.saveMeasurement(index, new Measurement(d,
						loc, m));
					}
					else
					{
						c.write("526 ERR Sensor en OFF.");
					}
				}
				else if (i == null)
				{
					c.write("525 ERR Falta parametro id_sensor.");
				}
				else
				{
					c.write("524 ERR Sensor desconocido.");
				}
			}
			else if (command.equals("GET_FOTO"))
			{
				Intent broadcastIntent = new Intent();
				broadcastIntent.setAction(MainActivity.ACTION);
				broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
				broadcastIntent.putExtra("client", cIndex);
				sendBroadcast(broadcastIntent);
			}
			else if (command.equals("GET_LOC") && c.gotPhoto())
			{
				Location loc = LocationService.getLocation(this);

				String response = "124 OK ";
				response += Location.convert(loc.getLatitude(),
				Location.FORMAT_SECONDS) + ";";
				response += Location.convert(loc.getLongitude(),
				Location.FORMAT_SECONDS);

				c.write(response);
			}
		}

		if ( ! command.equals("GET_FOTO"))
		{
			c.setPhoto(false);
		}
	}

	/**
	 * Get clients
	 * 
	 * @return A vector with all users
	 */
	public static Vector<User> getUsers()
	{
		if (server != null)
		{
			return server.getUsers();
		}
		return null;
	}

	/**
	 * @return The number of online users
	 */
	public static int onlineUsers()
	{
		return server != null ? server.onlineUsers() : 0;
	}

	/**
	 * @return The maximum number of users
	 */
	public static int getMaxUsers()
	{
		return server.getMaxConnections();
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		String action = intent.getStringExtra("action");

		if (action != null && action.equals("init"))
		{
			int port = intent.getIntExtra("port", 0);
			int maxConn = intent.getIntExtra("maxConn", 0);

			if (port != 0 && maxConn != 0)
			{
				try
				{
					server = new Server(port, maxConn, this);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		else if (action != null && action.equals("update"))
		{
			int maxConn = intent.getIntExtra("maxConn", 0);

			if (maxConn != 0)
			{
				server.setMaxConn(maxConn);
			}
		}
		else if (action != null && action.equals("command"))
		{
			int index = intent.getIntExtra("client", - 1);
			if (index >= 0)
			{
				String l = intent.getStringExtra("line");

				doCommand(l, index);
			}
		}
		else if (action != null && action.equals("photo"))
		{
			int index = intent.getIntExtra("client", - 1);
			boolean photo = intent.getBooleanExtra("photo", false);
			if (index >= 0 && photo)
			{
				Client c = server.getClient(index);
				c.setPhoto(true);

				Bitmap bmp = (Bitmap) intent.getExtras().get("data");
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
				stream.write( - 1);
				byte[] byteArray = stream.toByteArray();

				c.sendPhoto(byteArray);
			}
			else if (index >= 0)
			{
				Client c = server.getClient(index);
				c.setPhoto(false);
				c.sendPhoto(null);
			}
		}
	}
}