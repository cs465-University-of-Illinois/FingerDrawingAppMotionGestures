package edu.illinois.cs465.fingerdrawingappmotiongestures;

import android.app.Activity;
import android.graphics.Color;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class ColorSelectionInteraction extends GestureDetector.SimpleOnGestureListener
        implements View.OnTouchListener {

    private Activity activity;
    private Button colorButton;
    private GestureDetector mDetector;
    private int currentColorIndex;
    final private static String[] colors = {"BLACK", "RED", "GREEN", "BLUE"};

    public ColorSelectionInteraction(Activity activity) {
        this.activity = activity;
        currentColorIndex = 0;
        colorButton = (Button) activity.findViewById(R.id.stroke_color_button);
        colorButton.setOnTouchListener(this);

        mDetector = new GestureDetector(activity, this);
    }

    public boolean onTouch(View v, MotionEvent event) {
        mDetector.onTouchEvent(event);
        return true;
    }

    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
        if (Math.abs(velocityX) >= Math.abs(velocityY))  // test if mostly horizontal swipe
        {
            if (event1.getX() > event2.getX())  // test direction (this is right-to-left)
            {
                currentColorIndex++;
                if (currentColorIndex >= colors.length) {
                    currentColorIndex = 0;
                }
            } else {
                currentColorIndex--;
                if (currentColorIndex < 0) {
                    currentColorIndex = colors.length - 1;
                }
            }
            colorButton.setText(colors[currentColorIndex]);
            setColor(colors[currentColorIndex]);
        }
        return true;
    }


    private void setColor(String s) {
        int c = Color.BLACK;
        if (s == "BLACK") {
            c = Color.BLACK;
        } else if (s == "RED") {
            c = Color.RED;
        } else if (s == "GREEN") {
            c = Color.GREEN;
        } else if (s == "BLUE") {
            c = Color.BLUE;
        }
        DrawingView view = (DrawingView) activity.findViewById(R.id.drawing_view);
        if (view != null) {
            view.setColor(c);
        }
    }
}

