package xj.property.widget;

/**
 * Created by che on 2015/9/16.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import xj.property.R;
import xj.property.utils.DensityUtil;

public class MaskImage extends ImageView{
    int mImageSource=0;
    int mMaskSource=0;
    RuntimeException mException;

    int width,height;

    public MaskImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MaskImage, 0, 0);
        mImageSource = a.getResourceId(R.styleable.MaskImage_image, 0);
        mMaskSource = a.getResourceId(R.styleable.MaskImage_mask, 0);
        width = DensityUtil.dip2px(context, 234);
        height = DensityUtil.dip2px(context, 125);

        if (mImageSource == 0 || mMaskSource == 0) {
            mException = new IllegalArgumentException(a.getPositionDescription() +
                    ": The content attribute is required and must refer to a valid image.");
        }

        if (mException != null)
            throw mException;

        setMaskImageView(mImageSource, mMaskSource);

        a.recycle();
    }

    public void setMaskImageView(Bitmap mImageSource,int mMaskSource){
        /**
         * 主要代码实现
         */
        //获取图片的资源文件
        Bitmap original = mImageSource;
        //获取遮罩层图片
        Bitmap original2 = Bitmap.createScaledBitmap(original, width, height, true);
//        original.recycle();
        Bitmap mask = BitmapFactory.decodeResource(getResources(), mMaskSource);
        Bitmap mask2 = Bitmap.createScaledBitmap(mask, width, height, true);
//        mask.recycle();
        Bitmap result = Bitmap.createBitmap(mask2.getWidth(), mask2.getHeight(), Config.ARGB_8888);
        //将遮罩层的图片放到画布中
        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));//叠加重复的部分，显示下面的
        mCanvas.drawBitmap(original2,  0, 0, null);
        mCanvas.drawBitmap(mask2,  0, 0, paint);
        paint.setXfermode(null);
        setImageBitmap(result);
        setScaleType(ScaleType.CENTER);
//        original2.recycle();
//        mask2.recycle();
    }

    public void setMaskImageView(int mImageSource,int mMaskSource){
        setMaskImageView(BitmapFactory.decodeResource(getResources(), mImageSource),mMaskSource);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Drawable bitmapDrawable = this.getBackground();
//        Log.i("debbug", "bitmap.height="+bitmapDrawable.getBitmap().getHeight());
        Log.i("debbug", "canvas.weight=" + canvas.getWidth() + "  canvas.height=" + canvas.getHeight());
    }
}
