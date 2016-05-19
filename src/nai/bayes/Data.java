package nai.bayes;

import java.util.ArrayList;
import java.util.List;

public class Data<T> {
	private final List<T> attrs = new ArrayList<>();
	private T label;
	
	public int size() {
		return attrs.size();
	}
	
	public void addAttr(T attr) {
		attrs.add(attr);
	}
	
	public void setLabel(T label) {
		this.label = label;
	}

	public boolean is(T label) {
		return this.label == label;
	}
	
	public List<T> getAttrs() {
		return attrs;
	}
	
	public T label() {
		return label;
	}
}
