package jk.haibian.qlianglink;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class CGameSQLiteHelper extends SQLiteOpenHelper
{
	private static final int DATABASE_VERSION = 1;
	private static final String SQL_PATH = "/data/data/jk.haibian.qlianglink/databases/";
	private String SQL_NAME ;
	private final Context myContext;

	public CGameSQLiteHelper(Context context, String name, CursorFactory factory, int version)
	{
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
		myContext = context;
		SQL_NAME = name;
		Log.d("qlianglink", "CGameSQLiteHelper()");
	}

//	private void prepareDatabase()
//	{
//		Log.d("qlianglink", "CGameSQLiteHelper.prepareDatabase()");
//		try
//		{
//			String databaseFile = SQL_PATH + "/" + SQL_NAME;
//			
//			File dir = new File(SQL_PATH);
//			if (!dir.exists())
//				dir.mkdir();
//
//			File dbFile = new File(databaseFile);
//			if ( !dbFile.exists() )
//			{
//				InputStream ins = myContext.getResources().openRawResource(R.raw.ql_link);
//				FileOutputStream fos = new FileOutputStream(databaseFile);
//				
//				byte[] buffer = new byte[1024];
//				int count = 0;
//				while ((count = ins.read(buffer)) > 0)
//				{
//					fos.write(buffer, 0, count);
//					Log.d("qlianglink", "fos.write(...)");
//				}
//
//				fos.close();
//				ins.close();
//			}
//		} catch (Exception e)
//		{
//			Log.e("qlianglink", "Copy database file error!");
//		}
//	}


	@Override
	public void onCreate(SQLiteDatabase db)
	{
		InputStream inStream = myContext.getResources().openRawResource(R.raw.link_db);
		InputStreamReader isReader = new InputStreamReader(inStream);
		BufferedReader bufReader = new BufferedReader(isReader);
		
		try {
			String sql = "";
			db.beginTransaction();
			while ((sql = bufReader.readLine()) != null) {
				db.execSQL(sql);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		Log.d("qlianglink", "CGameSQLiteHelper.onCreate()");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// TODO Auto-generated method stub		
	}

	@Override
	public void onOpen(SQLiteDatabase db)
	{
		// TODO Auto-generated method stub
		super.onOpen(db);
	}

}
