package nai.bayes;

import java.util.List;

public class DataSet implements IDataSet {
	public static final boolean SPAM = true;
	public static final boolean HAM = false;
	
	public DataSet() {
		
	}
	
	public DataSet(String dataSetFile) {
		
	}
	
	@Override
	public List<Data> getSet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int nAttrs() {
		// TODO Liczba atrybutow bez okladki
		return 0;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}
}
