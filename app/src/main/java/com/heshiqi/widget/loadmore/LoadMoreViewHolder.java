package com.heshiqi.widget.loadmore;

import android.content.Context;
import android.view.View;

import com.heshiqi.widget.R;
import com.heshiqi.widget.adapter.viewholder.BaseViewHolder;


/**
 * Created by heshiqi on 16/12/5.
 */

public class LoadMoreViewHolder extends BaseViewHolder {

    private RefreshableFooterView refreshableFooterView;

    public LoadMoreViewHolder(View itemView, Context context) {
        super(context, itemView);
        refreshableFooterView = (RefreshableFooterView) itemView.findViewById(R.id.load_more_view);
    }

    public void render(LoadMoreRecyclerView.LoadMoreStatus loadMoreStatus, View.OnClickListener onClickListener) {
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
