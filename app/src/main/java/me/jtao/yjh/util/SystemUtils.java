package me.jtao.yjh.util;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.jtao.yjh.BaseApplication;
import me.jtao.yjh.R;

/**
 * Created by jiangtao on 2016/8/5.
 */
public class SystemUtils {

    public static void setClipboard(String str, View view){
        ClipboardManager cm = (ClipboardManager) BaseApplication.sContext.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(str);
        snackBar(view, "已复制到剪切板");
    }

    public static void snackBar(View view, String str){
        Snackbar bar = Snackbar.make(view, str, Snackbar.LENGTH_SHORT)
                .setAction("知道了", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }).setActionTextColor(Color.WHITE);
      //  ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(color);
        ((TextView)bar.getView().findViewById(android.support.design.R.id.snackbar_text))
                .setTextColor(Color.WHITE);
        bar.show();

    }



    public static String unix2Str(long timestamp){
        timestamp *= 1000;
        if(DateUtils.isToday(timestamp)){
        SimpleDateFormat hmformat = new SimpleDateFormat("HH:mm");
            return hmformat.format(new Date(timestamp));
        }
        SimpleDateFormat ymdformat = new SimpleDateFormat("MM-dd HH:mm");
        return ymdformat.format(new Date(timestamp));
    }
}
