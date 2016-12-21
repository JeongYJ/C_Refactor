package c_refactor.codes;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class forStructure {
	public List<String> forVariable = new ArrayList<String>();
	public List<String> forCondition= new ArrayList<String>();
	public List<String> forOperate= new ArrayList<String>();
	public int layer = 0;
	public boolean useforVar = false;
	public String forState = "";
	public forStructure(){
		
	}
	public String return_forCondition () {
		if(forCondition.get(0).contains("=")&&!forCondition.get(0).contains("=="))
			return forCondition.get(0).substring(forCondition.get(0).indexOf("=")+1);
		else if(forCondition.get(0).contains("=="))
			return forCondition.get(0).substring(forCondition.get(0).indexOf("==")+2);
		else if(forCondition.get(0).contains("<"))
			return forCondition.get(0).substring(forCondition.get(0).indexOf("<")+1);
		else if(forCondition.get(0).contains(">"))
			return forCondition.get(0).substring(forCondition.get(0).indexOf(">")+1);
		else
			return "";
	}
	public void check_forLoopVar(String forStr, TransformLoopStructure main ) throws IOException{
		String tempStr="";											// forStr -> for(i = 0 ; i <SIZE ; i++){
		tempStr = forStr.substring(forStr.indexOf('(')+1);			// tempStr -> i = 0 ; i <SIZE ; i++){
		tempStr = tempStr.substring(0,tempStr.indexOf(')'));		// tempStr -> i = 0 ; i <SIZE ; i++
		//System.out.println("for"+tempStr);
		tempStr = tempStr.replaceAll("\\p{Space}", "");
		String[] result = tempStr.split(";");			// i = 0 / i < SIZE / i++
		
		// for문의 1.초기화 부분	int i = 0
		String[] commaresult = result[0].split(",");
		if(commaresult.length > 0){
			for(int j = 0; j < commaresult.length; j++){                                                               
				
				String[] tempVar= commaresult[j].split("=");		// tempVar[0] : i(변수) , tempVar[1] : 0(값)
				forVariable.add(tempVar[0]);	
				if(main.localvarValue.containsKey(tempVar[0])){
					main.localvarValue.put(tempVar[0], tempVar[1]); 
				}
			}
		}
		
		// for문의 2.조건식검사 부분    i < SIZE
		commaresult = result[1].split(",");
		if(commaresult.length > 0)
			for(int j = 0; j < commaresult.length; j++){
				forCondition.add(commaresult[j]);
			}
		
		// for문의 3.연산하는 부분	  i++
		commaresult = result[2].split(",");
		if(commaresult.length > 0)
			for(int j = 0; j < commaresult.length; j++){
				forOperate.add(commaresult[j]);
			}    
		
		// for문 변수, 조건식, 연산 조회
//		for(Object object : forVariable){
//			String element = (String) object;
//			System.out.println("forVariable"+element);
//		}
//		for(Object object : forCondition){
//			String element = (String) object;
//			System.out.println("forCondition"+element);
//		}
//		for(Object object : forOperate){
//			String element = (String) object;
//			System.out.println("forOperate"+element);
//		}
	}
}
