package com.example.rdo_server.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.Vector;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.rdo_server.services.CommService;
import com.example.rdo_server.utilities.CommandAnalizer;
import com.example.rdo_server.utilities.Database;
import com.example.rdo_server.utilities.User;

/**
 * @author Razican (Iban Eguia)
 */
public class Server {

	private ServerSocket	server;
	private CommService		service;
	private Vector<Client>	clients;
	private int				maxConnections;

	/**
	 * Creates an instance for the server.
	 * 
	 * @param port - The port for the server
	 * @param maxConnections - The maximum number of connections
	 * @param service - The communication service launching the server
	 * @throws IOException - If there is an exception when creating the socket
	 */
	public Server(int port, int maxConnections, CommService service)
	throws IOException
	{
		this.maxConnections = maxConnections;
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
					while ( ! canAcceptMore())
					{
						;
					}
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
						c.write("318 OK Adios.");
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
	 * @return If the server can accept more connections
	 */
	protected boolean canAcceptMore()
	{
		return maxConnections > clients.size();
	}

	/**
	 * Sets the maximum number of connections
	 * 
	 * @param maxConn - The new number of maximum connections
	 */
	public void setMaxConn(int maxConn)
	{
		maxConnections = maxConn;
	}

	/**
	 * Get users
	 * 
	 * @return A vector with all users
	 */
	public Vector<User> getUsers()
	{
		Vector<User> v = new Vector<User>();

		SQLiteDatabase db = Database.getInstance().getReadableDatabase();

		Cursor c = db.rawQuery("SELECT * FROM USER", null);
		c.moveToFirst();

		for (int i = 0; i < c.getCount(); i++)
		{
			String username = c.getString(1);
			boolean online = checkOnline(username);

			v.add(new User(c.getInt(0), c.getString(1), online,
			online ? getIp(username) : null));

			c.moveToNext();
		}

		for (Client cl: clients)
		{
			if (cl.getUser() == null)
			{
				v.add(new User(0, null, true, cl.getIP()));
			}
		}

		return v;
	}

	private boolean checkOnline(String username)
	{
		for (Client c: clients)
		{
			if (c.getUser() != null && c.getUser().equals(username))
			{
				return true;
			}
		}

		return false;
	}

	private String getIp(String username)
	{
		for (Client c: clients)
		{
			if (c.getUser() != null && c.getUser().equals(username))
			{
				return c.getIP();
			}
		}

		return null;
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