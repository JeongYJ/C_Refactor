package c_refactor.swt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.javaeditor.JavaSourceViewer;
import org.eclipse.jdt.internal.ui.text.FastJavaPartitionScanner;
import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jdt.ui.text.IJavaPartitions;
import org.eclipse.jdt.ui.text.JavaSourceViewerConfiguration;
import org.eclipse.jdt.ui.text.JavaTextTools;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.LineNumberRulerColumn;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.text.templates.GlobalTemplateVariables.Cursor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.jface.text.TextViewer;
/*
 * Preview
 */

public class Preview_ {

	public boolean checkAllcode = true;
	private StyledText st_originCode;
	private StyledText styledText_1;
	
	private StyledText st_refactorCode;
	CommonFunc cf;
	StyledText styledText;
	//private Shell shell_1;

	Shell shell;
	
	public void open(String name) throws Exception {
		cf = new CommonFunc();
		Display display = Display.getDefault();
		Shell shell = createContents(display, name); // �⺻ SWTȭ�� ����
		centerScreen.centerScreen_sh(display, shell); // ��ǻ�� ȭ�� �߾ӿ� ����
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
	
	protected Shell createContents(Display display, String sh_name) throws Exception {
		shell = new Shell(display, SWT.Close | SWT.TITLE | SWT.MIN | SWT.MAX);
		shell.setSize(829, 663);
		shell.setText("Preview");
		shell.setLayout(new GridLayout(1, false));
		
		_Prev(shell);
		_Warning(shell);
		_Button(shell,sh_name);
		
		return shell;
	}

	
	/*
	 * Preview ȭ�� ���� �Լ�
	 */
	public void _Prev(Shell shell)
	{
		//view composite 
		Composite cp_prev = new Composite(shell, SWT.NONE);
		GridData gd_cp_preview = new GridData(GridData.FILL_BOTH , GridData.FILL_BOTH, true, true, 1, 1);
		gd_cp_preview.widthHint = 817;
		gd_cp_preview.heightHint = 548;
		cp_prev.setLayoutData(gd_cp_preview);
		cp_prev.setBounds(0, 0, 64, 64);
		cp_prev.setLayout(new GridLayout(3, false));

		origPreviewForm(cp_prev);
		rePreviewForm(cp_prev);
		new Label(cp_prev, SWT.NONE);
	}
	
	
	/*
	 * 	public void origPreviewForm(Composite cp)
	{
		ViewForm vf = new ViewForm(cp, SWT.BORDER | SWT.FLAT);
		vf.setBorderVisible(true);
		//GridLayout gl = new GridLayout();
		vf.setLayout(new FillLayout());
		vf.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite composite = new Composite(vf, SWT.BORDER);
		//FillLayout gl_composite = new FillLayout();
		composite.setLayout(new FillLayout());
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		vf.setContent(composite);
		
		CompositeRuler ruler = new CompositeRuler();
		LineNumberRulerColumn lnrc = new LineNumberRulerColumn();
		ruler.addDecorator(0,lnrc);
	
		JavaTextTools tools= JavaPlugin.getDefault().getJavaTextTools();
		
		SourceViewer sv = new JavaSourceViewer(
			      composite, ruler, null, true,
			      SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.FULL_SELECTION, null);
		
		st_originCode = sv.getTextWidget();
		st_originCode.setAlignment(SWT.CENTER);

		st_originCode.setLayoutData(new GridData(GridData.FILL_BOTH));
		st_originCode.setLeftMargin(10);
		st_originCode.setBottomMargin(10);
		st_originCode.setRightMargin(10);
		
		SourceViewerConfiguration config = 
                new JavaSourceViewerConfiguration(
                tools.getColorManager(),
                JavaPlugin.getDefault().getCombinedPreferenceStore(),
                null,
                null
                ); 
				
				sv.configure(config);
				
				
				IDocumentPartitioner partitioner = new FastPartitioner(new FastJavaPartitionScanner(), new String[] { 
		                IJavaPartitions.JAVA_DOC,
		                IJavaPartitions.JAVA_MULTI_LINE_COMMENT, 
		                IJavaPartitions.JAVA_SINGLE_LINE_COMMENT,
		                IJavaPartitions.JAVA_STRING, IJavaPartitions.JAVA_CHARACTER });
				
	  			IDocument d = new Document();
	  			
	  			d.setDocumentPartitioner(partitioner);
	  			d.set(textCheckPage.PreviewText);
	  			partitioner.connect(d); 
	  			sv.setDocument(d);
	  			sv.getTextWidget().setFont(JFaceResources.getFont(PreferenceConstants.EDITOR_TEXT_FONT)); 
	
	
			sv.getTextWidget().setFont(JFaceResources.getFont(PreferenceConstants.EDITOR_TEXT_FONT)); 
			new Label(composite, SWT.NONE);
			
			
			
			 final Font font = JFaceResources 
		                .getFont(PreferenceConstants.EDITOR_TEXT_FONT); 
			 sv.getTextWidget().setFont(font); 
			
			 final org.eclipse.swt.graphics.Cursor arrowCursor = sv.getTextWidget().getDisplay() 
		                .getSystemCursor(SWT.CURSOR_ARROW); 
		        sv.getTextWidget().setCursor(arrowCursor); 
			
		        
			ViewForm viewForm = new ViewForm(vf, SWT.NONE);
			vf.setTopLeft(viewForm);
			viewForm.setLayout(new GridLayout());
			GridData gd_vf2 = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
			viewForm.setLayoutData(gd_vf2);
			
			
			Label lblNewLabel = new Label(viewForm, SWT.BORDER);
			lblNewLabel.setAlignment(SWT.CENTER);
			viewForm.setTopLeft(lblNewLabel);
			lblNewLabel.setText("Original Source Code");
			
			Button savebutton1 = new Button(viewForm, SWT.BORDER);
			viewForm.setTopCenter(savebutton1);
			savebutton1.setText("Save Code");
			savebutton1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			savebutton1.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					saveText(shell, st_originCode, "Original Source Code");
				}
			});
		
	}
	 * */
	
	
	public void origPreviewForm(Composite cp)
	{
		ViewForm vf = new ViewForm(cp, SWT.BORDER | SWT.FLAT);
		vf.setBorderVisible(true);
		//GridLayout gl = new GridLayout();
		vf.setLayout(new FillLayout());
		vf.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite composite = new Composite(vf, SWT.BORDER);
		//FillLayout gl_composite = new FillLayout();
		composite.setLayout(new FillLayout());
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		vf.setContent(composite);
		
		CompositeRuler ruler = new CompositeRuler();
		LineNumberRulerColumn lnrc = new LineNumberRulerColumn();
		ruler.addDecorator(0,lnrc);
	
		JavaTextTools tools= JavaPlugin.getDefault().getJavaTextTools();
		
		SourceViewer sv = new JavaSourceViewer(
			      composite, ruler, null, true,
			      SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.FULL_SELECTION, null);
		
		st_originCode = sv.getTextWidget();
		st_originCode.setAlignment(SWT.CENTER);

		st_originCode.setLayoutData(new GridData(GridData.FILL_BOTH));
		st_originCode.setLeftMargin(10);
		st_originCode.setBottomMargin(10);
		st_originCode.setRightMargin(10);
		
		SourceViewerConfiguration config = 
                new JavaSourceViewerConfiguration(
                tools.getColorManager(),
                JavaPlugin.getDefault().getCombinedPreferenceStore(),
                null,
                null
                ); 
				
				sv.configure(config);
				
				
				IDocumentPartitioner partitioner = new FastPartitioner(new FastJavaPartitionScanner(), new String[] { 
		                IJavaPartitions.JAVA_DOC,
		                IJavaPartitions.JAVA_MULTI_LINE_COMMENT, 
		                IJavaPartitions.JAVA_SINGLE_LINE_COMMENT,
		                IJavaPartitions.JAVA_STRING, IJavaPartitions.JAVA_CHARACTER });
				
	  			IDocument d = new Document();
	  			
	  			d.setDocumentPartitioner(partitioner);
	  			d.set(textCheckPage.PreviewText);
	  			partitioner.connect(d); 
	  			sv.setDocument(d);
	  			sv.getTextWidget().setFont(JFaceResources.getFont(PreferenceConstants.EDITOR_TEXT_FONT)); 
	
	
			sv.getTextWidget().setFont(JFaceResources.getFont(PreferenceConstants.EDITOR_TEXT_FONT)); 
	
			
			
			
			 final Font font = JFaceResources 
		                .getFont(PreferenceConstants.EDITOR_TEXT_FONT); 
			 sv.getTextWidget().setFont(font); 
			
			 final org.eclipse.swt.graphics.Cursor arrowCursor = sv.getTextWidget().getDisplay() 
		                .getSystemCursor(SWT.CURSOR_ARROW); 
		        sv.getTextWidget().setCursor(arrowCursor); 
			
		        
			ViewForm viewForm = new ViewForm(vf, SWT.NONE);
			vf.setTopLeft(viewForm);
			viewForm.setLayout(new GridLayout());
			GridData gd_vf2 = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
			viewForm.setLayoutData(gd_vf2);
			
			
			Label lblNewLabel = new Label(viewForm, SWT.BORDER);
			lblNewLabel.setAlignment(SWT.CENTER);
			viewForm.setTopLeft(lblNewLabel);
			lblNewLabel.setText("Original Source Code");
			
			Button savebutton1 = new Button(viewForm, SWT.BORDER);
			viewForm.setTopCenter(savebutton1);
			savebutton1.setText("Save Code");
			savebutton1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			savebutton1.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					saveText(shell, st_originCode, "Original Source Code");
				}
			});
	
	}
	
	public void rePreviewForm(Composite cp)
	{
		ViewForm vf = new ViewForm(cp, SWT.BORDER | SWT.FLAT);
		vf.setBorderVisible(true);
		//GridLayout gl = new GridLayout();
		vf.setLayout(new FillLayout());
		vf.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite composite = new Composite(vf, SWT.BORDER);
		//FillLayout gl_composite = new FillLayout();
		composite.setLayout(new FillLayout());
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		vf.setContent(composite);
		
		CompositeRuler ruler = new CompositeRuler();
		LineNumberRulerColumn lnrc = new LineNumberRulerColumn();
		ruler.addDecorator(0,lnrc);
	
		JavaTextTools tools= JavaPlugin.getDefault().getJavaTextTools();
		
		SourceViewer sv = new JavaSourceViewer(
			      composite, ruler, null, true,
			      SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.FULL_SELECTION, null);
		
		st_refactorCode = sv.getTextWidget();
		st_refactorCode.setAlignment(SWT.CENTER);
		//st_originCode.setAlignment(SWT.CENTER);
		//GridData gd_styledText_1 = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
		//gd_styledText_1.widthHint = 300;
		//gd_styledText_1.heightHint = 400;
		st_refactorCode.setLeftMargin(10);
		st_refactorCode.setBottomMargin(10);
		st_refactorCode.setRightMargin(10);
		
		
		st_refactorCode.setLayoutData(new GridData(GridData.FILL_BOTH));
		SourceViewerConfiguration config = 
                new JavaSourceViewerConfiguration(
                tools.getColorManager(),
                JavaPlugin.getDefault().getCombinedPreferenceStore(),
                null,
                null
                ); 
				
				sv.configure(config);
				
				IDocumentPartitioner partitioner = new FastPartitioner(new FastJavaPartitionScanner(), new String[] { 
		                IJavaPartitions.JAVA_DOC,
		                IJavaPartitions.JAVA_MULTI_LINE_COMMENT, 
		                IJavaPartitions.JAVA_SINGLE_LINE_COMMENT,
		                IJavaPartitions.JAVA_STRING, IJavaPartitions.JAVA_CHARACTER });
				
	  			IDocument d = new Document();
	  			
	  			d.setDocumentPartitioner(partitioner);
	  			d.set(textCheckPage.resultText);
	  			partitioner.connect(d); 
	  			//st_originCode = sv.getTextWidget();
	  			//GridData gd_st_originCode = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	  			//gd_st_originCode.heightHint = 454;
	  			//gd_st_originCode.widthHint = 363;
	  			//st_originCode.setLayoutData(gd_st_originCode);
	  			
	  			sv.setDocument(d);
	  			sv.getTextWidget().setFont(JFaceResources.getFont(PreferenceConstants.EDITOR_TEXT_FONT)); 
	
		////////
		/*
		ViewForm vf = new ViewForm(cp, SWT.FLAT);
		vf.setBorderVisible(true);
		GridData gd_vf = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_vf.heightHint = 508;
		gd_vf.widthHint = 390;
		vf.setLayoutData(gd_vf);
		

		st_refactorCode = new StyledText(vf, SWT.V_SCROLL);
		st_refactorCode.setAlignment(SWT.CENTER);
		GridData gd_st_refactorCode = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_st_refactorCode.widthHint = 108;
		st_refactorCode.setLayoutData(gd_st_refactorCode);
		st_refactorCode.setText(textCheckPage.resultText);
		
		st_refactorCode.addListener(SWT.SCROLL_LOCK, new Listener() {
			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				  event.doit=false;
			}
		});
		*/
	  			sv.getTextWidget().setFont(JFaceResources.getFont(PreferenceConstants.EDITOR_TEXT_FONT)); 

							
				
				 final Font font = JFaceResources 
			                .getFont(PreferenceConstants.EDITOR_TEXT_FONT); 
				 sv.getTextWidget().setFont(font); 
				
				 final org.eclipse.swt.graphics.Cursor arrowCursor = sv.getTextWidget().getDisplay() 
			                .getSystemCursor(SWT.CURSOR_ARROW); 
			        sv.getTextWidget().setCursor(arrowCursor); 
				
			        
				ViewForm viewForm = new ViewForm(vf, SWT.NONE);
				vf.setTopLeft(viewForm);
				viewForm.setLayout(new GridLayout());
				GridData gd_vf2 = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
				viewForm.setLayoutData(gd_vf2);
				
				
				Label lblNewLabel2 = new Label(viewForm, SWT.BORDER);
				lblNewLabel2.setAlignment(SWT.CENTER);
				viewForm.setTopLeft(lblNewLabel2);
				lblNewLabel2.setText("Refactoring Source Code");
				
				Button savebutton2 = new Button(viewForm, SWT.BORDER);
				viewForm.setTopCenter(savebutton2);
				savebutton2.setText("Save Code");
				savebutton2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
				savebutton2.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						saveText(shell, st_refactorCode, "Refactoring Source Code");
					}
				});

			    
				
	  			/*
		vf.setContent(st_refactorCode);
		{
			Label lblNewLabel_1 = new Label(vf, SWT.NONE);
			lblNewLabel_1.setSize(new Point(120, 30));
			lblNewLabel_1.setAlignment(SWT.CENTER);
			vf.setTopLeft(lblNewLabel_1);
			lblNewLabel_1.setText("Refactoring Source Code");
		}
		
		Button btnNewButton = new Button(vf, SWT.NONE);
		//btnNewButton.setSize(30, 10);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				saveText(shell, st_refactorCode, "Refactoring Source Code");
			}
		});

		vf.setTopCenter(btnNewButton);
		btnNewButton.setText("Save Code");
	  			 */
		
	}
	
	/*�ؽ�Ʈ ���� ����*/
	  boolean saveText(Shell shell, StyledText st, String str) {
		  File file = null;
		  String inDate = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
		 // String lastDirectory;
		  //boolean unsaved;
		  if (file == null) {
			  FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
			 // if (lastDirectory != null)
			 // fileDialog.setFilterPath(lastDirectory);
	
			  String selectedFile = fileDialog.open();
		 if (selectedFile == null) {
			  System.out.println("File is not saved");
			  return false;
		  }
		 
		  file = new File(selectedFile);
		 //  lastDirectory = file.getParent();
		  }
		  
		  try {
			  
			String def = "/***  "+inDate+"  ***/";
			String def2 = "/***  "+str+"  ***/";
			FileWriter writer = new FileWriter(file);
			writer.write(def + def2 + st.getText());
			writer.close();
		  
		  //unsaved = false;
		  
		  return true;
		  } catch (IOException e) {}
		  
		  return false;
	  }


	/*
	 * Warning ȭ�� ���� �Լ�
	 */
	public void _Warning(Shell shell) throws Exception 
	{
		// warning composite
		Composite cp_warning = new Composite(shell, SWT.NONE);
		cp_warning.setLayout(new GridLayout(2, false));
		GridData gd_cp_warning = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_cp_warning.heightHint = 68;
		gd_cp_warning.widthHint = 795;
		cp_warning.setLayoutData(gd_cp_warning);
		
		ViewForm vf = new ViewForm(cp_warning, SWT.FLAT);
		vf.setBorderVisible(true);
		
		GridData gd_vf = new GridData(SWT.CENTER, SWT.FILL, false, false, 1, 1);
		gd_vf.heightHint = 60;
		gd_vf.widthHint = 795;
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
		gd_styledText.widthHint = 775;
		styledText.setLayoutData(gd_styledText);
		vf.setContent(styledText);
		vf.setTopLeft(lb_warning);
		new Label(cp_warning, SWT.NONE);
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
	
	/*
	 * ��ư ���� �Լ�
	 */
	public void _Button(Shell shell, String sh_name)
	{
		Composite cp_button = new Composite(shell, SWT.NONE);
		cp_button.setLayout(new GridLayout(3, false));
		GridData gd_cp_button = new GridData(SWT.RIGHT, SWT.BOTTOM, true, false, 1, 1);
		gd_cp_button.widthHint = 269;
		cp_button.setLayoutData(gd_cp_button);

		GridData gd_btn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn.widthHint = 86;
		Button btn_changeCode = cf.createButton(cp_button, gd_btn, "Short Code");
		
		// ��ü �ڵ带 �� ������, ���� �� �ڵ常 �� ������ ����
		btn_changeCode.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(checkAllcode == false)
				{
					//���� ȭ����  Short Codeȭ��! -> All Codeȭ������ ��ȯ
					st_originCode.setText(textCheckPage.selectedText);
					st_refactorCode.setText(textCheckPage.resultText);
					
					btn_changeCode.setText("All Code");
					checkAllcode = true;
				}
				else
				{
					//���� ȭ����  All Codeȭ�� -> Short Codeȭ������ ��ȯ
					st_originCode.setText(textCheckPage.PreviewText);
					st_refactorCode.setText(textCheckPage.resultText);
					
					btn_changeCode.setText("Short Code");
					checkAllcode = false;
				}
			}
		});
		
		Button btn_back  = cf.createButton(cp_button, gd_btn, "< Back");
		btn_back.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(sh_name.equals(textCheckPage.RemoveMV))
				{
					shell.close();
					Warning_ window = new Warning_();
					try {
						window.open(true, sh_name);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else
				{
					shell.close();
					Rename_ window = new Rename_();
					try {
						window.open(true, sh_name);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});

	
				Button btn_ok = cf.createButton(cp_button, gd_btn, "OK");
				// 
				btn_ok.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						st_refactorCode.setText("");
						shell.close();
					}
				});

	}
}
