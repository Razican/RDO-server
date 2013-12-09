package com.example.rdo_server.utilities;

import java.util.Vector;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rdo_server.R;

/**
 * @author Jordan Aranda Tejada
 */
public class UsersAdapter extends BaseAdapter {

	private final Context		context;
	private final Vector<User>	users;

	/**
	 * @param context The context of list.
	 * @param users The vector with all users
	 */
	public UsersAdapter(final Context context, final Vector<User> users)
	{
		this.context = context;
		this.users = users;
	}

	@Override
	public int getCount()
	{
		return users.size();
	}

	@Override
	public Object getItem(final int arg0)
	{
		return users.get(arg0);
	}

	@Override
	public long getItemId(final int position)
	{
		return users.get(position).getId();
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

		final TextView username = (TextView) v
		.findViewById(R.id.client_username_list);
		username.setText(users.get(position).getName());

		final TextView ipClient = (TextView) v
		.findViewById(R.id.client_ip_address_list);
		if (users.get(position).getIp() == null)
		{
			ipClient.setVisibility(View.INVISIBLE);
		}
		else
		{
			ipClient.setText(users.get(position).getIp());
			Log.d("USER_IP", users.get(position).getIp());
		}

		final ImageView iVUser = (ImageView) v
		.findViewById(R.id.connectedImage);
		iVUser.setImageResource(R.drawable.disconnected_icon);
		if (users.get(position).isOnline())
		{
			iVUser.setImageResource(R.drawable.coonected_icon);
		}

		return v;
	}
}