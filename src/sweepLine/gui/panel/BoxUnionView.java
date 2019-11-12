package sweepLine.gui.panel;

import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import sweepLine.algorithm.areaOfTheUnionOfTwoRectangles.BoxUnion;
import sweepLine.framework.AbstractFrameworkAlgorithm;

public class BoxUnionView implements AlgorithmView {
	
	Pane drawingPane;
	ListView<String> dataList;
	ListView<String> treeSetList;
	
	public BoxUnionView(Pane drawingPane, ListView<String> dataList, ListView<String> treeSetList) {
		this.drawingPane = drawingPane;
		this.dataList = dataList;
		this.treeSetList = treeSetList;
	}

	@Override
	public String getName() {
		return "Rectange intersection";
	}

	@Override
	public AbstractFrameworkAlgorithm getAlgorithm() {
		return new BoxUnion(drawingPane, dataList, treeSetList);
	}	

	@Override
	public int yScale() {
		return -1;
	}

	@Override
	public Category getCategory() {
		// TODO Auto-generated method stub
		return null;
	}

}
