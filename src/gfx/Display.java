package gfx;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.*;

public class Display extends JFrame implements ComponentListener {

 
	private static final long serialVersionUID = 1L;
	private String title;
    public static int width;
    public static int height;

    public Display(String title, int widthIn, int heightIn) {
        this.title = title;
        width = widthIn;
        height = heightIn;
        createDisplay();
    }

    private void createDisplay() {
        new JFrame(title);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setLocationRelativeTo(null);
        setVisible(true);
        getContentPane().addComponentListener(this);
    }

    public void addPanel(JPanel panel) {
        this.add(panel);
    }

    public JFrame getFrame() {
        return this;
    }

    public void componentResized(ComponentEvent ce) {
        height = this.getHeight();
        width = this.getWidth();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        
    }

    @Override
    public void componentShown(ComponentEvent e) {
        
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        
    }
    
}
