package xj.property.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

import java.lang.ref.WeakReference;

import xj.property.utils.DensityUtil;

/**
 * 垂直居中的ImageSpan
 *
 * @author KenChung
 */
public class VerticalImageSpan extends ImageSpan {

    private  Context context;
    private WeakReference<Drawable> mDrawableRef;

    public VerticalImageSpan(Drawable drawable) {
        super(drawable);
    }

    public VerticalImageSpan (Context context, Bitmap bitmap){
        super(context,bitmap);
        this.context  = context;
    }


//
//    public int getSize(Paint paint, CharSequence text, int start, int end,
//                       Paint.FontMetricsInt fontMetricsInt) {
//        Drawable drawable = getDrawable();
//        Rect rect = drawable.getBounds();
//        if (fontMetricsInt != null) {
//            Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
//            int fontHeight = fmPaint.bottom - fmPaint.top;
//            int drHeight = rect.bottom - rect.top;
//
//            int top = drHeight / 2 - fontHeight / 4;
//            int bottom = drHeight / 2 + fontHeight / 4;
//
//            fontMetricsInt.ascent = -bottom;
//            fontMetricsInt.top = -bottom;
//            fontMetricsInt.bottom = top;
//            fontMetricsInt.descent = top;
//        }
//        return rect.right;
//    }
//
//    @Override
//    public void draw(Canvas canvas, CharSequence text, int start, int end,
//                     float x, int top, int y, int bottom, Paint paint) {
//        Drawable drawable = getDrawable();
//        canvas.save();
//        int transY = 0;
//        transY = ((bottom - top) - drawable.getBounds().bottom) / 2 + top;
//        canvas.translate(x, transY);
//        drawable.draw(canvas);
//        canvas.restore();
//    }


    public int getSize(Paint paint, CharSequence text, int start, int end,
                       Paint.FontMetricsInt fm) {
//        Drawable drawable = getDrawable();
//        Rect rect = drawable.getBounds();
//        if (fontMetricsInt != null) {
//            Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
//            int fontHeight = fmPaint.bottom - fmPaint.top;
//            int drHeight = rect.bottom - rect.top;
//
//            int top = drHeight / 2 - fontHeight / 4;
//            int bottom = drHeight / 2 + fontHeight / 4;
//
//            fontMetricsInt.ascent = -bottom;
//            fontMetricsInt.top = -bottom;
//            fontMetricsInt.bottom = top;
//            fontMetricsInt.descent = top;
//        }
//        return rect.right;



        Drawable d = getCachedDrawable();
        Rect rect = d.getBounds();

        if (fm != null) {
            Paint.FontMetricsInt pfm = paint.getFontMetricsInt();
            // keep it the same as paint's fm
            fm.ascent = pfm.ascent;
            fm.descent = pfm.descent;
            fm.top = pfm.top;
            fm.bottom = pfm.bottom;
        }

        return rect.right;

    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, Paint paint) {
//        Drawable drawable = getDrawable();
//        canvas.save();
//        int transY = 0;
//        transY = ((bottom - top) - drawable.getBounds().bottom) / 2 + top;
//        canvas.translate(x, transY);
//        drawable.draw(canvas);
//        canvas.restore();


        Drawable b = getCachedDrawable();
        canvas.save();

        int drawableHeight = b.getIntrinsicHeight();
        int fontAscent = paint.getFontMetricsInt().ascent;
        int fontDescent = paint.getFontMetricsInt().descent;
        int transY =((bottom - top) - b.getBounds().bottom) / 4 + top - DensityUtil.dip2px(this.context,1.3f);

          // align bottom to bottom
                //+(drawableHeight - fontDescent + fontAscent)/4 ;  // align center to center

//        int transY = bottom - b.getBounds().bottom +  // align bottom to bottom
//                (drawableHeight - fontDescent + fontAscent) / 2;  // align center to center

        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();

    }














//
//    @Override
//    public int getSize(Paint paint, CharSequence text,
//                       int start, int end,
//                       Paint.FontMetricsInt fm) {
//        Drawable d = getCachedDrawable();
//        Rect rect = d.getBounds();
//
//        if (fm != null) {
//            Paint.FontMetricsInt pfm = paint.getFontMetricsInt();
//            // keep it the same as paint's fm
//            fm.ascent = pfm.ascent;
//            fm.descent = pfm.descent;
//            fm.top = pfm.top;
//            fm.bottom = pfm.bottom;
//        }
//
//        return rect.right;
//    }
//
//    @Override
//    public void draw(@NonNull Canvas canvas, CharSequence text,
//                     int start, int end, float x,
//                     int top, int y, int bottom, @NonNull Paint paint) {
//        Drawable b = getCachedDrawable();
//        canvas.save();
//
//        int drawableHeight = b.getIntrinsicHeight();
//        int fontAscent = paint.getFontMetricsInt().ascent;
//        int fontDescent = paint.getFontMetricsInt().descent;
//        int transY = bottom - b.getBounds().bottom +  // align bottom to bottom
//                (drawableHeight - fontDescent + fontAscent) / 2;  // align center to center
//
//        canvas.translate(x, transY);
//        b.draw(canvas);
//        canvas.restore();
//    }

    // Redefined locally because it is a private member from DynamicDrawableSpan
    private Drawable getCachedDrawable() {
        WeakReference<Drawable> wr = mDrawableRef;
        Drawable d = null;

        if (wr != null)
            d = wr.get();

        if (d == null) {
            d = getDrawable();
            mDrawableRef = new WeakReference<>(d);
        }

        return d;
    }








}
