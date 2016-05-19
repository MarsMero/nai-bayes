package nai.bayes;

import java.util.List;

public interface IDataSet<T> {
	List<Data<T>> getSet();
	int nAttrs();
	int size();
}
