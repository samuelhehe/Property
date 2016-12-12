package xj.property.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

public class RoTateTextView2 extends TextView {


    public RoTateTextView2(Context context) {
        super(context);
    }

    public RoTateTextView2(Context context, AttributeSet attrs) {
        super(context, attrs);  
    }  
   
    @Override  
    protected void onDraw(Canvas canvas) {
        //倾斜度45,上下左右居中
        canvas.rotate(-90, getMeasuredWidth()/3, getMeasuredHeight()/3);
        super.onDraw(canvas);
    }  
           
}  