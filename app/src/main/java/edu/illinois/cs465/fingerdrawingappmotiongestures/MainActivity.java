package edu.illinois.cs465.fingerdrawingappmotiongestures;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_menu);
        setActionBar(toolbar);

        ColorSelectionInteraction colorButton = new ColorSelectionInteraction(this);
        WidthSelectionInteraction wButton = new WidthSelectionInteraction(this);
    }

    protected void onResume() {
        super.onResume();
        DrawingView drawingview = (DrawingView) findViewById(R.id.drawing_view);
        if (drawingview != null) {
            drawingview.registerSensorListeners();
        }
    }

    protected void onPause() {
        super.onPause();
        DrawingView drawingview = (DrawingView) findViewById(R.id.drawing_view);
        if (drawingview != null) {
            drawingview.unRegisterSensorListeners();
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_overflow_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.erase_view_menu_item) {
            DrawingView view = (DrawingView) findViewById(R.id.drawing_view);
            if (view != null) {
                view.eraseContent();
            }
        }
        return true;
    }
}
