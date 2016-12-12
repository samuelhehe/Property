package xj.property.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import xj.property.R;

/**
 * Created by Administrator on 2015/4/9.
 */
public class ItemBar extends LinearLayout{
    public ItemBar(Context context) {
        super(context);
    }

    public ItemBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    private void init(){
       inflate(getContext(), R.layout.item_mine,null);
    }

    public static class Builder{
        Context context;
        Builder(){}
        public  ItemBar create(){
            return  new ItemBar(context);
        }
    }
}
