package xj.property.widget.pullrefreshview.library.listview;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import xj.property.R;


public class RefreshListView extends ListView implements OnScrollListener,
        AdapterView.OnItemClickListener {

    public static final int STATE_PULL_TO_REFRESH = 0; // 下拉刷新
    public static final int STATE_RELEASE_TO_REFRESH = 1; // 松开刷新
    public static final int STATE_REFRESHING = 2; // 正在刷新

    private int mCurrentState = STATE_PULL_TO_REFRESH;

    private int mHeaderHeight;// 头布局的高度
    private int mFooterHeight;// 脚布局高度

    private int startY = -1;

    private View mHeaderView;// 头布局
    private View mFooterView;// 脚布局

    private TextView tvTitle;
    private TextView tvTime;
    private ImageView ivArrow;
    private RotateAnimation animUp;
    private RotateAnimation animDown;
    private ProgressBar pbProgress;

    private OnRefreshListener mListener;// 下拉监听对象

    public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initHeaderView();
        initFooterView();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();
    }

    public RefreshListView(Context context) {
        super(context);
        initHeaderView();
        initFooterView();
    }

    /**
     * 初始化头布局
     */
    private void initHeaderView() {
        mHeaderView = View.inflate(getContext(), R.layout.refresh_header, null);
        tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
        tvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
        ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arrow);
        pbProgress = (ProgressBar) mHeaderView.findViewById(R.id.pb_progress);

        tvTime.setText(getFormatDate());// 设置当前时间

        this.addHeaderView(mHeaderView);// 添加头布局

        mHeaderView.measure(0, 0);// 先手动测量高度
        mHeaderHeight = mHeaderView.getMeasuredHeight();
        // 隐藏头布局
        mHeaderView.setPadding(0, -mHeaderHeight, 0, 0);

        initAnim();
    }

    /**
     * 初始化脚布局
     */
    private void initFooterView() {
        mFooterView = View.inflate(getContext(), R.layout.refresh_footer, null);

        this.addFooterView(mFooterView);// 给listview添加脚布局

        mFooterView.measure(0, 0);// 先手动测量高度
        mFooterHeight = mFooterView.getMeasuredHeight();

        // 隐藏脚布局
        mFooterView.setPadding(0, -mFooterHeight, 0, 0);
        this.setOnScrollListener(this);// 设置滑动监听
    }

    /**
     * 获取格式化后的时间
     *
     * @return
     */
    private String getFormatDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 时间HH如果是大写,是24小时制,如果小写,是12小时制
        String date = format.format(new Date());
        return date;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == -1) {
                    startY = (int) ev.getY();
                }

                if (mCurrentState == STATE_REFRESHING) {// 如果正在刷新,直接跳出, 不执行下面的代码
                    break;
                }

                int endY = (int) ev.getY();
                int dy = endY - startY;

                int firstVisiblePosition = getFirstVisiblePosition();// 获取listview第一个展示的元素位置

                if (dy > 0 && firstVisiblePosition == 0) {// 向下移动
                    int paddingTop = dy - mHeaderHeight;// 获取头布局的padding

                    if (paddingTop < 0 && mCurrentState != STATE_PULL_TO_REFRESH) {// 下拉刷新
                        mCurrentState = STATE_PULL_TO_REFRESH;
                        refreshState();
                    } else if (paddingTop > 0
                            && mCurrentState != STATE_RELEASE_TO_REFRESH) {// 松开刷新
                        mCurrentState = STATE_RELEASE_TO_REFRESH;
                        refreshState();
                    }

                    mHeaderView.setPadding(0, paddingTop, 0, 0);
                    return true;
                }

                break;
            case MotionEvent.ACTION_UP:
                startY = -1;
                if (mCurrentState == STATE_RELEASE_TO_REFRESH) {
                    mCurrentState = STATE_REFRESHING;
                    mHeaderView.setPadding(0, 0, 0, 0);// 完全显示头布局
                    refreshState();
                } else if (mCurrentState == STATE_PULL_TO_REFRESH) {
                    mHeaderView.setPadding(0, -mHeaderHeight, 0, 0);// 隐藏头布局
                }

                break;

            default:
                break;
        }

        return super.onTouchEvent(ev);
    }

    /**
     * 初始化箭头的旋转动画
     */
    private void initAnim() {
        // 初始化箭头向上的动画
        animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animUp.setFillAfter(true);
        animUp.setDuration(200);

        // 初始化箭头向下的动画
        animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animDown.setFillAfter(true);
        animDown.setDuration(200);
    }

    /**
     * 根据当前状态, 更新当前下拉刷新的界面
     */
    private void refreshState() {
        switch (mCurrentState) {
            case STATE_PULL_TO_REFRESH:
                tvTitle.setText("下拉刷新");
                ivArrow.startAnimation(animDown);
                ivArrow.setVisibility(View.VISIBLE);
                pbProgress.setVisibility(View.GONE);
                break;
            case STATE_RELEASE_TO_REFRESH:
                tvTitle.setText("松开刷新");
                ivArrow.startAnimation(animUp);
                ivArrow.setVisibility(View.VISIBLE);
                pbProgress.setVisibility(View.GONE);
                break;
            case STATE_REFRESHING:
                tvTitle.setText("正在刷新...");
                ivArrow.clearAnimation();// 必须先清除动画, 然后才能设置显示还是隐藏
                ivArrow.setVisibility(View.GONE);// 隐藏箭头
                pbProgress.setVisibility(View.VISIBLE);// 展示进度条

                if (mListener != null) {
                    mListener.onRefresh();// 下拉刷新回调
                }
                break;

            default:
                break;
        }
    }

    // pull-to-refresh
    /**
     * 收起下拉刷新的控件, 表示加载已经结束
     *
     * needUpdateTime: 如果加载失败, 是false,表示不需要更新时间
     */
    public void onRefreshComplete(boolean needUpdateTime) {
        mHeaderView.setPadding(0, -mHeaderHeight, 0, 0);// 隐藏头布局
        mCurrentState = STATE_PULL_TO_REFRESH;// 初始化状态

        // 初始化界面
        tvTitle.setText("下拉刷新");
        ivArrow.setVisibility(View.VISIBLE);
        pbProgress.setVisibility(View.GONE);

        if (needUpdateTime) {
            tvTime.setText(getFormatDate());// 更新时间
        }

        System.out.println("隐藏脚布局...");
        mFooterView.setPadding(0, -mFooterHeight, 0, 0);// 收起脚布局
        isLoadMore = false;// 表示当前没有加载更多数据
    }

    /**
     * 设置下拉监听
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    /**
     * 下拉刷新的回调接口
     * @author Kevin
     *
     */
    public interface OnRefreshListener {
        public void onRefresh();// 下拉刷新的回调方法

        public void onLoadMore();// 加载更多数据
    }

    private boolean isLoadMore;// 是否正在加载更多数据

    /**
     * 滑动状态发生变化
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE
                || scrollState == SCROLL_STATE_FLING) {
            int lastVisiblePosition = getLastVisiblePosition();
            if (lastVisiblePosition == getCount() - 1 && !isLoadMore) {// 判断当前展示的最后一个item是不是列表最后一个元素
                System.out.println("到底了...");
                isLoadMore = true;
                // 显示脚布局
                mFooterView.setPadding(0, 0, 0, 0);
                this.setSelection(getCount());// 设置listview的显示位置

                if (mListener != null) {
                    mListener.onLoadMore();
                }
            }
        }
    }

    /**
     * 滑动过程的监听
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
    }

    OnItemClickListener mClickListener;

    @Override
    public void setOnItemClickListener(
            OnItemClickListener listener) {
        mClickListener = listener;
        super.setOnItemClickListener(this);//拦截单击事件,由自己处理
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        //System.out.println("自定义item点击:" + position);
        if(mClickListener!=null) {
            mClickListener.onItemClick(parent, view, position-getHeaderViewsCount(), id);
        }
    }

}
