package sweepLine.gui.panel;

import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import sweepLine.algorithm.areaOfTheUnionOfTwoRectangles.BoxUnion;
import sweepLine.algorithm.closestPoints.ClosestPoints;
import sweepLine.framework.AbstractFrameworkAlgorithm;

public class ClosestsPointsView implements AlgorithmView {
	
	Pane drawingPane;
	ListView<String> dataList;
	ListView<String> treeSetList;
	
	public ClosestsPointsView(Pane drawingPane, ListView<String> dataList, ListView<String> treeSetList) {
		this.drawingPane = drawingPane;
		this.dataList = dataList;
		this.treeSetList = treeSetList;
	}

	@Override
	public String getName() {
		return "Closest's Points";
	}

	@Override
	public AbstractFrameworkAlgorithm getAlgorithm() {
		return new ClosestPoints(drawingPane, dataList, treeSetList);
	}	

	@Override
	public int yScale() {
		return 1;
	}

	@Override
	public Category getCategory() {
		return null;
	}

}
