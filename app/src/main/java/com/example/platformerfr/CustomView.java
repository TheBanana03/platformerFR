package com.example.platformerfr;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

public class CustomView extends View {
    private PlayerCharacter player;
    private boolean isMovingLeft;
    private boolean isMovingRight;
    private boolean isMovingUp;
    private boolean isMovingDown;
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                player.jumpCharacter();
            case MotionEvent.ACTION_MOVE:
                // Determine direction based on touch position
                float x = event.getX();
                float y = event.getY();

                isMovingLeft = x < getWidth() / 3.0;
                isMovingRight = x > getWidth() - getWidth() / 3.0;

                return true;

            case MotionEvent.ACTION_UP:
                // Stop movement when touch is lifted
                isMovingLeft = false;
                isMovingRight = false;
                isMovingUp = false;
                isMovingDown = false;
                return true;
        }
        return super.onTouchEvent(event);
    }
}
