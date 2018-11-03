package com.study.zhengxq27.healthfoodlist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.List;

public abstract class MyRecyclerViewAdapter<T> extends RecyclerView.Adapter<MyViewHolder>
{
    private Context context;
    private int layoutId;
    List<T>data;
    private OnItemClickListener onItemClickListener;

    public abstract void convert(MyViewHolder holder, T t);

    public MyRecyclerViewAdapter(Context _context, int _layoutId, List _data)
    {
        context = _context;
        layoutId = _layoutId;
        data = _data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = MyViewHolder.get(context, parent, layoutId);
        return holder;
    }

    @Override
    public void onBindViewHolder( final MyViewHolder holder, int position)
    {
        convert(holder, data.get(position)); // convert函数需要重写，下面会讲

        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onClick(holder.getAdapterPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onItemClickListener.onLongClick(holder.getAdapterPosition());
                    return false;
                }
            });
        }
    }
    @Override
    public int getItemCount()
    {
        return data.size();
    }

    public void setOnItemClickListener(OnItemClickListener _onItemClickListener)
    {
        this.onItemClickListener = _onItemClickListener;
    }

    public void setData( List<T> mdata)
    {
        data = mdata;
    }
}
