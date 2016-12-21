package c_refactor.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import c_refactor.codes.edc;
import c_refactor.codes.getCurrentCodes;
import c_refactor.swt.textCheckPage;

public class test002 extends AbstractHandler {
	
	public static edc ed;
	
	public test002() {
	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		//���� .java editor���� ����ڰ� ������ �ڵ� �ҷ����� 
				getCurrentCodes gc = new getCurrentCodes();
				textCheckPage.selectedText=null;
				gc.getAllText();
				
				ed=new edc();
				System.out.println(textCheckPage.selectedText);
				String result = ed.getresult(textCheckPage.PreviewText);
				textCheckPage.resultText = result;
				System.out.println(result);

		
		/*
		 * RenameWarninghandler.java
		 * void open(String titleName, boolean previewCheck, String var, String windowName)
		 */
		RenameWarninghandler rw = new RenameWarninghandler();
		rw.open(textCheckPage.Test002, true, "null", "EliminateNext"); 
		

		ed = null;
		System.gc();
		
		
		return null;
	}

}
