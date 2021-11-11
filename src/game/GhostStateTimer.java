package game;

import java.util.Timer;
import java.util.TimerTask;

import entity.Ghost;

public class GhostStateTimer {

    //Reference Objects
    private Ghost[] ghosts;

    private TimerTask[] tasks = new TimerTask[7];

    public GhostStateTimer(Ghost[] ghosts) {
        this.ghosts = ghosts;
    }

    public void startGhostStateTimer() {
        MyTimerTask1 t1 = new MyTimerTask1();
        t1.start();
        tasks[0] = t1;
    }
    
    public void stopTimers() {
        for (int i = 0; i < tasks.length; i++) {
            try {
                tasks[i].cancel();
            } catch (NullPointerException e) {
                
            }
        }
    }

    private void flipDirection() {
        for (Ghost g : ghosts) {
            if (!(g.isFreightened() || g.isEaten())) g.updateWantsDirection(g.oppositeDirection());
        }
    }

    class MyTimerTask1 extends TimerTask {
        private Timer timer;
    
        public void run() {
            Ghost.setState(1);
            flipDirection();
            MyTimerTask2 t2 = new MyTimerTask2();
            t2.start();
            tasks[1] = t2;
        }
    
        public void start() {
            timer = new Timer();
            timer.schedule(this, LevelManager.scatterChaseTimes[0]);
        }
    }
    
    class MyTimerTask2 extends TimerTask {
        private Timer timer;
    
        public void run() {
            Ghost.setState(0);
            flipDirection();
            MyTimerTask3 t3 = new MyTimerTask3();
            t3.start();
            tasks[2] = t3;
        }
    
        public void start() {
            timer = new Timer();
            timer.schedule(this, LevelManager.scatterChaseTimes[1]);
        }
    }
    
    class MyTimerTask3 extends TimerTask {
        private Timer timer;
    
        public void run() {
            Ghost.setState(1);
            flipDirection();
            MyTimerTask4 t4 = new MyTimerTask4();
            t4.start();
            tasks[3] = t4;
        }
    
        public void start() {
            timer = new Timer();
            timer.schedule(this, LevelManager.scatterChaseTimes[2]);
        }
    }

    class MyTimerTask4 extends TimerTask {
        private Timer timer;
    
        public void run() {
            Ghost.setState(0);
            flipDirection();
            MyTimerTask5 t5 = new MyTimerTask5();
            t5.start();
            tasks[4] = t5;
        }
    
        public void start() {
            timer = new Timer();
            timer.schedule(this, LevelManager.scatterChaseTimes[3]);
        }
    }

    class MyTimerTask5 extends TimerTask {
        private Timer timer;
    
        public void run() {
            Ghost.setState(1);
            flipDirection();
            MyTimerTask6 t6 = new MyTimerTask6();
            t6.start();
            tasks[5] = t6;
        }
    
        public void start() {
            timer = new Timer();
            timer.schedule(this, LevelManager.scatterChaseTimes[4]);
        }
    }

    class MyTimerTask6 extends TimerTask {
        private Timer timer;
    
        public void run() {
            Ghost.setState(0);
            flipDirection();
            MyTimerTask7 t7 = new MyTimerTask7();
            t7.start();
            tasks[6] = t7;
        }
    
        public void start() {
            timer = new Timer();
            timer.schedule(this, LevelManager.scatterChaseTimes[5]);
        }
    }

    class MyTimerTask7 extends TimerTask {
        private Timer timer;
    
        public void run() {
            Ghost.setState(1);
            flipDirection();
        }
    
        public void start() {
            timer = new Timer();
            timer.schedule(this, LevelManager.scatterChaseTimes[6]);
        }
    }

}
