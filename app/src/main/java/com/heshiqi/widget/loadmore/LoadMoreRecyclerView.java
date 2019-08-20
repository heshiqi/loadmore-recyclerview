package com.heshiqi.widget.loadmore;

import android.content.Context;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import com.heshiqi.widget.R;
import com.heshiqi.widget.adapter.viewholder.BaseViewHolder;


/**
 * Created by heshiqi on 16/12/2.
 */

public class LoadMoreRecyclerView extends RecyclerView implements View.OnClickListener {

    protected OnScrollListener mOnScrollListener;
    private RecyclerViewPositionHelper mRecyclerViewHelper;
    protected LAYOUT_MANAGER_TYPE layoutManagerType;
    private OnLoadMoreListener onLoadMoreListener;
    private BaseHeaderFooterAdapter mAdapter;

    private boolean automaticLoadMoreEnabled = false;//滑动到列表底部是否自动加载
    private boolean mIsLoadMoreWidgetEnabled = false;//是否启用加载更多
    private int mVisibleItemCount = 0;//当前界面显示布局的数量
    private int mTotalItemCount = 0;//要显示布局的总数量
    private int previousTotal = 0;//最后一次列表显示的item总数量
    private int mFirstVisibleItem;//列表顶部item的下标位置
    private int lastVisibleItemPosition;//列表底部item的下标位置
    private int[] mlastPositionsStaggeredGridLayout;

    private LoadMoreStatus loadMoreStatus;

    public LoadMoreRecyclerView(Context context) {
        super(context);
        initViews(context);
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(context);
    }

    /**
     * 开启加载更多功能 在{@link #setAdapter(BaseHeaderFooterAdapter)}之前调用
     */
    public void triggerLoadMore() {
        if (mAdapter != null) {
            new RuntimeException("在调用setAdapter(BaseHeaderFooterAdapter)之前 调用此方法");
        }
        loadMoreStatus = new LoadMoreStatus();
    }

    protected void initViews(Context context) {

    }

    /**
     * 添加滑动事件监听 处理加载更多事件
     */
    protected void setDefaultScrollListener() {
        removeOnScrollListener(mOnScrollListener);
        mOnScrollListener = new OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrollLoadMoreDetection(recyclerView);
            }
        };

        addOnScrollListener(mOnScrollListener);
    }

    /**
     * @param adapter
     */
    public void setAdapter(BaseHeaderFooterAdapter adapter) {
        super.setAdapter(adapter);
        mAdapter = adapter;
        if (loadMoreStatus != null) {
            mAdapter.setOnFooterViewClickListener(this);
            mAdapter.setFooter(loadMoreStatus);
            mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(this);
        }
    }


    /**
     * 处理滑动事件
     *
     * @param recyclerView
     */
    private void scrollLoadMoreDetection(RecyclerView recyclerView) {

        LayoutManager layoutManager = recyclerView.getLayoutManager();

        if (layoutManagerType == null) {
            if (layoutManager instanceof GridLayoutManager) {
                layoutManagerType = LAYOUT_MANAGER_TYPE.GRID;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                layoutManagerType = LAYOUT_MANAGER_TYPE.STAGGERED_GRID;
            } else if (layoutManager instanceof LinearLayoutManager) {
                layoutManagerType = LAYOUT_MANAGER_TYPE.LINEAR;
            } else {
                throw new RuntimeException("Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
            }
        }

        mTotalItemCount = layoutManager.getItemCount();
        mVisibleItemCount = layoutManager.getChildCount();

        switch (layoutManagerType) {
            case LINEAR:
                mFirstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();
                lastVisibleItemPosition = mRecyclerViewHelper.findLastVisibleItemPosition();
                break;
            case GRID:
                if (layoutManager instanceof GridLayoutManager) {
                    GridLayoutManager ly = (GridLayoutManager) layoutManager;
                    lastVisibleItemPosition = ly.findLastVisibleItemPosition();
                    mFirstVisibleItem = ly.findFirstVisibleItemPosition();
                }
                break;
            case STAGGERED_GRID:
                if (layoutManager instanceof StaggeredGridLayoutManager) {
                    StaggeredGridLayoutManager sy = (StaggeredGridLayoutManager) layoutManager;

                    if (mlastPositionsStaggeredGridLayout == null)
                        mlastPositionsStaggeredGridLayout = new int[sy.getSpanCount()];

                    sy.findLastVisibleItemPositions(mlastPositionsStaggeredGridLayout);
                    lastVisibleItemPosition = findMax(mlastPositionsStaggeredGridLayout);

                    sy.findFirstVisibleItemPositions(mlastPositionsStaggeredGridLayout);
                    mFirstVisibleItem = findMin(mlastPositionsStaggeredGridLayout);
                }
                break;
        }

        if (automaticLoadMoreEnabled) {

            if (mTotalItemCount > previousTotal) {
                automaticLoadMoreEnabled = false;
                previousTotal = mTotalItemCount;
            }
        }

        boolean bottomEdgeHit = (mTotalItemCount - mVisibleItemCount) <= mFirstVisibleItem;
        /**
         * 已经滑动到底部
         */
        if (bottomEdgeHit) {
            if (mIsLoadMoreWidgetEnabled) {
                /**auto activate load more**/
                if (!automaticLoadMoreEnabled) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.loadMore(mAdapter.getItemCount(), lastVisibleItemPosition);
                    }
                    automaticLoadMoreEnabled = true;
                }
            }
            previousTotal = mTotalItemCount;
        }
    }

    /**
     * 如果你禁用加载更多后想再次启用调用此方法
     */
    private void enableLoadmore() {
        if (mAdapter != null && loadMoreStatus != null) {
            mIsLoadMoreWidgetEnabled = true;
            setDefaultScrollListener();
        }
    }

    /**
     * 禁止加载更多
     */
    private void disableLoadmore() {
        mIsLoadMoreWidgetEnabled = false;
        if (mAdapter != null && loadMoreStatus != null) {
            removeOnScrollListener(mOnScrollListener);
        }
    }

    public void setLoadMoreStatus(LAOD_MORE_STATUS status) {
        switch (status) {
            case LOADING:
                if (!isLoadMoreEnabled()) {
                    enableLoadmore();
                }
                if (mAdapter != null && loadMoreStatus != null) {
                    loadMoreStatus.loadMoreStatus = LAOD_MORE_STATUS.LOADING;
                    mAdapter.notifyItemChanged(mAdapter.getItemCount() - 1);
                }
                break;
            case LOADING_ERROR:
                if (mAdapter != null && loadMoreStatus != null) {
                    loadMoreStatus.loadMoreStatus = LAOD_MORE_STATUS.LOADING_ERROR;
                    mAdapter.notifyItemChanged(mAdapter.getItemCount() - 1);
                }
                break;
            case LOADING_NO_MORE:
                if (mAdapter != null && loadMoreStatus != null) {
                    loadMoreStatus.loadMoreStatus = LAOD_MORE_STATUS.LOADING_NO_MORE;
                    mAdapter.notifyItemChanged(mAdapter.getItemCount() - 1);
                }
                break;
            case LOADING_HIDE:
                disableLoadmore();
                if (mAdapter != null && loadMoreStatus != null) {
                    loadMoreStatus.loadMoreStatus = LAOD_MORE_STATUS.LOADING_HIDE;
                    mAdapter.notifyItemChanged(mAdapter.getItemCount() - 1);
                }
                break;
        }
    }

    /**
     * 是否启用加载更多
     *
     * @return
     */
    public boolean isLoadMoreEnabled() {
        return mIsLoadMoreWidgetEnabled;
    }


    private int findMax(int[] lastPositions) {
        int max = Integer.MIN_VALUE;
        for (int value : lastPositions) {
            if (value > max)
                max = value;
        }
        return max;
    }

    private int findMin(int[] lastPositions) {
        int min = Integer.MAX_VALUE;
        for (int value : lastPositions) {
            if (value != RecyclerView.NO_POSITION && value < min)
                min = value;
        }
        return min;
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
     * 设置加载更多时间回调
     *
     * @param onLoadMoreListener load listen
     */
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public void onClick(View v) {
        if (onLoadMoreListener != null && mAdapter != null) {
            setLoadMoreStatus(LAOD_MORE_STATUS.LOADING);
            onLoadMoreListener.loadMore(mAdapter.getItemCount(), lastVisibleItemPosition);
        }
    }

    // TODO: 11/4/18 第二次刷新后再下划加载更多。其它标识位经测试后可都复位。
    public void reset() {
//        mFirstVisibleItem = 0;
        previousTotal = 0;
//        mTotalItemCount = 0;
//        mFirstVisibleItem = 0;
//        lastVisibleItemPosition=0;
//        automaticLoadMoreEnabled = false;
    }

    /**
     * 加载更多回调
     */
    public interface OnLoadMoreListener {
        void loadMore(int itemsCount, final int maxLastVisiblePosition);
    }

    public enum LAYOUT_MANAGER_TYPE {
        LINEAR,
        GRID,
        STAGGERED_GRID
    }

    public static final class LoadMoreStatus {
        public LAOD_MORE_STATUS loadMoreStatus = LAOD_MORE_STATUS.LOADING_HIDE;
    }

    public enum LAOD_MORE_STATUS {
        LOADING,
        LOADING_ERROR,
        LOADING_NO_MORE,
        LOADING_HIDE

    }

    public static class LoadMoreViewHolder extends BaseViewHolder {

        private RefreshableFooterView refreshableFooterView;

        public LoadMoreViewHolder(View itemView, Context context) {
            super(context,itemView);
            itemView.setTag(R.id.card_item_type,"888");
            refreshableFooterView = (RefreshableFooterView) itemView.findViewById(R.id.load_more_view);
        }

        public void render(LoadMoreStatus loadMoreStatus, OnClickListener onClickListener) {
            refreshableFooterView.setOnClickListener(onClickListener);
            switch (loadMoreStatus.loadMoreStatus) {
                case LOADING:
                    refreshableFooterView.onState(RefreshableFooterView.FooterState.STATE_LOADING);
                    refreshableFooterView.setEnabled(false);
                    break;
                case LOADING_NO_MORE:
                    refreshableFooterView.onState(RefreshableFooterView.FooterState.STATE_LOAD_NO_MORE);
                    refreshableFooterView.setEnabled(false);
                    break;
                case LOADING_ERROR:
                    refreshableFooterView.onState(RefreshableFooterView.FooterState.STATE_LOAD_ERROR);
                    refreshableFooterView.setEnabled(true);
                    break;
                case LOADING_HIDE:
                    refreshableFooterView.onState(RefreshableFooterView.FooterState.STATE_LOAD_HIDE);
                    refreshableFooterView.setEnabled(false);
                    break;
                default:
                    refreshableFooterView.onState(RefreshableFooterView.FooterState.STATE_LOAD_HIDE);
                    refreshableFooterView.setEnabled(false);
                    break;
            }
        }
    }
}
