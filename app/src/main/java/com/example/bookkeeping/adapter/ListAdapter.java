package com.example.bookkeeping.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookkeeping.R;
import com.example.bookkeeping.entity.Record;
import com.example.bookkeeping.fragment.ListFragment;
import com.example.bookkeeping.util.MyUtil;
import com.example.bookkeeping.widget.DatePickerDIY;
import com.example.bookkeeping.widget.SwipeLayout;
import com.example.bookkeeping.widget.SwipeLayoutManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> implements SwipeLayout.OnSwipeStateChangeListener, DatePickerDIY.IOnDateSetListener {
    private static final String TAG = "ListAdapter";

    private DatePickerDIY dpDIY;
    private View clickView;

    private int mType;
    private List<Record> mList;
    private Context mContext;
    private IAdapterListener mListener;

    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd");

    public ListAdapter(Context context, int type, List<Record> list, IAdapterListener listener, DatePickerDIY.IOnDateSetListener dateSetListener) {
        mType = type;
        mContext = context;
        mList = list;
        mListener = listener;
        dpDIY = new DatePickerDIY(context, dateSetListener, true, true, false);
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView moneyText;
        TextView timeText;
        SwipeLayout swipeLayout;
        RelativeLayout setBtn;
        RelativeLayout delBtn;
        CardView stickyWrapper;
        TextView tvYear;
        TextView tvMonth;

        public ViewHolder(View view) {
            super(view);
            swipeLayout = (SwipeLayout) view.findViewById(R.id.swipe_layout);
            moneyText = (TextView) view.findViewById(R.id.money);
            timeText = (TextView) view.findViewById(R.id.time);
            setBtn = (RelativeLayout) view.findViewById(R.id.edit);
            delBtn = (RelativeLayout) view.findViewById(R.id.del);
            stickyWrapper = (CardView) view.findViewById(R.id.sticky_wrapper);
            tvYear = (TextView) view.findViewById(R.id.tv_year);
            tvMonth = (TextView) view.findViewById(R.id.tv_month);
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

        int prevPosition = position - 1 > 0 ? position - 1 : 0;

        // 获取每个项的sticky文本（年月）
        Date curViewDate = MyUtil.convertToDate(record.getTime());
        int curYear = MyUtil.getYearFromDate(curViewDate);
        int curMonth = MyUtil.getMonthFromDate(curViewDate);

        Date prevViewDate = MyUtil.convertToDate(mList.get(prevPosition).getTime());
        int prevYear = MyUtil.getYearFromDate(prevViewDate);
        int prevMonth = MyUtil.getMonthFromDate(prevViewDate);

        // 相同项只显示一个sticky文本
        if (curYear != prevYear || curMonth != prevMonth) {
            holder.stickyWrapper.setVisibility(View.VISIBLE);
            holder.stickyWrapper.findViewById(R.id.tap_query_time).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickView = view;
                    dpDIY.toggleVisible();
                }
            });

            holder.tvYear.setText(String.valueOf(curYear));
            holder.tvMonth.setText(String.valueOf(curMonth));
        } else {
            holder.stickyWrapper.setVisibility(View.GONE);
        }
        holder.itemView.setContentDescription(curYear + "," + curMonth);

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
    public void onDateSet(Date date, int dType) {
        String str = mFormatter.format(date);
        String[] s = str.split("-");
        if (dType == 2) {
            TextView tvYear = (TextView) clickView.findViewById(R.id.tv_year);
            TextView tvMonth = (TextView) clickView.findViewById(R.id.tv_month);
            tvYear.setText(s[0]);
            tvMonth.setText(String.valueOf(Integer.parseInt(s[1])));

            ListFragment.refreshAdapter(mType, Integer.parseInt(s[0]), Integer.parseInt(s[1]));
        }
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
