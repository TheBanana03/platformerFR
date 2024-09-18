package com.example.platformerfr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.util.Log;

public class CustomView extends View {
    private PlayerCharacter player;
    private boolean isMovingLeft;
    private boolean isMovingRight;
    private Paint paint;
    private int floorY;

    public CustomView(Context context) {
        super(context);
        init(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        // Use ViewTreeObserver to wait for layout pass to get width and height
        ViewTreeObserver observer = getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Remove the listener to avoid multiple calls
                getViewTreeObserver().removeOnGlobalLayoutListener(this);

                // Initialize the player character with position in the middle of the screen
                int centerX = getWidth() / 2 - 100;
                int edgeX = getWidth();
                int centerY = getHeight() / 2 + 300;
                floorY = centerY;
                player = new PlayerCharacter(context, centerX, centerY, 200, 200, centerY, edgeX);

                invalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Clear the canvas
        canvas.drawColor(Color.WHITE);

        player.update(floorY);
        player.draw(canvas);

        player.moveCharacter(isMovingLeft, isMovingRight);

        invalidate();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setupButtons(Button buttonLeft, Button buttonRight, Button buttonJump) {
        // Handle left button press

        buttonLeft.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN) {
                isMovingLeft = isTouchInsideButton(event, buttonLeft);
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                isMovingLeft = false;
            }
            return true;
        });

        // Handle right button press
        buttonRight.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN) {
                isMovingRight = isTouchInsideButton(event, buttonRight);
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                isMovingRight = false;
            }
            return true;
        });

        // Handle jump button press
        buttonJump.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN) {
                player.jumpCharacter();
            }
            return true;
        });
    }

    private boolean isTouchInsideButton(MotionEvent event, Button button) {
        float touchX = event.getRawX();
        float touchY = event.getRawY();

        // Get button's location on the screen
        int[] location = new int[2];
        button.getLocationOnScreen(location);

        int[] viewLocation = new int[2];
        this.getLocationOnScreen(viewLocation);

        int buttonX = location[0] - viewLocation[0];
        int buttonY = location[1] - viewLocation[1];

        // Get button's width and height
        int buttonWidth = button.getWidth();
        int buttonHeight = button.getHeight();

        // Convert touch coordinates to button coordinates
        float relativeX = touchX - buttonX;
        float relativeY = touchY - buttonY;

       return relativeX >= 0 - buttonWidth*0.25 && relativeX <= buttonWidth*1.25 &&
               relativeY >= 0 - buttonHeight*0.25 && relativeY <= buttonHeight*1.25;
    }

}
