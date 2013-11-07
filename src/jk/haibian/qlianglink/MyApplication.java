package jk.haibian.qlianglink;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.graphics.Bitmap.Config;

public class MyApplication extends Application
{

	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub
		super.onCreate();

		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory()
				.cacheOnDisc()
				.bitmapConfig(Config.RGB_565)
				.imageScaleType(ImageScaleType.NONE)
				.showStubImage(R.drawable.loading)
				.build();
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.defaultDisplayImageOptions(options)
				.build();
		
		ImageLoader.getInstance().init(config);
	}

}
