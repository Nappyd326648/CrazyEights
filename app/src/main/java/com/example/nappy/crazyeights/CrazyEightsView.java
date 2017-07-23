package com.example.nappy.crazyeights;

/**
 * Created by nappy on 7/8/2017.
 */

import android.view.View;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

public class CrazyEightsView extends View {
    private Paint redPaint = new Paint();
    private int circleX;
    private int circleY;
    private float radius;

    public CrazyEightsView (Context context){
        super(context);
        this.redPaint.setAntiAlias(true);
        this.redPaint.setColor(Color.RED);
        this.circleX = 100;
        this.circleY = 100;
        this.radius = 30.0F;
    }

    protected void onDraw(Canvas canvas){
        canvas.drawCircle((float)this.circleX,(float)this.circleY,this.radius,this.redPaint);

    }


    public boolean onTouchEvent (MotionEvent event){
        int eventaction = event.getAction();
        int X =(int)event.getX();
        int Y =(int)event.getY();
        switch (eventaction){
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                this.circleX = X;
                this.circleY = Y;
                break;
        }
        this.invalidate();
        return true;
    }

}
