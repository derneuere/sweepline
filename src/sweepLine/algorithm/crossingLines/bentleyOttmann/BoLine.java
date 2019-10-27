package sweepLine.algorithm.crossingLines.bentleyOttmann;

import javafx.scene.shape.Line;
import sweepLine.algorithm.crossingLines.bentleyOttmann.BoEvent.EventType;
import sweepLine.gui.Designer;

public class BoLine extends Line {
	
	public final int id;
	public final BoEvent p1;
	public final BoEvent p2;
	
	// BST sorting number
	public double sortId;
	
	// Steigung
	public final double gradient;
	
	// Nullstelle
	public final double zero;
	
	
	
	public BoLine(int id, double x1, double y1, double x2, double y2) {
		
		// Sort points beforehand. Important for intersection tests
		// super constructors cannot be called late. Thanks, Oracle -.-"
		super(
			x1 < x2 || (x1 == x2 && y1 < y2) ? x1 : x2,
			x1 < x2 || (x1 == x2 && y1 < y2) ? y1 : y2,
			x1 < x2 || (x1 == x2 && y1 < y2) ? x2 : x1,
			x1 < x2 || (x1 == x2 && y1 < y2) ? y2 : y1
		);
		
		this.id = id;
		
		this.p1 = new BoEvent(this, EventType.LEFT, getStartX(), getStartY());
		this.p2 = new BoEvent(this, EventType.RIGHT, getEndX(), getEndY());
		
		// By default, sort using y1
		this.sortId = this.p1.getY();
		
		
		this.gradient = (p1.getY() - p2.getY()) / (p1.getX() - p2.getX());
		this.zero = p1.getX() - (p1.getY() * ((p2.getX()-p1.getX()) / (p2.getY()-p1.getY())));
		
		Designer.design(this);
	}
	
	
	public IntersectionEvent intersects(BoLine o) {
		
		// must not happen
		if(this == o) {
			return null;
		}
		
		
		System.out.println("Comparing Lines " + this.id + " vs " + o.id);
		
		
		
		if(this.gradient == o.gradient) {
			// parallel line segments - 0, 1 or infinite intersections
			
			if(this.zero == o.zero) {
				// Same zero - segments might overlap
				
				// Detect gaps (2d collision detection, normally used for boxes)
				//FIXME there might be a simpler way
				if(p1.getX() > o.p2.getX() || p2.getX() < o.p1.getX() || p1.getY() > o.p2.getY() || p2.getY() < o.p1.getY() ) {
					// no intersections
					return null;
				}
				else if(p1.getX() == o.p2.getX() && p1.getY() == o.p2.getY()) {
					// exactly one intersection
					return new IntersectionEvent(this, o, EventType.INTERSECTION, p1.getX(), p1.getY());	
				}
				else if(p2.getX() == o.p1.getX() && p2.getY() == o.p1.getY()) {
					// exactly one intersection
					return new IntersectionEvent(this, o, EventType.INTERSECTION, p2.getX(), p2.getY());
				}
				else {
					// infinite intersections
					//FIXME error handling
					System.err.println("Infinite intersections found for " + this + " <-> "+o);
				}
			}
			else {
				// parallel, but no intersections
				return null;
			}
		}
		
		
		
		// Calculate intersection point
		double beta = ((p2.getX()-p1.getX())*(o.p1.getY()-p1.getY()) - (o.p1.getX()-p1.getX())*(p2.getY()-p1.getY())) / ((o.p2.getX()-o.p1.getX())*(p2.getY()-p1.getY()) - (p2.getX()-p1.getX())*(o.p2.getY()-o.p1.getY()));
		double alpha = ((o.p1.getX() - p1.getX()) + beta*(o.p2.getX() - o.p1.getX())) / (p2.getX() - p1.getX());
		
		System.out.println("\tbeta: " + beta);
		System.out.println("\talpha: " + alpha);
		
		/*
		if(Double.isNaN(alpha) || Double.isNaN(beta) || alpha < 0 || beta > 1) {
			// These segments do not intersect, even though their lines would.
			return null;
		}
		*/
		
		if(
			Double.isNaN(alpha) || Double.isNaN(beta) || //FIXME this might be wrong: 90° lines produce NaN
			alpha < 0 || alpha > 1 ||
			beta < 0 || beta > 1
		) {
			// These segments do not intersect, even though their lines would.
			return null;
		}
		
		// Intersection
		return new IntersectionEvent(this, o, EventType.INTERSECTION, o.p1.getX() + beta * (o.p2.getX() - o.p1.getX()), o.p1.getY() + beta * (o.p2.getY() - o.p1.getY()));
	}
	
	
	
	public String dump() {
		return "Line(" + id + ", "+p1.dump()+" -> "+p2.dump()+")";
	}
	
}
