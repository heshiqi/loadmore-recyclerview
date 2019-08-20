package com.heshiqi.widget.loadmore;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

public class RingAnimHeaderView extends AbsRefreshableHeaderView {
    public RingAnimHeaderView(Context context) {
        super(context);
    }

    public RingAnimHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RingAnimHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void init(Context context) {

    }

    @Override
    public void onState(HeaderState state) {

    }

    @Override
    public void setProgress(float progress) {

    }

//    public static final int STYLE_RING_NEW = 1;
//    public static final int STYLE_RING_OLD = 0;
//
//    private RingAnimView vRingAnimView;
//    private TextGradientView vIndictor;
//    private HeaderState mHeaderState;
//
//    public RingAnimHeaderView(Context context) {
//        super(context);
//    }
//
//    public RingAnimHeaderView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public RingAnimHeaderView(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//    }
//
//    @Override
//    public void init(Context context) {
//        initView(context);
//    }
//
//    private void initView(Context context){
//        LayoutInflater.from(context).inflate(R.layout.ahlib_common_ring_anim_refresh_header, this);
//        vRingAnimView = (RingAnimView) findViewById(R.id.ring_anim_view);
//        vIndictor = (TextGradientView) findViewById(R.id.tv_pull_indicator);
//    }
//
//    public void setStyle(int style){
//        if (style == STYLE_RING_NEW) {
//            vIndictor.setGradientStyle(true);
//        } else {
//            vIndictor.setGradientStyle(false);
//        }
//    }
//
//    @Override
//    public void onState(HeaderState state) {
//        if (state == null || state == mHeaderState) {
//            return;
//        }
//        mHeaderState = state;
//        switch (state) {
//            case STATE_RESET:
//                vRingAnimView.stop();
//                break;
//            case STATE_PULL_TO_REFRESH:
//                vRingAnimView.start();
//                vIndictor.setText(mPullStatusIndictorText);
//                break;
//            case STATE_RELEASE_TO_REFRESH:
////                vRingAnimView.stop();
//                vIndictor.setGradientText(mReleaseStatusIndictorText);
//                break;
//            case STATE_REFRESHING:
//                vRingAnimView.start();
//                vIndictor.setGradientText(mRefreshingStatusIndictorText);
//                break;
//            default:
//                break;
//        }
//    }
//
//    @Override
//    public void setProgress(float progress) {
//
//    }
//
//    public void showRefreshText(boolean show){
//        if(null != vIndictor){
//            vIndictor.setVisibility(show ? VISIBLE : GONE);
//        }
//    }
}
