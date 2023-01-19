package ru.graphorismo.antsydive;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AntsyDiveView extends SurfaceView implements
        Runnable{

    private PlayerSubmarine playerSubmarine;
    public EnemyCreature enemyCreature1;
    public EnemyCreature enemyCreature2;
    public EnemyCreature enemyCreature3;


    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;


    volatile boolean playing;
    Thread gameThread = null;

    public AntsyDiveView(Context context, int screenX, int screenY) {
        super(context);
        ourHolder = getHolder();
        paint = new Paint();

        playerSubmarine = new PlayerSubmarine(context, screenX, screenY);
        enemyCreature1 = new EnemyCreature(context, screenX, screenY);
        enemyCreature2 = new EnemyCreature(context, screenX, screenY);
        enemyCreature3 = new EnemyCreature(context, screenX, screenY);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                playerSubmarine.setBoosting(false);
                break;
            case MotionEvent.ACTION_DOWN:
                playerSubmarine.setBoosting(true);
                break;
        }
        return true;
    }

    @Override
    public void run() {
        while (playing){
            update();
            draw();
            control();
        }
    }

    private void update(){
        playerSubmarine.update();
        int playerSpeed = playerSubmarine.getSpeed();
        enemyCreature1.update(playerSpeed);
        enemyCreature2.update(playerSpeed);
        enemyCreature3.update(playerSpeed);
    }

    private void draw(){
        if (ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();
            canvas.drawColor(Color.argb(255, 255, 255, 255));

            canvas.drawBitmap(
                    playerSubmarine.getBitmap(),
                    playerSubmarine.getX(),
                    playerSubmarine.getY(),
                    paint);

            canvas.drawBitmap
                    (enemyCreature1.getBitmap(),
                            enemyCreature1.getX(),
                            enemyCreature1.getY(), paint);
            canvas.drawBitmap
                    (enemyCreature2.getBitmap(),
                            enemyCreature2.getX(),
                            enemyCreature2.getY(), paint);
            canvas.drawBitmap
                    (enemyCreature3.getBitmap(),
                            enemyCreature3.getX(),
                            enemyCreature3.getY(), paint);


            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control(){
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {

        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
}
