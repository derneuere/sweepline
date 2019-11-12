package sweepLine.gui.panel;

import sweepLine.framework.AbstractFrameworkAlgorithm;

public interface AlgorithmView {
		
	public String getName();
	
	public int yScale();
	
	public AbstractFrameworkAlgorithm  getAlgorithm();
	
	public Category getCategory();
}
