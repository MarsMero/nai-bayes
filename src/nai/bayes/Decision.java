package nai.bayes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Decision<T> {
	private int count = 0;
	private T label = null;
	private List<Map<T,Value>> list;
	
	public Decision(int nAttrs, T label) {
		list = getList(nAttrs);
		this.label = label;
	}
	
	public void setLabel(T label) {
		this.label = label;
	}
	
	public T getLabel() {
		return label;
	}
	
	public int getCount() {
		return count;
	}
	
	public void incrCount() {
		count++;
	}
	
	public List<Map<T, Value>> getList() {
		return list;
	}
	
	private List<Map<T, Value>> getList(int nAttrs) {
		List<Map<T, Value>> list = new ArrayList<>();
		
		for(int i = 0; i < nAttrs; i++)
			list.add(new HashMap<T, Value>());
		
		return list;
	}
}
