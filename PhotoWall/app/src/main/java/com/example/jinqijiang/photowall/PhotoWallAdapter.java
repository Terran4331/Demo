package com.example.jinqijiang.photowall;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by jinqijiang on 2015/7/8.
 */

public class PhotoWallAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private Bitmap mPlaceHolder;
    private LruCache<String, BitmapDrawable> mMemoryCache;
    private Set<SoftReference<Bitmap>> mBitmapSoftRefSet;
    private DiskLruCache mDiskCache;
    private Object mDiskCacheLock = new Object();

    public PhotoWallAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
        mContext = context;
        mPlaceHolder = BitmapFactory.decodeResource(context.getResources(), R.drawable.place_hold);

        int cacheSize = (int) (Runtime.getRuntime().maxMemory() / 1024 / 32);
        mMemoryCache = new LruCache<String, BitmapDrawable>(cacheSize) {
            @Override
            protected int sizeOf(String key, BitmapDrawable value) {
                return value.getBitmap().getByteCount() / 1024;
            }

            @Override
            protected void entryRemoved(boolean evicted, String key, BitmapDrawable oldValue, BitmapDrawable newValue) {
                if (oldValue instanceof RecyclingBitmapDrawable)
                {
                    ((RecyclingBitmapDrawable) oldValue).setIsCached(false);
                }
                else {
                    if (Utils.hasHoneycomb()) {
                        mBitmapSoftRefSet.add(new SoftReference<Bitmap>(oldValue.getBitmap()));
                    }
                }
            }
        };

        if (Utils.hasHoneycomb()) {
            mBitmapSoftRefSet = Collections.synchronizedSet(new HashSet<SoftReference<Bitmap>>());
        }

        new InitDiskCacheAsyncTask().execute();
    }

    private class InitDiskCacheAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                File dir = getDiskCacheDir("thumb");
                if (!dir.exists()) {
                    dir.mkdir();
                }
                synchronized (mDiskCacheLock) {
                    mDiskCache = DiskLruCache.open(dir, getAppVersion(), 1, 1024 * 1024 * 10);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private File getDiskCacheDir(String folder) {
            String path = (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() ||
                    !Environment.isExternalStorageRemovable()) ? mContext.getExternalCacheDir().getPath() : mContext.getCacheDir().getPath();
            return new File(path + File.separator + folder);
        }

        private int getAppVersion() {
            try {
                return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return 1;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_photo, null);
            convertView = view;
        }
        RecyclingImageView photo = (RecyclingImageView) convertView.findViewById(R.id.photo);
        loadImage(getItem(position), photo);

        return convertView;
    }

    public void flushDiskCache() {
        try {
            synchronized (mDiskCacheLock) {
                mDiskCache.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeDiskCache() {
        try {
            synchronized (mDiskCacheLock) {
                mDiskCache.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadImage(String imageUrl, ImageView photo) {
        if (mMemoryCache.get(imageUrl) != null) {
            photo.setImageDrawable(mMemoryCache.get(imageUrl));
        } else {
            photo.setImageDrawable(new AsyncImageDrawable(imageUrl, mPlaceHolder, photo));
        }
    }

    public class AsyncImageDrawable extends BitmapDrawable {
        private AsyncPhotoTask mAsyncPhotoTask;

        public AsyncImageDrawable(String imageUrl, Bitmap placeHolder, ImageView photo) {
            super(placeHolder);

            Drawable image = photo.getDrawable();
            if (image != null && image instanceof AsyncImageDrawable) {
                AsyncImageDrawable temp = (AsyncImageDrawable) image;
                if (temp.getPhotTask() != null) {
                    temp.getPhotTask().cancel(true);
                }
            }

            mAsyncPhotoTask = new AsyncPhotoTask(photo);
            mAsyncPhotoTask.execute(imageUrl);
        }

        public AsyncPhotoTask getPhotTask() {
            return mAsyncPhotoTask;
        }
    }

    public class AsyncPhotoTask extends AsyncTask<String, Void, Bitmap> {

        private WeakReference<ImageView> mPhotoWeakReference;

        public AsyncPhotoTask(ImageView photo) {
            super();
            mPhotoWeakReference = new WeakReference<ImageView>(photo);
        }

        //这个函数返回值是2的幂，这是参照官网的做法，不过官网是用于BitmapFactory加载本地图片，而不是放缩Bitmap
        private int calculateSampleSize(int srcWidth, int srcHeight, int dstWidth, int dstHeight) {
            int sampleSize = 1;
            while ((srcWidth /= 2) >= dstWidth && (srcHeight /= 2) >= dstHeight) {
                sampleSize *= 2;
            }
            return sampleSize;
        }

        private Bitmap ScaleImage(Bitmap srcBitmap, int dstWidth, int dstHeight) {
            Matrix matrix = new Matrix();
            matrix.postScale((float) dstWidth / srcBitmap.getWidth(), (float) dstHeight / srcBitmap.getHeight());
            return Bitmap.createBitmap(srcBitmap, 0, 0, dstWidth, dstHeight, matrix, true);
        }

        private String getMd5(String key) {
            try {
                MessageDigest digest = MessageDigest.getInstance("MD5");
                digest.update(key.getBytes());
                return bytes2hexString(digest.digest());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return Integer.toString(key.hashCode());
        }

        private String bytes2hexString(byte[] bytes) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                String str = Integer.toHexString(0xff & bytes[i]);
                if (str.length() <= 1) {
                    sb.append('0');
                }
                sb.append(str);
            }
            return sb.toString();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String imageUrl = params[0];
            try {
                String fileName = getMd5(imageUrl);
                Bitmap image = getBitmap4DiskCache(fileName);//先从磁盘缓存中获取
                if (image == null) {//如果没有，再下载
                    URL url = new URL(imageUrl);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setConnectTimeout(5 * 1000);
                    urlConnection.setReadTimeout(10 * 1000);
                    InputStream inputStream = urlConnection.getInputStream();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inMutable = true;
                    options.inSampleSize = 1;
                    image = BitmapFactory.decodeStream(inputStream, null, options);
                    if (image != null) {
                        float density = getContext().getResources().getDisplayMetrics().density;
                        //int sampleSize = calculateSampleSize(image.getWidth(), image.getHeight(), (int)(90*density+0.5f), (int)(90*density+0.5f));
                        //Bitmap scaledBitmap = ScaleImage(image, image.getWidth()/sampleSize, image.getHeight()/sampleSize);
                        int dstWidth = (int) (90 * density + 0.5f);
                        if (dstWidth < image.getWidth()) {
                            int dstHeight = (int) ((float) (dstWidth / image.getWidth()) * image.getHeight());
                            image = ScaleImage(image, dstWidth, dstHeight);//生成缩略图
                        }

                        putBitmap2DiskCache(image, fileName);//添加到磁盘缓存
                    }
                }
                if (image != null) {
                    if (mMemoryCache.get(imageUrl) == null) {
                        //添加到内存缓存
                        if (Utils.hasHoneycomb())
                        {
                            BitmapDrawable drawable = new BitmapDrawable(mContext.getResources(), image);
                            mMemoryCache.put(imageUrl, drawable);
                        }
                        else
                        {
                            RecyclingBitmapDrawable drawable = new RecyclingBitmapDrawable(mContext.getResources(), image);
                            drawable.setIsCached(true);
                            mMemoryCache.put(imageUrl, drawable);
                        }
                    }
                }
                return image;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                ImageView photo = mPhotoWeakReference.get();
                if (photo != null && photo.getDrawable() != null) {
                    Drawable iamge = photo.getDrawable();
                    if (iamge instanceof AsyncImageDrawable && ((AsyncImageDrawable) iamge).getPhotTask() == this) {
                        photo.setImageBitmap(bitmap);
                    }
                }
            }
        }

        private Bitmap getBitmap4DiskCache(String fileName) {
            try {
                synchronized (mDiskCacheLock) {
                    DiskLruCache.Snapshot sp = mDiskCache.get(fileName);
                    if (sp != null) {
                        InputStream is = sp.getInputStream(0);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        options.inSampleSize = 1;
                        BitmapFactory.decodeStream(is, null, options);
                        is.close();

                        if (Utils.hasHoneycomb())
                        {
                            addInBitmapOptions(options);
                        }
                        sp.close();
                        sp = mDiskCache.get(fileName);
                        is = sp.getInputStream(0);
                        options.inJustDecodeBounds = false;
                        options.inMutable = true;
                        Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
                        is.close();
                        sp.close();

                        /*InputStream is = sp.getInputStream(0);
                        Bitmap bitmap = BitmapFactory.decodeStream(is);*/
                        return bitmap;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        private void addInBitmapOptions(BitmapFactory.Options options) {
            // inBitmap only works with mutable bitmaps, so force the decoder to
            // RETURN mutable bitmaps.
            options.inMutable = true;

                // Try to find a bitmap to use for inBitmap.
                Bitmap inBitmap = getBitmapFromReusableSet(options);

                if (inBitmap != null) {
                    // If a suitable bitmap has been found, set it as the value of
                    // inBitmap.
                    options.inBitmap = inBitmap;
                }
        }

        private Bitmap getBitmapFromReusableSet(BitmapFactory.Options options) {
            Bitmap bitmap = null;

            if (mBitmapSoftRefSet != null && !mBitmapSoftRefSet.isEmpty()) {
                synchronized (mBitmapSoftRefSet) {
                    final Iterator<SoftReference<Bitmap>> iterator
                            = mBitmapSoftRefSet.iterator();
                    Bitmap item;

                    while (iterator.hasNext()) {
                        item = iterator.next().get();

                        if (null != item && item.isMutable()) {
                            // Check to see it the item can be used for inBitmap.
                            if (canUseForInBitmap(item, options)) {
                                bitmap = item;

                                // Remove from reusable set so it can't be used again.
                                iterator.remove();
                                break;
                            }
                        } else {
                            // Remove from the set if the reference has been cleared.
                            iterator.remove();
                        }
                    }
                }
            }
            return bitmap;
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        private boolean canUseForInBitmap(
                Bitmap candidate, BitmapFactory.Options targetOptions) {

            if (Utils.hasKitKat()) {
                // From Android 4.4 (KitKat) onward we can re-use if the byte size of
                // the new bitmap is smaller than the reusable bitmap candidate
                // allocation byte count.
                int width = targetOptions.outWidth / targetOptions.inSampleSize;
                int height = targetOptions.outHeight / targetOptions.inSampleSize;
                int byteCount = width * height * getBytesPerPixel(candidate.getConfig());
                return byteCount <= candidate.getAllocationByteCount();
            }

            // On earlier versions, the dimensions must match exactly and the inSampleSize must be 1
            return candidate.getWidth() == targetOptions.outWidth
                    && candidate.getHeight() == targetOptions.outHeight
                    && targetOptions.inSampleSize == 1;
        }

        private int getBytesPerPixel(Bitmap.Config config) {
            if (config == Bitmap.Config.ARGB_8888) {
                return  4;
            } else if (config == Bitmap.Config.RGB_565) {
                return  2;
            } else if (config == Bitmap.Config.ARGB_4444) {
                return  2;
            } else if (config == Bitmap.Config.ALPHA_8) {
                return  1;
            }
            return  1;
        }

        private void putBitmap2DiskCache(Bitmap bitmap, String fileName) {
            DiskLruCache.Editor editor = null;
            try {
                synchronized (mDiskCacheLock) {
                    if (mDiskCache.get(fileName) == null) {
                        editor = mDiskCache.edit(fileName);
                        if (editor != null) {
                            OutputStream os = editor.newOutputStream(0);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                            os.flush();
                            os.close();
                            editor.commit();
                            return;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (editor != null) {
                    editor.abort();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
