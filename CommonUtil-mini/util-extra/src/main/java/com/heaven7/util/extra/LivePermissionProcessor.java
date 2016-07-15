package com.heaven7.util.extra;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;


/** camera and audio permission manager
 * Created by heaven7 on 2016/6/21.
 */
public class LivePermissionProcessor {

    private static final String TAG = "PermissionProcessor";

    public static final int REQUEST_CODE_CAMERA  = 88;
    public static final int REQUEST_CODE_AUDIO   = 89;

    private final Activity mActivity ;
    private final IPermissionCallback mCallback;

    private boolean mRequestedCamera;

    private boolean mCameraChecked;
    private boolean mAudioChecked;

    public LivePermissionProcessor(Activity mActivity, IPermissionCallback mCallback) {
        this.mActivity = mActivity;
        this.mCallback = mCallback;
    }

    /** request camera and then request audio */
    public void requestVideoPermission(){
        mRequestedCamera = true;
        if(ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            //Logger.i(TAG, "requestVideoPermission", "begin request camera...");
            ActivityCompat.requestPermissions(mActivity, new String[]{ Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
        }else{
            //build =22 ,default checkSelfPermission = PERMISSION_GRANTED.
            //here we need to check permission use camera and AudioRecord API to check
            if(!mCameraChecked) {
                boolean success = false;
                try {
                   // Logger.i(TAG, "requestVideoPermission", "begin openCamera() ...");
                    final Camera camera = OpenCameraInterface.open();
                    camera.release();
                    success = true;
                } catch (Exception e) {
                    mRequestedCamera = false;
                    Log.i(TAG, "requestVideoPermission__open camera failed, "+ e.toString());
                    mCallback.onRequestCameraFailed();
                }finally {
                    if(success){
                        mCameraChecked = true;
                        requestAudioPermission();
                    }
                }
            }else{
                requestAudioPermission();
            }
        }
    }

    public void requestAudioPermission(){
        if(ContextCompat.checkSelfPermission(mActivity, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
           // Logger.i(TAG, "requestVideoPermission", "begin request audio ...");
            ActivityCompat.requestPermissions(mActivity, new String[]{ Manifest.permission.RECORD_AUDIO }, REQUEST_CODE_AUDIO);
        }else{
            if(!mAudioChecked) {
                boolean success = false;
                try {
                   // Logger.i(TAG, "requestAudioPermission", "begin openAudio() ...");
                    doDemoAudio();
                    success = true;
                } catch (Exception e) {
                    Log.i(TAG, "requestAudioPermission : open audio record failed...exception = "+ e.toString());
                    mCallback.onRequestAudioFailed(mRequestedCamera);
                    mRequestedCamera = false;
                }finally {
                    if(success) {
                        mAudioChecked = true;
                        doWithRequestAudioSuccess();
                    }
                }
            }else{
                doWithRequestAudioSuccess();
            }
        }
    }

    private void doWithRequestAudioSuccess() {
        if (mRequestedCamera) {
            mRequestedCamera = false;
            mCallback.onRequestVideoPermissionSuccess();
        } else {
            mCallback.onRequestAudioPermissionSuccess();
        }
    }

    //http://blog.csdn.net/peijiangping1989/article/details/7042610
    //音频裸数据要播放 需要写入对应的头文件
    private void doDemoAudio() throws Exception{
        // 音频获取源
        int audioSource = MediaRecorder.AudioSource.MIC;
        // 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
         int sampleRateInHz = 44100;
        // 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
         int channelConfig = AudioFormat.CHANNEL_IN_STEREO;
        // 音频数据格式:PCM 16位每个样本。保证设备支持。PCM 8位每个样本。不一定能得到设备支持。
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        // 缓冲区字节大小
        int bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz,
                channelConfig, audioFormat);
        // 创建AudioRecord对象
        final AudioRecord ar = new AudioRecord(audioSource, sampleRateInHz,
                channelConfig, audioFormat, bufferSizeInBytes);
        ar.startRecording();
        Log.i(TAG, "doDemoAudio : AudioRecord____startRecording()");
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    ar.stop();
                    ar.release();
                    Log.i(TAG, "doDemoAudio : AudioRecord____release()");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
         if(requestCode == REQUEST_CODE_CAMERA){
             if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                 mRequestedCamera = false;
                // Logger.i(TAG, "onRequestPermissionsResult", " request camera failed...");
                 mCallback.onRequestCameraFailed();
             }else{
                 // camera成功，继续请求audio
                // Logger.i(TAG, "onRequestPermissionsResult", " request camera success...");
                 requestAudioPermission();
             }
         }else if(requestCode == REQUEST_CODE_AUDIO){
             if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                 //Logger.i(TAG, "onRequestPermissionsResult", " request audio failed...");
                 mCallback.onRequestAudioFailed(mRequestedCamera);
                 mRequestedCamera = false;
             }else{
                 doWithRequestAudioSuccess();
             }
         }
    }


    public interface IPermissionCallback{

        /** request camera and audio success */
        void onRequestVideoPermissionSuccess();
        /** request audio success */
        void onRequestAudioPermissionSuccess();
        /** request camera failed */
        void onRequestCameraFailed();
        /** request audio failed
         * @param hasCamera if have camera */
        void onRequestAudioFailed(boolean hasCamera);
    }
}
