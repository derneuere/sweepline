package sweepLine.algorithm.areaOfTheUnionOfTwoRectangles;

import sweepLine.framework.AbstractFrameworkEvent;

public class BoxUnionEvent extends AbstractFrameworkEvent {
	 
	public enum EventType {
		LOWERLEFT, UPPERRIGHT
	}
	public final EventType type;
	public BoxRectangle rect;

	public BoxUnionEvent(BoxRectangle rect, EventType type, double x, double y) {
		super(x,y);
		this.rect = rect;
		this.type = type;
	}
}
