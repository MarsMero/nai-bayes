package nai.bayes;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class DataSet implements IDataSet<String> {

	private int attrs;
	private final List<Data<String>> list = new ArrayList<>();
	private final List<Data<String>> readonlyref = Collections.unmodifiableList(list);
	
	public DataSet() throws IOException {
		this(Conf.DATA_FILE);
	}
	
	public DataSet(String dataSetFile) throws IOException {
		BufferedReader dataSetReader = new BufferedReader(new FileReader(dataSetFile));
		String line;
		String[] array = null;
		while((line = dataSetReader.readLine()) != null) {
			array = line.split(",");
			if(array.length > 1) {
				Data<String> data = new Data<>();
				data.setLabel(array[array.length-1]);
				for (int i = 0; i < array.length-1; i++) {
					data.addAttr(array[i]);
				}
				list.add(data);
			}
		}
		dataSetReader.close();
		if(array == null || array.length == 0)
			attrs = 0;
		else
			attrs = array.length - 1;
	}
	
	@Override
	public List<Data<String>> getSet() {
		return readonlyref;
	}

	@Override
	public int nAttrs() {
		return attrs;
	}

	@Override
	public int size() {
		return list.size();
	}
}
