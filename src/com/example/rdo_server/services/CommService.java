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
			// TODO
		}
		else if (command.equals("CLAVE"))
		{
			// TODO
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
			// TODO
		}
		else if (command.equals("OFFGPS"))
		{
			// TODO
		}
		else if (command.equals("GET_VALACT"))
		{
			// TODO
		}
		else if (command.equals("GET_FOTO"))
		{
			// TODO
		}
		else if (command.equals("GET_LOC"))
		{
			// TODO
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