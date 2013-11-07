package jk.haibian.qlianglink;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;

public class CLevelSelect extends Activity
{
	private GridView grid;
	private CGameData gameData;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
										  WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_clevel_select);
		Log.d("CLevelSelect","onCreate");
		
		grid = (GridView)findViewById(R.id.gridView2);
		
		gameData = CGameData.getInstance(null);
		
		intiGridView();
	}

	private void intiGridView()
	{
		List<SLevelInfo> levels = gameData.getLevelList();
		
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		Log.d("CLevelSelect","onResume");
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		Log.d("CLevelSelect","onPause");
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		Log.d("CLevelSelect","onDestroy");
	}


}
