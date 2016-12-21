package c_refactor.swt;

import java.io.IOException;

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

import c_refactor.codes.RMV;

public class Warning_ {

	CommonFunc cf;
	StyledText st;
	
	public void open(boolean previewCheck, String name) throws Exception {
		cf = new CommonFunc();
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

	protected Shell createContents(Display display, boolean previewCheck, String sh_name) throws Exception {
		Shell shell = new Shell(display, SWT.Close | SWT.TITLE | SWT.MIN | SWT.MAX);
		shell.setSize(628, 224);
		shell.setText(sh_name);
		shell.setLayout(new GridLayout(1, false));
		
		_Warning(shell);
		createButton(shell, previewCheck, sh_name); //버튼 생
	
		return shell;
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void _Warning(Shell shell) throws Exception
	{
		Composite cp_warning = new Composite(shell, SWT.NONE);
		cp_warning.setLayout(new GridLayout(2, false));
		GridData gd_cp_warning = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_cp_warning.heightHint = 138;
		gd_cp_warning.widthHint = 601;
		cp_warning.setLayoutData(gd_cp_warning);
		
		/*
		GridData gd_lb = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_lb.heightHint = 23;
		gd_lb.widthHint = 64;
		Label lb_warning = cf.createLabel(cp_warning, "Warning", gd_lb);
		
		ControlDecoration controlDecoration = new ControlDecoration(lb_warning, SWT.LEFT | SWT.TOP);
		controlDecoration.setImage(SWTResourceManager.getImage(Warning_.class, "/org/eclipse/jface/dialogs/images/message_warning.png"));
		controlDecoration.setDescriptionText("Some description");
		
		StyledText st_warning = new StyledText(cp_warning, SWT.BORDER);
		GridData gd_st_warning = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_st_warning.heightHint = 128;
		gd_st_warning.widthHint = 520;
		st_warning.setLayoutData(gd_st_warning);
		st_warning.setText(" ���  ");
		*/
		
		
		ViewForm vf = new ViewForm(cp_warning, SWT.FLAT);
		vf.setBorderVisible(true);
		
		GridData gd_vf = new GridData(SWT.CENTER, SWT.FILL, false, false, 1, 1);
		gd_vf.heightHint = 138;
		gd_vf.widthHint = 601;
		vf.setLayoutData(gd_vf);

		Label lb_warning = new Label(vf, SWT.NONE);
		GridData gd_lb_warning = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_lb_warning.widthHint = 68;
		lb_warning.setLayoutData(gd_lb_warning);
		lb_warning.setText("Warning");
		
		ControlDecoration controlDecoration = new ControlDecoration(lb_warning, SWT.LEFT | SWT.TOP);
		controlDecoration.setImage(SWTResourceManager.getImage(Warning_.class, "/org/eclipse/jface/dialogs/images/message_warning.png"));
		controlDecoration.setDescriptionText("Some description");
		st = new StyledText(vf, SWT.BORDER);
		test();
		GridData gd_styledText = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_styledText.heightHint = 138;
		gd_styledText.widthHint = 601;
		st.setLayoutData(gd_styledText);
		vf.setContent(st);
		vf.setTopLeft(lb_warning);
		//st.setText(" ��� ");
		
	}
	
	public void test() throws Exception{
		 IWorkspace workspace = ResourcesPlugin.getWorkspace();
		 IResource resource = workspace.getRoot();
		 IMarker[] markers = resource.findMarkers(IMarker.MARKER, true, IResource.DEPTH_INFINITE);
		 for (IMarker m : markers) {
			 st.setText("Source ID: " + m.getAttribute(IMarker.SOURCE_ID) + "\nLine Number: " + m.getAttribute(IMarker.LINE_NUMBER)+ "\nProblem: " + m.getAttribute(IMarker.PROBLEM));

		     System.out.println("Id: " + m.getId());
		     System.out.println("Problem: " + m.getAttribute(IMarker.PROBLEM));
		     System.out.println("Message: " + m.getAttribute(IMarker.MESSAGE));
		     System.out.println("Source ID: " + m.getAttribute(IMarker.SOURCE_ID));
		     System.out.println("Location: " + m.getAttribute(IMarker.LOCATION));
		     System.out.println("Line Number: " + m.getAttribute(IMarker.LINE_NUMBER));
		     System.out.println("Marker: " + m.getAttribute(IMarker.MARKER));
		 }
	 }
	
	
	public void createButton(Shell shell, Boolean previewCheck,String sh_name)
	{
		Composite cp_button = new Composite(shell, SWT.NONE);
		cp_button.setLayout(new GridLayout(3, false));
		cp_button.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false, 1, 1));
		
		GridData gd_btn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn.widthHint = 86;
		Button btn_preview = cf.createButton(cp_button, gd_btn, "Preview >");
		btn_preview.setEnabled(previewCheck);
		btn_preview.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {		
				try {
					if(sh_name.equals(textCheckPage.RemoveMV))
					{
						RMV	removeMV = new RMV(textCheckPage.PreviewText,textCheckPage.lineNum);
						removeMV.check_manyVar();
						textCheckPage.resultText = removeMV.return_RefactorCode();// resultText set�ϱ�
					}
						shell.close();
						Preview_ window = new Preview_();
						window.open(sh_name);
					}
					 catch (IOException e1) {
							e1.printStackTrace();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			}
		});
		
		//ok button
		Button btn_ok = cf.createButton(cp_button, gd_btn, "OK");
		btn_ok.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		
		//cancel button
		Button btn_cancel = cf.createButton(cp_button, gd_btn, "Cancel");
		btn_cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
	}
}
