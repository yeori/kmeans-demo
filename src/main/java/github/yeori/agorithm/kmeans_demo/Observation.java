package github.yeori.agorithm.kmeans_demo;
/**
 * 관측 데이터(represents observed data)
 * 
 * @author chmin.seo
 *
 */
public class Observation {
	public final double x;
	public final double y;
	private Cluster currentCluster;
	public Observation(int x, int y) {
		this((double)x, (double)y);
	}
	
	public Observation(double x, double y) {
		this.x = x;
		this.y = y;
		this.currentCluster = Cluster.NULL_CLUSTER;
	}
	
	public Cluster getCluster() {
		return currentCluster;
	}
	public void setCluster(Cluster newCluster) {
		this.currentCluster = newCluster;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Observation other = (Observation) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Dot [x=" + x + ", y=" + y + "]";
	}
}