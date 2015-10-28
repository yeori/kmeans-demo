package github.yeori.agorithm.kmeans_demo;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import github.yeori.agorithm.kmeans_demo.main.ui.vm.ClusterVM;

public class SwingUtil {

	
	public static void showMessage (Component parent, String msg) {
		JOptionPane.showMessageDialog(parent, msg);
	}

	public static List<Cluster> mapToCluster(List<ClusterVM> clusterVMs) {
		List<Cluster> clusters = new ArrayList<Cluster>(clusterVMs.size());
		for ( ClusterVM cvm :clusterVMs){
			clusters.add(cvm.getCluster());
		}
		return clusters;
	}
}
