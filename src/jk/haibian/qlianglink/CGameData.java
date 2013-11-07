package jk.haibian.qlianglink;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

@SuppressLint("DefaultLocale")
public class CGameData
{
	private static CGameData gameData = null;

	private String userName;
	private int beanNum = 0;
	private float partialBean = 0.f;

	private List<SLevelInfo> levelInfos = new ArrayList<SLevelInfo>();
	private CGameSQLiteHelper sqlHelper;
	private Context appContext;

	public static CGameData getInstance(Context context)
	{
		if (gameData == null) {
			gameData = new CGameData(context);
		}
		return gameData;
	}

	protected CGameData(Context context)
	{
		sqlHelper = new CGameSQLiteHelper(context, "ql_link.db3", null, 1);
		// sqlHelper.prepareDatabase(); //第一次启动时准备数据库
		appContext = context;
	}

	public boolean loadData()
	{
		loadUser();
		loadLevel();

		return true;
	}

	private void loadUser()
	{
//		TelephonyManager phoneM = (TelephonyManager)appContext.getSystemService(Context.TELEPHONY_SERVICE);
//		String id = phoneM.getDeviceId();
//		if (id == null || id == "") {
//			id = "abcde12345";
//		}

		String id = "abcde12345";
		SQLiteDatabase sqlite = sqlHelper.getReadableDatabase();
		Cursor cursor = sqlite.rawQuery("Select * From t_user Where name=?", new String[]{ id });
		if (cursor.moveToFirst()) {
			userName = cursor.getString(1);
			beanNum = cursor.getInt(2);

			cursor.close();
			sqlite.close();
		}
		else {
			Log.d("qlianglink", "user name: " + id);
			userName = id;
			beanNum = 100;

			SQLiteDatabase sqliteW = sqlHelper.getWritableDatabase();
			String sql = String.format("Insert Into t_user (name, beans) Values('%s', %d)", id, beanNum);
			sqliteW.execSQL(sql);
			sqliteW.close();
		}
	}

	private void loadLevel()
	{
		SQLiteDatabase sqlite = sqlHelper.getReadableDatabase();
		Cursor cursor = sqlite.rawQuery("select * from t_level ", null);
		while (cursor.moveToNext()) {
			SLevelInfo level = new SLevelInfo();
			level.levelId = cursor.getInt(0);
			level.enabled = cursor.getInt(1);
			level.passed = cursor.getInt(2);
			level.unlockCost = cursor.getInt(3);
			level.maxTime = cursor.getInt(4);
			level.score = cursor.getInt(5);
			level.imageNum = cursor.getInt(6);
			level.rowNum = cursor.getInt(7);
			level.colNum = cursor.getInt(8);
			level.iconSet = cursor.getString(9);
			levelInfos.add(level);
		}

		cursor.close();
		sqlite.close();
	}

	public boolean saveData()
	{
		SQLiteDatabase sqlite = sqlHelper.getWritableDatabase();

		for (int i = 0; i < levelInfos.size(); i++) {
			SLevelInfo level = levelInfos.get(i);
			String sql = String.format("Update t_level Set enabled=%d, passed=%d, score=%d Where levelId=%d",
												level.enabled, level.passed, level.score, level.levelId);
			sqlite.execSQL(sql);
		}

		String sql = String.format("Update t_user Set beans=%d Where name='%s'", beanNum, userName);
		sqlite.execSQL(sql);

		sqlite.close();
		return true;
	}

	public SLevelInfo getLevelInfo(int id)
	{
		for (int i = 0; i < levelInfos.size(); i++) {
			SLevelInfo level = levelInfos.get(i);
			if (level.levelId == id) {
				return level;
			}
		}
		return null;
	}

	public List<SLevelInfo> getLevelList()
	{
		return levelInfos;
	}

	public int getBeanNum()
	{
		return beanNum;
	}

	public void minusBean(int num)
	{
		if (this.beanNum >= num) {
			beanNum -= num;
		}
	}

	public void addBean(int num)
	{
		beanNum += num;
	}

	public void addPartialBean(float pbean)
	{
		partialBean += pbean;
		if (partialBean >= 1.0f) {
			beanNum++;
			partialBean = 0f;
		}
	}

	public void setEnable(int levelId)
	{
		SLevelInfo level = getLevelInfo(levelId);
		if (level != null) {
			level.enabled = 1;
		}
	}
	
	public void setPassed(int levelId)
	{
		SLevelInfo level = getLevelInfo(levelId);
		if (level != null) {
			level.passed = 1;
		}
	}
}
