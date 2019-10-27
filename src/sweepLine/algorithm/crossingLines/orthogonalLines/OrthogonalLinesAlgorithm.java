package sweepLine.algorithm.crossingLines.orthogonalLines;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.Random;

import java.awt.Point;

import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import sweepLine.algorithm.crossingLines.orthogonalLines.OrthogonalLinesEvent.Type;
import sweepLine.framework.AbstractFrameworkAlgorithm;

public class OrthogonalLinesAlgorithm extends AbstractFrameworkAlgorithm<OrthogonalLinesEvent,Double>{
	Point[][] lines;
	ArrayList<String> intersectionMessages;

	public OrthogonalLinesAlgorithm(Pane pane, ListView<String> lv, ListView<String> treeSetView) {
		super(pane, lv, treeSetView,
				new Comparator<OrthogonalLinesEvent>() {
					@Override
					public int compare(OrthogonalLinesEvent e1, OrthogonalLinesEvent e2) {
						if (e1.getX() < e2.getX())
							return -1;
						if (e1.getX() > e2.getX())
							return 1;
						// equal
						if (e1.getY() < e2.getY())
							return -1;
						if (e1.getY() > e2.getY())
							return 1;
						return 0;
					}
				}, new Comparator<Double>() {
					@Override
					public int compare(Double d1, Double d2) {
						if (d1 < d2)
							return -1;
						if (d1 > d2)
							return 1;
						return 0;
					}
				});
		intersectionMessages = new ArrayList<>();
	}
	
	@Override
	public void computeNext(OrthogonalLinesEvent event) {

		setMsg(event.toString());

		switch (event.t) {
		case HorizontalStart:
			addMsg("Add it to the tree.\n\n");
			addSssObject(event.getY());			
			break;
		case HorizontalEnd:
			removeSssObject(event.getY());
			addMsg("Remove it from the tree.\n\n");
			break;
		case VerticalStart:
			addMsg("Save the y coordinate.\n\n");
			OrthogonalLinesEvent.start_y = event.getY();
			break;
		case VerticalEnd:
			String out = "";
			addMsg("Do a range search between the saved y coordinate (" +
					OrthogonalLinesEvent.start_y + ") and the y coordinate of this point ("
					+ event.getY() + ").\n\n");
			SortedSet<Double> ys;
			
			ys = getBetween(OrthogonalLinesEvent.start_y, true, event.getY(), true);
			
			for (Double y : ys) {
				out += "Intersection at: " + event.getX() + " | " + y + "\n";
				// insersectionMessages is printed, when the algorithm has finished.
				intersectionMessages.add(event.getX() + " | " + y);
				addShape((new Circle(event.getX(), y, 5.0, Color.GREEN)));
			}
			addMsg(out + "\n");
			break;
		}
	}
	
	@Override
	public ArrayList<OrthogonalLinesEvent> getInitialEvents() {
		Point[][] points = {
			{ new Point(50, 300), new Point(300, 300) },
			{ new Point(60, 220), new Point(270, 220) },
			{ new Point(100, 100), new Point(100, 320) },
			{ new Point(200, 150), new Point(200, 250) },
			{ new Point(250, 250), new Point(250, 400) },
			{ new Point(150, 350), new Point(350, 350) },
		};
		lines = points;
		return makeEvents();
	}

	private ArrayList<OrthogonalLinesEvent> makeEvents() {
		ArrayList<OrthogonalLinesEvent> events = new ArrayList<>();

		for (Point[] p : lines) {
			if (p[0].x == p[1].x) {
				events.add(new OrthogonalLinesEvent(
						Type.VerticalStart, p[0].x, p[0].y));
				events.add(new OrthogonalLinesEvent(
						Type.VerticalEnd, p[1].x, p[1].y));
			} else if (p[0].y == p[1].y) {
				 events.add(new OrthogonalLinesEvent(
						Type.HorizontalStart, p[0].x, p[0].y));
				events.add(new OrthogonalLinesEvent(
						Type.HorizontalEnd, p[1].x, p[1].y));
			} else {
				System.out.println("Error: This algorithm only handles "
						+ "horizontal and vertical lines.");
			}
		}
		return events;
	}

	@Override
	public ArrayList<OrthogonalLinesEvent> getRandomEvents() {
		int count = 7;
		Point[][] points = new Point[count][2];
		Random rand = new Random();

		for (int i = 0; i < count; i++) {
			int r = rand.nextInt(2);

			if (r == 0) {
				// Horizontal
				int x1 = rand.nextInt((int)getPaneWidth());
				int x2 = rand.nextInt((int)getPaneWidth());
				int y = rand.nextInt((int)getPaneHeight());

				if (x1 < x2) {
					points[i][0] = new Point(x1, y);
					points[i][1] = new Point(x2, y);
				} else if (x2 < x1) {
					points[i][0] = new Point(x2, y);
					points[i][1] = new Point(x1, y);
				} else {
					// Both x coordinates are the same. Try again.
					continue;
				}
			} else {
				// Vertical
				int y1 = rand.nextInt((int)getPaneHeight());
				int y2 = rand.nextInt((int)getPaneHeight());
				int x = rand.nextInt((int)getPaneWidth());

				if (y1 < y2) {
					points[i][0] = new Point(x, y1);
					points[i][1] = new Point(x, y2);
				} else if (y2 < y1) {
					points[i][0] = new Point(x, y2);
					points[i][1] = new Point(x, y1);
				} else {
					// Both y coordinates are the same. Try again.
					continue;
				}
			}
		}

		lines = points;
		return makeEvents();
	}
	
	@Override
	public void draw() {
		for (Point[] p : lines) {
			Line l = new Line(p[0].x, p[0].y, p[1].x, p[1].y);
			addShape(l);
		}
	}

	@Override
	public void end() {
		setMsg("Intersections at:");
		for(String s:intersectionMessages) {
			addMsg(s);
		}
	}

	@Override
	protected void clear() {
		intersectionMessages = null;
	}
}
