package com.example.bookkeeping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<Record> mList;
    private Context mContext;
    private IAdapterListener mListener;

    public ListAdapter(Context context, List<Record> list, IAdapterListener listener) {
        mContext = context;
        mList = list;
        mListener = listener;
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView moneyText;
        TextView timeText;
        LinearLayout itemBox;
        LinearLayout editBox;
        RelativeLayout setBtn;
        RelativeLayout delBtn;

        public ViewHolder(View view) {
            super(view);
            moneyText = (TextView) view.findViewById(R.id.money);
            timeText = (TextView) view.findViewById(R.id.time);
            itemBox = (LinearLayout) view.findViewById(R.id.item_box);
            editBox = (LinearLayout) view.findViewById(R.id.edit_box);
            setBtn = (RelativeLayout) view.findViewById(R.id.edit);
            delBtn = (RelativeLayout) view.findViewById(R.id.del);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Record record = mList.get(position);
        holder.moneyText.setText(record.getMoney());
        holder.timeText.setText(record.getTime());
        // 编辑盒子点击事件
        holder.itemBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int visibleValue = holder.editBox.getVisibility();
                if (visibleValue == 8) {
                    holder.editBox.setVisibility(View.VISIBLE);
                } else if (visibleValue == 0) {
                    holder.editBox.setVisibility(View.GONE);
                }
            }
        });
        // 修改事件
        holder.setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = holder.getLayoutPosition(); // 根据视图实时更新的position
                Record record = mList.get(n);
                mListener.onSetBtnClick(view, record);
            }
        });
        // 删除事件
        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = holder.getLayoutPosition(); // 根据视图实时更新的position
                Record record = mList.get(n);
                removeData(n);
                mListener.onDelBtnClick(view, record.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void removeData(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }
}
