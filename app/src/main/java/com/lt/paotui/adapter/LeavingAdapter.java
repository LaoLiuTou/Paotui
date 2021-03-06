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

public class LeavingAdapter extends BaseRecyclerAdapter<LeavingAdapter.LeavingAdapterViewHolder> {
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

    public LeavingAdapter(List<Map> list, Context context) {
        this.list = list;
        largeCardHeight = DensityUtil.dip2px(context, 150);
        smallCardHeight = DensityUtil.dip2px(context, 100);
    }

    @Override
    public void onBindViewHolder(LeavingAdapterViewHolder holder,final int position, boolean isItem) {
        Map item = list.get(position);


        if(item.get("type").toString().equals("10")){
            holder.type.setText("购票");
        }
        else if(item.get("type").toString().equals("9")){
            holder.type.setText("购票");
        }
        else if(item.get("type").toString().equals("1")){
            holder.type.setText("电信资讯");
        }
        else if(item.get("type").toString().equals("2")){
            holder.type.setText("免费领手机");
        }
        else if(item.get("type").toString().equals("5")){
            holder.type.setText("优惠套餐");
        }
        else if(item.get("type").toString().equals("6")){
            holder.type.setText("手机报价");
        }
        else if(item.get("type").toString().equals("7")){
            holder.type.setText("二手机收售");
        }
        else if(item.get("type").toString().equals("21")){
            holder.type.setText("房屋信息");
        }
        else if(item.get("type").toString().equals("22")){
            holder.type.setText("招聘求职");
        }
        else if(item.get("type").toString().equals("23")){
            holder.type.setText("二手物品");
        }
        else if(item.get("type").toString().equals("24")){
            holder.type.setText("教育培训");
        }
        else if(item.get("type").toString().equals("25")){
            holder.type.setText("饮食");
        }
        else if(item.get("type").toString().equals("26")){
            holder.type.setText("出兑出售");
        }
        else if(item.get("type").toString().equals("27")){
            holder.type.setText("便民信息港");
        }
        else if(item.get("type").toString().equals("28")){
            holder.type.setText("二手车");
        }
        else if(item.get("type").toString().equals("29")){
            holder.type.setText("兼职");
        }
        else if(item.get("type").toString().equals("30")){
            holder.type.setText("惠民信息");
        }
        else if(item.get("type").toString().equals("31")){
            holder.type.setText("家政服务");
        }
        else if(item.get("type").toString().equals("33")){
            holder.type.setText("信用卡");
        }
        else if(item.get("type").toString().equals("34")){
            holder.type.setText("违章查询");
        }
        else if(item.get("type").toString().equals("606")){
            holder.type.setText("家电专区");
        }
        else if(item.get("type").toString().equals("607")){
            holder.type.setText("农村家禽类");
        }
        else if(item.get("type").toString().equals("608")){
            holder.type.setText("延百超市");
        }
        else if(item.get("type").toString().equals("609")){
            holder.type.setText("翼支付专区");
        }
        else if(item.get("type").toString().equals("610")){
            holder.type.setText("优惠专区");
        }
        else if(item.get("type").toString().equals("611")){
            holder.type.setText("商圈代购区");
        }
        else{
            holder.type.setText("其它");
        }


        holder.note.setText(item.get("note").toString());
        holder.status.setText(item.get("state").toString().equals("0")?"未回复":"已回复");
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
    public LeavingAdapterViewHolder getViewHolder(View view) {
        return new LeavingAdapterViewHolder(view, false);
    }

    public void setData(List<Map> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public LeavingAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_leaving, parent, false);
        LeavingAdapterViewHolder vh = new LeavingAdapterViewHolder(v, true);
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

    public class LeavingAdapterViewHolder extends RecyclerView.ViewHolder {

        public View rootView;
        public TextView type;
        public TextView note;
        public TextView status;
        public TextView c_dt;
        public int position;

        public LeavingAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                type = (TextView) itemView
                        .findViewById(R.id.type);
                note = (TextView) itemView
                        .findViewById(R.id.note);
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