package com.heshiqi.widget.loadmore;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.heshiqi.widget.R;
import com.heshiqi.widget.utils.ScreenUtil;


/**
 * Created by heshiqi on 17/8/12.
 */

public class RefreshableFooterView extends LinearLayout {
    private Context mContext;

    /**
     * Footer内部的Layout
     */
    private ViewGroup mInnerLayout;

    /**
     * 进度条控件
     */
    private ProgressBar mProgressBar;
    /**
     * 当前状态TextView
     */
    private TextView mBottomBtn;

    private String mNoMoreDataInfoText = getContext().getString(
            R.string.loadmore_no_more);
    private String mLoadErrorInfoText = getContext().getString(
            R.string.load_more_error);

    /**
     * 构造函数
     *
     * @param context
     */
    public RefreshableFooterView(Context context) {
        super(context);
        init(context);
    }

    /**
     * 构造函数
     *
     * @param context
     * @param attrs
     */
    public RefreshableFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 初始化布局参数
     */
    private void init(Context context) {
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.layout_refresh_footer, this);
        mInnerLayout = (ViewGroup) findViewById(R.id.footer_container);
        mBottomBtn = (TextView) findViewById(R.id.footer_loading_text);
        mProgressBar = (ProgressBar) findViewById(R.id.footer_progress_bar);
        mBottomBtn.setVisibility(View.VISIBLE);
        mBottomBtn.setClickable(false);
        mBottomBtn.setEnabled(false);
        mBottomBtn.setFocusable(false);
    }

    /**
     * 显示或隐藏Footer View
     *
     * @param show
     */
    private void showFooter(boolean show) {
        mInnerLayout.setVisibility(show ? VISIBLE : GONE);
    }

    /**
     * 重置FooterView
     */
    private void resetFooter() {
        mBottomBtn.setText("");
        mProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * 显示正在加载更多
     */
    private void setFooterInfoLoading() {
        mBottomBtn.setText("");
        mProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * 显示没有更多数据
     */
    private void setFooterInfoNoMore() {
        mBottomBtn.setText(mNoMoreDataInfoText);
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * 显示加载失败
     */
    private void setFooterInfoError() {
        mBottomBtn.setText(mLoadErrorInfoText);
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * 显示指定的提示文字
     *
     * @param info 指定的显示文字
     */
    public void setFooterInfo(String info) {
        onState(FooterState.STATE_LOAD_RESET);
        mBottomBtn.setText(info);
    }

    /**
     * 设置“正在加载”时，Footer底部需要显示的文字
     *
     * @param loadingDataInfoText
     */
    public void setLoadingDataInfoText(String loadingDataInfoText) {
//        this.mLoadingDataInfoText = loadingDataInfoText;
    }

    /**
     * 设置“没有更多数据”时，Footer底部需要显示的文字
     *
     * @param noMoreDataInfoText
     */
    public void setNoMoreDataInfoText(String noMoreDataInfoText) {
        this.mNoMoreDataInfoText = noMoreDataInfoText;
    }

    /**
     * 设置“加载失败”时，Footer底部需要显示的文字
     *
     * @param loadErrorInfoText
     */
    public void setLoadErrorInfoText(String loadErrorInfoText) {
        this.mLoadErrorInfoText = loadErrorInfoText;
    }

    /**
     * 如果只显示文本，则给FooterView设置不同显示样式
     *
     * @param onlyShowTipContent
     */
    private void setTipMsgStyle(boolean onlyShowTipContent) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mBottomBtn.getLayoutParams();
        if (!onlyShowTipContent) {
            params.setMargins(0, ScreenUtil.dp2Px(getContext(),5), 0, 0);
        } else {
            params.setMargins(0, ScreenUtil.dp2Px(getContext(),10), 0, ScreenUtil.dp2Px(getContext(),10));
        }

        mBottomBtn.setLayoutParams(params);

        mBottomBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, !onlyShowTipContent ? 10 : 12);
    }

    /**
     * Footer状态发生切换
     *
     * @param state 状态信息
     */
    public void onState(FooterState state) {
        if (state == null) {
            return;
        }
        switch (state) {
            case STATE_LOAD_RESET:
                showFooter(true);
                resetFooter();
                setTipMsgStyle(false);
                break;
            case STATE_LOADING:
                showFooter(true);
                setFooterInfoLoading();
                setTipMsgStyle(false);
                break;
            case STATE_LOAD_NO_MORE:
                showFooter(true);
                setFooterInfoNoMore();
                setTipMsgStyle(true);
                break;
            case STATE_LOAD_ERROR:
                showFooter(true);
                setFooterInfoError();
                setTipMsgStyle(true);
                break;
            case STATE_LOAD_HIDE:
                showFooter(false);
                setTipMsgStyle(false);
                break;
            default:
                break;
        }
    }

    public static enum FooterState {
        STATE_LOAD_RESET,
        STATE_LOADING,
        STATE_LOAD_NO_MORE,
        STATE_LOAD_ERROR,
        STATE_LOAD_HIDE;

        private FooterState() {
        }
    }
}