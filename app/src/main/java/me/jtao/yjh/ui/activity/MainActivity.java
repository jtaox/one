package me.jtao.yjh.ui.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import me.jtao.yjh.API;
import me.jtao.yjh.R;
import me.jtao.yjh.ui.fragment.SettingsFragment;
import me.jtao.yjh.ui.fragment.TPFragmentA;
import me.jtao.yjh.ui.fragment.YJFragmentA;
import me.jtao.yjh.util.ToastUtil;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    /**
     * 导航栏描述
     */
    private TextView tv_desc;
    /**
     * 导航栏头布局，修改背景
     */
    private LinearLayout ll_bg;

    private YJFragmentA yjFragment;
    private TPFragmentA tpFragment;
    private SettingsFragment settingsFragment;

    private android.app.FragmentManager fm;

    private Fragment currentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        initView();
    }

    private void initView() {
        // 导航相关
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                showFragment(item.getItemId());
                item.setChecked(true);
                drawerLayout.closeDrawers();
                return false;
            }
        });
        //View view = View.inflate(this, R.layout.header_content, null);
        //view.setPadding(0, UIUtils.getStatusBarHeight(), 0, 0);
        //navigationView.addHeaderView(view);

        // fragment相关
        fm = getFragmentManager();
        showFragment(R.id.menu_nav_yj);
    }

    private void findView() {
        navigationView = (NavigationView) findViewById(R.id.navigation);
        tv_desc = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_img_desc);
        ll_bg = (LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.ll_head_bg);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
    }

    public void setTxtDesc(String desc) {
        tv_desc.setText(desc);
        ToastUtil.makeText("修改图片信息成功");
    }

    private void showFragment(int menuId) {

        FragmentTransaction transaction = fm.beginTransaction();
        if (currentFragment != null)
            transaction.hide(currentFragment);
        switch (menuId) {
            case R.id.menu_nav_tp:
                if (tpFragment == null) {
                    tpFragment = new TPFragmentA();
                    transaction.add(R.id.fl_container, tpFragment);
                } else {
                    transaction.show(tpFragment);
                }
                currentFragment = tpFragment;
                setTitle("图片");
                break;
            case R.id.menu_nav_yj:
                if (yjFragment == null) {
                    yjFragment = new YJFragmentA();
                    transaction.add(R.id.fl_container, yjFragment);
                } else {
                    transaction.show(yjFragment);
                }
                currentFragment = yjFragment;
                setTitle("一句");
                break;
            case R.id.menu_nav_set:
                if(settingsFragment == null){
                    settingsFragment = new SettingsFragment();
                    transaction.add(R.id.fl_container, settingsFragment);
                } else {
                    transaction.show(settingsFragment);
                    currentFragment = settingsFragment;
                    setTitle("设置");
                }
                break;

        }
        transaction.commit();
    }

    private long pressTime;

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(navigationView)){
            drawerLayout.closeDrawers();
            return;
        }
        if (!(currentFragment instanceof YJFragmentA)) {
            showFragment(R.id.menu_nav_yj);
        } else {
            if((System.currentTimeMillis() - pressTime) < 1500) {
                super.onBackPressed();
            } else {
                pressTime = System.currentTimeMillis();
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 向下一层传递
        yjFragment.onActivityResult(requestCode, resultCode, data);

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



    /* *//**
     * 设置toolbar 标题
     * @param title
     *//*
    public void setTitle(String title){
        toolbar.setTitle(title);
    }

    *//**
     * 设置toolbar标题
     * @param resId
     *//*
    public void setTitle(int resId){
        toolbar.setTitle(resId);
    }


    private boolean toolbarIsVis = true;
    *//**
     * toolbar隐藏动画
     * @param duration
     *//*
    public void startToolbarHideAnim(long duration){
        if(toolbarIsVis){
            ObjectAnimator.ofFloat(toolbar, "translationY", 0, -toolbarSize - UIUtils.getStatusBarHeight())
                    .setDuration(duration)
                    .start();
            toolbarIsVis = false;
        }
    }

    public void startToolbarVisibileAnim(long duration){
        if(!toolbarIsVis){
            ObjectAnimator.ofFloat(toolbar, "translationY", -toolbarSize - UIUtils.getStatusBarHeight(), 0)
                    .setDuration(duration)
                    .start();
            toolbarIsVis = true;

        }
    }*/

}
