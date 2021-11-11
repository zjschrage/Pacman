package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInput implements KeyListener {

    public char currentDirection = 'a';
    public char inputDirection = 'a';

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'w':
                inputDirection = 'w';
                break;
            case 'a':
                inputDirection = 'a';
                break;
            case 's':
                inputDirection = 's';
                break;
            case 'd':
                inputDirection = 'd';
                break;
        }
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

    public char getCurrentDirection() {
        return currentDirection;
    }

    public char getInputDirection() {
        return inputDirection;
    }

    public void setCurrentDirection(char currentDirection) {
        this.currentDirection = currentDirection;
    }
    
}
