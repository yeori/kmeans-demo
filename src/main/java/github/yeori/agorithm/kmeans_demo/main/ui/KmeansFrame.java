package github.yeori.agorithm.kmeans_demo.main.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import github.yeori.agorithm.kmeans_demo.Cluster;
import github.yeori.agorithm.kmeans_demo.Observation;
import github.yeori.agorithm.kmeans_demo.SwingUtil;
import github.yeori.agorithm.kmeans_demo.input.InputFrame;
import github.yeori.agorithm.kmeans_demo.main.KmeansCalculator;
import github.yeori.agorithm.kmeans_demo.main.KmeansListener;
import github.yeori.agorithm.kmeans_demo.main.ui.MapPanel.MapPanelListener;
import github.yeori.agorithm.kmeans_demo.main.ui.renderer.ClusterVMRenderer;
import github.yeori.agorithm.kmeans_demo.main.ui.vm.ClusterVM;
import github.yeori.agorithm.kmeans_demo.util.MapReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JSplitPane;
import javax.swing.ListModel;

import java.awt.Cursor;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JToolBar;
import javax.swing.JButton;

public class KmeansFrame extends JFrame {

	private JPanel contentPane;
	
	private DefaultListModel<ClusterVM> clusterVMModel ;

	private JList<ClusterVM> clusterList;

	private MapPanel mapPanel;
	private JLabel lblInputFilePath;
	private JMenuItem menuClusterMode;
	private JMenuItem menuMapMode;

	private JButton runButton;

	private JButton btnStep;

	private KmeansController stepController = new KmeansController();

	private KmeansCalculator kmeansCalculator;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					KmeansFrame frame = new KmeansFrame();
					frame.prepareUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	protected void prepareUI() {
		clusterList.setCellRenderer(new ClusterVMRenderer());
//		this.clusterVMModel.addElement(new ClusterVM(new Cluster("CA", new Observation(10, 10)), Color.RED));
//		this.clusterVMModel.addElement(new ClusterVM(new Cluster("CB", new Observation(20, 20)), Color.BLUE));
		
		mapPanel.addMapPanelListener(new ClusterRegister());
		
	}

	/**
	 * Create the frame.
	 */
	public KmeansFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 500);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("Load");
		menuBar.add(mnFile);
		
		JMenuItem mntmSample = new JMenuItem("Sample");
		mntmSample.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processLoadSample();
			}
		});
		mnFile.add(mntmSample);
		
		JMenu mnInput = new JMenu("Input");
		menuBar.add(mnInput);
		
		JMenuItem mntmOpenInput = new JMenuItem("Open Input");
		mntmOpenInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processOpenInputFrame();
			}
		});
		mnInput.add(mntmOpenInput);
		
		JMenuItem mntmClearInput = new JMenuItem("Clear Input");
		mntmClearInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processClearInput();
			}
		});
		mnInput.add(mntmClearInput);
		
		JMenu mnCluster = new JMenu("Cluster");
		menuBar.add(mnCluster);
		
		JMenuItem mntmReset = new JMenuItem("Clear Cluster");
		mntmReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processClearCluster();
			}
		});
		mnCluster.add(mntmReset);
		
		menuClusterMode = new JMenuItem("Cluster Mode");
		menuClusterMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processTurnOnClusterMode();
			}
		});
		mnCluster.add(menuClusterMode);
		
		menuMapMode = new JMenuItem("Map Mode");
		menuMapMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processTurnOffClusterMode();
			}
		});
		menuMapMode.setEnabled(false);
		mnCluster.add(menuMapMode);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.8);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setResizeWeight(1.0);
		splitPane.setLeftComponent(splitPane_1);
		
		JPanel controllPanel = new JPanel();
		splitPane_1.setRightComponent(controllPanel);
		controllPanel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblClusters = new JLabel("Clusters");
		lblClusters.setFont(new Font("Tahoma", Font.BOLD, 18));
		controllPanel.add(lblClusters, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		controllPanel.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane);
		
		clusterList = new JList<ClusterVM>(initClusterListModel());
		scrollPane.setViewportView(clusterList);
		
		JPanel mapWrapperPanel = new JPanel();
		splitPane_1.setLeftComponent(mapWrapperPanel);
		mapWrapperPanel.setLayout(new BorderLayout(0, 0));
		
		lblInputFilePath = new JLabel("");
		lblInputFilePath.setFont(new Font("Tahoma", Font.PLAIN, 12));
		mapWrapperPanel.add(lblInputFilePath, BorderLayout.NORTH);
		
		mapPanel = new MapPanel(400, 400 );
		mapWrapperPanel.add(mapPanel, BorderLayout.CENTER);
		mapPanel.setFocusable(false);
		mapPanel.setLayout(null);
		
		JPanel logPanel = new JPanel();
		splitPane.setRightComponent(logPanel);
		
		JToolBar toolBar =  new JToolBar();
		contentPane.add(toolBar, BorderLayout.NORTH);
		
		runButton = ToolbarButtonFactory.createToolbarButton("RUN", "run.png", 24);
		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processRunKmeans();
			}
		});
		toolBar.add(runButton);
		
		btnStep = ToolbarButtonFactory.createToolbarBotton("STEP", 24);
		btnStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processKmeansStep();
			}
		});
		btnStep.setEnabled(false);
		toolBar.add(btnStep);
		
	}

	/**
	 * 클러스터를 지움.
	 */
	protected void processClearCluster() {
		clusterVMModel.clear();
		mapPanel.clearClusters();
		
		btnStep.setEnabled(false);
	}

	/**
	 * 로드된 입력을 지움.
	 */
	protected void processClearInput() {
		kmeansCalculator = null;
		mapPanel.clearObservations();
		btnStep.setEnabled(false);
		
	}

	protected void processKmeansStep() {
		if ( kmeansCalculator == null) {
			SwingUtil.showMessage(this, "load input and register cluster");
			return;
		}
		
		kmeansCalculator.step();
		this.clusterList.revalidate();
	}

	protected void processRunKmeans() {
		List<Observation> data = mapPanel.getObservations();
		if ( ! mapPanel.hasInput() ) {
			SwingUtil.showMessage(this, "add input using menu ( Load > Sample )");
			return ;
		}
		
		List<ClusterVM> clusterVMs = mapPanel.getCluster();
		if ( clusterVMs.size() ==0 ){
			SwingUtil.showMessage(this, "at least 2 closters required ( Cluster > Cluster Mode )");
			return ;
		}
		
		kmeansCalculator = new KmeansCalculator(data);
		kmeansCalculator.installCluster(SwingUtil.mapToCluster(clusterVMs));
		kmeansCalculator.addKmeansListener(stepController);
		btnStep.setEnabled(true);
		
		menuClusterMode.setEnabled(true);
		menuMapMode.setEnabled(false);
		mapPanel.setClusterMode(false);
	}

	protected void processTurnOnClusterMode() {
		mapPanel.setClusterMode(true);
		menuClusterMode.setEnabled(false);
		menuMapMode.setEnabled(true);
	}
	
	protected void processTurnOffClusterMode() {
		mapPanel.setClusterMode(false);
		menuClusterMode.setEnabled(true);
		menuMapMode.setEnabled(false);
	}

	protected void processOpenInputFrame() {
		InputFrame inputFrame = new InputFrame();
		inputFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		inputFrame.setTitle("CLICK THE INPUT PANEL");
		inputFrame.setAlwaysOnTop(true);
		inputFrame.installListener();
		inputFrame.setLocationRelativeTo(this);
		inputFrame.setVisible(true);
	}

	/**
	 * input 데이터를 읽어서 Map을 초기호함.
	 */
	protected void processLoadSample() {
		JFileChooser jc = new JFileChooser(new File("."));
		int option = jc.showOpenDialog(this);
		
		if ( option == JFileChooser.APPROVE_OPTION) {
			initMap(jc.getSelectedFile());
			lblInputFilePath.setText(jc.getSelectedFile().getAbsolutePath());
		}
		
	}

	private void initMap(File input) {
		MapReader reader = new MapReader( input );
		int w = reader.getMapWidth();
		int h = reader.getMapHeight();
		List<Observation> data = reader.getObservations();
		mapPanel.update(w, h, data);
		
	}

	private ListModel<ClusterVM> initClusterListModel() {
		this.clusterVMModel = new DefaultListModel<ClusterVM>();
		
		return clusterVMModel;
	}
	
	class ClusterRegister implements MapPanelListener {

		char ch = 'A';
		List<Color> colors = Arrays.asList(Color.RED, Color.CYAN, Color.BLUE, Color.GREEN.darker(), Color.MAGENTA, Color.ORANGE,Color.PINK);
		int colorIndex = 0;
		public void clusterRequested(int x, int y) {
			Color c= colors.get(colorIndex);
			ClusterVM cluster = new ClusterVM(new Cluster("C" + ch, new Observation(x, y)), c);
			clusterVMModel.addElement(cluster);
			colorIndex = (colorIndex+1) % colors.size();
			ch++;
			mapPanel.addClusterVM(cluster);
		}
		
	}
	
	class KmeansController implements KmeansListener{

		public void newStepStarted() {
			
		}

		public void observationUpdated(Observation o, Cluster oldCluster, Cluster newCluster) {
			System.out.println(String.format("%s : CLUSTER CHANGED FROM %s TO %s", o.toString(), oldCluster.getClusterName(), newCluster.getClusterName()));
		}

		public void centroidUpdated(Cluster c, Observation oldCentroid, Observation newCentroid) {
			System.out.println(String.format("%s: %s -> %s", c.getClusterName(), oldCentroid, newCentroid));
		}
		
		public void stepFinished() {
			mapPanel.repaint();
			System.out.println("======================");
			System.out.println();
		}
		
	}
}
