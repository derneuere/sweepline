package sweepLine.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import sweepLine.framework.AbstractFrameworkAlgorithm;
import sweepLine.gui.panel.AlgorithmView;
import sweepLine.gui.panel.AndrewsAlgorithmView;
import sweepLine.gui.panel.BentleyOttmannView;
import sweepLine.gui.panel.BoxUnionView;
import sweepLine.gui.panel.Category;
import sweepLine.gui.panel.ClosestsPointsView;
import sweepLine.gui.panel.OrthogonalLinesView;

public class Controller {

	private AbstractFrameworkAlgorithm<?, ?> actAlgo;

	public void initialize() {
		initializeAlgorithmTreeView();
		initializeDrawingPane();
		initializeDataList();
		initializeButtons();
	}

	// --------------------------------------------------------------------
	// Algorithm TreeView
	// --------------------------------------------------------------------

	@FXML
	private TreeView<String> algoTreeView;
	
	private TreeItem<String> buildTree(List<AlgorithmView> views) {
		TreeItem<String> root = new TreeItem<String>("Algorithms");
		
		Arrays.asList(Category.values()).forEach(i -> {
			root.getChildren().add(new TreeItem<String>(i.getText()));
		});
		
		views.forEach(i -> {
				TreeItem<String> currentRoot = root;
				if(i.getCategory() != null){
					TreeItem<String> categoryRoot = root.getChildren().stream()
							.filter(f -> f.getValue().equals(i.getCategory().getText()))
							.findFirst().get();
					currentRoot = categoryRoot;
				}
				currentRoot.getChildren().add(new TreeItem<String>(i.getName()));
				
		});
		
		root.setExpanded(true);
		return root;
	}
	
	private void initializeAlgorithmTreeView() {
		
		List<AlgorithmView> views = new ArrayList<>();
		views.add(new BentleyOttmannView(drawingPane, dataList, dataList));
		views.add(new BoxUnionView(drawingPane, dataList, dataList));
		views.add(new ClosestsPointsView(drawingPane, dataList, dataList));
		views.add(new OrthogonalLinesView(drawingPane, dataList, dataList));
		views.add(new AndrewsAlgorithmView(drawingPane, dataList, dataList));
		
		TreeItem<String> root = buildTree(views);
		algoTreeView.setRoot(root);		

		// On selection change
		algoTreeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
			@Override
			public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> old_val, TreeItem<String> selectedItem) {
				
				// Clear gui
				drawingPane.getChildren().clear();
				dataList.getItems().clear();
				btnNext.setDisable(false);
				
				// Set Algorithm name
				String name = "";
				TreeItem<String> parent = selectedItem;
				while (parent != null) {
					name += " - " + parent.getValue();
					parent = parent.getParent();
				}
				dataList.getItems().add(name.substring(3));
				
				
				AlgorithmView selectedView = views.stream().filter(i -> i.getName().equals(selectedItem.getValue())).findFirst().orElse(null);
				// Select algorithm
				if(selectedView != null) {
					actAlgo = selectedView.getAlgorithm();
					drawingPane.setScaleY(selectedView.yScale());
					actAlgo.init();
					btnRandom.setDisable(false);
					btnSolve.setDisable(false);
					btnNext.setDisable(false);
				}
			}
		});
	}

	

	// --------------------------------------------------------------------
	// Drawing Pane
	// --------------------------------------------------------------------

	@FXML
	private Pane drawingPane;

	private void initializeDrawingPane() {
		Rectangle r = new Rectangle(20, 20, 200, 200);
		r.setFill(Color.GREEN);
		drawingPane.getChildren().add(r);
	}

	// --------------------------------------------------------------------
	// Data List
	// --------------------------------------------------------------------

	@FXML
	private ListView<String> dataList;

	private void initializeDataList() {
		// Scroll to bottom on item add
		dataList.getItems().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> c) {
				dataList.scrollTo(c.getList().size() - 1);
			}
		});

	}

	// --------------------------------------------------------------------
	// TreeSet List
	// --------------------------------------------------------------------
	@FXML private ListView<String> treeSetList;

	private void initializeTreeSetList() {
	}

	// --------------------------------------------------------------------
	// Buttons
	// --------------------------------------------------------------------

	@FXML
	private Button btnRandom;
	@FXML
	private Button btnSolve;
	@FXML
	private Button btnReset;
	@FXML
	private Button btnPrev;
	@FXML
	private Button btnNext;

	private void initializeButtons() {

		btnRandom.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				actAlgo.random();
				btnReset.setDisable(true);
				btnPrev.setDisable(true);
				btnNext.setDisable(false);
			}
		});

		btnSolve.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				while(actAlgo.next());
				btnSolve.setDisable(true);
				btnPrev.setDisable(false);
				btnReset.setDisable(false);
			}
		});

		btnReset.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				actAlgo.reset();
				btnReset.setDisable(true);
				btnPrev.setDisable(true);
				btnNext.setDisable(false);
				btnSolve.setDisable(false);
			}
		});

		btnPrev.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (!actAlgo.prev()) {
					btnPrev.setDisable(true);
					btnReset.setDisable(true);
					System.err.println("Disable button");
				} else {
					btnNext.setDisable(false);
					btnSolve.setDisable(false);
				}
			}
		});

		btnNext.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				boolean hadNext = actAlgo.next();
				if(!hadNext) {
					btnNext.setDisable(true);
					btnSolve.setDisable(true);
				}
				btnPrev.setDisable(false);
				btnReset.setDisable(false);
			}
		});

	}

}
