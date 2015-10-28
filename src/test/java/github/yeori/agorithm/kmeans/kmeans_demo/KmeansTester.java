package github.yeori.agorithm.kmeans.kmeans_demo;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import github.yeori.agorithm.kmeans_demo.Cluster;
import github.yeori.agorithm.kmeans_demo.Observation;
import github.yeori.agorithm.kmeans_demo.main.KmeansCalculator;
import github.yeori.agorithm.kmeans_demo.main.KmeansListener;

public class KmeansTester {

	private List<Observation> data;

	@Before
	public void setUp() throws Exception {
		data = new ArrayList<Observation>();
		data.add(new Observation(17, 18));
		data.add(new Observation(50, 18));
		data.add(new Observation(28, 46));
		data.add(new Observation(77, 47));
		data.add(new Observation(56, 73));
		data.add(new Observation(303, 25));
		data.add(new Observation(351, 49));
		data.add(new Observation(298, 100));
		data.add(new Observation(250, 48));
		data.add(new Observation(151, 199));
		data.add(new Observation(197, 200));
		data.add(new Observation(201, 251));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		KmeansCalculator kc = new KmeansCalculator( data);
		
		List<Observation> seeds = new ArrayList<Observation>();
		seeds.add(new Observation( 50, 150));
		seeds.add(new Observation(150,  50));
		seeds.add(new Observation(200, 150));
		kc.calculate(seeds);
		
		KmeansStateListener handler = new KmeansStateListener(kc);
		kc.addKmeansListener ( handler );
		
		do {
			kc.step();
		} while ( handler.hasChanged());
		
		List<Cluster> clusters = kc.getClusters();
		for ( Cluster ct : clusters) {
			List<Observation> obs = ct.getObservations();
			System.out.println(ct);
			for ( Observation o : obs) {
				System.out.println(o);
			}
			System.out.println();
		}
		
	}
	
	
	static class ObservationLog{
		private Cluster oldC;
		private Cluster newc;
		private Observation target;
		public ObservationLog(Cluster oldC, Cluster newc, Observation target) {
			super();
			this.oldC = oldC.copy();
			this.newc = newc.copy();
			this.target = target;
		}
		
		@Override
		public String toString() {
			return  String.format("%s(%.2f, %.2f) -> %s(%.2f, %.2f): %s", 
					oldC.getClusterName(), oldC.getCentroid().x, oldC.getCentroid().y,
					newc.getClusterName(), newc.getCentroid().x, newc.getCentroid().y,
					target);
		}
	}
	
	static class CentroidLog {
		final public Cluster cluster;
		final public Observation oldLoc;
		final public Observation newLoc;
		public CentroidLog(Cluster c, Observation oldLoc, Observation newLoc) {
			super();
			this.cluster = c;
			this.oldLoc = oldLoc;
			this.newLoc = newLoc;
		}
		@Override
		public String toString() {
			return cluster.getClusterName() + ": " + oldLoc + " to " + newLoc;
		}
		
		
	}
	
	static class Step {
		private List<ObservationLog> logs = new ArrayList<KmeansTester.ObservationLog>();
		private List<CentroidLog> centroids = new ArrayList<KmeansTester.CentroidLog>();
		
		public void addLog(ObservationLog clusterChange) {
			logs.add(clusterChange);
		}

		public void addCentroidLog(CentroidLog centroidChange) {
			centroids.add(centroidChange);
		}
		
		public boolean stateChanged() {
			return logs.size() > 0 || centroids.size() > 0 ;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for ( ObservationLog s : logs ) {
				sb.append(s + "\n");
			}
			return sb.toString();
		}
	}
	
	static class KmeansStateListener implements KmeansListener {
		private List<Step> steps = new ArrayList<KmeansTester.Step>();
		private Step current ;
		private KmeansCalculator calc ;
		
		public KmeansStateListener( KmeansCalculator c) {
			calc = c;
		}
		public void newStepStarted() {
			current = new Step();
		}
		public boolean hasChanged() {
			return steps.size() == 0 || steps.size() > 0 && steps.get(steps.size()-1).stateChanged();
		}
		public void observationUpdated(Observation o, Cluster oldCluster, Cluster newCluster) {
			System.out.println(String.format("%s : CLUSTER CHANGED FROM %s TO %s", o.toString(), oldCluster.getClusterName(), newCluster.getClusterName()));
			current.addLog ( new ObservationLog(oldCluster, newCluster, o));
		}
		
		public void centroidUpdated(Cluster c, Observation oldCentroid, Observation newCentroid) {
			System.out.println(String.format("%s: %s -> %s", c.getClusterName(), oldCentroid, newCentroid));
			current.addCentroidLog ( new CentroidLog(c, oldCentroid, newCentroid));
		}
		
		public void stepFinished() {
			steps.add(current);
			current = null;
			System.out.println();
			List<Cluster> clusters = calc.getClusters();
			for ( Cluster ct : clusters) {
				List<Observation> obs = ct.getObservations();
				System.out.println(ct);
				for ( Observation o : obs) {
					System.out.println(o);
				}
				System.out.println();
			}
			System.out.println("=======================");
			
		}
	}

}
