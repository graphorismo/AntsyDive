package ru.graphorismo.antsydive;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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

    public PlayerSubmarine(Context context, int screenX, int screenY) {
        x = 50;
        y = 50;
        speed = 1;
        bitmap = BitmapFactory.decodeResource
                (context.getResources(), R.drawable.submarine_player);
        boosting = false;
        maxY = screenY - bitmap.getHeight()-100;
        minY = 60;
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
}
