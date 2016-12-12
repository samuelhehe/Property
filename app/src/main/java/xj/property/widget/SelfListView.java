package xj.property.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

/**
 * 作者：asia on 2016/1/19 12:39
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class SelfListView extends LinearLayout {
    private BaseAdapter mSelfAdapter;

    public SelfListView(Context context) {
        super(context);
    }

    public SelfListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /**
     * 删除ListView中上一次渲染的View，并添加新View。
     */
    public void buildList() {
        if (mSelfAdapter == null) {

        }

        if (getChildCount() > 0) {
            removeAllViews();
        }

        int count = mSelfAdapter.getCount();

        for(int i = 0 ; i < count ; i++) {
            View view = mSelfAdapter.getView(i, null, null);
            if (view != null) {
                addView(view, i);
            }
        }
    }


    public BaseAdapter getSelfAdapter() {
        return mSelfAdapter;
    }

    /**
     * 设置Adapter。
     *
     * @param selfAdapter
     */
    public void setAdapter(BaseAdapter selfAdapter) {
        this.mSelfAdapter = selfAdapter;
        buildList();
    }
}
