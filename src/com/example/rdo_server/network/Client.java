package com.example.rdo_server.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.UUID;

import android.database.Cursor;
import android.util.Log;

import com.example.rdo_server.utilities.Database;
import com.example.rdo_server.utilities.exceptions.NonExistentUserException;

/**
 * @author Razican (Iban Eguia)
 */
public class Client {

	private Socket				socket;
	private DataOutputStream	output;
	private BufferedReader		input;
	private String				user;
	private boolean				photo;
	private String				id;
	private boolean				open;
	private boolean				isAuthenticated;

	/**
	 * Creates a new client
	 * 
	 * @param socket - The socket for the client
	 * @throws IOException - If there is an exception with the buffers
	 */
	public Client(Socket socket) throws IOException
	{
		this.socket = socket;
		this.output = new DataOutputStream(socket.getOutputStream());
		this.input = new BufferedReader(new InputStreamReader(
		socket.getInputStream()));
		this.photo = false;
		this.id = UUID.randomUUID().toString();
		this.open = true;
		this.isAuthenticated = false;
	}

	/**
	 * Reads a string from the client
	 * 
	 * @return The string sent by the client
	 */
	public String read()
	{
		try
		{
			String l = input.readLine();
			if (l != null)
			{
				Log.d("Command", l);
			}
			return l;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param line - The line to write to the client
	 * @throws IOException - If it was unable to send the message to the client
	 */
	public void write(String line)
	{
		try
		{
			output.writeBytes(line + "\n");
			Log.d("Response", line);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Sends the photo to the client
	 * 
	 * @param byteArray - The byte array to send
	 */
	public void sendPhoto(byte[] byteArray)
	{
		write("PHOTOLEN " + (byteArray != null ? byteArray.length : 0));
		if (byteArray != null)
		{
			try
			{
				Log.d("PHOTO", "Sending photo of length " + byteArray.length
				+ " to client " + this);

				socket.getOutputStream().write(byteArray);
				socket.getOutputStream().flush();

				Log.d("PHOTO", "Photo sent");
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Closes the connection with the client
	 * 
	 * @throws IOException - If there is an exception when closing the
	 *             connection
	 */
	public void close() throws IOException
	{
		this.output.close();
		this.input.close();
		this.socket.close();

		this.open = false;
	}

	/**
	 * @return If the connection is open
	 */
	public boolean isOpen()
	{
		return open;
	}

	/**
	 * @return If the user is authenticated
	 */
	public boolean isAuthenticated()
	{
		return isAuthenticated;
	}

	/**
	 * Sets the user for the client
	 * 
	 * @param user - The user for the client
	 * @throws NonExistentUserException If the user does not exist
	 */
	public void setUser(String user) throws NonExistentUserException
	{
		if (existsUser(user))
		{
			this.user = user;
		}
		else
		{
			throw new NonExistentUserException();
		}
	}

	private boolean existsUser(String user)
	{
		Cursor c = Database
		.getInstance()
		.getReadableDatabase()
		.rawQuery("SELECT COUNT(*) FROM USER WHERE name = \"" + user + "\";",
		null);

		c.moveToFirst();
		return c.getInt(0) == 1;
	}

	/**
	 * @return The user of the client
	 */
	public Object getUser()
	{
		return user;
	}

	/**
	 * @return The IP of the client
	 */
	public String getIP()
	{
		return socket.getInetAddress().getHostAddress();
	}

	/**
	 * Sets the user for the client
	 * 
	 * @param photo - If the photo has been sent
	 */
	public void setPhoto(boolean photo)
	{
		this.photo = photo;
	}

	/**
	 * @return If the user has got a photo, for sending location
	 */
	public boolean gotPhoto()
	{
		return photo;
	}

	/**
	 * Checks the password of the user
	 * 
	 * @param password - The password of the user, SHA-1 encoded
	 * @return If the password is correct
	 */
	public boolean checkPassword(String password)
	{
		Cursor c = Database
		.getInstance()
		.getReadableDatabase()
		.rawQuery("SELECT password FROM USER WHERE name = \"" + user + "\"",
		null);
		c.moveToFirst();

		return isAuthenticated = password.equals(c.getString(0));
	}

	@Override
	public String toString()
	{
		String c = "";
		if (user != null)
		{
			c += user + " @ ";
		}
		c += getIP();
		c += " - ID: " + id.substring(0, 7);

		return c;
	}
}