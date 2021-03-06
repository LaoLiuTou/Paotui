package com.lt.paotui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.lt.paotui.R;
import com.lt.paotui.utils.DensityUtil;

import java.util.List;
import java.util.Map;

public class OrderptAdapter extends BaseRecyclerAdapter<OrderptAdapter.OrderptAdapterViewHolder> {
    private List<Map> list;
    private int largeCardHeight, smallCardHeight;

    // 利用接口 -> 给RecyclerView设置点击事件
    private ItemClickListener mItemClickListener ;
    public interface ItemClickListener{
        public void onItemClick(int position) ;
    }
    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.mItemClickListener = itemClickListener ;

    }

    public OrderptAdapter(List<Map> list, Context context) {
        this.list = list;
        largeCardHeight = DensityUtil.dip2px(context, 150);
        smallCardHeight = DensityUtil.dip2px(context, 100);
    }

    @Override
    public void onBindViewHolder(OrderptAdapterViewHolder holder,final int position, boolean isItem) {
        Map item = list.get(position);



        holder.ordernum.setText(item.get("ordernum").toString());
        holder.status.setText(item.get("status").toString().equals("0")?"未完成":"已完成");
        holder.c_dt.setText(item.get("c_dt").toString());
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
    public OrderptAdapterViewHolder getViewHolder(View view) {
        return new OrderptAdapterViewHolder(view, false);
    }

    public void setData(List<Map> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public OrderptAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_orderpt, parent, false);
        OrderptAdapterViewHolder vh = new OrderptAdapterViewHolder(v, true);
        return vh;
    }

    public void insert(Map<String,String> item, int position) {
        insert(list, item, position);
    }

    public void remove(int position) {
        remove(list, position);
    }

    public void clear() {
        clear(list);
    }

    public class OrderptAdapterViewHolder extends RecyclerView.ViewHolder {

        public View rootView;
        public TextView ordernum;
        public TextView status;
        public TextView c_dt;
        public int position;

        public OrderptAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                ordernum = (TextView) itemView
                        .findViewById(R.id.ordernum);
                status = (TextView) itemView
                        .findViewById(R.id.status);
                c_dt = (TextView) itemView
                        .findViewById(R.id.c_dt);
                rootView = itemView
                        .findViewById(R.id.card_view);
            }

        }
    }

    public Map<String,String> getItem(int position) {
        if (position < list.size())
            return list.get(position);
        else
            return null;
    }

}