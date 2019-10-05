package com.example.bookkeeping.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

public class SwipeLayout extends FrameLayout {
    private View contentView;
    private View deleteView;
    private int contentWidth;
    private int contentHeight;
    private int deleteWidth;
    private int deleteHeight;

    private ViewDragHelper mViewDragHelper;

    private float startX, startY;

    enum SwipeState {
        Open, Close;
    }

    private SwipeState currentState = SwipeState.Close; // 默认关闭状态

    public SwipeLayout(Context context) {
        super(context, null);
    }
    public SwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init();
    }
    public SwipeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mViewDragHelper = ViewDragHelper.create(this, callback);
    }

    // xml加载完后调用
    // 取得元素
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = getChildAt(0);
        deleteView = getChildAt(1);
    }

    // 取得元素宽高
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        contentWidth = contentView.getMeasuredWidth();
        contentHeight = contentView.getMeasuredHeight();
        deleteWidth = deleteView.getMeasuredWidth();
        deleteHeight = deleteView.getMeasuredHeight();
    }

    // 重新定位元素
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        contentView.layout(0, 0, contentWidth, contentHeight);
        deleteView.layout(contentView.getRight(), 0, contentView.getRight() + deleteWidth, deleteHeight);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = mViewDragHelper.shouldInterceptTouchEvent(ev);

        if (!SwipeLayoutManager.getInstance().isShouldSwipe(this)) {
            SwipeLayoutManager.getInstance().closeCurrentLayout();
            result = true;
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 不能同时打开
        if (!SwipeLayoutManager.getInstance().isShouldSwipe(this)) {
            requestDisallowInterceptTouchEvent(true);   // listview不能滑动
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();

                float dx = moveX - startX;
                float dy = moveY - startY;

                // 水平滚动时，让listview不能上下滑动
                if (Math.abs(dx) > Math.abs(dy)) {
                    // listview不能滑动
                    requestDisallowInterceptTouchEvent(true);
                }
                startX = moveX;
                startY = moveY;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        /**
         * 用于判断是否捕获当前child的触摸事件
         * @param child 当前触摸的子View
         * @param pointerId
         * @return true: 捕获并解析 false: 不处理
         */
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return child == contentView || child == deleteView;
        }

        /**
         * 获取view水平方向的拖拽范围
         * @param child
         * @return
         */
        @Override
        public int getViewHorizontalDragRange(@NonNull View child) {
            return deleteWidth;
        }

        /**
         * 水平拖拽时的回调方法
         * @param child 拖拽的view
         * @param left
         * @param dx 水平方向移动的距离
         * @return
         */
        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            // 限定边界
            if (child == contentView) {
                if (left > 0) {
                    left = 0;
                }
                if (left < -deleteWidth) {
                    left = -deleteWidth;
                }
            } else if (child == deleteView) {
                if (left > contentWidth) {
                    left = contentWidth;
                }
                if (left < contentWidth - deleteWidth) {
                    left = contentWidth - deleteWidth;
                }
            }

            return left;
        }

        /**
         * 当child的位置改变的时候执行,一般用来做其他子View跟随该view移动
         * @param changedView
         * @param left
         * @param top
         * @param dx
         * @param dy
         */
        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);

            if (changedView == contentView) {
                deleteView.layout(deleteView.getLeft() + dx, deleteView.getTop() + dy, deleteView.getRight() + dx, deleteView.getBottom() + dy);
            } else if (changedView == deleteView) {
                contentView.layout(contentView.getLeft() + dx, contentView.getTop() + dy, contentView.getRight() + dx, contentView.getBottom() + dy);
            }

            // 不能同时打开
            // 判断开关状态

            // 关闭状态处理
            if (contentView.getLeft() == 0 && currentState != SwipeState.Close) {
                currentState = SwipeState.Close;

                if (mListener != null) {
                    mListener.onClose(getTag());
                }
                SwipeLayoutManager.getInstance().clearCurrentLayout();
            }
            // 打开状态处理
            else if (contentView.getLeft() == -deleteWidth && currentState != SwipeState.Open) {
                currentState = SwipeState.Open;

                if (mListener != null) {
                    mListener.onOpen(getTag());
                }
                SwipeLayoutManager.getInstance().setSwipeLayout(SwipeLayout.this);
            }
        }

        // 手指松开回调
        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);

            // 滑动超过一半
            if (contentView.getLeft() < - deleteWidth / 2) {
                open();
            } else {
                close();
            }
        }
    };

    public void open() {
        mViewDragHelper.smoothSlideViewTo(contentView, -deleteWidth, contentView.getTop());
        ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
    }

    public void close() {
        mViewDragHelper.smoothSlideViewTo(contentView, 0, contentView.getTop());
        ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();

        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private OnSwipeStateChangeListener mListener;

    public void setOnSwipeStateChangeListener(OnSwipeStateChangeListener listener) {
        mListener = listener;
    }

    public interface OnSwipeStateChangeListener {
        void onOpen(Object tag);
        void onClose(Object tag);
    }
}
