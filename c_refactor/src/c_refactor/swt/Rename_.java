package c_refactor.swt;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**�̰� �������ΰŽô� */


import c_refactor.codes.RGV;
import c_refactor.codes.RLS;
import c_refactor.codes.RMV;
import c_refactor.handlers.EliminateCShandler;

public class Rename_ {
	
	private Text tb_name;
	StyledText styledText;

	public void open(boolean previewCheck, String name) throws Exception {
		Display display = Display.getDefault();
		Shell shell = createContents(display,previewCheck, name);
		centerScreen.centerScreen_sh(display,shell);
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	/**
	 * @throws Exception 
	 * @wbp.parser.entryPoint
	 */
	protected Shell createContents(Display display, boolean previewCheck, String sh_name) throws Exception {
		Shell shell = new Shell(display, SWT.Close | SWT.TITLE | SWT.MIN | SWT.MAX);
		shell.setSize(630, 200);
		shell.setText(sh_name);
		shell.setLayout(new GridLayout(1, false));
	
		createNewname(shell);
		
		//�̸� warning�� composite�� ���� ������ ��ġ�� �̻�������~
		Composite cp_warning = new Composite(shell, SWT.NONE);
		cp_warning.setLayout(new GridLayout(2, false));
		GridData gd_cp_warning = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_cp_warning.heightHint = 86;
		gd_cp_warning.widthHint = 602;
		cp_warning.setLayoutData(gd_cp_warning);
		
		
		
		//ViewForm vf = new ViewForm(cp_warning, SWT.FLAT);
		//vf.setBorderVisible(true);
		
		/*
		GridData gd_vf = new GridData(SWT.CENTER, SWT.FILL, false, false, 1, 1);
		gd_vf.heightHint = 72;
		gd_vf.widthHint = 570;
		vf.setLayoutData(gd_vf);
*/
		/*
		Label lblNewLabel = new Label(vf, SWT.NONE);
		vf.setContent(lblNewLabel);
		lblNewLabel.setText("d");
		*/
		new Label(cp_warning, SWT.NONE);
		
		Button btn_next = createButton(shell,previewCheck,sh_name,display);
		
		//rename�� ����� �߿��� �ϴ� ��� ��� �߰�
		if(sh_name.equals(textCheckPage.RemoveLS) || sh_name.equals(textCheckPage.EliminateCS))
		{
			btn_next.setText("OK");
			warningPlus(cp_warning);
		}
		else
			btn_next.setText("Next");
		cp_warning.layout();
		return shell;
	}

	public void createNewname(Shell vf) 
	{
		Composite cp_newname = new Composite(vf, SWT.NONE);
		cp_newname.setLayout(new GridLayout(2, false));
		
		GridData gd_cp_newname = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
		gd_cp_newname.heightHint = 29;
		gd_cp_newname.widthHint = 603;
		cp_newname.setLayoutData(gd_cp_newname);
		
		Label lb_newname = new Label(cp_newname, SWT.NONE);
		lb_newname.setText("New name :");

		tb_name = new Text(cp_newname, SWT.BORDER);
		GridData gd_tb_name = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
		gd_tb_name.widthHint = 514;
		tb_name.setLayoutData(gd_tb_name);
		tb_name.setText(textCheckPage.varWrightText);
	}

	public Button createButton(Shell shell, Boolean previewCheck, String sh_name, Display display)
	{
		Composite cp_button = new Composite(shell, SWT.NONE);
		cp_button.setLayout(new GridLayout(3, false));
		GridData gd_cp_button = new GridData(SWT.RIGHT, SWT.BOTTOM, false, false, 1, 1);
		gd_cp_button.widthHint = 272;
		cp_button.setLayoutData(gd_cp_button);
		
		Button btn_preview = new Button(cp_button, SWT.NONE);
		GridData gd_btn_preview = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_preview.widthHint = 82;
		btn_preview.setLayoutData(gd_btn_preview);
		btn_preview.setText("Preview >");
		btn_preview.setEnabled(previewCheck);
		btn_preview.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {		
				try {
					textCheckPage.varWrightText = tb_name.getText();
					System.out.println(tb_name.getText());
					
					/***************************������ �κ�****************************/
					
					if(sh_name.equals(textCheckPage.RemoveGV))	
					{
						// RGV( ���� �ҽ� �ڵ�, ����ڰ� ������ for�� �� ��ȣ, ����ڰ� �Է��� �� ���� �̸� )
						RGV removeGV = new RGV(textCheckPage.PreviewText,textCheckPage.lineNum,textCheckPage.varWrightText);			
						removeGV.check_globalVar();
						textCheckPage.resultText = removeGV.return_RefactorCode();// resultText set�ϱ�
					}
					else if(sh_name.equals(textCheckPage.RemoveLS))
					{
						RLS removeLS = new RLS(textCheckPage.PreviewText,textCheckPage.lineNum);
						removeLS.check_loopStructure();
						textCheckPage.resultText = removeLS.return_RefactorCode();// resultText set�ϱ�
					}
					else if(sh_name.equals(textCheckPage.RemoveMV))
					{
						RMV removeMV = new RMV(textCheckPage.PreviewText,textCheckPage.lineNum);
						removeMV.check_manyVar();
						textCheckPage.resultText = removeMV.return_RefactorCode();// resultText set�ϱ�
					}
					else if(sh_name.equals(textCheckPage.EliminateCS))
					{
						EliminateCShandler.ec.change_name_set(tb_name.getText());
					    System.out.println(textCheckPage.varWrightText);
						//EliminateCShandler.ec.setchange_name(textCheckPage.varWrightText);
						String result = EliminateCShandler.ec.getresult(textCheckPage.PreviewText);
						System.out.println("change : "+EliminateCShandler.ec.getChange());
						textCheckPage.resultText = result;
						System.out.println(result);
					}
					
						shell.close();
						Preview_ window = new Preview_();
						window.open(sh_name);
						
					shell.close();
						
				} catch (Exception ex) {
					ex.printStackTrace();
					
				}
			}
		});
		
		Button btn_next = new Button(cp_button, SWT.NONE);
		GridData gd_btn_next = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_next.widthHint = 84;
		btn_next.setLayoutData(gd_btn_next);
		btn_next.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textCheckPage.varWrightText = tb_name.getText();
				System.out.println(tb_name.getText());
				System.out.println(sh_name);
				if(sh_name.equals(textCheckPage.EliminateCS))
				{
					EliminateCShandler.ec.change_name_set(tb_name.getText());
					System.out.println(textCheckPage.varWrightText);
				//	EliminateCShandler.ec.setchange_name(textCheckPage.varWrightText);
					String result = EliminateCShandler.ec.getresult(textCheckPage.PreviewText);
					textCheckPage.resultText = result;
					System.out.println(result);
				}
				
					shell.close();
			}
		});
		
		Button btn_cancel = new Button(cp_button, SWT.NONE);
		GridData gd_btn_cancel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_cancel.widthHint = 87;
		btn_cancel.setLayoutData(gd_btn_cancel);
		btn_cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		btn_cancel.setText("Cancel");

	
		return btn_next;
	}
	
	//��� �߰� �Լ�
	
	public void warningPlus(Composite cp) throws Exception
	{
		ViewForm vf = new ViewForm(cp, SWT.FLAT);
		vf.setBorderVisible(true);
		
		GridData gd_vf = new GridData(SWT.CENTER, SWT.FILL, false, false, 1, 1);
		gd_vf.heightHint = 72;
		gd_vf.widthHint = 570;
		vf.setLayoutData(gd_vf);

		Label lb_warning = new Label(vf, SWT.NONE);
		GridData gd_lb_warning = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_lb_warning.widthHint = 68;
		lb_warning.setLayoutData(gd_lb_warning);
		lb_warning.setText("Warning");
		
		ControlDecoration controlDecoration = new ControlDecoration(lb_warning, SWT.LEFT | SWT.TOP);
		controlDecoration.setImage(SWTResourceManager.getImage(Warning_.class, "/org/eclipse/jface/dialogs/images/message_warning.png"));
		controlDecoration.setDescriptionText("Some description");
		styledText = new StyledText(vf, SWT.BORDER);
		test();
		GridData gd_styledText = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_styledText.heightHint = 65;
		gd_styledText.widthHint = 534;
		styledText.setLayoutData(gd_styledText);
		vf.setContent(styledText);
		vf.setTopLeft(lb_warning);
	}
	
	
	 public void test() throws Exception{
		 IWorkspace workspace = ResourcesPlugin.getWorkspace();
		 IResource resource = workspace.getRoot();
		 IMarker[] markers = resource.findMarkers(IMarker.MARKER, true, IResource.DEPTH_INFINITE);
		 for (IMarker m : markers) {
			 styledText.setText("Source ID: " + m.getAttribute(IMarker.SOURCE_ID) + "\nLine Number: " + m.getAttribute(IMarker.LINE_NUMBER)+ "\nProblem: " + m.getAttribute(IMarker.PROBLEM));

		     System.out.println("Id: " + m.getId());
		     System.out.println("Problem: " + m.getAttribute(IMarker.PROBLEM));
		     System.out.println("Message: " + m.getAttribute(IMarker.MESSAGE));
		     System.out.println("Source ID: " + m.getAttribute(IMarker.SOURCE_ID));
		     System.out.println("Location: " + m.getAttribute(IMarker.LOCATION));
		     System.out.println("Line Number: " + m.getAttribute(IMarker.LINE_NUMBER));
		     System.out.println("Marker: " + m.getAttribute(IMarker.MARKER));
		 }
	 }
	
	
	
}