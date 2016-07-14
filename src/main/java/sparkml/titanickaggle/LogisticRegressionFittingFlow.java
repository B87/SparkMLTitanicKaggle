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
import org.apache.spark.sql.types.StructType;
import org.apache.spark.mllib.regression.LabeledPoint;


/**  
 *  This is a simple example of how to solve the recurrent titanic Kaggle competition with the Apache Spark Ml library.
 * 	Is meant to show the different concepts of the API and fit the model using crossvalidation.
 * 
 * 	Feel free to fork, improve the code and push back, It will be warmly received.
 * 	Cheers!	
 * 	
 * 	@Author B.
 * 
 **/

public class LogisticRegressionFittingFlow implements Serializable {
	private static final long serialVersionUID = 3046778978425516932L;

	private static JavaSparkContext jsc;
	private static SQLContext sqlContext;
	private static String inputpath = "src/main/resources/train.csv";
	private static String testfilepath = "src/main/resources/test.csv";

	public LogisticRegressionFittingFlow() {
		
		SparkConf conf = new SparkConf().setAppName("SparkTitanicKaggle").setMaster("local[4]");
		LogisticRegressionFittingFlow.jsc = new JavaSparkContext(conf);
		sqlContext = new org.apache.spark.sql.SQLContext(jsc);
	}

	public static void main(String[] args) {
		LogisticRegressionFittingFlow lg = new LogisticRegressionFittingFlow();
		lg.run();
	}

	void run() {
		
		JavaRDD<String> rawInput = jsc.textFile(inputpath);
		JavaRDD<String> rawTest = jsc.textFile(testfilepath);
		JavaRDD<TitanicInputTrainingBean> TrainingBeanRDD = rawInput.map(line -> ParseLogic.parseRawTrainingInput(line));
		JavaRDD<TitanicInputTestingBean> TestingBeanRDD = rawTest.map(line -> ParseLogic.parseRawTestingInput(line));
		
		/*
		 * We'll drop here the columns that contain null values (empty strings in raw text), cause Spark ml algorithms 
		 * can't handle them. That could be a think to improve, maybe we are dropping valuable information given the small testing set we have.
		 */
		
		JavaRDD<TitanicInputTrainingBean> filteredTrainingBeanRDD = TrainingBeanRDD.filter(bean -> FilterLogic.dropNullTrainingValues(bean));
		JavaRDD<TitanicInputTestingBean> filteredTestingBeanRDD = TestingBeanRDD.filter(bean -> FilterLogic.dropNullTestingValues(bean));
		
		
		/* 
		 * Choose the features you want to use in the LogisticRegression and set them into 
		 * Labeled (training) and Unlabeled (testing) points
		 */
		
		JavaRDD<LabeledPoint> trainingRDD = filteredTrainingBeanRDD.map(bean -> ParseLogic.parseTrainBean(bean));
		JavaRDD<UnlabeledPoint> testRDD = filteredTestingBeanRDD.map(bean -> ParseLogic.parseTestBean(bean));
		
		
		/*
		 * Generate a DataFrame with the Points RDD's
		 */
		
		DataFrame trainingDF = sqlContext.createDataFrame(trainingRDD, LabeledPoint.class);
		DataFrame testDF = sqlContext.createDataFrame(testRDD, UnlabeledPoint.class);
		
		
		/*
		 * 
		 */
		
		LogisticRegression logisticR = new LogisticRegression().setMaxIter(50);
		
		Pipeline pipeL = new Pipeline().setStages(new PipelineStage[] {logisticR});

		CrossValidator crossVal = new CrossValidator().setEstimator(pipeL).setEvaluator(new BinaryClassificationEvaluator());
		
		ParamMap[] paramGrid = new ParamGridBuilder().addGrid(logisticR.regParam(), new double[]{0.1, 0.01})
			    .build();
		
		crossVal.setEstimatorParamMaps(paramGrid);
		crossVal.setNumFolds(4);
		
		CrossValidatorModel cvModel = crossVal.fit(trainingDF);
		
		/*
		 * Make the predictions for the test unlabeled points 
		 */
		
		DataFrame predictions = cvModel.transform(testDF);
		for (Row r: predictions.select("features", "probability", "prediction").collect()) {
		  System.out.println("(" + r.get(0)+ ") --> prob=" + r.get(1) + ", prediction=" + r.get(2));
		}
	}
}