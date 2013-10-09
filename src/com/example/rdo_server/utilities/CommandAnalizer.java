package com.example.rdo_server.utilities;

/**
 * @author Razican (Iban Eguia)
 */
public final class CommandAnalizer {

	/**
	 * Get the command string of the message
	 * 
	 * @param message - The message received by the server
	 * @return The command in the message
	 */
	public static String getCommand(String message)
	{
		return message.split(" ")[0];
	}
}