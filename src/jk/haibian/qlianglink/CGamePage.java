package jk.haibian.qlianglink;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adsmogo.adview.AdsMogoLayout;
import com.adsmogo.interstitial.AdsMogoInterstitial;
import com.adsmogo.interstitial.AdsMogoInterstitialListener;
import com.adsmogo.util.L;

public class CGamePage extends Activity
{
	private AdsMogoLayout adsMogoLayout;
	private AdsMogoInterstitial adsMogoFull;
	private CGameView gameView;
	private ProgressBar timeBar;
	private Button pauseButton;
	private Button addTime5Btn;
	private Button addTime15Btn;
	private TextView beanNumText;
	private CGameData gameData;
	private SLevelInfo levelInfo;
	private SoundPool soundPlay;
	private int sid_erease, sid_loose, sid_win;
	private Timer timer = null;
	private int timeLeave;
	private boolean bPlaying = false;
	private boolean bShowAds = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
											WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_game_page);

		pauseButton = (Button)findViewById(R.id.button1);
		pauseButton.setOnClickListener(pauseButtonClickListener);
		addTime15Btn = (Button)findViewById(R.id.button2);
		addTime15Btn.setOnClickListener(addTime15BtnClickListener);		
		addTime5Btn = (Button)findViewById(R.id.button3);
		addTime5Btn.setOnClickListener(addTime5BtnClickListener);
		beanNumText = (TextView)findViewById(R.id.txtBeanNum);
		gameView = (CGameView)findViewById(R.id.cGameView1);
		timeBar = (ProgressBar)findViewById(R.id.progressBar1);
		adsMogoLayout = (AdsMogoLayout)findViewById(R.id.adsMogoView);

		soundPlay = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
		sid_erease = soundPlay.load(this, R.raw.erease, 1);
		sid_loose = soundPlay.load(this, R.raw.lose, 1);
		sid_win = soundPlay.load(this, R.raw.win, 1);

		Intent intent = getIntent();
		int levelId = intent.getIntExtra("LEVEL_ID", 0);
		gameData = CGameData.getInstance(null);
		levelInfo = gameData.getLevelInfo(levelId);
		timeLeave = levelInfo.maxTime;
		timeBar.setMax(levelInfo.maxTime);
		timeBar.setProgress(timeLeave);
		int beanNum = gameData.getBeanNum();
		beanNumText.setText(String.valueOf(beanNum));
		pauseButton.setEnabled(false);
		gameView.initGraph(levelInfo, soundPlay, sid_erease);
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		Log.d("CGamePage", "onStart");
		startTimer();
	}

	protected void startTimer()
	{
		if (timer != null) {
			stopTimer();
		}

		TimerTask timerTask = new TimerTask()
		{
			public void run()
			{
				timeHandler.sendEmptyMessage(0x123);
			}
		};
		timer = new Timer();
		timer.schedule(timerTask, 1000, 1000);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		Log.d("CGamePage", "onResume");
		bPlaying = true;
	}

	@Override
	protected void onPause()
	{
		bPlaying = false;
		super.onPause();
		Log.d("CGamePage", "onPause");
	}

	protected void stopTimer()
	{
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	@Override
	protected void onStop()
	{
		stopTimer();
		super.onStop();
		Log.d("CGamePage", "onStop");
	}

	@Override
	protected void onDestroy()
	{
		CGameData.getInstance(null).saveData();
		if (adsMogoLayout != null) {
			adsMogoLayout.clearThread();
		}
		super.onDestroy();
		Log.d("CGamePage", "onDestroy");
	}

	private Handler timeHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what) {
			case 0x123: {
				if (adsMogoFull == null) {
					adsMogoFull = new AdsMogoInterstitial(CGamePage.this, "1ec52e5a4d394a17a4ca19332a2caf74", true);
					adsMogoFull.setAdsMogoInterstitialListener(adsmogoFullListener);
				}
				if (bPlaying) {
					if (timeLeave > 0) {
						timeLeave--;
						timeBar.setProgress(timeLeave);
						gameView.postInvalidate();

						if (timeLeave <= 0) {
							bPlaying = false;
							soundPlay.play(sid_loose, 0.4f, 0.4f, 0, 0, 1);
							AlertDialog lostDialog = createLostDialog();
							lostDialog.show();
						}
						else if (gameView.isCompleted()) {
							bPlaying = false;
							soundPlay.play(sid_win, 0.4f, 0.4f, 0, 0, 1);
							AlertDialog okDialog = createOkDialog();
							okDialog.show();
						}
					}
				}
				else if (bShowAds) {
					// gameData.addPartialBean(0.1f);
					// beanNumText.setText(String.valueOf(gameData.getBeanNum()));
				}
				break;
			}
			}
		}
	};

	protected AlertDialog createLostDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("����");
		builder.setMessage("��ʱʧ���ˣ�^_^o~ Ŭ����");
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface arg0, int arg1)
			{
				CGamePage.this.finish();
			}
		});
		builder.setCancelable(false);
		return builder.create();
	}

	protected AlertDialog createOkDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("��ʾ");
		builder.setMessage("�ɹ����أ�ȥ�����°�^_^");
		builder.setPositiveButton("�õ�", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface arg0, int arg1)
			{
				Intent intent = new Intent(CGamePage.this, CImageBrowse.class);
				intent.putExtra("LEVEL_ID", levelInfo.levelId);
				startActivity(intent);
				finish();
			}
		});
		builder.setNegativeButton("����", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				CGamePage.this.finish();
			}
		});
		builder.setCancelable(false);
		return builder.create();
	}

	private OnClickListener pauseButtonClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View arg0)
		{
//			Intent intent = new Intent(CGamePage.this, CImageBrowse.class);
//			intent.putExtra("LEVEL_ID", levelInfo.levelId);
//			startActivity(intent);
//			finish();		

			Toast.makeText(CGamePage.this, "��ͣʱ�й���ӽ�Ŷ~\n(������ͣ����20������)", Toast.LENGTH_SHORT).show();
			if (adsMogoFull.getInterstitialAdStart()) {
				L.i("AdsMoGo", "showFullAd Loading");
				adsMogoFull.showInterstitialAD();
				pauseButton.setEnabled(false);
				gameData.addBean(2);
				beanNumText.setText(String.valueOf(gameData.getBeanNum()));
			}
			else {
				pauseButton.setEnabled(false);
				//Toast.makeText(CGamePage.this, "�����δ׼����ϣ�", Toast.LENGTH_SHORT).show();
			}
		}
	};

	private OnClickListener addTime15BtnClickListener = new OnClickListener()
	{
		public void onClick(View view)
		{
			int beanNum = gameData.getBeanNum();
			if (beanNum > 2) {
				gameData.minusBean(2);
				beanNumText.setText(String.valueOf(beanNum-2));
				timeLeave += 25;
				timeBar.incrementProgressBy(25);
				if (timeLeave > levelInfo.maxTime) {
					timeLeave = levelInfo.maxTime;
				}
			}
			else {
				Toast.makeText(CGamePage.this, "��Ľ𶹲��㣡", Toast.LENGTH_SHORT).show();
			}
		}
	};

	private OnClickListener addTime5BtnClickListener = new OnClickListener()
	{
		public void onClick(View view)
		{
			int beanNum = gameData.getBeanNum();
			if (beanNum > 1) {
				gameData.minusBean(1);
				beanNumText.setText(String.valueOf(beanNum-1));
				timeLeave += 10;
				timeBar.incrementProgressBy(10);
				if (timeLeave > levelInfo.maxTime) {
					timeLeave = levelInfo.maxTime;
				}
			}
			else {
				Toast.makeText(CGamePage.this, "��Ľ𶹲��㣡", Toast.LENGTH_SHORT).show();
			}
		}
	};

	AdsMogoInterstitialListener adsmogoFullListener = new AdsMogoInterstitialListener()
	{
		// ��ʼԤ����ȫ/�������ʱ����(adNameΪ��ǰ���ƽ ̨����)
		@Override
		public void onInterstitialStartReady(String adName)
		{
			L.i("AdsMoGo", "=====onInterstitialStartReady=====:" + adName);
		}

		// Ԥ����ȫ��������ʱ����(adNameΪ��ǰ���ƽ̨�� ��)
		@Override
		public void onInterstitialReadyed(String adName)
		{
			L.i("AdsMoGo", "=====onInterstitialReadyed=====:" + adName);
			pauseButton.setEnabled(true);
		}

		// ���չʾ�ɹ�ʱ����(�˻ص���Ҫ����һ��intֵ���÷��� ֵ����Ϊ�´ι������ļ��ʱ�䡣adNameΪ��ǰ���ƽ̨����)
		@Override
		public int onInterstitialSucceed(String adName)
		{
			L.i("AdsMoGo", "=====onInterstitialSucceed=====:" + adName);
			bShowAds = true;
			bPlaying = false;
			return 20;
		}

		// ��������չʾʧ��ʱ���ã��˻ص���Ҫ����һ��intֵ���÷���ֵ����Ϊ�� �ι������ļ��ʱ�䣩
		@Override
		public int onInterstitialFailed()
		{
			L.i("AdsMoGo", "=====onInterstitialFailed=====");
			bShowAds = false;
			bPlaying = true;
			return 20;
		}

		@Override
		public void onInterstitialClickAd(String adName)
		{
			L.i("AdsMoGo", "=====onInterstitialClickAd=====:" + adName);
			gameData.addBean(15);
			beanNumText.setText(String.valueOf(gameData.getBeanNum()));		}

		@Override
		public void onInterstitialRealClickAd(String adName)
		{
			L.i("AdsMoGo", "=====onInterstitialRealClickAd=====:" + adName);
		}

		@Override
		public boolean onInterstitialCloseAd()
		{
			L.i("AdsMoGo", "=====onInterstitialCloseAd=====");
			bShowAds = false;
			bPlaying = true;
			return false;
		}

		@Override
		public View onInterstitialGetView()
		{
			L.i("AdsMoGo", "=====onInterstitialGetView=====");
			return pauseButton;
		}
	};

}
