package github.yeori.agorithm.kmeans_demo.main.ui.vm;

import java.awt.Color;

import github.yeori.agorithm.kmeans_demo.Observation;

public class ObservatinVM {

	private Observation target ;
	private Color color;
	public ObservatinVM(Observation target, Color color) {
		super();
		this.target = target;
		this.color = color;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((target == null) ? 0 : target.hashCode());
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
		ObservatinVM other = (ObservatinVM) obj;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ObservatinVM " + target.x + "," + target.y + ", " + color + "]";
	}
	
	
}
