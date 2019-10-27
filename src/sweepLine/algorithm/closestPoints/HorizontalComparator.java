package sweepLine.algorithm.closestPoints;

import java.util.Comparator;

public class HorizontalComparator implements Comparator<PointEvent>{

	@Override
	public int compare(PointEvent a, PointEvent b) {
        if (a.getX() < b.getX()) {
            return -1;
        }
        if (a.getX() > b.getX()) {
            return 1;
        }
        if (a.getY() < b.getY()) {
            return -1;
        }
        if (a.getY() > b.getY()) {
            return 1;
        }
        return 0;
    }
	
}