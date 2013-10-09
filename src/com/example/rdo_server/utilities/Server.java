package com.example.rdo_server.utilities;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.Vector;

import android.content.Intent;

import com.example.rdo_server.services.CommService;

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
		server = new ServerSocket(port);
		this.service = service;

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
		(new Thread()
		{

			@Override
			public void run()
			{
				try
				{
					Client c = new Client(server.accept());
					clients.add(c);
					int index = clients.indexOf(c);

					String line = null;

					while ( ! CommandAnalizer.getCommand((line = c.read()))
					.equals("SALIR"))
					{
						Intent intent = new Intent(service, CommService.class);
						intent.putExtra("action", "command");
						intent.putExtra("line", line);
						intent.putExtra("client", index);

						service.startService(intent);
					}

					c.write("318 OK Adiós.");
					c.close();
					clients.remove(c);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * @return The iterator for the clients
	 */
	public Iterator<Client> getClientIterator()
	{
		return clients.iterator();
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