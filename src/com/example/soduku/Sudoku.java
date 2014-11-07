package com.example.soduku;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
@Theme("soduku")
public class Sudoku extends UI {
	
	private Panel panel;
	private GridLayout grid;
	private Board board;
	
	private GridLayout inputGrid;
	private Panel inputPanel;
	
	private final VerticalLayout vLayout = new VerticalLayout();
	private final HorizontalLayout hLayout = new HorizontalLayout();
	
	private final HorizontalLayout h2Layout = new HorizontalLayout();
	
	private UploadReceiver uploadReceiver;
	private Upload upload;
	private Button solveButton = new Button("Solve");

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = Sudoku.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {


		// Find the application directory
		String basepath = VaadinService.getCurrent()
		                  .getBaseDirectory().getAbsolutePath();
		
		System.out.println( basepath );


		// build a panel
		// https://vaadin.com/api/7.3.2/com/vaadin/ui/Panel.html
		
		// place a 9 x 9 grid control into the panel
		// https://vaadin.com/api/7.3.2/com/vaadin/ui/GridLayout.html
		
		// add a label to each element of the grid control
		// https://vaadin.com/api/com/vaadin/ui/Label.html
		// https://vaadin.com/api/7.3.2/com/vaadin/data/Property.Viewer.html
		// https://vaadin.com/api/com/vaadin/data/Property.html
		// https://vaadin.com/book/-/page/datamodel.html
		
		// each component should now be addressable by x,y

		grid = new GridLayout( 9, 9 );
		
		grid.setMargin(false);
		grid.setSpacing(false);
		grid.setWidth("300px");
		grid.setHeight("300px");
		grid.addLayoutClickListener(new GridClickListener());
		
		inputGrid = new GridLayout(3,3);
		inputGrid.setWidth("100px");
		inputGrid.setHeight("100px");
		
		panel = new Panel();
		
		panel.setContent(grid);
		panel.setWidth("305px");
		panel.setHeight("305px");
		
		inputPanel = new Panel();
		
		inputPanel.setContent(inputGrid);
		inputPanel.setWidth("110px");
		inputPanel.setHeight("110px");
		inputGrid.setMargin(false);
		inputGrid.setSpacing(true);
		
		h2Layout.setSpacing(true);
		
		board = new Board();
		
		// connect the tile to the display
		for( int col = 0; col < 9; col++ )
			for( int row = 0; row < 9; row++ )
			{
				Label label = new Label();

				label.setPropertyDataSource(board.getCellElement(col, row));
				label.addValueChangeListener(new CEValueChangeListener());

				label.setWidth(null);
				label.setImmediate(true);
			
				grid.addComponent( label, col, row );
				grid.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
			}
		
		//Making out 1-9 grid for input
		for(int i = 1; i <= 9; i++){
			
			Label inputLabel = new Label();
			inputLabel.setValue(Integer.toString(i));
			
			inputLabel.setWidth(null);
			inputLabel.setImmediate(true);
			
			inputGrid.addComponent(inputLabel);
			inputGrid.setComponentAlignment(inputLabel, Alignment.MIDDLE_CENTER);
			
		}
		
		uploadReceiver = new UploadReceiver(grid, board);
		upload = new Upload(" ", uploadReceiver);
		upload.setButtonCaption("Load Soduko File");
		upload.setImmediate(true);

		vLayout.addComponent( hLayout );
		hLayout.addComponent(upload);
		hLayout.addComponent(solveButton);
		
		
		
		hLayout.setMargin(true);
		hLayout.setSpacing(true);
		hLayout.setComponentAlignment(solveButton, Alignment.BOTTOM_RIGHT);
		
		
		h2Layout.addComponent(panel);
		h2Layout.addComponent(inputPanel);
		
		vLayout.addComponent(h2Layout);
		vLayout.setMargin(true);
		vLayout.setSpacing(true);
		
		setContent(vLayout);

		// upload.setReceiver(uploadReceiver);
		upload.addFinishedListener(uploadReceiver);
		
		/*
		 * Click on the Solve Button
		 */
		solveButton.addClickListener( new Solver( grid, board ));
	}
}