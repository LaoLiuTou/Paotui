package com.lt.paotui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lt.paotui.R;
import com.lt.paotui.utils.Config;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/5/6.
 */

public class MyRVAdapter extends RecyclerView.Adapter<MyRVAdapter.ViewHolder> {
    private Context context;
    private List<Map> list;
    // 利用接口 -> 给RecyclerView设置点击事件
    private ItemClickListener mItemClickListener ;
    public interface ItemClickListener{
        public void onItemClick(int position) ;
    }
    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.mItemClickListener = itemClickListener ;

    }
    public MyRVAdapter(List<Map> list, Context context){
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_rv_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        holder.rv_item_image = (ImageView) view.findViewById(R.id.rv_item_image);
        holder.rv_item_tv = (TextView) view.findViewById(R.id.rv_item_tv);
        holder.rv_item_time = (TextView) view.findViewById(R.id.rv_item_time);
        holder.bottomline = (TextView) view.findViewById(R.id.bottomline);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Map item = list.get(position);
        Glide.with(context).load(Config.url+item.get("image").toString()).into(holder.rv_item_image);
        holder.rv_item_tv.setText(item.get("title").toString());
        holder.rv_item_time.setText("发布时间："+item.get("c_dt").toString());
        if(position==2){
            holder.bottomline.setVisibility(View.GONE);
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
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }
        ImageView rv_item_image;
        TextView rv_item_tv;
        TextView rv_item_time;
        TextView bottomline;
    }

}
