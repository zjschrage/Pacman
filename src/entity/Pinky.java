package entity;

import java.awt.Graphics2D;

import game.Board;
import game.Game;
import gfx.Assets;
import gfx.Display;

public class Pinky extends Ghost {

    private int[] scatterCoords = {60, -84};

    public Pinky(Game game, Board board, Pacman pacman, float x, float y) {
        super(game, board, pacman, x, y, Ghost.DEFAULT_GHOST_WIDTH, Ghost.DEFAULT_GHOST_HEIGHT);
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
                    g.drawImage(Assets.pinkyU[animator[animatorCounter]], (int)x-(width/2) + screenXOffset, (int)y-(height/2) + screenYOffset + MAP_Y_OFFSET, width, height, null);
                    break;
                case 'a':
                    g.drawImage(Assets.pinkyL[animator[animatorCounter]], (int)x-(width/2) + screenXOffset, (int)y-(height/2) + screenYOffset + MAP_Y_OFFSET, width, height, null);
                    break;
                case 's':
                    g.drawImage(Assets.pinkyD[animator[animatorCounter]], (int)x-(width/2) + screenXOffset, (int)y-(height/2) + screenYOffset + MAP_Y_OFFSET, width, height, null);
                    break;
                case 'd':
                    g.drawImage(Assets.pinkyR[animator[animatorCounter]], (int)x-(width/2) + screenXOffset, (int)y-(height/2) + screenYOffset + MAP_Y_OFFSET, width, height, null);
                    break;
            }
        }
    }

    public int[] calculateTarget() {
        int xTarget = 0;
        int yTarget = 0;
        switch (game.getKeyManager().currentDirection) {
            case 'w':
                xTarget = (int)pacman.getX() - board.getTileSize()*4;
                yTarget = (int)pacman.getY() - board.getTileSize()*4;
                break;
            case 'a':
                xTarget = (int)pacman.getX() - board.getTileSize()*4;
                yTarget = (int)pacman.getY();
                break;
            case 's':
                xTarget = (int)pacman.getX();
                yTarget = (int)pacman.getY() + board.getTileSize()*4;
                break;
            case 'd':
                xTarget = (int)pacman.getX() + board.getTileSize()*4;
                yTarget = (int)pacman.getY();
                break;
        }
        int[] targetCoords = {xTarget, yTarget};
        return targetCoords;
    }
    
}
