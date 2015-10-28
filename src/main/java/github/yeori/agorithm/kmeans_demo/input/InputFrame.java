package github.yeori.agorithm.kmeans_demo.input;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JList;

import github.yeori.agorithm.kmeans_demo.Observation;
import github.yeori.agorithm.kmeans_demo.SwingUtil;

import javax.swing.JLabel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InputFrame extends JFrame {

	private DefaultListModel<Observation> model = new DefaultListModel<Observation>();
	private JPanel contentPane;
	private JButton textField;
	private InputPanel inputPanel;
	private JList<Observation> observationList;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InputFrame frame = new InputFrame();
					frame.installListener();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void installListener() {
		inputPanel.addInputListener(new InputPanel.InputListener() {
			
			public void clickedAt(int x, int y) {
				System.out.println(x + ", " + y);
				DefaultListModel<Observation> model = (DefaultListModel<Observation>) observationList.getModel();
				model.addElement(new Observation(x, y));
				revalidate();
			}
		});
		
	}

	/**
	 * Create the frame.
	 */
	public InputFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 500);
		contentPane = new JPanel();
//		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		inputPanel = new InputPanel(400, 400);
		contentPane.add(inputPanel, BorderLayout.CENTER);
		
		JPanel dataPanel = new JPanel();
		contentPane.add(dataPanel, BorderLayout.EAST);
		dataPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		dataPanel.add(scrollPane);
		
		observationList = new JList<Observation>(model);
		scrollPane.setViewportView(observationList);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		textField = new JButton("GENERATE");
		textField.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				processSave();
				
			}
		});
		panel.add(textField);
		
		JLabel lblSaveTo = new JLabel("SAVE TO..");
		panel.add(lblSaveTo, BorderLayout.WEST);
	}

	protected void processSave() {
		Date cur = new Date();
		File filepath = new File(new File("input"), generateName(cur));
		try {
			PrintWriter out = new PrintWriter(filepath);
			int dataSize = model.getSize();
			out.println("# generated at " + new Date());
			out.println("# all samples are expected to be located within [map.w, map.h]");
			out.println("# programmed by yeori@github");
			out.println();
			out.println("map.w=" + inputPanel.getMapWidth());
			out.println("map.h=" + inputPanel.getMapHeight());
			out.println("sample.size=" + dataSize);
			for (int i = 0; i < dataSize; i++) {
				Observation o = model.getElementAt(i);
				out.println(String.format("sample.%d=%d,%d",i, (int)o.x, (int)o.y));
			}
			out.close();
			
			SwingUtil.showMessage(this, "saved to " + filepath.getPath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	private String generateName(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd_hh.MM.ss");
		String time = df.format(date);
		return String.format("kmeans-%s.txt", time);
	}

}
