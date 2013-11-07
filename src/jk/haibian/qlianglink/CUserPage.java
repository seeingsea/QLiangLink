package jk.haibian.qlianglink;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.adsmogo.interstitial.AdsMogoInterstitial;
import com.adsmogo.interstitial.AdsMogoInterstitialListener;
import com.adsmogo.offers.MogoOffer;
import com.adsmogo.offers.MogoOfferPointCallBack;
import com.adsmogo.util.L;
import com.baidu.mobads.appoffers.OffersManager;

public class CUserPage extends Activity implements MogoOfferPointCallBack
{
	Activity activity;
	public static String mogoID = "1ec52e5a4d394a17a4ca19332a2caf74";

	static {
		try {
			File sdcardFile = Environment.getExternalStorageDirectory();
			File file = new File(sdcardFile + "/MogoOfferID.txt");

			if (file != null && file.exists()) {
				FileInputStream fis = new FileInputStream(file);
				BufferedReader br = new BufferedReader(new InputStreamReader(fis));

				String str = br.readLine();
				if (str != null && !"".equals(str.trim())) {
					mogoID = str;
				}

				br.close();
				fis.close();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private AdsMogoInterstitial adsMogoFull;
	private TextView tvwUserName, tvwBeanNum;
	private Button btnShowOffer, btnRecharge, btnShowAds;
	private CGameData gameData;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
										  WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_cuser_page);
		Log.d("CUserPage","onCreate");
		
		activity = this;
		tvwUserName = (TextView)findViewById(R.id.tvUserName);
		tvwUserName.setText("�û�����" + Build.MODEL );//+ " - " + Build.VERSION.RELEASE);
		tvwBeanNum = (TextView)findViewById(R.id.txtBean);
		btnShowOffer = (Button)findViewById(R.id.button1);
		btnShowOffer.setOnClickListener(new ShowOfferListener());
		btnShowAds = (Button)findViewById(R.id.button3);
		btnShowAds.setOnClickListener(new ShowAdsListent());
		btnRecharge = (Button)findViewById(R.id.button2);
		btnRecharge.setOnClickListener(new RechargeListener());
		
//		adsMogoFull = new AdsMogoInterstitial(CUserPage.this, "1ec52e5a4d394a17a4ca19332a2caf74", true);
//		adsMogoFull.setAdsMogoInterstitialListener(adsmogoFullListener);
		gameData = CGameData.getInstance(null);
		
		mogoOfferInit();	
	}

	protected void mogoOfferInit()
	{
		MogoOffer.init(this, mogoID);
		MogoOffer.addPointCallBack(this);
		MogoOffer.setOfferListTitle("��ȡ����");
		MogoOffer.setOfferEntranceMsg("���������");
		MogoOffer.setMogoOfferScoreVisible(false);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		Log.d("CUserPage","onResume");
		MogoOffer.RefreshPoints(activity);
	}

	@Override
	protected void onPause()
	{
		Log.d("CUserPage","onPause");
		super.onPause();
	}

	@Override
	protected void onDestroy()
	{
		CGameData.getInstance(null).saveData();
		MogoOffer.clear(this);
		Log.d("CUserPage","onDestroy");
		super.onDestroy();
	}

	class ShowOfferListener implements OnClickListener
	{
		public void onClick(View v)
		{
			MogoOffer.showOffer(activity);
		}
	}
	class ShowAdsListent implements OnClickListener
	{
		public void onClick(View v)
		{
			Intent intent = new Intent(CUserPage.this, CMultiAdsPage.class);
			startActivity(intent);
			
//			if (adsMogoFull.getInterstitialAdStart()) {
//				L.i("AdsMoGo", "showFullAd Loading");
//				adsMogoFull.showInterstitialAD();
//				gameData.addBean(1);
//				tvwBeanNum.setText("������" + gameData.getBeanNum());
//			}
//			else {
//				Toast.makeText(CUserPage.this, "�����δ׼����ϣ�", Toast.LENGTH_SHORT).show();
//			}
		}
	}	
	class RechargeListener implements OnClickListener
	{
		public void onClick(View v)
		{
			Toast.makeText(CUserPage.this, "��һ�汾���ţ�", Toast.LENGTH_SHORT).show();
		}
	}
	

	AdsMogoInterstitialListener adsmogoFullListener = new AdsMogoInterstitialListener()
	{
		// ��ʼԤ����ȫ/�������ʱ����(adNameΪ��ǰ���ƽ ̨����)
		@Override
		public void onInterstitialStartReady(String adName)
		{
			Log.d("AdsMoGo", "=====onInterstitialStartReady=====:" + adName);
		}

		// Ԥ����ȫ��������ʱ����(adNameΪ��ǰ���ƽ̨�� ��)
		@Override
		public void onInterstitialReadyed(String adName)
		{
			Log.d("AdsMoGo", "=====onInterstitialReadyed=====:" + adName);
		}

		// ���չʾ�ɹ�ʱ����(�˻ص���Ҫ����һ��intֵ���÷��� ֵ����Ϊ�´ι������ļ��ʱ�䡣adNameΪ��ǰ���ƽ̨����)
		@Override
		public int onInterstitialSucceed(String adName)
		{
			Log.d("AdsMoGo", "=====onInterstitialSucceed=====:" + adName);
			return 20;
		}

		// ��������չʾʧ��ʱ���ã��˻ص���Ҫ����һ��intֵ���÷���ֵ����Ϊ�� �ι������ļ��ʱ�䣩
		@Override
		public int onInterstitialFailed()
		{
			Log.d("AdsMoGo", "=====onInterstitialFailed=====");
			return 20;
		}

		@Override
		public void onInterstitialClickAd(String adName)
		{
			Log.d("AdsMoGo", "=====onInterstitialClickAd=====:" + adName);
			gameData.addBean(10);
			tvwBeanNum.setText("������" + gameData.getBeanNum());		
		}

		@Override
		public void onInterstitialRealClickAd(String adName)
		{
			Log.d("AdsMoGo", "=====onInterstitialRealClickAd=====:" + adName);
		}

		@Override
		public boolean onInterstitialCloseAd()
		{
			Log.d("AdsMoGo", "=====onInterstitialCloseAd=====");
			return false;
		}

		@Override
		public View onInterstitialGetView()
		{
			Log.d("AdsMoGo", "=====onInterstitialGetView=====");
			return btnRecharge;
		}
	};

	@Override
	public void updatePoint(long point)
	{
		Log.w("CUserPage", String.format("beanNum: %d", point));
		int beanNum = gameData.getBeanNum();
		gameData.addBean((int)point);
		tvwBeanNum.setText("������" + (beanNum + point));
		
		if (point > 0) {
			MogoOffer.spendPoints(activity, (int)point);
		}
	}

}
