package sweepLine.gui.panel;

import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import sweepLine.algorithm.crossingLines.bentleyOttmann.BentleyOttmann;
import sweepLine.framework.AbstractFrameworkAlgorithm;

public class BentleyOttmannView implements AlgorithmView {

	Pane drawingPane;
	ListView<String> dataList;
	ListView<String> treeSetList;
	
	public BentleyOttmannView(Pane drawingPane, ListView<String> dataList, ListView<String> treeSetList) {
		this.drawingPane = drawingPane;
		this.dataList = dataList;
		this.treeSetList = treeSetList;
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Bentley-Ottmann";
	}

	@Override
	public int yScale() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public AbstractFrameworkAlgorithm getAlgorithm() {
		// TODO Auto-generated method stub
		return new BentleyOttmann(drawingPane, dataList, treeSetList);
	}

	@Override
	public Category getCategory() {
		// TODO Auto-generated method stub
		return Category.CrossingLines;
	}

}
