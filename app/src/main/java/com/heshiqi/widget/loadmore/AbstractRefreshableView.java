package com.heshiqi.widget.loadmore;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import com.heshiqi.widget.R;


/**
 * Created by heshiqi on 17/9/15.
 *

 * 下拉刷新控件 原有的下拉刷新控件不支持RecyclerView，为了保持下拉刷新风格的一致性，参考{@link }自定义支持RecyclerView
 */

public abstract class AbstractRefreshableView<T extends ViewGroup, A/*adapter*/> extends LinearLayout {

    private final static String TAG = AbstractRefreshableView.class.getSimpleName();

    private T mContentView;

    /**
     * 下拉刷新时，出现的Header View
     */
    protected AbsRefreshableHeaderView mRefreshableHeader;
    /**
     * AbsListView是否支持下拉刷新，默认支持
     */
    protected boolean mIsPullRefreshEabled = true;

    /**
     * 下拉刷新时，用来维护上拉刷新View的状态
     */
    protected AbsRefreshableHeaderView.HeaderState mHeaderState = AbsRefreshableHeaderView.HeaderState.STATE_RESET;

    /**
     * 列表数据状态Adapter，主要用来获得当前列表数据的一些状态
     */
    private ListDataStatusAdapter mListDataStatusAdapter;

    /**
     * 列表数据状态Adapter，主要用来获得当前列表数据的一些状态
     */
    public interface ListDataStatusAdapter {
        public boolean isDataFinish();
    }

    /**
     * 刷新监听器，主要用来监听刷新/加载更多的事件
     */
    protected PullToRefreshCallback mPullToRefreshCallback;

    /**
     * 是否处于拖动状态
     */
    protected boolean mIsBeingDragged = false;
    protected float mLastMotionY;
    protected float mInitialMotionY;
    protected float mCurMotionY;

    /**
     * 在自定义的ListView中拖动一定距离后,更新状态，默认值250
     */
    protected int mRefreshTransitionHeight = 250;
    /**
     * 判断滑动状态的 高度
     */
    protected int mCurrentRefreshTransitionHeight = mRefreshTransitionHeight;
    /**
     * 执行下拉操作时,最小的手势位移值.（小于这个值,则不会触发下拉刷新的操作）单位dp
     */
    protected int mMinPullActionShift = 5;

    protected final static int REFRESH_TRANSITION_DIVIDER = 8;
    protected static float FRICTION = 2.0f;
    /**
     * 屏幕下方最后一个可见的位置
     */
    private int mLastVisibleIndex = 0;

    /**
     * 用于处理平滑滚动
     */
    public static final int SMOOTH_SCROLL_DURATION_MS = 300;
    private SmoothScrollRunnable mSmoothScrollRunnable;
    private Interpolator mScrollAnimationInterpolator = new DecelerateInterpolator();

    /**
     * 在下拉刷新时，需要动态计算当前的下拉进度。此处为一个偏移量，为的是在刚开始下拉的一段距离内，仍然认为进度为0
     */
    private int mDefaultProgressOffset;

    public AbstractRefreshableView(Context context) {
        super(context);
        init(context, null);
    }

    public AbstractRefreshableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AbstractRefreshableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * 布局Layout初始化
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        mDefaultProgressOffset = dpToPxInt(getContext(), 50);
        setHeightForScreenHeight(context);

//        TypedArray typedArray = context.obtainStyledAttributes(attrs, com.autohome.commonlib.R.styleable.RefreshableView);
        setOrientation(VERTICAL);
        // Step 1. add header，默认的下拉控件是油表的样式
        mRefreshableHeader = getHeaderView(context);
        mRefreshableHeader.setId(R.id.attention_refresh_headerview_id);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mRefreshTransitionHeight);
        lp.topMargin = -mRefreshTransitionHeight;
        addView(mRefreshableHeader, lp);
        // Step 2. create absListView
        mContentView = createListView(context, attrs);
        mContentView.setId(R.id.attention_refresh_recycleview_id);
        // Step 4. add absListView
        LayoutParams lp2 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mContentView, lp2);

//        typedArray.recycle();
    }

    protected void setHeightForScreenHeight(Context context) {
        mRefreshTransitionHeight = getScreenHeight(context) / REFRESH_TRANSITION_DIVIDER;

        mCurrentRefreshTransitionHeight = mRefreshTransitionHeight;
    }

    public static int getScreenHeight(Context context) {
       return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 设置下拉刷新时的控件样式
     *
     * @param header
     */
    public void setRefreshableHeaderView(AbsRefreshableHeaderView header) {
        if (header != null) {
            removeView(mRefreshableHeader);
            mRefreshableHeader = header;
            mRefreshableHeader.setId(R.id.attention_refresh_headerview_id);
            // 重新添加Header控件
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mRefreshTransitionHeight);
            lp.topMargin = -mRefreshTransitionHeight;
            addView(mRefreshableHeader, 0, lp);
        }
    }

    /**
     * 返回当前的下拉刷新控件中，所使用HeaderView对象
     *
     * @return header
     */
    public AbsRefreshableHeaderView getRefreshableHeaderView() {
        return mRefreshableHeader;
    }

    /**
     * 获取头部view
     *
     * @param context
     * @return
     */
    protected AbsRefreshableHeaderView getHeaderView(Context context) {
        return new RingAnimHeaderView(context);
    }

    /**
     * 创建ListView，支持多种列表如ListView，GridView等
     *
     * @param context
     * @param attrs
     * @return
     */
    protected abstract T createListView(Context context, AttributeSet attrs);

    /**
     * 设置Adapter适配器
     *
     * @param adapter 适配器
     */
    public abstract void setAdapter(A adapter);

    /**
     * 获取Adapter适配器
     *
     * @return
     */
    public abstract A getAdapter();

    /**
     * 设置选中的ListView项
     *
     * @param position
     */
    abstract void setSelection(int position);

    /**
     * 获取 AbsListView
     *
     * @return
     */
    public T getListView() {
        return mContentView;
    }

    /**
     * 触发ListView的界面刷新
     */
    public void startRefreshing() {
        startRefreshing(false);
    }

    /**
     * 触发ListView的界面刷新
     *
     * @param isUserManual 是否为用户手动下拉，导致触发的刷新行为
     */
    public void startRefreshing(boolean isUserManual) {
        if (mIsPullRefreshEabled) {
            mHeaderState = AbsRefreshableHeaderView.HeaderState.STATE_REFRESHING;
            mRefreshableHeader.onState(mHeaderState);
            smoothScrollTo(-mRefreshTransitionHeight, SMOOTH_SCROLL_DURATION_MS);
            if (mPullToRefreshCallback != null) {
                mPullToRefreshCallback.onPullToRefresh(isUserManual);
            }
        }
    }

    /**
     * 刷新成功时，外部需调用此方法通知RefreshabeView去停止UI的刷新
     */
    public void onRefreshComplete() {
        smoothScrollTo(0);
        mHeaderState = AbsRefreshableHeaderView.HeaderState.STATE_RESET;
        mRefreshableHeader.onState(mHeaderState);
    }

    /**
     * 控制RefreshView的滑动
     *
     * @param scrollValue 滑动距离
     */
    private void smoothScrollTo(int scrollValue) {
        smoothScrollTo(scrollValue, SMOOTH_SCROLL_DURATION_MS);
    }

    /**
     * 用于设置平滑滚动，可根据不同情况，添加不同的Interpolator
     */
    private final class SmoothScrollRunnable implements Runnable {
        private final Interpolator mInterpolator;
        private final int mScrollToY;
        private final int mScrollFromY;
        private final long mDuration;

        private boolean mContinueRunning = true;
        private long mStartTime = -1;
        private int mCurrentY = -1;

        public SmoothScrollRunnable(int fromY, int toY, long duration) {
            mScrollFromY = fromY;
            mScrollToY = toY;
            mInterpolator = mScrollAnimationInterpolator;
            mDuration = duration;
        }

        @Override
        public void run() {
            if (mStartTime == -1) {
                mStartTime = System.currentTimeMillis();
            } else {
                long normalizedTime = (1000 * (System.currentTimeMillis() - mStartTime)) / mDuration;
                normalizedTime = Math.max(Math.min(normalizedTime, 1000), 0);

                final int deltaY = Math.round((mScrollFromY - mScrollToY)
                        * mInterpolator.getInterpolation(normalizedTime / 1000f));
                mCurrentY = mScrollFromY - deltaY;
                AbstractRefreshableView.this.scrollTo(0, mCurrentY);
            }

            // 如果还没有滚动到特定的位置，那么，就继续滚动
            if (mContinueRunning && mScrollToY != mCurrentY) {
                postDelayed(this, 16);
            }
            setCurrentScrollY(mCurrentY);
        }

        public void stop() {
            mContinueRunning = false;
            removeCallbacks(this);
        }
    }

    /**
     * 传出 当前滚动的Y值
     *
     * @param currentY
     */
    protected void setCurrentScrollY(int currentY) {
    }

    /**
     * 控制RefreshView的滑动
     *
     * @param newScrollValue 滑动距离
     * @param duration       持续时间
     */
    protected final void smoothScrollTo(int newScrollValue, long duration) {
        if (null != mSmoothScrollRunnable) {
            mSmoothScrollRunnable.stop();
        }

        final int oldScrollValue = getScrollY();

        if (oldScrollValue != newScrollValue) {
            if (null == mScrollAnimationInterpolator) {
                // Default interpolator is a Decelerate Interpolator
                mScrollAnimationInterpolator = new DecelerateInterpolator();
            }
            mSmoothScrollRunnable = new SmoothScrollRunnable(oldScrollValue, newScrollValue, duration);
            post(mSmoothScrollRunnable);
        }
    }

    /**
     * 控制AbsListView的滑动
     *
     * @param postion Listview的位置
     */
    abstract void smoothScrollToPosition(int postion);

    /**
     * 下拉刷新时的监听器
     */
    public interface PullToRefreshCallback {
        /**
         * 下拉刷新时的回调方法
         *
         * @param isUserManual 是否为用户手动下拉，导致触发的刷新行为
         * @return 如果为true，则不会再执行OnRefreshListener中的onRefresh方法（即，不再兼容老的OnRefreshListener）
         */
        boolean onPullToRefresh(boolean isUserManual);
    }

    /**
     * 设置下拉刷新时的监听器
     *
     * @param pullToRefreshCallback
     */
    public void setPullToRefreshCallback(PullToRefreshCallback pullToRefreshCallback) {
        mPullToRefreshCallback = pullToRefreshCallback;
    }


    /**
     * 注册列表数据状态的Adapter，主要用来获得当前列表数据的一些状态
     *
     * @param adapter
     */
    public void setListDataStatusAdapter(ListDataStatusAdapter adapter) {
        mListDataStatusAdapter = adapter;
    }

    /**
     * 在适当的时候，要对事件进行拦截。下拉刷新的过程中，事件不往子View传递。
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        if (!mIsPullRefreshEabled) {
            return super.onInterceptTouchEvent(ev);
        }
        if (isDataRefreshing()) {
            return super.onInterceptTouchEvent(ev);
        }
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mIsBeingDragged = false;
            return false;
        }

        if (action != MotionEvent.ACTION_DOWN && mIsBeingDragged) {
            return true;
        }
        float y = ev.getY();
        switch (action) {
            case MotionEvent.ACTION_MOVE: {
                mCurMotionY = y;
                if (isReadyForPullAction()) {
                    mLastMotionY = y;
                    mIsBeingDragged = true;
                }
                break;
            }
            case MotionEvent.ACTION_DOWN: {
                mLastMotionY = mInitialMotionY = ev.getY();
                if (isReadyForPullAction()) {
                    mIsBeingDragged = false;
                }
                break;
            }
        }

        return mIsBeingDragged;
    }

    abstract boolean isReadyForPullAction();

    /**
     * 设置执行下拉操作时,最小的手势位移值.（小于这个值,则不会触发下拉刷新的操作）
     *
     * @param shiftValue 最小的手势位移值,单位dp
     */
    public void setMinPullActionShift(int shiftValue) {
        mMinPullActionShift = shiftValue;
    }

    /**
     * 获取执行下拉操作时,最小的手势位移值.（小于这个值,则不会触发下拉刷新的操作）
     *
     * @return 最小的手势位移值, 单位dp
     */
    public int getMinPullActionShift() {
        return mMinPullActionShift;
    }

    /**
     * 对相应的事件进行处理。处理加载更多的逻辑
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mIsPullRefreshEabled) {
            return super.onTouchEvent(event);
        }
        if (isDataRefreshing()) {
            return super.onTouchEvent(event);
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN && event.getEdgeFlags() != 0) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE: {
                if (mIsBeingDragged) {
                    mLastMotionY = event.getY();
                    performPullAction();
                    return true;
                }
                break;
            }

            case MotionEvent.ACTION_DOWN: {
                if (isReadyForPullAction()) {
                    mLastMotionY = mInitialMotionY = event.getY();
                    return true;
                }
                break;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                if (mIsBeingDragged) {
                    mIsBeingDragged = false;
                    if (mHeaderState == AbsRefreshableHeaderView.HeaderState.STATE_RELEASE_TO_REFRESH) {
                        startRefreshing(true);
                    } else {
                        onRefreshComplete();
                    }
                    return true;
                }
                break;
            }
        }
        return false;
    }

    protected void performPullAction() {
        final int newScrollValue;
        final float initialMotionValue, lastMotionValue;
        initialMotionValue = mInitialMotionY;
        lastMotionValue = mLastMotionY;
        newScrollValue = Math.round(Math.min(initialMotionValue - lastMotionValue, 0) / FRICTION);
        if (-newScrollValue < mCurrentRefreshTransitionHeight) {
            mHeaderState = AbsRefreshableHeaderView.HeaderState.STATE_PULL_TO_REFRESH;
        } else {
            mHeaderState = AbsRefreshableHeaderView.HeaderState.STATE_RELEASE_TO_REFRESH;
        }
        mRefreshableHeader.onState(mHeaderState);
        publishProgresToHeader(Math.abs(newScrollValue));
        scrollTo(0, newScrollValue);
    }

    /**
     * 将当前的进度，传递到Header当中
     *
     * @param offset
     */
    protected void publishProgresToHeader(int offset) {
        if (Math.abs(offset) < mDefaultProgressOffset) {
            return;
        }
        float fenmu = (mRefreshTransitionHeight - mDefaultProgressOffset) / (float) mRefreshTransitionHeight;
        float progress = ((float) Math.abs(offset) - mDefaultProgressOffset)
                / ((float) fenmu * mRefreshTransitionHeight) * 100f;
        mRefreshableHeader.setProgress(progress);
    }


    /**
     * 设置是否支持下拉刷新的功能
     *
     * @param enabled
     */
    public void setPullRefreshEnabled(boolean enabled) {
        mIsPullRefreshEabled = enabled;
    }


    /**
     * dp转换px
     *
     * @param context 上下文环境
     * @param dp      dp值
     * @return int
     */
    public static int dpToPxInt(Context context, float dp) {
        return (int) (dpToPx(context, dp) + 0.5f);
    }

    /**
     * dp转换px
     *
     * @param context 上下文环境
     * @param dp      dp值
     * @return float
     */
    public static float dpToPx(Context context, float dp) {
        if (context == null) {
            return -1;
        }
        return dp * context.getResources().getDisplayMetrics().density;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        try {
            super.onRestoreInstanceState(state);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断当前是否正在刷新数据
     * 如果当前“正在刷新”，则返回true。否则，返回false
     *
     * @return 是否正在刷新数据
     */
    public boolean isDataRefreshing() {
        boolean isDataLoading = false;
        if (mHeaderState == AbsRefreshableHeaderView.HeaderState.STATE_REFRESHING) {
            isDataLoading = true;
        }
        return isDataLoading;
    }
}
