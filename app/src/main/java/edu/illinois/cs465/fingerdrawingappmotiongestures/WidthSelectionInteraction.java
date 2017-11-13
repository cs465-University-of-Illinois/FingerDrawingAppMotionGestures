package edu.illinois.cs465.fingerdrawingappmotiongestures;

import android.app.Activity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;


public class WidthSelectionInteraction extends GestureDetector.SimpleOnGestureListener
        implements View.OnTouchListener {

    private Activity activity;
    private Button sizeButton;
    private GestureDetector mDetector;
    private int index;
    final private static String[] sizes = {"SMALL", "MEDIUM", "LARGE"};


    public WidthSelectionInteraction(Activity activity) {
        this.activity = activity;
        index = 0;

        sizeButton = (Button) activity.findViewById(R.id.stroke_width_button);
        sizeButton.setOnTouchListener(this);

        mDetector = new GestureDetector(activity, this);
    }

    public boolean onTouch(View v, MotionEvent event){
        mDetector.onTouchEvent(event);
        return true;
    }

    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
        if (Math.abs(velocityX) >= Math.abs(velocityY)) {
            if (event1.getX() > event2.getX()) {
                index++;
                if (index >= sizes.length) {
                    index = 0;
                }
            } else {
                index--;
                if (index < 0) {
                    index = sizes.length - 1;
                }
            }
            sizeButton.setText(sizes[index]);
            setStrokeWidth(sizes[index]);
        }
        return true;
    }

    private void setStrokeWidth(String s) {
        float w = 10;
        if (s == "SMALL") {
            w = 10;
        } else if (s == "MEDIUM") {
            w = 20;
        } else if (s == "LARGE") {
            w = 30;
        }
        DrawingView view = (DrawingView) activity.findViewById(R.id.drawing_view);
        if (view != null) {
            view.setStrokeWidth(w);
        }
    }

}

