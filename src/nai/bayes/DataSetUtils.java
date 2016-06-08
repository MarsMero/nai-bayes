package nai.bayes;

import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.converters.CSVSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Discretize;
import weka.filters.unsupervised.attribute.RemoveUseless;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class DataSetUtils {
	// filters from weka.filters.unsupervised.attribute
	// for example RemoveUseless
	// and Discretize

    public static void split(String dataSetFile, String trainSetFile, String testSetFile) throws IOException {
        BufferedReader dataSetReader = new BufferedReader(new FileReader(dataSetFile));
        PrintWriter trainSetWriter = new PrintWriter(new File(trainSetFile));
        PrintWriter testSetWriter = new PrintWriter(new File(testSetFile));
        List<String> lines = new LinkedList<>();
        String line;
        while ((line = dataSetReader.readLine()) != null) {
            lines.add(line);
        }
        dataSetReader.close();
        int testSize = (int)(lines.size()*0.8);
        Random random = new Random();
        for (int i = 0; i < testSize; i++) {
            trainSetWriter.println(lines.remove(random.nextInt(lines.size())));
        }
        while (!lines.isEmpty()) {
            testSetWriter.println(lines.remove(0));
        }
        trainSetWriter.flush();
        testSetWriter.flush();
        trainSetWriter.close();
        testSetWriter.close();
    }

    public static void filter(String dataSetFile, String outputSetFile) throws Exception {
        CSVLoader csvLoader = new CSVLoader();
        csvLoader.setNoHeaderRowPresent(true);
        csvLoader.setFile(new File(dataSetFile));
        DataSource source = new DataSource(csvLoader);
        Instances data = source.getDataSet();
        data.setClassIndex(data.numAttributes() - 1);
        RemoveUseless removeUseless = new RemoveUseless();
        removeUseless.setInputFormat(data);
        data = Filter.useFilter(data, removeUseless);
        Discretize discretize = new Discretize();
        discretize.setInputFormat(data);
        data = Filter.useFilter(data, discretize);
        File file = new File(outputSetFile);
        Files.deleteIfExists(file.toPath());
        CSVSaver csvSaver = new CSVSaver();
        csvSaver.setFile(file);
        csvSaver.setNoHeaderRow(true);
        csvSaver.setInstances(data);
        csvSaver.writeBatch();
    }

}
