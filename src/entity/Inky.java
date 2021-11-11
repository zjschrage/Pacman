package entity;

import java.awt.Graphics2D;

import game.Board;
import game.Game;
import gfx.Assets;
import gfx.Display;

public class Inky extends Ghost {

    private Ghost blinky;
    private int[] scatterCoords = {672, 744};


    public Inky(Game game, Board board, Pacman pacman, Ghost blinky, float x, float y) {
        super(game, board, pacman, x, y, Ghost.DEFAULT_GHOST_WIDTH, Ghost.DEFAULT_GHOST_HEIGHT);
        this.blinky = blinky;

    }

    @Override
    public void tick() {
         //Speed Control
         sNow = System.nanoTime();
         sDelta += (sNow - sLastTime) / sTimePerTick;
         sLastTime = sNow;
         if (sDelta < 1) {
             return;
         }
         sDelta--;
         if (speed == 0) return;
        //Pathfind at every intersection
        if (board.isOnTrack((int)x) && board.isOnTrack((int)y) && board.getTile((int)x, (int)y).isIntersection()) {
            if (eaten) wantsDirection = pathfind(board, currentDirection, ghostHouseCoords[0], ghostHouseCoords[1], false);
            else if (freightened) wantsDirection = pathfind(board, currentDirection, 0, 0, true);
            else {
                switch (state) {
                    case 0:
                        wantsDirection = pathfind(board, currentDirection, scatterCoords[0], scatterCoords[1], false);
                        break;
                    case 1:
                        int[] targetCoords = calculateTarget();
                        wantsDirection = pathfind(board, currentDirection, targetCoords[0], targetCoords[1], false);
                        break;
                }
            }
        }
        //Continuously move every tick
        boolean[] movedAndChanged = move(game, board, wantsDirection, currentDirection, moveDistance);
        if (movedAndChanged[0]) {
            //Update Direction
            if(movedAndChanged[1]) updateDirection(wantsDirection);
            //Animation
            aNow = System.nanoTime();
            aDelta += (aNow - aLastTime) / aTimePerTick;
            aLastTime = aNow;
            if (aDelta >= 1) {
                animatorCounter++;
                animatorCounter2++;
                if (animatorCounter == animatorLength) animatorCounter = 0;
                if (animatorCounter2 == animatorLength2) animatorCounter2 = 0;
                aDelta = 0;
            }
        }

    }

    @Override
    public void render(Graphics2D g) {
        if (getVisibility()) return;
        int screenXOffset = (Display.width - Assets.maze.getWidth())/2;
        int screenYOffset = (Display.height-864)/2;
        if (freightened) {
            if (revertWarning) g.drawImage(Assets.freightenedBW[animator2[animatorCounter2]], (int)x-(width/2) + screenXOffset, (int)y-(height/2) + screenYOffset + MAP_Y_OFFSET, width, height, null);
            else g.drawImage(Assets.freightenedB[animator[animatorCounter]], (int)x-(width/2) + screenXOffset, (int)y-(height/2) + screenYOffset + MAP_Y_OFFSET, width, height, null);
        }
        else if (eaten) {
            switch (currentDirection) {
                case 'w':
                    g.drawImage(Assets.eatenU, (int)x-(width/2) + screenXOffset, (int)y-(height/2) + screenYOffset + MAP_Y_OFFSET, width, height, null);
                    break;
                case 'a':
                    g.drawImage(Assets.eatenL, (int)x-(width/2) + screenXOffset, (int)y-(height/2) + screenYOffset + MAP_Y_OFFSET, width, height, null);
                    break;
                case 's':
                    g.drawImage(Assets.eatenD, (int)x-(width/2) + screenXOffset, (int)y-(height/2) + screenYOffset + MAP_Y_OFFSET, width, height, null);
                    break;
                case 'd':
                    g.drawImage(Assets.eatenR, (int)x-(width/2) + screenXOffset, (int)y-(height/2) + screenYOffset + MAP_Y_OFFSET, width, height, null);
                    break;
            }
        }
        else {
            switch (currentDirection) {
                case 'w':
                    g.drawImage(Assets.inkyU[animator[animatorCounter]], (int)x-(width/2) + screenXOffset, (int)y-(height/2) + screenYOffset + MAP_Y_OFFSET, width, height, null);
                    break;
                case 'a':
                    g.drawImage(Assets.inkyL[animator[animatorCounter]], (int)x-(width/2) + screenXOffset, (int)y-(height/2) + screenYOffset + MAP_Y_OFFSET, width, height, null);
                    break;
                case 's':
                    g.drawImage(Assets.inkyD[animator[animatorCounter]], (int)x-(width/2) + screenXOffset, (int)y-(height/2) + screenYOffset + MAP_Y_OFFSET, width, height, null);
                    break;
                case 'd':
                    g.drawImage(Assets.inkyR[animator[animatorCounter]], (int)x-(width/2) + screenXOffset, (int)y-(height/2) + screenYOffset + MAP_Y_OFFSET, width, height, null);
                    break;
            }
        }
    }

    public int[] calculateTarget() {
        float a = blinky.getX();
            float b = blinky.getY();
            float px = pacman.getX();
            float py = pacman.getY();
            switch (game.getKeyManager().currentDirection) {
                case 'w':
                    px -= board.getTileSize()*2;
                    py -= board.getTileSize()*2;
                    break;
                case 'a':
                    px -= - board.getTileSize()*2;
                    break;
                case 's':
                    py += board.getTileSize()*2;
                    break;
                case 'd':
                    px += board.getTileSize()*2;
                    break;
            }
            float p = Math.abs(a-px);
            float q = Math.abs(b-py);
            int xTarget = 0;
            int yTarget = 0;
            if (a < px && b <= py) {
                xTarget = (int)(px + p);
                yTarget = (int)(py + q);
            }
            else if (a >= px && b < py) {
                xTarget = (int)(px - p);
                yTarget = (int)(py + q);
            }
            else if (a > px && b >= py) {
                xTarget = (int)(px - p);
                yTarget = (int)(py - q);
            }
            else if (a <= px && b > py) {
                xTarget = (int)(px + p);
                yTarget = (int)(py - q);
            }
            int[] targetCoords = {xTarget, yTarget};
            return targetCoords;
    }
    
}
