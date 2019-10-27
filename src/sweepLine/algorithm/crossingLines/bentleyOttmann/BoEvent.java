package sweepLine.algorithm.crossingLines.bentleyOttmann;


import javafx.scene.paint.Color;
import sweepLine.framework.AbstractFrameworkEvent;
import sweepLine.gui.Designer;


public class BoEvent extends AbstractFrameworkEvent {
	
	public enum EventType {
		LEFT, RIGHT, INTERSECTION
	}
	
	public final BoLine lineSegment;
	public final EventType type;
	
	public BoEvent(BoLine lineSegment, EventType type, double x, double y) {
		super(x, y);
		this.lineSegment = lineSegment;
		this.type = type;
		
		Designer.design(this);
	}

	public String dump() {
		return "(" + this.getX() + ", " + this.getY() + ")";
	}

	
	public void setHighlight(boolean set) {
		if(set) {
			this.setStroke(Color.RED);
		}
		else {
			switch(type) {
			case INTERSECTION:
				this.setStroke(Color.RED);
				break;
			case LEFT:
				break;
			case RIGHT:
				break;
			default:
				break;
			
			}
		}
	}
	
}
