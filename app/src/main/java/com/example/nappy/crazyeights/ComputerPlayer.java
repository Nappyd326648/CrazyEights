package com.example.nappy.crazyeights;

/**
 * Created by nappy on 7/7/2017.
 */

import java.util.List;

public class ComputerPlayer {

    public int makePlay(List<Card>hand,int suit, int rank){
        int play = 0;

        int i;
        int tempId;
        for(i = 0; i < hand.size(); ++i){
            tempId = ((Card)hand.get(i)).getId();
            int tempRank = ((Card)hand.get(i)).getRank();
            int tempSuit = ((Card)hand.get(i)).getSuit();

            if(tempRank != 13 || tempRank != 14 ){
                if (rank == 13 || rank == 14){
                    if(suit == tempSuit){
                        play = tempId;
                    }
                }else if(suit == tempSuit|| rank == tempRank){
                    play = tempId;
                }
            }
        }
        if (play == 0){
            for (i = 0; i < hand.size(); ++i){
                tempId = ((Card)hand.get(i)).getId();
                if(tempId == 113 || tempId == 213 || tempId == 313 || tempId == 413 ||
                        tempId == 114 || tempId == 214 || tempId == 314 || tempId == 414 ){
                    play = tempId;
                }
            }
        }
        return play;
    }
    public int chooseSuit(List<Card> hand){
        short suit = 100;
        int numBlue = 0;
        int numGreen = 0;
        int numRed = 0;
        int numYellow = 0;

        for (int i = 0; i < hand.size(); ++i){
            int tempRank = ((Card)hand.get(i)).getRank();
            int tempSuit = ((Card)hand.get(i)).getSuit();
            if(tempRank != 8){
                if (tempSuit == 100){
                    ++numBlue;
                }else if (tempSuit == 200){
                    ++numGreen;
                }else if (tempSuit == 300){
                    ++numRed;
                }else if (tempSuit == 400){
                    ++numYellow;
                }
            }
        }
        if (numGreen > numBlue && numGreen > numRed && numGreen > numYellow ){
            suit = 200;
        } else if (numRed > numBlue && numRed > numGreen && numRed > numYellow ){
            suit = 300;
        } else if(numYellow > numBlue && numYellow > numGreen && numYellow > numRed ){
            suit = 400;
        }
        return suit;
    }
}
