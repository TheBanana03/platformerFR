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
                player = new PlayerCharacter(context, centerX, centerY, centerY, edgeX);

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                // Determine direction based on touch position
                float x = event.getX();
                float y = event.getY();

                double leftButton = getWidth() * 0.2;
                double rightButton = leftButton + (getWidth() * 0.2);

                isMovingLeft = x < leftButton;
                isMovingRight = x > leftButton && x < rightButton;

                return true;

            case MotionEvent.ACTION_UP:
                // Stop movement when touch is lifted
                isMovingLeft = false;
                isMovingRight = false;
                return true;
        }
        return super.onTouchEvent(event);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setupButton(Button buttonJump) {
        // Handle jump button press
        buttonJump.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN) {
                player.jumpCharacter();
            }
            return true;
        });
    }
}
