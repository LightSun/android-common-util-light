package com.heaven7.core.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;

import static android.support.v7.widget.StaggeredGridLayoutManager.TAG;

/**
 * get image from pick or camera.
 *
 * @author heaven7
 * @since 1.1.3
 */
public class ImageHelper {

    private static final int NONE = 0;
    private static final int PHOTO_PATH = 1;    // 拍照
    private static final int PHOTO_ZOOM = 2;     // 缩放
    private static final int PHOTO_RESOULT = 3;  // 结果

    private static final String IMAGE_UNSPECIFIED = "image/*";

    private final WeakReference<Activity> mWeakActivity;
    private final ImageCallback mCallback;
    private final String mDir;
    private boolean mDestroied;
    private File mFile;

    //Environment.getExternalStorageDirectory() + "/class100/cache",
    public ImageHelper(String dir , Activity activity, ImageCallback mCallback) {
        this.mDir = dir;
        this.mWeakActivity = new WeakReference<Activity>(activity);
        this.mCallback = mCallback;
    }

    //write sd card
    private File getImageFile() {
        File file = new File(mDir, System.currentTimeMillis() + ".jpg");
        File parent = file.getParentFile();
        if(!parent.exists()){
            if(!parent.mkdirs()){
                Logger.w(TAG,"getImageFile","mkdirs failed. file = " + parent.getAbsolutePath());
            }
        }
        return file;
    }

    public void pick(){
        pick(null);
    }
    /**
     * get image by zoom
     */
    public void pick(String path) {
        if (mDestroied) return;

        final Activity activity = getActivity();
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_UNSPECIFIED);
        Uri uri = Uri.fromFile(TextUtils.isEmpty(path) ? getImageFile() : new File(path));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent, PHOTO_ZOOM);
    }

    public void destroy() {
        this.mDestroied = true;
    }

    private Activity getActivity() {
       final Activity activity = mWeakActivity.get();
        if (activity == null)
            throw new IllegalStateException("activity is mDestroied?");
        return activity;
    }

    public void camera(){
        camera(null);
    }

    /**
     *
     * @param path absolute mPath
     */
    public void camera(String path) {
        if (mDestroied) return;
        final Activity activity = getActivity();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        mFile = path !=null ? new File(path) : getImageFile();
        intent.putExtra(MediaStore.EXTRA_OUTPUT,  Uri.fromFile(mFile));

        activity.startActivityForResult(intent, PHOTO_PATH);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == NONE)
            return;

        switch (requestCode){
            case PHOTO_PATH:
                // 设置文件保存路径,这里放在跟目录下
                startPhotoZoom(Uri.fromFile(mFile));
                break;

            // 读取相册缩放图片
            case PHOTO_ZOOM:
                if(data == null){
                    Logger.i("ImageHelper","onActivityResult","Intent data= null");
                    return;
                }
                startPhotoZoom(data.getData());
                break;

            case PHOTO_RESOULT:
                if(data == null || data.getExtras() == null){
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver()
                                .openInputStream(Uri.fromFile(mFile)));
                        mCallback.onSuccess(mFile, bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }else {
                    Bundle extras = data.getExtras();
                    Bitmap photo = extras.getParcelable("data");
                    if(photo != null) {
                        final File file = getImageFile();
                        try {
                            FileOutputStream out = new FileOutputStream(file);
                            photo.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            out.flush();
                            out.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mCallback.onSuccess(file, photo);
                    }else{
                        Logger.w("ImageHelper","onActivityResult","PHOTO_RESOULT ====> but photo = null.");
                    }
                }

                break;
        }
    }

    private void startPhotoZoom(Uri uri) {
        if (mDestroied) return;
        final Activity activity = getActivity();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 64);
        intent.putExtra("outputY", 64);
        intent.putExtra("return-data", true);

        intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(mFile = getImageFile()));
        mCallback.buildZoomIntent(intent);

        activity.startActivityForResult(intent, PHOTO_RESOULT);
    }

    public static abstract class ImageCallback{


        public abstract void onSuccess(File mFile, Bitmap photo);

        /**
         * called if you want to change intent parameter of zoom.
         * @param defaultIntent the default zoom intent
         */
        public void buildZoomIntent(Intent defaultIntent){

        }
    }

}
