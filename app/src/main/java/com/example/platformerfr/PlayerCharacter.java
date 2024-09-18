package com.example.platformerfr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class PlayerCharacter {
    private Bitmap sprite, spriteFlipped;
    private Bitmap currentSprite;

    private Rect boundingBox;
    private int x, y;
    private final int width, height;
    private double xSpeed, ySpeed;
    private final double gravity;
    private boolean isJumping;
    private final Paint paint;
    private int floorY;
    private int wallX;

    public PlayerCharacter(Context context, int x, int y, int width, int height, int floorY, int wallX) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.xSpeed = 10;
        this.ySpeed = 0;
        this.gravity = 0.25;
        this.floorY = floorY;
        this.wallX = wallX;

        // Initialize bounding box and paint
        boundingBox = new Rect(x, y, x + width, y + height);
        paint = new Paint();
        paint.setColor(0xFF00FF00);

        sprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.playersprite);
        sprite = Bitmap.createScaledBitmap(sprite, width, height, true);

        spriteFlipped = BitmapFactory.decodeResource(context.getResources(), R.drawable.playerspriteflipped);
        spriteFlipped = Bitmap.createScaledBitmap(spriteFlipped, width, height, true);

        currentSprite = sprite;

        isJumping = false;
    }

    public void update(int floorY) {
        // Gravity is always applied while in the air
        if (y + height < floorY || ySpeed < 0) {
            y += ySpeed;
            ySpeed += gravity;
        } else {
            // Character hits the floor
            y = floorY - height;
            ySpeed = 0;  // Stop falling
            isJumping = false; // Allow jumping again
        }

        // Update bounding box based on the character's position
        boundingBox.set(x, y, x + width, y + height);
    }

    public void draw(Canvas canvas) {
        // Draw the character on the canvas
        canvas.drawBitmap(currentSprite, x, y, paint);
    }

    public void jumpCharacter() {
        if (!isJumping) {
            ySpeed = -13;  // Initial jump speed (negative so the character goes up)
            isJumping = true;  // Prevent multiple jumps in the air
        }
    }

    public void moveCharacter(Boolean left, Boolean right) {
        if (left) {
            x -= xSpeed;
            currentSprite = sprite;
        }
        if (right) {
            x += xSpeed;
            currentSprite = spriteFlipped;
        }

        // Ensure the character doesn't move out of the screen bounds
        if (x < 0) {
            x = 0;
        }
        if (x + width > wallX) {
            x = wallX - width;
        }

        update(floorY);
    }

    public void setFloorY(int floorY) {
        this.floorY = floorY;
    }

    public void setWallX(int wallX) {
        this.wallX = wallX;
    }
}
