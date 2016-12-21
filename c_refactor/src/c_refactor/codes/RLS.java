package c_refactor.codes;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class RLS extends TransformLoopStructure{
	public RLS(String sourceCode,int inputcount) throws IOException {
		super(sourceCode, inputcount);
		// TODO Auto-generated constructor stub
	}

	/* for문 1과 for문 2가 같은 변수를 사용하고 있는 지 판단하는 함수 */
	public boolean check_commonVar(forStructure for1, forStructure for2){
		String[] for1_token = for1.forState.split("\\[|\\]|\\+|\\-|\\/|\\%|\\*|\\=|;|\\(|\\)");			// 변수 추출
		String[] for2_token = for2.forState.split("\\[|\\]|\\+|\\-|\\/|\\%|\\*|\\=|;|\\(|\\)");
		 
		for(int i = 0; i < for1_token.length;i++){			
			for(int j = 0; j < for2_token.length; j++){
				if(localVariable.contains(for1_token[i])||globalVariable.contains(for1_token[i]))		// 지역변수 혹은 전역변수 리스트에 포함되어 있으면
				{
					if(for1_token[i].equals(for2_token[j]))		// for1문의 변수와 for2문의 변수가 같으면
						return true;							// 서로 같은 변수를 사용하고 있다고 리턴
				}
			}
		}
		return false;					// 서로 같은 변수를 사용하지 않으면 두 for문이 연관성이 없으므로 false 리턴
	}
	public void check_loopStructure() {
		int startloop = 0;
		String str = "";
		String mainforState = "";				// for(i = 0; i < SizeM
		String result = forLoopStructure.replaceAll("\\p{Space}", "");
		Stack<String> forstack = new Stack<String>();
		Stack<String> forstack2 = new Stack<String>();
		
		List<String> forsVar = new ArrayList<String>();		// 중첩되는 for문들의  변수들 저장
		String check_useVar = "";
		boolean isPossible = true;
		boolean isLayerCoupling = false;
		int forCount = 1;
		List<forStructure> subFors = new ArrayList<forStructure>();
		
		mainFor.layer = 1;
		for(int i = 0; i < mainFor.forVariable.size(); i++)
			forsVar.add(mainFor.forVariable.get(i));
		
		while(result.length()!=0){
			if(result.matches("for.*")){
				forCount++;
				startloop++;
				loopStateCount.add(0);
				if(forCount > 2)			// for문이 2개 이상일때
				{
					subFors.add(new forStructure());
					try {
						// sub for문 변수, 조건, 연산자분리.
						subFors.get(subFors.size()-1).check_forLoopVar(result.substring(0,result.indexOf(")")+1),this);
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}		
					subFors.get(subFors.size()-1).layer = startloop;
				}
				result = result.substring(result.indexOf(")")+1);
			}
			else if(result.charAt(0)=='{'){
				forstack.push("{");
				//System.out.println("{");
				result = result.substring(1);
			}
			else if(result.charAt(0)=='}'){
				String popForState = "";
				result = result.substring(1);	
				while(!(popForState=forstack.pop()).contains("{")){	
					if(subFors.size() > 0)
					subFors.get(subFors.size()-1).forState += popForState;
					loopStateCount.set(startloop-1, loopStateCount.get(startloop-1)+1);			// for문이 가지고 있는 문장수 ++
				}
				startloop--;
			}
			else{                                                                                   
				if(result.matches(".*;.*")){
					forstack.push(result.substring(0,result.indexOf(";")+1));
					str = result.substring(0,result.indexOf(";")+1);
				}else{
				}
				
				result = result.substring(str.length());
				//System.out.println(result);
			}
		}
		
		while(!forstack.empty()){
			check_useVar = forstack.pop();
			loopStateCount.set(startloop-1, loopStateCount.get(startloop-1)+1);
		}
		for(int i = 0; i < subFors.size();i++){
			System.out.println(i+"번째"+subFors.get(i).layer+ "들어있는 문장 : "+subFors.get(i).forState);
		}
		
		if(loopStateCount.get(0)==0 && subFors.size()>0){			// 메인 For문이 포함하고 있는 문장이 0이고 서브 For문들의 개수가 0이 아닐때
			int i = 0;
			while(true){
				for(int j = i+1 ; j < subFors.size(); j++){			// 같은 계층에 있는 FOR문이 존재하는 지 확인
					if(subFors.get(i).layer == subFors.get(j).layer)
					{
						if(check_commonVar(subFors.get(i),subFors.get(j)))
						{	
							isPossible = false;
							break;
						}
					}
				}
				i++;
				if(loopStateCount.get(i)>0)
					break;
				if(subFors.size() == i){							// FOR문안에 있는 subFOR문들을 모두 탐색했을 때
					i--;
					break;
				}
			}
			String sumforState = "";
			result = forLoopStructure.replaceAll("\\p{Space}","");	
			if(i==0){
				System.out.println("단일 반복문으로 바꿀 중첩 반목문이 존재하지 않습니다.");
			}else{
				mainforState = "for ( ";
				for(int k = 0 ; k < mainFor.forVariable.size(); k++)
					mainforState = mainforState + mainFor.forVariable.get(k)+" = " + localvarValue.get(mainFor.forVariable.get(k))+",";
				mainforState = mainforState.substring(0,mainforState.length()-1);
				mainforState += ";" + mainFor.forCondition.get(0);
				int tempforcount = 0;
				
				while(result.length()!=0){
					if(result.matches("for.*")){
						result = result.substring(result.indexOf(")")+1);
						if((tempforcount > 0 && tempforcount < i+1))
						{	
							sumforState += "\n\t"+mainforState;
							for(int j = 0 ; j < subFors.get(tempforcount-1).forVariable.size(); j++)		// 메인 For문의 변수들의 정보를 저장한다.
								forsVar.add(subFors.get(tempforcount-1).forVariable.get(j));
							if(subFors.get(tempforcount-1).return_forCondition()!="")
								sumforState += " * "+subFors.get(tempforcount-1).return_forCondition();		// SIZE * SIZE
						}
						if(tempforcount == i+1)
							break;
						tempforcount++;
						
					}else if(result.charAt(0)=='{'){
						forstack.push("{");
						result = result.substring(1);
					}
					else if(result.charAt(0)=='}'){
						result = result.substring(1);	
							if(tempforcount == i+1){
								sumforState += ";";
								for(int k = 0; k < mainFor.forOperate.size(); k++)
									sumforState += mainFor.forOperate.get(k) + ",";
								sumforState = sumforState.substring(0,  sumforState.length()-1);
								sumforState += "){";
								i++;
							}
							
							while(!(check_useVar=forstack.pop()).contains("{")){
								if(tempforcount == i){
									sumforState += check_useVar;
								}
								for(int j = 0 ; j < forsVar.size(); j++){
									if(check_useVar.contains(forsVar.get(j))){
										isPossible = false;
										sumforState += "}";
									}
								}
							}
							sumforState += "}";
						
					}
					else{                                                                                   
						if(result.matches(".*;.*")){
							forstack.push(result.substring(0,result.indexOf(";")+1));
							str = result.substring(0,result.indexOf(";")+1);
						}else{
							
						}
						
						result = result.substring(str.length());
						//System.out.println(result);
						
					}
				}
				if(!forstack.empty()){
					if(tempforcount == i+1){
						sumforState += ";";
						for(int k = 0; k < mainFor.forOperate.size(); k++)
							sumforState += mainFor.forOperate.get(k) + ",";
						sumforState = sumforState.substring(0,  sumforState.length()-1);
						sumforState += "){";
					}
				}
				while(!forstack.empty()){
					check_useVar = forstack.pop();
					if(tempforcount == i+1){
						sumforState += check_useVar;
					}
					for(int j = 0 ; j < forsVar.size(); j++){
						if(check_useVar.contains(forsVar.get(j))){
							isPossible = false;
						}
					}	
					
				}
				//sumforState += "}";
				if(isPossible){
					//System.out.println(sumforState);
					sumforState = sumforState.substring(0,sumforState.length()-1);
					forLoopStructure = sumforState;
				}else{
					
				}
				System.out.println("forLoopStructure : "+forLoopStructure);
			}
		}
		

	}
}
