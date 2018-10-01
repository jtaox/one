package me.jtao.yjh.util;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import me.jtao.yjh.BaseApplication;

/**
 * Created by jiangtao on 2016/7/27.
 */
public class UIUtils {
    public static float dip2px(int dip){
        float density = getContext().getResources().getDisplayMetrics().density;
        float px = dip * density;
        return px;
    }

    public static int screenWidth(){
        return getContext().getResources().getDisplayMetrics().widthPixels;
    }

    public static Context getContext(){
        return BaseApplication.sContext;
    }

    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static View inflate(int id){
        return View.inflate(BaseApplication.sContext, id, null);
    }

    public static String getString(int id){
        return BaseApplication.sContext.getResources().getString(id);
    }

    /** 工具方法 获取上层viewpager */
    public static View getVpParent(View view){
        View parent = (View) view.getParent();
        if(parent instanceof ViewPager){
            return parent;
        } else{
            return getVpParent(parent);
        }
    }

    public static String getProgressText(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));
        double minute = calendar.get(Calendar.MINUTE);
        double second = calendar.get(Calendar.SECOND);

        DecimalFormat format = new DecimalFormat("00");
        return format.format(minute) + ":" + format.format(second);
    }

}
