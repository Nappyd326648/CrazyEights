package com.example.nappy.crazyeights;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by nappy on 4/1/2017.
 */

public class TitleView extends View{

    private Bitmap titleGraphic;
    private Bitmap playBtnUp;
    private Bitmap playBtnDn;
    private int screenW;
    private int screenH;
    private boolean playBtnPressed;
    private Context myContext;

    public TitleView(Context context){
        super(context);
        myContext=context;
        titleGraphic = BitmapFactory.decodeResource
                (getResources(),R.drawable.title_graphic);
        playBtnUp = BitmapFactory.decodeResource(getResources(),
                R.drawable.play_button_up);
        playBtnDn = BitmapFactory.decodeResource(getResources(),
                R.drawable.play_button_down);
    }
    @Override
    public void onSizeChanged( int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
        screenW = w;
        screenH = h;

    }
    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawBitmap(titleGraphic,
                (screenW-titleGraphic.getWidth())/2, 0,null);
        if (playBtnPressed){
            canvas.drawBitmap(playBtnDn,(screenW-playBtnUp.getWidth())/2,
                    (int) (screenH*0.7),null);
        }else {
            canvas.drawBitmap(playBtnUp, (
                            screenW - playBtnUp.getWidth()) / 2,
                    (int) (screenH * 0.7), null);
        }

    }
    public boolean onTouchEvent(MotionEvent event){
        int eventaction = event.getAction();
        int X = (int)event.getX();
        int Y = (int)event.getY();

        switch (eventaction){

            case MotionEvent.ACTION_DOWN:
                if (X > (screenW-playBtnUp.getWidth())/2 &&
                    X < ((screenW-playBtnUp.getWidth())/2) +
                        playBtnUp.getWidth()&&
                Y > (int)(screenH*0.7) &&
                        Y < (int)(screenH*0.7)+
                                playBtnUp.getHeight()){
                playBtnPressed = true;
            }
                break;

            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:
                if (playBtnPressed){
                    Intent gameIntent = new Intent(myContext, GameActivity.class);
                    myContext.startActivity(gameIntent);
                }
                playBtnPressed = false;
                break;
        }
        invalidate();
        return true;
    }
}
