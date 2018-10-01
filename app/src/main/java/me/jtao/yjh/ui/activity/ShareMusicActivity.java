package me.jtao.yjh.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import me.jtao.yjh.API;
import me.jtao.yjh.R;
import me.jtao.yjh.bean.AppResult;
import me.jtao.yjh.bean.Music;
import me.jtao.yjh.bean.QueryAll;
import me.jtao.yjh.bean.SingerInfo;
import me.jtao.yjh.manage.OkHttpClientManager;
import me.jtao.yjh.util.SPUtil;
import me.jtao.yjh.util.ToastUtil;

/**
 * Created by jiangtao on 2016/8/22.
 */
public class ShareMusicActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

    private Toolbar toolbar;
    private TextView tvMusicName, tvMusicSinger;
    private EditText etDesc, etAuthor;
    private CircleImageView ivBg;
    private Button btnMusicControl;
    private QueryAll.SongListBean song;
    private MaterialDialog dialog;
    private SPUtil util;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_music);
        findView();
        init();
    }

    private void init() {
        util = SPUtil.getInstance();
        if(!TextUtils.isEmpty(util.getMusicDesc()))
            etDesc.setText(util.getMusicDesc());
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("分享音乐");

        initAuthor();

    }

    private void initAuthor() {
        etAuthor.setText(util.getNickname());
    }

    private void findView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvMusicName = (TextView) findViewById(R.id.tv_music_name);
        tvMusicSinger = (TextView) findViewById(R.id.tv_singer);
        ivBg = (CircleImageView) findViewById(R.id.iv_music_bg);
        btnMusicControl = (Button) findViewById(R.id.btn_music_control);
        etAuthor = (EditText) findViewById(R.id.et_author);
        etDesc = (EditText) findViewById(R.id.et_desc);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveContent();
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        saveContent();
        super.onBackPressed();
    }

    private void saveContent() {
        if(!TextUtils.isEmpty(etDesc.getText().toString()))
            util.setMusicDesc(etDesc.getText().toString());
        else
            util.setMusicDesc("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_music, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.menu_music_add) {
            // 添加音乐
            Intent intent = new Intent();
            intent.setClass(this, QueryMusicActivity.class);
            startActivityForResult(intent, 0);
        } else if (item.getItemId() == R.id.menu_music_share) {
            sendData();
        }
        return false;
    }

    private void sendData() {
        if (song == null) {
            ToastUtil.makeText("请选择要分享的音乐");
            return;
        }
        String desc = etDesc.getText().toString();
        String author = etAuthor.getText().toString();
        if (TextUtils.isEmpty(author) || TextUtils.isEmpty(desc)) {
            ToastUtil.makeText("推荐理由或推荐者不能为空");
            return;
        }
        if(!author.equals(util.getNickname()))
            util.setNickname(author);
        if (dialog == null)
            dialog = new MaterialDialog.Builder(this)
                    .content("正在提交中...")
                    .progress(true, 0)
                    .build();
        dialog.show();
        Map<String, String> map = new HashMap<>();
        map.put(API.URL_APP_SEND_PARAM_DESC, desc);
        map.put(API.URL_APP_SEND_PARAM_AUTHOR, author);
        map.put(API.URL_APP_SEND_PARAM_SONGID, song.song_id);
        OkHttpClientManager.postAsyn(API.URL_APP_SEND_MUSIC, new OkHttpClientManager.ResultCallback<AppResult>() {
            @Override
            public void onError(Request request, Exception e) {
                ToastUtil.makeText(e.getMessage());
            }

            @Override
            public void onResponse(AppResult response) {
                dialog.dismiss();
                if (response.state.equals("200")) {
                    ToastUtil.makeText("提交成功");
                    finish();
                } else {
                    ToastUtil.makeText(response.data.result);
                }
            }
        }, map);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 0)
            return;
        if (resultCode != RESULT_OK)
            return;
        song = data.getParcelableExtra("song_list");
        refreshView();
    }

    private void refreshView() {
        tvMusicSinger.setText(Html.fromHtml(song.author));
        tvMusicName.setText(Html.fromHtml(song.title));

//        ToastUtil.makeText(Thread.currentThread().getName());

        // 通过songid 获取歌曲详细信息
        new AsyncTask<String, Void, String>() {

            public String picUrl;

            @Override
            protected String doInBackground(String... strings) {
                String songid = strings[0];
                try {
                    // 同步访问
                    String result = OkHttpClientManager.getAsString(MessageFormat.format(API.URL_APP_MUSIC_INFO,
                            songid));
                    // to gson
                    Gson gson = new Gson();
                    Music response = gson.fromJson(result, Music.class);
                    picUrl = response.songinfo.artist_500_500;
                    if (TextUtils.isEmpty(picUrl)) {
                        Gson singerinfo = new Gson();
                        SingerInfo singer = singerinfo.fromJson(OkHttpClientManager.getAsString(MessageFormat.format(API.URL_APP_SINGER_INFO,
                                response.songinfo.ting_uid)), SingerInfo.class);
                        picUrl = singer.avatar_middle;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return picUrl;
            }

            @Override
            protected void onPostExecute(String picUrl) {
//                ToastUtil.makeText(Thread.currentThread().getName());
                if (!TextUtils.isEmpty(picUrl))
                    Picasso.with(ShareMusicActivity.this)
                            .load(picUrl)
                            .into(ivBg);

            }
        }.execute(song.song_id);
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
