package state;

import java.awt.Color;
import java.awt.Graphics2D;

import game.Game;
import gfx.Assets;
import gfx.Display;

public class MenuState extends State {

    public MenuState(Game game) {
        super(game);
    }

    @Override
    public void tick() {
        if (hoveringOverPlay() && game.getMouseManager().isLeftPressed()) {
        	((GameState)game.gameState).init();
        	State.setState(game.gameState);
        }
    }

    @Override
    public void render(Graphics2D g) {
    	g.setColor(Color.BLACK);
    	g.fillRect(0, 0, Display.width, Display.height);
        g.drawImage(Assets.title, (Display.width - Assets.title.getWidth()*2)/2, (Display.height-600)/4, Assets.title.getWidth()*2, Assets.title.getHeight()*2, null);
        if (hoveringOverPlay()) g.drawImage(Assets.redPointer, (Display.width - 24)/2 - 24*2, (Display.height - 24)/4 + 80, 24, 24, null);
        else g.drawImage(Assets.pointer, (Display.width - 24)/2 - 24*2, (Display.height - 24)/4 + 80, 24, 24, null);
        g.drawImage(Assets.p, (Display.width - 24)/2 - 24*1, (Display.height - 24)/4 + 80, 24, 24, null);
        g.drawImage(Assets.l, (Display.width - 24)/2, (Display.height - 24)/4 + 80, 24, 24, null);
        g.drawImage(Assets.a, (Display.width - 24)/2 + 24*1, (Display.height - 24)/4 + 80, 24, 24, null);
        g.drawImage(Assets.y, (Display.width - 24)/2 + 24*2, (Display.height - 24)/4 + 80, 24, 24, null);
    }
    
    public boolean hoveringOverPlay() {
    	int x = game.getMouseManager().getMouseX();
    	int y = game.getMouseManager().getMouseY();
    	if (x > (Display.width - 24)/2 - 24*2 && x < (Display.width - 24)/2 + 24*3 && y > (Display.height - 24)/4 + 80 && y < (Display.height - 24)/4 + 104) return true;
    	return false;
    }
    
}
