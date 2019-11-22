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

public class HotLineAdapter extends RecyclerView.Adapter<HotLineAdapter.ViewHolder> {
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
    public HotLineAdapter(List<Map> list, Context context){
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_hotline_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        holder.image_item = (ImageView) view.findViewById(R.id.image_item);
        holder.name_item = (TextView) view.findViewById(R.id.name_item);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Map item = list.get(position);
        Glide.with(context).load(Config.url+item.get("image").toString()).into(holder.image_item);
        holder.name_item.setText(item.get("title").toString());
        //holder.rv_item_time.setText("发布时间："+item.get("c_dt").toString());


        // 点击事件一般都写在绑定数据这里，当然写到上边的创建布局时候也是可以的
        if (mItemClickListener != null){
            holder.image_item.setOnClickListener(new View.OnClickListener() {
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
        ImageView image_item;
        TextView name_item;
    }

}
