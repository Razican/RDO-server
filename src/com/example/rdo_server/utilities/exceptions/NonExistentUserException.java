package com.example.rdo_server.utilities.exceptions;

/**
 * @author Razican (Iban Eguia)
 */
public class NonExistentUserException extends Exception {

	private static final long	serialVersionUID	= - 5887796751086789367L;

	/**
	 * Creates a NonExistentUserException
	 */
	public NonExistentUserException()
	{
		super("The given user does not exist");
	}
}