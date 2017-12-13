package com.heshiqi.widget.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.heshiqi.widget.R;
import com.heshiqi.widget.entity.HeaderEntity;


/**
 * Created by heshiqi on 17/8/16.
 */

public class HeaderViewHolder extends BaseViewHolder<HeaderEntity> {

    private TextView content;

    public HeaderViewHolder(final Context context, View itemView) {
        super(context, itemView);
        content = (TextView) itemView.findViewById(R.id.content);

    }

    @Override
    public void render(HeaderEntity headerEntity) {
        content.setText(headerEntity.getContent());
    }
}
