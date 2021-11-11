package entity;

import java.awt.Graphics2D;

import game.Board;
import game.Game;
import tile.MapTile;

public class Mob extends Entity {

    protected int moveDistance = 2;
    protected double speed = 1;

    //Speed Control
    protected int sFps = (int)(Game.FPS*speed);
    protected double sTimePerTick = 1000000000 / sFps;
    protected double sDelta = 0;
    protected long sNow;
    protected long sLastTime = System.nanoTime();

    public Mob(Game game, Board board, float x, float y, int width, int height) {
        super(game, board, x, y, width, height);
    }

    @Override
    public void tick() {
        
    }

    @Override
    public void render(Graphics2D g) {
        
    }

    public boolean[] move(Game game, Board board, char direction, char currentDirection, int moveDistance) {
        //Return Values
        boolean moved = false;
        boolean changed = false;

        //Tunnel Edge Case
        if (x == 0 && y == 348 && direction == 'a') {
            x = 670;
            moved = true;
        }
        else if (x == 670 && y == 348 && direction == 'd') {
            x = 0;
            moved = true;
        }

        //Valid Directions at a given Tile
        MapTile currentTile = board.getTile((int)x, (int)y);
        boolean[] validDirections = currentTile.getValidDirections();

        //Move if legal
        boolean newDirec = false;
        if (direction != currentDirection) {
            if (direction == 'w' && validDirections[0]) {
                if (board.isOnTrack((int)x)) {
                    y -= moveDistance;
                    newDirec = true;
                    moved = true;
                    changed = true;
                }
            }
            else if (direction == 's' && validDirections[2]) {
                if (board.isOnTrack((int)x)) {
                    y += moveDistance;
                    newDirec = true;
                    moved = true;
                    changed = true;
                }
            }
            else if (direction == 'a' && validDirections[1]) {
                if (board.isOnTrack((int)y)) {
                    x -= moveDistance;
                    newDirec = true;
                    moved = true;
                    changed = true;
                }
            }
            else if (direction == 'd' && validDirections[3]) {
                if (board.isOnTrack((int)y)) {
                    x += moveDistance;
                    newDirec = true;
                    moved = true;
                    changed = true;
                }
            }
        }
        if (!newDirec) {
            switch (currentDirection) {
                case 'w':
                    if (validDirections[0] || !board.isOnTrack((int)y)) {
                        y -= moveDistance;
                        moved = true;
                    }
                    break;
                case 'a':
                    if (validDirections[1] || !board.isOnTrack((int)x)) {
                        x -= moveDistance;
                        moved = true;
                    }
                    break;
                case 's':
                    if (validDirections[2] || !board.isOnTrack((int)y)) {
                        y += moveDistance;
                        moved = true;
                    }
                    break;
                case 'd':
                    if (validDirections[3] || !board.isOnTrack((int)x)) {
                        x += moveDistance;
                        moved = true;
                    }
                    break;
            }
        }
        boolean[] movedAndChanged = {moved, changed};
        return movedAndChanged;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
        updateSpeedControlVariables();
    }

    public void updateSpeedControlVariables() {
        sFps = (int)(Game.FPS*speed);
        if (sFps != 0) sTimePerTick = 1000000000 / sFps;
        else sTimePerTick = Integer.MAX_VALUE;
    }

    public void setInvisible() {
        invisible = true;
    }

    public void setVisible() {
        invisible = false;
    }

    public boolean getVisibility() {
        return invisible;
    }

    public void sLastTimeUpdate() {
        sLastTime = System.nanoTime();
    }
    
}
