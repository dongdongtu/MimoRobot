package com.chance.mimorobot.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import okhttp3.ResponseBody;

public class FileUtils
{

	public static final String ROOT_DIR		= "Android/data/";
	public static final String DOWNLOAD_DIR	= "download";
	public static final String CACHE_DIR		= "cache";
	public static final String ICON_DIR		= "icon";

	public static final String APP_STORAGE_ROOT = "AndroidNAdaption";

	/** 判断SD卡是否挂载 */
	public static boolean isSDCardAvailable()
	{
		if (Environment.MEDIA_MOUNTED.equals(Environment
														.getExternalStorageState()))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/** 获取下载目录 */
	public static String getDownloadDir()
	{
		return getDir(DOWNLOAD_DIR);
	}

	/** 获取缓存目录 */
	public static String getCacheDir()
	{
		return getDir(CACHE_DIR);
	}

	/** 获取icon目录 */
	public static String getIconDir()
	{
		return getDir(ICON_DIR);
	}

	/** 获取应用目录，当SD卡存在时，获取SD卡上的目录，当SD卡不存在时，获取应用的cache目录 */
	public static String getDir(String name)
	{
		StringBuilder sb = new StringBuilder();
		if (isSDCardAvailable())
		{
			sb.append(getAppExternalStoragePath());
		}
		else
		{
			sb.append(getCachePath());
		}
		sb.append(name);
		sb.append(File.separator);
		String path = sb.toString();
		if (createDirs(path))
		{
			return path;
		}
		else
		{
			return null;
		}
	}

	/** 获取SD下的应用目录 */
	public static String getExternalStoragePath()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
		sb.append(File.separator);
		sb.append(ROOT_DIR);
		sb.append(File.separator);
		return sb.toString();
	}

	/** 获取SD下当前APP的目录 */
	public static String getAppExternalStoragePath()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
		sb.append(File.separator);
		sb.append(APP_STORAGE_ROOT);
		sb.append(File.separator);
		return sb.toString();
	}

	/** 获取应用的cache目录 */
	public static String getCachePath()
	{
		File f = AppUtil.getContext().getCacheDir();
		if (null == f)
		{
			return null;
		}
		else
		{
			return f.getAbsolutePath() + "/";
		}
	}
	/**
	 * 复制bitmap 对象到指定的路径
	 *
	 * @param path
	 * @return
	 */
	public static boolean copyBitmap2Path(Bitmap bitmap, String path) {
		try {
			File file = new File(path);
			FileOutputStream baos = null;
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			baos = new FileOutputStream(path);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			baos.flush();
			baos.close();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/** 创建文件夹 */
	public static boolean createDirs(String dirPath)
	{
		File file = new File(dirPath);
		if (!file.exists() || !file.isDirectory()) { return  file.mkdirs(); }
		return true;
	}

	/**产生图片的路径，这里是在缓存目录下*/
	public static String generateImgePathInStoragePath( ){

		return getDir(ICON_DIR)+  String.valueOf(System.currentTimeMillis()) + ".jpg";
	}
	public static String generateImgePathInStoragePath(Context context, String m){

		//写入photo文件夹下
//		RxFileTool.createOrExistsDir(RxFileTool.getSDCardPath()+"landManager/photo/"+m+"/");
		return  getDir(ICON_DIR)+ String.valueOf(System.currentTimeMillis()) + ".jpg";
	}
	public static String generateVideoPathInStoragePath(Context context, String m){

		//写入photo文件夹下
//		RxFileTool.createOrExistsDir(RxFileTool.getSDCardPath()+"landManager/photo/"+m+"/");
		return  getDir(ICON_DIR)+ String.valueOf(System.currentTimeMillis()) + ".mp4";
	}

	/**
	 * 获取存储路径，可以写在FileUtils中
	 */
	public String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState()
				.equals(Environment.MEDIA_MOUNTED);//判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();//获取跟目录
		}
		return sdDir.toString();
	}

	/**
	 * 保存image
	 * @param photo
	 * @param spath
	 * @return
	 */
	public static boolean saveImage(Bitmap photo, String spath) {
		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(spath, false));
			photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}


	/**
	 * 发起剪裁图片的请求
	 * @param activity 上下文
	 * @param srcFile 原文件的File
	 * @param output 输出文件的File
	 * @param requestCode 请求码
     */
	public static void startPhotoZoom(Activity activity, File srcFile, File output, int requestCode) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(getImageContentUri(activity,srcFile), "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");

		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", 800);
		intent.putExtra("outputY", 480);
		// intent.putExtra("return-data", false);

		//        intent.putExtra(MediaStore.EXTRA_OUTPUT,
		//                Uri.fromFile(new File(FileUtils.picPath)));

		intent.putExtra("return-data", false);// true:不返回uri，false：返回uri
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		// intent.putExtra("noFaceDetection", true); // no face detection

		activity.startActivityForResult(intent, requestCode);
	}

	/**安卓7.0裁剪根据文件路径获取uri*/
	public static Uri getImageContentUri(Context context, File imageFile) {
		String filePath = imageFile.getAbsolutePath();
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Images.Media._ID },
				MediaStore.Images.Media.DATA + "=? ",
				new String[] { filePath }, null);

		if (cursor != null && cursor.moveToFirst()) {
			int id = cursor.getInt(cursor
					.getColumnIndex(MediaStore.MediaColumns._ID));
			Uri baseUri = Uri.parse("content://media/external/images/media");
			return Uri.withAppendedPath(baseUri, "" + id);
		} else {
			if (imageFile.exists()) {
				ContentValues values = new ContentValues();
				values.put(MediaStore.Images.Media.DATA, filePath);
				return context.getContentResolver().insert(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			} else {
				return null;
			}
		}
	}

	/**
	 * 复制bm
	 * @param bm
	 * @return
     */
	public static String saveBitmap(Bitmap bm) {
		String croppath="";
		try {
			File f = new File(FileUtils.generateImgePathInStoragePath());
			//得到相机图片存到本地的图片
			croppath=f.getPath();
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return croppath;
	}

	/**
	 * 通过路径获取Bitmap对象
	 *
	 * @param path
	 * @return
	 */
	public static Bitmap getBitmap(String path) {
		Bitmap bm = null;
		InputStream is = null;
		try {
			File outFilePath = new File(path);
			//判断如果当前的文件不存在时，创建该文件一般不会不存在
			if (!outFilePath.exists()) {
				boolean flag = false;
				try {
					flag = outFilePath.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("---创建文件结果----" + flag);
			}
			is = new FileInputStream(outFilePath);
			bm = BitmapFactory.decodeStream(is);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bm;
	}

	//判断文件是否存在
	public  static boolean fileIsExists(String filePath)
	{
		try
		{
			File f = new File(filePath);
			if(!f.exists())
			{
				return false;
			}
		}
		catch (Exception e)
		{
			return false;
		}
		return true;
	}

	/**
	 * 按质量压缩bm
	 * @param bm
	 * @param quality 压缩率
	 * @return
	 */
	public static String saveBitmapByQuality(Bitmap bm, int quality) {
		String croppath="";
		try {
			File f = new File(FileUtils.generateImgePathInStoragePath());
			//得到相机图片存到本地的图片
			croppath=f.getPath();
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG,quality, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return croppath;
	}

	/**
	 * 使用属性文件保存用户的信息
	 *
	 * @param context 上下文
	 * @param username 用户名
	 * @param password  密码
	 * @return
	 */
	public static boolean saveProUserInfo(Context context, String username,
                                          String password) {
		try {
			// 使用Android上下问获取当前项目的路径
			File file = new File(context.getFilesDir(), "info.properties");
			// 创建输出流对象
			FileOutputStream fos = new FileOutputStream(file);
			// 创建属性文件对象
			Properties pro = new Properties();
			// 设置用户名或密码
			pro.setProperty("username", username);
			pro.setProperty("password", password);
			// 保存文件
			pro.store(fos, "info.properties");
			// 关闭输出流对象
			fos.close();
			return true;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}




	/**
	 * 返回属性文件对象
	 *
	 * @param context 上下文
	 * @return
	 */
	public static Properties getProObject(Context context) {
		try {
			// 创建File对象
			File file = new File(context.getFilesDir(), "info.properties");
			// 创建FileIutputStream 对象
			FileInputStream fis = new FileInputStream(file);
			// 创建属性对象
			Properties pro = new Properties();
			// 加载文件
			pro.load(fis);
			// 关闭输入流对象
			fis.close();
			return pro;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean writeResponseBodyToDisk(ResponseBody body, String path) {
		try {
			// 改成自己需要的存储位置
			File futureStudioIconFile = new File(path);
			if (futureStudioIconFile.exists()) {
				futureStudioIconFile.delete();
			}

			InputStream inputStream = null;
			OutputStream outputStream = null;

			try {
				byte[] fileReader = new byte[4096];

				long fileSize = body.contentLength();
				long fileSizeDownloaded = 0;

				inputStream = body.byteStream();
				outputStream = new FileOutputStream(futureStudioIconFile);

				while (true) {
					int read = inputStream.read(fileReader);
					if (read == -1) {
						break;
					}
					outputStream.write(fileReader, 0, read);
					fileSizeDownloaded += read;
				}
				outputStream.flush();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			} finally {
				if (inputStream != null) {
					inputStream.close();
				}

				if (outputStream != null) {
					outputStream.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
