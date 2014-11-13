package com.example.soduku;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.DragAndDropWrapper.DragStartMode;
import com.vaadin.ui.Panel;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;

import fi.jasoft.dragdroplayouts.DDGridLayout;
import fi.jasoft.dragdroplayouts.DDGridLayout.GridLayoutTargetDetails;
import fi.jasoft.dragdroplayouts.client.ui.LayoutDragMode;
import fi.jasoft.dragdroplayouts.events.LayoutBoundTransferable;

@SuppressWarnings("serial")
public class PlayBoard extends CustomComponent {
	private static final int BROWS = 9;
	private static final int BCOLUMNS = 9;
	
	private static final int IROWS = 9;
	private static final int ICOLUMNS = 9;
	
	private Board board;
	private int i;
	
	private Panel panel;
	
	public PlayBoard() {
		setCaption("Game Board");
		setSizeFull();
		
		board = new Board();
		panel = new Panel();
	
		
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.setSizeFull();
		Label header = new Label("Welcome to Suduku!");
		hLayout.addComponent(header);
		
		final DDGridLayout boardLayout = new DDGridLayout(BCOLUMNS, BROWS);
		boardLayout.setWidth("400px");
		boardLayout.setHeight("100%");
		
		boardLayout.setComponentHorizontalDropRatio(0);
		boardLayout.setComponentVerticalDropRatio(0);
		
		boardLayout.setDragMode(LayoutDragMode.CLONE);	
		
		
		final DDGridLayout inputLayout = new DDGridLayout(ICOLUMNS, IROWS);
		inputLayout.setWidth("300px");
		inputLayout.setHeight("100%");
		
		inputLayout.setComponentHorizontalDropRatio(0);
		inputLayout.setComponentVerticalDropRatio(0);
		
		inputLayout.setDragMode(LayoutDragMode.CLONE);	
		
		panel.setContent(hLayout);

		
		boardLayout.setDropHandler(new DropHandler() {
			public AcceptCriterion getAcceptCriterion() {
			       return AcceptAll.get();
				}
			
			public void drop(DragAndDropEvent event) {
				GridLayoutTargetDetails details = (GridLayoutTargetDetails) event
                        .getTargetDetails();
                LayoutBoundTransferable transferable = (LayoutBoundTransferable) event
                        .getTransferable();
                
                int column = details.getOverColumn();
                int row = details.getOverRow();
                
                Component ourComponent = transferable.getComponent();
                
                
			}
			
		});
		
		
		for (int row = 0; row < BROWS; row++) {
            for (int col = 0; col < BCOLUMNS; col++) {
                if (row == 0 || row == BROWS-1 || col == 0 || col == BCOLUMNS-1) {
                    Label label = new Label();
                    label.setPropertyDataSource(board.getCellElement(col, row));
                    label.addValueChangeListener(new CEValueChangeListener());
                    boardLayout.addComponent(label, col, row);
                    boardLayout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
                }
            }
        }
		
		
		//Input Grid
		i = 1;
		for (int row = 0; row < IROWS; row++) {
            for (int col = 0; col < IROWS; col++) {
                if (row == 0 || row == IROWS-1 || col == 0 || col == ICOLUMNS-1) {
                    Label inputLabel = new Label();
                    inputLabel.setValue(Integer.toString(i));
                    inputLabel.setWidth(null);
        			inputLabel.setImmediate(true);
                    
        			inputLayout.addComponent(inputLabel);
        			inputLayout.setComponentAlignment(inputLabel, Alignment.MIDDLE_CENTER);
        			
                    i++;
                }
            }
        }
		
		
		hLayout.addComponent(boardLayout);
		hLayout.addComponent(inputLayout);
		hLayout.setExpandRatio(boardLayout, 1);

	}
}
