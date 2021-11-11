package state;

import java.awt.Graphics2D;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Color;

import game.Board;
import game.Game;
import game.GhostStateTimer;
import game.LevelManager;
import game.ScoreManager;
import game.SoundManager;
import gfx.Assets;
import gfx.Display;
import entity.Blinky;
import entity.Clyde;
import entity.Dots;
import entity.Ghost;
import entity.Inky;
import entity.Pacman;
import entity.Pinky;

public class GameState extends State {

    private LevelManager levelManager;
    private ScoreManager scoreManager;
    private SoundManager soundManager;
    private Board board;
    private Dots dots;
    private Pacman pacman;
    private Ghost blinky;
    private Ghost pinky;
    private Ghost inky;
    private Ghost clyde;
    private Ghost[] ghosts = new Ghost[4];
    private GhostStateTimer ghostStateTimer;

    public static boolean pause = true;

    public static final int MAP_Y_OFFSET = 72;

    public GameState(Game game) {
        super(game);
        levelManager = new LevelManager();
        scoreManager = new ScoreManager(new File("res/files/highscore.txt"));
        board = new Board();
        dots = new Dots(game, board);
        pacman = new Pacman(game, board, 336, 564);
        blinky = new Blinky(game, board, pacman, 336, 276);
        pinky = new Pinky(game, board, pacman, 336, 276);
        inky = new Inky(game, board, pacman, blinky, 336, 276);
        clyde = new Clyde(game, board, pacman, 336, 276);
        ghosts[0] = blinky;
        ghosts[1] = pinky;
        ghosts[2] = inky;
        ghosts[3] = clyde;
        pacman.passGhostReference(ghosts);
    }

    @Override
    public void tick() {
    	//Check if game is over
    	if (LevelManager.gameOver) {
    		if (game.getMouseManager().isLeftPressed()) {
    			ghostStateTimer.stopTimers();
    			State.setState(game.menuState);
    		}
    	}
    	//Or Check if new game
    	else if (!pause) {
            //Check if dots are cleared to end level
            if (Dots.getDotsRemaining() == 0) endLevel();
            //Update Dots
            dots.tick();
            //Update Pacman
            pacman.tick();
            //Update Ghosts
            blinky.tick();
            pinky.tick();
            inky.tick();
            clyde.tick();
        }
    }

    @Override
    public void render(Graphics2D g) {
        //Black background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Display.width, Display.height);
        //Render Maze
        g.drawImage(Assets.maze, (Display.width - Assets.maze.getWidth())/2, (Display.height-864)/2 + MAP_Y_OFFSET, null);
        //Render Level Indicator
        levelManager.render(g);
        //Render Score Counter
        scoreManager.render(g);
        //Render Dots and Pellets
        dots.render(g);
        //Render Pacman
        pacman.render(g);
        //Render Ghosts
        clyde.render(g);
        inky.render(g);
        pinky.render(g);
        blinky.render(g);
        //Recycle graphics object
        g.dispose();
    }

    public void endLevel() {

        pause = true;
        ghostStateTimer.stopTimers();

        Timer t = new Timer();
        TimerTask endLevelDelay = new TimerTask() {

            @Override
            public void run() {
                blinkMaze();
            }

        };
        t.schedule(endLevelDelay, 1000);

    }

    public void blinkMaze() {

        for (Ghost g : ghosts) {
            g.setInvisible();
        }

        levelManager.aLastTimeUpdate();
        LevelManager.mazeBlink = true;

        Timer t = new Timer();
        TimerTask blinkMazeDelay = new TimerTask() {

            @Override
            public void run() {
                LevelManager.mazeBlink = false;
                levelUp();
            }

        };
        t.schedule(blinkMazeDelay, 3000);

    }

    public void levelUp() {

        LevelManager.ready = true;
        LevelManager.level++;
        board.init();
        Dots.dotsRemaining = 244;
        Ghost.setState(0);
        pacman.setX(336);
        pacman.setY(564);
        pacman.setAnimatorCounter(0);
        pacman.updateDirection('a');
        game.getKeyManager().inputDirection = 'a';
        for (Ghost g : ghosts) {
            g.setX(336);
            g.setY(276);
            g.updateWantsDirection('a');
            g.updateDirection('a');
            g.setUnFreightened();
            g.setUnEaten();
            g.setRevertWarning(false);
        }
        LevelManager.setLevelSpeeds(LevelManager.level);
        LevelManager.setScatterChaseTimes(LevelManager.level);

        Timer t = new Timer();
        TimerTask startDelay = new TimerTask() {

            @Override
            public void run() {
                pause = false;
                LevelManager.ready = false;
                pacman.sLastTimeUpdate();
                pacman.setVisible();
                for (Ghost g : ghosts) {
                    g.sLastTimeUpdate();
                    g.setVisible();
                }
                ghostStateTimer.startGhostStateTimer();
            }

        };
        t.schedule(startDelay, 2000);

    }
    
    public void init() {
    	//soundManager.playSound("/sounds/pacman_beginning.wav");
    	pause = true;
    	LevelManager.gameOver = false;
    	LevelManager.ready = true;
    	LevelManager.level = 1;
    	ScoreManager.clearScore();
    	ScoreManager.lives = 3;
    	board.init();
        Dots.dotsRemaining = 244;
        Ghost.setState(0);
        pacman.setX(336);
        pacman.setY(564);
        pacman.setAnimatorCounter(0);
        pacman.updateDirection('a');
        game.getKeyManager().inputDirection = 'a';
        for (Ghost g : ghosts) {
            g.setX(336);
            g.setY(276);
            g.updateWantsDirection('a');
            g.updateDirection('a');
            g.setUnFreightened();
            g.setUnEaten();
            g.setRevertWarning(false);
        }
        LevelManager.setLevelSpeeds(LevelManager.level);
        LevelManager.setScatterChaseTimes(LevelManager.level);
        TimerTask startDelay = new TimerTask() {

            @Override
            public void run() {
                pause = false;
                LevelManager.ready = false;
                ghostStateTimer = new GhostStateTimer(ghosts);
                pacman.sLastTimeUpdate();
                pacman.setVisible();
                for (Ghost g : ghosts) {
                    g.sLastTimeUpdate();
                    g.setVisible();
                }
                ghostStateTimer.startGhostStateTimer();
            }

        };
        new Timer().schedule(startDelay, 5000);
    }
    
}
