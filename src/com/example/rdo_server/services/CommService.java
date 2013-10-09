package com.example.rdo_server.services;

import java.io.IOException;

import android.app.IntentService;
import android.content.Intent;

import com.example.rdo_server.utilities.Client;
import com.example.rdo_server.utilities.CommandAnalizer;
import com.example.rdo_server.utilities.Server;

/**
 * @author Razican (Iban Eguia)
 */
public class CommService extends IntentService {

	private Server	server;
	private boolean	gpsActive;
	private boolean	photo;

	/**
	 * Creates a Communication Service
	 */
	public CommService()
	{
		super("CommService");
		photo = false;
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
			// TODO
		}
		else if (command.equals("HISTORICO"))
		{
			// TODO
		}
		else if (command.equals("ON"))
		{
			// TODO
		}
		else if (command.equals("OFF"))
		{
			// TODO
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
			// TODO
		}
		else if (command.equals("GET_FOTO"))
		{
			if (gpsActive)
			{
				// TODO sacar foto y enviar
				photo = true;
			}
			else
			{
				c.write("530 ERR GPS en estado OFF.");
			}
		}
		else if (command.equals("GET_LOC"))
		{
			if (photo)
			{
				// TODO
			}
			else
			{
				// TODO
			}
		}

		if ( ! command.equals("GET_FOTO"))
		{
			photo = false;
		}
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		String action = intent.getStringExtra("action");

		if (action.equals("init"))
		{
			int port = intent.getIntExtra("port", 0);

			if (port != 0)
			{
				try
				{
					server = new Server(port, this);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		else if (action.equals("command"))
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