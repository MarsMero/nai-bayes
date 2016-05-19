package nai.bayes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class BayesClassifier<T> {
	
	private IDataSet<T> trainSet;
	private IDataSet<T> testSet;
	private Probabilities<T> prob;
	
	public BayesClassifier() {
		trainSet = new DataSet<>();
		testSet = new DataSet<>();
		prob = new Probabilities<>(trainSet);
	}
	
	public BayesClassifier(IDataSet<T> trainSet, IDataSet<T> testSet) {
		this.trainSet = trainSet;
		this.testSet = testSet;
		prob = new Probabilities<T>(trainSet);
	}
	
	@SuppressWarnings("unchecked")
	public BayesClassifier(IDataSet<T> trainSet,IDataSet<T> testSet, String probsFile) {
		prob = Probabilities.deserialize(probsFile);
		this.trainSet = trainSet;
		this.testSet = testSet;
	}
	
	public void test() {
		int correct = 0;
		for(Data<T> data : testSet.getSet()) 
			if(data.label().equals(probLabel(data.getAttrs())))
				correct++;
		
		
		System.out.println("correct: " + correct + "/" + testSet.size());
		System.out.println("percent: " + (correct/(double)testSet.size()*100) + "%");
	}
	
	public T probLabel(List<T> attrs) {
		Map<T, Double> map = new HashMap<>();
		
		for(T lable : prob.getDescisions().keySet())
			map.put(lable, 1.0);
		
		Map<T, Double> result;
		for(int i = 0; i < attrs.size(); i++) {
			result = prob.getProbs(i, attrs.get(i));
			for(Entry<T, Double> set : result.entrySet()) {
				double val = map.get(set.getKey()).doubleValue();
				val *= set.getValue();
				map.put(set.getKey(), val);
			}
		}
		
		return getMaxProbLabel(map);
	}
	
	private T getMaxProbLabel(Map<T, Double> map) {
		return null;
	}
	
	
}
