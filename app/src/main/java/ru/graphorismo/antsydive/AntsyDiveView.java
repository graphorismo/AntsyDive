package ru.graphorismo.antsydive;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AntsyDiveView extends SurfaceView implements
        Runnable{

    private PlayerSubmarine playerSubmarine;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;

    volatile boolean playing;
    Thread gameThread = null;

    public AntsyDiveView(Context context) {
        super(context);
        ourHolder = getHolder();
        paint = new Paint();
        playerSubmarine = new PlayerSubmarine(context);
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
