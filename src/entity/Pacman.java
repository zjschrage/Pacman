package entity;

import java.awt.Graphics2D;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import game.Board;
import game.Game;
import game.LevelManager;
import game.ScoreManager;
import gfx.Assets;
import gfx.Display;
import state.GameState;

public class Pacman extends Mob {

    //Reference Objects
    public Ghost[] ghosts;

    //Other Variables
    public boolean ghostsFreightened = false;

    //Movement Animation
    private int[] animator = {0, 1, 2, 1};
    private int animatorLength = 4;
    private int animatorCounter = 0;

    //Death Animation
    private boolean isDying = false;
    private int deathAnimCounter = 0;
    private double dTimePerTick = 1000000000 / 6;
    private double dDelta = 0;
    private long dNow;
    private long dLastTime = System.nanoTime();

    public Pacman(Game game, Board board, float x, float y) {
        super(game, board, x, y, Entity.DEFAULT_ENTITY_WIDTH, Entity.DEFAULT_ENTITY_HEIGHT);
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
        //Test movement
        boolean[] movedAndChanged = move(game, board, game.getKeyManager().getInputDirection(), game.getKeyManager().getCurrentDirection(), moveDistance);
        if (movedAndChanged[0]) {
            //Update Direction
            if (movedAndChanged[1]) updateDirection(game.getKeyManager().getInputDirection());
            //Dot and Power pellet consumption
            if (board.getTile((int)x, (int)y).getId() == 1) {
                board.setTileId((int)x, (int)y, 7);
                Dots.eatDot();
                if (Dots.getDotsRemaining() == 174 || Dots.getDotsRemaining() == 74) ScoreManager.setFruitAvailible();
                ScoreManager.score += 10;
                ScoreManager.scoreImgUpdate();
                if (!ghostsFreightened) setSpeed(LevelManager.speeds[1]);
                else setSpeed(LevelManager.speeds[3]);
            }
            else if (board.getTile((int)x, (int)y).getId() == 2) {
                board.setTileId((int)x, (int)y, 7);
                Dots.eatDot();
                if (Dots.getDotsRemaining() == 174 || Dots.getDotsRemaining() == 74) ScoreManager.setFruitAvailible();
                ScoreManager.score += 50;
                ScoreManager.scoreImgUpdate();
                //Set ghosts to freightened mode
                for (Ghost g : ghosts) {
                    if (!g.eaten) {
                        g.setFreightened();
                    }
                }
                ghosts[0].freightenedTimer();
                ghostsFreightened = true;
                setSpeed(LevelManager.speeds[2]);
            }
            else setSpeed(LevelManager.speeds[0]);
            //Eating Fruit
            if (ScoreManager.getFruitAvailible()) {
                if (x >= 324 && x <= 348 && y == 420) {
                    ScoreManager.score += ScoreManager.fruitEatingScores[Math.min(LevelManager.level-1, 12)];
                    ScoreManager.scoreImgUpdate();
                    ScoreManager.setFruitUnavailible();
                    ScoreManager.setFruitEaten();
                }
            }
            //Animation
            aNow = System.nanoTime();
            aDelta += (aNow - aLastTime) / aTimePerTick;
            aLastTime = aNow;
            if (aDelta >= 1) {
                animatorCounter++;
                if (animatorCounter == animatorLength) animatorCounter = 0;
                aDelta = 0;
            }
        }
        //Ghost Collision
        for (Ghost g : ghosts) {
            //Only die once if collision with multiple ghosts
            if (isDying) return;
            if ((int)g.getX()/board.getTileSize() == (int)x/board.getTileSize() && (int)g.getY()/board.getTileSize() == (int)y/board.getTileSize()) {
                //Eating a Ghost
                if (g.freightened == true) {
                    g.setEaten();
                    //Find out how many ghosts are freightened for score calculation
                    int n = 3;
                    for (Ghost g2 : ghosts) {
                        if (g2.isFreightened()) n--;
                    }
                    ScoreManager.score += ScoreManager.ghostEatingScores[n];
                    ScoreManager.scoreImgUpdate();
                    //Eat Ghost --> Pac and all Ghosts Pause
                    setSpeed(0);
                    for (Ghost g2 : ghosts) {
                        g2.setSpeed(0);
                    }
                    //Eat Ghost --> Pac and eaten ghost invisible
                    setInvisible();
                    g.setInvisible();
                    //Display ghost eating points
                    ScoreManager.ghostEatingPause(ScoreManager.ghostEatingScores[n], (int)x, (int)y + MAP_Y_OFFSET);
                    //Revert Timer
                    Timer t = new Timer();
                    TimerTask eatGhostPause = new TimerTask() {

                        @Override
                        public void run() {
                            //Remove ghost eating points
                            ScoreManager.endGhostEatingPause();
                            //Revert Speeds after pause
                            setSpeed(LevelManager.speeds[2]);
                            for (Ghost g3 : ghosts) {
                                if (g3.isFreightened()) g3.setSpeed(LevelManager.speeds[5]);
                                else if (g3.isEaten()) g3.setSpeed(1);
                                else g3.setSpeed(LevelManager.speeds[4]);
                            }
                            //Set Pac and eaten ghost back to visible
                            setVisible();
                            g.setVisible();
                        }
                        
                    };
                    t.schedule(eatGhostPause, 500);

                }
                //Dying
                else if (!g.eaten) {
                    ScoreManager.lives--;
                    GameState.pause = true;
                    for (Ghost g2 : ghosts) {
                        g2.setInvisible();
                    }
                    //Death Animation
                    isDying = true;
                    dLastTime = System.nanoTime();
                    Timer t = new Timer();
                    TimerTask deathAnimationDelay = new TimerTask() {

                        @Override
                        public void run() {
                            //Following Death Animation check lives count
                            if(ScoreManager.lives == 0) {
                                if (ScoreManager.score > ScoreManager.highScore) {
                                    try {
                                        FileWriter w = new FileWriter("res/files/highscore.txt");
                                        w.write(ScoreManager.score + "");
                                        w.close();
                                    } catch (IOException e) {

                                    }
                                }
                                GameState.pause = false;
                                LevelManager.gameOver = true;
                            }
                            else {
                            	
                            	LevelManager.ready = true;
                            	
                            	setX(336);
                                setY(564);
                                setSpeed(LevelManager.speeds[0]);
                                setVisible();
                                setAnimatorCounter(0);
                                updateDirection('a');
                                game.getKeyManager().inputDirection = 'a';
                                for (Ghost g2 : ghosts) {
                                    g2.setX(336);
                                    g2.setY(276);
                                    g2.setUnFreightened();
                                    g2.setUnEaten();
                                    g2.setRevertWarning(false);
                                    g2.setSpeed(LevelManager.speeds[4]);
                                    g2.setVisible();
                                    g2.updateWantsDirection('a');
                                    g2.updateDirection('a');
                                }
                            	
                            	Timer t = new Timer();
                            	TimerTask readyAfterDeath = new TimerTask() {

									@Override
									public void run() {
										LevelManager.ready = false;
		                                for (Ghost g2 : ghosts) {
		                                    g2.sLastTimeUpdate();
		                                }
		                                sLastTimeUpdate();
		                                GameState.pause = false;
									}
                            		
                            	};
                            	t.schedule(readyAfterDeath, 2000);
                            	
                            }
                        }

                    };
                    t.schedule(deathAnimationDelay, 3500);
                }
            }
        }
    }

    @Override
    public void render(Graphics2D g) {
        if (getVisibility()) return;
        int screenXOffset = (Display.width - Assets.maze.getWidth())/2;
        int screenYOffset = (Display.height-864)/2;
        if (isDying) {
            dNow = System.nanoTime();
            dDelta += (dNow - dLastTime) / dTimePerTick;
            dLastTime = dNow;
            if (dDelta >= 1) {
                deathAnimCounter++;
                dDelta--;
                if(deathAnimCounter == 11) {
                    isDying = false;
                    deathAnimCounter = 0;
                    setInvisible();
                    return;
                }
            }
            g.drawImage(Assets.pacmanDeath[deathAnimCounter], (int)x-(width/2) + screenXOffset, (int)y-(height/2) + (Display.height-864)/2  + MAP_Y_OFFSET, null);
        }
        else {
            switch (game.getKeyManager().getCurrentDirection()) {
                case 'w':
                    g.drawImage(Assets.pacmanU[animator[animatorCounter]], (int)x-(width/2) + screenXOffset, (int)y-(height/2) + screenYOffset  + MAP_Y_OFFSET, width, height, null);
                    break;
                case 'a':
                    g.drawImage(Assets.pacmanL[animator[animatorCounter]], (int)x-(width/2) + screenXOffset, (int)y-(height/2) + screenYOffset + MAP_Y_OFFSET, width, height, null);
                    break;
                case 's':
                    g.drawImage(Assets.pacmanD[animator[animatorCounter]], (int)x-(width/2) + screenXOffset, (int)y-(height/2) + screenYOffset + MAP_Y_OFFSET, width, height, null);
                    break;
                case 'd':
                    g.drawImage(Assets.pacmanR[animator[animatorCounter]], (int)x-(width/2) + screenXOffset, (int)y-(height/2) + screenYOffset + MAP_Y_OFFSET, width, height, null);
                    break;
            }
        }
    }

    public void updateDirection(char d) {
        game.getKeyManager().setCurrentDirection(d);
    }

    public void passGhostReference(Ghost[] ghosts) {
        this.ghosts = ghosts;
    }

    public void setAnimatorCounter(int animatorCounterIn) {
        animatorCounter = animatorCounterIn;
    }

}
