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
		String result = forLoopStructure.replaceAll("\\p{Space}", "");// ���� ������ ���� Split
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
						if (i == 0){			// token[0] �� ���Թ��� �º�
							for(int j = 0 ; j < globalVariable.size(); j++){									// ������������ ���� ��ŭ
								if(token[i].equals(globalVariable.get(j))&& !localVariable.contains(token[i])){	// ���Թ��� �º��� ���������� �����ϰ�, �� ������ ���������� �ƴ� ��
									if(reName == "") {	// ����ڰ� ������ ���� �̸��� ���� ���
										localVarName = token[i]+"_local";											// localVarName : ���������� �ٲ� �������� �̸�
										while(localVariable.contains(localVarName)){								// �̹� �����ϴ� �����̸��̶��
											localVarName= token[i]+"_local"+String.valueOf(num++);					// �������� �ʴ� �����̸��� �ɶ����� num�� 1�� �������� _local �ڿ� ���δ�.
										}
									}else{
										try{
											if(!localVariable.contains(reName))									// ���� ������ �߿��� �̹� ����ڰ� ������ �̸��� ���� ������ ���� ���									
												localVarName = reName;
											else
												;	// ���� ������
										}catch(Exception e){
											
										}
									}
									backwardAddState.add(token[i] + " = " + localVarName);						// for�� �ڿ�  'sum = sum_local'������ �߰��Ѵ�.   
									forLoopStructure = forLoopStructure.replaceAll(token[i],localVarName);							// �������� sum -> �������� sum_local�� ��ü
									if(!addvarType.containsKey(localVarName)){									// ���� �����ؾ��ϴ� �������� ��Ͽ� �ߺ����� �ʴ� �ٸ�
										addvarType.put(localVarName, globalvarType.get(token[i]));				// 'sum_local' ������ : 'int' �ڷ���
										addvar.add(localVarName);												// 'sum_local' ������ ���� �����ؾ��ϴ� �������� ��Ͽ� �߰�
									}
										
								}
							}
						}
						else{					// ���Թ� �캯
							for(int j = 0 ; j < globalVariable.size(); j++){
								if(token[0]!=token[i]&&token[i].equals(globalVariable.get(j))&& !localVariable.contains(token[i])){		// �º��� ���� �ʰ�, ���Թ��� �캯�� ���������� �����ϰ�, �� ������ ���������� �ƴ� ��
									forwardAddState.add(localVarName+" = " + token[i]);							// for�� �տ� 'sum_local = sum'������ �߰��Ѵ�.
									forLoopStructure = forLoopStructure.replaceAll(token[i],localVarName);							// �������� sum -> �������� sum_local�� ��ü
									if(!addvarType.containsKey(localVarName)){									// ���� �����ؾ��ϴ� �������� ��Ͽ� �ߺ����� �ʴ´ٸ�
										addvarType.put(localVarName, globalvarType.get(token[i]));				// 'sum_local' ������ : 'int' �ڷ���
										addvar.add(localVarName);												// 'sum_local' ������ ���� �����ؾ��ϴ� �������� ��Ͽ� �߰�
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
	//��� ����
}
