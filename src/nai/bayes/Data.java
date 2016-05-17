package nai.bayes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Data implements Iterable<Double> {
	private final List<Double> attrs = new ArrayList<>();
	private boolean label;
	
	public int size() {
		return attrs.size();
	}
	
	public void addAttr(double attr) {
		attrs.add(attr);
	}
	
	public void setLabel(boolean label) {
		this.label = label;
	}

	public boolean is(boolean label) {
		return this.label == label;
	}
	
	@Override
	public Iterator<Double> iterator() {
		return attrs.iterator();
	}
}
