package github.yeori.agorithm.kmeans_demo;

public class KmeansMath {

	/**
	 * cluster의 중심(centroid)과 관측지점 사이의 제곱 거리를 반환함.
	 * @param cluster
	 * @param o
	 * @return
	 */
	public static double getSumOfSqaure( Cluster cluster, Observation o) {
		Observation c = cluster.getCentroid();
		double dx = c.x - o.x;
		double dy = c.y - o.y;
		return Math.sqrt(dx*dx + dy*dy);
	}
}
