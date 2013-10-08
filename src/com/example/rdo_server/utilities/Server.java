package com.example.rdo_server.utilities;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author Razican (Iban Eguia)
 */
public class Server {

	private ServerSocket	server;
	private Vector<Client>	clients;

	/**
	 * Creates an instance for the server.
	 * 
	 * @param port - The port for the server
	 * @throws IOException - If there is an exception when creating the socket
	 */
	public Server(int port)
	{
		try
		{
			server = new ServerSocket(port);
			acceptNewClient();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
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
					clients.add(new Client(server.accept()));
					System.out.println("Cliente aceptado");
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
				i.next().close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		server.close();
	}

	/**
	 * @param args Application arguments
	 */
	public static void main(String[] args)
	{
		Server server = new Server(3000);
	}
}