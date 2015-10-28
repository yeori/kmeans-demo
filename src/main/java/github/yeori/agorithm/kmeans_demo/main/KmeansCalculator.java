package github.yeori.agorithm.kmeans_demo.main;

import java.util.ArrayList;
import java.util.List;

import github.yeori.agorithm.kmeans_demo.Cluster;
import github.yeori.agorithm.kmeans_demo.KmeansMath;
import github.yeori.agorithm.kmeans_demo.Observation;

public class KmeansCalculator {

	private List<Observation> observations ;
	
	private List<Cluster> clusters ;
	
	private List<KmeansListener> listeners = new ArrayList<KmeansListener>();
	
	public KmeansCalculator ( List<Observation> data) {
		this.observations = data;
	}
	
	public void calculate ( List<Observation> seedOfClusters ) {
		
		clusters = prepareCluster(seedOfClusters);
	}
	
	public void installCluster (List<Cluster> clusters) {
		this.clusters = clusters;
	}
	
	public void step() {
		notifyNewStep();
		assignObservations(clusters, observations);
		updateCentroids(clusters);
		notifyStepFinished();
	}
	
	private List<Cluster> prepareCluster (List<Observation> seedOfClusters ) {
		char ch = 'A';
		List<Cluster> clusters = new ArrayList<Cluster>(seedOfClusters.size());
		for ( Observation initialSeed : seedOfClusters) {
			clusters.add(new Cluster("C" + ch , initialSeed));
			ch++;
		}
		return clusters;
	}
	
	/**
	 * 중심값(centroid) 갱신
	 * @param clusters2
	 */
	private void updateCentroids(List<Cluster> clusters) {
		
		for( Cluster c : clusters ) {
			Observation oldCentroid = c.getCentroid();
			Observation newCentroid = c.updateCentroid();
			if ( !newCentroid.equals(oldCentroid)) {
				notifyCentroidUpdated ( c, oldCentroid, newCentroid);
			}
		}
	}



	private void assignObservations(List<Cluster> clusters, List<Observation> observations) {
		for ( Observation o : observations) {
			Cluster nearest = findNearestCluster( o, clusters );
			changeCluster ( o, nearest);
		}
	}


	private void changeCluster(Observation o, Cluster newCluster) {
		if ( newCluster == null) {
			throw new RuntimeException("new cluster cannot be null");
		}
		
		Cluster oldCluster = o.getCluster();
		if ( !newCluster.equals(oldCluster) ) {
			oldCluster.removeObservation(o);
			newCluster.addObservation(o);
			o.setCluster(newCluster);
			notifyObservationUpdated ( o, oldCluster, newCluster);
		}
	}

	protected void notifyNewStep(){
		for ( KmeansListener l : listeners) {
			l.newStepStarted();
		}
	}
	/**
	 * cluster의 중심값(centroid)이 갱신되었음을 리스너들에게 통보함.
	 * @param c -중심값(cetroid)이 갱신된 클러스터
	 * @param oldCentroid - 이전의 중심값
	 * @param newCentroid - 새로 갱신된 중심값.
	 */
	protected void notifyCentroidUpdated(Cluster c, Observation oldCentroid, Observation newCentroid) {
		for ( KmeansListener l : listeners) {
			l.centroidUpdated ( c, oldCentroid, newCentroid);
		}
	}
	
	/**
	 * 주어진 관측값의 cluster가 변경되었음.
	 * @param o 관측값
	 * @param oldCluster 이전에 속했던 클러스터
	 * @param newCluster 새로운 클러스터(o가 현재 속한 클러스터)
	 */
	protected void notifyObservationUpdated(Observation o, Cluster oldCluster, Cluster newCluster) {
		for ( KmeansListener l : listeners) {
			l.observationUpdated ( o, oldCluster, newCluster);
		}
	}
	
	protected void notifyStepFinished() {
		for ( KmeansListener l : listeners) {
			l.stepFinished();
		}
	}

	private Cluster findNearestCluster(Observation o, List<Cluster> clusters) {
		Cluster nearest = clusters.get(0);
		double minDistance = KmeansMath.getSumOfSqaure ( nearest, o);
		for ( Cluster c : clusters) {
			double dist = KmeansMath.getSumOfSqaure(c, o);
			if ( Double.compare(dist, minDistance) < 0 ) {
				nearest = c;
				minDistance = dist;
			}
		}
		return nearest;
	}

	public void addKmeansListener(KmeansListener l) {
		listeners.add(l);
	}

	public List<Cluster> getClusters() {
		return clusters;
	}

	
	/*static class KMSeedGenerator {
		private int nClusters;
		
		List<Observation> seeds = new ArrayList<Observation>();
		public KMSeedGenerator( int nClusters) {
			this.nClusters = nClusters;
		}
		
		public List<Observation> getSeeds() {
			return seeds;
		}
	}*/
	
}
