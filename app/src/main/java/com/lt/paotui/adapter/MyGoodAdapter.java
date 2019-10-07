package com.lt.paotui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.lt.paotui.R;
import com.lt.paotui.utils.Config;

import java.util.List;
import java.util.Map;

public class MyGoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Map> list;
    private Context context;
    private final int Line = 1;
    private final int Grid = 2;
    private int type;

    private LeavingAdapter.ItemClickListener mItemClickListener ;
    public interface ItemClickListener{
        public void onItemClick(int position) ;
    }
    public void setOnItemClickListener(LeavingAdapter.ItemClickListener itemClickListener){
        this.mItemClickListener = itemClickListener ;

    }

    public MyGoodAdapter(List<Map> list, Context context, int type) {
        this.list = list;
        this.context = context;
        this.type = type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (i) {
            case Line:
                view = LayoutInflater.from(context).inflate(R.layout.item_goods_xrecyclelin, null);
                holder = new LineHolder(view);
                break;
            case Grid:
                view = LayoutInflater.from(context).inflate(R.layout.item_goods_xrecyclegrid, null);
                holder = new GridHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        //判断前一个参数是否是后一个参数的一个实例
        if (viewHolder instanceof LineHolder) {
            ((LineHolder) viewHolder).lin_item_price.setText("型号："+list.get(i).get("brand")+" "+list.get(i).get("model")+"");
            ((LineHolder) viewHolder).lin_item_title.setText(list.get(i).get("title")+"");
            ((LineHolder) viewHolder).lin_item_salenum.setText("成色："+list.get(i).get("oldlevel")+"");

            JSONArray imageList = JSON.parseArray(list.get(i).get("banners")+"");
            Glide.with(context).load(Config.url+imageList.get(0)).into(((LineHolder) viewHolder).lin_item_img);
        } else {
            ((GridHolder) viewHolder).grid_item_price.setText("型号："+list.get(i).get("brand")+" "+list.get(i).get("model")+"");
            ((GridHolder) viewHolder).grid_item_title.setText(list.get(i).get("title")+"");
            ((GridHolder) viewHolder).grid_item_salenum.setText("成色："+list.get(i).get("oldlevel")+"");
            JSONArray imageList = JSON.parseArray(list.get(i).get("banners")+"");
            Glide.with(context).load(Config.url+imageList.get(0)).into(((GridHolder) viewHolder).grid_item_img);
        }
        // 点击事件一般都写在绑定数据这里，当然写到上边的创建布局时候也是可以的
        if (mItemClickListener != null){
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 这里利用回调来给RecyclerView设置点击事件
                    mItemClickListener.onItemClick(i);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 点击切换的时候调用过这个方法
     * 商品排列的方式 1：垂直列表排列 2：网格
     *
     * @param type
     */
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getItemViewType(int position) {
        return type;
    }

    //    设置线性视图
    class LineHolder extends RecyclerView.ViewHolder {

        private ImageView lin_item_img;
        private TextView lin_item_title;
        private TextView lin_item_price;
        private TextView lin_item_salenum;

        public LineHolder(@NonNull View itemView) {
            super(itemView);
            lin_item_img = itemView.findViewById(R.id.lin_item_img);
            lin_item_title = itemView.findViewById(R.id.lin_item_title);
            lin_item_price = itemView.findViewById(R.id.lin_item_price);
            lin_item_salenum = itemView.findViewById(R.id.lin_item_salenum);
        }
    }

    //    设置网格视图
    class GridHolder extends RecyclerView.ViewHolder {

        private ImageView grid_item_img;
        private TextView grid_item_title;
        private TextView grid_item_price;
        private TextView grid_item_salenum;

        public GridHolder(@NonNull View itemView) {
            super(itemView);
            grid_item_img = itemView.findViewById(R.id.grid_item_img);
            grid_item_title = itemView.findViewById(R.id.grid_item_title);
            grid_item_price = itemView.findViewById(R.id.grid_item_price);
            grid_item_salenum = itemView.findViewById(R.id.grid_item_salenum);
        }
    }


}
