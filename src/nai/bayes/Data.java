package nai.bayes;

import java.util.ArrayList;
import java.util.List;

public class Data {
	public final List<Double[]> x = new ArrayList<>();
	public final List<Integer> d = new ArrayList<>();
	
	public int size() {
		return x.size();
	}
}
