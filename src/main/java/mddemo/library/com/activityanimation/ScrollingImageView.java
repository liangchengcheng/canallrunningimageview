package mddemo.library.com.activityanimation;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Author:  梁铖城
 * Email:   1038127753@qq.com
 * Date:   2015年11月7日13:45:30
 * Description: 一直可以动的imageview
 */
public class ScrollingImageView extends View {

    private int speed;
    private Bitmap bitmap;

    private Rect clipBounds = new Rect();
    private int offset = 0;

    private boolean isStarted;

    public ScrollingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ParallaxView, 0, 0);
        try {
            //通过定义的属性获取速度和图片
            speed = typedArray.getDimensionPixelSize(R.styleable.ParallaxView_speed, 10);
            bitmap = BitmapFactory.decodeResource(getResources(), typedArray.getResourceId(R.styleable.ParallaxView_src, 0));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), bitmap.getHeight());
    }

    public void stop() {
        if (isStarted) {
            isStarted = false;
            invalidate();
        }
    }

    public void start() {
        if (!isStarted) {
            isStarted = true;
            postInvalidateOnAnimation();
        }
    }

    private float getBitmapLeft(int layerWidth,int left){
        float bitmapLeft=left;
        if (speed<0){
            bitmapLeft=clipBounds.width()-layerWidth-left;
        }
        return bitmapLeft;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //判断canvas是否存在不存在的话就直接返回
        if (canvas==null){
            return;
        }

        canvas.getClipBounds(clipBounds);
        int normalizedOffset=offset;
        //获取图片的宽度
        int layerWidth=bitmap.getWidth();

        if (offset<-layerWidth){
            offset+=(int)(Math.floor(Math.abs(normalizedOffset)/(float)layerWidth)* layerWidth);
        }

        int left=offset;
        while(left<clipBounds.width()){
            canvas.drawBitmap(bitmap,getBitmapLeft(layerWidth,left),0,null);
            left+=layerWidth;
        }
        if (isStarted){
            offset-=speed;
            postInvalidateOnAnimation();
        }
    }
}
