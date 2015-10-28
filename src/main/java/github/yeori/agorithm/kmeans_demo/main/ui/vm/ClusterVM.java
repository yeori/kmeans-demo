package github.yeori.agorithm.kmeans_demo.main.ui.vm;

import java.awt.Color;

import github.yeori.agorithm.kmeans_demo.Cluster;

public class ClusterVM {

	private Cluster cluster;
	private Color color;
	
	public ClusterVM(Cluster cluster, Color color) {
		this.cluster = cluster;
		this.color = color;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cluster == null) ? 0 : cluster.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClusterVM other = (ClusterVM) obj;
		if (cluster == null) {
			if (other.cluster != null)
				return false;
		} else if (!cluster.equals(other.cluster))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ClusterVM [" + cluster.getClusterName() + ", " + color + "]";
	}

	public Color getClusterColor() {
		return color;
	}

	public Cluster getCluster() {
		return cluster;
	}
	
}
