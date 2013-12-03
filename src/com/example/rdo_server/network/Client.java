package com.example.rdo_server.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.UUID;

import android.util.Log;

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

				Log.d("PHOTO", "Last bytes: " + byteArray[byteArray.length - 2]
				+ " " + byteArray[byteArray.length - 2]);
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
	 * Sets the user for the client
	 * 
	 * @param user - The user for the client
	 */
	public void setUser(String user)
	{
		this.user = user;
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
		return user != null && user.equals("admin")
		&& password.equals("8cb2237d0679ca88db6464eac60da96345513964"); // pass:12345
	}

	@Override
	public String toString()
	{
		String c = "";
		if (user != null)
		{
			c += user + " @ ";
		}
		c += socket.getInetAddress().getHostAddress();
		c += " - ID: " + id.substring(0, 7);

		return c;
	}
}