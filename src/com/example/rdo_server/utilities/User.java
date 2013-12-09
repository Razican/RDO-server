package com.example.rdo_server.utilities;

/**
 * @author Razican (Iban Eguia)
 */
public class User {

	private int		id;
	private String	name;
	private boolean	online;
	private String	ip;

	/**
	 * Creates a user
	 * 
	 * @param id - The ID of the user
	 * @param name - The name of the user (if logged in)
	 * @param online - If the user is online
	 * @param ip - The IP of the user (if online)
	 */
	public User(int id, String name, boolean online, String ip)
	{
		this.id = id;
		this.name = name;
		this.online = online;
		this.ip = ip;
	}

	/**
	 * @return The ID of the user
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @return The name of the user
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return If the user is online
	 */
	public boolean isOnline()
	{
		return online;
	}

	/**
	 * @return The IP of the user
	 */
	public String getIp()
	{
		return ip;
	}
}