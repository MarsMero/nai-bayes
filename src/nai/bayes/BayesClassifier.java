package nai.bayes;

import java.util.List;

public class BayesClassifier {
	
	private IDataSet trainSet;
	private IDataSet testSet;
	private Probabilities prob;
	
	private double pX = 1;
	
	public BayesClassifier() {
		trainSet = new DataSet();
		testSet = new DataSet();
		prob = new Probabilities(trainSet);
	}
	
	public BayesClassifier(IDataSet trainSet, IDataSet testSet) {
		this.trainSet = trainSet;
		this.testSet = testSet;
		prob = new Probabilities(trainSet);
	}
	
	public BayesClassifier(IDataSet trainSet,IDataSet testSet, String probsFile) {
		prob = Probabilities.deserialize(probsFile);
		this.trainSet = trainSet;
		this.testSet = testSet;
	}
	
	public void test() {
		int passed = 0;
		for(Data data : testSet.getSet()) {
			if(label(data.getAttrs()) == data.label()) {
				passed++;
			}
		}
		
		System.out.println("passed: " + passed + "/" + testSet.getSet().size());
	}
	
	public boolean isSpam(List<Double> attrs) {
		double spam = 1;
		double ham = 1;
		double sum = prob.getHamCount() + prob.getSpamCount();
		
		for(int i = 0; i < attrs.size(); i++) {
			spam *= prob.spamProbability(i, attrs.get(i));
			ham *= prob.hamProbability(i, attrs.get(i));
		}
		
		spam = (spam*prob.getSpamCount()/sum)/pX;
		ham = (ham*prob.getHamCount()/sum)/pX;
		return spam > ham;
	}
	
	public boolean label(List<Double> attrs) {
		return isSpam(attrs) ? DataSet.SPAM : DataSet.HAM;
	}
	
}
