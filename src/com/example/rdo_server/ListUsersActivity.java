package com.example.rdo_server;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Jordan Aranda Tejada
 */
public class ListUsersActivity extends Activity {

	private ListView	list;
	private TextView	usersConnectedTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_users);

		// We get a reference to the interface controls
		list = (ListView) findViewById(R.id.list_Users);
		usersConnectedTextView = (TextView) findViewById(R.id.text_view_users_connected);

		// final Vector<Client> clients = Server.getAllClients();
		//
		// final ClientsAdapter adapter = new ClientsAdapter(this, clients);

		// list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(final AdapterView<?> arg0, final View view,
			final int pos, final long arg3)
			{
				// TODO final Intent intent = new Intent(ListUsersActivity.this,
				// ClientDataActivity.class);
				// final Bundle b = new Bundle();
				// b.putInt("client_id", clients.get(pos).getId());
				// intent.putExtras(b);
				// startActivity(intent);
			}
		});

		// TODO Set connected users
		usersConnectedTextView.setText("Usuarios conectados: 8");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_users, menu);
		return true;
	}
}