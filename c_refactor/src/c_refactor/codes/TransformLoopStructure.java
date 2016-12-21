package c_refactor.codes;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Stack;

public class TransformLoopStructure {
	public String[] valueType = {"void","double","int","char","float","short","long"};
	
	
	public Hashtable<String, String> funcType = new Hashtable();				// �Լ� ���� ��
	public Hashtable<String, String> globalvarType = new Hashtable();		// ���� ���� �ڷ���
	public Hashtable<String, String> globalvarValue = new Hashtable();		// ���� ���� ��
	public Hashtable<String, String> localvarType = new Hashtable();			// ���� ���� �ڷ���
	public Hashtable<String, String> localvarValue = new Hashtable();		// ���� ���� ��
	public List<String> globalVariable = new ArrayList<String>();			// ���� ���� ����Ʈ
	public List<String> localVariable = new ArrayList<String>();				// ���� ���� ����Ʈ
	
	public List<String> forVariable = new ArrayList<String>();				// for�� ���� ����
	public List<String> forCondition= new ArrayList<String>();				// for�� ���ǹ�
	public List<String> forOperate= new ArrayList<String>();					// for�� ������
	public List<Integer> funclineCount = new ArrayList<Integer>(); 			// �Լ� �� ��ȣ ����

	public String forLoopStructure = "";							// for�� ���� ����
	public String localVarNextLine;								 
	
	public List<String> oldVar = new ArrayList<String>();		// ������ ���� ���� ����Ʈ
	public List<String> oldVarEx = new ArrayList<String>();		// ������ ���� ���� ����
	public String newVar = ""; 									// ������ ���� ������ ��ü�� ����
	
	public List<String> frontglobalVar = new ArrayList<String>();	// �տ� �߰��� ��������
	public List<String> backglobalVar = new ArrayList<String>();		// �ڿ� �߰��� ��������
	
	public List<Integer> loopStateCount = new ArrayList<Integer>();
	
	/* �߰��ؾ��ϴ� ���� */
	public Hashtable<String, String> addvarType = new Hashtable();
	public List<String> addvar = new ArrayList<String>();
	
	public int count = 0;
	public int forcount = 0;
	public int forsize = 0;
	
	int inputCount = 0;		// ����ڰ� ������ for�� �� ��ȣ
	int funcCount = 0 ;		// �����丵 ����� for���� ���ԵǾ� �ִ� �Լ� �� ��ȣ
	
	static String[] byOriginalLine;
	String[] byLine;					// ���� �ҽ����� '\t' '\r' ����
	int byLineIndex = 0;
	
	forStructure mainFor = new forStructure();				// �����丵 �� for��
	
	TransformLoopStructure(String sourceCode, int inputcount) throws IOException{
		String tempStr = "";
		String line;
		inputCount = inputcount;		// ����ڰ� ������ for���� �� ��ȣ ����
		byLine = sourceCode.split("\n");	// ������ ������ ��� ������ ���๮�ڸ� �������� ���ڿ��� ������.
		byOriginalLine =  sourceCode.split("\n");
		
		for(int i = 0; i < byLine.length; i++){
			byLine[i] = byLine[i].replaceAll("[\t\r]", "");
		}
		
		
		while((line = byLine[byLineIndex++])!=null&& !(byLineIndex >= byLine.length)) { 
			
			count++;
			line = remove_commentline(line,byLine);	// �ּ� ������ �����Ѵ�.
			
			// ���� ���μ��� �κ� �Ǵ� ���� ���� �ǳʶٱ�
			if(line.equals(" ") || line.matches(".*#.*"))
			{
				continue;
			}
			// ���� ���� �Ǵ� �Լ� ���� �κ�
			else if(line.matches(".*;.*")){
				
				String[] result = line.split(";");
				int resultSize = result.length;
				
				if(!line.matches(".*;")){				// ���� ';'�� ������ �ʴ´ٸ� 			 ex) int a = 0; int 
					tempStr = result[result.length-1];	// ';'���� ������ �ʴ� ������ �ӽ÷� �����صд�. ex) tempStr : int
					resultSize -=1;						// ���� ';'���� ���� ������� ũ��� 1���� �����Ѵ�.	resultSize : 2 - 1 = 1
				}
				else if(tempStr != ""){					// ������ ���� �� ���� �ڵ� �κа� ���� �� ������ ��ģ��.
					result[0] = tempStr+result[0];
					tempStr = "";						// tempStr �ʱ�ȭ
				}
				if(resultSize >= 1){					// resultSize �� 1�� �̻� ������
					for(int i = 0; i < resultSize; i++)	
					{	
						String tempresult = result[i].replaceAll("\\p{Space}", "");				// ���� ����  ���� ��� voidfunction()
						/* '()'�� �����ϴ��� (�Լ� �����ϴ� �κ�) */
						if(result[i].contains("(")&&result[i].contains(")")){
							extract_funcName(tempresult.substring(0,result[i].indexOf('(')-1));	// ���� ��� voidfunction
						}
						// ���� �Լ��� �ƴ϶�� ���� ����!
						else{
							extract_varName(tempresult,"global");	// ���� ��� intvariable
						}
							
					}
				}
					
			}
			// �Լ� ���� �κ�
			else if(line.contains("{")){
				tempStr = tempStr + line;
				break;
			}
				
			else
				tempStr = line;
			
		}  
		

		/* �ڵ忡 ���ǵǴ� �Լ����� �ٹ�ȣ�� �����Ѵ�.*/
		while(true){
			//funclineCount.add(count);
			String temp;
			Stack<String> stack = new Stack<String>();
			if(byLineIndex >= byLine.length)	
				break;
			if(line.contains("(")&&line.contains(")")&&stack.empty()){
				temp = line.replaceAll("\\p{Space}", "");
				for(int i = 0; i<valueType.length; i++)
					if(temp.matches(valueType[i]+".*")){
						funclineCount.add(count);
						break;
					}
				 if(line.contains("{"))
					 stack.push(line);
				 else{
					 while(!line.contains("{")){
						 line = byLine[byLineIndex++];
						 line = remove_commentline(line,byLine);
						 count++;
					 } 
					 stack.push(line);
				 }
			}
			//stack.push("{");
			while(!stack.empty()) {
				line = byLine[byLineIndex++];
				count++;
				line = remove_commentline(line,byLine);	// �ּ� ������ �����Ѵ�.
				if(line==" ")
					continue;
				else if(line.contains("}")){
					while(!(temp=stack.pop()).contains("{"));//System.out.println(temp);
				}
				else {
					stack.push(line);
				}
			}
			if(byLineIndex >= byLine.length)
				break;
			line = byLine[byLineIndex++];
			count++;
		}
		
		//br.close();
		byLineIndex = 0;
		
		
		
		for(int i = 0 ; i < funclineCount.size()-1; i++){
			if(inputCount < funclineCount.get(i+1) && inputCount > funclineCount.get(i) ){
					funcCount = funclineCount.get(i);
					break;
			}
			
		}
		
		count = 0;
		//br = new BufferedReader(new FileReader("input3.txt"));
		byLineIndex = 0;
		while(true){ 
			count++;
			line = byLine[byLineIndex++];
			line = remove_commentline(line,byLine);	// �ּ� ������ �����Ѵ�.
			if(count==funcCount)
				break;
		}
		
		/* ���������� �����Ѵ�. */
		get_localVariable(byLine);
		line = localVarNextLine;	// ���� ���� �Ѱ��ش�.
		
		while(true){ 
			if(count==inputCount)
				break;
			else{					// �߰� ���������� ���� ���� ��� �� �������ش�.
				String [] result = line.split("\\||\\&|\\}|\\{|\\)|\\(|;");
				for(int i = 0 ; i < result.length; i++){
					if(isAssignState(result[i])){		// ���������� ���� ���ԵǴ� ���
						change_localvarValue(result[i]);
					}
						
				}
			}
			count++;
			line = byLine[byLineIndex++];
			line = remove_commentline(line,byLine);	// �ּ� ������ �����Ѵ�.
		}
		
		extract_for(byLine,line);
	}
	public boolean isAssignState(String str){
		if(str.contains("=")&&!str.contains("==")&&!str.contains(">")&&!str.contains("<")&&!str.contains("!"))
			return true;
		else
			return false;
	}
	
	public String readContentFrom(String textFileName) throws IOException {
        BufferedReader bufferedTextFileReader = new BufferedReader(new FileReader(textFileName));
        StringBuilder contentReceiver = new StringBuilder();
        char[] buf = new char[1024];  
        
        while (bufferedTextFileReader.read(buf) > 0) {
            contentReceiver.append(buf);
        } 
 
        return contentReceiver.toString();
    } 
	public void change_localvarValue(String AssignState ){	// ���Թ����� ������ ���� ������ �и��ϰ� ������ ���� �����Ѵ�.
		String assignState = "";
		if(AssignState.matches(".*\\s.*"))
			assignState = AssignState.replaceAll("\\s", "");	// ���� ����
		else
			assignState = AssignState;
		
		String var = "";		// ����
		String varScope = "";
		String varType = "";
		String rightSentence = "";
		String operator = "";
		int value = 0;
		if(assignState.matches(".*\\+\\=.*"))
			operator = "+=";
		else if(assignState.matches(".*\\-\\=.*"))
			operator = "-=";
		else if(assignState.matches(".*\\*\\=.*"))
			operator = "*=";
		else if(assignState.matches(".*\\/\\=.*"))
			operator = "/=";
		else if(assignState.matches(".*\\%\\=.*"))
			operator = "%=";
		else if(assignState.matches(".*\\=.*"))
			operator = "=";
		
		String[] temp = assignState.split(operator);
		var = temp[0];
		rightSentence = temp[1];
		
		for(int i = 0 ; i < localVariable.size(); i++){				// ���� �������� Ž���Ѵ�. 
			if(var.matches(localVariable.get(i)))
			{
				var = localVariable.get(i);
				value = Integer.parseInt(localvarValue.get(var));
				varType = localvarType.get(var);
				varScope = "local";
				break;
			}
		}
		if(var == ""){		// �������� �߿� ������ ã�� ������ ��
			for(int i = 0 ; i < globalVariable.size(); i++){		// ���� �������� Ž���Ѵ�.
				if(assignState.matches(globalVariable.get(i)+".*"))
				{
					var = globalVariable.get(i);
					value = Integer.parseInt(globalvarValue.get(var));
					varType = globalvarType.get(var);
					varScope = "global";
					break;
				}
			}
		}
		
		if(var == ""||varType != "int"){		// ��������, ������������ ã�Ƴ��� ���� ������� �Ǵ�
			return;								// int���� �ƴҰ��. (���������� �ƴ�)
		}
		else{
			assignState = assignState.substring(var.length());
			if(rightSentence.matches("^[0-9]*$"))	// �캯 ���� ���ڷθ� ���� �Ǿ� �ִ��� 
			{
				if(operator == "=")		
					value = Integer.parseInt(rightSentence);
				else if(operator == "+=")		
					value = value + Integer.parseInt(rightSentence);
				else if(operator == "-=")		
					value = value - Integer.parseInt(rightSentence);
				else if(operator == "/=")		
					value = value / Integer.parseInt(rightSentence);
				else if(operator == "*=")		
					value = value * Integer.parseInt(rightSentence);
				else if(operator == "%=")		
					value = value % Integer.parseInt(rightSentence);
			}else{
				return;
			}
			if(varScope == "global")		// ���� �������
				globalvarValue.replace(var, String.valueOf(value));	// ���� ������ ���� �����Ѵ�.
			else{							// ���� �������
				localvarValue.replace(var, String.valueOf(value));	// ���� ������ ���� �����Ѵ�.
			}
		}
		
		
	}
	public void extract_for(String[] byLine, String Line) throws IOException{
		
		String line = Line;
		/* for�� ���� */
		Stack<String> forstack = new Stack<String>();
		Boolean isFor = false;
		int flag = 0;
		do{
			count++;
			line = remove_commentline(line,byLine);	// �ּ� ������ �����Ѵ�.
			if(line.equals(" "))
				continue;
			if(line.matches(".*for.*"))
			{
				String forStr = line.substring(line.indexOf("for"));	// forStr : for ( i = 0 ; i < SIZE; i++ ) {
				forStr = forStr.substring(0,forStr.indexOf(')')+1);		// forStr : for ( i = 0 ; i < SIZE; i++ )
				if(flag == 0)
				{	
					mainFor.check_forLoopVar(forStr, this);				// forStr : for ( i = 0 ; i < SIZE; i++ ) �� �м��Ѵ�.
					forcount = count;
					flag = 1;
				}
				line = line.substring(line.indexOf("for")+forStr.length()); // line : {
				
				forLoopStructure += forStr;						//	forLoopStructure : for ( i = 0 ; i < SIZE; i++ )
				isFor=true;
				
			}
			if(isFor&&line.contains("{"))
			{
				forstack.push(line);
				forLoopStructure += line;					//	forLoopStructure : for ( i = 0 ; i < SIZE; i++ ) {
				while(!forstack.empty()){ 
					if(line.contains("}")){
						while(!forstack.pop().contains("{"));
						line = "";
					}else{
						line = byLine[byLineIndex++];				
						count++;
						line = remove_commentline(line,byLine);	// �ּ� ������ �����Ѵ�.
						forstack.push(line);
						forLoopStructure += line;			//	forLoopStructure : for ( i = 0 ; i < SIZE; i++ )
					}
					
				}
				isFor = false;
				// for�� ��� ���� �Ϸ�. forLoopStructure
				//System.out.println("forloopstructure : "+forLoopStructure);
				forsize = count - forcount + 1;
				//System.out.println("forsize:"+forsize);
				//check_globalVar();
				//check_manyVar();
				//check_loopStructure();
				break;
			}
			else if(isFor&&line.matches(".*;.*"))
			{
				isFor = false;
				forLoopStructure += line.substring(0,line.indexOf(";")+1);
				//System.out.println("forloopstructure : "+forLoopStructure);
				forsize = count - forcount + 1;
				//System.out.println("forsize:"+forsize);
				//check_globalVar();
				//check_manyVar();
				//check_loopStructure();
				break;
			}
			
		}while((line=byLine[byLineIndex++])!=null && !(byLineIndex >= byLine.length));

	}
	public String remove_commentline(String line,String[] byLine) throws IOException{
		
		String templine;
		boolean commentLine = false;
		
		/* �ּ� �����ϴ� �κ� */
		if(line.contains("//")) 										// '//' �����ϴ� ���� 
			line = line.substring(0, line.indexOf("//"));				
		else if(line.contains("/*")) {									// '/*' ����
			templine = line;
			line = line.substring(0, line.indexOf("/*"));				// '*/' ����
			if(templine.contains("*/")){								// '*/' �� ������ ���ڿ� �ڸ��� 
				line += templine.substring(templine.indexOf("*/")+2);	
				commentLine = false;
			}else{
				commentLine = true;
			}
		}
		if(commentLine){											// '/* */'�� ���� ���� ���
			do{
				if(line.contains("*/"))
				{
					line = line.substring(line.indexOf("*/") + 2);
					commentLine = false;
					break;
				}
				count++;
			}while((line = byLine[byLineIndex++])!=null&& !(byLineIndex >= byLine.length));
		}
		return line;
	}
	public void get_localVariable(String[] byLine) throws IOException{
		String line;
		String tempStr="";
		
		boolean endLocal = false;
		while((line = byLine[byLineIndex++])!=null&& !(byLineIndex >= byLine.length)){
			count++;
			line = remove_commentline(line,byLine);
			//System.out.println(count+":"+line);
			
			if(line.equals(" ")) continue;				// �� ���� �ٽ� ������.
			else if(line.matches(".*;.*")){				// ';'�� �����Ѵٸ�
				String[] result = line.split(";");		// ';'�� �������� ������ ������.
				int resultSize = result.length;
				
				if(!line.matches(".*;")){				// �������� ';'���� ������ �ʴ´ٸ�
					tempStr = result[result.length-1];	// �ӽ÷� ������ ������ �����Ѵ�.
					resultSize -=1;						// ������ ������ ������ ����� ���� ����
				}
					
				if(tempStr != ""){						// �� ���� ������ ������ ';' ���� ������ �ʾҴٸ�?
					result[0] = tempStr+result[0];		// �� �� ���� ������ ';'���� ���� ���δ�.
					tempStr = "";
				}
				
				//System.out.println(result[0]);
				if(resultSize >= 1){
					for(int i = 0; i < resultSize; i++)
					{	
						result[i] = result[i].trim();
						// �ڷ����� �����ϴ��� (�������� �����ϴ� �κ�)
						for(int j = 0; j < valueType.length; j++)
							if(result[i].matches(valueType[j]+" .*")){
								String tempresult = result[i].replaceAll("\\p{Space}", "");	// ���� ����  ���� ��� intvariable=0
								extract_varName(tempresult,"local");	// ���� ��� intvariable
								//System.out.println("localvariable: "+tempresult);
								endLocal = false;		// ���� ���� ������ ������ �ʾ����� ǥ��
								break;
							}else
								endLocal = true;		// ���� ���� ������ �������� ǥ��
						 if(endLocal){
							 localVarNextLine =  line;
							 return ;
						 }
							 
					}
				}
					
			}
			
		}
	}
	// �Լ� ������ �ڷ����� �Լ� �̸��� �и��Ѵ�.
	public void extract_funcName(String str){
		String tempStr = str;
		for(int i = 0; i<valueType.length; i++)
			if(tempStr.matches(".*"+valueType[i]+".*")){
				funcType.put(tempStr.replace(valueType[i], ""),valueType[i]);	// function, void
				break;
			}
	}
	// ���� ������ �ڷ����� �Լ� �̸��� �и��Ѵ�.
	public void extract_varName(String str, String isGlobal){
		String[] returnTypeName = new String[2];
		String tempStr = str;
		
		for(int i = 0; i<valueType.length; i++)
			if(tempStr.matches(".*"+valueType[i]+".*")){
				returnTypeName[0] = valueType[i];						// int
				returnTypeName[1] = tempStr.replaceFirst(valueType[i], "");	// variableName or variableName=0
				break;
			}
		String[] numberOfVariables = returnTypeName[1].split(",");
		for(int j = 0 ; j < numberOfVariables.length; j++)
		{	
			String[] tempStrs = numberOfVariables[j].split("=");
			//System.out.println("name:"+tempStrs[0]+ " type"+returnTypeName[0]);
			if(isGlobal == "global"){	// ���� ���� �� ��
				globalVariable.add(tempStrs[0]);
				globalvarType.put(tempStrs[0], returnTypeName[0]);	// variableName , int
				if(tempStrs.length > 1)
				{	
					globalvarValue.put(tempStrs[0], tempStrs[1]);	// variableName , 0
					//System.out.println("name:"+tempStrs[0]+ " value"+tempStrs[1]);
				}
			}
			else{		// ���� ���� �� ��
				localVariable.add(tempStrs[0]);
				localvarType.put(tempStrs[0], returnTypeName[0]);	// variableName , int
				if(tempStrs.length > 1)
					localvarValue.put(tempStrs[0], tempStrs[1]);	// variableName , 0
				else
					localvarValue.put(tempStrs[0], "-1");	// variableName , 0
			}
			
		}		
	}
	public String return_RefactorCode() throws IOException{
		String line = "";
		int count = 1;
		String refactorCode = "";
		
		byLineIndex = 0;
		
		while((line=byOriginalLine[byLineIndex++])!=null&&!(byLineIndex >= byOriginalLine.length)){
			if(count == funcCount+1 && addvar.size()>0){
				for(int i = 0 ; i < addvar.size(); i++){
					refactorCode = refactorCode + "\t"+addvarType.get(addvar.get(i))+" "+addvar.get(i)+";";
					refactorCode += "\n";
				}
			}
			else if(count == inputCount){
				while(forsize-- > 0){
					line = byOriginalLine[byLineIndex++];
					count++;
					//byLineIndex++;
				}
				String tempstr = forLoopStructure;
				while((tempstr.contains(")")||tempstr.contains("{")||tempstr.contains("}")||tempstr.contains(";"))&&tempstr!="")
				{
					line ="";
				    if(tempstr.contains("{")){
				    	if(tempstr.contains(";")){
							if(tempstr.indexOf(";") < tempstr.indexOf("("))
							{	
								refactorCode = refactorCode + tempstr.substring(0,tempstr.indexOf(";")+1);
								tempstr = tempstr.substring(tempstr.indexOf(";")+1);
							}else{
								refactorCode = refactorCode + tempstr.substring(0,tempstr.indexOf("{")+1);
								tempstr = tempstr.substring(tempstr.indexOf("{")+1);
							}
				    	}
						else{
							refactorCode = refactorCode + tempstr.substring(0,tempstr.indexOf("{")+1);
							tempstr = tempstr.substring(tempstr.indexOf("{")+1);
						}
					}else if(tempstr.contains("(")){
						if(tempstr.contains(";")){
							if(tempstr.indexOf(";") < tempstr.indexOf("("))
							{	
								refactorCode = refactorCode + tempstr.substring(0,tempstr.indexOf(";")+1);
								tempstr = tempstr.substring(tempstr.indexOf(";")+1);
							}
							else{
								refactorCode = refactorCode + tempstr.substring(0,tempstr.indexOf(")")+1);
								tempstr = tempstr.substring(tempstr.indexOf(")")+1);
							}
						}else{
							refactorCode = refactorCode + tempstr.substring(0,tempstr.indexOf(")")+1);
							tempstr = tempstr.substring(tempstr.indexOf(")")+1);
						}
					}else if(tempstr.contains(";")){
						refactorCode = refactorCode + tempstr.substring(0,tempstr.indexOf(";")+1);
						tempstr = tempstr.substring(tempstr.indexOf(";")+1);
					}else{
						refactorCode = refactorCode + tempstr.substring(0,tempstr.indexOf("}")+1);
						tempstr = tempstr.substring(tempstr.indexOf("}")+1);
					}
				    refactorCode += "\n";
				    refactorCode = refactorCode + "\t";
				}
				//wr.write(forLoopStructure);
				
			}
			refactorCode += line;
			refactorCode += "\n";
			count++;
			
		}
		byLineIndex = 0;						// �о���� �ε��� �ʱ�ȭ
		refactorCode += byOriginalLine[count-1];	// ������ �� �߰�
		refactorCode += "\n";					// �ٹٲ� �߰�
		System.out.print(refactorCode);
		return refactorCode;
	}
}
