package com.example.bookkeeping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookkeeping.R;
import com.example.bookkeeping.entity.Record;
import com.example.bookkeeping.widget.SwipeLayout;
import com.example.bookkeeping.widget.SwipeLayoutManager;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> implements SwipeLayout.OnSwipeStateChangeListener {
    private static final String TAG = "ListAdapter";

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
        SwipeLayout swipeLayout;
        RelativeLayout setBtn;
        RelativeLayout delBtn;

        public ViewHolder(View view) {
            super(view);
            swipeLayout = (SwipeLayout) view.findViewById(R.id.swipe_layout);
            moneyText = (TextView) view.findViewById(R.id.money);
            timeText = (TextView) view.findViewById(R.id.time);
            setBtn = (RelativeLayout) view.findViewById(R.id.edit);
            delBtn = (RelativeLayout) view.findViewById(R.id.del);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.record, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Record record = mList.get(position);
        holder.swipeLayout.setTag(position);
        holder.swipeLayout.setOnSwipeStateChangeListener(this);

        holder.moneyText.setText(record.getMoney());
        holder.timeText.setText(record.getTime());
        // 修改事件
        holder.setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = holder.getLayoutPosition(); // 根据视图实时更新的position
                Record record = mList.get(n);
                SwipeLayoutManager.getInstance().closeCurrentLayout();
                SwipeLayoutManager.getInstance().clearCurrentLayout();
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
                SwipeLayoutManager.getInstance().closeCurrentLayout();
                SwipeLayoutManager.getInstance().clearCurrentLayout();
                mListener.onDelBtnClick(view, record.getId());
            }
        });
    }

    @Override
    public void onOpen(Object tag) {

    }

    @Override
    public void onClose(Object tag) {

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void removeData(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }
    public interface IAdapterListener {
        void onSetBtnClick(View view, Record record);
        void onDelBtnClick(View view, int id);
    }
}
