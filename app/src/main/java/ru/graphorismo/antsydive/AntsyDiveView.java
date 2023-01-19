package ru.graphorismo.antsydive;

import android.content.Context;
import android.view.SurfaceView;

public class AntsyDiveView extends SurfaceView implements
        Runnable{

    volatile boolean playing;
    Thread gameThread = null;

    public AntsyDiveView(Context context) {
        super(context);
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
    }
    private void draw(){
    }
    private void control(){
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
