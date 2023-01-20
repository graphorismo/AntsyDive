package ru.graphorismo.antsydive;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class PlayerSubmarine {

    private boolean boosting;
    private Bitmap bitmap;
    private int x, y;
    private int speed = 0;

    private final int GRAVITY = -12;
    private final int maxY;
    private final int minY;
    private final int MIN_SPEED = 1;
    private final int MAX_SPEED = 20;

    private Rect hitBox;

    public PlayerSubmarine(Context context, int screenX, int screenY) {
        x = 50;
        y = 50;
        speed = 1;
        bitmap = BitmapFactory.decodeResource
                (context.getResources(), R.drawable.submarine_player);
        boosting = false;
        maxY = screenY - bitmap.getHeight()-100;
        minY = 60;

        hitBox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update() {
        if (boosting) {
            speed += 2;
        } else {
            speed -= 5; }
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }
        if (speed < MIN_SPEED) {
            speed = MIN_SPEED;
        }
        y -= speed + GRAVITY;
        if (y < minY) {
            y = minY;
        }
        if (y > maxY) {
            y = maxY;
        }
        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + bitmap.getWidth();
        hitBox.bottom = y + bitmap.getHeight();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setBoosting(boolean boosting) {
        this.boosting = boosting;
    }

    public Rect getHitBox() {
        return hitBox;
    }
}
