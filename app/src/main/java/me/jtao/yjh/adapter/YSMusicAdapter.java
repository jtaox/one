package me.jtao.yjh.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.jtao.yjh.BaseApplication;
import me.jtao.yjh.R;
import me.jtao.yjh.bean.MusicItem;
import me.jtao.yjh.util.SystemUtils;
import me.jtao.yjh.util.UIUtils;
import me.jtao.yjh.widget.StereoView;

/**
 * Created by jiangtao on 2016/8/10.
 */
public class YSMusicAdapter extends RecyclerView.Adapter<YSMusicAdapter.MyViewHolder> {


    public static final int ITEM_PATE_LRC = 0;
    public static final int ITEM_PATE_MAIN = 1;
    public static final int ITEM_PATE_CONTROLL = 2;

    private XRecyclerView recyclerView;
    private List<MusicItem> list;
    private OnControllClickListener listener;
    private int currentPage = ITEM_PATE_MAIN;
    private OnPlayViewVisableListener visableListener;


    private int isPlayPos = -1;

    public YSMusicAdapter(List<MusicItem> list, XRecyclerView recyclerView) {
        this.list = list;
        this.recyclerView = recyclerView;
    }


    public void setList(List<MusicItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setListener(OnControllClickListener listener) {
        this.listener = listener;
    }

    public void setVisableListener(OnPlayViewVisableListener visableListener) {
        this.visableListener = visableListener;
    }

    public List<MusicItem> getList() {
        return list;
    }

    @Override
    public YSMusicAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = UIUtils.inflate(R.layout.item_ys_fragment);
        final View itemview = LayoutInflater.from(BaseApplication.sContext).inflate(R.layout.item_ys_fragment, parent, false);
        final MyViewHolder holder = new MyViewHolder(itemview);

        holder.btncontroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onClick(itemview, holder.getLayoutPosition());
            }
        });

        holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(listener != null)
                    listener.onSeekBarProgressChangedListener(seekBar, i, b, holder.getLayoutPosition());
                Log.d("Sea", "int " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(YSMusicAdapter.MyViewHolder holder, int position) {
        final int pos = position;
        Log.d("Sea", "正在播放 :" + this.isPlayPos);
        MusicItem item = list.get(position);
        holder.tvTitle.setText(item.title);
        holder.tvProposal.setText(item.desc);
        holder.tvSinger.setText(item.singer);
        holder.tvAuthor.setText(item.author);
        holder.tvTime.setText(SystemUtils.unix2Str(item.time));
        if (!TextUtils.isEmpty(item.artist))
            Picasso.with(BaseApplication.sContext)
                    .load(item.artist)
                    .error(R.mipmap.ic_launcher)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(holder.ivBg);
        holder.view.setTag(list.get(pos));

        if(isPlayPos == position){
            holder.startAnim();
            changePage(holder);
            Log.d("Sea", "当前pos" + position +",正在播放 " + isPlayPos + ", 开启动画，page" + currentPage);
            // 回调fragment
            if(visableListener != null)
                visableListener.onPlayViewVisableListener(holder);
        } else {
            if(holder.isRunning())
                holder.stopAnim();
            // 没有点击的全部默认页面
            holder.sv_stereo.setItem(1);
        }

        /*if (holder.ivBg.getTag() != null && (Boolean) holder.ivBg.getTag()) {
            holder.startAnim();
        } else {
            if (holder.isRunning()) {
                holder.stopAnim();
            }
        }*/
        /*MyViewHolder animHolder = (MyViewHolder) recyclerView.getTag();
        if(animHolder != null && animHolder == holder){
            holder.startAnim();
        } else {
            if(holder.isRunning())
                holder.stopAnim();
        }*/

    }


    private void changePage(MyViewHolder holder) {
        // 当前为正在播放，根据之前选择的状态调整
        holder.sv_stereo.setItem(currentPage);
    }

    public void setIsPlayPos(int pos) {
        Log.d("Sea", "setIsPlayPos调用:" + pos);
        int i = pos - 1;
        this.isPlayPos = i;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void append(List<MusicItem> data) {
        list.addAll(data);
        notifyDataSetChanged();
    }


    public static interface OnControllClickListener {
        void onClick(View view, int position);
        void onSeekBarProgressChangedListener(SeekBar seekBar, int i, boolean b, int layoutPosition);
    }

    public static interface OnPlayViewVisableListener{
        void onPlayViewVisableListener(MyViewHolder holder);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public View view;
        public TextView tvSinger, tvTitle, tvProposal, tvAuthor, tvTime,
                tvMax, tvProgress;
        public CircleImageView ivBg;
        public Button btncontroll;
        public StereoView sv_stereo;
        private ObjectAnimator animator;
        public SeekBar seekBar;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            initView();
            initAnim();
        }

        private void initView() {
            tvSinger = (TextView) view.findViewById(R.id.tv_singer);
            tvProposal = (TextView) view.findViewById(R.id.tv_reason);
            tvTitle = (TextView) view.findViewById(R.id.tv_music_name);
            ivBg = (CircleImageView) view.findViewById(R.id.iv_music_bg);
            btncontroll = (Button) view.findViewById(R.id.btn_music_control);
            tvAuthor = (TextView) view.findViewById(R.id.tv_author);
            tvTime = (TextView) view.findViewById(R.id.tv_time);
            sv_stereo = (StereoView) view.findViewById(R.id.sv_stereo);
            tvMax = (TextView) view.findViewById(R.id.tv_max);
            tvProgress = (TextView) view.findViewById(R.id.tv_progress);
            seekBar = (SeekBar) view.findViewById(R.id.sb_seekbar);
            // 避免seekbar和viewpager的滑动冲突
            seekBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    ((ViewParent)UIUtils.getVpParent(seekBar)).requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });
        }

        public void startAnim() {
            animator.setTarget(ivBg);
            animator.start();
        }

        public boolean isRunning() {
            return animator.isRunning();
        }

        public void stopAnim() {
            if (isRunning()) {
                animator.end();
            }
        }

        private void initAnim() {
            animator = new ObjectAnimator();
            animator.setPropertyName("rotation");
            animator.setFloatValues(0.0f, 360f);
            // 不停顿
            animator.setInterpolator(new LinearInterpolator());
            animator.setDuration(3000);
            // 无限循环
            animator.setRepeatCount(-1);

        }
    }
}
