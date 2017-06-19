package com.ky.kyandroid;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.xutils.x;

/**
 * Created by caizhui on 2016-11-28.
 * 备注：全局应用程序类：用于保存和调用全局应用配置
 */
public class AppContext extends Application {
	
		/** AppContext */
		private static AppContext sApplication = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		sApplication = this;
		// 初始化XUtils3
		x.Ext.init(this);
		// 设置debug模式
		x.Ext.setDebug(true);
		initImageLoader(this);
	}
	
	/**
	 * 获取Application实例
	 * 
	 * @return
	 */
	public static AppContext getInstance() {
		if (sApplication == null) {
			throw new IllegalStateException("Application is not created.");
		}
		return sApplication;
	}

	//初始化ImageLoader
	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize( 100 * 1024 * 1024) // 缓存大小
				.diskCacheFileCount(300)
//				.imageDownloader(new MyImageDownloader(context))
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();

		ImageLoader.getInstance().init(config);
	}

	//改方法名 和 图片路径名等
	public static DisplayImageOptions getImgBuilder() {
		return new DisplayImageOptions.Builder()
//	        .showStubImage(R.drawable.ic_launcher)          // 设置图片下载期间显示的图片
//	        .showImageForEmptyUri()  // 设置图片Uri为空或是错误的时候显示的图片
//	        .showImageOnFail()       // 设置图片加载或解码过程中发生错误显示的图片
//			.showImageOnLoading(R.drawable.touxiang_normal) //下载过程中显示的图片
				.cacheInMemory(true)     // 设置下载的图片是否缓存在内存中
				.cacheOnDisk(true)       // 设置下载的图片是否缓存在SD卡中
				.bitmapConfig(Bitmap.Config.RGB_565) //设置图片的解码类型
//	        .delayBeforeLoading(1000)  //设置图片下载前的延迟
				.displayer(new RoundedBitmapDisplayer(20))   //设置显示风格这里是圆角矩形
				.build();
	}
}
