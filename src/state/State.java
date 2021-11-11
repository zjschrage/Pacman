package state;

import java.awt.Graphics2D;

import game.Game;

public abstract class State {

    private static State currentState = null;

    //Game State Manager
    public static void setState(State state) {
        currentState = state;
    }

    public static State getState() {
        return currentState;
    }

    //Game State Abstract Methods

    protected Game game;

    public State(Game game) {
        this.game = game;
    }
    
    public abstract void tick();
    public abstract void render(Graphics2D g);
    
}
