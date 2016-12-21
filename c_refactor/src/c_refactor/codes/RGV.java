package c_refactor.codes;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
public class RGV extends TransformLoopStructure{
	public String reName = "";
	public RGV(String sourceCode,int inputcount,String rename) throws IOException {
		super(sourceCode, inputcount);
		// TODO Auto-generated constructor stub
		if(!rename.equals("null")&& !rename.equals(""))
			reName = rename;
	}
	
	public void check_globalVar(){
		Stack<String> forstack = new Stack<String>();
		String str = "";
		String result = forLoopStructure.replaceAll("\\p{Space}", "");// 변수 추출을 위한 Split
		List<String> forwardAddState = new ArrayList<String>();
		List<String> backwardAddState = new ArrayList<String>();
		int num = 0;
		String localVarName = "";
		
		while(result.length()!=0){
			if(result.matches("for.*")){
				result = result.substring(result.indexOf(")")+1);
					
			}
			else if(result.charAt(0)=='{'){
				forstack.push("{");
				//System.out.println("{");
				result = result.substring(1);
			}
			else if(result.charAt(0)=='}'){
				result = result.substring(1);
				String temp = "";
				while(!(temp=forstack.pop()).contains("{")){
					String[] token = temp.split("\\[|\\]|\\+|\\-|\\/|\\%|\\*|\\=|;");			
					
					for(int i = 0; i < token.length;i++)
					{
						if (i == 0){			// token[0] 은 대입문의 좌변
							for(int j = 0 ; j < globalVariable.size(); j++){									// 전역변수들의 개수 만큼
								if(token[i].equals(globalVariable.get(j))&& !localVariable.contains(token[i])){	// 대입문의 좌변에 전역변수가 존재하고, 그 변수가 지역변수가 아닐 때
									if(reName == "") {	// 사용자가 정의한 변수 이름이 없을 경우
										localVarName = token[i]+"_local";											// localVarName : 전역변수를 바꿀 지역변수 이름
										while(localVariable.contains(localVarName)){								// 이미 존재하는 변수이름이라면
											localVarName= token[i]+"_local"+String.valueOf(num++);					// 존재하지 않는 변수이름이 될때까지 num을 1씩 증가시켜 _local 뒤에 붙인다.
										}
									}else{
										try{
											if(!localVariable.contains(reName))									// 지역 변수들 중에서 이미 사용자가 정의한 이름과 같은 변수가 있을 경우									
												localVarName = reName;
											else
												;	// 에러 던지기
										}catch(Exception e){
											
										}
									}
									backwardAddState.add(token[i] + " = " + localVarName);						// for문 뒤에  'sum = sum_local'문장을 추가한다.   
									forLoopStructure = forLoopStructure.replaceAll(token[i],localVarName);							// 전역변수 sum -> 지역변수 sum_local로 대체
									if(!addvarType.containsKey(localVarName)){									// 새로 선언해야하는 지역변수 목록에 중복되지 않는 다면
										addvarType.put(localVarName, globalvarType.get(token[i]));				// 'sum_local' 변수명 : 'int' 자료형
										addvar.add(localVarName);												// 'sum_local' 변수를 새로 선언해야하는 지역변수 목록에 추가
									}
										
								}
							}
						}
						else{					// 대입문 우변
							for(int j = 0 ; j < globalVariable.size(); j++){
								if(token[0]!=token[i]&&token[i].equals(globalVariable.get(j))&& !localVariable.contains(token[i])){		// 좌변과 같지 않고, 대입문의 우변에 전역변수가 존재하고, 그 변수가 지역변수가 아닐 때
									forwardAddState.add(localVarName+" = " + token[i]);							// for문 앞에 'sum_local = sum'문장을 추가한다.
									forLoopStructure = forLoopStructure.replaceAll(token[i],localVarName);							// 전역변수 sum -> 지역변수 sum_local로 대체
									if(!addvarType.containsKey(localVarName)){									// 새로 선언해야하는 지역변수 목록에 중복되지 않는다면
										addvarType.put(localVarName, globalvarType.get(token[i]));				// 'sum_local' 변수명 : 'int' 자료형
										addvar.add(localVarName);												// 'sum_local' 변수를 새로 선언해야하는 지역변수 목록에 추가
									}
								}
							}
						}
					}
				}
			}
			else{
				if(result.matches(".*for.*")){
					forstack.push(result.substring(0,result.indexOf("for")));
					str = result.substring(0,result.indexOf("for"));
				}else{
					forstack.push(result.substring(0,result.indexOf(";")+1));
					str = result.substring(0,result.indexOf(";")+1);
				}
				
				result = result.substring(str.length());
				//System.out.println(result);
			}
		}
		
		String forwardAddStates = "";
		for(int i = 0; i < forwardAddState.size(); i++){
			forwardAddStates += forwardAddState.get(i) +";";
		}
		forLoopStructure = forwardAddStates + forLoopStructure;
		for(int i = 0; i < backwardAddState.size(); i++){
			forLoopStructure += backwardAddState.get(i) +";";
		}
	}
	//결과 저장
}
