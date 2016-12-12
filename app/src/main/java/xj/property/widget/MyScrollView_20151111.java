package xj.property.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2015/3/13.
 * <p/>
 * 修改内容：
 * 1. ScrollView 滑动惯性监听
 */
public class MyScrollView_20151111 extends ScrollView {
    public ScrollViewListener scrollViewListener;

    public MyScrollView_20151111(Context context) {
        super(context);
    }

    public MyScrollView_20151111(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView_20151111(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }



    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    public interface ScrollViewListener {
        public void onScrollChanged(View v, int x, int y, int oldx, int oldy);
    }

    public int topHeifght;
    public int topHeight;
    public boolean isDown;
    public boolean isHeight;

//    @Override
//    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
////        if(isHeight){
////            super.onOverScrolled(scrollX, topHeifght, false, false);
////        }else {
//            super.onOverScrolled(scrollX, scrollY, false, false);
////        }
//
//    }

    @Override
    public void fling(int velocityY) {
        super.fling(0);
    }

}
