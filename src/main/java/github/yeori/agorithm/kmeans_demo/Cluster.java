package github.yeori.agorithm.kmeans_demo;

import java.util.ArrayList;
import java.util.List;

public class Cluster {
	private Observation centroid ;
	final private String clusterName ;
	
	public static final Cluster NULL_CLUSTER = new Cluster("C-NL", new Observation(0, 0));
	/**
	 * belonging to this cluster
	 */
	private List<Observation>  observations = new ArrayList<Observation>();
	
	public Cluster (String clusterName, Observation initialSeed) {
		centroid = initialSeed;
		this.clusterName = clusterName;
	}

	public void addObservation(Observation o) {
		observations.add(o);
		
	}

	public Observation getCentroid() {
		return centroid;
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clusterName == null) ? 0 : clusterName.hashCode());
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
		Cluster other = (Cluster) obj;
		if (clusterName == null) {
			if (other.clusterName != null)
				return false;
		} else if (!clusterName.equals(other.clusterName))
			return false;
		return true;
	}

	/**
	 * centroid를 갱신함. 
	 * @return 새로 계산된 centroid를 반환함.
	 */
	public Observation updateCentroid() {
		Observation o;
		double tx= 0, ty= 0;
		for( int i = 0 ; i < observations.size(); i++) {
			o = observations.get(i);
			tx += o.x;
			ty += o.y;
		}
		centroid = new Observation(tx/observations.size(),ty/observations.size());
		return centroid;
	}

	public String getClusterName() {
		return this.clusterName;
	}

	/**
	 * cluster에 포함된 observation의 갯수
	 * @return number of observations belonging to this cluster
	 */
	public int countObservations() {
		return observations.size();
	}

	@Override
	public String toString() {
		return String.format("Cluster %s at (%2f,%2f) has %d observations", clusterName, centroid.x, centroid.y, observations.size());
	}

	public void removeObservation(Observation o) {
		observations.remove(o);
	}

	public Cluster copy() {
		Cluster copied = new Cluster(clusterName, centroid);
		return copied;
	}

	public List<Observation> getObservations() {
		return observations;
	}
	
	
	
}