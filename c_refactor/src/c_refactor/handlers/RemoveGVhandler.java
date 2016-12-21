package c_refactor.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import c_refactor.codes.getCurrentCodes;
import c_refactor.swt.textCheckPage;

public class RemoveGVhandler extends AbstractHandler {

	public RemoveGVhandler() {
	}

	public Object execute(ExecutionEvent event) throws ExecutionException {

		//���� .java editor���� ����ڰ� ������ �ڵ� �ҷ����� 
		getCurrentCodes gc = new getCurrentCodes();
		textCheckPage.selectedText=null;
		gc.getAllText();
		
		/*
		 * RenameWarninghandler.java
		 * void open(String titleName, boolean previewCheck, String var, String windowName)
		 */
		RenameWarninghandler rw = new RenameWarninghandler();
		rw.open(textCheckPage.RemoveGV, true, "null", "Rename"); 
		
		return null;
	}
}
