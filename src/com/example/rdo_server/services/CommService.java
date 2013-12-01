package com.example.rdo_server.services;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import com.example.rdo_server.utilities.Client;
import com.example.rdo_server.utilities.CommandAnalizer;
import com.example.rdo_server.utilities.Server;

/**
 * @author Razican (Iban Eguia)
 */
public class CommService extends IntentService {

	private static Server	server;
	private static boolean	gpsActive;

	/**
	 * Creates a Communication Service
	 */
	public CommService()
	{
		super("CommService");
	}

	private void doCommand(String l, Client c)
	{
		String command = CommandAnalizer.getCommand(l);

		if (command.equals("USUARIO"))
		{
			String user = CommandAnalizer.getParameter(l);
			if (user != null)
			{
				c.setUser(user);
				c.write("301 OK Bienvenido " + user + ".");
			}
			else
			{
				c.write("501 ERR Falta el nombre de usuario.");
			}
		}
		else if (command.equals("CLAVE"))
		{
			String password = CommandAnalizer.getParameter(l);

			if (password != null)
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
		else if (command.equals("LISTSENSOR"))
		{
			// TODO get the list of sensors
		}
		else if (command.equals("HISTORICO"))
		{
			// TODO get historic data
		}
		else if (command.equals("ON"))
		{
			String i = CommandAnalizer.getParameter(l);
			int index = - 1;

			if (i != null
			&& (index = Integer.parseInt(i) - 1) < SensorService.listSensors()
			.size() && index >= 0)
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
			&& (index = Integer.parseInt(i) - 1) < SensorService.listSensors()
			.size() && index >= 0)
			{
				if ( ! SensorService.isEnabled(index))
				{
					c.write("314 OK Sensor desactivado.");
				}
				else
				{
					SensorService.disable(index);
					c.write("313 OK Sensor activo.");
				}
			}
			else
			{
				c.write("527 ERR Sensor no existe.");
			}
		}
		else if (command.equals("ONGPS"))
		{
			if ( ! gpsActive)
			{
				gpsActive = true;
				c.write("315 OK GPS activado.");
			}
			else
			{
				c.write("529 ERR GPS en estado ON.");
			}
		}
		else if (command.equals("OFFGPS"))
		{
			if (gpsActive)
			{
				gpsActive = false;
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
			&& (index = Integer.parseInt(i) - 1) < SensorService.listSensors()
			.size() && index >= 0)
			{
				if (SensorService.isEnabled(index))
				{
					// 224 OK 22/10/09;11:23:12; 4°41'24.14"-74°02'46.86;24
					String response = "224 OK ";
					SimpleDateFormat df = new SimpleDateFormat(
					"dd/mm/yy;hh:mm:ss", Locale.getDefault());
					response += df.format(new Date());
					response += ";;"; // TODO coordenadas
					response += SensorService.measure(index);

					c.write(response);
					// XXX Y si no hay coordenadas?
				}
				else
				{
					c.write("526 ERR Sensor en OFF.");
				}
			}
			else if (i == null)
			{
				c.write("525 ERR Falta parámetro id_sensor.");
			}
			else
			{
				c.write("524 ERR Sensor desconocido.");
			}
		}
		else if (command.equals("GET_FOTO"))
		{
			LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			if (gpsActive
			&& locMan.isProviderEnabled(LocationManager.GPS_PROVIDER))
			{
				// TODO sacar foto y enviar
				c.setPhoto(true);
			}
			else
			{
				c.write("530 ERR GPS en estado OFF.");
			}
		}
		else if (command.equals("GET_LOC"))
		{
			LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			if (c.gotPhoto())
			{
				Criteria locC = new Criteria();
				locC.setAccuracy(Criteria.ACCURACY_FINE);

				Location loc = locMan.getLastKnownLocation(locMan
				.getBestProvider(locC, true));

				String response = "124 OK ";
				response += Location.convert(loc.getLatitude(),
				Location.FORMAT_SECONDS);
				response += Location.convert(loc.getLongitude(),
				Location.FORMAT_SECONDS);

				c.write(response);
			}
			else
			{
				// XXX Y qué pasa aquí?
			}
		}

		if ( ! command.equals("GET_FOTO"))
		{
			c.setPhoto(false);
		}
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		String action = intent.getStringExtra("action");

		if (action != null && action.equals("init"))
		{
			int port = intent.getIntExtra("port", 0);

			if (port != 0)
			{
				try
				{
					server = new Server(port, this);
					gpsActive = ((LocationManager) getSystemService(Context.LOCATION_SERVICE))
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		else if (action != null && action.equals("command"))
		{
			int index = intent.getIntExtra("client", - 1);
			if (index >= 0)
			{
				Client c = server.getClient(index);
				String l = intent.getStringExtra("line");

				doCommand(l, c);
			}
		}
	}
}