package me.jtao.yjh;

import android.app.Application;
import android.content.Context;

/**
 * Created by jiangtao on 2016/7/27.
 */
public class BaseApplication extends Application {
    public static Context sContext;
    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
    }
}
