package nai.bayes;

public class Value {
	
	private double probability;
	private int count = 1;

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	public int getCount() {
		return count;
	}

	public void incrCount() {
		count++;
	}
	
}
