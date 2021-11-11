package game;

import java.awt.Graphics2D;

import gfx.Assets;
import gfx.Display;
import state.GameState;

public class LevelManager {

    public static int level = 1;

    //Ready!
    public static boolean ready = true;

    //Maze Blink
    public static boolean mazeBlink = false;
    //Maze Blink Animation
    private boolean blink = true;
    private int aFps = 2;
    private double aTimePerTick = 1000000000 / aFps;
    private double aDelta = 0;
    private long aNow;
    private long aLastTime = System.nanoTime();

    //Game Over
    public static boolean gameOver = false;

    //Entity Speeds
    public static double[] speeds;
    private static double[] speeds1 = new double[7];
    private static double[] speeds2_4 = new double[7];
    private static double[] speeds5_20 = new double[7];
    private static double[] speeds21 = new double[7];

    //Scatter Chase Timer Intervals
    public static int[] scatterChaseTimes;
    private static int[] scatterChaseTimes1 = new int[7];
    private static int[] scatterChaseTimes2_4 = new int[7];
    private static int[] scatterChaseTimes5 = new int[7];

    //Fright Duration
    public static double[] frightDuration;

    /**
     * speedsN = speeds for level N
     * speedsN_M = speeds for levels N-M
     * 
     * Entries 0-6:
     * 0: Pacman normal speed
     * 1: Pacman normal dot eating speed
     * 2: Pacman ghost freightened speed
     * 3: Pacman ghost freightened dot eating speed
     * 4: Ghost normal speed
     * 5: Ghost freightened speed
     * 6: Ghost tunnel speed
     * 
     * scatterChaseTimes is formatted similarly in milliseconds
     * 
     */

    public LevelManager() {

        //Initialize speeds array
        speeds1[0] = 0.80;
        speeds1[1] = 0.71;
        speeds1[2] = 0.90;
        speeds1[3] = 0.79;
        speeds1[4] = 0.75;
        speeds1[5] = 0.50;
        speeds1[6] = 0.40;

        speeds2_4[0] = 0.90;
        speeds2_4[1] = 0.79;
        speeds2_4[2] = 0.95;
        speeds2_4[3] = 0.83;
        speeds2_4[4] = 0.85;
        speeds2_4[5] = 0.55;
        speeds2_4[6] = 0.45;

        speeds5_20[0] = 1.00;
        speeds5_20[1] = 0.87;
        speeds5_20[2] = 1.00;
        speeds5_20[3] = 0.87;
        speeds5_20[4] = 0.95;
        speeds5_20[5] = 0.60;
        speeds5_20[6] = 0.50;

        speeds21[0] = 0.90;
        speeds21[1] = 0.79;
        speeds21[2] = 1.00; //NA
        speeds21[3] = 0.87; //NA
        speeds21[4] = 0.95;
        speeds21[5] = 0.60; //NA
        speeds21[6] = 0.50; 

        setLevelSpeeds(level);

        //Initialize scatter chase times array
        scatterChaseTimes1[0] = 7000;
        scatterChaseTimes1[1] = 20000;
        scatterChaseTimes1[2] = 7000;
        scatterChaseTimes1[3] = 20000;
        scatterChaseTimes1[4] = 5000;
        scatterChaseTimes1[5] = 20000;
        scatterChaseTimes1[6] = 5000;

        scatterChaseTimes2_4[0] = 7000;
        scatterChaseTimes2_4[1] = 20000;
        scatterChaseTimes2_4[2] = 7000;
        scatterChaseTimes2_4[3] = 20000;
        scatterChaseTimes2_4[4] = 5000;
        scatterChaseTimes2_4[5] = 1033000;
        scatterChaseTimes2_4[6] = 17;

        scatterChaseTimes5[0] = 5000;
        scatterChaseTimes5[1] = 20000;
        scatterChaseTimes5[2] = 5000;
        scatterChaseTimes5[3] = 20000;
        scatterChaseTimes5[4] = 5000;
        scatterChaseTimes5[5] = 1033000;
        scatterChaseTimes5[6] = 17;

        setScatterChaseTimes(level);

    }

    public static void setLevelSpeeds(int level) {
        if (level == 1) speeds = speeds1;
        else if (level < 5) speeds = speeds2_4;
        else if (level < 21) speeds = speeds5_20;
        else speeds = speeds21;
    }

    public static void setScatterChaseTimes(int level) {
        if (level == 1) scatterChaseTimes = scatterChaseTimes1;
        else if (level < 5) scatterChaseTimes = scatterChaseTimes2_4;
        else scatterChaseTimes = scatterChaseTimes5;
    }

    public void tick() {
        
    }

    public void render(Graphics2D g) {
        //Render Fruit Level Indicator
        int screenXOffset = (Display.width - Assets.maze.getWidth())/2;
        int screenYOffset = (Display.height-864)/2;
        if (level <= 6) {
            for (int i = 0; i < level; i++) {
                g.drawImage(Assets.fruits[i], 576 - 48*(i) + screenXOffset, 744 + screenYOffset + GameState.MAP_Y_OFFSET, null);
            }
        }
        else {
            for (int i = 0; i < 7; i++) {
                g.drawImage(Assets.fruits[Math.min(i+(level-7),12)], 576 - 48*(i) + screenXOffset, 744 + screenYOffset + GameState.MAP_Y_OFFSET, null);
            }
        }
        //Ready!
        if (ready) {
            g.drawImage(Assets.ready, 336 - (Assets.ready.getWidth()/2) + screenXOffset, 420 - (Assets.ready.getHeight()/2) + screenYOffset + GameState.MAP_Y_OFFSET, null);
        }
        //Maze Blink
        if (mazeBlink) {
            aNow = System.nanoTime();
            aDelta += (aNow - aLastTime) / aTimePerTick;
            aLastTime = aNow;
            if (aDelta >= 1) {
                blink = !blink;
                aDelta = 0;
            }
            if (blink) g.drawImage(Assets.wMaze, (Display.width - Assets.maze.getWidth())/2, (Display.height-864)/2 + GameState.MAP_Y_OFFSET, null);
        }
        //Game Over
        if (gameOver) {
            g.drawImage(Assets.gameOver, 336 - (Assets.gameOver.getWidth()/2) + screenXOffset, 420 - (Assets.gameOver.getHeight()/2) + screenYOffset + GameState.MAP_Y_OFFSET, null);
        }
    }

    public void aLastTimeUpdate() {
        aLastTime = System.nanoTime();
    }
    
}
