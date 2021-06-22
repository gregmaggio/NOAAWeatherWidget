package ca.datamagic.noaa.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.logging.Logger;

import androidx.annotation.Nullable;
import ca.datamagic.noaa.logging.LogFactory;

public class RadarView extends View {
    private static final int INVALID_POINTER_ID = -1;
    private static Logger _logger = LogFactory.getLogger(RadarView.class);
    private double _aspectRatio = 1.0;
    private Bitmap _backgroundBitmap = null;
    private Bitmap _radarBitmap = null;
    private ScaleGestureDetector _scaleDetector = null;
    private float _scaleFactor = 1.f;
    private float _lastTouchX = 0f;
    private float _lastTouchY = 0f;
    private int _activePointerId = 0;
    private float _posX = 0f;
    private float _posY = 0f;
    private GestureDetector _gestureDetector = null;

    public RadarView(Context context) {
        super(context);
        _scaleDetector = new ScaleGestureDetector(context, new RadarView.ScaleListener());
        _gestureDetector = new GestureDetector(context, new RadarView.DoubleTapListener());
        setLongClickable(false);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        _scaleDetector = new ScaleGestureDetector(context, new RadarView.ScaleListener());
        _gestureDetector = new GestureDetector(context, new RadarView.DoubleTapListener());
        setLongClickable(false);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        _scaleDetector = new ScaleGestureDetector(context, new RadarView.ScaleListener());
        _gestureDetector = new GestureDetector(context, new RadarView.DoubleTapListener());
        setLongClickable(false);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        _scaleDetector = new ScaleGestureDetector(context, new RadarView.ScaleListener());
        _gestureDetector = new GestureDetector(context, new RadarView.DoubleTapListener());
        setLongClickable(false);
    }

    public double getAspectRatio() {
        return _aspectRatio;
    }

    public void setAspectRatio(double newVal) {
        _aspectRatio = newVal;
    }

    public Bitmap getBackgroundBitmap() {
        return _backgroundBitmap;
    }

    public void setBackgroundBitmap(Bitmap newVal) {
        _backgroundBitmap = newVal;
    }

    public Bitmap getRadarBitmap() {
        return _radarBitmap;
    }

    public void setRadarBitmap(Bitmap newVal) {
        _radarBitmap = newVal;
    }

    public void resetScale() {
        _scaleFactor = 1.f;
        _lastTouchX = 0f;
        _lastTouchY = 0f;
        _activePointerId = 0;
        _posX = 0f;
        _posY = 0f;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            _logger.info("Radar Image Touch");
            MainActivity mainActivity = MainActivity.getThisInstance();
            if (mainActivity != null) {
                NonSwipeableViewPager viewPager = mainActivity.getViewPager();
                if (viewPager != null) {
                    viewPager.setEnabled(false);
                }
            }

            // Let the ScaleGestureDetector inspect all events.
            _scaleDetector.onTouchEvent(event);
            _gestureDetector.onTouchEvent(event);

            int action = event.getAction() & MotionEvent.ACTION_MASK;
            _logger.info("action: " + action);

            int pointerCount = event.getPointerCount();
            _logger.info("pointerCount: " + pointerCount);

            switch (action) {
                case MotionEvent.ACTION_DOWN: {
                    _logger.info("ACTION_DOWN");
                    final float x = event.getX();
                    final float y = event.getY();
                    _lastTouchX = x;
                    _lastTouchY = y;
                    _activePointerId = event.getPointerId(0);
                    break;
                }
                case MotionEvent.ACTION_POINTER_DOWN: {
                    _logger.info("ACTION_DOWN");
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    _logger.info("ACTION_MOVE");
                    final int pointerIndex = event.findPointerIndex(_activePointerId);
                    final float x = event.getX(pointerIndex);
                    final float y = event.getY(pointerIndex);

                    // Only move if the ScaleGestureDetector isn't processing a gesture.
                    if (!_scaleDetector.isInProgress()) {
                        final float dx = x - _lastTouchX;
                        final float dy = y - _lastTouchY;

                        _posX += dx;
                        _posY += dy;

                        invalidate();
                    }

                    _lastTouchX = x;
                    _lastTouchY = y;
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    _logger.info("ACTION_UP");
                    _activePointerId = INVALID_POINTER_ID;
                    break;
                }
                case MotionEvent.ACTION_CANCEL: {
                    _logger.info("ACTION_CANCEL");
                    _activePointerId = INVALID_POINTER_ID;
                    break;
                }
                case MotionEvent.ACTION_POINTER_UP: {
                    _logger.info("ACTION_POINTER_UP");
                    final int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                    final int pointerId = event.getPointerId(pointerIndex);
                    if (pointerId == _activePointerId) {
                        // This was our active pointer going up. Choose a new
                        // active pointer and adjust accordingly.
                        final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                        _lastTouchX = event.getX(newPointerIndex);
                        _lastTouchY = event.getY(newPointerIndex);
                        _activePointerId = event.getPointerId(newPointerIndex);
                    }
                    break;
                }
            }
        } catch (Throwable t) {
            _logger.warning("Exception caught in RadarView.onTouchEvent. Exception: " + t.getMessage());
        }
        return true;
    }

    private void drawBitmap(Bitmap bitmap, Canvas canvas) {
        double canvasWidth = canvas.getWidth() - 10;
        double canvasHeight = canvas.getHeight() - 10;
        double destWidth = canvasWidth;
        double destHeight = destWidth / _aspectRatio;
        Rect dest = new Rect(5, 5, (int)destWidth, (int)destHeight);
        Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        canvas.save();
        canvas.translate(_posX, _posY);
        canvas.scale(_scaleFactor, _scaleFactor);
        canvas.drawBitmap(bitmap, src, dest, null);
        canvas.restore();
    }

    public void zoomIn() {
        _scaleFactor *= 2f;

        // Don't let the object get too small or too large.
        _scaleFactor = Math.max(0.1f, Math.min(_scaleFactor, 10.0f));

        invalidate();
    }

    public void zoomOut() {
        _scaleFactor /= 2f;

        // Don't let the object get too small or too large.
        _scaleFactor = Math.max(0.1f, Math.min(_scaleFactor, 10.0f));

        invalidate();
    }

    public void resetZoom() {
        resetScale();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        _logger.info("width: " + Integer.toString(canvas.getWidth()));
        _logger.info("height: " + Integer.toString(canvas.getHeight()));
        canvas.drawColor(Color.WHITE);
        if (_backgroundBitmap != null) {
            drawBitmap(_backgroundBitmap, canvas);
        }
        if (_radarBitmap != null) {
            drawBitmap(_radarBitmap, canvas);
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            _scaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            _scaleFactor = Math.max(0.1f, Math.min(_scaleFactor, 10.0f));

            invalidate();
            return true;
        }
    }

    private class DoubleTapListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTapEvent(MotionEvent event) {
            _logger.info("Double Tap Event");
            _scaleFactor *= 2f;
            _posX = 0;
            _posY = 0;

            // Don't let the object get too small or too large.
            _scaleFactor = Math.max(0.1f, Math.min(_scaleFactor, 10.0f));

            invalidate();
            return true;
        }
    }
}
