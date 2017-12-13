package com.heshiqi.widget.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heshiqi.widget.adapter.viewholder.HeaderViewHolder;
import com.heshiqi.widget.adapter.viewholder.MainViewHolder;
import com.heshiqi.widget.core.contract.MainContract;
import com.heshiqi.widget.R;
import com.heshiqi.widget.adapter.viewholder.BaseViewHolder;
import com.heshiqi.widget.entity.HeaderEntity;
import com.heshiqi.widget.entity.MainEntity;
import com.heshiqi.widget.loadmore.LoadMoreRecyclerView;
import com.heshiqi.widget.loadmore.LoadMoreViewHolder;


/**
 * Created by heshiqi on 17/8/16.
 */

public class LoadMoreAdapter extends BaseHeaderFooterAdapter<BaseViewHolder, HeaderEntity, LoadMoreRecyclerView.LoadMoreStatus> {

    private Context mContext;
    private LayoutInflater layoutInflater;
    private MainContract.Presenter presenter;

    public LoadMoreAdapter(Context context, MainContract.Presenter presenter) {
        this.mContext = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.presenter = presenter;
    }

    @Override
    protected BaseViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_header_view, parent, false);
        return new HeaderViewHolder(mContext, itemView);
    }

    @Override
    protected BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_main, parent, false);
        return new MainViewHolder(mContext, itemView);
    }

    @Override
    protected BaseViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_layout_load_more, parent, false);
        return new LoadMoreViewHolder(itemView, mContext);
    }

    @Override
    protected void onBindHeaderViewHolder(BaseViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            final HeaderViewHolder viewHolder = (HeaderViewHolder) holder;
            HeaderEntity entity = getHeader();
            if (entity != null) {
                viewHolder.render(entity);
            }
        }
    }

    @Override
    protected void onBindItemViewHolder(BaseViewHolder holder, int position) {
        MainEntity entity = getItem(position);
        if (entity != null) {
            if (holder instanceof MainViewHolder) {
                MainViewHolder userViewHolder = (MainViewHolder) holder;
                userViewHolder.render(entity);
            }
        }
    }

    @Override
    protected void onBindFooterViewHolder(BaseViewHolder holder, int position) {
        if (holder instanceof LoadMoreViewHolder) {
            LoadMoreRecyclerView.LoadMoreStatus loadMoreStatus = getFooter();
            if (loadMoreStatus != null) {
                final LoadMoreViewHolder moreViewHolder = (LoadMoreViewHolder) holder;
                moreViewHolder.render(loadMoreStatus, onFooterViewClickListener);
            }
        }
    }

    @Override
    public int getAdapterItemCount() {
        return presenter.getDatas().size();
    }

    private MainEntity getItem(int position) {
        return presenter.getItem(position);
    }

}
