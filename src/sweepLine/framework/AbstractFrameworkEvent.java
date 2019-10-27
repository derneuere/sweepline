package sweepLine.framework;

import javafx.scene.shape.Circle;
import sweepLine.library.EventInterface;

public abstract class AbstractFrameworkEvent extends Circle implements EventInterface{
	
	protected AbstractFrameworkEvent(double x, double y) {
		super(x, y, 5);
	}
	
	public double getX() {
		return getCenterX();
	}
	public double getY() {
		return getCenterY();
	}
}
