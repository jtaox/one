package me.jtao.yjh.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

import me.jtao.yjh.BaseApplication;
import me.jtao.yjh.R;
import me.jtao.yjh.adapter.YJViewPagerAdapter;
import me.jtao.yjh.bean.AppYjh;
import me.jtao.yjh.ui.activity.EditActivity;
import me.jtao.yjh.ui.activity.MainActivity;
import me.jtao.yjh.ui.activity.ShareMusicActivity;
import me.jtao.yjh.ui.fragment.yj.YJFragment;
import me.jtao.yjh.ui.fragment.yj.YPFragment;
import me.jtao.yjh.ui.fragment.yj.YSFragment;
import me.jtao.yjh.util.ToastUtil;

/**
 * Created by Administrator on 2016/7/25.
 */
public class YJFragmentA extends ABaseFragment {

    private TabLayout tabLayout;
    private ViewPager vp;
    private List<ABaseFragment> list;
    private Toolbar toolbar;
    private FloatingActionsMenu fam_menu;
    private FrameLayout fl_bg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected View contentView() {
        return inflater.inflate(R.layout.fragment_yj, null);
    }

    @Override
    protected void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("One Life");
        toolbar.setTitleTextColor(Color.WHITE);
        activity.setSupportActionBar(toolbar);
        //toolbar.setPadding(0, UIUtils.getStatusBarHeight(), 0, 0);

        fl_bg = (FrameLayout) findViewById(R.id.fl_bg);
        fl_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fam_menu.isExpanded())
                    fam_menu.collapse();
            }
        });

        fam_menu = (FloatingActionsMenu) findViewById(R.id.fam_menu);
        final FloatingActionButton addYj = new FloatingActionButton(BaseApplication.sContext);
        addYj.setTitle("写一句话");
        addYj.setSize(FloatingActionButton.SIZE_MINI);
        addYj.setColorNormalResId(R.color.fab_sub_bg);
        addYj.setIcon(R.drawable.ic_border_color_black_18dp);
        addYj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, EditActivity.class);
//                startActivity(intent);
                startActivityForResult(intent, EditActivity.REQUEST_CODE_YJ);
            }
        });

        FloatingActionButton addYs = new FloatingActionButton(BaseApplication.sContext);
        addYs.setTitle("分享歌曲");
        addYs.setSize(FloatingActionButton.SIZE_MINI);
        addYs.setColorNormalResId(R.color.fab_sub_bg);
        addYs.setIcon(R.drawable.ic_music_note_black_18dp);
        addYs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, ShareMusicActivity.class));
            }
        });


        FloatingActionButton addYp = new FloatingActionButton(BaseApplication.sContext);
        addYp.setTitle("写文章");
        addYp.setSize(FloatingActionButton.SIZE_MINI);
        addYp.setColorNormalResId(R.color.fab_sub_bg);
        addYp.setIcon(R.drawable.ic_format_quote_black_18dp);
        addYp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.makeText("暂未开放");
            }
        });
        fam_menu.addButton(addYj);
        fam_menu.addButton(addYs);
        fam_menu.addButton(addYp);

        fam_menu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                fl_bg.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMenuCollapsed() {
                fl_bg.setVisibility(View.GONE);
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tl_tab);
        tabLayout.setBackgroundResource(R.color.colorPrimary);
        tabLayout.setTabTextColors(Color.WHITE, Color.WHITE);
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);

        vp = (ViewPager) findViewById(R.id.vp_yj);
        vp.setOffscreenPageLimit(2);

        list = new ArrayList<>();
        list.add(new YJFragment().setPageTitle("一句"));
        list.add(new YSFragment().setPageTitle("一首"));
        list.add(new YPFragment().setPageTitle("一篇"));

        vp.setAdapter(new YJViewPagerAdapter(getFragmentManager(), list));
        tabLayout.setupWithViewPager(vp);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_main, menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EditActivity.REQUEST_CODE_YJ){
            list.get(0).onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        fam_menu.collapse();
    }
}
