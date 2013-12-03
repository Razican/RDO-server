package com.example.rdo_server.utilities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Xabier Ferrer Ardanaz
 * @author Razican (Iban Eguia)
 */
public class Database extends SQLiteOpenHelper {

	private static Database	instance;

	/**
	 * @param context - The context of the application
	 */
	private Database(final Context context)
	{
		super(context, "RDO-Server-DB", null, 1);
	}

	@Override
	public void onCreate(final SQLiteDatabase db)
	{
		db.execSQL("PRAGMA foreign_keys=ON;");
		create_tables(db);
		insert_data(db);
	}

	private void create_tables(final SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE CONFIG ("
		+ "\"key\" TEXT PRIMARY KEY NOT NULL, \"value\" TEXT);");

		db.execSQL("CREATE TABLE SENSOR ("
		+ "\"id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
		+ "\"description\" TEXT,"
		+ "\"enabled\" INTEGER CHECK (enabled BETWEEN 0 AND 1));");

		db
		.execSQL("CREATE TABLE MEASUREMENT ("
		+ "\"date\" INTEGER NOT NULL,"
		+ "\"sensor_id\" INTEGER NOT NULL REFERENCES SENSOR(id) ON DELETE CASCADE,"
		+ "\"latitude\" REAL NOT NULL, \"longitude\" REAL NOT NULL,"
		+ "\"value\" REAL NOT NULL, PRIMARY KEY (date, sensor_id));");
	}

	private void insert_data(SQLiteDatabase db)
	{
		db
		.execSQL("INSERT INTO CONFIG VALUES (\"patient_id\", \"Paciente 1\");");
		db
		.execSQL("INSERT INTO SENSOR (\"description\", \"enabled\") VALUES "
		+ "(\"Pulsometro\", 1), (\"Termometro\", 1),(\"Medidor de respiracion\", 1),(\"Medidor de glucosa\", 1);");
	}

	@Override
	public SQLiteDatabase getWritableDatabase()
	{
		final SQLiteDatabase db = super.getWritableDatabase();
		db.execSQL("PRAGMA foreign_keys=ON;");

		return db;
	}

	@Override
	public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
	final int newVersion)
	{
		// TODO upgrade
	}

	/**
	 * Initializes the database
	 * 
	 * @param context - The context of the application
	 */
	public static void init(Context context)
	{
		instance = new Database(context);
	}

	/**
	 * @return The database of the application
	 */
	public static Database getInstance()
	{
		return instance;
	}
}