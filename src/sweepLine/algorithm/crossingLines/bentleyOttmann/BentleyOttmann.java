package sweepLine.algorithm.crossingLines.bentleyOttmann;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import sweepLine.framework.AbstractFrameworkAlgorithm;

//TODO comment: Bentley-Ottmann Algorithm [ source: http://geomalgorithms.com/a09-_intersect-3.html ]
//FIXME remove german comments
public class BentleyOttmann extends AbstractFrameworkAlgorithm<BoEvent, BoLine> {

	private int nextLineId = 1;
	BoLine[] lines = {
			new BoLine(nextLineId++, 50, 150, 350, 300),
			new BoLine(nextLineId++, 100, 450, 450, 150),
			new BoLine(nextLineId++, 125, 250, 400, 100),
			new BoLine(nextLineId++, 250, 50, 425, 250),
			new BoLine(nextLineId++, 325, 400, 470, 400),
			new BoLine(nextLineId++, 375, 350, 375, 450)
	};
	
	// Initialize event queue EQ = all segment endpoints;
	// -> done while initialization
	
	// Sort EQ by increasing x and y;
	// -> done while adding events
	
	
	// Initialize sweep line SL to be empty;
	// -> done
	
	// Initialize output intersection list IL to be empty;
	ArrayList<IntersectionEvent> intersectionList = new ArrayList<>();
	
	// Unique intersection points
	HashSet<String> uniqueIntersectionSet = new HashSet<>();
	
	public BentleyOttmann(Pane pane, ListView<String> output, ListView<String> treeSetView) {
		// initialize superclass with two comparators of which the first is for the prio queue and the latter for the binary search tree (sweep status structure))
		super(pane, output, treeSetView,
			new Comparator<BoEvent>() {
			@Override
			public int compare(BoEvent o1, BoEvent o2) {
				if(o1.getX() == o2.getX()) {
					return o1.getY() <= o2.getY() ? -1 : 1;
				}
				else if(o1.getX() < o2.getX()) {
					return -1;
				}
				return 1;
			}
		}, new Comparator<BoLine>() {
			@Override
			public int compare(BoLine o1, BoLine o2) {
				if(o1.sortId == o2.sortId) {
					return 0;
				}
				return o1.sortId < o2.sortId ? -1 : 1;
			}
		});
	}

	@Override
	public void computeNext(BoEvent actEvent) {
		
		// Variable initialization
		BoLine eventLine;
		IntersectionEvent intersection;
		BoLine above, below;
		
		// While (EQ is nonempty) {
		// Let E = the next event from EQ;
		switch (actEvent.type) {
		case LEFT:
			// event is a left endpoint

			// Let segE = E's segment;
			eventLine = actEvent.lineSegment;

			// Add segE to SL;
			// --> add left event, sorted by y
			addSssObject(actEvent.lineSegment);

			// Let segA = the segment Above segE in SL;
			// Let segB = the segment Below segE in SL;
			above = getSssHigher(actEvent.lineSegment);
			below = getSssLower(actEvent.lineSegment);

			// uniquely check intersection with nearest lines
			if (above != null && isFirstCheck(eventLine, above)
					&& (intersection = eventLine.intersects(above)) != null) {
				// insert intersection event into the event queue
				addEvent(intersection);
				addMsg("	LFT->above->addEvent: " + intersection.dump());
			}
			if (below != null && isFirstCheck(eventLine, below)
					&& (intersection = eventLine.intersects(below)) != null) {
				// insert intersection event into the event queue
				addEvent(intersection);
				addMsg("	LFT->below->addEvent: " + intersection.dump());
			}
			break;
		case RIGHT:

			// event is a right endpoint

			// Let segE = E's segment;
			eventLine = actEvent.lineSegment;

			// Let segA = the segment Above segE in SL;
			// Let segB = the segment Below segE in SL;
			// remember siblings
			above = getSssHigher(actEvent.lineSegment);
			below = getSssLower(actEvent.lineSegment);

			// Delete segE from SL;
			// remove processed line - bye bye
			removeSssObject(actEvent.lineSegment);

			// uniquely test intersection of new direct siblings: above vs below
			if (below != null && above != null && isFirstCheck(below, above)
					&& (intersection = below.intersects(above)) != null) {
				// insert intersection event into the event queue
				addEvent(intersection);
				addMsg("	RGT->addEvent: " + intersection.dump());
			}

			break;
		case INTERSECTION:
			// event is an intersection event

			// FIXME error handling
			if (!(actEvent instanceof IntersectionEvent)) {
				throw new RuntimeException("Intersection event of a wrong type: " + actEvent);
			}
			IntersectionEvent ie = (IntersectionEvent) actEvent;

			// Add E’s intersect point to the output list IL;
			// FIXME intersection events must store both corresponding lines
			intersectionList.add(ie);

			// Let segE1 above segE2 be E's intersecting segments in SL;
			// Swap their positions so that segE2 is now above segE1;
			// -> Remove items from BST, swap sortIds and finally readd them
			removeSssObject(ie.lineSegment);
			removeSssObject(ie.lineSegmentB);
			double tmp = ie.lineSegment.sortId;
			ie.lineSegment.sortId = ie.lineSegmentB.sortId;
			ie.lineSegmentB.sortId = tmp;
			addSssObject(ie.lineSegment);
			addSssObject(ie.lineSegmentB);

			// -> determine who's above - IntersectionLine's lines aren't sorted
			// FIXME is this correct?
			BoLine segE1, segE2;
			if (ie.lineSegment.p2.getY() > ie.lineSegmentB.p2.getY()) {
				segE2 = ie.lineSegment;
				segE1 = ie.lineSegmentB;
			} else {
				segE2 = ie.lineSegmentB;
				segE1 = ie.lineSegment;
			}

			// -> check line intersections between the new direct siblings

			// Let segA = the segment above segE2 in SL;
			above = getSssHigher(segE2);
			// If (I = Intersect(segE2 with segA) exists)
			// If (I is not in EQ already)
			// Insert I into EQ;
			if (above != null && isFirstCheck(segE2, above) && (intersection = segE2.intersects(above)) != null) {
				// insert intersection event into the event queue
				addEvent(intersection);
				addMsg("	INT->segE2/above->addEvent: " + intersection.dump());
			}

			// Let segB = the segment below segE1 in SL;
			below = getSssLower(segE1);
			// If (I = Intersect(segE1 with segB) exists)
			// If (I is not in EQ already)
			// Insert I into EQ;
			if (below != null && isFirstCheck(segE1, below) && (intersection = segE1.intersects(below)) != null) {
				// insert intersection event into the event queue
				addEvent(intersection);
				addMsg("	INT->segE1/below->addEvent: " + intersection.dump());
			}
			break;
		}
	}
	
	protected boolean isFirstCheck(BoLine a, BoLine b) {
		String checkId = (a.id < b.id ? a.id+":"+b.id : b.id+":"+a.id);
		if(this.uniqueIntersectionSet.contains(checkId)) {
			return false;
		}
		uniqueIntersectionSet.add(checkId);
		return true;
	}

	@Override
	protected void end() {
		// we are done, print results
		// return IL;
		// show intersections
		StringBuilder sb = new StringBuilder();
		sb.append("Results (all intersections):");
		for (IntersectionEvent ie : intersectionList) {
			sb.append("\n\t" + ie.dump());
		}
		addMsg(sb.toString());
	}

	@Override
	protected void draw() {
		for (BoLine line:lines) {
			addShape(line);
			// draw text for line
			Text text = new Text(line.getStartX() - 30, line.getStartY() + 4, "L" + (line.id));
			text.setStroke(Color.BLACK);
			addShape(text);
		}
	}

	@Override
	protected ArrayList<BoEvent> getInitialEvents() {
		ArrayList<BoEvent> events = new ArrayList<>();
		for (BoLine line:lines) {
			events.add(line.p1);
			events.add(line.p2);
		}
		return events;
	}

	@Override
	protected ArrayList<BoEvent> getRandomEvents() {
		int count = 7;
		ArrayList<BoEvent> events = new ArrayList<>();
		lines = new BoLine[count];
		double border = 50;
		double pw = getPaneWidth() - 2 * border;
		double ph = getPaneHeight() - 2 * border;
		
		// limit height a bit
		if(pw < ph) {
			ph = pw;
		}
		
		for(int i=0; i<7; i++) {
			lines[i] = new BoLine(
				i,
				Math.random() * pw + border,
				Math.random() * ph + border,
				Math.random() * pw + border,
				Math.random() * ph + border
			);
		}
		
		for (BoLine line:lines) {
			events.add(line.p1);
			events.add(line.p2);
		}
		return events;
	}

	@Override
	protected void clear() {
		nextLineId = 0;
		intersectionList = null;
		uniqueIntersectionSet = null;
	}

}
