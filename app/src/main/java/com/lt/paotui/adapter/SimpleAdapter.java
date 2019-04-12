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

public class SimpleAdapter extends BaseRecyclerAdapter<SimpleAdapter.SimpleAdapterViewHolder> {
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

    public SimpleAdapter(List<Map> list, Context context) {
        this.list = list;
        largeCardHeight = DensityUtil.dip2px(context, 150);
        smallCardHeight = DensityUtil.dip2px(context, 100);
    }

    @Override
    public void onBindViewHolder(SimpleAdapterViewHolder holder,final int position, boolean isItem) {
        Map item = list.get(position);



        holder.ordernum.setText(item.get("ordernum").toString());
        holder.driver.setText(item.get("driver").toString());
        holder.status.setText(item.get("status").toString().equals("0")?"已完成":"未完成");
        holder.pay_dt.setText(item.get("pay_dt").toString());
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
    public SimpleAdapterViewHolder getViewHolder(View view) {
        return new SimpleAdapterViewHolder(view, false);
    }

    public void setData(List<Map> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public SimpleAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_recylerview, parent, false);
        SimpleAdapterViewHolder vh = new SimpleAdapterViewHolder(v, true);
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

    public class SimpleAdapterViewHolder extends RecyclerView.ViewHolder {

        public View rootView;
        public TextView ordernum;
        public TextView driver;
        public TextView status;
        public TextView pay_dt;
        public int position;

        public SimpleAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                ordernum = (TextView) itemView
                        .findViewById(R.id.ordernum);
                driver = (TextView) itemView
                        .findViewById(R.id.driver);
                status = (TextView) itemView
                        .findViewById(R.id.status);
                pay_dt = (TextView) itemView
                        .findViewById(R.id.pay_dt);
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