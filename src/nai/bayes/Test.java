package nai.bayes;

public class Test {
    public static void main(String[] args) throws Exception {
        DataSetUtils.filter(Conf.DATA_FILE, Conf.FILTERED_DATA_FILE);
        DataSetUtils.split(Conf.FILTERED_DATA_FILE, Conf.TRAIN_FILE, Conf.TEST_FILE);
        IDataSet<String> trainSet = new DataSet(Conf.TRAIN_FILE);
        IDataSet<String> testSet = new DataSet(Conf.TEST_FILE);
        BayesClassifier<String> bayesClassifier = new BayesClassifier<String>(trainSet, testSet);
        bayesClassifier.test();
    }
}