package com.ch.hueclientinfo.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ch.hueclientinfo.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    private List<String> dataList;
    private ItemClickListener itemClickListener;
    public MyAdapter(){
    }

    public void setDataList(List<String> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }
    public interface ItemClickListener {
        void onItemClicked(View view, int position);
    }
    //设置点击回调接口

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    //生成ViewHolderm
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.title, parent, false);
        return new ViewHolder(itemView);
    }

    private String getItem(int position){
        return dataList.get(position);
    }

    //更新列表Item视图(根据需要绑定click事件)

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String str = getItem(position);
        if (str.length()<24) {
            holder.title.setText(str);
        }else {
            holder.title.setText(str.substring(0,22)+"...");
        }
        View view = holder.itemView;
        if(view == null) {

        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener != null){
                    itemClickListener.onItemClicked(v,position);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    //ViewHolder保存每个item视图

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.mytitle);

        }
    }

}