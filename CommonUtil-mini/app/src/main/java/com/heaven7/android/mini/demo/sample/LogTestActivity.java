package com.heaven7.android.mini.demo.sample;

import android.os.Bundle;
import android.os.Environment;

import com.heaven7.android.mini.demo.BaseActivity;
import com.heaven7.android.mini.demo.R;
import com.heaven7.core.util.Logger;
import com.heaven7.core.util.log.LogManager;

import java.util.List;


/**
 * Created by heaven7 on 2016/6/29.
 */
public class LogTestActivity extends BaseActivity {

    private static final String TAG = "LogTestActivity";
    private final LogManager mLogManager = new LogManager(Environment.getExternalStorageDirectory()+"/heaven7", LogManager.MODE_WRITE_FILE_AND_LOGCAT);

    @Override
    protected int getlayoutId() {
        return R.layout.ac_test_log;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mLogManager.write(LogManager.LEVEL_INFO, TAG, "initData", new RuntimeException("aaaa bbbbbb cccc dddd eeee ,ffff gggg hhhh jjjjj kkkkk."));
        for(int i=0;  i<10 ;i++){
            if(i>0 && i < 6){
                mLogManager.write(i,TAG, "initData_"+i, "messagejdsfjdsjfdsfjdsfjdsfkjdsfkjdskjf" +
                        "dsfkjdsjfkdsfkjdskjfdsjkfdkjsfdkjsfdkjsfkjdsfkjdsfkjdskjfdskjfdskjfdkjsfjds____________"+i);
            }else{
                mLogManager.write(LogManager.LEVEL_INFO, TAG+"__"+i, "initData", "in loop: i = "+ i);
            }
        }
        testRead();
    }

    private void testRead() {
        mLogManager.read(new LogManager.FilterOptions(), new LogManager.IReadCallback() {
            @Override
            public void onResult(List<LogManager.LogRecord> records) {
                Logger.i(TAG, "testRead", "LogRecord: size = " + records.size() +" , " + records);
            }
        });
    }
}
