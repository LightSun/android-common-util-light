package com.heaven7.core.util.log;

import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.IntDef;
import android.util.Log;

import com.heaven7.core.util.Logger;
import com.heaven7.core.util.WeakHandler;
import com.heaven7.memory.util.RunnablePool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * Created by heaven7 on 2016/6/17.
 */
public final class LogManager implements RunnablePool.IRunnbleExecutor{

    private static final String TAG            = "LogManager";
    private static final boolean DEBUG         = true;

    private static final String NEW_LINE       = "\r\n";
    private static final String GAP            = "_";
    private static final String STATE          = "STATE";
    private static final String CONTENT        = "CONTENT";
    private static final String EQ             = "=";

    private static final String START_LINE     =  "【<<<!@#$%^&*()_+heaven7_log_begin+_)(*&^%$#@!>>>】";
    private static final String END_LINE       =  "【<<<!@#$%^&*()_+heaven7_log_end+_)(*&^%$#@!>>>】";

    //the out mode
    public static final int MODE_WRITE_FILE              = 1;
    public static final int MODE_WRITE_LOGCAT            = 1 << 1;
    public static final int MODE_WRITE_FILE_AND_LOGCAT   = MODE_WRITE_FILE | MODE_WRITE_LOGCAT;


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MODE_WRITE_FILE, MODE_WRITE_LOGCAT, MODE_WRITE_FILE_AND_LOGCAT})
    public @interface ModeType{
    }

    //the log level
    public static final int LEVEL_VERBOSE    = 0x00000005;
    public static final int LEVEL_DEBUG      = 0x00000004;
    public static final int LEVEL_INFO       = 0x00000003;
    public static final int LEVEL_WARNING    = 0x00000002;
    public static final int LEVEL_ERROR      = 0x00000001;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LEVEL_VERBOSE, LEVEL_DEBUG, LEVEL_INFO , LEVEL_WARNING, LEVEL_ERROR })
    public @interface LevelType{
    }

    //the filter mode of read
    // for read filter : dir，date， level, main tag，methodTag, exception, content

    /** filter flag dir */
    public static final int FILTER_FLAG_DIR            = 1 ;
    /** filter flag date */
    public static final int FILTER_FLAG_DATE           = 1 << 1;
    /** filter flag log level */
    public static final int FILTER_FLAG_LOG_LEVEL      = 1 << 2 ;
    /** filter flag main tag */
    public static final int FILTER_FLAG_MAIN_TAG       = 1 << 3 ;
    /** filter flag method tag */
    public static final int FILTER_FLAG_METHOD_TAG     = 1 << 4 ;
    /** filter flag exception */
    public static final int FILTER_FLAG_EXCEPTION      = 1 << 5 ;
    /** filter flag contains content  */
    public static final int FILTER_FLAG_CONTENT        = 1 << 6 ;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = {FILTER_FLAG_DIR, FILTER_FLAG_DATE, FILTER_FLAG_LOG_LEVEL ,
            FILTER_FLAG_MAIN_TAG, FILTER_FLAG_METHOD_TAG,
            FILTER_FLAG_EXCEPTION, FILTER_FLAG_CONTENT
                     } , flag = true)
    public @interface FilterType{
    }

    private static final ILogWriterFilter DEFAULT_FILTER = new ILogWriterFilter() {
        @Override
        public boolean accept(int logLevel, String firstTag, String secondTag) {
            return true;
        }
    };
    private static final ILogFormatter DEFAULT_FORMATTER = new ILogFormatter() {
        @Override
        public String format(String methodTag, String msg) {
            return "called [ " + methodTag + "() ]: " + msg;
        }
    };
    private static final ILogCipherer DEFAULT_CIPHERER = new ILogCipherer() {
        @Override
        public String encrypt(String src) {
            return src;
        }
        @Override
        public String decrypt(String src) {
            return src;
        }
    };

    /**
     * the log filter
     */
    public interface ILogWriterFilter {

        /**
         * @param level the log level
         * @param mainTag the main tag , often is the simple name of class.
         * @param otherTag the other tag, maybe the method name
         * @return true to accept the log. otherwise the current log will be refused.
         */
        boolean accept(int level, String mainTag, String otherTag);
    }

    /**
     * the log formatter
     */
    public interface ILogFormatter {

        /**
         * @param methodTag   method tag
         * @param msg the messages
         * @return  the format string
         */
        String format(String methodTag, String msg);
    }
    /**
     * the log cipherer
     * Created by heaven7 on 2016/6/22.
     */
    public interface ILogCipherer{

        String encrypt(String  src);

        String decrypt(String  src);

    }

    private final String mDir;
    private final int mMode;

    private ILogCipherer     mLogCipherer    = DEFAULT_CIPHERER ;
    private ILogWriterFilter mWriteFilter    = DEFAULT_FILTER;
    private ILogFormatter    mLogFormatter   = DEFAULT_FORMATTER;

    /**
     * a runnable pool for reuse
     */
    private final RunnablePool mPool = new RunnablePool(10);
    private final Handler mHandler;

    /**
     * the share handler thread if you don't care
     */
    private static HandlerThread sHandlerThread;

    /**
     * create a LogManager
     * @param dir  the dir for read or write log file
     * @param mMode the mode
     * @param writeFileHandler the handler thread to write the log file
     */
    public LogManager(String dir, @ModeType int mMode, Handler writeFileHandler) {
        this.mDir = dir;
        this.mMode = mMode;
        File dirFile = new File(dir);
        if(dirFile.isFile()){
            throw new IllegalStateException("must be a dir");
        }
        if(!dirFile.exists()){
            dirFile.mkdirs();
        }
        if(writeFileHandler == null){
            if(sHandlerThread == null){
                sHandlerThread = new HandlerThread("log_LogManager", Thread.MIN_PRIORITY );
                sHandlerThread.start();
            }
            this.mHandler = new WeakHandler<LogManager>(sHandlerThread.getLooper(),this){};
        }else {
            this.mHandler = writeFileHandler;
        }
    }

    public LogManager(String dir,@ModeType int mMode) {
         this(dir,mMode,null);
    }

    // i consider that multi app use this
    public ILogCipherer getLogCipherer() {
        return mLogCipherer;
    }
    public void setLogCipherer(ILogCipherer logCipherer) {
        this.mLogCipherer = logCipherer;
    }

    public ILogWriterFilter getLogWriterFilter() {
        return mWriteFilter;
    }
    public void setLogWriterFilter(ILogWriterFilter filter) {
        this.mWriteFilter = filter;
    }

    public ILogFormatter getLogFormatter() {
        return mLogFormatter;
    }
    public void setLogFormatter(ILogFormatter logFormatter) {
        this.mLogFormatter = logFormatter;
    }

    /**
     * write the log to logcat or file or logcat and file
     * @param level the log level
     * @param tag the log tag
     * @param methodTag the method tag
     * @param exception the exception class name
     * @param message the content message
     */
    public void write(@LevelType  int level, String tag , String methodTag , String exception ,String message){
        if(!mWriteFilter.accept(level, tag, methodTag)){
            return;  //refused
        }
        final String msg = this.mLogFormatter.format(methodTag, message);
        if( (mMode & MODE_WRITE_LOGCAT ) != 0 ){
            switch (level){
                case LEVEL_VERBOSE:
                    Log.i(tag, msg);
                    break;
                case LEVEL_DEBUG:
                    Log.d(tag, msg);
                    break;
                case LEVEL_INFO:
                    Log.i(tag, msg);
                    break;
                case LEVEL_WARNING:
                    Log.w(tag, msg);
                    break;
                case LEVEL_ERROR:
                    Log.e(tag, msg);
                    break;
            }
        }
        //for read filter : dir， date， level, main tag，methodTag, exception, content
        //also need write
        if ((mMode & MODE_WRITE_FILE) != 0) {
            String tmp = String.valueOf(System.currentTimeMillis()).concat(GAP)
                    .concat(String.valueOf(level)).concat(GAP)
                    .concat(tag).concat(GAP)
                    .concat(methodTag).concat(GAP)
                    .concat(exception);

             String result =  START_LINE.concat(NEW_LINE)
                    .concat(STATE).concat(EQ).concat(getLogCipherer().encrypt(tmp)).concat(NEW_LINE)
                    .concat(CONTENT).concat(EQ).concat(getLogCipherer().encrypt(message)).concat(NEW_LINE)
                    .concat(END_LINE).concat(NEW_LINE);
            //post to write
            mHandler.post(mPool.obtain(this,0, result));
        }
    }

    @Override
    public void execute(int what, Object... params) {
        final String filename = LogFileCutUtil.getLogFilename(mDir, "LogManager");
        write2SD(filename, (String) params[0], true);
    }

    public void parse(){
           //TODO
    }

    private static void write2SD(String filename, String message, boolean append ) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(Logger.createFileIfNeed(filename), append )); // append
            bw.append(message);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bw != null)
                try {
                    bw.close();
                } catch (IOException e) {
                    //ignore
                }
        }
    }

    private static void parse(String filename, ILogCipherer cipherer, List<LogRecord> outList){
        File file = new File(filename);
        if(!file.exists())
            throw new RuntimeException("file not exist , filename = " + filename);
        if(!file.isFile())
            throw new RuntimeException("not a file , filename = " + filename);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;

            LogRecord record;
            String state;
            String content;
            String endLine;

            while((line = br.readLine())!=null){
                if(line.equals(START_LINE)){
                     state = br.readLine();
                     content = br.readLine();
                     endLine = br.readLine();
                     if(endLine.equals(END_LINE)){
                         record = parseLogRecord(state,content,cipherer);
                         if(record != null){
                             outList.add(record);
                         }
                     }else{
                         logWhenDebug("parse", "find a log record, but end line is incorrect.endLine = " + endLine);
                     }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(br!=null)
                try {
                    br.close();
                } catch (IOException e) {
                    //ignore
                }
        }
    }

    private static void logWhenDebug(String method, String msg) {
        if(DEBUG){
            Log.d(TAG, "called [ "+ method + "() ]: " + msg);
        }
    }

    private static LogRecord parseLogRecord(String state, String content, ILogCipherer cipherer) {
        logWhenDebug("parseLogRecord","begin parse: state = " + state +" ,content = " + content);
        try{
            String str = state.split(EQ)[1];
            str = cipherer.decrypt(str);
            final String[] tags = str.split(GAP);
             //parse -> time,level,tag,methodTag,exceptionName
            LogRecord record = new LogRecord();
            record.setTime(Long.parseLong(tags[0]));
            record.setLevel(Integer.parseInt(tags[1]));
            record.setTag(tags[2]);
            record.setMethodTag(tags[3]);
            record.setExceptionName(tags[4]);

            //parse content
            str = content.split(EQ)[1];
            str = cipherer.decrypt(str);
            record.setMessage(str);
            return record;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  the log record
     *  dir， date， level, main tag，methodTag, exception, content
     */
    public static class LogRecord{
         private int level;
         private long time;
         private String tag;
         private String methodTag;
         private String exceptionName;
         private String message;

        public int getLevel() {
            return level;
        }
        public void setLevel(int level) {
            this.level = level;
        }

        public long getTime() {
            return time;
        }
        public void setTime(long time) {
            this.time = time;
        }

        public String getTag() {
            return tag;
        }
        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getMethodTag() {
            return methodTag;
        }
        public void setMethodTag(String methodTag) {
            this.methodTag = methodTag;
        }

        public String getExceptionName() {
            return exceptionName;
        }
        public void setExceptionName(String exceptionName) {
            this.exceptionName = exceptionName;
        }

        public String getMessage() {
            return message;
        }
        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "LogRecord{" +
                    "level=" + level +
                    ", time=" + time +
                    ", tag='" + tag + '\'' +
                    ", methodTag='" + methodTag + '\'' +
                    ", exceptionName='" + exceptionName + '\'' +
                    ", message='" + message + '\'' +
                    '}';
        }
    }
}
