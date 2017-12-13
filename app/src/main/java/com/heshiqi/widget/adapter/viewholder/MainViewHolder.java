package com.heshiqi.widget.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.heshiqi.widget.R;
import com.heshiqi.widget.entity.MainEntity;
import com.heshiqi.widget.utils.ImageOptionsFactory;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * Created by heshiqi on 17/8/8.
 */

public class MainViewHolder extends BaseViewHolder<MainEntity> {

    private ImageView imageView;
    private TextView content;

    public MainViewHolder(Context context, View itemView) {
        super(context, itemView);
        imageView = (ImageView) itemView.findViewById(R.id.item_pic);
        content = (TextView) itemView.findViewById(R.id.item_content);
    }

    @Override
    public void render(MainEntity mainEntity) {
        super.render(mainEntity);
        content.setText(mainEntity.getContent());
        ImageLoader.getInstance().displayImage(mainEntity.getImg(), imageView, ImageOptionsFactory.getDisplayImageOptions(R.mipmap.ic_launcher));
    }
}
