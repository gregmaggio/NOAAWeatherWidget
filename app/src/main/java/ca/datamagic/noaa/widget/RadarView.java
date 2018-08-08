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

import ca.datamagic.noaa.dto.BitmapsDTO;
import ca.datamagic.noaa.logging.LogFactory;

public class RadarView extends View {
    private static Logger _logger = LogFactory.getLogger(RadarView.class);
    private static double _aspectRatio = 1.089514066496164;
    private BitmapsDTO _backgroundBitmaps = null;
    private Bitmap _radarBitmap = null;

    public RadarView(Context context) {
        super(context);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public BitmapsDTO getBackgroundBitmaps() {
        return _backgroundBitmaps;
    }

    public void setBackgroundBitmaps(BitmapsDTO newVal) {
        _backgroundBitmaps = newVal;
    }

    public Bitmap getRadarBitmap() {
        return _radarBitmap;
    }

    public void setRadarBitmap(Bitmap newVal) {
        _radarBitmap = newVal;
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
        if (_backgroundBitmaps != null) {
            for (int ii = 0; ii < _backgroundBitmaps.size(); ii++) {
                drawBitmap(_backgroundBitmaps.get(ii), canvas);
            }
        }
        if (_radarBitmap != null) {
            drawBitmap(_radarBitmap, canvas);
        }
    }
}
