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

		// 设置监听回调 其中包括 请求 展示 请求失败等事件的回调
		tvBeanNum = (TextView)findViewById(R.id.textView2);
		adsMogoLayout = ((AdsMogoLayout)this.findViewById(R.id.adsMogoView));
		adsMogoLayout.setAdsMogoListener(this);
		adsMogoLayout = ((AdsMogoLayout)this.findViewById(R.id.adsMogoView12));
		adsMogoLayout.setAdsMogoListener(this);
		adsMogoLayout = ((AdsMogoLayout)this.findViewById(R.id.adsMogoView13));
		adsMogoLayout.setAdsMogoListener(this);
		
		gameData = CGameData.getInstance(null);
		tvBeanNum.setText("金豆数：" + gameData.getBeanNum());
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
		// 清除 adsMogoLayout 实例 所产生用于多线程缓冲机制的线程池
		// 此方法请不要轻易调用，如果调用时间不当，会造成无法统计计数
//		if (adsMogoLayout != null) {
//			adsMogoLayout.clearThread();
//		}
		super.onDestroy();
	}

	/**
	 * 当用户点击广告*(Mogo服务根据次记录点击数，次点击是过滤过的点击，一条广告一次展示只能对应一次点击)
	 */
	@Override
	public void onClickAd(String arg0)
	{
		Log.d(AdsMogoUtil.ADMOGO, "-=onClickAd=-");
		int idx = (int)Math.floor( Math.random()*10);
		gameData.addBean(beans[idx]);
		tvBeanNum.setText("金豆数：" + gameData.getBeanNum());
	}

	// 当用户点击了广告关闭按钮时回调(关闭广告按钮功能可以在Mogo的App管理中设置)
	// return false 则广告关闭 return true 广告将不会关闭
	@Override
	public boolean onCloseAd()
	{
		Log.d(AdsMogoUtil.ADMOGO, "-=onCloseAd=-");
		return false;
	}

	/**
	 * 当用户关闭了下载类型广告的详细界面时回调(广告物料类型为下载广告并且是弹出简介下载的才会有此Dialog)
	 */
	@Override
	public void onCloseMogoDialog()
	{
		Log.d(AdsMogoUtil.ADMOGO, "-=onCloseMogoDialog=-");
	}

	/**
	 * 所有广告平台请求失败时回调
	 */
	@Override
	public void onFailedReceiveAd()
	{
		Log.d(AdsMogoUtil.ADMOGO, "-=onFailedReceiveAd=-");

	}

	/**
	 * 当用户点击广告*(真实点击 Mogo不会根据此回调时记录点击数，次点击是无过滤过的点击)
	 */
	@Override
	public void onRealClickAd()
	{
		Log.d(AdsMogoUtil.ADMOGO, "-=onRealClickAd=-");

	}

	/**
	 * 请求广告成功时回调 arg0为单一平台的广告视图 arg1为请求平台名称
	 */
	@Override
	public void onReceiveAd(ViewGroup arg0, String arg1)
	{
		Log.d(AdsMogoUtil.ADMOGO, "-=onReceiveAd=-");

	}

	/**
	 * 开始请求广告时回调 arg0为请求平台名称
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
