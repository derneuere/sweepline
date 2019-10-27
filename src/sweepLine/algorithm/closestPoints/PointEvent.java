package sweepLine.algorithm.closestPoints;

import sweepLine.framework.AbstractFrameworkEvent;

public class PointEvent extends AbstractFrameworkEvent{
	
	protected PointEvent(double x, double y) {
		super(x, y);
	}

	public double distance(PointEvent p){	
		return(Math.sqrt(Math.pow(p.getX()-this.getX(), 2)+Math.pow(p.getY()-this.getY(), 2)));
	}
}
