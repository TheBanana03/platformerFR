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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
public class CustomView extends View {
    private PlayerCharacter player;
    private boolean isMovingLeft;
    private boolean isMovingRight;
    private Paint paint;
    private int floorY;

    private List<Projectile> projectiles;

    public CustomView(Context context) {
        super(context);
        init(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        ViewTreeObserver observer = getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int centerX = getWidth() / 2 - 100;
                int edgeX = getWidth();
                int centerY = getHeight() / 2 + 300;
                floorY = centerY;
                player = new PlayerCharacter(context, centerX, centerY, centerY, edgeX);
                projectiles = new ArrayList<>();
                invalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);

        player.update(floorY);
        player.draw(canvas);

        updateProjectiles();
        drawProjectiles(canvas);

        player.moveCharacter(isMovingLeft, isMovingRight);

        invalidate();
    }

    private void updateProjectiles() {
        Iterator<Projectile> iterator = projectiles.iterator();
        while (iterator.hasNext()) {
            Projectile projectile = iterator.next();
            projectile.moveProjectile(projectile.getDirection());
            if (projectileIsOutOfBounds(projectile)) {
                iterator.remove();
            }
        }
    }

    private void drawProjectiles(Canvas canvas) {
        for (Projectile projectile : projectiles) {
            projectile.draw(canvas);
        }
    }

    private boolean projectileIsOutOfBounds(Projectile projectile) {
        return projectile.getX() < 0 || projectile.getX() + projectile.getWidth() > getWidth();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                double leftButton = getWidth() * 0.2;
                double rightButton = leftButton + (getWidth() * 0.2);
                isMovingLeft = x < leftButton;
                isMovingRight = x > leftButton && x < rightButton;
                return true;

            case MotionEvent.ACTION_UP:
                isMovingLeft = false;
                isMovingRight = false;
                return true;
        }
        return super.onTouchEvent(event);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setupButton(Button buttonJump, Button buttonAttack) {
        buttonJump.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                player.jumpCharacter();
            }
            return true;
        });

        buttonAttack.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                fireProjectile();
            }
            return true;
        });
    }

    private void fireProjectile() {
        // Check if enough time has passed based on attack speed
        long currentTime = System.currentTimeMillis();
        long attackInterval = (long) (1000 / player.getAtkSpeed());
        if (currentTime - player.getLastAttackTime() >= attackInterval) {
            Projectile projectile = new Projectile(getContext(), player.getX(), player.getY(), floorY, getWidth(), player.getDirection());
            projectiles.add(projectile);
            player.setLastAttackTime(currentTime);
        }
    }
}
