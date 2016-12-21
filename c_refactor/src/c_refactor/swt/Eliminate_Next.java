package c_refactor.swt;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

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
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import c_refactor.codes.edc;

/*
 * Eliminate Dead Code View
 * getDelVariable() // ªË¡¶∫Øºˆ ¿Ã∏ß æÚæÓø¿±‚
 * getDelFunction() // ªË¡¶ «‘ºˆ ¿Ã∏ß æÚæÓø¿±‚
 * List«¸Ωƒ¿∏∑Œ πﬁæ∆ø¬¥Ÿ
 */

public class Eliminate_Next {

	public boolean checkAllcode = true;
	public StyledText st_originCode;
	public StyledText st_refactorCode;
	public static String elements = null;
	CommonFunc cf;
	public StyledText st;
	Shell shell;
	
	public void open(boolean previewCheck, String name) {
		cf = new CommonFunc();
		Display display = Display.getDefault();

		Shell shell = createContents(display, previewCheck, name); //Ω© »≠∏È ∂ÁøÏ±‚
		centerScreen.centerScreen_sh(display, shell); //¡ﬂæ”ø° »≠∏È ∂Áøˆ¡÷±‚
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/*shell ª˝º∫ π◊ ∆˚ ∂ÁøÏ±‚*/
	/**
	 * @wbp.parser.entryPoint
	 */
	protected Shell createContents(Display display, boolean previewCheck, String name) 
	{
		shell = new Shell();
		shell.setText(name);
		shell.setMinimumSize(new Point(554, 676));
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		shell.setSize(554, 676);
		shell.setLayout(new GridLayout(1, false));
		System.out.println("name : "+ name);
		
		
	//	if(name.equals(textCheckPage.EliminateDC))	
	//	{
			EDC_Performed(shell);
	//	}
	//	else if(name.equals(textCheckPage.Test002))	
		//{
	//		Test_Performed(shell);
	//	}
		
		_Prev(shell);
		_Warning(shell);
		_Button(shell);
	
		return shell;
	}
	
	
	public void Test_Performed(Shell shell)
	{
		Composite cp_changePerf = new Composite(shell, SWT.NONE);
		cp_changePerf.setLayout(new GridLayout(2, false));
		
		GridData gd_cp_changePerf = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_cp_changePerf.heightHint = 116;
		gd_cp_changePerf.widthHint = 527;
		cp_changePerf.setLayoutData(gd_cp_changePerf);
		
		GridData gd_lb = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1,1);
		cf.createLabel(cp_changePerf,"Changes to be Performed", gd_lb);
		cf.createLabel(cp_changePerf, "" ,gd_lb);	
		cf.createLabel(cp_changePerf, "Function" ,gd_lb);	
		//cf.createLabel(cp_changePerf, "Variable" ,gd_lb);
	
		createTreeItem(cp_changePerf, edc.getDelFunction(), "Function", 208);
	}
	
	
	
	
	/*
	 * EliminateDC tree form
	 */
	public void EDC_Performed(Shell shell)
	{
		Composite cp_changePerf = new Composite(shell, SWT.NONE);
		cp_changePerf.setLayout(new GridLayout(2, false));
		
		GridData gd_cp_changePerf = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_cp_changePerf.heightHint = 116;
		gd_cp_changePerf.widthHint = 527;
		cp_changePerf.setLayoutData(gd_cp_changePerf);
		
		GridData gd_lb = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1,1);
		cf.createLabel(cp_changePerf,"Changes to be Performed", gd_lb);
		cf.createLabel(cp_changePerf, "" ,gd_lb);	
		cf.createLabel(cp_changePerf, "Function" ,gd_lb);	
		cf.createLabel(cp_changePerf, "Variable" ,gd_lb);
	
		createTreeItem(cp_changePerf, edc.getDelFunction(), "Function", 208);
		createTreeItem(cp_changePerf, edc.getDelVariable(), "Variable", 196);
	}
		
	/*
	 * tree 
	 */
	public void createTreeItem(Composite cp, List<String> list, String name, int size)
	{
		final Tree tree = new Tree(cp, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		tree.setSize(517,90);
		GridData gd_tree = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_tree.widthHint = size;
		
		tree.setLayoutData(gd_tree);
	
		List<String> VarList = list;	
			
		for(int i=0;i<VarList.size();i++)
		{
		         Eliminate_Next.elements = VarList.get(i);
		         System.out.println(name+ " " +Eliminate_Next.elements+" Function");
		         TreeItem trtm = new TreeItem(tree, 0);
		         trtm.setChecked(true);
				 trtm.setText(elements);
		}
		tree.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				 if (event.detail == SWT.CHECK) {
					 //√º≈©µ«æ˙¿ª ∞ÊøÏ æ∆∑° ƒ⁄µÂø° ±◊∞Õ¿ª π›øµ«—¥Ÿ.
			         // text.setText(event.item + " was checked.");
			     } else {
			    	 
			        // text.setText(event.item + " was selected");
			     }
			}
		});
	}
	
	/*
	 * Preview »≠∏È ª˝º∫ «‘ºˆ
	 */
	public void _Prev(Shell shell)
	{
		Composite cp_prev = new Composite(shell, SWT.NONE);
		cp_prev.setLayout(new GridLayout(2, false));
		GridData gd_cp_prev = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_cp_prev.heightHint = 341;
		gd_cp_prev.widthHint = 527;
		cp_prev.setLayoutData(gd_cp_prev);
		
	//	GridData gd_lb = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
	//	cf.createLabel(cp_prev,"Original Source Code", gd_lb);
	//	cf.createLabel(cp_prev,"Refactored Source Code", gd_lb);
		origPreviewForm(cp_prev);
		
		/*
		st_originCode = new StyledText(cp_prev, SWT.V_SCROLL);
		GridData gd_st_originCode = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_st_originCode.widthHint = 253;
		st_originCode.setLayoutData(gd_st_originCode);
		//∫Ø∞Ê ¿¸ ƒ⁄µÂ ∫∏ø©¡÷±‚
		st_originCode.setText(textCheckPage.PreviewText);
		st_originCode.addListener(SWT.SCROLL_LOCK, new Listener() {
			@Override
			public void handleEvent(Event event) {
				  event.doit=false;
			}
		});
		*/
		rePreviewForm(cp_prev);
		/*
		st_refactorCode = new StyledText(cp_prev, SWT.V_SCROLL);
		
		GridData gd_st_refactorCode = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_st_refactorCode.widthHint = 245;
		st_refactorCode.setLayoutData(gd_st_refactorCode);
		//∫Ø∞Ê »ƒ ƒ⁄µÂ ∫∏ø©¡÷±‚
		st_refactorCode.setText(textCheckPage.resultText);
		st_refactorCode.addListener(SWT.SCROLL_LOCK, new Listener() {
			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				  event.doit=false;
			}
		});
		*/
		//∏ÆΩ∫≥ ∏¶ ∂≥æÓ∂ﬂ∑¡≥ı¿∏∏È ø°∑Ø∞° ≥≠¥Ÿ...
		/*
		cf.makeCodeStyleText(st_originCode, cp_prev, textCheckPage.PreviewText, 253);
		cf.makeCodeStyleText(st_refactorCode, cp_prev, textCheckPage.resultText, 245);
		*/
		//new Label(cp_prev, SWT.NONE);
	}

	
	//org.eclipse.wb.core.ui; (java editor);
	
/*
	public void origPreviewForm(Composite cp)
	{	
		ViewForm vf = new ViewForm(cp, SWT.FLAT);
		vf.setBorderVisible(true);
		GridData gd_vf = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_vf.heightHint = 341;
		gd_vf.widthHint = 260;
		vf.setLayoutData(gd_vf);
		
		GridData gd_st_originCode = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_st_originCode.widthHint = 108;
		st_originCode = new StyledText(vf, SWT.V_SCROLL);
		st_originCode.setAlignment(SWT.CENTER);
		st_originCode.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		st_originCode.setText(textCheckPage.PreviewText);
		
		st_originCode.addListener(SWT.SCROLL_LOCK, new Listener() {
			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				  event.doit=false;
			}
		});
		
		vf.setContent(st_originCode);
		{
			Label lblNewLabel = new Label(vf, SWT.NONE);
			lblNewLabel.setSize(new Point(120, 30));
			lblNewLabel.setTextDirection(335);
			lblNewLabel.setAlignment(SWT.CENTER);
			vf.setTopLeft(lblNewLabel);
			lblNewLabel.setText("Original Source Code");
		}
		
		Button btnNewButton_1 = new Button(vf, SWT.NONE);
		//btnNewButton_1.setSize(30, 10);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				saveText(shell, st_originCode, "Original Source Code");
			}
		});
		vf.setTopCenter(btnNewButton_1);
		btnNewButton_1.setText("Save Code");
	}
	
	public void rePreviewForm(Composite cp)
	{
		ViewForm vf = new ViewForm(cp, SWT.FLAT);
		vf.setBorderVisible(true);
		GridData gd_vf = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_vf.heightHint = 341;
		gd_vf.widthHint = 260;
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
	}
	
*/

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
	  	
	  			sv.setDocument(d);
	
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

			   
		
	}
	
	
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
	 * Warning »≠∏È ª˝º∫ «‘ºˆ
	 */
	public void _Warning(Shell shell)
	{
		Composite cp_warning = new Composite(shell, SWT.NONE);
		cp_warning.setLayout(new GridLayout(1, false));
		
		GridData gd_cp_warning = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_cp_warning.widthHint = 130;
		gd_cp_warning.heightHint = 107;
		cp_warning.setLayoutData(gd_cp_warning);
		
		ViewForm vf = new ViewForm(cp_warning, SWT.FLAT);
		vf.setBorderVisible(true);
		
		GridData gd_vf = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_vf.heightHint = 130;
		gd_vf.widthHint = 107;
		vf.setLayoutData(gd_vf);

		Label lb_warning = new Label(vf, SWT.NONE);
		GridData gd_lb_warning = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		lb_warning.setLayoutData(gd_lb_warning);
		lb_warning.setText("Warning");
		
		ControlDecoration controlDecoration = new ControlDecoration(lb_warning, SWT.LEFT | SWT.TOP);
		controlDecoration.setImage(SWTResourceManager.getImage(Eliminate_Next.class, "/org/eclipse/jface/dialogs/images/message_warning.png"));
		controlDecoration.setDescriptionText("Some description");
		st = new StyledText(vf, SWT.BORDER);
		GridData gd_styledText = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_styledText.heightHint = 130;
		gd_styledText.widthHint = 107;
		st.setLayoutData(gd_styledText);
		
		vf.setContent(st);
		vf.setTopLeft(lb_warning);
	}
	
	/*
	 * πˆ∆∞ ª˝º∫ «‘ºˆ
	 */
	public void _Button(Shell shell)
	{
		Composite cp_btn = new Composite(shell, SWT.NONE);	
		
		GridData gd_cp_btn = new GridData(SWT.RIGHT, SWT.BOTTOM, false, false, 1, 1);
		gd_cp_btn.widthHint = 158;
		cp_btn.setLayoutData(gd_cp_btn);
		cp_btn.setLayout(new GridLayout(2, false));
		
		GridData gd_btn_changeCode = new GridData(SWT.RIGHT, SWT.FILL, false, true, 1, 1);
		gd_btn_changeCode.widthHint = 72;
		Button btn_changeCode = cf.createButton(cp_btn, gd_btn_changeCode, "Short Code");
		
		btn_changeCode.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(checkAllcode == false)
				{
					//«ˆ¿Á »≠∏È¿∫  Short Code»≠∏È! -> All Code»≠∏È¿∏∑Œ ∫Ø»Ø
					st_originCode.setText(textCheckPage.selectedText);
					st_refactorCode.setText(textCheckPage.resultText);
					
					btn_changeCode.setText("All Code");
					checkAllcode = true;
				}
				else
				{
					//«ˆ¿Á »≠∏È¿∫  All Code»≠∏È -> Short Code»≠∏È¿∏∑Œ ∫Ø»Ø
					st_originCode.setText(textCheckPage.PreviewText);
					st_refactorCode.setText(textCheckPage.resultText);
					
					btn_changeCode.setText("Short Code");
					checkAllcode = false;
				}
				
				//µø¿€æ»«‘
				//checkAllcode = cf.buttonSelectListener(btn_changeCode, st_originCode, st_refactorCode,checkAllcode,textCheckPage.selectedText,textCheckPage.PreviewText,textCheckPage.resultText);
			}
		});

		GridData gd_btn_ok = new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1);
		gd_btn_ok.widthHint = 74;
		Button btn_ok = cf.createButton(cp_btn, gd_btn_ok, "OK");
		btn_ok.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
	}
}
