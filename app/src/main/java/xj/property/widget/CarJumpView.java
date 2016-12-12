package xj.property.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ScrollView;

import java.util.ArrayList;

import xj.property.R;
import xj.property.utils.DensityUtil;

/**
 * Created by Administrator on 2015/4/20.
 */
public class CarJumpView extends ScrollView {
    int width, height;
    Bitmap bm;
    Paint p = new Paint();
    int count = 18;
    Handler handler;
    int differ;
    ArrayList<step> steps = new ArrayList<>();

    public CarJumpView(Context context) {
        super(context);
        bm = BitmapFactory.decodeResource(getResources(), R.drawable.snacks_add);
    }

    public CarJumpView(Context context, AttributeSet attrs) {
        super(context, attrs);
        bm = BitmapFactory.decodeResource(getResources(), R.drawable.snacks_add);
    }

    public CarJumpView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bm = BitmapFactory.decodeResource(getResources(), R.drawable.snacks_add);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                boolean flag = false;
                for (int i = 0; i < steps.size(); i++)
                    if (steps.get(i).points.size() > 0) {
                        steps.get(i).points.remove(0);
                        flag = true;
                    }
                if (flag)
                    invalidate();
            }
        };
        differ = DensityUtil.dip2px(getContext(), 30);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < steps.size(); i++) {
            if (steps.get(i).points.isEmpty()) continue;
            Point point = steps.get(i).points.get(0);
            canvas.drawBitmap(bm, point.x, point.y, p);
        }
        handler.sendEmptyMessageDelayed(1, 25);

    }

    public void startJump(int startX, int startY, int targetX, int targetY) {
        step step = new step();
        step.points = new ArrayList<>();
        differ = startX / 25 + startY / 15;
        for (int i = 0; i < count; i++) {
//            step.points.add(new Point(startX+(targetX-startX)/count*i,startY+(targetY-startY)/count*i));
//            int y=(startY+(height-startY)/count*i);
            //终点减去起点，除以步长。加起点
            int x = startX + (targetX - startX) / count * (i + 1);
//            int y=startY-differ*3;
            float y = 0;
            if (i < count / 3) {
                y = startY - differ * i;
            } else {
//                y=(height-y)/count*(i+1)+y;
                y = (i - count / 3) * (i - count / 3) * 9 + startY - differ * 3;
            }
            step.points.add(new Point(x, (int) y));
        }
        steps.add(step);
        invalidate();
    }

    class step {
        ArrayList<Point> points;
    }

}
