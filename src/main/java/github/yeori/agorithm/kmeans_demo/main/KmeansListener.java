package github.yeori.agorithm.kmeans_demo.main;

import github.yeori.agorithm.kmeans_demo.Cluster;
import github.yeori.agorithm.kmeans_demo.Observation;

public interface KmeansListener {

	/**
	 * observation moved from old cluster to new cluster
	 * @param o
	 * @param oldCluster
	 * @param newCluster
	 */
	void observationUpdated(Observation o, Cluster oldCluster, Cluster newCluster);

	void newStepStarted();

	void stepFinished();

	void centroidUpdated(Cluster c, Observation oldCentroid, Observation newCentroid);

}
