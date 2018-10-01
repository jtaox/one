package me.jtao.yjh.util;

import android.view.Gravity;
import android.widget.Toast;

import me.jtao.yjh.BaseApplication;
import me.jtao.yjh.ui.fragment.TPFragmentA;

/**
 * Created by jiangtao on 2016/8/3.
 */
public class ToastUtil {
    private static Toast toast;
    public static void makeText(String text){
        if(toast == null){
            toast = Toast.makeText(BaseApplication.sContext, text, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(text);
        }
        toast.show();
    }
}
