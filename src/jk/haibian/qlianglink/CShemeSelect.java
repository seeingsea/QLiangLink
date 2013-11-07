package jk.haibian.qlianglink;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.adsmogo.adview.AdsMogoLayout;

public class CShemeSelect extends Activity implements OnItemClickListener
{
//	private AdsMogoLayout adsMogoLayout;
	private GridView grid;
	private TextView txtBean;

	int[] imageIds = new int[]{ R.drawable.snow_mountain, R.drawable.island, R.drawable.sands3,
			R.drawable.boat, R.drawable.swimmingpool, R.drawable.bathroom };

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
											WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_csheme_select);
		Log.d("CShemeSelect", "onCreate");
//		adsMogoLayout = (AdsMogoLayout)findViewById(R.id.adsMogoView);
		txtBean = (TextView)findViewById(R.id.txtBean);

		initGrid();
	}

	protected void initGrid()
	{
		// 初始化列表数据
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < imageIds.length; i++)
		{
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("image", imageIds[i]);
			listItems.add(item);
		}
		// 创建Adapter
		SimpleAdapter simAdapter = new SimpleAdapter(this, listItems, R.layout.grid_cell,
																	new String[]{ "image" }, new int[]{ R.id.imageView1 });

		// 给GridView设置Adapter
		grid = (GridView)findViewById(R.id.gridView2);
		grid.setAdapter(simAdapter);
		grid.setOnItemClickListener(this);
	}

	// handle GridView's ItemClick event
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		CGameData gameData = CGameData.getInstance(null);
		SLevelInfo level = gameData.getLevelInfo(position + 1);
		if (null == level)
			return;

		int beanNum = gameData.getBeanNum();

		if (level.enabled == 1)
		{
			Intent intent = new Intent(CShemeSelect.this, CGamePage.class);
			intent.putExtra("LEVEL_ID", level.levelId);
			startActivity(intent);
		}
		else if (level.unlockCost <= beanNum)
		{
			gameData.minusBean(level.unlockCost);
			gameData.setEnable(level.levelId);
			Intent intent = new Intent(CShemeSelect.this, CGamePage.class);
			intent.putExtra("LEVEL_ID", level.levelId);
			startActivity(intent);
		}
		else
		{
			AlertDialog dialog = createDialog();
			dialog.show();
		}
	}

	private AlertDialog createDialog()
	{
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(CShemeSelect.this);

		// 2. set the dialog characteristics
		builder.setMessage("你的金豆不足，快去挣点儿吧^_^\r\n");
		builder.setTitle("提示:");

		builder.setPositiveButton("好的", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				Intent intent = new Intent(CShemeSelect.this, CUserPage.class);
				startActivity(intent);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				// User cancelled the dialog
			}
		});

		// 3. Get the AlertDialog from create()
		return builder.create();
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		Log.d("CShemeSelect", "onStart");
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		Log.d("CShemeSelect", "onResume");
		CGameData gameData = CGameData.getInstance(null);
		txtBean.setText("金豆数：" + gameData.getBeanNum());
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		Log.d("CShemeSelect", "onPause");
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		Log.d("CShemeSelect", "onStop");
	}

	@Override
	protected void onDestroy()
	{
//		if (adsMogoLayout != null)
//		{
//			adsMogoLayout.clearThread();
//		}
		super.onDestroy();
		Log.d("CShemeSelect", "onDestroy");
	}

}
