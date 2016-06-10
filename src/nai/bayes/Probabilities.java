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
import java.util.Map.Entry;
import java.util.Set;

public class Probabilities<L> implements Serializable {
	private static final long serialVersionUID = 7584990748084327783L;
	
	private List<Integer> variety;
	private Map<L, Decision<L>> decisions;
	private int dataSetSize;
	
	public Probabilities(IDataSet<L> set) {
		dataSetSize = set.size();
		decisions = getDecisions(set);
		variety = getVariety(set);
		countValues(set);
		calcProbabilities();
	}
	
	public Map<L, Double> getProbabilities(int attrIndex, L value) {
		Map<L, Double> map = new HashMap<>();
		
		for(Entry<L, Decision<L>> set : decisions.entrySet()) 
			map.put(set.getKey(), getProb(attrIndex, value, set.getValue()));
		
		
		return map;
	}
	
	public double getDecisionProbability(L label) {
		return decisions.get(label).getCount()/(double)dataSetSize;
	}
	
	public Map<L, Decision<L>> getDescisions() {
		return decisions;
	}
	
	private double getProb(int attrIndex, L value, Decision<L> dec) {
		Map<L, Value> map = dec.getList().get(attrIndex);

		if(map.containsKey(value)) 
			return map.get(value).getProbability();
		else
			return calculateProbability(0, dec.getCount(), variety.get(attrIndex));
	}
	
	private Map<L, Decision<L>> getDecisions(IDataSet<L> dataSet) {
		Map<L, Decision<L>> map = new HashMap<>();
		Set<L> set = new HashSet<>();
		
		for(Data<L> d : dataSet.getSet()) {
			if(!set.contains(d.label())) {
				set.add(d.label());
				map.put(d.label(), new Decision<L>(dataSet.nAttrs(), d.label()));
			}
			map.get(d.label()).incrCount();
		}
		
		return map;
	}
	
	private List<Integer> getVariety(IDataSet<L> dataSet) {
		List<Integer> var = new ArrayList<>();
		
		Set<L> set = new HashSet<>();
		
		for(int i = 0; i < dataSet.nAttrs(); i++) {
			set.clear();
			
			for(int j = 0; j < dataSet.size(); j++) 
				set.add(dataSet.getSet().get(j).getAttrs().get(i));
			var.add(set.size());
		}
		
		return var;
	}
	
	private void countValues(IDataSet<L> dataSet) {
		for(Data<L> data : dataSet.getSet()) 
			for(Entry<L, Decision<L>> set : decisions.entrySet()) 
				if(data.label().equals(set.getKey())) 
					addUpValues(data, set.getValue());
	}
	
	private void addUpValues(Data<L> data, Decision<L> dec) {
		Iterator<L> idata = data.getAttrs().iterator();
		Iterator<Map<L, Value>> idec = dec.getList().iterator();
		L attr;
		Map<L, Value> map;
		
		while(idata.hasNext()) {
			attr = idata.next();
			map = idec.next();
			if(map.containsKey(attr))
				map.get(attr).incrCount();
			else
				map.put(attr, new Value());
		}
		
	}
	
	private void calcProbabilities() {
		for(Decision<L> dec : decisions.values()) {
			setProbabilities(dec);
		}
	}
	
	private void setProbabilities(Decision<L> dec) {
		Iterator<Map<L, Value>> idec = dec.getList().iterator();
		Iterator<Integer> ivar = variety.iterator();
		Map<L, Value> map;
		Integer var;
		
		while(idec.hasNext()) {
			map = idec.next();
			var = ivar.next();
			for(Value val : map.values())
				val.setProbability(calculateProbability(val.getCount(), dec.getCount(), var));
		}
		
	}

	private double calculateProbability(double valCount, double nValues, double variety) {
		return (valCount + 1.0)/(nValues + variety);
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
