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
				if(isAssignState(noSpaceresult[i]))			// ���� ������ �����ϴ� ������ �ִ� �� �˻�
				{
					for(int k = 0 ; k < oldVar.size(); k++)		// ������ ���� ��������
					{
						if(noSpaceresult[i].matches(oldVar.get(k)+".*"))	// ���Թ��� ��������� �������
							oldVar.remove(k);								// ������ ������������ �����Ѵ�.
					}
				}
				else if(noSpaceresult[j].contains(forOp)){		// ++�� �����Ѵٸ�
					checkVar = noSpaceresult[j].substring(0,noSpaceresult[j].indexOf(forOp));	// checkVar : i2
					if(localvarValue.containsKey(checkVar))										// ���� ���� �߿� i2�� ���ԵǾ��ִ���
						if(localvarValue.get(checkVar).equals(localvarValue.get(var))){			// i2�� ���� i1�� ���� ������
							oldVar.add(checkVar);					// oldVar : i2 (�����ؾ��� ��������)
							newVar = var;							// newVar : i1 (������ ��������)
						}
				}
			}
		}
		
		/* forLoopStructure���� �����丵�� for������ �ٲ۴�.*/
		String forStr = forLoopStructure.substring(forLoopStructure.indexOf('(')+1);	// forStr :  i = 0 ; i < SIZE; i++ ) {
		forStr = forStr.substring(0,forStr.indexOf(')'));								// forStr : i = 0 ; i < SIZE; i++ 
		
		result = forStr.split(";");			// i = 0 / i < SIZE / i++
		forStr = "	for (";
		
		// �ʿ���� ���������� for�� �������� �����Ѵ�.
		for(int i = 0; i < oldVar.size(); i++){
			for(int j = 0 ; j < mainFor.forVariable.size(); j++)
				if(mainFor.forVariable.get(j).equals(oldVar.get(i)))
					mainFor.forVariable.remove(j);
		}
		
		// for���� 1.�ʱ�ȭ �κ�	int i = 0
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
