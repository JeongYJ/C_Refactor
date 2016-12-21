package c_refactor.codes;


import java.io.IOException;

public class RMV extends TransformLoopStructure{
	public RMV(String sourceCode,int inputcount) throws IOException {
		super(sourceCode, inputcount);
		// TODO Auto-generated constructor stub
	}

	public void check_manyVar(){
		String[] result = forLoopStructure.substring(forLoopStructure.indexOf(')')+1).split("\\)|\\(|\\[|\\]|;");
		String[] noSpaceresult = forLoopStructure.substring(forLoopStructure.indexOf(')')+1).split("\\)|\\(|\\[|\\]|;");
		String forOp = "";
		for(int i = 0 ; i < result.length; i++)
			noSpaceresult[i] = noSpaceresult[i].replaceAll("\\p{Space}", "");
		
		for(int i = 0; i < mainFor.forOperate.size(); i++)
		{	
			String var = "";
			forOp = mainFor.forOperate.get(i);
			for(int j = 0; j < mainFor.forVariable.size(); j++)
				if(forOp.matches(".*"+mainFor.forVariable.get(j)+".*"))
				{
					var = mainFor.forVariable.get(j);		// var : i1
					break;
				}
			forOp = forOp.replaceAll(var, "");				// forOp : += 1
			forOp = forOp.replaceAll("\\p{Space}", "");		// forOp : +=1
			
			for(int j = 0 ; j < result.length; j++){
				String checkVar = "";
				if(isAssignState(noSpaceresult[i]))			// 증감 변수에 대입하는 문장이 있는 지 검사
				{
					for(int k = 0 ; k < oldVar.size(); k++)		// 제거할 증감 변수에서
					{
						if(noSpaceresult[i].matches(oldVar.get(k)+".*"))	// 대입문에 증감면수가 잇을경우
							oldVar.remove(k);								// 제거할 증감변수에서 제거한다.
					}
				}
				else if(noSpaceresult[j].contains(forOp)){		// ++를 포함한다면
					checkVar = noSpaceresult[j].substring(0,noSpaceresult[j].indexOf(forOp));	// checkVar : i2
					if(localvarValue.containsKey(checkVar))										// 지역 변수 중에 i2가 포함되어있는지
						if(localvarValue.get(checkVar).equals(localvarValue.get(var))){			// i2의 값과 i1의 값이 같은지
							oldVar.add(checkVar);					// oldVar : i2 (제거해야할 증감변수)
							newVar = var;							// newVar : i1 (통일할 증감변수)
						}
				}
			}
		}
		
		/* forLoopStructure문을 리팩토링한 for문으로 바꾼다.*/
		String forStr = forLoopStructure.substring(forLoopStructure.indexOf('(')+1);	// forStr :  i = 0 ; i < SIZE; i++ ) {
		forStr = forStr.substring(0,forStr.indexOf(')'));								// forStr : i = 0 ; i < SIZE; i++ 
		
		result = forStr.split(";");			// i = 0 / i < SIZE / i++
		forStr = "	for (";
		
		// 필요없는 증감변수를 for문 변수에서 제거한다.
		for(int i = 0; i < oldVar.size(); i++){
			for(int j = 0 ; j < mainFor.forVariable.size(); j++)
				if(mainFor.forVariable.get(j).equals(oldVar.get(i)))
					mainFor.forVariable.remove(j);
		}
		
		// for문의 1.초기화 부분	int i = 0
		for(int k = 0 ; k < 3; k++){
			String[] commaresult = result[k].split(",");
			commaresult[0] = commaresult[0].substring(commaresult[0].indexOf('(')+1);
			for(int j = 0 ; j < commaresult.length; j++){
				for(int i = 0; i < mainFor.forVariable.size(); i++){
					if(commaresult[j].matches(".*"+mainFor.forVariable.get(i)+".*"))
						forStr += " "+commaresult[j]+",";
				}
			}
			forStr = forStr.substring(0, forStr.length()-1);
			if(k < 2)
				forStr += ";";
		}
		forStr += ")";
		
		String forState = forLoopStructure.substring(forLoopStructure.indexOf(")")+1);
		for(int i = 0 ; i < oldVar.size(); i++)
		{
			String Ex = forOp;
			String temp = "";
			
			for (int j = 0; j < Ex.length(); j++) {
				if(Ex.charAt(j)=='+'||Ex.charAt(j)=='-'||Ex.charAt(j)=='=')
				{
					temp += "\\"+Ex.charAt(j)+"";
				}
				else{
					temp += Ex.charAt(j);
				}
			}
			forState = forState.replaceAll(oldVar.get(i)+temp, newVar);
		}
		
		forLoopStructure = forStr + forState;
	}
}
