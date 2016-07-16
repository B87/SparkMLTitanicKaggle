package sparkml.titanickaggle.common;

import sparkml.titanickaggle.bean.TitanicToPredictBean;
import sparkml.titanickaggle.bean.TitanicTrainingBean;

public class FilterLogic {

	public static boolean dropNullTrainingValues(TitanicTrainingBean bean){
		
		try{
			  if (bean.getPassengerId()!=null && bean.getpClass()!=null && bean.getAge()!=null
					  && bean.getSibsSp() !=null&& bean.getpArch() !=null && bean.getFare()!=null) {
			     return true;
			  }
			} catch (Exception e){
				
			}
		
		return false;
	}
	
	public static boolean dropNullTestingValues(TitanicToPredictBean bean){
		
		try{
			  if (bean.getPassengerId()!=null && bean.getpClass()!=null && bean.getAge()!=null
					  && bean.getSibsSp() !=null&& bean.getpArch() !=null && bean.getFare()!=null) {
			     return true;
			  }
			} catch (Exception e){
				
			}
		
		return false;
	}
}
