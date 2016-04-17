package com.example.danyatazyeen.orderofoperations;

/**
 * Created by danyatazyeen on 4/16/16.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class BounceSurfaceView extends SurfaceView implements
        SurfaceHolder.Callback {

    private BounceThread bounceThread;
    private SurfaceHolder holder;

    public BounceSurfaceView (Context context, AttributeSet attrs){
        super (context, attrs);

        holder = getHolder();
        holder.addCallback(this);

        //CREATE A NEW THREAD
        bounceThread = new BounceThread(holder);
    }

    //IMPLEMENT THE INHERITED ABSTRACT METHODS
    public void surfaceChanged (SurfaceHolder holder, int format,
                                int width, int height) {}
    public void surfaceCreated (SurfaceHolder holder) {
        bounceThread.start();
    }

    public void surfaceDestroyed (SurfaceHolder holder) {
        bounceThread.endBounce();
        Thread dummyThread = bounceThread;
        bounceThread = null;
        dummyThread.interrupt();
    }
}