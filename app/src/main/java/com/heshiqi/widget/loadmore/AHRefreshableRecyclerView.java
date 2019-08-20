package com.heshiqi.widget.loadmore;

import android.content.Context;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.autohome.commontools.android.ScreenUtils;


/**
 * Created by heshiqi on 17/9/15.
 */

public class AHRefreshableRecyclerView extends AbstractRefreshableView<LoadMoreRecyclerView, BaseHeaderFooterAdapter> {


    public AHRefreshableRecyclerView(Context context) {
        super(context);
    }

    public AHRefreshableRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AHRefreshableRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected LoadMoreRecyclerView createListView(Context context, AttributeSet attrs) {
        return new LoadMoreRecyclerView(context, attrs);
    }


    @Override
    public void setAdapter(BaseHeaderFooterAdapter adapter) {
        getListView().setAdapter(adapter);
    }

    @Override
    public BaseHeaderFooterAdapter getAdapter() {
        return (BaseHeaderFooterAdapter) getListView().getAdapter();
    }


    @Override
    public void setSelection(int position) {
        getListView().scrollToPosition(position);
    }

    @Override
    public void smoothScrollToPosition(int postion) {
        getListView().smoothScrollToPosition(postion);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        getListView().setLayoutManager(layoutManager);
    }

    /**
     * 打开加载更多
     */
    public void triggerLoadMore(){
        getListView().triggerLoadMore();
    }

    /**
     * 添加分隔线
     * @param itemDecoration
     */
    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration){
       getListView().addItemDecoration(itemDecoration);
    }

    /**
     * 设置加载更多事件
     * @param loadMoreListener
     */
    public void setOnLoadMoreListener(LoadMoreRecyclerView.OnLoadMoreListener loadMoreListener){
        getListView().setOnLoadMoreListener(loadMoreListener);
    }

    /**
     * 设置加载更多布局的显示状态
     * @param status
     */
    public void setLoadMoreStatus(LoadMoreRecyclerView.LAOD_MORE_STATUS status){
        getListView().setLoadMoreStatus(status);
    }

    @Override
    boolean isReadyForPullAction() {
        if (getListView().getChildCount() > 0) {
            RecyclerView rv = getListView();
            RecyclerView.LayoutManager lm = rv.getLayoutManager();
            if (rv.getAdapter() == null) {
                return false;
            }
            View firstChild = rv.getChildAt(0);
            boolean result = rv.getChildLayoutPosition(firstChild) * lm.getDecoratedMeasuredHeight(firstChild) - lm.getDecoratedTop(firstChild) <= 0;
            return result && mCurMotionY - mLastMotionY > ScreenUtils.dpToPxInt(getContext(), mMinPullActionShift);
        }
        return false;
    }

    @Override
    public void startRefreshing(boolean isUserManual) {
        //刷新之前重置标识位。
        if (mIsPullRefreshEabled) {
            getListView().reset();
            getListView().setLoadMoreStatus(LoadMoreRecyclerView.LAOD_MORE_STATUS.LOADING_HIDE);
        }
        super.startRefreshing(isUserManual);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    public void onRefreshComplete(boolean isLoadMoreComplete){
        if(isLoadMoreComplete){
            getListView().setLoadMoreStatus(LoadMoreRecyclerView.LAOD_MORE_STATUS.LOADING_HIDE);
        }else{
            getListView().setLoadMoreStatus(LoadMoreRecyclerView.LAOD_MORE_STATUS.LOADING);
        }
        super.onRefreshComplete();
    }
}
