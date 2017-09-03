package com.example.nappy.crazyeights;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Paint;
import android.widget.Toast;
import android.app.Dialog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import android.graphics.Color;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by nappy on 4/9/2017.
 */

public class GameView extends View {
    private int screenW;
    private int screenH;
    private Context myContext;
    private List <Card> deck = new ArrayList<Card>();
    private Paint whitePaint;
    private Paint redPaint;
    private List <Card> myHand = new ArrayList<Card>();
    private List <Card> oppHand1 = new ArrayList<Card>();
    private List <Card> oppHand2 = new ArrayList<Card>();
    private List <Card> oppHand3 = new ArrayList<Card>();
    private int myScore = 0;
    private int oppScore = 0;
    private float scale;
    private Bitmap cardBack;
    private List <Card> discardPile = new ArrayList<Card>();
    private boolean myTurn;
    private boolean isreversed = false;
    private int comTurn;
    private ComputerPlayer computerPlayer = new ComputerPlayer();
    private int movingCardIdx = -1;
    private int movingX;
    private int movingY;
    private int validRank = 0;
    private int validSuit = 0;
    private Bitmap nextCardButton;
    private int scoreThisHand = 0;
    private int scaledCardW;
    private int scaledCardH;


    public GameView (Context context){
        super(context);
        myContext = context;
        scale = myContext.getResources().getDisplayMetrics().density;
        whitePaint = new Paint();
        whitePaint.setAntiAlias(true);
        whitePaint.setColor(Color.WHITE);
        whitePaint.setStyle(Paint.Style.STROKE);
        whitePaint.setTextAlign(Paint.Align.LEFT);
        whitePaint.setTextSize(scale* 15);
        redPaint = new Paint();
        redPaint.setAntiAlias(true);
        redPaint.setColor(Color.RED);
        redPaint.setStyle(Paint.Style.STROKE);
        redPaint.setTextAlign(Paint.Align.LEFT);
        redPaint.setTextSize(scale* 15);
    }
    public void onSizeChanged(int w,int h,int oldw,int oldh){
        super.onSizeChanged(w,h,oldw,oldh);
        screenW = w;
        screenH = h;
        Bitmap tempBitmap = BitmapFactory.decodeResource(myContext.
                        getResources(), R.drawable.card_back);

        scaledCardW = (int) (screenW / 8);
        scaledCardH = (int)(scaledCardW * 1.28);
        cardBack = Bitmap.createScaledBitmap(tempBitmap,
                scaledCardW ,scaledCardH,false);

        nextCardButton = BitmapFactory.decodeResource
                (getResources(), R.drawable.arrow_next);
        initCards();
        dealCard();
        drawCard(discardPile);
        validSuit = discardPile.get(0).getSuit();
        validRank = discardPile.get(0).getRank();
        comTurn = new Random().nextInt(3);
        while (comTurn==0){
            comTurn = new Random().nextInt(3);
        }
        myTurn = (new Random()).nextBoolean();
        if(!myTurn){
            makeComputerPlay();
        }

    }


    protected void onDraw (Canvas canvas){

        for(int i = 0; i < oppHand1.size(); ++i){
            canvas.drawBitmap(cardBack, i * (scale * 5),
                    redPaint.getTextSize()+(50*scale), null);
        }
        canvas.drawBitmap(cardBack, (screenW / 2 - cardBack.getWidth() -10),
                (screenH / 2) - (cardBack.getHeight() / 2),null);
        canvas.drawText(Integer.toString(oppHand1.size()),
                50, redPaint.getTextSize() +70, redPaint);

        canvas.drawBitmap(cardBack, (screenW / 2 - cardBack.getWidth() -450),
                (screenH / 2) - (cardBack.getHeight() / 2),null);
        canvas.drawText(Integer.toString(oppHand2.size()),
                50, redPaint.getTextSize() +1200, redPaint);

        canvas.drawBitmap(cardBack, (screenW / 2 - cardBack.getWidth() +650),
                (screenH / 2) - (cardBack.getHeight() / 2),null);
        canvas.drawText(Integer.toString(oppHand3.size()),
                1400, redPaint.getTextSize() +1200, redPaint);
        if(!discardPile.isEmpty()){
            canvas.drawBitmap((discardPile.get(0)).getBitmap(),
                    (screenW / 2 )+ 10, (screenH/2) -
                            (cardBack.getHeight() / 2), null);
        }
        if (myHand.size() > 7){
            canvas.drawBitmap(nextCardButton,
                    screenW - nextCardButton.getWidth()- (30 * scale),
                    screenH - nextCardButton.getHeight()
                            - scaledCardH - (90 * scale), null);
        }

        for(int i = 0; i < myHand.size(); ++ i){
            if (i == movingCardIdx) {
                canvas.drawBitmap((myHand.get(i)).getBitmap()
                        ,movingX, movingY,null);
            } else if (i < 7){
                canvas.drawBitmap(myHand.get(i).getBitmap(), i * (scaledCardW + 5),
                        screenH - scaledCardH - whitePaint.getTextSize() - (50 *
                                scale),null);
            }
        }
        invalidate();
    }

    public boolean onTouchEvent (MotionEvent event){
        int eventaction = event.getAction();
        int X = (int) event.getX();
        int Y = (int) event.getY();

        switch (eventaction){

            case MotionEvent.ACTION_DOWN:
                if(myTurn){
                    for(int i = 0; i < 7; i++ ){
                        if(X > i * (scaledCardW + 5) && X < i * (scaledCardW + 5) + scaledCardW && Y > (screenH -
                        scaledCardH) - whitePaint.getTextSize() - 50 * scale){
                            movingCardIdx = i;
                            movingX = X - (int)(30 * scale);
                            movingY = Y - (int)(70 * scale);

                        }
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:
                movingX = X - (int)(30 * scale);
                movingY = Y - (int)(70 * scale);
                break;

            case MotionEvent.ACTION_UP:

                if(movingCardIdx > -1 &&
                        X > (screenW / 2) - (100 * scale) &&
                        X < (screenW / 2) + (100 * scale) &&
                        Y > (screenH /2) - (100 * scale) &&
                        Y < (screenH / 2) + (100 *scale) &&
                        (myHand.get(movingCardIdx).getRank() == 13 ||
                                myHand.get(movingCardIdx).getRank() == 14 ||
                                myHand.get(movingCardIdx).getRank() == 10 ||
                        myHand.get(movingCardIdx).getRank() == validRank ||
                        myHand.get(movingCardIdx).getSuit() == validSuit)){
                    validRank = myHand.get(movingCardIdx).getRank();
                    validSuit = myHand.get(movingCardIdx).getSuit();
                    discardPile.add(0,myHand.get(movingCardIdx));
                    myHand.remove(movingCardIdx);
                    if(myHand.isEmpty()){
                        endHand();
                    } else if (validRank == 13){
                        showChooseSuitDialog();
                    }else if( validRank == 14){
                        if(isreversed==false){
                            drawCard(oppHand2);
                            drawCard(oppHand2);
                            drawCard(oppHand2);
                            drawCard(oppHand2);
                        } else {
                            drawCard(oppHand3);
                            drawCard(oppHand3);
                            drawCard(oppHand3);
                            drawCard(oppHand3);
                        }

                        showChooseSuitDialog();
                    } else if (validRank ==10){
                        if(isreversed==false){
                            drawCard(oppHand2);
                            drawCard(oppHand2);
                        } else {
                            drawCard(oppHand3);
                            drawCard(oppHand3);
                        }
                        myTurn = false;
                        if(isreversed==false){
                            comTurn = 2;
                        } else {
                            comTurn = 3;
                        }
                        makeComputerPlay();
                    }else if (validRank==11){
                        if (isreversed== true){
                            isreversed= false;
                        }else {
                            isreversed = true;}
                        if(isreversed==false){
                            comTurn = 2;
                        } else {
                            comTurn = 3;
                        }

                    }else if (validRank==12){
                    comTurn = 1;
                        makeComputerPlay();
                    }
                    else {
                        myTurn = false;
                        if(isreversed==false){
                            comTurn = 2;
                        } else {
                            comTurn = 3;
                        }
                        makeComputerPlay();
                    }
                }
                if (movingCardIdx == -1 && myTurn &&
                        X > (screenW /2) - (100 * scale) &&
                        X < (screenW/2)+ (100 * scale) &&
                        Y > (screenH/2)-(100*scale)&&
                        Y < (screenH / 2) + (100 * scale)){
                    if(checkForValidDraw()){
                        drawCard(myHand);
                    }else {
                        Toast.makeText(myContext, "You have a valid play. ", Toast.LENGTH_SHORT).show();
                    }
                }
                if(myHand.size() > 7 && X > (screenW - nextCardButton.getWidth())
                        - 30 * scale && Y > (screenH - nextCardButton.getHeight()
                - scaledCardH) - 90 * scale && Y < (screenH -
                        nextCardButton.getHeight() - scaledCardH) - 60 * scale){
                    Collections.rotate(myHand,1);
                }
                movingCardIdx = -1;
                break;


        }
        invalidate();
        return true;
    }
    private void showChooseSuitDialog(){
        final Dialog chooseSuitDialog = new Dialog(myContext);
        chooseSuitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        chooseSuitDialog.setContentView(R.layout.choose_suit_dialog);
        final Spinner suitSpinner = (Spinner)chooseSuitDialog.findViewById
                (R.id.suitSpinner);
        ArrayAdapter <CharSequence> adapter = ArrayAdapter.createFromResource
                (myContext, R.array.suits, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        suitSpinner.setAdapter(adapter);
        Button okButton =(Button)chooseSuitDialog.findViewById(R.id.okButton);
        okButton.setOnClickListener (new OnClickListener(){
            public void onClick(View view){
                validSuit = (suitSpinner.getSelectedItemPosition() +1)*100;
                String suitText = "";
                if(validSuit == 100) {
                    suitText = "Blue";
                }else if(validSuit == 200){
                    suitText = "Green";

                }else if(validSuit == 300){
                    suitText = "Red";

                }else if(validSuit == 400){
                    suitText = "Yellow";
                }
                chooseSuitDialog.dismiss();
                Toast.makeText(myContext,"You chose " + suitText,
                        Toast.LENGTH_SHORT).show();
                myTurn = false;
                makeComputerPlay();
            }
        });
        chooseSuitDialog.show();
    }

    private void initCards(){
        for (int i = 0; i < 4; i++){
            for(int j = 102; j < 115; j++){
                int tempId = j + (i*100);
                Card tempCard = new Card(tempId);
                int resourceId = getResources().getIdentifier("card" + tempId,
                "drawable", myContext.getPackageName());
                Bitmap tempBitmap = BitmapFactory.decodeResource(
                        myContext.getResources(),resourceId);
                scaledCardW = (int) (screenW/8);
                scaledCardH = (int)  (scaledCardW*1.28);

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(tempBitmap,
                        scaledCardW,scaledCardH,false);
                tempCard.setBitmap(scaledBitmap);
                deck.add(tempCard);
            }
        }
    }
    private void drawCard(List<Card> handToDraw){
        handToDraw.add(0,deck.get(0));
        deck.remove(0);
        if (deck.isEmpty()){
            for (int i = discardPile.size()-1; i > 0;i--){
                deck.add(discardPile.get(i));
                discardPile.remove(i);
                Collections.shuffle(deck,new Random());
            }
        }
    }
    private void dealCard(){
        Collections.shuffle(deck,new Random());
        for (int i = 0; i < 7; i++ ){
            drawCard(myHand);
            drawCard(oppHand1);
            drawCard(oppHand2);
            drawCard(oppHand3);
        }
    }
    private boolean checkForValidDraw(){
        boolean canDraw = true;

        for(int i = 0; i < myHand.size(); ++i){
            int tempId = myHand.get(i).getId();
            int tempRank = myHand.get(i).getRank();
            int tempSuit = myHand.get(i).getSuit();
            if(validSuit == tempSuit || validRank == tempRank ||tempId
                    == 113 || tempId == 213 || tempId == 313 || tempId == 413 ||
                    tempId == 114 || tempId == 214 || tempId == 314 || tempId == 414){
                canDraw = false;
            }
        }
        return canDraw;
    }
    private void makeComputerPlay(){
        int tempPlay = 0;
        if (comTurn == 1) {

            while (tempPlay == 0) {
                tempPlay = computerPlayer.makePlay(oppHand1, validSuit,
                        validRank);
                if (tempPlay == 0) {
                    drawCard(oppHand1);
                }
            }
            if (tempPlay == 113 || tempPlay == 213 || tempPlay == 313 || tempPlay == 413) {
                validSuit = computerPlayer.chooseSuit(oppHand1);

                validRank = 13;


                String i = "";
                if (validSuit == 100) {
                    i = "Blue";
                } else if (validSuit == 200) {
                    i = "Green";
                } else if (validSuit == 300) {
                    i = "Red";
                } else if (validSuit == 400) {
                    i = "Yellow";
                }
                Toast.makeText(myContext, "Computer chose" + i,
                        Toast.LENGTH_SHORT).show();
            } else if (tempPlay == 114 || tempPlay == 214 || tempPlay == 314 || tempPlay == 414) {
                if (isreversed==false){
                    drawCard(oppHand3);
                    drawCard(oppHand3);
                    drawCard(oppHand3);
                    drawCard(oppHand3);
                } else {
                    drawCard(oppHand2);
                    drawCard(oppHand2);
                    drawCard(oppHand2);
                    drawCard(oppHand2);
                }


                validRank = 14;

                String i = "";
                if (validSuit == 100) {
                    i = "Blue";
                } else if (validSuit == 200) {
                    i = "Green";
                } else if (validSuit == 300) {
                    i = "Red";
                } else if (validSuit == 400) {
                    i = "Yellow";
                }
                Toast.makeText(myContext, "Computer chose" + i,
                        Toast.LENGTH_SHORT).show();
            } else if (tempPlay == 110 || tempPlay == 210 || tempPlay == 310 || tempPlay == 410) {
                validRank = 10;
                if (isreversed==false){
                    drawCard(oppHand3);
                    drawCard(oppHand3);
                } else {
                    drawCard(oppHand2);
                    drawCard(oppHand2);
                }
            }else if (tempPlay==111||tempPlay==211||tempPlay==311||tempPlay==411){
                validRank = 11;
                if (isreversed== true){
                    isreversed= false;
                }else {
                isreversed = true;}

            }else if (tempPlay==112||tempPlay==212||tempPlay==312||tempPlay==412){
                validRank = 12;
                myTurn = true;

            } else {
                validSuit = Math.round((tempPlay / 100) * 100);
                validRank = tempPlay - validSuit;
            }
            for (int i = 0; i < oppHand1.size(); i++) {
                Card tempCard = oppHand1.get(i);
                if (tempPlay == tempCard.getId()) {
                    discardPile.add(0, oppHand1.get(i));
                    oppHand1.remove(i);
                }
            }
            if (oppHand1.isEmpty()) {
                endHand();
            }
            if(isreversed==false){
                comTurn = 3;
                makeComputerPlay();
            }else {
                comTurn = 2;
                makeComputerPlay();
            }

        }else if (comTurn == 2){

            while (tempPlay == 0) {
                tempPlay = computerPlayer.makePlay(oppHand2, validSuit,
                        validRank);
                if (tempPlay == 0) {
                    drawCard(oppHand2);
                }
            }
            if (tempPlay == 113 || tempPlay == 213 || tempPlay == 313 || tempPlay == 413) {
                validSuit = computerPlayer.chooseSuit(oppHand2);

                validRank = 13;


                String i = "";
                if (validSuit == 100) {
                    i = "Blue";
                } else if (validSuit == 200) {
                    i = "Green";
                } else if (validSuit == 300) {
                    i = "Red";
                } else if (validSuit == 400) {
                    i = "Yellow";
                }
                Toast.makeText(myContext, "Computer chose" + i,
                        Toast.LENGTH_SHORT).show();
            } else if (tempPlay == 114 || tempPlay == 214 || tempPlay == 314 || tempPlay == 414) {
                if (isreversed==false){
                    drawCard(oppHand1);
                    drawCard(oppHand1);
                    drawCard(oppHand1);
                    drawCard(oppHand1);
                } else {
                    drawCard(myHand);
                    drawCard(myHand);
                    drawCard(myHand);
                    drawCard(myHand);
                }


                validRank = 14;

                String i = "";
                if (validSuit == 100) {
                    i = "Blue";
                } else if (validSuit == 200) {
                    i = "Green";
                } else if (validSuit == 300) {
                    i = "Red";
                } else if (validSuit == 400) {
                    i = "Yellow";
                }
                Toast.makeText(myContext, "Computer chose" + i,
                        Toast.LENGTH_SHORT).show();
            } else if (tempPlay == 110 || tempPlay == 210 || tempPlay == 310 || tempPlay == 410) {
                if (isreversed==false){
                    drawCard(oppHand1);
                    drawCard(oppHand1);
                } else {
                    drawCard(myHand);
                    drawCard(myHand);
                }
            }else if (tempPlay==111||tempPlay==211||tempPlay==311||tempPlay==411){
                validRank = 11;
                if (isreversed== true){
                    isreversed= false;
                }else {
                    isreversed = true;}

            }else if (tempPlay==112||tempPlay==212||tempPlay==312||tempPlay==412){
                validRank = 12;
                comTurn = 3;
                makeComputerPlay();

            } else {
                validSuit = Math.round((tempPlay / 100) * 100);
                validRank = tempPlay - validSuit;
            }
            for (int i = 0; i < oppHand2.size(); i++) {
                Card tempCard = oppHand2.get(i);
                if (tempPlay == tempCard.getId()) {
                    discardPile.add(0, oppHand2.get(i));
                    oppHand2.remove(i);
                }
            }
            if (oppHand2.isEmpty()) {
                endHand();
            }
            if(isreversed==false){
                comTurn = 1;
                makeComputerPlay();
            }else {
               myTurn = true;
            }
        }else if (comTurn == 3){

            while (tempPlay == 0) {
                tempPlay = computerPlayer.makePlay(oppHand3, validSuit,
                        validRank);
                if (tempPlay == 0) {
                    drawCard(oppHand3);
                }
            }
            if (tempPlay == 113 || tempPlay == 213 || tempPlay == 313 || tempPlay == 413) {
                validSuit = computerPlayer.chooseSuit(oppHand2);

                validRank = 13;


                String i = "";
                if (validSuit == 100) {
                    i = "Blue";
                } else if (validSuit == 200) {
                    i = "Green";
                } else if (validSuit == 300) {
                    i = "Red";
                } else if (validSuit == 400) {
                    i = "Yellow";
                }
                Toast.makeText(myContext, "Computer chose" + i,
                        Toast.LENGTH_SHORT).show();
            } else if (tempPlay == 114 || tempPlay == 214 || tempPlay == 314 || tempPlay == 414) {
                if (isreversed==false){
                    drawCard(myHand);
                    drawCard(myHand);
                    drawCard(myHand);
                    drawCard(myHand);
                } else {
                    drawCard(oppHand1);
                    drawCard(oppHand1);
                    drawCard(oppHand1);
                    drawCard(oppHand1);
                }


                validRank = 14;

                String i = "";
                if (validSuit == 100) {
                    i = "Blue";
                } else if (validSuit == 200) {
                    i = "Green";
                } else if (validSuit == 300) {
                    i = "Red";
                } else if (validSuit == 400) {
                    i = "Yellow";
                }
                Toast.makeText(myContext, "Computer chose" + i,
                        Toast.LENGTH_SHORT).show();
            } else if (tempPlay == 110 || tempPlay == 210 || tempPlay == 310 || tempPlay == 410) {
                validRank = 10;
                if (isreversed==false){
                    drawCard(myHand);
                    drawCard(myHand);
                } else {
                    drawCard(oppHand1);
                    drawCard(oppHand1);
                }
            }else if (tempPlay==111||tempPlay==211||tempPlay==311||tempPlay==411){
                validRank = 11;
                if (isreversed== true){
                    isreversed= false;
                }else {
                    isreversed = true;}

            }else if (tempPlay==112||tempPlay==212||tempPlay==312||tempPlay==412){
                validRank = 12;
                comTurn = 2;
                makeComputerPlay();

            } else {
                validSuit = Math.round((tempPlay / 100) * 100);
                validRank = tempPlay - validSuit;
            }
            for (int i = 0; i < oppHand3.size(); i++) {
                Card tempCard = oppHand3.get(i);
                if (tempPlay == tempCard.getId()) {
                    discardPile.add(0, oppHand3.get(i));
                    oppHand3.remove(i);
                }
            }
            if (oppHand3.isEmpty()) {
                endHand();
            }
            if(isreversed==false){
                myTurn = true;

            }else {
                comTurn = 1;
                makeComputerPlay();
            }

        }
    }
    private void endHand(){
        final Dialog endHandDialog = new Dialog(myContext);
        endHandDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        endHandDialog.setContentView(R.layout.end_hand_dialog);
        updateScores();
        TextView endHandText = (TextView)endHandDialog.findViewById(R.id.endHandText);
        if (myHand.isEmpty()){
            if (myScore >= 300){
                endHandText.setText("You reached " + myScore + " points. You won !" +
                                " Would you like to play again ?");
            }else {
                endHandText.setText("You went out and got " + scoreThisHand +
                        " points!");
            }
        } else if(oppHand1.isEmpty()){
            if (oppScore >= 300){
                endHandText.setText("the computer reached " + oppScore + " points. Sorry, you lost !" +
                        " Would you like to play again ?");
            }else {
                endHandText.setText("The computer went out and got " + scoreThisHand +
                        " points!");
            }
        }else if(oppHand2.isEmpty()){
            if (oppScore >= 300){
                endHandText.setText("the computer reached " + oppScore + " points. Sorry, you lost !" +
                        " Would you like to play again ?");
            }else {
                endHandText.setText("The computer went out and got " + scoreThisHand +
                        " points!");
            }
        }else if(oppHand3.isEmpty()){
            if (oppScore >= 300){
                endHandText.setText("the computer reached " + oppScore + " points. Sorry, you lost !" +
                        " Would you like to play again ?");
            }else {
                endHandText.setText("The computer went out and got " + scoreThisHand +
                        " points!");
            }
        }

        Button nextHandButton = (Button)endHandDialog.findViewById(R.id.nextHandButton);
        if(oppScore >= 300 || myScore >= 300){
            nextHandButton.setText("New Game");
        }
        nextHandButton.setOnClickListener(new OnClickListener(){
            public void onClick(View view){
           if(oppScore >= 300 || myScore >= 300){
               myScore = 0;
               oppScore = 0;
           }
           initNewHand();
                endHandDialog.dismiss();
            }
        });
        endHandDialog.show();
    }
    private void updateScores(){

        for (int i = 0; i < myHand.size(); ++i){
            oppScore += myHand.get(i).getScoreValue();
            scoreThisHand += myHand.get(i).getScoreValue();
        }
        for(int i = 0; i < oppHand1.size(); ++i){
            myScore += oppHand1.get(i).getScoreValue();
            scoreThisHand += oppHand1.get(i).getScoreValue();
        }
    }
    private void initNewHand(){
        scoreThisHand = 0;
        if (myHand.isEmpty()){
            myTurn = true;
        } else if(oppHand1.isEmpty()){
            comTurn = 1;
            myTurn = false;
        }else if(oppHand2.isEmpty()){
            comTurn = 2;
            myTurn = false;
        }else if(oppHand3.isEmpty()){
            comTurn = 3;
            myTurn = false;
        }
        deck.addAll(discardPile);
        deck.addAll(myHand);
        deck.addAll(oppHand1);
        deck.addAll(oppHand2);
        deck.addAll(oppHand3);
        discardPile.clear();
        myHand.clear();
        oppHand1.clear();
        oppHand2.clear();
        oppHand3.clear();
        dealCard();
        drawCard(discardPile);
        validSuit =  discardPile.get(0).getSuit();
        validRank =  discardPile.get(0).getRank();
        if(!myTurn){
            makeComputerPlay();
        }
    }

}
