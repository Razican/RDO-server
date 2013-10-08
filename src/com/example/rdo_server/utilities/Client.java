package com.example.rdo_server.utilities;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @author Razican (Iban Eguia)
 */
public class Client {

	private Socket				socket;
	private DataOutputStream	output;
	private BufferedReader		input;

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
			return this.input.readLine();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * @param line - The line to write to the client
	 * @throws IOException - If it was unable to send the message to the client
	 */
	public void write(String line) throws IOException
	{
		this.output.writeBytes(line);
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
	}
}