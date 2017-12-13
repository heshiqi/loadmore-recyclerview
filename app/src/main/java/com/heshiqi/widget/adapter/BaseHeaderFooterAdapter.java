package com.heshiqi.widget.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by heshiqi on 16/12/2.
 */

public abstract class BaseHeaderFooterAdapter<VH extends RecyclerView.ViewHolder, H/*头布局的数据类泛型*/, F/*尾布局的数据类泛型*/> extends RecyclerView.Adapter<VH> {

    /**
     * 头部布局类型
     */
    public static final int TYPE_HEARDER = -1;
    /**
     * 尾部布局类型
     */
    public static final int TYPE_FOOTER = -2;
    /**
     * 正常布局类型
     */
    public static final int TYPE_NORMAL = 0;


    private H header;//头部布局数据
    private F footer;//尾部布局数据

    /**
     * 列表项的点击事件
     */
    private OnItemClickListener onItemClickListener;

    /**
     * 尾部布局的点击事件
     */
    protected View.OnClickListener onFooterViewClickListener;

    /**
     * 定义列表项事件接口类
     */
    public interface OnItemClickListener {
        void onItemClicked(View view, int position);
    }

    public BaseHeaderFooterAdapter() {

    }

    /**
     * 设置列表项的点击事件
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 设置尾部布局的点击事件
     * @param onFooterViewClickListener
     */
    public void setOnFooterViewClickListener(View.OnClickListener onFooterViewClickListener) {
        this.onFooterViewClickListener = onFooterViewClickListener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        VH viewHolder = null;
        if (viewType == TYPE_HEARDER) {//头布局
            viewHolder = onCreateHeaderViewHolder(parent, viewType);
        } else if (viewType == TYPE_FOOTER) {//尾布局
            viewHolder = onCreateFooterViewHolder(parent, viewType);
        } else {
            viewHolder = onCreateItemViewHolder(parent, viewType);
        }
        return viewHolder;
    }

    /**
     * 创建头部布局view
     * @param parent
     * @param viewType
     * @return
     */
    protected VH onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    /**
     * 创建列表布局view
     * @param parent
     * @param viewType
     * @return
     */
    protected abstract VH onCreateItemViewHolder(ViewGroup parent, int viewType);

    /**
     * 创建尾部布局view
     * @param parent
     * @param viewType
     * @return
     */
    protected VH onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public final void onBindViewHolder(final VH holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == TYPE_HEARDER) {
            onBindHeaderViewHolder(holder, position);
        } else if (viewType == TYPE_FOOTER) {
            onBindFooterViewHolder(holder, position);
        } else {
            onBindItemViewHolder(holder, position-getHeaderCount());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClicked(holder.itemView, holder.getAdapterPosition() - getHeaderCount());
                    }
                }
            });
        }
    }


    protected void onBindHeaderViewHolder(VH holder, int position) {
    }

    protected abstract void onBindItemViewHolder(VH holder, int position);


    protected void onBindFooterViewHolder(VH holder, int position) {
    }

    @Override
    public int getItemCount() {
        return getAdapterItemCount() + additionalItems();
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderPosition(position)) {
            return TYPE_HEARDER;
        } else if (isFooterPosition(position)) {
            return TYPE_FOOTER;
        } else {
            return getItemEntityViewType(position-getHeaderCount());
        }
    }

    public int getItemEntityViewType(int position) {
        return TYPE_NORMAL;
    }


    /**
     * 附加到列表的footview
     *
     * @return
     */
    protected int additionalItems() {
        int offset = 0;
        if (hasFooter()) offset++;
        if (hasHeader()) offset++;
        return offset;
    }

    /**
     * 获取item的数量
     *
     * @return
     */
    public abstract int getAdapterItemCount();


    public boolean isHeaderPosition(int position) {
        return hasHeader() && position == 0;
    }

    public boolean isFooterPosition(int position) {
        int last_item = getItemCount() - 1;
        return hasFooter() && position == last_item;
    }


    protected boolean hasFooter() {
        return getFooter() != null;
    }

    protected boolean hasHeader() {
        return getHeader() != null;
    }

    public void setFooter(F footer) {
        this.footer = footer;
    }

    public F getFooter() {
        return footer;
    }

    public H getHeader() {
        return header;
    }

    public void setHeader(H header) {
        this.header = header;
    }

    public int getHeaderCount() {
        return hasHeader() ? 1 : 0;
    }

}
