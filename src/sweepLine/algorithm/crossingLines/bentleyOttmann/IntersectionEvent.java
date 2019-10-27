package sweepLine.algorithm.crossingLines.bentleyOttmann;

public class IntersectionEvent extends BoEvent {
	
	public final BoLine lineSegmentB;
	
	public IntersectionEvent(BoLine lineSegment, BoLine lineSegmentB, EventType type, double x, double y) {
		super(lineSegment, type, x, y);
		this.lineSegmentB = lineSegmentB;
	}
	
	@Override
	public String dump() {
		return "IntersectionEvent: Line " + this.lineSegment.id + " intersects Line " + lineSegmentB.id + " at (" + getX()+", "+getY()+")";
	}
	
}
