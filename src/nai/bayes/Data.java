package nai.bayes;

import java.util.ArrayList;
import java.util.List;

public class Data {
	private final List<Double[]> x = new ArrayList<>();
	private final List<Integer> d = new ArrayList<>();
	
	public int size() {
		return x.size();
	}
	
	public Double[] xAt(int index) {
		return x.get(index);
	}
	
	public int dAt(int index) {
		return d.get(index);
	}
	
	public void addX(Double[] x) {
		this.x.add(x);
	}
	
	public void addD(Integer d) {
		this.d.add(d);
	}
}
