package ca.datamagic.noaa.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.logging.Logger;

import ca.datamagic.noaa.logging.LogFactory;

public class SkewTView extends View {
    private static Logger _logger = LogFactory.getLogger(SkewTView.class);
    private static double _aspectRatio = 1.250639386189258;
    private Bitmap _skewTBitmap = null;

    public SkewTView(Context context) {
        super(context);
    }

    public SkewTView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SkewTView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SkewTView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public Bitmap getSkewTBitmap() {
        return _skewTBitmap;
    }

    public void setSkewTBitmap(Bitmap newVal) {
        _skewTBitmap = newVal;
    }

    private static void drawBitmap(Bitmap bitmap, Canvas canvas) {
        double canvasWidth = canvas.getWidth() - 10;
        double canvasHeight = canvas.getHeight() - 10;
        double destWidth = canvasWidth;
        double destHeight = destWidth / _aspectRatio;
        Rect dest = new Rect(5, 5, (int)destWidth, (int)destHeight);
        Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        canvas.drawBitmap(bitmap, src, dest, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        _logger.info("width: " + Integer.toString(canvas.getWidth()));
        _logger.info("height: " + Integer.toString(canvas.getHeight()));
        canvas.drawColor(Color.WHITE);
        if (_skewTBitmap != null) {
            drawBitmap(_skewTBitmap, canvas);
        }
    }
}
