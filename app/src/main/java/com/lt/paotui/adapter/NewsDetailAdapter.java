package com.lt.paotui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.lt.paotui.R;
import com.lt.paotui.utils.Config;
import com.lt.paotui.utils.DensityUtil;

import java.util.List;
import java.util.Map;

public class NewsDetailAdapter extends BaseRecyclerAdapter<NewsDetailAdapter.NewsDetailAdapterViewHolder> {
    private List<String> list;
    private int largeCardHeight, smallCardHeight;

    private Context context;
    // 利用接口 -> 给RecyclerView设置点击事件
    private ItemClickListener mItemClickListener ;
    public interface ItemClickListener{
        public void onItemClick(int position) ;
    }
    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.mItemClickListener = itemClickListener ;

    }

    public NewsDetailAdapter(List<String> list, Context context) {
        this.list = list;
        this.context=context;
        largeCardHeight = DensityUtil.dip2px(context, 150);
        smallCardHeight = DensityUtil.dip2px(context, 100);
    }

    @Override
    public void onBindViewHolder(  NewsDetailAdapterViewHolder holder,final int position, boolean isItem) {
          String item = list.get(position);


        Glide.with(context).load(Config.url+item).into(holder.image);
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            holder.rootView.getLayoutParams().height = position % 2 != 0 ? largeCardHeight : smallCardHeight;
        }

        // 点击事件一般都写在绑定数据这里，当然写到上边的创建布局时候也是可以的
        if (mItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 这里利用回调来给RecyclerView设置点击事件
                    mItemClickListener.onItemClick(position);
                }
            });
        }
    }

    @Override
    public int getAdapterItemViewType(int position) {
        return 0;
    }

    @Override
    public int getAdapterItemCount() {
        return list.size();
    }

    @Override
    public NewsDetailAdapterViewHolder getViewHolder(View view) {
        return new NewsDetailAdapterViewHolder(view, false);
    }

    public void setData(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public NewsDetailAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_news_detail, parent, false);
        NewsDetailAdapterViewHolder vh = new NewsDetailAdapterViewHolder(v, true);
        return vh;
    }

    public void insert(String item, int position) {
        insert(list, item, position);
    }

    public void remove(int position) {
        remove(list, position);
    }

    public void clear() {
        clear(list);
    }

    public class NewsDetailAdapterViewHolder extends RecyclerView.ViewHolder {

        public View rootView;
        public ImageView image;
        public int position;

        public NewsDetailAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                image = (ImageView) itemView
                        .findViewById(R.id.image);
                rootView = itemView
                        .findViewById(R.id.card_view);
            }

        }
    }

    public String getItem(int position) {
        if (position < list.size())
            return list.get(position);
        else
            return null;
    }

}