package ru.graphorismo.antsydive;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class AntsyDiveView extends SurfaceView implements
        Runnable{

    private PlayerSubmarine playerSubmarine;
    public EnemyCreature enemyCreature1;
    public EnemyCreature enemyCreature2;
    public EnemyCreature enemyCreature3;
    public EnemyCreature enemyCreature4;
    public EnemyCreature enemyCreature5;
    public ArrayList<OceanDust> dustList;


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
        enemyCreature4 = new EnemyCreature(context, screenX, screenY);
        enemyCreature5 = new EnemyCreature(context, screenX, screenY);

        dustList = new ArrayList<OceanDust>();

        int numSpecs = 40;
        for (int i = 0; i < numSpecs; i++) {
            // Where will the dust spawn?
            OceanDust spec = new OceanDust(screenX, screenY);
            dustList.add(spec);
        }


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

        if(Rect.intersects
                (playerSubmarine.getHitBox(), enemyCreature1.getHitBox())) {
            enemyCreature1.setX(-100);
        }
        if(Rect.intersects
                (playerSubmarine.getHitBox(), enemyCreature2.getHitBox())){
            enemyCreature2.setX(-100);
        }
        if(Rect.intersects
                (playerSubmarine.getHitBox(), enemyCreature3.getHitBox())){
            enemyCreature3.setX(-100);
        }
        if(Rect.intersects
                (playerSubmarine.getHitBox(), enemyCreature4.getHitBox())){
            enemyCreature4.setX(-100);
        }
        if(Rect.intersects
                (playerSubmarine.getHitBox(), enemyCreature5.getHitBox())){
            enemyCreature5.setX(-100);
        }

        playerSubmarine.update();
        int playerSpeed = playerSubmarine.getSpeed();
        enemyCreature1.update(playerSpeed);
        enemyCreature2.update(playerSpeed);
        enemyCreature3.update(playerSpeed);
        enemyCreature4.update(playerSpeed);
        enemyCreature5.update(playerSpeed);
        for (OceanDust dust : dustList) {
            dust.update(playerSpeed);
        }
    }

    private void draw(){
        if (ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();
            canvas.drawColor(Color.argb(255, 0, 125, 125));

            paint.setColor(Color.argb(255, 255, 255, 255));

            for (OceanDust dust : dustList) {
                canvas.drawCircle(dust.getX(), dust.getY(), 3, paint);
            }

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
            canvas.drawBitmap
                    (enemyCreature4.getBitmap(),
                            enemyCreature4.getX(),
                            enemyCreature4.getY(), paint);
            canvas.drawBitmap
                    (enemyCreature5.getBitmap(),
                            enemyCreature5.getX(),
                            enemyCreature5.getY(), paint);

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
