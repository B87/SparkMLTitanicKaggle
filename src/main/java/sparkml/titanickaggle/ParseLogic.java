package sparkml.titanickaggle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.sql.Row;

public class ParseLogic {

	private static String pattern = "^(.*?),\".*\",(.*)";

	/*
	 * Parse logic to transform the raw input into a bean, as we can see the
	 * names are dropped in this step. They where complicated to properly format
	 * to use and irrelevant for the predictions.
	 */
	public static TitanicInputTrainingBean parseRawTrainingInput(String line) throws Exception {
		Pattern r = Pattern.compile(pattern);
		TitanicInputTrainingBean titanicBean = new TitanicInputTrainingBean();
		Matcher m = r.matcher(line);
		if (m.matches()) {
			String lineParsed = m.group(1).concat("," + m.group(2));
			String[] fields = lineParsed.split(",");
			titanicBean.setPassengerId(toInteger(fields[0]));
			titanicBean.setSurvived(toDouble(fields[1]));
			titanicBean.setpClass(toInteger(fields[2]));
			titanicBean.setSex(fields[3]);
			titanicBean.setAge(toDouble(fields[4]));
			titanicBean.setSibsSp(toInteger(fields[5]));
			titanicBean.setpArch(toInteger(fields[6]));
			titanicBean.setTicket(fields[7]);
			titanicBean.setFare(toDouble(fields[8]));
			titanicBean.setCabin(fields[9]);
			//titanicBean.setEmbarked(fields[10]);

		}
		return titanicBean;
	}
	
	public static titanicInputToPredictBean parseRawTestingInput(String line) throws Exception {
		Pattern r = Pattern.compile(pattern);
		titanicInputToPredictBean titanicBean = new titanicInputToPredictBean();
		Matcher m = r.matcher(line);
		if (m.matches()) {
			String lineParsed = m.group(1).concat("," + m.group(2));
			String[] fields = lineParsed.split(",");
			titanicBean.setPassengerId(toInteger(fields[0]));
			titanicBean.setpClass(toInteger(fields[1]));
			titanicBean.setSex(fields[2]);
			titanicBean.setAge(toDouble(fields[3]));
			titanicBean.setSibsSp(toInteger(fields[4]));
			titanicBean.setpArch(toInteger(fields[5]));
			titanicBean.setTicket(fields[6]);
			titanicBean.setFare(toDouble(fields[7]));
			titanicBean.setCabin(fields[8]);
			//titanicBean.setEmbarked(fields[9]);

		}
		return titanicBean;
	}
	

	public static Integer toInteger(String myString) {
		if (StringUtils.isBlank(myString)) {
			return null;
		}
		return Integer.parseInt(myString);
	}

	public static Double toDouble(String myString) {
		if (StringUtils.isBlank(myString)) {
			return null;
		}
		return Double.parseDouble(myString);
	}

	/*  For now, we'll take the Integer values that come with the training set in order to make
	 *  the predictions, in the future we'll classify String inputs like the Sex field into Doubles
	 *  (i.e : 0.0 for male and 1.0 for female) 
	 */
	public static LabeledPoint parseTrainBean(TitanicInputTrainingBean bean) {
		LabeledPoint point = new LabeledPoint(bean.getSurvived(), Vectors.dense(bean.getpClass(),
																				bean.getAge(),
																				bean.getSibsSp(),
																				bean.getpArch(),
																				bean.getFare()));
		return point;

	}

	/*
	 * 
	 */
	public static UnlabeledPoint parseTestBean(titanicInputToPredictBean bean) {
		UnlabeledPoint point = new UnlabeledPoint(Vectors.dense(bean.getpClass(),
																bean.getAge(),
																bean.getSibsSp(),
																bean.getpArch(),
																bean.getFare()));
		return point;
	}
	
//	public static Row parseTrainingRow(Row Inrow){
//		row.get(arg0);
//		
//		return
//		
//	}

}