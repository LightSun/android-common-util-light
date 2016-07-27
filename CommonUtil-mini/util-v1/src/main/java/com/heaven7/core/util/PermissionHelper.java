package com.heaven7.core.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * the permission helper to request a list of permissions.
 * if any permission request failed. the permission list of request is end and call callback right now.
 * Created by heaven7 on 2016/7/25.
 */
public class PermissionHelper {

    private final Activity mActivity;

    private  ICallback mCallback;
    private  PermissionParam[] mParams;

    /**
     * the check index for verify loop
     */
    private  int mCheckingIndex;

    /**
     * the permission callback
     */
    public interface ICallback{

        /**
         * called when request permissions done or failed.
         * @param requestPermission  the permission to request
         * @param requestCode  the request code
         * @param success  if success to request the permission group.
         */
        void onRequestPermissionResult(String requestPermission, int requestCode, boolean success);
    }

    public PermissionHelper(Activity activity){
        if(activity == null){
            throw new NullPointerException();
        }
        this.mActivity = activity;
    }

    /**
     * begin request the permission group
     * @param params the permission params to request ,but must in order
     * @param callback  the callback
     */
    public void startRequestPermission(PermissionParam[] params, ICallback callback){
        if(params==null || callback==null){
            throw new NullPointerException();
        }
        this.mParams = params;
        this.mCallback = callback;
        requestPermissionImpl();
    }

    /**
     * begin request the permission group
     * @param requestPermissions the permissions to request and must in order
     * @param requestCodes the request codes , but must in order and  match the requestPermissions
     * @param callback  the callback
     */
    public void startRequestPermission(String[] requestPermissions, int[] requestCodes, ICallback callback){
        if(requestPermissions==null || requestCodes==null){
            throw new NullPointerException();
        }
        if(requestPermissions.length != requestCodes.length){
            throw new IllegalArgumentException("caused by requestPermissions.length != requestCodes.length");
        }
        int size = requestPermissions.length;
        PermissionParam[] params = new PermissionParam[size];
        for(int i=0 ; i<size ;i++){
            params[i] = new PermissionParam(requestPermissions[i],requestCodes[i]);
        }
        startRequestPermission(params,callback);
    }

    /**
     * do begin request permission.
     */
    protected void requestPermissionImpl(){
        final Activity activity = this.mActivity;
        final PermissionParam permissionParam = mParams[mCheckingIndex];

        if(ContextCompat.checkSelfPermission(activity, permissionParam.requestPermission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{ permissionParam.requestPermission }, permissionParam.requestCode);
        }else {
            if(Build.VERSION.SDK_INT < 23 || permissionParam.verifier==null){
                checkNext(permissionParam, true);
            }else {
                //check third app intercept
                checkNext(permissionParam,permissionParam.verifier.verify(permissionParam.params));
            }
        }
    }

    /**
     * check and do next with permission
     * @param permissionParam the permission param
     * @param success the result of check the current permission
     */
    private void checkNext(PermissionParam permissionParam, boolean success) {
        if(!success){
            mCheckingIndex = 0;
            mCallback.onRequestPermissionResult(permissionParam.requestPermission, permissionParam.requestCode, false);
            return ;
        }
        //check request end
        if(mParams.length-1 > mCheckingIndex){
            //request next
            mCheckingIndex += 1;
            requestPermissionImpl();
        }else{
            //request end
            mCheckingIndex = 0;
            mCallback.onRequestPermissionResult(permissionParam.requestPermission, permissionParam.requestCode, true);
        }
    }

    /**
     * for activity call.
     * @param requestCode the request code
     * @param permissions the permissions
     * @param grantResults the grant result
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        final PermissionParam permissionParam = mParams[mCheckingIndex];
        if(permissionParam.requestCode == requestCode){
            checkNext(permissionParam, grantResults[0] == PackageManager.PERMISSION_GRANTED);
         }
    }

    /**
     * the permission param
     */
    public static class PermissionParam{
        final String requestPermission;
        final int requestCode;
        /**
         * the exception verifier , when we want to check permission by ourselves.
         * this is caused by some third app intercept the permission.
         */
        final ExceptionVerifier verifier;
        /**
         * the params to execute verification
         */
        final Object params;

        /**
         * @see #PermissionParam(String, int, ExceptionVerifier, Object[])
         * @param requestPermission the permission to request
         * @param requestCode the request code
         */
        public PermissionParam(String requestPermission, int requestCode) {
           this(requestPermission,requestCode,null);
        }

        /**
         * create a permission param by the param
         * @param requestPermission the permission
         * @param requestCode  the request code
         * @param verifier the exception verifier
         * @param params  the extra params to execute verification
         * @param <Prams>  the extra params to execute verification
         * @param <Result> the result of execute verification
         */
        public <Prams,Result> PermissionParam(String requestPermission, int requestCode, ExceptionVerifier<Prams,Result> verifier, Prams...params) {
            if(requestPermission==null){
                throw new IllegalArgumentException("requestPermission can't be null");
            }
            this.requestPermission = requestPermission;
            this.requestCode = requestCode;
            this.verifier = verifier;
            this.params = params;
        }
    }

/**
 *  boolean success = false;
 try {
 doDemoAudio();
 success = true;
 } catch (Exception e) {
 mCallback.onRequestAudioFailed(mRequestedCamera);
 mRequestedCamera = false;
 }finally {
 if(success) {
 mAudioChecked = true;
 doWithRequestAudioSuccess();
 }
 }
 */
}
