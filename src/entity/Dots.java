package entity;

import java.awt.Graphics2D;

import game.Board;
import game.Game;
import gfx.Assets;
import gfx.Display;
import tile.MapTile;

public class Dots extends Entity {

    public static int dotsRemaining = 244; 

    //Animation
    protected int animatorLength = 2;
    protected int animatorCounter = 0;

    public Dots(Game game, Board board) {
        super(game, board, 0, 0, 0, 0);
        aFps = 2;
        aTimePerTick = 1000000000 / aFps;
    }

    public void tick() {
        //Blinking for the Power pellet
        aNow = System.nanoTime();
        aDelta += (aNow - aLastTime) / aTimePerTick;
        aLastTime = aNow;
        if (aDelta >= 1) {
            animatorCounter++;
            if (animatorCounter == animatorLength) animatorCounter = 0;
            aDelta = 0;
        }
    }

    public void render(Graphics2D g) {
        MapTile[][] tempBoard = board.getBoard();
        for (int i = 0; i < tempBoard[0].length; i++) {
            for (int j = 0; j < tempBoard.length; j++) {
                if (tempBoard[j][i].getId() == 1) g.drawImage(Assets.dot, board.getTileSize()*i + (Display.width - Assets.maze.getWidth())/2, board.getTileSize()*j + (Display.height-864)/2 + MAP_Y_OFFSET, null);
                if (tempBoard[j][i].getId() == 2 && animatorCounter == 0) g.drawImage(Assets.powerPellet, board.getTileSize()*i  + (Display.width - Assets.maze.getWidth())/2, board.getTileSize()*j + (Display.height-864)/2  + MAP_Y_OFFSET, null);
            }
        }
    }

    public static int getDotsRemaining() {
        return dotsRemaining;
    }

    public static void setDotsRemaining(int dotsRemainingIn) {
        dotsRemaining = dotsRemainingIn;
    }

    public static void eatDot() {
        dotsRemaining--;
    }
    
}
