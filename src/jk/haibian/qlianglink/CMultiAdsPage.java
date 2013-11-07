package jk.haibian.qlianglink;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.adsmogo.adapters.AdsMogoCustomEventPlatformEnum;
import com.adsmogo.adview.AdsMogoLayout;
import com.adsmogo.controller.listener.AdsMogoListener;
import com.adsmogo.util.AdsMogoUtil;

public class CMultiAdsPage extends Activity implements AdsMogoListener
{
	AdsMogoLayout adsMogoLayout;
	TextView tvBeanNum;
	CGameData gameData;
	int[] beans = new int[]{10,10,10,10,10,20,20,20,30,30};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
										  WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_cmulti_ads_page);

		// ���ü����ص� ���а��� ���� չʾ ����ʧ�ܵ��¼��Ļص�
		tvBeanNum = (TextView)findViewById(R.id.textView2);
		adsMogoLayout = ((AdsMogoLayout)this.findViewById(R.id.adsMogoView));
		adsMogoLayout.setAdsMogoListener(this);
		adsMogoLayout = ((AdsMogoLayout)this.findViewById(R.id.adsMogoView12));
		adsMogoLayout.setAdsMogoListener(this);
		adsMogoLayout = ((AdsMogoLayout)this.findViewById(R.id.adsMogoView13));
		adsMogoLayout.setAdsMogoListener(this);
		
		gameData = CGameData.getInstance(null);
		tvBeanNum.setText("������" + gameData.getBeanNum());
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy()
	{
		// ��� adsMogoLayout ʵ�� ���������ڶ��̻߳�����Ƶ��̳߳�
		// �˷����벻Ҫ���׵��ã��������ʱ�䲻����������޷�ͳ�Ƽ���
//		if (adsMogoLayout != null) {
//			adsMogoLayout.clearThread();
//		}
		super.onDestroy();
	}

	/**
	 * ���û�������*(Mogo������ݴμ�¼��������ε���ǹ��˹��ĵ����һ�����һ��չʾֻ�ܶ�Ӧһ�ε��)
	 */
	@Override
	public void onClickAd(String arg0)
	{
		Log.d(AdsMogoUtil.ADMOGO, "-=onClickAd=-");
		int idx = (int)Math.floor( Math.random()*10);
		gameData.addBean(beans[idx]);
		tvBeanNum.setText("������" + gameData.getBeanNum());
	}

	// ���û�����˹��رհ�ťʱ�ص�(�رչ�水ť���ܿ�����Mogo��App����������)
	// return false ����ر� return true ��潫����ر�
	@Override
	public boolean onCloseAd()
	{
		Log.d(AdsMogoUtil.ADMOGO, "-=onCloseAd=-");
		return false;
	}

	/**
	 * ���û��ر����������͹�����ϸ����ʱ�ص�(�����������Ϊ���ع�沢���ǵ���������صĲŻ��д�Dialog)
	 */
	@Override
	public void onCloseMogoDialog()
	{
		Log.d(AdsMogoUtil.ADMOGO, "-=onCloseMogoDialog=-");
	}

	/**
	 * ���й��ƽ̨����ʧ��ʱ�ص�
	 */
	@Override
	public void onFailedReceiveAd()
	{
		Log.d(AdsMogoUtil.ADMOGO, "-=onFailedReceiveAd=-");

	}

	/**
	 * ���û�������*(��ʵ��� Mogo������ݴ˻ص�ʱ��¼��������ε�����޹��˹��ĵ��)
	 */
	@Override
	public void onRealClickAd()
	{
		Log.d(AdsMogoUtil.ADMOGO, "-=onRealClickAd=-");

	}

	/**
	 * ������ɹ�ʱ�ص� arg0Ϊ��һƽ̨�Ĺ����ͼ arg1Ϊ����ƽ̨����
	 */
	@Override
	public void onReceiveAd(ViewGroup arg0, String arg1)
	{
		Log.d(AdsMogoUtil.ADMOGO, "-=onReceiveAd=-");

	}

	/**
	 * ��ʼ������ʱ�ص� arg0Ϊ����ƽ̨����
	 */
	@Override
	public void onRequestAd(String arg0)
	{
		Log.d(AdsMogoUtil.ADMOGO, "-=onRequestAd=-");

	}

	@Override
	public Class getCustomEvemtPlatformAdapterClass(AdsMogoCustomEventPlatformEnum arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
