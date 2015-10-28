package github.yeori.agorithm.kmeans_demo.main.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import github.yeori.agorithm.kmeans_demo.Cluster;
import github.yeori.agorithm.kmeans_demo.Observation;
import github.yeori.agorithm.kmeans_demo.main.ui.vm.ClusterVM;

public class MapPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4294326941952902801L;

	private List<Observation> dots = new ArrayList<Observation>();
	
	private int mapWidth;
	private int mapHeight;
	private Color dotColor;
	private int dotRadius;
	
	private boolean showGrid;
	private int gridSize ;
	
	private int clusterRadius = 3;
	private List<MapPanelListener> listeners = new ArrayList<MapPanel.MapPanelListener>();
	
	private ClusterInputHandler handler = new ClusterInputHandler();
	
	private List<ClusterVM> clusters = new ArrayList<ClusterVM>();
	
	/**
	 * @wbp.parser.constructor
	 */
	public MapPanel(int w, int h ) {
		this(w, h, Color.BLACK, 2);
		
	}
	

	public MapPanel(int mapWidth, int mapHeight, Color dotColor, int dotRadius) {
		this(mapWidth, mapHeight, dotColor, dotRadius, null);
	}
	
	public MapPanel(int mapWidth, int mapHeight, Color dotColor, int dotRadius, List<Observation> data) {
		super();
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		this.dotColor = dotColor;
		this.dotRadius = dotRadius;
		
		if( data != null) {
			this.dots.addAll(data);
		}

		this.gridSize=50;
		setOpaque(true);
		installListeners();
	}


	private void installListeners() {
//		ClickHandler c = new ClickHandler();
//		this.addMouseListener(c);
//		this.addMouseMotionListener(c);
		
	}
	
	public void addMapPanelListener( MapPanelListener l) {
		listeners.add(l);
	}
	
	public void removeMapPanelListener ( MapPanelListener l) {
		listeners.remove(l);
	}
	
	public void addClusterVM ( ClusterVM c) {
		this.clusters.add(c);
		this.repaint();
	}

	@Override
	protected void paintComponent(final Graphics g) {
		
		if ( SwingUtilities.isEventDispatchThread() ) {
			paintMapPanel(g);			
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					paintMapPanel(g);
				}
			});
		}
	}
	
	protected void paintMapPanel ( Graphics g) {
		final Dimension size = getSize();
		g.setColor(Color.lightGray);
		g.fillRect(0, 0, size.width, size.height);
		drawBoundary(g);
		if ( showGrid) {
			drawGrid(g);			
		}
		drawDots(g);
		drawCluster(g, clusters);
	}

	private void drawCluster(Graphics g, List<ClusterVM> cvms) {
		Color cache = g.getColor();
		
		for ( int i = 0 ; i < cvms.size(); i++) {
			ClusterVM cvm = cvms.get(i);
			Observation loc = cvm.getCluster().getCentroid();
			int cx = (int)loc.x, cy = (int)loc.y;
			int r = clusterRadius;
			Color color= cvm.getClusterColor();
			g.setColor(color);
			g.fillArc(cx-r, cy-r, 1+2*r, 1+2*r, 0, 360);
			g.setColor(Color.WHITE);
			g.drawArc(cx-r, cy-r, 1+2*r, 1+2*r, 0, 360);
			g.setColor(Color.BLACK);
			g.drawArc(cx-r-1, cy-r-1, 1+2*r+2, 1+2*r+2, 0, 360);
		}
		
		g.setColor(cache);
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
			ClusterVM c = findClusterVM ( dot.getCluster());
			g.setColor(c != null ? c.getClusterColor() : getDotColor());
			g.fillRect((int)(dot.x-r), (int)(dot.y-r), 1 + 2*r, 1 + 2*r);
		}
		
		g.setColor(origin);
		
	}
	
	private ClusterVM findClusterVM(Cluster c) {
		for ( ClusterVM vm : clusters) {
			if ( vm.getCluster().equals(c) ) {
				return vm;
			}
		}
		return null;
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
	
	public boolean inBoundary(int x, int y) {
		
		return ( x >= 0 && x < mapWidth) && ( y >= 0 && y < mapHeight);
	}

	public int getMapWidth() {
		return mapWidth;
	}
	public int getMapHeight() {
		return mapHeight;
	}


	public void update(int width, int height, List<Observation> data) {
		this.mapWidth = width;
		this.mapHeight = height;
		this.dots.clear();
		this.dots.addAll(data);
		this.revalidate();
		this.repaint();
		
	}


	public void setClusterMode(boolean clusterMode) {
		this.showGrid = clusterMode;
		if ( clusterMode) {
			this.addMouseListener(handler);
			this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		} else {
			this.removeMouseListener(handler); 
			this.setCursor(Cursor.getDefaultCursor());
		}
		this.repaint();
	}
	
	class ClusterInputHandler extends MouseAdapter {
		
		@Override
		public void mousePressed(MouseEvent e) {
			int x= e.getX();
			int y = e.getY();
			
			notifyClusterInput(x, y);
		}
	}
	
	public static interface MapPanelListener {
		public void clusterRequested ( int x, int y );
	}

	public void notifyClusterInput(final int x, final int y) {
		new Thread(new Runnable() {
			
			public void run() {
				for ( MapPanelListener l : listeners ) {
					l.clusterRequested(x,  y);
				}
				
			}
		}).start();
		
	}


	public boolean hasInput() {
		return dots.size() > 0;
	}


	public int getClusterSize() {
		return clusters.size();
	}


	public List<Observation> getObservations() {
		return new ArrayList<Observation>(dots);
	}

	public List<ClusterVM> getCluster() {
		return new ArrayList<ClusterVM>(clusters);
	}

	/**
	 * 입력된 관측값을 모두 지움.
	 */
	public void clearObservations() {
		dots.clear();
		repaint();
		
	}


	/**
	 * 등록된 클러스터 모두 제거함
	 */
	public void clearClusters() {
		clusters.clear();
		repaint();
	}
}
