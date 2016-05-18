package nai.bayes;

import java.util.List;

public class BayesClassifier {
	
	private IDataSet ds;
	private Probabilities prob;
	
	private double pX = 1;
	
	public BayesClassifier() {
		ds = new DataSet();
		prob = new Probabilities(ds.getTrainSet());
	}
	
	public BayesClassifier(IDataSet dataSet) {
		ds = dataSet;
		prob = new Probabilities(ds.getTrainSet());
	}
	
	public BayesClassifier(IDataSet dataSet, String probsFile) {
		prob = Probabilities.deserialize(probsFile);
		ds = dataSet;
	}
	
	public void test() {
		int passed = 0;
		for(Data data : ds.getTestSet()) {
			if(label(data.getAttrs()) == data.label()) {
				passed++;
			}
		}
		
		System.out.println("passed: " + passed + "/" + ds.getTestSet().size());
	}
	
	public boolean isSpam(List<Double> attrs) {
		double spam = 0;
		double ham = 0;
		double sum = prob.getHamCount() + prob.getSpamCount();
		
		for(int i = 0; i < attrs.size(); i++) {
			spam += prob.spamProbability(i, attrs.get(i));
			ham += prob.hamProbability(i, attrs.get(i));
		}
		
		spam = (spam*prob.getSpamCount()/sum)/pX;
		ham = (ham*prob.getHamCount()/sum)/pX;
		return spam > ham;
	}
	
	public boolean label(List<Double> attrs) {
		return isSpam(attrs) ? DataSet.SPAM : DataSet.HAM;
	}
	
}
