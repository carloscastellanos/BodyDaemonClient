/**
 * 
 */
package cc.bodydclient;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

/**
 * @author carlos
 *
 */
class ClientPanel extends JPanel implements MouseListener, MouseMotionListener
{
	int panelWidth, panelHeight;
    String currentLine = "";
    int currLinePos = 0;
	
	// constructor
    protected ClientPanel(int width, int height)
    {
        setSize(width,height);
        panelWidth = width;
        panelHeight = height;
    }
    
    protected void init() {
        setBackground(Color.red);
        // listen for mouse clicks and motion (see handler functions below)
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    

    // This is called by Client to draw text to screen
    protected synchronized void drawLine(String line)
    {
        currentLine = line;
        repaint();
    }

    // Draw to screen: just prints out lines as they come in
    public void paint(Graphics g) {
        g.drawString(currentLine,10,currLinePos*10);
        currLinePos++;
        if (currLinePos*10 > panelHeight) {
            g.clearRect(0, 0, panelWidth, panelHeight);
            currLinePos = 0;
        }
    }

    public void update(Graphics g) {
        paint(g);
    }
    
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

    public void mouseDragged(MouseEvent e)
    {
    	
    }

    public void mouseMoved(MouseEvent e)
    {
    	
    }
}
