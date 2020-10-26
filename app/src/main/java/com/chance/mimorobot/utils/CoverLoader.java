package com.chance.mimorobot.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.LruCache;

import com.chance.mimorobot.R;
import com.chance.mimorobot.model.SongPlayInfo;


/**
 * 专辑封面图片加载器
 */
public class CoverLoader {
    public static final int THUMBNAIL_MAX_LENGTH = 500;
    private static final String KEY_NULL = "null";

    // 封面缓存
    private LruCache<String, Bitmap> mCoverCache;
    private Context mContext;

    private enum Type {
        THUMBNAIL(""),
        BLUR("#BLUR"),
        ROUND("#ROUND");

        private String value;

        Type(String value) {
            this.value = value;
        }
    }

    public static CoverLoader getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static CoverLoader instance = new CoverLoader();
    }

    private CoverLoader() {
        // 获取当前进程的可用内存（单位KB）
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // 缓存大小为当前进程可用内存的1/8
        int cacheSize = maxMemory / 8;
        mCoverCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    return bitmap.getAllocationByteCount() / 1024;
                } else {
                    return bitmap.getByteCount() / 1024;
                }
            }
        };
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
    }

    public Bitmap loadThumbnail(SongPlayInfo music) {
        return loadCover(music, Type.THUMBNAIL);
    }

    public Bitmap loadBlur(SongPlayInfo music) {
        return loadCover(music, Type.BLUR);
    }

    public Bitmap loadRound(SongPlayInfo music) {
        return loadCover(music, Type.ROUND);
    }

    private Bitmap loadCover(SongPlayInfo music, Type type) {
        return getDefaultCover(type);
    }

    private Bitmap getDefaultCover(Type type) {
        switch (type) {
            case BLUR:
                return BitmapFactory.decodeResource(mContext.getResources(), R.drawable.play_page_default_bg);
            case ROUND:
                Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.play_page_default_cover);
                bitmap = ImageUtils.resizeImage(bitmap, ScreenUtils.getScreenWidth() / 2, ScreenUtils.getScreenWidth() / 2);
                return bitmap;
            default:
                return BitmapFactory.decodeResource(mContext.getResources(), R.drawable.default_cover);
        }
    }

    /**
     * 从下载的图片加载封面<br>
     * 网络音乐
     */
    private Bitmap loadCoverFromFile(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(path, options);
    }
}
