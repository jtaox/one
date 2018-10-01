package me.jtao.yjh.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;

import me.jtao.yjh.R;
import me.jtao.yjh.bean.AppResult;
import me.jtao.yjh.bean.AppYjh;
import me.jtao.yjh.presenter.SendDataPresenter;
import me.jtao.yjh.ui.view.IUserView;
import me.jtao.yjh.util.SPUtil;
import me.jtao.yjh.util.ToastUtil;

/**
 * Created by jiangtao on 2016/8/22.
 */
public class EditActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, IUserView, View.OnClickListener {

    public static final int REQUEST_CODE_YJ = 0;
    public static final int REQUEST_CODE_YS = 1;
    public static final int REQUEST_CODE_YP = 2;


    private TextInputLayout tilAuthor;
    private TextInputLayout tilContent;
    private Toolbar toolbar;
    private EditText etAuthor, etYjh;
    private InputMethodManager im;
    private SPUtil util;
    private SendDataPresenter presenter;
    private MaterialDialog dialog;
    private Button btnAdd;
    private ImageView iv_img;
    private String imagePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        findView();
        init();

    }

    private void init() {
       // tilAuthor.setError("sss");
//        tilAuthor.set


        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("写下一句话");
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(this);
        im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        util = SPUtil.getInstance();
        presenter = new SendDataPresenter(this);
        // 设置用户名
        etAuthor.setText(util.getNickname());

        String content = util.getYjhContent();
        if(!TextUtils.isEmpty(content))
            etYjh.setText(content);
        dialog = new MaterialDialog.Builder(this)
                .content("正在提交中...")
                .progress(true, 0)
                .build();

        btnAdd.setOnClickListener(this);


    }

    private void findView() {
        tilAuthor = (TextInputLayout) findViewById(R.id.til_author);
        tilContent = (TextInputLayout) findViewById(R.id.til_content);
        etAuthor = (EditText) findViewById(R.id.et_author);
        etYjh = (EditText) findViewById(R.id.et_yjh);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnAdd = (Button) findViewById(R.id.btn_add);
        iv_img = (ImageView) findViewById(R.id.iv_img);
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
        if(!TextUtils.isEmpty(etYjh.getText().toString()))
            util.setYjhContent(etYjh.getText().toString());
        else
            util.setYjhContent("");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_edit_yjh, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if(item.getItemId() == R.id.menu_edit_send){
            // 如果键盘显示，先隐藏键盘
            if(im.isActive())
                im.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            AppYjh yjh = new AppYjh();
            yjh.author = etAuthor.getText().toString();
            yjh.content = etYjh.getText().toString();
            yjh.imgpath = this.imagePath;
            if(TextUtils.isEmpty(yjh.author) || TextUtils.isEmpty(yjh.content)){
                ToastUtil.makeText("内容或作者不能为空");
                return true;
            }

            if(!yjh.author.equals(util.getNickname()))
                util.setNickname(yjh.author);

            presenter.sendYJH(yjh);
        }
        return true;
    }

    @Override
    public void addDataSuccess() {
        dialog.dismiss();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void addDataFailed(AppResult response) {
        dialog.dismiss();
        if(response != null)
            ToastUtil.makeText(response.data.result);

    }

    @Override
    public void preSendData() {
        // 添加数据前，显示progressdialog
        dialog.show();
    }

    @Override
    public void unknow(String s) {
        ToastUtil.makeText(s);
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

    @Override
    public void onClick(View view) {
        Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
        getAlbum.setType("image/*");
        startActivityForResult(getAlbum,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK)
            return;
        if(requestCode != 0)
            return;

        Uri selectedImage =  data.getData();
        if(iv_img.getVisibility() == View.GONE)
            iv_img.setVisibility(View.VISIBLE);
        Bitmap bitmap = decodeSampledBitmapFromResource(selectedImage.getPath(), 100, 100);
        iv_img.setImageBitmap(bitmap);
        this.imagePath = selectedImage.getPath();

    }



    ////////////////////////////////////
    public int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public Bitmap decodeSampledBitmapFromResource(String res, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeResource(res, resId, options);
        BitmapFactory.decodeFile(res, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(res, options);
    }

}
