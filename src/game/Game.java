package game;

import javax.swing.JPanel;

import input.KeyboardInput;
import input.MouseInput;
import gfx.Assets;
import gfx.Display;
import state.GameState;
import state.MenuState;
import state.State;

import java.awt.Graphics;
import java.awt.Graphics2D;

public class Game extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	private Display display;
    private Thread thread;
    private boolean running = false;

    public String title;
    public int width;
    public int height;

    public State gameState;
    public State menuState;

    private KeyboardInput keyManager;
    private MouseInput mouseManager;

    public static final int FPS = 90;

    public Game(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
        keyManager = new KeyboardInput();
        mouseManager = new MouseInput();
    }

    private void init() {
        //Load Assets
        Assets.init();

        //Create JFrame and add JPanel to JFrame
        display = new Display(title, width, height);
        setSize(width, height);
        display.getFrame().addKeyListener(keyManager);
        display.getFrame().addMouseListener(mouseManager);
        display.getFrame().addMouseMotionListener(mouseManager);
        display.addPanel(this);

        //Create State Objects and set state
        gameState = new GameState(this);
        menuState = new MenuState(this);
        State.setState(menuState);
    }

    private void tick() {
        if (State.getState() != null) State.getState().tick();
    }

    private void render() {
        repaint();
    }

    public void paintComponent(Graphics g) {
        if (State.getState() != null) State.getState().render((Graphics2D) g);
    }

    @Override
    public void run() {
        init();

        double timePerTick = 1000000000 / FPS;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();
        // long timer = 0;
        // int ticks = 0;

        while(running) {
            now = System.nanoTime();
            delta += (now - lastTime) / timePerTick;
            // timer += now - lastTime;
            lastTime = now;
            if (delta >= 1) {
                tick();
                render();
                delta--;
                // ticks++;
            }
            // if (timer >= 1000000000) {
            //     System.out.println("FPS: " + ticks);
            //     timer = 0;
            //     ticks = 0;
            // }
        }
    }

    public KeyboardInput getKeyManager() {
        return keyManager;
    }
    
    public MouseInput getMouseManager() {
    	return mouseManager;
    }

    public synchronized void start() {
        if (running) return;
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        if (!running) return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
    
}
