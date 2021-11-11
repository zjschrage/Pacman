package entity;

import java.awt.Graphics2D;

import game.Board;
import game.Game;

public abstract class Entity {
    
    protected Game game;
    protected Board board;
    protected float x;
    protected float y;
    protected int width;
    protected int height;

    //Animation
    protected int aFps = 15;
    protected double aTimePerTick = 1000000000 / aFps;
    protected double aDelta = 0;
    protected long aNow;
    protected long aLastTime = System.nanoTime();

    //Animation variables
    protected boolean invisible = false;

    public static final int MAP_Y_OFFSET = 72;

    public static final int DEFAULT_ENTITY_WIDTH = 41;
    public static final int DEFAULT_ENTITY_HEIGHT = 41;

    public Entity(Game game, Board board, float x, float y, int width, int height) {
        this.game = game;
        this.board = board;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void tick();
    public abstract void render(Graphics2D g);

    public float getX() {
        return x;
    }
    
    public float getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public void setHeight(int height) {
        this.height = height;
    }

}
