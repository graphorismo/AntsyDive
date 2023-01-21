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
        Runnable {

    Context context;

    private float distanceRemaining;
    private long timeTaken;
    private long timeStarted;
    private long fastestTime;

    private boolean gameEnded;

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

    private int screenX;
    private int screenY;


    volatile boolean playing;
    Thread gameThread = null;

    public AntsyDiveView(Context context, int screenX, int screenY) {
        super(context);
        ourHolder = getHolder();
        paint = new Paint();
        this.screenX = screenX;
        this.screenY = screenY;
        dustList = new ArrayList<OceanDust>();
        this.context = context;
        startGame();


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                playerSubmarine.setBoosting(false);
                break;
            case MotionEvent.ACTION_DOWN:
                playerSubmarine.setBoosting(true);
                if(gameEnded){
                    startGame();
                }
                break;
        }
        return true;
    }

    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();
        }
    }

    private void update() {

        boolean hitDetected = false;

        if (Rect.intersects
                (playerSubmarine.getHitBox(), enemyCreature1.getHitBox())) {
            enemyCreature1.setX(-100);
            hitDetected = true;
        }
        if (Rect.intersects
                (playerSubmarine.getHitBox(), enemyCreature2.getHitBox())) {
            enemyCreature2.setX(-100);
            hitDetected = true;
        }
        if (Rect.intersects
                (playerSubmarine.getHitBox(), enemyCreature3.getHitBox())) {
            enemyCreature3.setX(-100);
            hitDetected = true;
        }
        if (Rect.intersects
                (playerSubmarine.getHitBox(), enemyCreature4.getHitBox())) {
            enemyCreature4.setX(-100);
            hitDetected = true;
        }
        if (Rect.intersects
                (playerSubmarine.getHitBox(), enemyCreature5.getHitBox())) {
            enemyCreature5.setX(-100);
            hitDetected = true;
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

        if(hitDetected) {
            playerSubmarine.reduceShieldStrength();
            if (playerSubmarine.getShieldStrength() < 0) {
                gameEnded = true;
            }
        }

        if(!gameEnded) {
            distanceRemaining -= playerSubmarine.getSpeed();
            timeTaken = System.currentTimeMillis() - timeStarted;
        }

        if(distanceRemaining < 0){
            if(timeTaken < fastestTime) {
                fastestTime = timeTaken;
            }
            distanceRemaining = 0;
            gameEnded = true;
        }


    }

    private void draw() {
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

            // Draw the hud
            if(!gameEnded){
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor(Color.argb(255, 255, 255, 255));
                paint.setTextSize(36);

                paint.setColor(Color.argb(255, 0, 0, 0));

                canvas.drawText("Fastest:" + fastestTime + "s",
                        10, 40, paint);
                canvas.drawText("Time:" + timeTaken + "s",
                        screenX / 2, 40,
                        paint);
                canvas.drawText("Distance:" + distanceRemaining / 1000 + " KM",
                        screenX / 3, screenY - 100, paint);
                canvas.drawText("Shield:" +
                                playerSubmarine.getShieldStrength(), 10,
                        screenY - 100, paint);
                canvas.drawText("Speed:" + playerSubmarine.getSpeed() +
                        " MPS", (screenX / 3) * 2, screenY - 100, paint);
            }else{
                paint.setTextSize(80);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("Game Over", screenX/2, 100, paint);
                paint.setTextSize(25);
                canvas.drawText("Fastest:"+
                        fastestTime + "s", screenX/2, 160, paint);
                canvas.drawText("Time:" + timeTaken +
                        "s", screenX / 2, 200, paint);
                canvas.drawText("Distance remaining:" +
                        distanceRemaining/1000 + " KM",screenX/2, 240, paint);
                paint.setTextSize(80);
                canvas.drawText("Tap to replay!", screenX/2, 350, paint);
            }


                ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {
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

    private void startGame() {
        gameEnded = false;
        playerSubmarine = new PlayerSubmarine(context, screenX, screenY);
        enemyCreature1 = new EnemyCreature(context, screenX, screenY);
        enemyCreature2 = new EnemyCreature(context, screenX, screenY);
        enemyCreature3 = new EnemyCreature(context, screenX, screenY);
        enemyCreature4 = new EnemyCreature(context, screenX, screenY);
        enemyCreature5 = new EnemyCreature(context, screenX, screenY);
        int numSpecs = 40;
        for (int i = 0; i < numSpecs; i++) {
            OceanDust spec = new OceanDust(screenX, screenY);
            dustList.add(spec);
        }
        distanceRemaining = 10000;
        timeTaken = 0;
        timeStarted = System.currentTimeMillis();

    }
}
