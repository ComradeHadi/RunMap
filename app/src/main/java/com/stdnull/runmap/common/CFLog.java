package com.stdnull.runmap.common;

import android.util.Log;

/**
 * Created by chen on 2017/1/19.
 */

public class CFLog {
    public static final boolean DEBUG = true;
    public static void e(String tag, String msg){
        if(DEBUG){
            Log.e(tag,msg);
        }
    }
    public static void d(String tag, String msg){
        if(DEBUG){
            Log.d(tag,msg);
        }
    }

    public static void i(String tag, String msg){
        if(DEBUG){
            Log.i(tag,msg);
        }
    }

}
