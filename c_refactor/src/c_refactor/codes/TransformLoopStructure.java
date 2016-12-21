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
	
	
	public Hashtable<String, String> funcType = new Hashtable();				// 함수 리턴 형
	public Hashtable<String, String> globalvarType = new Hashtable();		// 전역 변수 자료형
	public Hashtable<String, String> globalvarValue = new Hashtable();		// 전역 변수 값
	public Hashtable<String, String> localvarType = new Hashtable();			// 지역 변수 자료형
	public Hashtable<String, String> localvarValue = new Hashtable();		// 지역 변수 값
	public List<String> globalVariable = new ArrayList<String>();			// 전역 변수 리스트
	public List<String> localVariable = new ArrayList<String>();				// 지역 변수 리스트
	
	public List<String> forVariable = new ArrayList<String>();				// for문 증감 변수
	public List<String> forCondition= new ArrayList<String>();				// for문 조건문
	public List<String> forOperate= new ArrayList<String>();					// for문 연산자
	public List<Integer> funclineCount = new ArrayList<Integer>(); 			// 함수 줄 번호 저장

	public String forLoopStructure = "";							// for문 구조 저장
	public String localVarNextLine;								 
	
	public List<String> oldVar = new ArrayList<String>();		// 제거할 증감 변수 리스트
	public List<String> oldVarEx = new ArrayList<String>();		// 제거할 증감 변수 연산
	public String newVar = ""; 									// 제거할 증감 변수를 대체할 변수
	
	public List<String> frontglobalVar = new ArrayList<String>();	// 앞에 추가할 지역변수
	public List<String> backglobalVar = new ArrayList<String>();		// 뒤에 추가할 지역변수
	
	public List<Integer> loopStateCount = new ArrayList<Integer>();
	
	/* 추가해야하는 변수 */
	public Hashtable<String, String> addvarType = new Hashtable();
	public List<String> addvar = new ArrayList<String>();
	
	public int count = 0;
	public int forcount = 0;
	public int forsize = 0;
	
	int inputCount = 0;		// 사용자가 선택한 for문 줄 번호
	int funcCount = 0 ;		// 리팩토링 대상의 for문이 포함되어 있는 함수 줄 번호
	
	static String[] byOriginalLine;
	String[] byLine;					// 원본 소스에서 '\t' '\r' 제거
	int byLineIndex = 0;
	
	forStructure mainFor = new forStructure();				// 리팩토링 할 for문
	
	TransformLoopStructure(String sourceCode, int inputcount) throws IOException{
		String tempStr = "";
		String line;
		inputCount = inputcount;		// 사용자가 선택한 for문의 줄 번호 대입
		byLine = sourceCode.split("\n");	// 파일의 내용을 모두 가져와 개행문자를 기준으로 문자열을 나눈다.
		byOriginalLine =  sourceCode.split("\n");
		
		for(int i = 0; i < byLine.length; i++){
			byLine[i] = byLine[i].replaceAll("[\t\r]", "");
		}
		
		
		while((line = byLine[byLineIndex++])!=null&& !(byLineIndex >= byLine.length)) { 
			
			count++;
			line = remove_commentline(line,byLine);	// 주석 문장을 제거한다.
			
			// 프리 프로세서 부분 또는 공백 라인 건너뛰기
			if(line.equals(" ") || line.matches(".*#.*"))
			{
				continue;
			}
			// 전역 변수 또는 함수 선언 부분
			else if(line.matches(".*;.*")){
				
				String[] result = line.split(";");
				int resultSize = result.length;
				
				if(!line.matches(".*;")){				// 끝에 ';'로 끝나지 않는다면 			 ex) int a = 0; int 
					tempStr = result[result.length-1];	// ';'으로 끝가지 않는 문장을 임시로 저장해둔다. ex) tempStr : int
					resultSize -=1;						// 실제 ';'으로 나뉜 문장들의 크기는 1개가 감소한다.	resultSize : 2 - 1 = 1
				}
				else if(tempStr != ""){					// 끝나지 않은 전 줄의 코드 부분과 다음 줄 문장을 합친다.
					result[0] = tempStr+result[0];
					tempStr = "";						// tempStr 초기화
				}
				if(resultSize >= 1){					// resultSize 가 1개 이상 있으면
					for(int i = 0; i < resultSize; i++)	
					{	
						String tempresult = result[i].replaceAll("\\p{Space}", "");				// 공백 제거  예를 들어 voidfunction()
						/* '()'를 포함하는지 (함수 선언하는 부분) */
						if(result[i].contains("(")&&result[i].contains(")")){
							extract_funcName(tempresult.substring(0,result[i].indexOf('(')-1));	// 예를 들어 voidfunction
						}
						// 선언 함수가 아니라면 전역 변수!
						else{
							extract_varName(tempresult,"global");	// 예를 들어 intvariable
						}
							
					}
				}
					
			}
			// 함수 시작 부분
			else if(line.contains("{")){
				tempStr = tempStr + line;
				break;
			}
				
			else
				tempStr = line;
			
		}  
		

		/* 코드에 정의되는 함수들의 줄번호를 추출한다.*/
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
				line = remove_commentline(line,byLine);	// 주석 문장을 제거한다.
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
			line = remove_commentline(line,byLine);	// 주석 문장을 제거한다.
			if(count==funcCount)
				break;
		}
		
		/* 지역변수를 추출한다. */
		get_localVariable(byLine);
		line = localVarNextLine;	// 다음 줄을 넘겨준다.
		
		while(true){ 
			if(count==inputCount)
				break;
			else{					// 중간 지역변수가 값이 변할 경우 값 변경해준다.
				String [] result = line.split("\\||\\&|\\}|\\{|\\)|\\(|;");
				for(int i = 0 ; i < result.length; i++){
					if(isAssignState(result[i])){		// 지역변수에 값이 대입되는 경우
						change_localvarValue(result[i]);
					}
						
				}
			}
			count++;
			line = byLine[byLineIndex++];
			line = remove_commentline(line,byLine);	// 주석 문장을 제거한다.
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
	public void change_localvarValue(String AssignState ){	// 대입문에서 변수와 대입 문장을 분리하고 변수의 값을 변경한다.
		String assignState = "";
		if(AssignState.matches(".*\\s.*"))
			assignState = AssignState.replaceAll("\\s", "");	// 공백 제거
		else
			assignState = AssignState;
		
		String var = "";		// 변수
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
		
		for(int i = 0 ; i < localVariable.size(); i++){				// 지역 변수에서 탐색한다. 
			if(var.matches(localVariable.get(i)))
			{
				var = localVariable.get(i);
				value = Integer.parseInt(localvarValue.get(var));
				varType = localvarType.get(var);
				varScope = "local";
				break;
			}
		}
		if(var == ""){		// 지역변수 중에 변수를 찾지 못했을 때
			for(int i = 0 ; i < globalVariable.size(); i++){		// 전역 변수에서 탐색한다.
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
		
		if(var == ""||varType != "int"){		// 지역변수, 전역변수에서 찾아내지 못한 변수라면 또는
			return;								// int형이 아닐경우. (증감변수가 아님)
		}
		else{
			assignState = assignState.substring(var.length());
			if(rightSentence.matches("^[0-9]*$"))	// 우변 식이 숫자로만 구성 되어 있는지 
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
			if(varScope == "global")		// 전역 변수라면
				globalvarValue.replace(var, String.valueOf(value));	// 전역 변수의 값을 변경한다.
			else{							// 지역 변수라면
				localvarValue.replace(var, String.valueOf(value));	// 지역 변수의 값을 변경한다.
			}
		}
		
		
	}
	public void extract_for(String[] byLine, String Line) throws IOException{
		
		String line = Line;
		/* for문 추출 */
		Stack<String> forstack = new Stack<String>();
		Boolean isFor = false;
		int flag = 0;
		do{
			count++;
			line = remove_commentline(line,byLine);	// 주석 문장을 제거한다.
			if(line.equals(" "))
				continue;
			if(line.matches(".*for.*"))
			{
				String forStr = line.substring(line.indexOf("for"));	// forStr : for ( i = 0 ; i < SIZE; i++ ) {
				forStr = forStr.substring(0,forStr.indexOf(')')+1);		// forStr : for ( i = 0 ; i < SIZE; i++ )
				if(flag == 0)
				{	
					mainFor.check_forLoopVar(forStr, this);				// forStr : for ( i = 0 ; i < SIZE; i++ ) 을 분석한다.
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
						line = remove_commentline(line,byLine);	// 주석 문장을 제거한다.
						forstack.push(line);
						forLoopStructure += line;			//	forLoopStructure : for ( i = 0 ; i < SIZE; i++ )
					}
					
				}
				isFor = false;
				// for문 모두 추출 완료. forLoopStructure
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
		
		/* 주석 제거하는 부분 */
		if(line.contains("//")) 										// '//' 포함하는 문장 
			line = line.substring(0, line.indexOf("//"));				
		else if(line.contains("/*")) {									// '/*' 부터
			templine = line;
			line = line.substring(0, line.indexOf("/*"));				// '*/' 까지
			if(templine.contains("*/")){								// '*/' 이 있으면 문자열 자르기 
				line += templine.substring(templine.indexOf("*/")+2);	
				commentLine = false;
			}else{
				commentLine = true;
			}
		}
		if(commentLine){											// '/* */'이 여러 줄일 경우
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
			
			if(line.equals(" ")) continue;				// 빈 줄은 다시 돌린다.
			else if(line.matches(".*;.*")){				// ';'을 포함한다면
				String[] result = line.split(";");		// ';'을 기준으로 문장을 나눈다.
				int resultSize = result.length;
				
				if(!line.matches(".*;")){				// 마지막에 ';'으로 끝나지 않는다면
					tempStr = result[result.length-1];	// 임시로 마지막 문장을 저장한다.
					resultSize -=1;						// 마지막 문장을 제외한 문장들 수를 저장
				}
					
				if(tempStr != ""){						// 전 줄의 마지막 문장이 ';' 으로 끝나지 않았다면?
					result[0] = tempStr+result[0];		// 그 후 줄의 문장의 ';'까지 같이 붙인다.
					tempStr = "";
				}
				
				//System.out.println(result[0]);
				if(resultSize >= 1){
					for(int i = 0; i < resultSize; i++)
					{	
						result[i] = result[i].trim();
						// 자료형을 포함하는지 (지역변수 선언하는 부분)
						for(int j = 0; j < valueType.length; j++)
							if(result[i].matches(valueType[j]+" .*")){
								String tempresult = result[i].replaceAll("\\p{Space}", "");	// 공백 제거  예를 들어 intvariable=0
								extract_varName(tempresult,"local");	// 예를 들어 intvariable
								//System.out.println("localvariable: "+tempresult);
								endLocal = false;		// 지역 변수 범위가 끝나지 않았음을 표시
								break;
							}else
								endLocal = true;		// 지역 변수 범위가 끝났음을 표시
						 if(endLocal){
							 localVarNextLine =  line;
							 return ;
						 }
							 
					}
				}
					
			}
			
		}
	}
	// 함수 선언문의 자료형과 함수 이름을 분리한다.
	public void extract_funcName(String str){
		String tempStr = str;
		for(int i = 0; i<valueType.length; i++)
			if(tempStr.matches(".*"+valueType[i]+".*")){
				funcType.put(tempStr.replace(valueType[i], ""),valueType[i]);	// function, void
				break;
			}
	}
	// 변수 선언문의 자료형과 함수 이름을 분리한다.
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
			if(isGlobal == "global"){	// 전역 변수 일 때
				globalVariable.add(tempStrs[0]);
				globalvarType.put(tempStrs[0], returnTypeName[0]);	// variableName , int
				if(tempStrs.length > 1)
				{	
					globalvarValue.put(tempStrs[0], tempStrs[1]);	// variableName , 0
					//System.out.println("name:"+tempStrs[0]+ " value"+tempStrs[1]);
				}
			}
			else{		// 지역 변수 일 때
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
		byLineIndex = 0;						// 읽어오는 인덱스 초기화
		refactorCode += byOriginalLine[count-1];	// 마지막 줄 추가
		refactorCode += "\n";					// 줄바꿈 추가
		System.out.print(refactorCode);
		return refactorCode;
	}
}
