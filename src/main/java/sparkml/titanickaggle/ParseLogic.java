package sparkml.titanickaggle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;

public class ParseLogic {

	private static String pattern = "^(.*?),\".*\",(.*)";

	/*
	 * Parse logic to transform the raw input into a bean, as we can see the
	 * names are dropped in this step. They where complicated to properly format
	 * to use and irrelevant for the predictions.
	 */
	public static TitanicInputBean parseRawInput(String line) throws Exception {
		Pattern r = Pattern.compile(pattern);
		TitanicInputBean titanicBean = new TitanicInputBean();
		Matcher m = r.matcher(line);
		if (m.matches()) {
			String lineParsed = m.group(1).concat("," + m.group(2));
			String[] fields = lineParsed.split(",");
			titanicBean.setPassengerId(toInteger(fields[0]));
			titanicBean.setSurvived(toDouble(fields[1]));
			titanicBean.setpClass(toInteger(fields[2]));
			titanicBean.setSex(fields[3]);
			titanicBean.setAge(toInteger(fields[4]));
			titanicBean.setSibsSp(toInteger(fields[5]));
			titanicBean.setpArch(toInteger(fields[6]));
			titanicBean.setTicket(fields[7]);
			titanicBean.setFare(toDouble(fields[8]));
			titanicBean.setCabin(fields[9]);
			titanicBean.setEmbarked(fields[10]);

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

	/*
	 * 
	 */
	public static LabeledPoint parseTrainBean(TitanicInputBean bean) {
		LabeledPoint point = new LabeledPoint(bean.getSurvived(), Vectors.dense(0.0, 0.0));
		return point;

	}

	/*
	 * 
	 */
	public static UnlabeledPoint parseTestBean(TitanicInputBean bean) {
		UnlabeledPoint point = new UnlabeledPoint();
		return point;
	}

}