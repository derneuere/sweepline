package sweepLine.algorithm.convexHull;

import javafx.scene.shape.Line;
import sweepLine.framework.AbstractFrameworkEvent;

public class ChEvent extends AbstractFrameworkEvent {

	// hull line representation
	public Line chLineUpper;
	public Line chLineLower;

	public ChEvent(double x, double y) {
		super(x, y);
	}
}
