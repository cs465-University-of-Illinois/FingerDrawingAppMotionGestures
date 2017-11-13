package edu.illinois.cs465.fingerdrawingappmotiongestures;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;

public class DrawingView extends View implements View.OnTouchListener {
    private Bitmap mBitmap; // Bitmap will provide the memory for the pixel colors
    private Paint mPaint;   // Paint provides styles such as line width and color
    private Canvas mCanvas; // Canvas provides tools for drawing such as shapes and lines
    private HashMap pointerMap; // use to map a pointer to its last (x,y) coord
    private OrientationTrackerAccelerometer orientation;
    private ShakeGestureDetector shakeDetector;


    public DrawingView(Context context) {
        super(context);
        initializeDrawingView();
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeDrawingView();
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeDrawingView();
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeDrawingView();
    }

    private void initializeDrawingView() {
        mPaint = new Paint();
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        setColor(Color.BLACK);
        setStrokeWidth(10);
        setOnTouchListener(this);
        pointerMap = new HashMap();
        orientation = new OrientationTrackerAccelerometer(getContext());
        shakeDetector = new ShakeGestureDetector(getContext(), this);

    }

    /* Called by Android when the width and height of the screen changes, such as when it is rotated. */
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        super.onSizeChanged(width, height, oldw, oldh);
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    /* Called by Android when the screen must be refreshed. Do NOT invoke onDraw() directly.
    Instead call invalidate(), which will insert a draw event onto the app's queue, and Android
    will process it by calling onDraw(). */
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
    }


    /* Implement the callback for touch events for DrawingView.
     */
    public boolean onTouch(View v, MotionEvent event) {
        float x, y;
        int index = event.getActionIndex();        // pointer causing the event
        int pointerId = event.getPointerId(index); // ID of pointer causing the event
        int action = event.getActionMasked();      // action causing the event

        if (orientation.getPitch() > 20) {
            return true;
        }

        switch(action) {
            case MotionEvent.ACTION_DOWN:           // catch first touch point
            case MotionEvent.ACTION_POINTER_DOWN:   // catch subsequent touch points
                x = event.getX(index);  // get (x,y) of the pointer causing the event
                y = event.getY(index);

                Point p = new Point((int) x, (int) y);
                pointerMap.put(pointerId, p);
                mCanvas.drawCircle(x, y, mPaint.getStrokeWidth(), mPaint);
                invalidate();  // causes onDraw() to be called
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                pointerMap.remove(pointerId);  // remove when pointer lifts from screen
                break;

            case MotionEvent.ACTION_MOVE:
                /* A move event may capture the movement of multiple pointers. Loop
                through the pointers. Draw line from last touch point to current point.
                 */
                for (int i=0; i < event.getPointerCount(); ++i) {
                    x = event.getX(i);
                    y = event.getY(i);
                    pointerId = event.getPointerId(i);
                    Point last = (Point) pointerMap.get(pointerId);
                    if (last != null) {
                        mCanvas.drawLine(last.x, last.y, x, y, mPaint);
                    }
                    pointerMap.put(pointerId, new Point((int) x, (int) y));
                }
                invalidate();
                break;

            default:
                break;
        }
        return true;
    }

    /* Wrapper for setting the color */
    public void setColor(int color) {
        mPaint.setColor(color);
    }

    /* Wrapper for setting the width of the line stroke */
    public void setStrokeWidth(float s) {
        mPaint.setStrokeWidth(s);
    }

    /* This method will create a clean drawing surface by creating a new bitmap.
     * After creating the bitmap, notice the call to invalidate(). This will cause
     * Android to invoke onDraw(), which will draw the bitmap to the screen. */
    public void eraseContent() {
        if (mBitmap != null) {
            mBitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
            invalidate();
        }
    }


    public void registerSensorListeners() {
        if (orientation != null) {
            orientation.registerSensorListeners();
        }
        if (shakeDetector != null) {
            shakeDetector.registerSensorListeners();
        }
    }

    public void unRegisterSensorListeners() {
        if (orientation != null) {
            orientation.unRegisterSensorListeners();
        }
        if (shakeDetector != null) {
            shakeDetector.unRegisterSensorListeners();
        }
    }

}