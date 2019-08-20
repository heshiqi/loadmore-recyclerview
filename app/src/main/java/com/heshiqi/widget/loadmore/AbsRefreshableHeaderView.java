package com.heshiqi.widget.loadmore;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.heshiqi.widget.R;
import com.heshiqi.widget.utils.DateTimeUtil;

import java.util.Date;

public abstract class AbsRefreshableHeaderView extends RelativeLayout {
    protected long mLastUpdateTime;

    /**
     * HeaderView中指示器的文字显示，默认状态下，该指示器显示："下拉刷新"
     */
    protected String mPullStatusIndictorText = getContext().getString(R.string.pull_to_refresh_pull_label);
    /**
     * HeaderView中指示器的文字显示，可松开刷新的状态下，该指示器显示："松开刷新"
     */
    protected String mReleaseStatusIndictorText = getContext().getString(R.string.pull_to_refresh_release_label);
    /**
     * HeaderView中指示器的文字显示，刷新中的状态显示："正在刷新"
     */
    protected String mRefreshingStatusIndictorText = getContext().getString(R.string.pull_to_refresh_refreshing_label);

    /**
     * 构造函数
     *
     * @param context
     */
    public AbsRefreshableHeaderView(Context context) {
        super(context);
        init(context);
    }

    /**
     * 构造函数
     *
     * @param context
     * @param attrs
     */
    public AbsRefreshableHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 构造函数
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public AbsRefreshableHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    /**
     * 保存上次更新时间
     *
     * @param lastUpdateTime 更新时间
     */
    protected void saveLastUpdateTime(long lastUpdateTime) {
        mLastUpdateTime = lastUpdateTime;
    }

    /**
     * 获取上次更新时间的文字描述
     *
     * @return
     */
    protected String getLastUpdateString() {
        Resources res = getResources();
        String timeString = DateTimeUtil.twoDateDistance(new Date(mLastUpdateTime), new Date());
        return res.getString(R.string.last_update) + timeString;
    }

    /**
     * 初始化HeaderView
     *
     * @param context 上下文环境
     */
    public abstract void init(Context context);

    /**
     * 根据用户不同的下拉操作，Header的状态发生变化时
     *
     * @param state HeaderState
     */
    public abstract void onState(HeaderState state);

    /**
     * 下拉进度刷新
     *
     * @param progress 更新进度
     */
    public abstract void setProgress(float progress);

    /**
     * 设置HeaderView中指示器在默认状态下的文字显示，默认显示为："下拉刷新"
     *
     * @param pullStatusIndictorText
     */
    public void setPullStatusIndictorText(String pullStatusIndictorText) {
        this.mPullStatusIndictorText = pullStatusIndictorText;
    }

    /**
     * 设置HeaderView中指示器在松开刷新状态下的文字显示，默认显示为："松开刷新"
     *
     * @param releaseStatusIndictorText
     */
    public void setReleaseStatusIndictorText(String releaseStatusIndictorText) {
        this.mReleaseStatusIndictorText = releaseStatusIndictorText;
    }

    /**
     * 设置HeaderView中指示器在刷新中状态下的文字显示，默认显示为："刷新中"
     *
     * @param refreshingStatusIndictorText
     */
    public void setRefreshingStatusIndictorText(String refreshingStatusIndictorText) {
        this.mRefreshingStatusIndictorText = refreshingStatusIndictorText;
    }

    /**
     * 设置下拉 引导图 显示
     * @param flag true 显示
     */
    public void setHeaderImageStatus(boolean flag, int height) {
    }

    /**
     * 设置下拉 引导图 显示的url，及高度
     * @param url
     * @param height
     */
    public void setHeaderImageUrl(String url, int height) {
    }

    /**
     * 获取 下拉 引导图的高度
     * @return
     */
    public int getHeaderImageHeight() {
        return 0;
    }

    public enum HeaderState {
        /**
         * 初始状态
         */
        STATE_RESET,
        /**
         * 正在下拉状态
         */
        STATE_PULL_TO_REFRESH,
        /**
         * 下拉释放刷新状态
         */
        STATE_RELEASE_TO_REFRESH,
        /**
         * 加载中状态
         */
        STATE_REFRESHING
    };

}
