package nai.bayes;

import java.util.List;

public interface IDataSet {
	List<Data> getTrainSet();
	List<Data> getTestSet();
}
