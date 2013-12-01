package com.example.rdo_server.utilities;

/**
 * @author Razican (Iban Eguia)
 */
public final class CommandAnalizer {

	private CommandAnalizer()
	{
	}

	/**
	 * Get the command string of the message
	 * 
	 * @param message - The message received by the server
	 * @return The command in the message
	 */
	public static String getCommand(String message)
	{
		if (message != null)
		{
			String[] array = message.split(" ");

			if (array.length >= 1)
			{
				return array[0];
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}

	/**
	 * Gets the parameter of the command
	 * 
	 * @param message - The message received by the server
	 * @return the parameter of the command
	 */
	public static String getParameter(String message)
	{
		String[] array = message.split(" ");

		if (array.length == 2)
		{
			return array[1];
		}
		else
		{
			return null;
		}
	}
}