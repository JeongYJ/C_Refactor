package c_refactor.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import c_refactor.codes.getCurrentCodes;
import c_refactor.swt.textCheckPage;

public class test001 extends AbstractHandler {
	
	public test001() {
	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		//값 받아오기 (어떤 값을 받아와서 사용할지 결정)
		getCurrentCodes gc = new getCurrentCodes();
		textCheckPage.selectedText=null;
		gc.getAllText();
		
		/*
		 * RenameWarninghandler.java
		 * void open(String titleName, boolean previewCheck, String var, String windowName)
		 */
		RenameWarninghandler rw = new RenameWarninghandler();
		rw.open(textCheckPage.Test001, true, "null", "Warning"); 
		
		return null;
	}

}
