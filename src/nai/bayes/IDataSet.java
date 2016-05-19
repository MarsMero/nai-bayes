package nai.bayes;

import java.util.List;

public interface IDataSet {
	List<Data> getSet();
	int nAttrs();
	int size();
}
