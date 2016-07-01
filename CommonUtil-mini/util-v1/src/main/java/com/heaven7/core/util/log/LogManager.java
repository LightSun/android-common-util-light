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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heaven7 on 2016/6/17.
 */
public final class LogManager{

    private static final String TAG            = "LogManager";
    private static final boolean DEBUG         = true;

    private static final String NEW_LINE       = "\r\n";
    private static final String GAP            = "_=0123456789LogManager9876543210=_";
    private static final String STATE          = "STATE";
    private static final String CONTENT        = "CONTENT";
    private static final String EQ             = "=";

    private static final String START_LINE     =  "【<<<!@#$%^&*()_+heaven7_log_begin+_)(*&^%$#@!>>>】";
    private static final String END_LINE       =  "【<<<!@#$%^&*()_+heaven7_log_end+_)(*&^%$#@!>>>】";
    private static final int WHAT_WRITE        = 0x00000001;
    private static final int WHAT_READ         = 0x00000002;

    //the out mode
    public static final int MODE_WRITE_FILE              = 1;
    public static final int MODE_WRITE_LOGCAT            = 1 << 1;
    public static final int MODE_WRITE_FILE_AND_LOGCAT   = MODE_WRITE_FILE | MODE_WRITE_LOGCAT;


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ MODE_WRITE_FILE, MODE_WRITE_LOGCAT, MODE_WRITE_FILE_AND_LOGCAT })
    public @interface ModeType{
    }

    //the log level
    public static final int LEVEL_VERBOSE    = 0x00000001;
    public static final int LEVEL_DEBUG      = 0x00000002;
    public static final int LEVEL_INFO       = 0x00000003;
    public static final int LEVEL_WARNING    = 0x00000004;
    public static final int LEVEL_ERROR      = 0x00000005;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LEVEL_VERBOSE, LEVEL_DEBUG, LEVEL_INFO , LEVEL_WARNING, LEVEL_ERROR })
    public @interface LevelType{
    }

    // for read filter : dir，date， level, main tag，methodTag, exception, content
   /* *//** filter flag dir *//*
    public static final int FILTER_FLAG_DIR                = 1 ;
    *//** filter flag date *//*
    public static final int FILTER_FLAG_DATE               = 1 << 1;
    *//** filter flag log level *//*
    public static final int FILTER_FLAG_LOG_LEVEL          = 1 << 2 ;
    *//** filter flag main tag *//*
    public static final int FILTER_FLAG_MAIN_TAG           = 1 << 3 ;
    *//** filter flag method tag *//*
    public static final int FILTER_FLAG_METHOD_TAG         = 1 << 4 ;
    *//** filter flag exception class name *//*
    public static final int FILTER_FLAG_EXCEPTION          = 1 << 5 ;
    *//** filter flag contains content  *//*
    public static final int FILTER_FLAG_CONTAINS_CONTENT   = 1 << 6 ;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = {FILTER_FLAG_DIR, FILTER_FLAG_DATE, FILTER_FLAG_LOG_LEVEL ,
            FILTER_FLAG_MAIN_TAG, FILTER_FLAG_METHOD_TAG,
            FILTER_FLAG_EXCEPTION, FILTER_FLAG_CONTAINS_CONTENT
                     } , flag = true)
    public @interface FilterType{
    }*/

    private static final ILogWriterFilter DEFAULT_FILTER = new ILogWriterFilter() {
        @Override
        public boolean accept(int logLevel, String firstTag, String secondTag, String exception) {
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
     * the callback of read logs.
     */
    public interface IReadCallback{
        /**
         * this will be called after read done.
         * @param records the result list.
         */
        void onResult(List<LogRecord> records);
    }
    /**
     * the log filter
     */
    public interface ILogWriterFilter {

        /**
         *  true to accept it or else will ignored.
         * @param level the log level
         * @param mainTag the main tag , often is the simple name of class.
         * @param otherTag the other tag, maybe the method name
         * @param exceptionClassName the exception class name
         * @return true to accept the log. otherwise the current log will be refused.
         */
        boolean accept(int level, String mainTag, String otherTag, String exceptionClassName);
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

    /**
     * the runnable executor for write/read log file
     */
    private final RunnablePool.IRunnbleExecutor mExecutor = new RunnablePool.IRunnbleExecutor() {
        @Override
        public void execute(int what, Object... params) {
            switch (what){
                case WHAT_WRITE:
                    final String filename = LogFileCutUtil.getLogFilename(mDir, "LogManager");
                    write2SD(filename, (String) params[0], true);
                    break;

                case WHAT_READ: {
                    FilterOptions ops = (FilterOptions) params[0];
                    IReadCallback callback = (IReadCallback) params[1];
                    List<LogRecord> list = new ArrayList<>();
                    readLogsImpl(new File(mDir), list, ops);
                    callback.onResult(list);
                }
                break;
            }
        }
    };

    private final String mDir;
    private final int mMode;

    private ILogCipherer     mLogCipherer    = DEFAULT_CIPHERER ;
    private ILogWriterFilter mWriteFilter    = DEFAULT_FILTER;
    private ILogFormatter    mLogFormatter   = DEFAULT_FORMATTER;

    /**
     * a runnable pool for reuse
     */
    private static final RunnablePool sPool = new RunnablePool(10);
    private final Handler mHandler;

    /**
     * the share handler thread if you don't care
     */
    private static HandlerThread sHandlerThread;


    /**
     * create a LogManager
     * @param dir  the dir for read or write log file
     * @param mMode the mode
     * @param workHandler the handler thread to write/read the log file
     */
    public LogManager(String dir, @ModeType int mMode, Handler workHandler) {
        this.mDir = dir;
        this.mMode = mMode;
        File dirFile = new File(dir);
        if(dirFile.isFile()){
            throw new IllegalStateException("must be a dir");
        }
        if(!dirFile.exists()){
            dirFile.mkdirs();
        }
        if(workHandler == null){
            if(sHandlerThread == null){
                sHandlerThread = new HandlerThread("log_LogManager", Thread.MIN_PRIORITY );
                sHandlerThread.start();
            }
            this.mHandler = new WeakHandler<LogManager>(sHandlerThread.getLooper(),this){};
        }else {
            this.mHandler = workHandler;
        }
    }
    /**
     * create a LogManager and the mode is {@link #MODE_WRITE_FILE_AND_LOGCAT}
     * @param dir  the dir for read or write log file
     * @param workHandler the handler thread to write/read the log file
     */
    public LogManager(String dir, Handler workHandler) {
        this(dir, MODE_WRITE_FILE_AND_LOGCAT, workHandler);
    }
    /**
     * create a LogManager and the workHandler is the global share handler
     * @param dir  the dir for read or write log file
     * @param mMode the mode
     */
    public LogManager(String dir,@ModeType int mMode) {
         this(dir,mMode,null);
    }
    /**
     * create a LogManager and the workHandler is the global share handler,the mode is {@link #MODE_WRITE_FILE_AND_LOGCAT}
     * @param dir  the dir for read or write log file
     */
    public LogManager(String dir) {
         this(dir, null );
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


    public void v(String tag , String methodTag, String message){
        write(LogManager.LEVEL_VERBOSE, tag, methodTag, message);
    }
    public void d(String tag , String methodTag, String message){
        write(LogManager.LEVEL_DEBUG, tag, methodTag, message);
    }
    public void i(String tag , String methodTag, String message){
        write(LogManager.LEVEL_INFO, tag, methodTag, message);
    }
    public void w(String tag , String methodTag, String message){
        write(LogManager.LEVEL_WARNING, tag, methodTag, message);
    }
    public void e(String tag , String methodTag, String message){
        write(LogManager.LEVEL_ERROR, tag, methodTag, message);
    }

    /**
     * write the log to logcat or file or logcat with file
     * @param level the log level
     * @param tag the log tag
     * @param methodTag the method tag
     * @param e the Throwable
     */
    public void write(@LevelType  int level, String tag , String methodTag, Throwable e){
         write(level, tag,methodTag, e.getClass().getName(), Logger.toString(e));
    }
    /**
     * write the log to logcat or file or logcat with file
     * @param level the log level
     * @param tag the log tag
     * @param methodTag the method tag
     * @param message the message
     */
    public void write(@LevelType  int level, String tag , String methodTag, String message){
        write(level, tag,methodTag, null, message);
    }
    /**
     * write the log to logcat or file or logcat and file
     * @param level the log level
     * @param tag the log tag
     * @param methodTag the method tag
     * @param exception the exception class name,can be null
     * @param message the content message
     */
    public void write(@LevelType  int level, String tag , String methodTag, String exception ,String message){
        if(!mWriteFilter.accept(level, tag, methodTag, exception)){
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
                    .concat(String.valueOf(tag)).concat(GAP)
                    .concat(String.valueOf(methodTag)).concat(GAP)
                    .concat(String.valueOf(exception));

             String result =  START_LINE.concat(NEW_LINE)
                    .concat(STATE).concat(EQ).concat( String.valueOf(getLogCipherer().encrypt(tmp)) ).concat(NEW_LINE)
                    .concat(CONTENT).concat(EQ).concat( String.valueOf(getLogCipherer().encrypt(msg)) ).concat(NEW_LINE)
                    .concat(END_LINE).concat(NEW_LINE);
            //post to write
            mHandler.post(sPool.obtain(mExecutor, WHAT_WRITE, result));
        }
    }

    /**
     * read the logs from local file.
     * @param ops  the filter options,can be null, if you don't need fiter log
     * @param callback the read callback
     */
    public void read(FilterOptions ops, IReadCallback callback){
        mHandler.post(sPool.obtain(mExecutor, WHAT_READ, ops, callback ));
    }

    private void readLogsImpl(File dir, List<LogRecord> outList, FilterOptions ops){
        final File[] files = dir.listFiles();
        if(files == null || files.length == 0){
            return ;
        }
        final ILogCipherer mLogCipherer = this.mLogCipherer;
        for (File f : files){
            if(f.isDirectory()){
                if(ops==null || ops.dir == null || f.getAbsolutePath().equals(ops.dir)){
                    readLogsImpl(f, outList, ops);
                }
            }else{
                readLogFile(f, mLogCipherer, outList, ops);
            }
        }
    }

    private static void readLogFile(File file, ILogCipherer cipherer, List<LogRecord> outList, FilterOptions ops){
        if(!file.exists())
            throw new RuntimeException("file not exist , filename = " + file.getAbsolutePath());
        if(!file.isFile())
            throw new RuntimeException("not a file , filename = " + file.getAbsolutePath());
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;

            LogRecord record;
            String state;
            String content;
            String tmp;

            while((line = br.readLine())!=null){
                if(line.equals(START_LINE)){
                     state = br.readLine();
                     content = br.readLine();
                    //find the end line
                    while( (tmp = br.readLine() )!=null ) {
                        if (END_LINE.equals(tmp)) {
                            //parse and reset
                            record = parseLogRecord(state, content, cipherer, ops);
                            if (record != null) {
                                outList.add(record);
                            }
                            break;
                        } else {
                            content = content.concat(tmp);
                        }
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

    /**
     *  parse to a log record
     * @param state the state line
     * @param content the content line
     * @param cipherer the Log Cipherer
     * @param ops the filter options
     * @return a LogRecord if successed parsed or else null。
     */
    private static LogRecord parseLogRecord(String state, String content, ILogCipherer cipherer, FilterOptions ops) {
        logWhenDebug("parseLogRecord","begin parse: state = " + state +" ,content = " + content);
        try{
            String str = state.substring(state.indexOf(EQ)+1);
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
            str = content.substring(content.indexOf(EQ)+1);
            str = cipherer.decrypt(str);
            record.setMessage(str);

            if(verifyFilterOptions(record,ops)){
                return record;
            }else{
                return null;
            }
        }catch (Exception e){
            //may be decrypt failed.
            e.printStackTrace();
            return null;
        }
    }

    private static boolean verifyFilterOptions(LogRecord record, FilterOptions ops) {
        if(ops == null){
            return true;
        }
        if(ops.startTime != 0 && record.getTime() < ops.startTime){
            return false;
        }
        if(ops.endTime != 0 && record.getTime() > ops.endTime){
            return false;
        }
        if(ops.level!=0 && record.getLevel() != ops.level){
            return false;
        }
        if(ops.lowestLevel!=0 && record.getLevel() < ops.lowestLevel){
            return false;
        }
        if(ops.tag!=null && !record.getTag().equals(ops.tag)){
            return false;
        }
        if(ops.methodTag!=null && !ops.methodTag.equals(record.getMethodTag())){
            return false;
        }
        if(ops.exceptionName!=null && !ops.exceptionName.equals(record.getExceptionName())){
            return false;
        }
        if(ops.content!=null && !record.getMessage().contains(ops.content)){
            return false;
        }
        return true;
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
    /**
     * the filter options:  dir， date( startTime with endTime )， level(level with lowestLevel), main tag，methodTag, exception,content
     */
    public static class FilterOptions{
        public int level ;
        public int lowestLevel ; //all >= level will allow
        public long startTime ;
        public long endTime ;
        public String tag ;
        public String methodTag ;
        public String exceptionName ;
        public String dir ;
        public String content ;
    }

    /**
     *  the log record:
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
