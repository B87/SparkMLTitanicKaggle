package sparkml.titanickaggle;


public class FilterLogic {

	public static boolean dropNullTrainingValues(TitanicInputTrainingBean bean){
		
		try{
			  if (bean.getPassengerId()!=null && bean.getpClass()!=null && bean.getAge()!=null
					  && bean.getSibsSp() !=null&& bean.getpArch() !=null && bean.getFare()!=null) {
			     return true;
			  }
			} catch (Exception e){
				
			}
		
		return false;
	}
	
	public static boolean dropNullTestingValues(titanicInputToPredictBean bean){
		
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
