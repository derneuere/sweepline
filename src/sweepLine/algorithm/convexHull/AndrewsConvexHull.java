package sweepLine.algorithm.convexHull;

import java.util.ArrayList;
import java.util.Comparator;

import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import sweepLine.framework.AbstractFrameworkAlgorithm;
import sweepLine.gui.Designer;

public class AndrewsConvexHull extends AbstractFrameworkAlgorithm <ChEvent, ChEvent> {
	
	// resulting convex hull (counterclockwise)
	private ChEvent[] upperHull;
	private ChEvent[] lowerHull;
	
	private int uk = 0;
	private int lk = 0;
	
	public AndrewsConvexHull(Pane pane, ListView<String> output, ListView<String> treeSetView) {
		// initialize superclass with two comparators of which the first is for the prio queue and the latter for the binary search tree (sweep status structure))
		// This is O(n log n) for sorting events by coordinates
		
		super(pane, output, treeSetView,
				new Comparator<ChEvent>() {
					@Override
					public int compare(ChEvent o1, ChEvent o2) {
						if (o1.getX() == o2.getX()) {
							return o1.getY() <= o2.getY() ? -1 : 1;
						} else if (o1.getX() < o2.getX()) {
							return -1;
						}
						return 1;
					}
				},
				null
		);
	}
	
	@Override
	public void computeNext(ChEvent event) {
		
		// initialize resulting arrays once
		if(upperHull == null) {
			upperHull = new ChEvent[numberOfEvents()];
			lowerHull = new ChEvent[numberOfEvents()];
		}
		// determine lower hull
		// visually, this is the "upper" hull because the coords 0,0 tend to be in the upper left corner in computer graphics ;)

		while (uk >= 2 && getEventPosition(upperHull[uk-2], upperHull[uk-1], event) <= 0) {
			// remove lines that aren't part of the convex hull (and change their line's style)
			uk--;
			Designer.designConvexHullLineInactiveUpper(upperHull[uk].chLineUpper);
		}

		// add actual event as part of the convex hull (and display it)
		if(uk >= 1) {
			event.chLineUpper = new Line(upperHull[uk-1].getCenterX(), upperHull[uk-1].getCenterY(), event.getCenterX(), event.getCenterY());
			Designer.designConvexHullLineActiveUpper(event.chLineUpper);
			addShape(event.chLineUpper);
		}
		upperHull[uk++] = event;

		// determine upper hull
		// skip events that are not part of the convex hull 
		while (lk >=2 && getEventPosition(lowerHull[lk-2], lowerHull[lk-1], event) >= 0) {
			// remove lines that aren't part of the convex hull (and change their line's style)
			lk--;
			Designer.designConvexHullLineInactiveLower(lowerHull[lk].chLineLower);
		}

		// add actual event as part of the new convex hull (and display it)
		if(lk >= 1) {
			event.chLineLower = new Line(lowerHull[lk-1].getCenterX(), lowerHull[lk-1].getCenterY(), event.getCenterX(), event.getCenterY());
			Designer.designConvexHullLineActiveLower(event.chLineLower);
			addShape(event.chLineLower);
		}
		lowerHull[lk++] = event;
	}
	
	/**
	 * Checks whether the variable event is part of the convex hull
	 * @param a 2nd last element of the current convex hull
	 * @param b last element of the current convex hull
	 * @param event to test
	 * @return returns a positive value if b is on the left of o->a,
	 * 		   a negative value if b is on the right of o->a and 0 if it's on the line segment.
	 */
	protected double getEventPosition(ChEvent a, ChEvent b, ChEvent event) {
		return (b.getX() - a.getX()) * (event.getY() - a.getY()) - (b.getY() - a.getY()) * (event.getX() - a.getX());
	}

	@Override
	public ArrayList<ChEvent> getRandomEvents() {
		ArrayList<ChEvent> events = new ArrayList<>();
		double border = 50;
		double pw = getPaneWidth() - 2 * border;
		double ph = getPaneHeight() - 2 * border;
		
		// limit ratio to 2:1 (1:2)
		if(pw > 2*ph) {
			pw = 2*ph;
		}
		else if(ph > 2*pw) {
			ph = 2*pw;
		}

		// limit height a bit
		if(pw < ph) {
			ph = pw;
		}

		for(int i=0; i<12; i++) {
			events.add(new ChEvent(
				Math.random() * pw + border,
				Math.random() * ph + border
			));
		}
		return events;
	}

	@Override
	protected void draw() {
		// not used
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected ArrayList<ChEvent> getInitialEvents() {
		ArrayList<ChEvent> events = new ArrayList<>();
		events.add(new ChEvent(50, 150));
		events.add(new ChEvent(350, 300));
		events.add(new ChEvent(100, 450));
		events.add(new ChEvent(450, 150));
		events.add(new ChEvent(125, 250));
		events.add(new ChEvent(400, 100));
		events.add(new ChEvent(250,  50));
		events.add(new ChEvent(425, 250));
		events.add(new ChEvent(325, 400));
		events.add(new ChEvent(470, 400));
		events.add(new ChEvent(375, 350));
		events.add(new ChEvent(375, 450));

		return events;
	}

	@Override
	protected void clear() {
		lowerHull = null;
		upperHull = null;
		lk = 0;
		uk = 0;
	}
}
