package sweepLine.gui.panel;

import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import sweepLine.algorithm.convexHull.AndrewsConvexHull;
import sweepLine.algorithm.crossingLines.orthogonalLines.OrthogonalLinesAlgorithm;
import sweepLine.framework.AbstractFrameworkAlgorithm;

public class AndrewsAlgorithmView implements AlgorithmView {

	Pane drawingPane;
	ListView<String> dataList;
	ListView<String> treeSetList;
	
	public AndrewsAlgorithmView(Pane drawingPane, ListView<String> dataList, ListView<String> treeSetList) {
		this.drawingPane = drawingPane;
		this.dataList = dataList;
		this.treeSetList = treeSetList;
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Andrew's Algorithm";
	}

	@Override
	public int yScale() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public AbstractFrameworkAlgorithm getAlgorithm() {
		return new AndrewsConvexHull(drawingPane, dataList, treeSetList);
	}

	@Override
	public Category getCategory() {
		return null;
	}

}
