package sweepLine.gui.panel;

import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import sweepLine.algorithm.crossingLines.orthogonalLines.OrthogonalLinesAlgorithm;
import sweepLine.framework.AbstractFrameworkAlgorithm;

public class OrthogonalLinesView implements AlgorithmView {

	Pane drawingPane;
	ListView<String> dataList;
	ListView<String> treeSetList;
	
	public OrthogonalLinesView(Pane drawingPane, ListView<String> dataList, ListView<String> treeSetList) {
		this.drawingPane = drawingPane;
		this.dataList = dataList;
		this.treeSetList = treeSetList;
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Orthogonal Lines";
	}

	@Override
	public int yScale() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public AbstractFrameworkAlgorithm getAlgorithm() {
		// TODO Auto-generated method stub
		return new OrthogonalLinesAlgorithm(drawingPane, dataList, treeSetList);
	}

	@Override
	public Category getCategory() {
		// TODO Auto-generated method stub
		return Category.CrossingLines;
	}

}
