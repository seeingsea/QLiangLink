package jk.haibian.qlianglink;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.adsmogo.adview.AdsMogoLayout;

public class StartPage extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
										  WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_start_page);
		Log.d("qlianglink","onCreate");
				
		CGameData gameData = CGameData.getInstance(getApplicationContext());
		gameData.loadData();
	}

	public void onBtnCasul(View v)
	{
		Intent intent = new Intent(this, CShemeSelect.class);
		startActivity(intent);
	}
	
	public void onBtnQuests(View v)
	{
		Toast.makeText(this, "下一版本开放。\r\n更多精彩敬请期待！", Toast.LENGTH_SHORT).show();
//		Intent intent = new Intent(this, CLevelSelect.class);
//		startActivity(intent);
	}
	
	public void onBtnMe(View v)
	{
		Intent intent = new Intent(this, CUserPage.class);
		startActivity(intent);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		Log.d("StartPage","onResume");
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		Log.d("StartPage","onPause");
	}

	@Override
	protected void onDestroy()
	{
		Log.d("StartPage","onDestroy");
		CGameData gameData = CGameData.getInstance(null);
		gameData.saveData();
		
		AdsMogoLayout.clear();
		super.onDestroy();
	}
	
}
