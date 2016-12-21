package c_refactor.codes;

import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import c_refactor.swt.textCheckPage;

public class getCurrentCodes {
	
	public void getAllText()
	{
		 //����ڰ� java editor���� ������ �ڵ�� ���� �ٹ�ȣ�� �ٷ� 
		   IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		   IEditorPart tmp = page.getActiveEditor();
		
		   /*��ü �ؽ�Ʈ ���� �ҷ����� �ڵ�!
		    * 
		    * ITextOperationTarget : Defines the target for text operations.
		    * IEditorPart.getAdapter(Class adapter) :  Returns an object which is an instance of the given class associated with this object.
		    * (adapter - the adapter class to look up)
		    * 
		    * instanceof ������ : ���������� �����ϰ� �ִ� �ν��Ͻ��� ���� Ÿ���� �˾ƺ��� ���� ���, �ַ� ���ǹ��� ���
		    * �������� instanceof Ÿ��(Ŭ������), Return boolean
		    * instanceof�� �̿��� �������� true�� ����ٴ� ���� ���������� �˻��� Ÿ������ ����ȯ�� �����ϴٴ� ���� ����. 
		    * ���� null�� ���������� ���� instanceof ������ �����ϸ�  false ����� ��´�.
		    * 
		    * ITextOperationTarget.getDocument() : Returns the text viewer's input document 
		    * 
		    * */
		   
		   ITextOperationTarget target =
		            (ITextOperationTarget)tmp.getAdapter(ITextOperationTarget.class);
		    if (target instanceof ITextViewer) { //���� target�� ITextViewer�� ����ȯ �����ϴٸ�
		    	textCheckPage.PreviewText = ((ITextViewer) target).getDocument().get();
		        System.out.println("((ITextViewer)target).getDocumnet().get()\n" + ((ITextViewer) target).getDocument().get());
		    }
		    
		    getCurrentText(tmp);
		    
	}
	
	 public static void getCurrentText(IEditorPart tmp) {

		 ITextSelection tmpSel = ((ITextSelection) tmp.getEditorSite().getSelectionProvider().getSelection());
		   
		   textCheckPage.selectedText = tmpSel.getText(); //������ �ؽ�Ʈ ����
		   textCheckPage.lineNum = tmpSel.getStartLine(); //������ ���� �� ��ȣ ����
		   
		   System.out.println("textCheckPage.selectedText \n" + textCheckPage.selectedText);//������ �ؽ�Ʈ ���
		   System.out.println("textCheckPage.lineNum \n" + textCheckPage.lineNum);//���� �� ��ȣ ���
		   
	}
}
