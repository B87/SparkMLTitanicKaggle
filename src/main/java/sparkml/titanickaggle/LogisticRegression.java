package sparkml.titanickaggle;

import java.io.Serializable;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.mllib.regression.LabeledPoint;

public class LogisticRegression implements Serializable {
	private static final long serialVersionUID = 3046778978425516932L;

	private static JavaSparkContext jsc;
	private static SQLContext sqlContext;
	private static String inputpath = "src/main/resources/train.csv";
	private static String testfilepath = "src/main/resources/test.csv";

	public LogisticRegression() {
		SparkConf conf = new SparkConf().setAppName("SparkTitanicKaggle").setMaster("local[1]");
		LogisticRegression.jsc = new JavaSparkContext(conf);
		sqlContext = new org.apache.spark.sql.SQLContext(jsc);
	}

	public static void main(String[] args) {
		LogisticRegression lg = new LogisticRegression();
		lg.run();
	}

	void run() {
		
		// Read input and test files and parse them into Beans
		// This separation is made to make the feature changes easier		
		JavaRDD<String> rawInput = jsc.textFile(inputpath);
		JavaRDD<String> rawTest = jsc.textFile(testfilepath);
		JavaRDD<TitanicInputBean> titanicTrainBeanRDD = rawInput.map(line -> ParseLogic.parseRawInput(line));
		JavaRDD<TitanicInputBean> titanicTestBeanRDD = rawTest.map(line -> ParseLogic.parseRawInput(line));
		
		
		// Choose the features you want to use in the LogisticRegression and set them into 
		// Labeled (training) and Unlabeled (testing) points
		JavaRDD<LabeledPoint> trainingRDD = titanicTrainBeanRDD.map(bean -> ParseLogic.parseTrainBean(bean));
		JavaRDD<UnlabeledPoint> testRDD = titanicTestBeanRDD.map(bean -> ParseLogic.parseTestBean(bean));
		
		
		// Generate a DataFrame with the Points RDD's
		DataFrame trainingDF = sqlContext.createDataFrame(titanicTrainBeanRDD, LabeledPoint.class);
		DataFrame testDF = sqlContext.createDataFrame(titanicTestBeanRDD, UnlabeledPoint.class);
		
		//trainingDF.show();
		//TestDF.show();
	}
}