package github.yeori.agorithm.kmeans_demo.main.ui.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;

import github.yeori.agorithm.kmeans_demo.Cluster;
import github.yeori.agorithm.kmeans_demo.main.ui.vm.ClusterVM;

public class ClusterVMRenderer implements ListCellRenderer<ClusterVM> {

	private JLabel label = new JLabel();
	public Component getListCellRendererComponent(JList<? extends ClusterVM> list, ClusterVM value, int index,
			boolean isSelected, boolean cellHasFocus) {
		
		Color c = value.getClusterColor();
		Cluster cluster = value.getCluster();
		Border border = BorderFactory.createLineBorder(c, 3);
		label.setBorder(border);
		label.setText(String.format("%s at (%.2f, %.2f): [%d]", 
				cluster.getClusterName(), 
				cluster.getCentroid().x, 
				cluster.getCentroid().y, 
				cluster.countObservations()));
		return label;
	}

}
