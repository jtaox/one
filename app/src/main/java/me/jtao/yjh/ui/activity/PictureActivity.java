package me.jtao.yjh.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

import me.jtao.yjh.R;
import me.jtao.yjh.util.ToastUtil;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by jiangtao on 2016/8/10.
 */
public class PictureActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private ImageView ivPic;
    private PhotoViewAttacher attacher;

    public static Intent newIntent(Context context, String url) {
        Intent intent = new Intent();
        intent.putExtra("url", url);
        intent.setClass(context, PictureActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        initView();
        initData();
    }

    private void initData() {
        Picasso.with(this)
                .load(getIntent().getStringExtra("url"))
                .into(ivPic);
        // 支持放大缩小
        attacher = new PhotoViewAttacher(ivPic);
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("查看图片");
        toolbar.setAlpha(0.7f);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(this);
        /*getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        ivPic = (ImageView) findViewById(R.id.iv_picture);
        ivPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbarAnim();
            }
        });
        ivPic.postDelayed(new Runnable() {
            @Override
            public void run() {
                toolbarAnim();
            }
        }, 1500);
    }

    private boolean isHidden = false;

    private void toolbarAnim() {
        toolbar.animate()
                .translationY(isHidden ? 0 : -toolbar.getHeight())
                .setInterpolator(new DecelerateInterpolator(2))
                .start();
        isHidden = !isHidden;
    }


    @Override
    public void onClick(View view) {
        onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}

