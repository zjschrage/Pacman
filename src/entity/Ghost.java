package entity;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;


import game.Board;
import game.Game;
import game.LevelManager;

public class Ghost extends Mob {

    protected static int state = 0;

    /**
     *  state
     *  0      - Scatter
     *  1      - Chase
     */
    
    //Reference variables
    protected Pacman pacman;

    //State variables
    protected boolean freightened = false;
    protected boolean eaten = false;
    protected boolean revertWarning = false;

    //Other variables
    protected int[] ghostHouseCoords = {300, 276};

    //Freightened Timer
    public static Timer t;

    //Movement
    protected char wantsDirection = 'a';
    protected char currentDirection = 'a';

    //Animation
    protected int[] animator = {0, 1};
    protected int animatorLength = 2;
    protected int animatorCounter = 0;
    protected int[] animator2 = {0, 1, 2, 3};
    protected int animatorLength2 = 4;
    protected int animatorCounter2 = 0;

    //Image Scaling
    public static int DEFAULT_GHOST_WIDTH = 48;
    public static int DEFAULT_GHOST_HEIGHT = 48;

    public Ghost(Game game, Board board, Pacman pacman, float x, float y, int width, int height) {
        super(game, board, x, y, width, height);
        this.pacman = pacman;
        setSpeed(LevelManager.speeds[4]);
    }

    @Override
    public void tick() {
        
    }

    @Override
    public void render(Graphics2D g) {
        
    }

    public static void setState(int stateIn) {
        state = stateIn;
    }

    public void setFreightened() {
        freightened = true;
        updateWantsDirection(oppositeDirection());
        setSpeed(LevelManager.speeds[5]);
    }

    public void setEaten() {
        freightened = false;
        eaten = true;
        setSpeed(1);
    }

    public void setUnFreightened() {
        freightened = false;
        setSpeed(LevelManager.speeds[4]);
    }

    public void setUnEaten() {
        eaten = false;
        setSpeed(LevelManager.speeds[4]);
    }

    public void setRevertWarning(boolean b) {
        this.revertWarning = b;
    }

    public boolean isFreightened() {
        return freightened;
    }

    public boolean isEaten() {
        return eaten;
    }

    public void freightenedTimer() {
        if (!pacman.ghostsFreightened) t = new Timer();
        else {
            t.cancel();
            t = new Timer();
            for (Ghost g : pacman.ghosts) {
                g.setRevertWarning(false);
            }
        }
        TimerTask revertWarning = new TimerTask() {

            @Override
            public void run() {
                for (Ghost g : pacman.ghosts) {
                    g.setRevertWarning(true);
                }
            }
            
        };
        TimerTask revert = new TimerTask() {

            @Override
            public void run() {
                pacman.ghostsFreightened = false;
                for (Ghost g : pacman.ghosts) {
                    g.setUnFreightened();
                    g.setRevertWarning(false);
                }
                pacman.setSpeed(LevelManager.speeds[0]);
            }
            
        };
        t.schedule(revertWarning, 6000);
        t.schedule(revert, 12000);
    }

    public char pathfind(Board board, char currentDirection, int xTarget, int yTarget, boolean frightened) {

        boolean[] vDirections = board.getTile((int)x, (int)y).getValidDirections();
        char[] directions = {'w', 'a', 's', 'd'};
        int ts = board.getTileSize();
        int tileX = (int)(x/ts);
        int tileY = (int)(y/ts);
        boolean tileBlocked = false;

        //Tiles above ghost house and start platform are blocked upward for ghosts
        if (tileX == 12 || tileX == 15) {
            if (tileY == 11 || tileY == 23) {
                tileBlocked = true;
            }
        }

        //Random movement when freightened
        if (frightened) {
            HashMap<Character, Double> map = new HashMap<>();
            for (int i = 0; i < vDirections.length; i++) {
                if (vDirections[i]) {
                    switch (directions[i]) {
                        case 'w':
                            if (!tileBlocked) map.put(directions[i], Math.random());
                            else map.put(directions[i], Double.MAX_VALUE);
                            break;
                        case 'a':
                            map.put(directions[i], Math.random());
                            break;
                        case 's':
                            map.put(directions[i], Math.random());
                            break;
                        case 'd':
                            map.put(directions[i], Math.random());
                            break;
                    }
                }
                else map.put(directions[i], Double.MAX_VALUE);
            }
            map.put(oppositeDirection(), Double.MAX_VALUE);

            double min = Double.MAX_VALUE;
            char d = '0';
            for (Entry<Character, Double> e : map.entrySet()) {
                if (e.getValue() < min) {
                    min = e.getValue();
                    d = e.getKey();
                }
            }

            //If the chosen direction is a tunnel, slow down
            updateTunnelSpeed(d);

            return d;
        }

        //Targeting system when scattering chasing or eaten
        if (eaten && x == ghostHouseCoords[0] && y == ghostHouseCoords[1]) {
            //Enter Ghost House animation and return to normal
            setUnEaten();
        }
        HashMap<Character, Double> map = new HashMap<>();
        for (int i = 0; i < vDirections.length; i++) {
            if (vDirections[i]) {
                switch (directions[i]) {
                    case 'w':
                        if (!tileBlocked) map.put(directions[i], l2Norm((int)x, (int)y-ts/2, xTarget, yTarget));
                        else map.put(directions[i], Double.MAX_VALUE);
                        break;
                    case 'a':
                        map.put(directions[i], l2Norm((int)x-ts/2, (int)y, xTarget, yTarget));
                        break;
                    case 's':
                        map.put(directions[i], l2Norm((int)x, (int)y+ts/2, xTarget, yTarget));
                        break;
                    case 'd':
                        map.put(directions[i], l2Norm((int)x+ts/2, (int)y, xTarget, yTarget));
                        break;
                }
            }
            else map.put(directions[i], Double.MAX_VALUE);
        }
        map.put(oppositeDirection(), Double.MAX_VALUE);

        double min = Double.MAX_VALUE;
        char d = '0';
        for (Entry<Character, Double> e : map.entrySet()) {
            if (e.getValue() < min) {
                min = e.getValue();
                d = e.getKey();
            }
        }

        //If the chosen direction is a tunnel, slow down
        updateTunnelSpeed(d);

        return d;
        
    }

    public char oppositeDirection() {
        switch(currentDirection) {
            case 'w':
                return 's';
            case 'a':
                return 'd';
            case 's':
                return 'w';
            case 'd':
                return 'a';
        }
        return '0';
    }

    public double l2Norm(int a, int b, int c, int d) {
        return Math.sqrt(Math.pow(d-b, 2) + Math.pow(c-a, 2));
    }

    public void updateTunnelSpeed(char d) {
        if (x == 516 && y == 348) {
            if (d == 'd') setSpeed(LevelManager.speeds[6]);
            else {
                if (freightened) setSpeed(LevelManager.speeds[5]);
                else if (eaten) setSpeed(1);
                else setSpeed(LevelManager.speeds[4]);
            }
        }
        else if (x == 156 && y == 348) {
            if (d == 'a') setSpeed(LevelManager.speeds[6]);
            else {
                if (freightened) setSpeed(LevelManager.speeds[5]);
                else if (eaten) setSpeed(1);
                else setSpeed(LevelManager.speeds[4]);
            }
        }
    }

    public void updateDirection(char d) {
        currentDirection = d;
    }

    public void updateWantsDirection(char d) {
        wantsDirection = d;
    }
    
}
