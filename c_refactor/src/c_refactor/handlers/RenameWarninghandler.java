package c_refactor.handlers;

import c_refactor.swt.Eliminate_Next;
import c_refactor.swt.Rename_;
import c_refactor.swt.Warning_;
import c_refactor.swt.textCheckPage;

public class RenameWarninghandler {

	public void open(String titleName, boolean previewCheck, String var, String windowName){
		try {
			//default�����̸� �޾� �־���
			textCheckPage.varWrightText = var;
			System.out.println("open" + titleName + " " + windowName); //�ܼ�â�� �����
			System.out.println("variable is : " + var);
			
			if(windowName.equals("Rename")){
				Rename_ window = new Rename_(); 
				window.open(previewCheck,titleName);
			}
			else if(windowName.equals("Warning")){
				Warning_ window = new Warning_();
				window.open(previewCheck,titleName);
			}
			else if(windowName.equals("EliminateNext")){
				Eliminate_Next window = new Eliminate_Next();
				window.open(previewCheck,titleName);
			}	
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
