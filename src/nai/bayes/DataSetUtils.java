package nai.bayes;

import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.converters.CSVSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Discretize;
import weka.filters.unsupervised.attribute.PKIDiscretize;
import weka.filters.unsupervised.attribute.PrincipalComponents;
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

        //data = removeUseless(data);
        data = pkiDiscretize(data);

        File file = new File(outputSetFile);
        Files.deleteIfExists(file.toPath());
        CSVSaver csvSaver = new CSVSaver();
        csvSaver.setFile(file);
        csvSaver.setNoHeaderRow(true);
        csvSaver.setInstances(data);
        csvSaver.writeBatch();
    }

    private static Instances removeUseless(Instances data) throws Exception {
        RemoveUseless removeUseless = new RemoveUseless();
        removeUseless.setInputFormat(data);
        return Filter.useFilter(data, removeUseless);
    }

    private static Instances discretize(Instances data) throws Exception {
        Discretize discretize = new Discretize();
        discretize.setUseEqualFrequency(true);
        discretize.setBins(2);
        discretize.setInputFormat(data);
        return Filter.useFilter(data, discretize);
    }

    private static Instances pkiDiscretize(Instances data) throws  Exception {
        PKIDiscretize pkiDiscretize = new PKIDiscretize();
        pkiDiscretize.setUseEqualFrequency(true);
        pkiDiscretize.setInputFormat(data);
        return Filter.useFilter(data, pkiDiscretize);
    }
}
