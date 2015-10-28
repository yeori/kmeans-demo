package github.yeori.agorithm.kmeans_demo.input;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import github.yeori.agorithm.kmeans_demo.Observation;

public class InputPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6997638332286751965L;

	private List<Observation> dots = new ArrayList<Observation>();
	
	private List<InputListener> listeners = new ArrayList<InputPanel.InputListener>();
	private int mapWidth;
	private int mapHeight;
	private Color dotColor;
	private int dotRadius;
	
	private boolean showGrid ;
	private int gridSize;

	private int cursorX, cursorY;
	/**
	 * @wbp.parser.constructor
	 */
	public InputPanel(int w, int h) {
		this(w, h, Color.BLACK, 2);
		
	}
	

	public InputPanel(int mapWidth, int mapHeight, Color dotColor, int dotRadius) {
		super();
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		this.dotColor = dotColor;
		this.dotRadius = dotRadius;

		this.cursorX = mapWidth + 1;
		this.cursorY = mapHeight + 1;
		
		this.showGrid = true;
		this.gridSize = 50;
		setOpaque(true);
		installListeners();
	}


	private void installListeners() {
		ClickHandler c = new ClickHandler();
		this.addMouseListener(c);
		this.addMouseMotionListener(c);
		
	}

	@Override
	protected void paintComponent(Graphics g) {
		
		Dimension size = getSize();
		g.setColor(Color.lightGray);
		g.fillRect(0, 0, size.width, size.height);
		drawBoundary(g);
		drawDots(g);
		if ( showGrid) {
			drawGrid(g);
		}
		drawCursorInfo(g);
	}
	
	private void drawGrid(Graphics g) {
		int gs = gridSize;
		int W = mapWidth;
		int H = mapHeight;
		Color origin = g.getColor();
		g.setColor(Color.LIGHT_GRAY);
		for ( int offsetX = gs ; offsetX < W ; offsetX += gs) {
			g.drawLine(offsetX, 0, offsetX, H);
		}
		for ( int offsetY = gs ; offsetY < H ; offsetY += gs) {
			g.drawLine(0, offsetY, W, offsetY);
		}
		
		g.setColor(origin);
		
	}


	private void drawCursorInfo(Graphics g) {
		Color origin = g.getColor();
		
		g.setColor(Color.DARK_GRAY);
		if ( cursorX < mapWidth &&  cursorY < mapHeight) {
			Font oldF = g.getFont();
			g.setFont(new Font("Courier New", Font.PLAIN, 14));
			g.drawString(format("x:%d, y:%d", cursorX, cursorY), 10, 30);
			g.setFont(oldF);
		}
		
		g.setColor(origin);
		
	}


	private void drawBoundary(Graphics g) {
		Color origin = g.getColor();
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, mapWidth, mapHeight);
//		g.setColor(Color.BLACK);
//		g.drawRect(0, 0, mapWidth+1, mapHeight+1);
		
		g.setColor(origin);
	}

	private void drawDots(Graphics g) {
		Color origin = g.getColor();
		
		g.setColor(getDotColor());
		int r = getDotRadius();
		for ( int i = 0 ; i < dots.size(); i++) {
			Observation dot = dots.get(i);
			g.fillRect((int)(dot.x-r), (int)(dot.y-r), 1 + 2*r, 1 + 2*r);
		}
		
		g.setColor(origin);
		
	}
	
	public boolean isGridVisible() {
		return showGrid;
	}
	
	public void setGridVisible(boolean visible) {
		showGrid = visible;
	}
	
	public void setGridSize( int size) {
		gridSize = size;
	}
	
	public int getGridSize() {
		return gridSize;
	}

	public int getDotRadius() {
		return this.dotRadius;
	}

	public Color getDotColor() {
		return this.dotColor;
	}

	private static String format ( String format, Object ... args) {
		return String.format(format, args);
	}
	
	private class ClickHandler implements MouseListener, MouseMotionListener{

		public void mouseClicked(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
			int x = e.getX(), y = e.getY();
			if ( inBoundary ( x, y )) {
				dots.add(new Observation(x, y));
				notifyToListener ( x, y);
				repaint();				
			}
			
		}

		public void mouseReleased(MouseEvent e) {
			
		}

		public void mouseEntered(MouseEvent e) {
			
		}

		public void mouseExited(MouseEvent e) {
			
		}

		public void mouseDragged(MouseEvent e) {
		}

		public void mouseMoved(MouseEvent e) {
			printCursorPosition(e.getX(), e.getY());
			
		}
		
	}
	public void addInputListener ( InputListener l ) {
		if ( listeners.contains(l)) {
			removeInputListener(l);
		}
		listeners.add(l);
	}
	
	public void printCursorPosition(int x, int y) {
		this.cursorX = x;
		this.cursorY = y;
		repaint();
		
	}


	public boolean inBoundary(int x, int y) {
		
		return ( x >= 0 && x < mapWidth) && ( y >= 0 && y < mapHeight);
	}

	/**
	 * notifies listeners which position to be clicked
	 * @param x
	 * @param y
	 */
	public void notifyToListener(final int x, final int y) {
		Runnable r = new Runnable() {
			public void run() {
				for ( InputListener l : listeners ) {
					l.clickedAt(x, y);
				}
				
			}
		};
		
		new Thread(r).start();
	}

	public void removeInputListener ( InputListener l ){
		listeners.remove(l);
	}
	public static interface InputListener {
		public void clickedAt ( int x, int y ) ;
	}
	public int getMapWidth() {
		return mapWidth;
	}
	public int getMapHeight() {
		return mapHeight;
	}

}
