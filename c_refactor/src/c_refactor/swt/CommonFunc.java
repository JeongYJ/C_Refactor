package c_refactor.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class CommonFunc {

	public void CommonFunc()
	{}
	
	/* NullPoint Error ��ϴ�
	public void makeCodeStyleText(StyledText st_Code, Composite cp, String text, int width)
	{
		st_Code = new StyledText(cp, SWT.V_SCROLL);
		
		GridData gd_st_Code = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_st_Code.widthHint = width;
		st_Code.setLayoutData(gd_st_Code);
		//���� �� �ڵ� �����ֱ�
		st_Code.setText(text);
		
		st_Code.addListener(SWT.SCROLL_LOCK, new Listener() {
			@Override
			public void handleEvent(Event event) {
				  event.doit=false;
			}
		});
	}
	*/
	
	public Button createButton(Composite cp, GridData gd, String text)
	{
		Button btn = new Button(cp, SWT.NONE);
		btn.setLayoutData(gd);
		btn.setText(text);
		return btn;
	}
	
	public Button createButton(ViewForm vf, GridData gd, String text)
	{
		Button btn = new Button(vf, SWT.NONE);
		btn.setLayoutData(gd);
		btn.setText(text);
		return btn;
	}
	
	
	public Label createLabel(Composite cp, String text, GridData gd)
	{
		Label lb = new Label(cp,SWT.NONE);
		lb.setLayoutData(gd);
		lb.setText(text);	
		return lb;
	}
	

	public Label createLabel(ViewForm vf, String text, GridData gd)
	{
		Label lb = new Label(vf,SWT.NONE);
		lb.setLayoutData(gd);
		lb.setText(text);	
		return lb;
	}
	
	
	/*
	public Boolean buttonSelectListener(Button btn, StyledText st_orig, StyledText st_ref, Boolean check, String orig, String orig_st, String ref)
	{
		Boolean check_ = check;
		if(check_ == false)
		{
					//���� ȭ����  Short Codeȭ��! -> All Codeȭ������ ��ȯ
					st_orig.setText(orig);
					st_ref.setText(ref);
					
					btn.setText("All Code");
					check_ = true;
		}
		else
		{
					//���� ȭ����  All Codeȭ�� -> Short Codeȭ������ ��ȯ
					st_orig.setText(orig_st);
					st_ref.setText(ref);
					
					btn.setText("Short Code");
					check_ = false;
		}
		
		return check_;
			
	}
	*/
	
}
