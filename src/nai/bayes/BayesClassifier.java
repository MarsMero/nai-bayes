package nai.bayes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class BayesClassifier<T> {
	
	private IDataSet<T> trainSet;
	private IDataSet<T> testSet;
	private Probabilities<T> prob;
	
	public BayesClassifier(IDataSet<T> trainSet, IDataSet<T> testSet) {
		if(trainSet == null || testSet == null)
			throw new NullPointerException();
		if(testSet.nAttrs() != trainSet.nAttrs())
			throw new IllegalArgumentException("Test DataSet's number of arguments " +
					"is different from Train DataSet.");
		this.trainSet = trainSet;
		this.testSet = testSet;
		prob = new Probabilities<>(this.trainSet);
	}
	
	public BayesClassifier(IDataSet<T> trainSet,IDataSet<T> testSet, String probsFile) {
		if(trainSet == null || testSet == null)
			throw new NullPointerException();
		if(testSet.nAttrs() != trainSet.nAttrs())
			throw new IllegalArgumentException("Test DataSet's number of arguments " +
					"is different from Train DataSet.");
		prob = Probabilities.deserialize(probsFile);
		this.trainSet = trainSet;
		this.testSet = testSet;
	}
	
	public void test() {
		int correct = 0;
		for(Data<T> data : testSet.getSet()) 
			if(data.label().equals(classify(data.getAttrs())))
				correct++;
		
		
		System.out.println("correct: " + correct + "/" + testSet.size());
		System.out.println("percent: " + (correct/(double)testSet.size()*100) + "%");
	}
	
	public T classify(List<T> attrs) {
		Map<T, Double> map = new HashMap<>();
		
		for(T lable : prob.getDescisions().keySet())
			map.put(lable, 1.0);
		
		Map<T, Double> result;
		for(int i = 0; i < attrs.size(); i++) {
			result = prob.getProbabilities(i, attrs.get(i));
			for(Entry<T, Double> set : result.entrySet()) {
				double val = map.get(set.getKey()).doubleValue();
				val *= set.getValue();
				map.put(set.getKey(), val);
			}
		}

		for(Entry<T, Double> set : map.entrySet()) {
			double x = set.getValue();
			double y = prob.getDecisionProbability(set.getKey());
			double val = set.getValue()*prob.getDecisionProbability(set.getKey());
			map.put(set.getKey(), val);
		}
		
		return getMaxProbLabel(map);
	}
	
	private T getMaxProbLabel(Map<T, Double> map) {
		double maxVal = 0;
		T maxLab = null;
		
		for(Entry<T, Double> set : map.entrySet()) {
			if(set.getValue() > maxVal) {
				maxVal = set.getValue();
				maxLab = set.getKey();
			}
		}
		
		return maxLab;
	}
	
	
}
