package com.example.rdo_server.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.Vector;

import android.content.Intent;
import android.util.Log;

import com.example.rdo_server.services.CommService;
import com.example.rdo_server.utilities.CommandAnalizer;

/**
 * @author Razican (Iban Eguia)
 */
public class Server {

	private ServerSocket	server;
	private CommService		service;
	private Vector<Client>	clients;

	/**
	 * Creates an instance for the server.
	 * 
	 * @param port - The port for the server
	 * @param service - The communication service launching the server
	 * @throws IOException - If there is an exception when creating the socket
	 */
	public Server(int port, CommService service) throws IOException
	{
		this.server = new ServerSocket(port);
		this.service = service;
		this.clients = new Vector<Client>();

		Log.d("Server", "Server listening on port " + port);

		acceptNewClient();
	}

	/**
	 * Get the client at a given index
	 * 
	 * @param index - The index of the client
	 * @return The client at that index
	 */
	public Client getClient(int index)
	{
		return clients.get(index);
	}

	private void acceptNewClient()
	{
		Log.d("Server", "Waiting for a new client");
		(new Thread()
		{

			@Override
			public void run()
			{
				try
				{
					Client c = new Client(server.accept());
					clients.add(c);

					Log.d("Server", "Client " + c + " accepted");
					Log.d("Server", clients.size() + " client(s) in server");

					acceptNewClient();

					String line = null;
					int index = clients.indexOf(c);

					while ((line = c.read()) != null
					&& ! CommandAnalizer.getCommand(line).equals("SALIR")
					&& c.isOpen())
					{
						Intent intent = new Intent(service, CommService.class);
						intent.putExtra("action", "command");
						intent.putExtra("line", line);
						intent.putExtra("client", index);

						service.startService(intent);
					}

					if (line != null)
					{
						c.write("318 OK Adiós.");
					}

					c.close();
					clients.remove(c);

					Log.d("Server", "Client " + c + " closed");
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * Closes the server
	 * 
	 * @throws IOException - If the socket fails to close
	 */
	public void close() throws IOException
	{
		Iterator<Client> i = clients.iterator();

		while (i.hasNext())
		{
			try
			{
				Client c = i.next();
				c.close();
				clients.remove(c);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		server.close();
	}
}