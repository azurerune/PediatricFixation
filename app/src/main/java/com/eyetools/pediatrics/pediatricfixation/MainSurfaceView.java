package com.eyetools.pediatrics.pediatricfixation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by xu on 8/19/15.
 */
public class MainSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    static String TAG = "PediatricFixation";
    int mAnimation_Index = 0;
    public MainSurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public MainSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        tryDrawing(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        tryDrawing(holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public void initSurface(int animation_index) {
        mAnimation_Index = animation_index;
    }

    void tryDrawing(SurfaceHolder holder) {
        Canvas canvas = holder.lockCanvas();
        Log.i(TAG, "Trying to draw");
        if (canvas != null) {
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setTextSize(16);
            canvas.drawText("testing", 40, 40, paint);
            holder.unlockCanvasAndPost(canvas);
        }
    }
}
