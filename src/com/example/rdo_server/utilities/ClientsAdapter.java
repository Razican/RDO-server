package com.example.rdo_server.utilities;

import java.util.Vector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rdo_server.R;
import com.example.rdo_server.network.Client;

/**
 * @author Jordan Aranda Tejada
 */
public class ClientsAdapter extends BaseAdapter {

	private final Context			context;
	private final Vector<Client>	clients;

	/**
	 * @param context The context of list.
	 * @param users The vector with all users
	 */
	public ClientsAdapter(final Context context, final Vector<Client> clients)
	{
		this.context = context;
		this.clients = clients;
	}

	@Override
	public int getCount()
	{
		return clients.size();
	}

	@Override
	public Object getItem(final int arg0)
	{
		return clients.get(arg0);
	}

	@Override
	public long getItemId(final int position)
	{
		return 0; // TODO client.get(position).getId();
	}

	@Override
	public View getView(final int position, final View convertView,
	final ViewGroup parent)
	{
		View v = convertView;

		if (convertView == null)
		{
			final LayoutInflater inf = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inf.inflate(R.layout.client_row, null);
		}

		// CLIENT USERNAME
		final TextView username = (TextView) v
		.findViewById(R.id.client_username_list);
		username.setText("Admin"); // TODO get client username from database

		// CLIENT IP
		final TextView ipClient = (TextView) v
		.findViewById(R.id.client_ip_address_list);
		ipClient.setText("127.0.0.1");

		return v;
	}
}