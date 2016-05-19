package nai.bayes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Probabilities implements Serializable {
	private static final long serialVersionUID = 7584990748084327783L;
	
	private final List<Map<Double, ClassValue>> spam = new ArrayList<>();
	private final List<Map<Double, ClassValue>> ham = new ArrayList<>();	
	
	private int spamCount;
	private int hamCount;
	
	private List<Integer> variety;
	
	public Probabilities(IDataSet set) {
		init(set.nAttrs());
		variety = getVariety(set);
		setSpamHamCount(set.getSet());
		calculatePosibilities(set.getSet());
	}
	
	public int getSpamCount() {
		return spamCount;
	}
	
	public int getHamCount() {
		return hamCount;
	}
	
	public double spamProbability(int attr,double value) {
		return attrProbability(attr, value, spam, spamCount);
	}
	
	public double hamProbability(int attr, double value) {
		return attrProbability(attr, value, ham, hamCount);
	}
	
	private List<Integer> getVariety(IDataSet dataSet) {
		List<Integer> var = new ArrayList<>();
		
		Set<Double> set = new HashSet<>();
		
		for(int i = 0; i < dataSet.nAttrs(); i++) {
			set.clear();
			
			for(int j = 0; j < dataSet.size(); j++) 
				set.add(dataSet.getSet().get(j).getAttrs().get(i));
			var.add(set.size());
		}
		
		return var;
	}
	
	private double attrProbability(int attrIndex, double value, List<Map<Double, ClassValue>> map, int count) {
		ClassValue cv;
		
		if((cv = map.get(attrIndex).get(value)) != null) 
			return cv.getProbability();
		else {
			return 1.0/(double)(count + variety.get(attrIndex));
		}
	}
	
	private void init(int size) {
		for(int i = 0; i < size; i++) {
			spam.add(new HashMap<Double, ClassValue>());
			ham.add(new HashMap<Double, ClassValue>());
		}
	}
	
	private void setSpamHamCount(List<Data> set) {
		int spam = 0;
		for(Data data : set) 
			if(data.is(DataSet.SPAM))
				spam++;
		spamCount = spam;
		hamCount = set.size() - spam;
	}
	
	private void calculatePosibilities(List<Data> set) {
		forEachDataCountValues(set);
		forEachAttrCalculateProb();
	}
	
	private void forEachDataCountValues(List<Data> set) {
		for(Data data : set) {
			if(data.is(DataSet.SPAM)) 
				countValues(data, spam);
			else
				countValues(data, ham);
		}
	}
	
	private void countValues(Data data, List<Map<Double, ClassValue>> list) {
		ClassValue bc;
		Iterator<Map<Double, ClassValue>> il = list.iterator();
		Map<Double, ClassValue> map;
		
		for(double d : data) {
			map = il.next();
			if((bc = map.get(d)) != null) 
				bc.add();
			else
				map.put(d, new ClassValue());
		}
	}
	
	private void forEachAttrCalculateProb() {
		setProb(spam, spamCount);
		setProb(ham, hamCount);
	}
	
	private void setProb(List<Map<Double, ClassValue>> list, int count) {
		for(Map<Double, ClassValue> map : list) {
			int i = 0;
			for(ClassValue value : map.values()) {
				value.setProbability((value.getCount() + 1.0)/(double)(count + variety.get(i)));
				i++;
			}
		}
	}
	
	public static void serialize(Probabilities probs) {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("data/probs.data"))) {
			out.writeObject(probs);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Probabilities deserialize(String probsFile) {
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(probsFile))) {
			return (Probabilities) in.readObject();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
