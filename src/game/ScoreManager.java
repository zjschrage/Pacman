package game;

import java.awt.Graphics2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import gfx.Assets;
import gfx.Display;
import state.GameState;

public class ScoreManager {

    public static int lives = 3;

    public static int score = 0;
    public static int highScore = 0;
    private static int[] scoreDigits = new int[6];
    private static int[] highScoreDigits = new int[6];

    public static int[] ghostEatingScores = {200, 400, 800, 1600};
    private static boolean ghostEatingPause = false;
    private static int ghostEatingPauseScore = 0;
    private static int ghostEatingPauseX = 0;
    private static int ghostEatingPauseY = 0;

    public static int[] fruitEatingScores = {100, 300, 500, 700, 1000, 2000, 3000, 5000};
    private static boolean fruitAvailible = false;
    private static boolean fruitEaten = false;

    public ScoreManager(File highscore) {
        try {
            Scanner in = new Scanner(highscore);
            int high = in.nextInt();
            highScore = high;
            int idx = 0;
            while(high > 0) {
                highScoreDigits[idx] = high % 10;
                high /= 10;
                idx++;
            }
            in.close();
        
        } catch (FileNotFoundException e) {

        }
        
    }

    public void tick() {
        
    }

    public void render(Graphics2D g) {
        //Render Score
        int screenXOffset = (Display.width - Assets.maze.getWidth())/2;
        int screenYOffset = (Display.height-864)/2;
        for (int i = scoreDigits.length - 1; i >= 0; i--) {
            switch (scoreDigits[i]) {
                case 0:
                    g.drawImage(Assets.zero, 24*(6-i) + screenXOffset, 24 + screenYOffset, null);
                    break;
                case 1:
                    g.drawImage(Assets.one, 24*(6-i) + screenXOffset, 24 + screenYOffset, null);
                    break;
                case 2:
                    g.drawImage(Assets.two, 24*(6-i) + screenXOffset, 24 + screenYOffset, null);
                    break;
                case 3:
                    g.drawImage(Assets.three, 24*(6-i) + screenXOffset, 24 + screenYOffset, null);
                    break;
                case 4:
                    g.drawImage(Assets.four, 24*(6-i) + screenXOffset, 24 + screenYOffset, null);
                    break;
                case 5:
                    g.drawImage(Assets.five, 24*(6-i) + screenXOffset, 24 + screenYOffset, null);
                    break;
                case 6:
                    g.drawImage(Assets.six, 24*(6-i) + screenXOffset, 24 + screenYOffset, null);
                    break;
                case 7:
                    g.drawImage(Assets.seven, 24*(6-i) + screenXOffset, 24 + screenYOffset, null);
                    break;
                case 8:
                    g.drawImage(Assets.eight, 24*(6-i) + screenXOffset, 24 + screenYOffset, null);
                    break;
                case 9:
                    g.drawImage(Assets.nine, 24*(6-i) + screenXOffset, 24 + screenYOffset, null);
                    break;
            }
        }
        //Render High Score
        g.drawImage(Assets.h, 216 + screenXOffset, screenYOffset, 24, 24, null);
        g.drawImage(Assets.i, 216+(24)*(1) + screenXOffset, screenYOffset, 24, 24, null);
        g.drawImage(Assets.g, 216+(24)*(2) + screenXOffset, screenYOffset, 24, 24, null);
        g.drawImage(Assets.h, 216+(24)*(3) + screenXOffset, screenYOffset, 24, 24, null);
        g.drawImage(Assets.s, 216+(24)*(5) + screenXOffset, screenYOffset, 24, 24, null);
        g.drawImage(Assets.c, 216+(24)*(6) + screenXOffset, screenYOffset, 24, 24, null);
        g.drawImage(Assets.o, 216+(24)*(7) + screenXOffset, screenYOffset, 24, 24, null);
        g.drawImage(Assets.r, 216+(24)*(8) + screenXOffset, screenYOffset, 24, 24, null);
        g.drawImage(Assets.e, 216+(24)*(9) + screenXOffset, screenYOffset, 24, 24, null);
        for (int i = highScoreDigits.length - 1; i >= 0; i--) {
            switch (highScoreDigits[i]) {
                case 0:
                    g.drawImage(Assets.zero, 264 + 24*(5-i) + screenXOffset, 24 + screenYOffset, null);
                    break;
                case 1:
                    g.drawImage(Assets.one, 264 + 24*(5-i) + screenXOffset, 24 + screenYOffset, null);
                    break;
                case 2:
                    g.drawImage(Assets.two, 264 + 24*(5-i) + screenXOffset, 24 + screenYOffset, null);
                    break;
                case 3:
                    g.drawImage(Assets.three, 264 + 24*(5-i) + screenXOffset, 24 + screenYOffset, null);
                    break;
                case 4:
                    g.drawImage(Assets.four, 264 + 24*(5-i) + screenXOffset, 24 + screenYOffset, null);
                    break;
                case 5:
                    g.drawImage(Assets.five, 264 + 24*(5-i) + screenXOffset, 24 + screenYOffset, null);
                    break;
                case 6:
                    g.drawImage(Assets.six, 264 + 24*(5-i) + screenXOffset, 24 + screenYOffset, null);
                    break;
                case 7:
                    g.drawImage(Assets.seven, 264 + 24*(5-i) + screenXOffset, 24 + screenYOffset, null);
                    break;
                case 8:
                    g.drawImage(Assets.eight, 264 + 24*(5-i) + screenXOffset, 24 + screenYOffset, null);
                    break;
                case 9:
                    g.drawImage(Assets.nine, 264 + 24*(5-i) + screenXOffset, 24 + screenYOffset, null);
                    break;
            }
        }
        //Render Lives Indicator
        for (int i = 1; i < lives; i++) {
            g.drawImage(Assets.lives, 48*i + screenXOffset, 744 + screenYOffset + GameState.MAP_Y_OFFSET, null);
        }
        //Render Ghost Eaten Points
        if (ghostEatingPause) {
            switch (ghostEatingPauseScore) {
                case 200:
                    g.drawImage(Assets.twoHundred, ghostEatingPauseX-(Assets.twoHundred.getWidth()/2) + screenXOffset, ghostEatingPauseY-(Assets.twoHundred.getHeight()/2) + screenYOffset, null);
                    break;
                case 400:
                    g.drawImage(Assets.fourHundred, ghostEatingPauseX-(Assets.twoHundred.getWidth()/2) + screenXOffset, ghostEatingPauseY-(Assets.twoHundred.getHeight()/2) + screenYOffset, null);
                    break;
                case 800:
                    g.drawImage(Assets.eightHundred, ghostEatingPauseX-(Assets.twoHundred.getWidth()/2) + screenXOffset, ghostEatingPauseY-(Assets.twoHundred.getHeight()/2) + screenYOffset, null);
                    break;
                case 1600:
                    g.drawImage(Assets.sixteenHundred, ghostEatingPauseX-(Assets.twoHundred.getWidth()/2) + screenXOffset, ghostEatingPauseY-(Assets.twoHundred.getHeight()/2) + screenYOffset, null);
                    break;
            }
        }
        //Render Fruit and Fruit score
        if (fruitAvailible) {
            g.drawImage(Assets.fruits[Math.min(LevelManager.level-1, 12)], 312 + screenXOffset, 396 + screenYOffset + GameState.MAP_Y_OFFSET, null);
        }
        else if (fruitEaten) {
            g.drawImage(Assets.fruitsScores[Math.min(LevelManager.level-1, 12)], 304 + screenXOffset, 408 + screenYOffset + GameState.MAP_Y_OFFSET, null);
        }
    }

    public static void scoreImgUpdate() {
        //Update the score array for graphical display
        int scoreCopy = score;
        int idx = 0;
        while(scoreCopy > 0) {
            scoreDigits[idx] = scoreCopy % 10;
            scoreCopy /= 10;
            idx++;
        }
        //Check if score is over high score
        if (score >= highScore) {
            highScoreDigits = scoreDigits;
        }
    }
    
    public static void clearScore() {
    	score = 0;
    	scoreDigits = new int[6];
    }

    public static void ghostEatingPause(int scoreIn, int x, int y) {
        ghostEatingPause = true;
        ghostEatingPauseScore = scoreIn;
        ghostEatingPauseX = x;
        ghostEatingPauseY = y;
    }

    public static void endGhostEatingPause() {
        ghostEatingPause = false;
    }

    public static void setFruitAvailible() {
        fruitAvailible = true;
        Timer t = new Timer();
        TimerTask fruitTimer = new TimerTask() {

            @Override
            public void run() {
                fruitAvailible = false;
            }
            
        };
        t.schedule(fruitTimer, 9000 + (int)(Math.random()*1000));
    }

    public static void setFruitUnavailible() {
        fruitAvailible = false;
    }

    public static boolean getFruitAvailible() {
        return fruitAvailible;
    }

    public static void setFruitEaten() {
        fruitEaten = true;
        Timer t = new Timer();
        TimerTask fruitScoreTimer = new TimerTask() {

            @Override
            public void run() {
                fruitEaten = false;
            }
            
        };
        t.schedule(fruitScoreTimer, 3000);
    }
    
}
