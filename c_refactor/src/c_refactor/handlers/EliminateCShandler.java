package c_refactor.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import c_refactor.codes.ecs;
import c_refactor.codes.getCurrentCodes;
import c_refactor.swt.textCheckPage;

public class EliminateCShandler extends AbstractHandler{
	
	public static ecs ec;
	
	public EliminateCShandler() {
	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		getCurrentCodes gc = new getCurrentCodes();
		textCheckPage.selectedText=null;
		gc.getAllText();
		
		ec=new ecs();
		String text=ec.change_name_set(null);
		System.out.println(text);
		
		
		/*
		 * RenameWarninghandler.java
		 * void open(String titleName, boolean previewCheck, String var, String windowName)
		 */
		RenameWarninghandler rw = new RenameWarninghandler();
		rw.open(textCheckPage.EliminateCS, true, text , "Rename"); 

	
		return null;
	}
	
	
	
}