package me.jtao.yjh.ui.activity;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.Random;

import me.jtao.yjh.R;
import me.jtao.yjh.bean.AppArticle;
import me.jtao.yjh.util.UIUtils;
import me.jtao.yjh.widget.CompatCollapsingToolbarLayout;

/**
 * Created by jiangtao on 2016/8/19.
 */
public class ArticleDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvTitle, tvContent, tvDesc, tvAuthor;
    private AppArticle article;
    private CompatCollapsingToolbarLayout cooCollapsingToolbarLayout;
    private LinearLayout llContent;
    private AppBarLayout appbarLayout;
    private CoordinatorLayout coordinatorLayout;
    private Toolbar toolbar;
    private ImageView ivMore;
    private int line3Height;
    private int tvDescHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        findView();
        initData();
        setListener();
    }


    private void setListener() {
        ivMore.setOnClickListener(this);
        appbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset <= -llContent.getHeight() / 2) {
                    cooCollapsingToolbarLayout.setTitle(article.title);
                } else {
                    cooCollapsingToolbarLayout.setTitle("");
                }
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initData() {
        article = getIntent().getParcelableExtra("article");
        tvAuthor.setText(String.format(getString(R.string.share), article.author));
        tvContent.setText(article.content);
        tvTitle.setText(article.title);
        descView();
        String[] colors = getResources().getStringArray(R.array.colors);
        int random = new Random().nextInt(colors.length);
        int color = Color.parseColor(colors[random]);
        cooCollapsingToolbarLayout.setContentScrimColor(color);
        cooCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        appbarLayout.setBackgroundColor(color);

        // 这一步是为了状态栏透明
        coordinatorLayout.setBackgroundColor(color);
        cooCollapsingToolbarLayout.setStatusBarScrimColor(color);
        toolbar.setBackgroundColor(color);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.inflateMenu(R.menu.article_menus);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        setSupportActionBar(toolbar);
        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.article_menus,menu);
        return true;
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

    private void descView() {

        //tvDesc.setText(article.des);
        final TextView tv = new TextView(this);
        tv.setMaxLines(3);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        tv.setText(article.des);

        // 测量高度
        int heightSpec = View.MeasureSpec.makeMeasureSpec((int) UIUtils.dip2px(230), View.MeasureSpec.AT_MOST);
        int widthSpec = View.MeasureSpec.makeMeasureSpec(UIUtils.screenWidth(), View.MeasureSpec.AT_MOST);
        tv.measure(widthSpec, heightSpec);

        // 获取高度
        line3Height = tv.getMeasuredHeight();
        tvDesc.setText(article.des);

        tvDesc.post(new Runnable() {

            @Override
            public void run() {
                tvDescHeight = tvDesc.getHeight();
                if(tvDescHeight> line3Height){
                    tvDesc.setMaxLines(3);
                    tvDesc.setEllipsize(TextUtils.TruncateAt.END);
                    ivMore.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void findView() {
        tvAuthor = (TextView) findViewById(R.id.tv_author);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvContent = (TextView) findViewById(R.id.tv_content);
        tvDesc = (TextView) findViewById(R.id.tv_desc);
        ivMore = (ImageView) findViewById(R.id.iv_more);
        cooCollapsingToolbarLayout = (CompatCollapsingToolbarLayout) findViewById(R.id.ctl_colltoolbar);
        appbarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
        llContent = (LinearLayout) findViewById(R.id.ll_content);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coorlayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }
    private boolean isClose = true;
    @Override
    public void onClick(View view) {
        final ViewGroup.LayoutParams params = tvDesc.getLayoutParams();
        if(view.getId() == R.id.iv_more){
            if(isClose) { // 关闭状态
                tvDesc.setMaxLines(100);
                tvDesc.getLayoutParams().height = line3Height;
                ValueAnimator animator = ValueAnimator.ofInt(line3Height, tvDescHeight);
                animator.setDuration(500);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int i = (int) valueAnimator.getAnimatedValue();
                        params.height = i;
                        tvDesc.setLayoutParams(params);
                    }
                });
                animator.start();
                isClose = false;
                ivMore.animate()
                        .rotation(180)
                        .setDuration(200)
                        .start();
            } else { // 开启状态

                ValueAnimator animator = ValueAnimator.ofInt(tvDesc.getLayoutParams().height, line3Height)
                        .setDuration(500);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int i = (int) valueAnimator.getAnimatedValue();
                        params.height = i;
                        tvDesc.setLayoutParams(params);
                    }
                });
                animator.start();
                isClose = true;
                ivMore.animate()
                        .rotationBy(180)
                        .setDuration(200)
                        .start();
            }

        } else {
        }
    }
}
