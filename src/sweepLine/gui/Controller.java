package sweepLine.gui;

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
import sweepLine.algorithm.areaOfTheUnionOfTwoRectangles.BoxUnion;
import sweepLine.algorithm.closestPoints.ClosestPoints;
import sweepLine.algorithm.convexHull.AndrewsConvexHull;
import sweepLine.algorithm.crossingLines.bentleyOttmann.BentleyOttmann;
import sweepLine.algorithm.crossingLines.orthogonalLines.OrthogonalLinesAlgorithm;
import sweepLine.framework.AbstractFrameworkAlgorithm;

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

	private void initializeAlgorithmTreeView() {
		// FIXME reflection: get all algorithms
		TreeItem<String> root = new TreeItem<String>("Algorithm");

		algoTreeView.setRoot(root);

		TreeItem<String> algo1 = new TreeItem<String>("Crossing lines");
		root.getChildren().add(algo1);
		algo1.getChildren().add(new TreeItem<String>("Orthogonal lines"));
		algo1.getChildren().add(new TreeItem<String>("Bentley-Ottmann"));
		algo1.setExpanded(true);

		TreeItem<String> algo2 = new TreeItem<String>("Closest Points");
		root.getChildren().add(algo2);
		// ...
		algo2.setExpanded(true);

		TreeItem<String> algo3 = new TreeItem<String>("Rectange intersection");
		root.getChildren().add(algo3);
		// ...
		algo3.setExpanded(true);

		TreeItem<String> algo4 = new TreeItem<String>("Andrew's algorithm");
		root.getChildren().add(algo4);

		root.setExpanded(true);

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
				
				

				// Select algorithm
				switch (selectedItem.getValue()) {
				case "Bentley-Ottmann":
					BentleyOttmann algo = new BentleyOttmann(drawingPane, dataList, treeSetList);
					drawingPane.setScaleY(1);
					actAlgo = algo;
					break;
				case "Orthogonal lines":
					OrthogonalLinesAlgorithm a = new OrthogonalLinesAlgorithm(drawingPane, dataList, treeSetList);
					drawingPane.setScaleY(1);
					actAlgo = a;
					break;
				case "Closest Points":
					ClosestPoints alg = new ClosestPoints(drawingPane, dataList, treeSetList);
					drawingPane.setScaleY(-1);
					actAlgo = alg;
					break;
				case "Andrew's algorithm":
					AndrewsConvexHull algor = new AndrewsConvexHull(drawingPane, dataList, treeSetList);
					drawingPane.setScaleY(1);
					actAlgo = algor;
					break;
				case "Rectange intersection":
					BoxUnion al = new BoxUnion(drawingPane, dataList, treeSetList);
					drawingPane.setScaleY(-1);
					actAlgo = al;
					break;
				default:
					// FIXME handle
					dataList.getItems().add("Not yet implemented");
				}
				actAlgo.init();
				btnRandom.setDisable(false);
				btnSolve.setDisable(false);
				btnNext.setDisable(false);

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
