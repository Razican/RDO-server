package com.example.rdo_server;

import razican.utils.StringUtils;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.rdo_server.utilities.Database;
import com.example.rdo_server.utilities.User;

/**
 * @author Jordan Aranda Tejada
 */
public class UserDataActivity extends Activity {

	private User		user;
	private EditText	usernameEditText;
	private EditText	passwordEditText;
	private Button		btnDelete;
	private Button		btnSave;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_data);

		usernameEditText = (EditText) findViewById(R.id.editText_user_name);
		passwordEditText = (EditText) findViewById(R.id.editText_password);

		this.user = (User) this.getIntent().getExtras().getSerializable("user");

		usernameEditText.setText(user.getName());

		btnDelete = (Button) findViewById(R.id.button_delete);
		btnDelete.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(final View view)
			{
				delete();
			}
		});
		btnSave = (Button) findViewById(R.id.button_save);
		btnSave.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(final View view)
			{
				save();
			}
		});
	}

	private void delete()
	{
		SQLiteDatabase db = Database.getInstance().getWritableDatabase();

		db.rawQuery("DELETE FROM USER WHERE id = " + user.getId() + ";", null);

		// TODO diálogo

		finish();
	}

	private void save()
	{
		if (usernameEditText.getText().toString().equals(""))
		{
			// TODO dialog
		}
		else
		{
			user.setName(usernameEditText.getText().toString());
		}

		SQLiteDatabase db = Database.getInstance().getWritableDatabase();

		db.rawQuery("UPDATE USER SET name = \"" + user.getName()
		+ "\" WHERE id = " + user.getId() + ";", null);

		if ( ! passwordEditText.getText().toString().equals(""))
		{
			String pass = StringUtils.sha1(passwordEditText.getText()
			.toString());

			db.rawQuery("UPDATE USER SET password = \"" + pass
			+ "\" WHERE id = " + user.getId() + ";", null);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_data, menu);
		return true;
	}
}