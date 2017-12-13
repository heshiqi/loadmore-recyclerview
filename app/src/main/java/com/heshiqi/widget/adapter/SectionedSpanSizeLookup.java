package com.heshiqi.widget.adapter;

import android.support.v7.widget.GridLayoutManager;


public class SectionedSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

    protected BaseHeaderFooterAdapter adapter = null;
    protected GridLayoutManager layoutManager = null;

    public SectionedSpanSizeLookup(BaseHeaderFooterAdapter adapter, GridLayoutManager layoutManager) {
        this.adapter = adapter;
        this.layoutManager = layoutManager;
    }

    @Override
    public int getSpanSize(int position) {
        int result = 1;
        switch (adapter.getItemViewType(position)) {
            case BaseHeaderFooterAdapter.TYPE_HEARDER:
            case BaseHeaderFooterAdapter.TYPE_FOOTER:
                result = layoutManager.getSpanCount();
                break;
            default:
                result = 1;
                break;

        }
        return result;
    }

    @Override
    public int getSpanIndex(int position, int spanCount) {
        //position 0 item type BaseRecyclerAdapter.TYPE_HEADER, just one item in this group.
        if (position == 0) {
            return 0;
        } else {
            return (position - 1) % spanCount;
        }
    }

    @Override
    public int getSpanGroupIndex(int adapterPosition, int spanCount) {
        //position 0 item type BaseRecyclerAdapter.TYPE_HEADER, just one item in this group.
        if (adapterPosition == 0) {
            return 0;
        } else {
            return (adapterPosition + spanCount - 1) / spanCount;
        }
    }
}
