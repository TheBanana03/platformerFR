package com.example.platformerfr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Projectile {
    private Bitmap sprite;

    private Rect boundingBox;
    private int x, y;
    private final int width, height;
    private double speed;
    private final Paint paint;
    private int floorY;
    private int wallX;
    private String direction;

    public Projectile(Context context, int x, int y, int floorY, int wallX, String direction) {
        this.x = x;
        this.y = y;
        this.width = 100;
        this.height = 30;
        this.speed = 25;
        this.floorY = floorY;
        this.wallX = wallX;
        this.direction = direction;

        // Initialize bounding box and paint
        boundingBox = new Rect(x, y, x + width, y + height);
        paint = new Paint();
        paint.setColor(0xFF00FF00);

        sprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.playersprite);
        sprite = Bitmap.createScaledBitmap(sprite, width, height, true);
    }

    public void update() {
        // Update bounding box based on the character's position
        boundingBox.set(x, y, x + width, y + height);
    }

    public void draw(Canvas canvas) {
        // Draw the character on the canvas
        canvas.drawBitmap(sprite, x, y, paint);
    }

    public void moveProjectile(String direction) {
        switch (direction) {
            case "Left":
                x -= speed;
                break;
            case "Right":
                x += speed;
                break;
        }

        if (x < 0 || x + width > wallX) {
            destroyProjectile();
        }

        update();
    }

    private void destroyProjectile() {
        if (sprite != null && !sprite.isRecycled()) {
            sprite.recycle();
            sprite = null;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getDirection() {
        return direction;
    }
}
