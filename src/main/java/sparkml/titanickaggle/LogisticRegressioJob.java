package sparkml.titanickaggle;

import java.io.Serializable;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator;
import org.apache.spark.ml.param.ParamMap;
import org.apache.spark.ml.tuning.CrossValidator;
import org.apache.spark.ml.tuning.CrossValidatorModel;
import org.apache.spark.ml.tuning.ParamGridBuilder;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import scala.Tuple2;
import sparkml.titanickaggle.bean.TitanicToPredictBean;
import sparkml.titanickaggle.bean.TitanicTrainingBean;
import sparkml.titanickaggle.bean.UnlabeledPoint;
import sparkml.titanickaggle.common.FilterLogic;
import sparkml.titanickaggle.common.ParseLogic;

import org.apache.spark.mllib.evaluation.MulticlassMetrics;
import org.apache.spark.mllib.regression.LabeledPoint;


/**  
 *  This is a simple example of how to solve the recurrent Titanic Kaggle competition using a LogisticRegression
 *  from Apache Spark Java API using the cross validation methodology to fit our algorithm.
 * 
 * 	Feel free to fork, improve the code and push back, It will be warmly received.
 * 	Cheers!	
 * 	
 * 	@Author B87
 * 
 **/

public class LogisticRegressioJob implements Serializable {
	private static final long serialVersionUID = 3046778978425516932L;

	private static JavaSparkContext jsc;
	private static SQLContext sqlsc;
	private static String inputpath = "src/main/resources/train.csv";
	private static String testfilepath = "src/main/resources/test.csv";

	public LogisticRegressioJob() {
		
		SparkConf conf = new SparkConf().setAppName("SparkTitanicKaggle").setMaster("local[4]");
		LogisticRegressioJob.jsc = new JavaSparkContext(conf);
		sqlsc = new org.apache.spark.sql.SQLContext(jsc);
	}

	public static void main(String[] args) {
		LogisticRegressioJob lg = new LogisticRegressioJob();
		lg.run();
	}

	void run() {
		
		JavaRDD<String> rawInput = jsc.textFile(inputpath);
		JavaRDD<String> rawTest = jsc.textFile(testfilepath);
		JavaRDD<TitanicTrainingBean> TrainingBeanRDD = rawInput.map(line -> ParseLogic.parseRawTrainingInput(line));
		JavaRDD<TitanicToPredictBean> toPredictBeanRDD = rawTest.map(line -> ParseLogic.parseRawTestingInput(line));
		
		/*
		 *  We'll drop here the columns that contain null values (empty strings in raw text), cause Spark ml algorithms 
		 *  can't handle them. That could be a think to improve, maybe we are dropping valuable information given the small testing set we have.
		 */
		
		JavaRDD<TitanicTrainingBean> filteredTrainingBeanRDD = TrainingBeanRDD.filter(bean -> FilterLogic.dropNullTrainingValues(bean));
		JavaRDD<TitanicToPredictBean> filteredTOpREDICTBeanRDD = toPredictBeanRDD.filter(bean -> FilterLogic.dropNullTestingValues(bean));
		
		
		/** 
		 *  Choose the features you want to use in the LogisticRegression and set them into Labeled (training and testing set) and Unlabeled (points to predict). 
		 *  We'll split our test into training and testing set as well.
		 *  
		 *  @link ParseLogic
		 */
		
		JavaRDD<LabeledPoint>[] splits = filteredTrainingBeanRDD.map(bean -> ParseLogic.parseTrainBean(bean)).randomSplit(new double[] {0.7,0.3}, 125L);
		JavaRDD<LabeledPoint> trainingRDD = splits[0];
		JavaRDD<LabeledPoint> testRDD = splits[1];
		JavaRDD<UnlabeledPoint> toPredictRDD = filteredTOpREDICTBeanRDD.map(bean -> ParseLogic.parseTestBean(bean));
		
		
		/*
		 *  Generate a DataFrame with the Points RDD's
		 */
		
		DataFrame trainingDF = sqlsc.createDataFrame(trainingRDD, LabeledPoint.class).cache();
		DataFrame testingDF = sqlsc.createDataFrame(testRDD, LabeledPoint.class);
		DataFrame ToPredictDF = sqlsc.createDataFrame(toPredictRDD, UnlabeledPoint.class).select("vector").toDF("features");
		
		
		/* 
		 *  Train a Logistic Regression using the cross validation method, with 3 folds. And make predictions with the testing data set.
		 * 
		 */
		
		LogisticRegression logisticR = new LogisticRegression().setMaxIter(50);
		
		CrossValidator crossVal = new CrossValidator().setEstimator(logisticR).setEvaluator(new BinaryClassificationEvaluator());
		
		ParamMap[] paramGrid = new ParamGridBuilder().addGrid(logisticR.regParam(), new double[]{0.1, 0.01}).build();
		
		crossVal.setEstimatorParamMaps(paramGrid);
		crossVal.setNumFolds(3);
		
		CrossValidatorModel cvModel = crossVal.fit(trainingDF);
		trainingDF.unpersist();
		
		DataFrame testingResults = cvModel.transform(testingDF);
		
		/*
		 *  Make the predictions for the test unlabeled points and show the model accuracy defined by the testing data set.
		 * 
		 */
		
		DataFrame predictions = cvModel.transform(ToPredictDF);
		for (Row r: predictions.select("features", "probability", "prediction").take(20)) {
		  System.out.println("(" + r.get(0)+ ") --> prob=" + r.get(1) + ", prediction=" + r.get(2));
		}
		
		System.out.println("The expected accuracy of the predictions is: " + cvModel.getEvaluator().evaluate(testingResults, cvModel.paramMap()));
	}
}