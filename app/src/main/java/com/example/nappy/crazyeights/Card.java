package com.example.nappy.crazyeights;

import android.graphics.Bitmap;

/**
 * Created by nappy on 4/10/2017.
 */

public class Card {

    private int id;
    private int suit;
    private int rank;
    private Bitmap bmp;
    private int scoreValue;

    public Card(int newId){
        this.id = newId;
        this.suit= Math.round((float)(this.id/100)*100);
        this.rank = this.id - this.suit;
        if (this.rank == 8){
            this.scoreValue = 50;
        } else if(this.rank == 14){
            this.scoreValue = 1;
        } else if(this.rank > 9 && this.rank < 14){
            this.scoreValue = 10;
        } else {
            this.scoreValue = this.rank;
        }
    }
    public int getScoreValue(){return this.scoreValue;}
    public void setBitmap(Bitmap newBitmap){
        this.bmp = newBitmap;
    }
    public Bitmap getBitmap(){
        return this.bmp;
    }
    public int getId(){
        return id;
    }

    public int getSuit() {
        return this.suit;
    }

    public int getRank() {
        return this.rank;
    }
}
